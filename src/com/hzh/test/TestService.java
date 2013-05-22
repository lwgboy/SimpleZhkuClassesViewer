package com.hzh.test;

import android.test.AndroidTestCase;
import android.util.Log;

import com.hzh.service.CourseService;
import com.hzh.service.MainService;

public class TestService extends AndroidTestCase {
	
	public void testDownloadCourses() throws Exception {
		MainService cs = new MainService(this.getContext());
		String stuID = "201010214312";
		String passworld = "3338957";
		String term = "20120";
		cs.downloadAndSave(stuID, passworld, term);
	}
	
	public void testDeleteCoursse() throws Exception {
		CourseService cs = new CourseService(this.getContext());
		cs.deleteTermById(7);
		
	}
	
	public void testfineTermById() throws Exception {
		CourseService cs = new CourseService(this.getContext());
		
		Log.i(MainService.TAG, "≤È—Ø≤‚ ‘£°£°");
		Log.i(MainService.TAG, cs.fineTermById(2).toString());
		
	}
}
