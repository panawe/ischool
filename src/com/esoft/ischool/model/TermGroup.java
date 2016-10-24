package com.esoft.ischool.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

@Entity
@Table(name="TERM_GROUP")
public class TermGroup extends BaseEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "TERM_GROUP_ID")
	private Long id;
	
	//@NotNull
	//@Length(max = 50)
	@Column(name = "NAME")
	private String name;
	
	public TermGroup(){}
	public TermGroup(String name) {
		// TODO Auto-generated constructor stub
		this.name=name;
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
