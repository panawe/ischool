package com.esoft.ischool.model;

import java.util.Date;

public class ExamDay {
	private Date examDay;
	private String examDayDescription;
	public Date getExamDay() {
		return examDay;
	}
	public void setExamDay(Date examDay) {
		this.examDay = examDay;
	}
	public String getExamDayDescription() {
		return examDayDescription;
	}
	public void setExamDayDescription(String examDayDescription) {
		this.examDayDescription = examDayDescription;
	}
}
