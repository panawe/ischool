package com.esoft.ischool.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity

@Table(name="SUBJECT")
public class Subject extends BaseEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name="SUBJECT_ID")
	private Long id;
	
	@Column(name="NAME")
	private String name;
	
	public Subject(){}
	public Subject(Subject sub) {
		// TODO Auto-generated constructor stub
		this.name=sub.getName();
	}

	@Override
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}


	public String getName() {
		return name;
	}


	public void setName(String name) {
		this.name = name;
	}

	@Override
	public String toString() {
		return "Subject [name=" + name + ", subjectId=" + id
				+ ", getCreateDate()=" + getCreateDate() + ", getModDate()=" + getModDate()
				+ ", getModifiedBy()=" + getModifiedBy() + "]";
	}

}
