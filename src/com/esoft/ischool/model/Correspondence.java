package com.esoft.ischool.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity()
@Table(name = "CORRESPONDANCE")
public class Correspondence extends BaseEntity {

	@Id
	@Column(name = "CORRESPONDANCE_ID")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	//@NotNull
	@Column(name = "DESCRIPTION")
	private String description;

	//@NotNull
	//@Length(max = 250)
	@Column(name = "SUBJECT")
	private String subject="";
	
	//@NotNull
	@Column(name = "CORRESPONDANCE_DATE")
	private Date correspondenceDate = new Date();
	
	@Column(name="SENT")
	private Short sent = 0;
	
	public Short getSent() {
		return sent;
	}

	public void setSent(Short sent) {
		this.sent = sent;
	}

	@Override
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getSubject() {
		return subject;
	}

	public void setSubject(String subject) {
		this.subject = subject;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Date getCorrespondenceDate() {
		return correspondenceDate;
	}

	public void setCorrespondenceDate(Date correspondenceDate) {
		this.correspondenceDate = correspondenceDate;
	}
}
