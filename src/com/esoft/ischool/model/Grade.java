package com.esoft.ischool.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="GRADE")
public class Grade extends BaseEntity {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name="GRADE_ID")
	private Long id;
	
	//@NotNull
	@Column(name="NAME")
	private String name;
	
	//@NotNull
	@Column(name="BEGIN_RANGE")
	private Double beginRange;
	
	//@NotNull
	@Column(name ="END_RANGE")
	private Double endRange;
	
	public Grade(){}
	
	public Grade(Grade grd) {
		// TODO Auto-generated constructor stub
		this.name=grd.getName();
		this.beginRange=grd.getBeginRange();
		this.endRange=grd.getEndRange();
	}

	public Double getBeginRange() {
		return beginRange;
	}
	public void setBeginRange(Double beginRange) {
		this.beginRange = beginRange;
	}
	public Double getEndRange() {
		return endRange;
	}
	public void setEndRange(Double endRange) {
		this.endRange = endRange;
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
		return  name;
	}
	

}
