package com.esoft.ischool.vo;

import java.util.Date;
public class CourseVO  {
	
	private Long id;
	
	private Long teacherId;

	private String subject;

	private String teacherFirstName;
	
	private String teacherLastName;

	private String className;

	private Date beginDate;
	
	private Date endDate;
	
	private String groupCode;

	private Double maxMark;
	
	private String courseGroupDesc;
	
	
	public String getCourseGroupDesc() {
		return courseGroupDesc;
	}

	public void setCourseGroupDesc(String courseGroupDesc) {
		this.courseGroupDesc = courseGroupDesc;
	}

	public Long getTeacherId() {
		return teacherId;
	}

	public void setTeacherId(Long teacherId) {
		this.teacherId = teacherId;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getSubject() {
		return subject;
	}

	public void setSubject(String subject) {
		this.subject = subject;
	}

	public String getTeacherFirstName() {
		return teacherFirstName;
	}

	public void setTeacherFirstName(String teacherFirstName) {
		this.teacherFirstName = teacherFirstName;
	}

	public String getTeacherLastName() {
		return teacherLastName;
	}

	public void setTeacherLastName(String teacherLastName) {
		this.teacherLastName = teacherLastName;
	}

	public String getClassName() {
		return className;
	}

	public void setClassName(String className) {
		this.className = className;
	}

	public Double getMaxMark() {
		return maxMark;
	}

	public void setMaxMark(Double maxMark) {
		this.maxMark = maxMark;
	}


	public Date getBeginDate() {
		return beginDate;
	}

	public void setBeginDate(Date beginDate) {
		this.beginDate = beginDate;
	}

	public Date getEndDate() {
		return endDate;
	}

	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}

	public String getGroupCode() {
		return groupCode;
	}

	public void setGroupCode(String groupCode) {
		this.groupCode = groupCode;
	}

}
