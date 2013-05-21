package com.hzh.simplezhkuclassesviewer;

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

import com.hzh.bean.CourseBean;
import com.hzh.service.MainService;

public class MainActivity extends Activity {

	EditText stuID;
	EditText stuPsw;
	MainService mainService = new MainService();

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

		stuID = (EditText) this.findViewById(R.id.stu_id);
		stuPsw = (EditText) this.findViewById(R.id.password);

		Button btn = (Button) this.findViewById(R.id.login);
		btn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				HashMap<String, List<CourseBean>> data = (HashMap<String, List<CourseBean>>) mainService
						.downloadAndSave(stuID.getText().toString(), stuPsw
								.getText().toString());
				Intent intent = new Intent(MainActivity.this,
						DisplayActivity.class);
				intent.putExtra("courses", data);
				startActivity(intent);
			}

		});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

}
