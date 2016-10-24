package com.esoft.ischool.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import com.esoft.ischool.model.BaseEntity;

@Entity
@Table(name="COURSE_ASSIGNMENT_FILE")
public class CourseAssignmentFile extends BaseEntity{

	@Id
	@Column(name="COURSE_ASSIGNMENT_FILE_ID")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@Column(name="CLASS_ID")
	private Long classId;
	
	@Column(name="SCHOOLYEAR_ID")
	private Long yearId;
	
	@Column(name="SUBJECT_ID")
	private Long subjectId;
	
	@Column(name="EXAM_TYPE_ID")
	private Long examTypeId;

	@Column(name="EXAM_DATE")
	private Date examDate;

	@Column(name="DUE_DATE")
	private Date returnDate;

	@Column(name="ASSIGNMENT_FILE_ID")
	private Long assignmentFileId;
	
	public CourseAssignmentFile() {
		// TODO Auto-generated constructor stub
	}
	
	public CourseAssignmentFile(Long classId, Long yearId, Long subjectId, Long examTypeId, 
			Date examDate, Date returnDate, Long assignmentFileId) {
		setClassId(classId);
		setYearId(yearId);
		setSubjectId(subjectId);
		setExamTypeId(examTypeId);
		setExamDate(examDate);
		setReturnDate(returnDate);
		setAssignmentFileId(assignmentFileId);
	}
	
	@Override
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getClassId() {
		return classId;
	}

	public void setClassId(Long classId) {
		this.classId = classId;
	}

	public Long getYearId() {
		return yearId;
	}

	public void setYearId(Long yearId) {
		this.yearId = yearId;
	}

	public Long getSubjectId() {
		return subjectId;
	}

	public void setSubjectId(Long subjectId) {
		this.subjectId = subjectId;
	}

	public Long getExamTypeId() {
		return examTypeId;
	}

	public void setExamTypeId(Long examTypeId) {
		this.examTypeId = examTypeId;
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

	public Long getAssignmentFileId() {
		return assignmentFileId;
	}

	public void setAssignmentFileId(Long assignmentFileId) {
		this.assignmentFileId = assignmentFileId;
	}
}
