package com.hzh.service;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import com.hzh.bean.CourseBean;
import com.hzh.exception.NameOrPasswordExcpetion;

public class MainService {

	public static String TAG = "CourseMsg";
	
	HttpService httpService = new HttpService();
	CourseService courseService = new CourseService();

	public Map<String, List<CourseBean>> downloadAndSave(String stuID, String password) {
		try {
			String html = httpService.getcoures2Html(stuID, password);
			return courseService.save(stuID, html);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		} catch (NameOrPasswordExcpetion e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		} 
				
	}



}
