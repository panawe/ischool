package com.esoft.ischool.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "SCHOOL_RELIGION")
public class SchoolReligion extends BaseEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "SCH_RELIGION_ID")
	private Long id;

	@Column(name = "NAME")
	private String name;

	public SchoolReligion(){}
	public SchoolReligion(SchoolReligion sr) {
		// TODO Auto-generated constructor stub
		this.name=sr.getName();
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
}
