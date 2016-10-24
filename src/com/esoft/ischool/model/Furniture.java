package com.esoft.ischool.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.esoft.ischool.util.Utils;

@Entity
@Table(name="FURNITURE")
public class Furniture extends BaseEntity {
	
	@Id
	@Column(name="FURNITURE_ID")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	//@NotNull
	@ManyToOne
	@JoinColumn(name = "SUBJECT_ID", nullable = true)
	private Subject subject;

	//@NotNull
	@ManyToOne
	@JoinColumn(name = "SCHOOLYEAR_ID", nullable = true)
	private SchoolYear schoolYear;

	//@NotNull
	@ManyToOne
	@JoinColumn(name = "LEVEL_ID", nullable = true)
	private Level level;

	
	@Column(name = "DESCRIPTION")
	private String description;
	
	@Override
	public Long getId() {
		return id;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Subject getSubject() {
		return subject;
	}

	public void setSubject(Subject subject) {
		this.subject = subject;
	}

	public SchoolYear getSchoolYear() {
		return schoolYear;
	}

	public void setSchoolYear(SchoolYear schoolYear) {
		this.schoolYear = schoolYear;
	}

	public Level getLevel() {
		return level;
	}

	public void setLevel(Level level) {
		this.level = level;
	}

}
