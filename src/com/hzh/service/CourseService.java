package com.hzh.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.hzh.bean.CourseBean;
import com.hzh.exception.TermNoExitsExcpetinon;

public class CourseService {

	public static final String[] WEEK_DAY = { "一", "二", "三", "四", "五", "六", "日" };
	public static final String[] TERM_NAME = { "一", "二" };

	private DBHelper dbHelper;

	public CourseService(Context context) {
		dbHelper = new DBHelper(context);
	}

	/**
	 * 保存html课表到数据库
	 * 
	 * @param stuID学生号
	 * @param html网页
	 * @return courseBaan集合
	 * @throws TermNoExitsExcpetinon 
	 * @throws Exception 
	 */
	public Map<String, List<CourseBean>> saveTerm(String stuID, String term,
			String html) throws TermNoExitsExcpetinon {
		Map<String, List<CourseBean>> courses = html2courses(html, stuID, term);
		
		if(courses == null) throw new TermNoExitsExcpetinon();

		SQLiteDatabase db = dbHelper.getWritableDatabase();
		db.beginTransaction();
		
		Cursor cursor = db.rawQuery("select * from user where ower_id=? AND term_id=?", new String[] {stuID, term});
		while(cursor.moveToNext()) {
			int id = cursor.getInt(cursor.getColumnIndex("_id"));
			this.deleteTerm(id, db);
			Log.i(MainService.TAG, "Delete!!");
		}
		cursor.close();
		
		ContentValues values = new ContentValues();
		values.put("ower_id", stuID);
		values.put("term_id ", term);

		StringBuilder termName = new StringBuilder();
		termName.append(term.subSequence(0, 4));
		termName.append("-");
		termName.append(Integer.parseInt(term.subSequence(0, 4).toString()) + 1);
		termName.append("学年第" + TERM_NAME[term.charAt(4) - '0'] + "学期");

		values.put("term_name", termName.toString());


		db.insert("user", "ower_id", values);
		for (int i = 0; i < 7; i++) {
			for (CourseBean c : courses.get(String.valueOf(i))) {
				save(c, db);
			}
		}
		db.setTransactionSuccessful();
		db.endTransaction();
		db.close();
		return courses;
	}


	/**
	 * 转换html到CourseBean集合
	 * @param term 
	 * @param stuID 
	 * 
	 * @param html网页
	 * @return
	 */
	public Map<String, List<CourseBean>> html2courses(String html, String stuID, String term) {
		Document doc = Jsoup.parse(html);
		Element es;
		try {
			es = doc.select("table").get(3);
		} catch (Exception e1) {
			return null;
		}
		
		StringBuilder[] classes = new StringBuilder[7];
		for (int i = 0; i < classes.length; i++) {
			classes[i] = new StringBuilder();
		}

		for (Element e : es.select("td")) {
			String text = e.text();
			if (!"".equals(text.trim())) {
				if (text.contains("]一[")) {
					classes[0]
							.append(text.replaceAll("\\[\\d{6}\\]", "") + " ");

				} else if (text.contains("]二[")) {
					classes[1]
							.append(text.replaceAll("\\[\\d{6}\\]", "") + " ");

				} else if (text.contains("]三[")) {
					classes[2]
							.append(text.replaceAll("\\[\\d{6}\\]", "") + " ");

				} else if (text.contains("]四[")) {
					classes[3]
							.append(text.replaceAll("\\[\\d{6}\\]", "") + " ");

				} else if (text.contains("]五[")) {
					classes[4]
							.append(text.replaceAll("\\[\\d{6}\\]", "") + " ");

				} else if (text.contains("]六[")) {
					classes[5]
							.append(text.replaceAll("\\[\\d{6}\\]", "") + " ");

				} else if (text.contains("]日[")) {
					classes[6]
							.append(text.replaceAll("\\[\\d{6}\\]", "") + " ");
				}
			}
		}

		Map<String, List<CourseBean>> courses = new HashMap<String, List<CourseBean>>();

		for (int i = 0; i < 7; i++) {
			List<CourseBean> coursesList = courseToList(classes[i].toString(), i, stuID, term);
			courses.put(i + "", coursesList);
		}

		Log.i(MainService.TAG, "ok!");
		Log.i(MainService.TAG, courses.toString());
		return courses;

	}

	/**
	 * 格式化html数据到courseBean
	 * 
	 * @param courses 某天的课程
	 * @param day某一天
	 * @return 某一天课程Bean的集合
	 */
	private List<CourseBean> courseToList(String courses, int day, String stuID, String term) {
		List<CourseBean> coursesList = new ArrayList<CourseBean>();
		if (courses.length() == 0)
			return coursesList;
		String[] coursesArray = courses.split(" ");
		int dayNo = 0;
		for (int i = 0; i < coursesArray.length; i += 4) {
			CourseBean course = new CourseBean();
			course.setClassName(coursesArray[i]);
			course.setTeacherName(coursesArray[i + 1]);
			String weeks = coursesArray[i + 2].substring(0, coursesArray[i + 2].indexOf(WEEK_DAY[day]));
			String numofclass = coursesArray[i + 2].substring(coursesArray[i + 2].indexOf(WEEK_DAY[day]) + 1, coursesArray[i + 2].length());
			course.setWeeks(weeks);
			course.setClasses(numofclass);
			course.setClassroom(coursesArray[i + 3]);
			course.setDayNo(String.valueOf(dayNo));
			course.setDayWeek(String.valueOf(day));
			course.setOwerId(stuID);
			course.setTermId(term);
			coursesList.add(course);
			dayNo++;
		}
		return coursesList;
	}

	/**
	 * 保存一个bean到数据库,转换中间
	 * 
	 * @param course存入bean
	 * @param db 数据库
	 */
	private void save(CourseBean course, SQLiteDatabase db) {
		
		ContentValues values = new ContentValues();
		
		values.put("term_id", course.getTermId());
		values.put("course_name", course.getClassName());
		values.put("course_teacher", course.getTeacherName());
		values.put("course_dayWeek", course.getDayWeek());
		values.put("course_week", course.getWeeks());
		values.put("course_classes", course.getClasses());
		values.put("course_classroom", course.getClassroom());
		values.put("course_dayNo", course.getDayNo());
		values.put("ower_id", course.getOwerId());
		
		db.insert("course", "course_name", values);
	}
	
	/**
	 * 保存一个bean到数据库（中间在上面）
	 * 
	 * @param course存入bean
	 * @param db 数据库
	 */
	public void save(CourseBean course) {
		SQLiteDatabase db = dbHelper.getWritableDatabase();
		save(course, db);
		db.close();
	}
	
	public void delete(int id) {
		SQLiteDatabase db = dbHelper.getWritableDatabase();
		db.delete("course", "_id=" + id, null);
		db.close();
	}
	
	
	public void deleteTermById(int id) {
		SQLiteDatabase db = dbHelper.getWritableDatabase();
		db.beginTransaction();
		deleteTerm(id, db);
		db.setTransactionSuccessful();
		db.endTransaction();
		db.close();		
	}

	private void deleteTerm(int id, SQLiteDatabase db) {
		Cursor cursor = db.rawQuery("select * from user where _id=" + id, null);
		if(cursor.moveToNext()) {
			String owerId = cursor.getString(cursor.getColumnIndex("ower_id"));
			String termId =  cursor.getString(cursor.getColumnIndex("term_id"));
			db.execSQL("delete from user where _id=" + id);
			db.execSQL("delete from course where ower_id=? AND term_id = ?", new String[] {owerId, termId});
		}
		cursor.close();
	}
	
	public Map<String, List<CourseBean>> fineTermById(int id) {
		SQLiteDatabase db = dbHelper.getReadableDatabase();
		Cursor cursor = db.rawQuery("select * from user where _id=" + id, null);
		if(cursor.moveToFirst()) {
			String owerId = cursor.getString(cursor.getColumnIndex("ower_id"));
			String termId =  cursor.getString(cursor.getColumnIndex("term_id"));
			Map<String, List<CourseBean>> courses = new HashMap<String, List<CourseBean>>();
			List<CourseBean> dayCourses = null;
			cursor.close();
			cursor = db.rawQuery("select * from course where ower_id=? AND term_id = ? order by '_id' asc", new String[] {owerId, termId});
			String flagDayWeek = "";
			while(cursor.moveToNext()) {
				int _id = cursor.getInt(cursor.getColumnIndex("_id"));
				String course_name = cursor.getString(cursor.getColumnIndex("course_name"));
				String course_teacher = cursor.getString(cursor.getColumnIndex("course_teacher"));
				String course_dayWeek = cursor.getString(cursor.getColumnIndex("course_dayWeek"));
				String course_week = cursor.getString(cursor.getColumnIndex("course_week"));
				String course_classes = cursor.getString(cursor.getColumnIndex("course_classes"));
				String course_classroom = cursor.getString(cursor.getColumnIndex("course_classroom"));
				String course_dayNo = cursor.getString(cursor.getColumnIndex("course_dayNo"));
				
				CourseBean course = new CourseBean(_id, course_name, course_teacher, course_week, course_classes, course_classroom, course_dayNo, course_dayWeek, termId, owerId);
				if(!flagDayWeek.equals(course_dayWeek)) {
					flagDayWeek = course_dayWeek;
					dayCourses = new ArrayList<CourseBean>();
					courses.put(new String(flagDayWeek), dayCourses);
				}
				dayCourses.add(course);			
			}
			
			for(int i = 0; i < 7; i++) {
				if(courses.get(String.valueOf(i)) == null) {
					courses.put(String.valueOf(i), new ArrayList<CourseBean>());
				}
			}
			cursor.close();
			db.close();
			return courses;
		}
		cursor.close();
		db.close();
		return null;
	}

	public Map<String, List<CourseBean>> fineTerm(String stuId, String termId) {
		SQLiteDatabase db = dbHelper.getWritableDatabase();
		
		Cursor cursor = db.rawQuery("select * from user where ower_id=? AND term_id=?", new String[] {stuId, termId});
		if(cursor.moveToFirst()) {
			int id = cursor.getInt(cursor.getColumnIndex("_id"));
			cursor.close();
			db.close();
			return this.fineTermById(id);
		}
		cursor.close();
		db.close();
		return null;
	}
	 

}
