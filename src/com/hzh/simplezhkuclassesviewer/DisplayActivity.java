package com.hzh.simplezhkuclassesviewer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import com.hzh.bean.CourseBean;
import com.hzh.service.CourseAdapter;
import com.hzh.service.CourseService;
import com.hzh.util.HtmlUtil;

public class DisplayActivity extends Activity {

	HashMap<String, List<CourseBean>> data;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_display);
		data = (HashMap<String, List<CourseBean>>) this.getIntent().getSerializableExtra("courses");

		showCourses();
	}

	private void showCourses() {
		List<CourseBean> list = new ArrayList<CourseBean>();
		for(int i = 0; i < data.size(); i++) {
			CourseBean week = new CourseBean();
			week.setClassName( "ÐÇÆÚ" + HtmlUtil.WEEK_DAY[i]);
			list.add(week);
			
			for (CourseBean course : this.data.get(String.valueOf(i))) {
				list.add(course);
			}			
		}
		
		CourseAdapter adapter = new CourseAdapter(list, this, R.layout.coursev_view_each);
		
		ListView listView = (ListView)this.findViewById(R.id.listView);
		listView.setAdapter(adapter);
	}

	private void showCourses_old() {
		List<HashMap<String, Object>> list = new ArrayList<HashMap<String, Object>>();
		for(int i = 0; i < data.size(); i++) {
			HashMap<String, Object> week = new HashMap<String, Object>();
			week.put("dayWeek", "ÐÇÆÚ" + HtmlUtil.WEEK_DAY[i]);
			list.add(week);
			
			for (CourseBean course : this.data.get(String.valueOf(i))) {
				HashMap<String, Object> item = new HashMap<String, Object>();
				item.put("className", course.getClassName());
				item.put("TeacherName", course.getTeacherName());
				item.put("weeks", course.getWeeks());
				item.put("classes", course.getClasses());
				item.put("classroom", course.getClassroom());
				list.add(item);
			}
			
		}
		SimpleAdapter adapter = new SimpleAdapter(this, list, R.layout.coursev_view_each,
													new String[] { "className", "TeacherName", "weeks", "classes", "classroom" },
													new int[] {R.id.course_name, R.id.course_teacher, R.id.course_weeks, R.id.course_classes, R.id.course_classroom });
		
		ListView listView = (ListView)this.findViewById(R.id.listView);
		listView.setAdapter(adapter);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.display, menu);
		return true;
	}

}
