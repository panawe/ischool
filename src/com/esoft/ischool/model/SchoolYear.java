
package com.esoft.ischool.model;

import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="SCHOOLYEAR")
public class SchoolYear extends BaseEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "SCHOOLYEAR_ID")
	private Long id;
	
	//@Length(max = 100)
	@Column(name = "DESCRIPTION")
	private String description;

	@Column(name = "NAME")
	private String year="";

	@Column(name = "BEGIN_DATE")
	private Date beginDate;

	@Column(name = "END_DATE")
	private Date endDate;

	public SchoolYear(){};
	public SchoolYear(SchoolYear sy) {
		// TODO Auto-generated constructor stub
		this.description=sy.getDescription();
		this.year=sy.getYear();
		this.beginDate=sy.getBeginDate();
		this.endDate=sy.getEndDate();
	}

	@Override
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}


	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}


	public String getYear() {
		return year;
	}

	public void setYear(String year) {
		this.year = year;
	}

	public Date getBeginDate() {
		return beginDate;
	}

	public void setBeginDate(Date beginDate) {
		this.beginDate = beginDate;
	}

	public Date getEndDate() {
		return endDate;
	}

	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}
	
	
}
