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
@Table(name="EXAMPTION")
public class Exemption extends BaseEntity {
	
	@Id
	@Column(name="EXAMPTION_ID")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@Column(name="COMMENTS")
	private String comments;

	//@NotNull
	@ManyToOne
	@JoinColumn(name = "STUDENT_ID", nullable = true)
	private Student student;

	//@NotNull
	@ManyToOne
	@JoinColumn(name = "COURSE_ID", nullable = true)
	private Course course;

	@Column(name="EXPT_REASON_ID")
	private Integer exemptionReasonId;

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

	public Student getStudent() {
		return student;
	}

	public void setStudent(Student student) {
		this.student = student;
	}

	public Course getCourse() {
		return course;
	}

	public void setCourse(Course course) {
		this.course = course;
	}

	public Integer getExemptionReasonId() {
		return exemptionReasonId;
	}

	public void setExemptionReasonId(Integer exemptionReasonId) {
		this.exemptionReasonId = exemptionReasonId;
	}
}
