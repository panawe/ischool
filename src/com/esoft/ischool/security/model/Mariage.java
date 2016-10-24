package com.esoft.ischool.security.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.esoft.ischool.model.BaseEntity;

@Entity
@Table(name = "MARIAGE")
public class Mariage extends BaseEntity {

	@Id
	@Column(name = "MARIAGE_ID")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@ManyToOne
	@JoinColumn(name = "HUSBAND")
	private User husband;

	@ManyToOne
	@JoinColumn(name = "WIFE")
	private User wife;
	
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "MM/dd/yyyy")
	@Column(name = "MARIAGE_DATE")
	private Date mariageDate;	

	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "MM/dd/yyyy")
	@Column(name = "DIVORCE_DATE")
	private Date divorceDate;	

	@Override
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public User getHusband() {
		return husband;
	}

	public void setHusband(User husband) {
		this.husband = husband;
	}

	public User getWife() {
		return wife;
	}

	public void setWife(User wife) {
		this.wife = wife;
	}

	public Date getMariageDate() {
		return mariageDate;
	}

	public void setMariageDate(Date mariageDate) {
		this.mariageDate = mariageDate;
	}

	public Date getDivorceDate() {
		return divorceDate;
	}

	public void setDivorceDate(Date divorceDate) {
		this.divorceDate = divorceDate;
	}

 
}
