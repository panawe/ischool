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


@Entity
@Table(name="SENROLLMENT")
public class StudentEnrollment extends BaseEntity {
	
	@Id
	@Column(name="ENROLLMENT_ID")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@Column(name="COMMENTS")
	private String comments;

	//@NotNull
	@Column(name="ENROLLMENT_DATE")
	private Date enrollmentDate;

	//@NotNull
	@ManyToOne
	@JoinColumn(name = "STUDENT_ID", nullable = true)
	private Student student;

	//@NotNull
	@ManyToOne
	@JoinColumn(name = "CLASS_ID", nullable = true)
	private LevelClass levelClass;

	//@NotNull
	@ManyToOne
	@JoinColumn(name = "SCHOOLYEAR_ID", nullable = true)
	private SchoolYear schoolYear;
	
	
	public SchoolYear getSchoolYear() {
		return schoolYear;
	}

	public void setSchoolYear(SchoolYear schoolYear) {
		this.schoolYear = schoolYear;
	}

	@Override
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getComments() {
		return comments;
	}

	public void setComments(String comments) {
		this.comments = comments;
	}

	public Date getEnrollmentDate() {
		return enrollmentDate;
	}

	public void setEnrollmentDate(Date enrollmentDate) {
		this.enrollmentDate = enrollmentDate;
	}

	public Student getStudent() {
		return student;
	}

	public void setStudent(Student student) {
		this.student = student;
	}

	public LevelClass getLevelClass() {
		return levelClass;
	}

	public void setLevelClass(LevelClass levelClass) {
		this.levelClass = levelClass;
	}

}
