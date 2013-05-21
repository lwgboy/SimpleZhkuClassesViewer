package com.hzh.test;

import android.test.AndroidTestCase;

import com.hzh.service.MainService;

public class TestService extends AndroidTestCase {
	
	public void testDownloadCourses() throws Exception {
		MainService cs = new MainService();
		String stuID = "201010214312";
		String passworld = "3338957";
		cs.downloadAndSave(stuID, passworld);
	}
}
