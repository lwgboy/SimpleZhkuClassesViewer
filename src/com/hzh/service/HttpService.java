package com.hzh.service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import com.hzh.exception.NameOrPasswordExcpetion;

public class HttpService {

	private HttpClient client;
	public HttpService() {
		client = new DefaultHttpClient();
		client.getParams().setIntParameter("http.socket.timeout",3000);
	}
	
	public  HttpResponse sendPostRequest(HttpClient client2,
			String url, Map<String, String> p) throws IOException {
		List<NameValuePair> par = new ArrayList<NameValuePair>();
		if (p != null && !p.isEmpty()) {
			for (Map.Entry<String, String> e : p.entrySet()) {
				par.add(new BasicNameValuePair(e.getKey(), e.getValue()));
			}
		}

		UrlEncodedFormEntity entity = new UrlEncodedFormEntity(par, "GBK");
		HttpPost post = new HttpPost(url);
		post.setHeader("Content-Type", "application/x-www-form-urlencoded");
		post.setEntity(entity);

		HttpResponse response = client2.execute(post);
		// response.getAllHeaders();

		if (response.getStatusLine().getStatusCode() == 200) {
			return response;
		}

		return null;
	}
	
	public  HttpResponse sendPostRequest(String url, Map<String, String> p) throws IOException {
		return this.sendPostRequest(this.client, url, p);
	}

	public String getcoures2Html(String stuID, String password, String term) throws IOException, NameOrPasswordExcpetion {
		String url = "http://jw.zhku.edu.cn/jwmis/_data/index_LOGIN.aspx";
		Map<String, String> p = new HashMap<String, String>();
		p.put("Sel_Type", "STU");
		p.put("UserID", stuID);
		p.put("PassWord", password);
		HttpResponse response = sendPostRequest(client, url, p);
		if (response != null) {		
			String html = EntityUtils.toString(response.getEntity());
			System.out.println(html);
			if(html.contains("登录失败") || html.contains("帐号或密码不正确！请重新输入。")) {
				throw new NameOrPasswordExcpetion();
			}
		}

		/*
		 * Sel_XNXQ:20121 rad:1 px:0 Submit01:(unable to decode value)
		 */
		url = "http://jw.zhku.edu.cn/jwmis/znpk/Pri_StuSel_rpt.aspx";
		p.clear();
//		p.put("Sel_XNXQ", "20121");
		p.put("Sel_XNXQ", term);
		p.put("rad", "0");
		p.put("px", "0");

		response = sendPostRequest(client, url, p);
		if(response != null) {
			String html = EntityUtils.toString(response.getEntity());
			return html;
		}
		return null;
		
		
	}
	
}
