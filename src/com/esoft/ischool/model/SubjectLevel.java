package com.esoft.ischool.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity

@Table(name="SUBJECT_LEVEL")
public class SubjectLevel extends BaseEntity{

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name="SL_ID")
	private Long id;
	
	@ManyToOne
	@JoinColumn(name="SUBJECT_ID")
	private Subject subject;
	
	@ManyToOne
	@JoinColumn(name="LEVEL_ID")
	private Level level;

	public Subject getSubject() {
		return subject;
	}

	public void setSubject(Subject subject) {
		this.subject = subject;
	}

	public Level getLevel() {
		return level;
	}

	public void setLevel(Level level) {
		this.level = level;
	}

	@Override
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	@Override
	public String toString() {
		return "SubjectLevel [level=" + level + ", slId=" + id + ", subject="
				+ subject + ", getCreateDate()=" + getCreateDate() + ", getModDate()="
				+ getModDate() + ", getModifiedBy()=" + getModifiedBy() + "]";
	}
	
	
	
}
