package com.hzh.simplezhkuclassesviewer;

import java.io.IOException;
import java.io.Serializable;
import java.util.HashMap;
import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.hzh.bean.CourseBean;
import com.hzh.exception.NameOrPasswordExcpetion;
import com.hzh.exception.TermNoExitsExcpetinon;
import com.hzh.service.MainService;

public class MainActivity extends Activity {

	EditText stuID;
	EditText stuPsw;
	EditText stuTerm;
	MainService mainService;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder()
				.detectDiskReads().detectDiskWrites().detectNetwork()
				.penaltyLog().build());
		StrictMode.setVmPolicy(new StrictMode.VmPolicy.Builder()
				.detectLeakedSqlLiteObjects()
				.penaltyLog()
				.penaltyDeath().build());

		mainService = new MainService(this);
		stuID = (EditText) this.findViewById(R.id.stu_id);
		stuPsw = (EditText) this.findViewById(R.id.password);
		stuTerm = (EditText) this.findViewById(R.id.term);

		Button btn = (Button) this.findViewById(R.id.login);
		Button btn2 = (Button) this.findViewById(R.id.view);
		
		MyButtonOnClickListener btnListen = new MyButtonOnClickListener();
		btn.setOnClickListener(btnListen);
		btn2.setOnClickListener(btnListen);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
	
	private final class MyButtonOnClickListener implements OnClickListener {

		@Override
		public void onClick(View arg0) {
			HashMap<String, List<CourseBean>> data;
			switch (arg0.getId()) {
			case R.id.login:				
				try {
					data = (HashMap<String, List<CourseBean>>) mainService.downloadAndSave(stuID.getText().toString(), stuPsw.getText().toString(), stuTerm.getText().toString());
					Toast.makeText(MainActivity.this, "下载成功！", Toast.LENGTH_SHORT).show();
					Intent intent = new Intent(MainActivity.this, DisplayActivity.class);
					intent.putExtra("courses", data);
					startActivity(intent);
					Toast.makeText(MainActivity.this, "下载成功！！", Toast.LENGTH_SHORT).show();
				} catch (IOException e) {
					Toast.makeText(MainActivity.this, "下载失败！网络错误！", Toast.LENGTH_SHORT).show();
					e.printStackTrace();
				} catch (NameOrPasswordExcpetion e) {
					Toast.makeText(MainActivity.this, "下载失败！用户名密码错误或者！", Toast.LENGTH_SHORT).show();
					e.printStackTrace();
				} catch (TermNoExitsExcpetinon e) {
					Toast.makeText(MainActivity.this, "下载失败！学期不存在或该学期课程为空！！", Toast.LENGTH_SHORT).show();
					e.printStackTrace();
				}
				

				break;
			case R.id.view:
				if((data = (HashMap<String, List<CourseBean>>) mainService.viewTerm(stuID.getText().toString(), stuTerm.getText().toString())) != null) {
					Intent intent = new Intent(MainActivity.this, DisplayActivity.class);
					intent.putExtra("courses", data);
					startActivity(intent);
				} else {
					Toast.makeText(MainActivity.this, "课表不存在！", Toast.LENGTH_SHORT).show();
				}
				break;
			}
//			HashMap<String, List<CourseBean>> data = (HashMap<String, List<CourseBean>>) mainService
//					.downloadAndSave(stuID.getText().toString(), stuPsw
//							.getText().toString());
//			Intent intent = new Intent(MainActivity.this,
//					DisplayActivity.class);
//			intent.putExtra("courses", data);
//			startActivity(intent);
		}

	}

}
