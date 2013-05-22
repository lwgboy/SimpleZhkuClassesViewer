package com.hzh.service;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;

public class DBHelper extends SQLiteOpenHelper{

	public DBHelper(Context context) {
		super(context, "zhkuDB.db", null, 1);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		//db.execSQL("CREATE TABLE IF NOT EXISTS person (personid integer primary key autoincrement, name varchar(20))");
		db.execSQL("drop table if exists user");
		db.execSQL("drop table if exists course");
		db.execSQL("create table user(_id integer primary key autoincrement, ower_id char(20), term_id char(20), term_name char(20))");
		db.execSQL("create table course(_id integer primary key autoincrement, term_id  char(20),  ower_id char(10),course_name char(50), "
				 + "course_teacher char(20), course_dayWeek char(20), course_week  char(20),course_classes char(20), "
				 + "course_classroom char(20), course_dayNo char(20))");
		
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		//db.execSQL("ALTER TABLE person ADD phone VARCHAR(12) NULL ");
		//db.execSQL("ALTER TABLE person ADD bank integer NULL ");
		System.out.println("Update£¡£¡");
	}
	
}
