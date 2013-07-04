package com.hzh.simplezhkuclassesviewer;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.PagerTabStrip;
import android.support.v4.view.PagerTitleStrip;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.Window;
import android.widget.ListView;

import com.hzh.bean.CourseBean;
import com.hzh.service.CourseAdapter;

public class DisplayActivity extends Activity  {

	HashMap<String, List<CourseBean>> data;
	
	ViewPager viewPager;
	PagerTabStrip pagerTitleStrip;
	List<View> views;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		// »•µÙ±ÍÃ‚¿∏
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);  

		setContentView(R.layout.activity_display);
		
		data = (HashMap<String, List<CourseBean>>) this.getIntent().getSerializableExtra("courses");
		
		 viewPager = (ViewPager)findViewById(R.id.viewpager);  
		 pagerTitleStrip = (PagerTabStrip)findViewById(R.id.pagertitle);  
		 //pagerTitleStrip.setDrawFullUnderline(true);
		 pagerTitleStrip.setNonPrimaryAlpha(0.2f);
		 
		 views = new ArrayList<View>();
		 LayoutInflater inflater = LayoutInflater.from(this);
		 for(int i = 0; i < 7; i++) {
			 View view = inflater.inflate(R.layout.course_view_0+i, null);
			 ListView listView = (ListView) view.findViewById(R.id.each_listView);
			 showCourses(listView, i);
			 views.add(view); 
		 }
		 
		 viewPager.setAdapter(new MyViewPagerAdapter(this));
		 
		 int dayOfWeek = Calendar.getInstance().get(Calendar.DAY_OF_WEEK);
		 dayOfWeek = dayOfWeek == 1 ? 6 : dayOfWeek-2;
		 viewPager.setCurrentItem(504+dayOfWeek);
		
	}
	

	

	private void showCourses(ListView listView, int day) {	
			CourseAdapter adapter = new CourseAdapter(data.get(String.valueOf(day)), this, R.layout.coursev_view_each);	
			listView.setAdapter(adapter);
	}
	
	
	private final class MyViewPagerAdapter extends PagerAdapter {

		private Context context;
		public MyViewPagerAdapter(Context context) {
			this.context = context;
		}

		@Override
		public int getCount() {
			return 1001;
		}

		@Override
		public boolean isViewFromObject(View arg0, Object arg1) {
			return arg0 == arg1;
		}
		
		@Override
		public void destroyItem(View container, int position, Object object) {
			int p = position % 7;
			((ViewPager)container).removeView(views.get(p));
		}

		@Override
		public CharSequence getPageTitle(int position) {
			int p = position % 7;
			return context.getString(R.string.monday+p);
		}

		@Override
		public Object instantiateItem(View container, int position) {
			int p = position % 7;
			((ViewPager)container).addView(views.get(p));
			return views.get(p);
		}

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.display, menu);
		return true;
	}

}
