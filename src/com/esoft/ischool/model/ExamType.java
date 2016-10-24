package com.esoft.ischool.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="EXAM_TYPE")
public class ExamType extends BaseEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "EXAM_TYPE_ID")
	private Long id;

	//@NotNull
	//@Length(max = 50)
	@Column(name = "NAME")
	private String name;
	public ExamType(){}
	public ExamType(ExamType et) {
		// TODO Auto-generated constructor stub
		this.name=et.getName();
		
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
