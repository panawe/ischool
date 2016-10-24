package com.esoft.ischool.vo;

import java.util.Date;

import org.apache.commons.lang.StringUtils;

public class ExamSearchVO {
	private String matricule;
	private String firstName;
	private String lastName;
	private String className;
	private Date examDate;
	private String examName;
	private String year;
	private String termName;
	private String subjectName;
	
	public String getMatricule() {
		return matricule;
	}
	public void setMatricule(String matricule) {
		this.matricule = matricule;
	}
	public String getLastName() {
		return lastName;
	}
	public void setLastName(String lastName) {
		this.lastName = lastName;
	}
	public String getFirstName() {
		return firstName;
	}
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}
	public String getClassName() {
		return className;
	}
	public void setClassName(String className) {
		this.className = className;
	}
	public Date getExamDate() {
		return examDate;
	}
	public void setExamDate(Date examDate) {
		this.examDate = examDate;
	}
	public String getExamName() {
		return examName;
	}
	public void setExamName(String examName) {
		this.examName = examName;
	}
	public String getYear() {
		return year;
	}
	public void setYear(String year) {
		this.year = year;
	}
	public String getTermName() {
		return termName;
	}
	public void setTermName(String termName) {
		this.termName = termName;
	}
	public String getSubjectName() {
		return subjectName;
	}
	public void setSubjectName(String subjectName) {
		this.subjectName = subjectName;
	}
	
	public boolean containsNotEmptyField() {
		return (
		StringUtils.isNotBlank(getMatricule())
		|| StringUtils.isNotBlank(getLastName())
		|| StringUtils.isNotBlank(getFirstName())
		|| StringUtils.isNotBlank(getYear())
		|| StringUtils.isNotBlank(getTermName())
		|| StringUtils.isNotBlank(getClassName())
		|| StringUtils.isNotBlank(getSubjectName())
		|| getExamDate() != null
		|| StringUtils.isNotBlank(getExamName())
		|| getExamDate() != null);
		
	}
}
