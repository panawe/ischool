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
@Table(name="COURSE")
public class Course extends BaseEntity {
	
	@Id
	@Column(name="COURSE_ID")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	//@NotNull
	@ManyToOne
	@JoinColumn(name = "SUBJECT_ID", nullable = true)
	private Subject subject;

	//@NotNull
	@ManyToOne
	@JoinColumn(name = "TEACHER_ID", nullable = true)
	private Teacher teacher;

	//@NotNull
	@ManyToOne
	@JoinColumn(name = "CLASS_ID", nullable = true)
	private LevelClass levelClass;

	@Column(name = "BEGIN_DATE")
	private Date beginDate;
	
	@Column(name = "END_DATE")
	private Date endDate;
	
	@Column(name = "GROUP_CODE")
	private String groupCode;

	@Transient
	private String courseGroupDesc;
	
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

	@Override
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Subject getSubject() {
		return subject;
	}

	public void setSubject(Subject subject) {
		this.subject = subject;
	}

	public LevelClass getLevelClass() {
		return levelClass;
	}

	public void setLevelClass(LevelClass levelClass) {
		this.levelClass = levelClass;
	}

	public Teacher getTeacher() {
		return teacher;
	}

	public void setTeacher(Teacher teacher) {
		this.teacher = teacher;
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

	public String getCourseGroupDesc() {
		return courseGroupDesc;
	}

	public void setCourseGroupDesc(String courseGroupDesc) {
		this.courseGroupDesc = courseGroupDesc;
	}
}
