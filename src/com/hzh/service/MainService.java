package com.hzh.service;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.http.client.ClientProtocolException;

import android.content.Context;

import com.hzh.bean.CourseBean;
import com.hzh.bean.TermBean;
import com.hzh.exception.NameOrPasswordExcpetion;
import com.hzh.exception.TermNoExitsExcpetinon;

public class MainService {

	public static String TAG = "CourseMsg";
	
	HttpService httpService = new HttpService();
	CourseService courseService;

	public MainService(Context context) {
		this.courseService = new CourseService(context);
	}
	
	public Map<String, List<CourseBean>> downloadAndSave(String stuID, String password, String term) throws IOException, NameOrPasswordExcpetion, TermNoExitsExcpetinon {
		String html = httpService.getcoures2Html(stuID, password, term);
		return courseService.saveTerm(stuID, term, html);
		
	}


	public Map<String, List<CourseBean>> viewTerm(String stuId, String termId) {
		return courseService.fineTerm(stuId, termId);
	}


	public String[] getAllTermIDsByNetWork(String stuID, String password) throws ClientProtocolException, IOException, NameOrPasswordExcpetion {
		return httpService.getAllTermId(stuID, password);
	}
	
	public List<TermBean> getAllTermIDsByDB(String owerId) {
		return courseService.getAllTerm(owerId);
	}
	
	

}
