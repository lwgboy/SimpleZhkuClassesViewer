package com.hzh.simplezhkuclassesviewer;

import java.io.IOException;
import java.io.Serializable;
import java.util.HashMap;
import java.util.List;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
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
import com.hzh.bean.TermBean;
import com.hzh.exception.NameOrPasswordExcpetion;
import com.hzh.exception.TermNoExitsExcpetinon;
import com.hzh.service.MainService;
import com.hzh.util.MyStringUtil;

public class MainActivity extends Activity {

	EditText stuID;
	EditText stuPsw;
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

		private String[] termIDs;
		private String [] cnTerms;
		private int select;
		private List<TermBean> termList;
		HashMap<String, List<CourseBean>> data;
		@Override
		public void onClick(View arg0) {
			switch (arg0.getId()) {
			case R.id.login:			
				try {
					termIDs = mainService.getAllTermIDsByNetWork(stuID.getText().toString(), stuPsw.getText().toString());
					cnTerms = new String[termIDs.length];
					for(int i = 0; i < termIDs.length; i++) {
						cnTerms[i] = MyStringUtil.termId2Chinese(termIDs[i]);
					}

					// �ܸ��� ��ʾһ��ѡ��ѧ�ڶԻ���Ķ�������N�������࣬�д��Ż�
					new AlertDialog.Builder(MainActivity.this)  
						.setTitle("��ѡ��ѧ��Ҫ������")  
						.setSingleChoiceItems(cnTerms, 0, new DialogInterface.OnClickListener() {

							@Override
							public void onClick(DialogInterface dialog, int which) {
								select = which;							
							}
						})
						.setPositiveButton("ȷ��", new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog, int which) {
								//Toast.makeText(MainActivity.this, cnTerms[select] , Toast.LENGTH_SHORT).show();							
								try {
									data = (HashMap<String, List<CourseBean>>) mainService.downloadAndSave(stuID.getText().toString(), stuPsw.getText().toString(),  termIDs[select]);
									Intent intent = new Intent(MainActivity.this, DisplayActivity.class);
									intent.putExtra("courses", data);
									startActivity(intent);
									Toast.makeText(MainActivity.this, "���سɹ�����", Toast.LENGTH_SHORT).show();
								} catch (IOException e) {
									Toast.makeText(MainActivity.this, "����ʧ�ܣ��������", Toast.LENGTH_SHORT).show();
									e.printStackTrace();
								} catch (NameOrPasswordExcpetion e) {
									Toast.makeText(MainActivity.this, "����ʧ�ܣ��û������������ߣ�", Toast.LENGTH_SHORT).show();
									e.printStackTrace();
								} catch (TermNoExitsExcpetinon e) {
									Toast.makeText(MainActivity.this, "����ʧ�ܣ�ѧ�ڲ����ڻ��ѧ�ڿγ�Ϊ�գ���", Toast.LENGTH_SHORT).show();
									e.printStackTrace();
								}
							}
							
						}).setNegativeButton("ȡ��", null).show();  

				} catch (IOException e) {
					Toast.makeText(MainActivity.this, "����ʧ�ܣ��������", Toast.LENGTH_SHORT).show();
					e.printStackTrace();
				} catch (NameOrPasswordExcpetion e) {
					Toast.makeText(MainActivity.this, "����ʧ�ܣ��û������������ߣ�", Toast.LENGTH_SHORT).show();
					e.printStackTrace();
				} 
				
				break;
			case R.id.view:
				termList = mainService.getAllTermIDsByDB(stuID.getText().toString());
				cnTerms = new String[termList.size()];
				for(int i = 0; i < cnTerms.length; i++) {
					cnTerms[i] = termList.get(i).getTermChinese();
				}
				// ���������������N�������࣬�д��Ż�
				new AlertDialog.Builder(MainActivity.this)  
					.setTitle("��ѡ��Ҫ�򿪵Ŀα�")  
					.setSingleChoiceItems(cnTerms, 0, new DialogInterface.OnClickListener() {						
						@Override
						public void onClick(DialogInterface dialog, int which) {
							select = which;					
						}
					})
					.setPositiveButton("ȷ��",new DialogInterface.OnClickListener() {						
						@Override
						public void onClick(DialogInterface dialog, int which) {
							if((data = (HashMap<String, List<CourseBean>>) mainService.viewTerm(stuID.getText().toString(), termList.get(select).getTermId())) != null) {
								Intent intent = new Intent(MainActivity.this, DisplayActivity.class);
								intent.putExtra("courses", data);
								startActivity(intent);
								//MainActivity.this.overridePendingTransition(R.anim.enter_alpha, R.anim.exit_alpha);
							} else {
								Toast.makeText(MainActivity.this, "�α����ڣ�", Toast.LENGTH_SHORT).show();
							}										
						}
					})
					.setNegativeButton("ȡ��", null).show();  
				
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
