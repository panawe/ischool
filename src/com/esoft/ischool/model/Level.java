package com.esoft.ischool.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="LEVELS")
public class Level extends BaseEntity {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name="LEVEL_ID")
	private Long id;
	
	@Column(name="NAME")
	private String name;

	public Level(){}
	
	public Level(Long id) {
		this.id = id;
	}

	public Level(Level sl) {
		// TODO Auto-generated constructor stub
		this.name=sl.getName();
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
		return "Level [levelId=" + id + ", name=" + name
				+ ", getCreateDate()=" + getCreateDate() + ", getModDate()=" + getModDate()
				+ ", getModifiedBy()=" + getModifiedBy() + "]";
	}
	

}
