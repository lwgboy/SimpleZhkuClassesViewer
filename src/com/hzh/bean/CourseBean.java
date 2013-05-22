package com.hzh.bean;

import java.io.Serializable;

public class CourseBean implements Serializable{
	
	int classId;
	String className;
	String teacherName;
	String weeks;
	String classes;
	String classroom;
	
	String dayNo;
	String dayWeek;
	
	String termId;
	String owerId;
	
	
	
	
	public String getTermId() {
		return termId;
	}
	public void setTermId(String termId) {
		this.termId = termId;
	}
	public String getOwerId() {
		return owerId;
	}
	public void setOwerId(String owerId) {
		this.owerId = owerId;
	}
	public String getDayNo() {
		return dayNo;
	}
	public void setDayNo(String dayNo) {
		this.dayNo = dayNo;
	}
	public String getDayWeek() {
		return dayWeek;
	}
	public void setDayWeek(String dayWeek) {
		this.dayWeek = dayWeek;
	}
	public int getClassId() {
		return classId;
	}
	public void setClassId(int classId) {
		this.classId = classId;
	}
	public String getClassName() {
		return className;
	}
	public void setClassName(String className) {
		this.className = className;
	}
	public String getTeacherName() {
		return teacherName;
	}
	public void setTeacherName(String teacherName) {
		this.teacherName = teacherName;
	}
	public String getWeeks() {
		return weeks;
	}
	public void setWeeks(String weeks) {
		this.weeks = weeks;
	}
	public String getClasses() {
		return classes;
	}
	public void setClasses(String classes) {
		this.classes = classes;
	}
	public String getClassroom() {
		return classroom;
	}
	public void setClassroom(String classroom) {
		this.classroom = classroom;
	}
	
	
	public CourseBean(int classId, String className, String teacherName,
			String weeks, String classes, String classroom, String dayNo,
			String dayWeek, String termId, String owerId) {
		super();
		this.classId = classId;
		this.className = className;
		this.teacherName = teacherName;
		this.weeks = weeks;
		this.classes = classes;
		this.classroom = classroom;
		this.dayNo = dayNo;
		this.dayWeek = dayWeek;
		this.termId = termId;
		this.owerId = owerId;
	}
	public CourseBean() {
		// TODO Auto-generated constructor stub
	}
	@Override
	public String toString() {
		return "CourseBean [classId=" + classId + ", className=" + className
				+ ", teacherName=" + teacherName + ", weeks=" + weeks
				+ ", classes=" + classes + ", classroom=" + classroom
				+ ", dayNo=" + dayNo + ", dayWeek=" + dayWeek + ", termId="
				+ termId + ", owerId=" + owerId + "]";
	}
	
	
	
}
