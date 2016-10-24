package com.esoft.ischool.vo;

import java.util.Date;

import org.apache.commons.lang.StringUtils;

public class FileSearchVO {
	private String yearName;
	private String subjectName;
	private String className;
	private String fileName;
	private String examTypeName;
	private Date examDate;
	private Date returnDate;

	
	public FileSearchVO() {}
	
	public FileSearchVO(String yearName, String subjectName, String className, String fileName) {
		setYearName(yearName);
		setSubjectName(subjectName);
		setClassName(className);
		setFileName(fileName);
	}
	
	public String getYearName() {
		return yearName;
	}
	public void setYearName(String yearName) {
		this.yearName = yearName;
	}
	public String getSubjectName() {
		return subjectName;
	}
	public void setSubjectName(String subjectName) {
		this.subjectName = subjectName;
	}
	public String getClassName() {
		return className;
	}
	public void setClassName(String className) {
		this.className = className;
	}
	public String getFileName() {
		return fileName;
	}
	public void setFileName(String fileName) {
		this.fileName = fileName;
	}
	
	public String getExamTypeName() {
		return examTypeName;
	}

	public void setExamTypeName(String examTypeName) {
		this.examTypeName = examTypeName;
	}

	public Date getExamDate() {
		return examDate;
	}

	public void setExamDate(Date examDate) {
		this.examDate = examDate;
	}

	public Date getReturnDate() {
		return returnDate;
	}

	public void setReturnDate(Date returnDate) {
		this.returnDate = returnDate;
	}

	public boolean containsNotEmptyField() {
		return (
		StringUtils.isNotBlank(getYearName())
		|| StringUtils.isNotBlank(getSubjectName())
		|| StringUtils.isNotBlank(getClassName())
		|| StringUtils.isNotBlank(getFileName())
		|| StringUtils.isNotBlank(getExamTypeName())
		|| (returnDate != null)
		|| (examDate != null)

		);
	}
}
