package com.esoft.ischool.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="RATING")
public class Rating extends BaseEntity {
	
	@Id
	@Column(name="RATING_ID")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@Column(name="NAME")
	private String name;

	public Rating(){}

	public Rating(Rating rat) {
		// TODO Auto-generated constructor stub
		this.name=rat.getName();
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
		return "Rating [levelId=" + id + ", name=" + name
				+ ", getCreateDate()=" + getCreateDate() + ", getModDate()=" + getModDate()
				+ ", getModifiedBy()=" + getModifiedBy() + "]";
	}
	

}
