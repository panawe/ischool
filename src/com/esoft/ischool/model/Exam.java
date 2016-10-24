package com.esoft.ischool.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

@Entity
@Table(name="EXAM")
public class Exam extends BaseEntity {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name="EXAM_ID")
	private Long id;
	
	@Column(name="NAME")
	private String name;

	@Column(name="EXAM_DATE")
	private Date examDate;

	//@NotNull
	@ManyToOne
	@JoinColumn(name = "SCHOOLYEAR_ID", nullable = true)
	private SchoolYear schoolYear ;
	
	public Date getExamDate() {
		return examDate;
	}

	public void setExamDate(Date examDate) {
		this.examDate = examDate;
	}

	//@NotNull
	@ManyToOne
	@JoinColumn(name = "EXAM_TYPE_ID")
	private ExamType examType;

	//@NotNull
	@ManyToOne
	@JoinColumn(name = "TERM_ID")
	private Term term;


	//@NotNull
	@ManyToOne
	@JoinColumn(name = "COURSE_ID", nullable = true)
	private Course course;


	@Column(name="RATIO")
	private Integer ratio;

	@Column(name ="EVALUATION_TYPE")
	private Short evaluationType=0;
	
	@Column(name ="PUBLISH_MARKS")
	private Short publishMarks=0;
	
	//@NotNull
	//@Range(min=1, max=99999)
	@Column(name="MAX_MARK")
	private Double maxMark;
	
	
	public Double getMaxMark() {
		return maxMark;
	}

	public void setMaxMark(Double maxMark) {
		this.maxMark = maxMark;
	}

	public SchoolYear getSchoolYear() {
		return schoolYear;
	}

	public void setSchoolYear(SchoolYear schoolYear) {
		this.schoolYear = schoolYear;
	}

	public Short getEvaluationType() {
		return evaluationType;
	}

	public void setEvaluationType(Short evaluationType) {
		this.evaluationType = evaluationType;
	}

	public boolean getPublishMarks() {
		return publishMarks==0?false:true;
	}

	public void setPublishMarks(boolean publishMark) {
		this.publishMarks = (publishMark==true?new Short("1"):0);
	}

	@Override
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


	public void setExamType(ExamType examType) {
		this.examType = examType;
	}

	public void setTerm(Term term) {
		this.term = term;
	}

	public void setCourse(Course course) {
		this.course = course;
	}

	public Integer getRatio() {
		return ratio;
	}

	public void setRatio(Integer ratio) {
		this.ratio = ratio;
	}

	public ExamType getExamType() {
		return examType;
	}

	public Term getTerm() {
		return term;
	}

	public Course getCourse() {
		return course;
	}

	@Override
	public String toString() {
		return "Exam [course=" + course + ", examDate=" + examDate
				+ ", examType=" + examType + ", id=" + id + ", name=" + name
				+ ", ratio=" + ratio + ", term=" + term + "]";
	}
	
}
