package com.esoft.ischool.model;

import java.util.Date;

public class TuitionEnrollment {
	
	private Tuition tuition;
	private StudentEnrollment enrollment;
	private Double paid;	
	private Double rebate;
	private String comment;
	private String receivedBy;
	private Date payDate;
	private Integer id;

	public Double getRebate() {
		return rebate;
	}
	public void setRebate(Double rebate) {
		this.rebate = rebate;
	}
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getComment() {
		return comment;
	}
	public void setComment(String comment) {
		this.comment = comment;
	}
	public String getReceivedBy() {
		return receivedBy;
	}
	public void setReceivedBy(String receivedBy) {
		this.receivedBy = receivedBy;
	}
	public Date getPayDate() {
		return payDate;
	}
	public void setPayDate(Date payDate) {
		this.payDate = payDate;
	}
	public Tuition getTuition() {
		return tuition;
	}
	public void setTuition(Tuition tuition) {
		this.tuition = tuition;
	}
	public StudentEnrollment getEnrollment() {
		return enrollment;
	}
	public void setEnrollment(StudentEnrollment enrollment) {
		this.enrollment = enrollment;
	}
	public Double getPaid() {
		return paid;
	}
	public void setPaid(Double paid) {
		this.paid = paid;
	}
}
