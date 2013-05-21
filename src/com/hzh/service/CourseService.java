package com.hzh.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import android.util.Log;

import com.hzh.bean.CourseBean;

public class CourseService {

	public static final String[] WEEK_DAY = {"一", "二", "三", "四", "五", "六", "日"};
	
	public Map<String, List<CourseBean>> save(String stuID, String html) {
		return html2courses(html);

	}

	public Map<String, List<CourseBean>> html2courses(String html) {
		Document doc = Jsoup.parse(html);
		Element es = doc.select("table").get(3);
		StringBuilder[] classes = new StringBuilder[7];
		for (int i = 0; i < classes.length; i++) {
			classes[i] = new StringBuilder();
		}

		for (Element e : es.select("td")) {
			String text = e.text();
			if (!"".equals(text.trim())) {
				if (text.contains("]一[")) {
					classes[0].append(text.replaceAll("\\[\\d{6}\\]", "") + " ");

				} else if (text.contains("]二[")) {
					classes[1].append(text.replaceAll("\\[\\d{6}\\]", "") + " ");

				} else if (text.contains("]三[")) {
					classes[2].append(text.replaceAll("\\[\\d{6}\\]", "") + " ");

				} else if (text.contains("]四[")) {
					classes[3].append(text.replaceAll("\\[\\d{6}\\]", "") + " ");

				} else if (text.contains("]五[")) {
					classes[4].append(text.replaceAll("\\[\\d{6}\\]", "") + " ");

				} else if (text.contains("]六[")) {
					classes[5].append(text.replaceAll("\\[\\d{6}\\]", "") + " ");

				} else if (text.contains("]日[")) {
					classes[6].append(text.replaceAll("\\[\\d{6}\\]", "") + " ");
				}
			}
		}

		Map<String, List<CourseBean>> courses = new HashMap<String, List<CourseBean>>();

		for (int i = 0; i < 7; i++) {
			List<CourseBean> coursesList = courseToList(classes[i].toString(), i);
			courses.put(i + "", coursesList);
		}
		
		Log.i(MainService.TAG, "ok!");
		Log.i(MainService.TAG, courses.toString());
		return courses;

	}

	private List<CourseBean> courseToList(String courses, int day) {
		List<CourseBean> coursesList = new ArrayList<CourseBean>();
		if(courses.length() == 0) return coursesList;
		String[] coursesArray = courses.split(" ");
		for(int i = 0; i < coursesArray.length; i+=4) {
			CourseBean course = new CourseBean();
			course.setClassName(coursesArray[i]);
			course.setTeacherName(coursesArray[i+1]);
			String weeks = coursesArray[i+2].substring(0, coursesArray[i+2].indexOf(WEEK_DAY[day]));
			String numofclass = coursesArray[i+2].substring(coursesArray[i+2].indexOf(WEEK_DAY[day]) + 1,  coursesArray[i+2].length());
			course.setWeeks(weeks);
			course.setClasses(numofclass);
			course.setClassroom(coursesArray[i+3]);	
			coursesList.add(course);
		}
		return coursesList;
	}

}
