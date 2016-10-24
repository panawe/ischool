package com.esoft.ischool.vo;
import java.util.Date;
public class ExamVO {	
	private Long id;
	private String name;
	private Date examDate;
	private String schoolYear ;
	private String examType;
	private String term;
	private String course;
	private Integer ratio;
	private Short evaluationType=0;
	private Short publishMarks=0;
	private Double maxMark;
	private String className;

	public String getClassName() {
		return className;
	}
	public void setClassName(String className) {
		this.className = className;
	}
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public Date getExamDate() {
		return examDate;
	}
	public void setExamDate(Date examDate) {
		this.examDate = examDate;
	}
	public String getSchoolYear() {
		return schoolYear;
	}
	public void setSchoolYear(String schoolYear) {
		this.schoolYear = schoolYear;
	}
	public String getExamType() {
		return examType;
	}
	public void setExamType(String examType) {
		this.examType = examType;
	}
	public String getTerm() {
		return term;
	}
	public void setTerm(String term) {
		this.term = term;
	}
	public String getCourse() {
		return course;
	}
	public void setCourse(String course) {
		this.course = course;
	}
	public Integer getRatio() {
		return ratio;
	}
	public void setRatio(Integer ratio) {
		this.ratio = ratio;
	}
	public Short getEvaluationType() {
		return evaluationType;
	}
	public void setEvaluationType(Short evaluationType) {
		this.evaluationType = evaluationType;
	}
	public Short getPublishMarks() {
		return publishMarks;
	}
	public void setPublishMarks(Short publishMarks) {
		this.publishMarks = publishMarks;
	}
	public Double getMaxMark() {
		return maxMark;
	}
	public void setMaxMark(Double maxMark) {
		this.maxMark = maxMark;
	}
	
	
}
