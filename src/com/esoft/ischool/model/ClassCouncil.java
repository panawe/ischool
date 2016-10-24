package com.esoft.ischool.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.validator.NotNull;

@Entity

@Table(name="CLASS_COUNCIL")
public class ClassCouncil extends BaseEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name="CLASS_COUNCIL_ID")
	private Long id;
	
	@NotNull
	@Column(name="DECISIONS")
	private String decisions;
	
	@NotNull
	@Column(name="COUNCIL_DATE")
	private Date councilDate;
	
	public String getDecisions() {
		return decisions;
	}

	public void setDecisions(String decisions) {
		this.decisions = decisions;
	}

	public Date getCouncilDate() {
		return councilDate;
	}

	public void setCouncilDate(Date councilDate) {
		this.councilDate = councilDate;
	}

	@Override
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	@Override
	public String toString() {
		return "ClassCouncil [id=" + id + ", decisions=" + decisions
				+ ", councilDate=" + councilDate + "]";
	}

}
