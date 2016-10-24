package com.esoft.ischool.model;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;


@Entity
@Table(name = "EXAM_TIMETABLE")
public class ExamTimeTable extends BaseEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "TIMETABLE_ID")
	private Long id;

	//@NotNull
	@Column(name = "EXAM_DATE")
	private Date examDate;

	//@NotNull
	@Column(name = "BEGIN_TIME")
	private String beginTime;

	//@NotNull
	@Column(name = "END_TIME")
	private String endTime;

	//@NotNull
	@ManyToOne
	@JoinColumn(name = "CLASS_ID", nullable = true)
	private LevelClass levelClass;
	
	//@NotNull
	@ManyToOne
	@JoinColumn(name = "COURSE_ID", nullable = true)
	private Course course;

	//@NotNull
	@ManyToOne
	@JoinColumn(name = "SCHOOLYEAR_ID", nullable = true)
	private SchoolYear schoolYear;

	@ManyToOne
	@JoinColumn(name = "TERM_ID", nullable = true)
	private Term term;

	@Transient
	private String dayOfWeekDescription;
	
	public Course getCourse() {
		return course;
	}

	public void setCourse(Course course) {
		this.course = course;
	}

	public Date getExamDate() {
		return examDate;
	}

	public void setExamDate(Date examDate) {
		this.examDate = examDate;
	}

	public LevelClass getLevelClass() {
		return levelClass;
	}

	public void setLevelClass(LevelClass levelClass) {
		this.levelClass = levelClass;
	}

	public SchoolYear getSchoolYear() {
		return schoolYear;
	}

	public void setSchoolYear(SchoolYear schoolYear) {
		this.schoolYear = schoolYear;
	}

	public Term getTerm() {
		return term;
	}

	public void setTerm(Term term) {
		this.term = term;
	}

	public String getBeginTime() {
		return beginTime;
	}

	public void setBeginTime(String beginTime) {
		this.beginTime = beginTime;
	}

	public String getEndTime() {
		return endTime;
	}

	public void setEndTime(String endTime) {
		this.endTime = endTime;
	}

	@Override
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getDayOfWeekDescription() {
		return dayOfWeekDescription;
	}

	public void setDayOfWeekDescription(String dayOfWeekDescription) {
		this.dayOfWeekDescription = dayOfWeekDescription;
	}
}
