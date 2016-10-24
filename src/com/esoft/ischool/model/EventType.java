package com.esoft.ischool.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;


@Entity
@Table(name = "EVENT_TYPE")
public class EventType extends BaseEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "EVENT_TYPE_ID")
	private Long id;
	
	//@NotNull
	//@Length(max = 50)
	@Column(name = "NAME")
	private String name;

	//@Length(max = 1)
	@Column(name = "SHOW_ON_GRADE_REPORT")
	private String showOnGradeReport;	
	
	public EventType(){}
	public EventType(EventType sl) {
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
	public String getShowOnGradeReport() {
		return showOnGradeReport;
	}
	public void setShowOnGradeReport(String showOnGradeReport) {
		this.showOnGradeReport = showOnGradeReport;
	}
}
