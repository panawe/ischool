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
@Table(name="AUDITS")
public class Audits extends BaseEntity{

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name="AUDITS_ID")
	private Long id;
	
	@ManyToOne
	@JoinColumn(name="SCHOOL_ID")
	private School school;
	
	@Column(name="DETAILS")
	private String details;

	@Override
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	@Override
	public School getSchool() {
		return school;
	}

	@Override
	public void setSchool(School school) {
		this.school = school;
	}

	public String getDetails() {
		return details;
	}

	public void setDetails(String details) {
		this.details = details;
	}
}
