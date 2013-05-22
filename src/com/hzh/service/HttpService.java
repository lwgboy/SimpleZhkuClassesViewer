package com.hzh.service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.util.EntityUtils;

import com.hzh.exception.NameOrPasswordExcpetion;
import com.hzh.util.HtmlUtil;

public class HttpService {

	private HttpClient client;
	private int timeoutConnection = 3000;  
	private int timeoutSocket = 5000;
	private BasicHttpParams httpParameters;  
	
	private String[] termIDs;  // 登陆时顺便取出来的
	
	public String[] getTermIDs() {
		return termIDs;
	}


	public HttpService() {
		 httpParameters = new BasicHttpParams();// Set the timeout in milliseconds until a connection is established.   
		 HttpConnectionParams.setConnectionTimeout(httpParameters, timeoutConnection);// Set the default socket timeout (SO_TIMEOUT) // in milliseconds which is the timeout for waiting for data.   
		 HttpConnectionParams.setSoTimeout(httpParameters, timeoutSocket);  

		client = new DefaultHttpClient(httpParameters);
		//client.getParams().setIntParameter("http.socket.timeout",10000);
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

		Map<String, String> p = login(stuID, password);

		/*
		 * Sel_XNXQ:20121 rad:1 px:0 Submit01:(unable to decode value)
		 */
		String url = "http://jw.zhku.edu.cn/jwmis/znpk/Pri_StuSel_rpt.aspx";
		p.clear();
//		p.put("Sel_XNXQ", "20121");
		p.put("Sel_XNXQ", term);
		p.put("rad", "0");
		p.put("px", "0");

		HttpResponse response = sendPostRequest(client, url, p);
		if(response != null) {
			String html = EntityUtils.toString(response.getEntity());
			return html;
		}
		return null;			
	}


	private Map<String, String> login(String stuID, String password)
			throws IOException, NameOrPasswordExcpetion,
			ClientProtocolException {
		String url = "http://jw.zhku.edu.cn/jwmis/_data/index_LOGIN.aspx";
		Map<String, String> p = new HashMap<String, String>();
		p.put("Sel_Type", "STU");
		p.put("UserID", stuID);
		p.put("PassWord", password);
		HttpResponse response = sendPostRequest(client, url, p);
		if (response != null) {		
			String html = EntityUtils.toString(response.getEntity());
			//System.out.println(html);
			if(html.contains("登录失败") || html.contains("帐号或密码不正确！请重新输入。")) {
				throw new NameOrPasswordExcpetion();
			}
		}
		
		return p;
	}
	
	public String[] getAllTermId(String stuID, String password) throws ClientProtocolException, IOException, NameOrPasswordExcpetion {
		this.login(stuID, password);
		String url = "http://jw.zhku.edu.cn/jwmis/znpk/Pri_StuSel.aspx";
		HttpGet httpGet = new HttpGet(url);
		HttpResponse response = client.execute(httpGet);
		if(response != null) {
			String html = EntityUtils.toString(response.getEntity());
			this.termIDs = HtmlUtil.getAllTermId(html);
		}
		return this.termIDs;
	}
	
}
