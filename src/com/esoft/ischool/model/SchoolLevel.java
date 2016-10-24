package com.esoft.ischool.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "SCHOOL_LEVEL")
public class SchoolLevel extends BaseEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "SCH_LEVEL_ID", unique = true, nullable = false)
	private Long id;

	@Column(name = "NAME")
	private String name;

	public SchoolLevel(){}
	
	public SchoolLevel(SchoolLevel sl) {
		this.name=sl.name;
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
