package com.esoft.ischool.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name="MARK")
public class Mark extends BaseEntity {
	
	@Id
	@Column(name="MARK_ID")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	//@NotNull
	@ManyToOne
	@JoinColumn(name = "EXAM_ID")
	private Exam exam;

	//@NotNull
	@ManyToOne
	@JoinColumn(name = "ENROLLMENT_ID")
	private StudentEnrollment studentEnrollment;

	@Column(name="MARK")
	private Double mark;


	@ManyToOne
	@JoinColumn(name = "GRADE_ID")
	private Grade grade;

	@Column(name ="APPROVED_BY")
	private Long approvedBy;

	
	public Long getApprovedBy() {
		return approvedBy;
	}

	public void setApprovedBy(Long approvedBy) {
		this.approvedBy = approvedBy;
	}

	@Override
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}


	public Exam getExam() {
		return exam;
	}

	public void setExam(Exam exam) {
		this.exam = exam;
	}

	public Double getMark() {
		return mark;
	}

	public void setMark(Double mark) {
		this.mark = mark;
	}

	public Grade getGrade() {
		return grade;
	}

	public void setGrade(Grade grade) {
		this.grade = grade;
	}

	public StudentEnrollment getStudentEnrollment() {
		return studentEnrollment;
	}

	public void setStudentEnrollment(StudentEnrollment studentEnrollment) {
		this.studentEnrollment = studentEnrollment;
	}

}
