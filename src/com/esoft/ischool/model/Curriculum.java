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
@Table(name="CURRICULUM")
public class Curriculum extends BaseEntity {
	
	@Id
	@Column(name="CURRICULUM_ID")
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

	@Column(name = "WEEK_NBR")
	private int weekNumber;
	
	@Column(name = "WEEK_START_DATE")
	private Date weekStartDate;
	
	@Column(name = "OBJECTIVE")
	private String objective;
	
	@Override
	public Long getId() {
		return id;
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

	public int getWeekNumber() {
		return weekNumber;
	}

	public void setWeekNumber(int weekNumber) {
		this.weekNumber = weekNumber;
	}

	public Date getWeekStartDate() {
		return weekStartDate;
	}

	public void setWeekStartDate(Date weekStartDate) {
		this.weekStartDate = weekStartDate;
	}

	public String getObjective() {
		return objective;
	}

	public void setObjective(String objective) {
		this.objective = objective;
	}
	
	@Transient
	public String getShortObjective() {
		return objective != null && objective.length() > 200 ? Utils.truncateHTML(objective,200,null): objective;
	}

	@Transient
	public boolean getShowObjectiveLink() {
		return objective != null && objective.length() > 200 ? true : false;
	}

}
