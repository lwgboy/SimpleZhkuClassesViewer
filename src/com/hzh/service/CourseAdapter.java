package com.hzh.service;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.hzh.bean.CourseBean;
import com.hzh.simplezhkuclassesviewer.R;

public class CourseAdapter extends BaseAdapter {

	List<CourseBean> courseBeans;
	LayoutInflater inflater;
	int layout;

	public CourseAdapter(List<CourseBean> courseBeans, Context context,
			int layout) {
		this.courseBeans = courseBeans;
		inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		this.layout = layout;

	}

	@Override
	public int getCount() {
		return courseBeans.size();
	}

	@Override
	public Object getItem(int arg0) {
		return courseBeans.get(arg0);
	}

	@Override
	public long getItemId(int arg0) {
		return arg0;
	}

	@Override
	public View getView(int arg0, View arg1, ViewGroup arg2) {
		if (arg1 == null) {
			arg1 = inflater.inflate(layout, null);
		}

		TextView dayWeek = (TextView) arg1.findViewById(R.id.day_week);
		TextView courseName = (TextView) arg1.findViewById(R.id.course_name);
		TextView courseTeacher = (TextView) arg1.findViewById(R.id.course_teacher);
		TextView courseWeeks = (TextView) arg1.findViewById(R.id.course_weeks);
		TextView courseclasses = (TextView) arg1.findViewById(R.id.course_classes);
		TextView courseClassroom = (TextView) arg1.findViewById(R.id.course_classroom);

		CourseBean c = courseBeans.get(arg0);

		if (c.getClassName().contains("ÐÇÆÚ")) {
			
			if(!dayWeek.isShown()) dayWeek.setVisibility(View.VISIBLE);
			
			dayWeek.setText(c.getClassName());
			courseName.setVisibility(View.GONE);
			courseTeacher.setVisibility(View.GONE);
			courseWeeks.setVisibility(View.GONE);
			courseclasses.setVisibility(View.GONE);
			courseClassroom.setVisibility(View.GONE);
		} else {
			if(!courseName.isShown()) courseName.setVisibility(View.VISIBLE);
			if(!courseTeacher.isShown()) courseTeacher.setVisibility(View.VISIBLE);
			if(!courseWeeks.isShown()) courseWeeks.setVisibility(View.VISIBLE);
			if(!courseclasses.isShown()) courseclasses.setVisibility(View.VISIBLE);
			if(!courseClassroom.isShown()) courseClassroom.setVisibility(View.VISIBLE);
			
			dayWeek.setVisibility(View.GONE);
			
			courseName.setText(c.getClassName());
			courseTeacher.setText(c.getTeacherName());
			courseWeeks.setText(c.getWeeks());
			courseclasses.setText(c.getClasses());
			courseClassroom.setText(c.getClassroom());
		}
		return arg1;
	}
}
