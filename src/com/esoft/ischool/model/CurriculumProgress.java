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

@Entity
@Table(name="CURRICULUM_PROGRESS")
public class CurriculumProgress extends BaseEntity {
	
	@Id
	@Column(name="PROGRESS_ID")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	//@NotNull
	@ManyToOne
	@JoinColumn(name = "CURRICULUM_ID", nullable = true)
	private Curriculum curriculum;

	//@NotNull
	@ManyToOne
	@JoinColumn(name = "CLASS_ID", nullable = true)
	private LevelClass levelClass;

	//@NotNull
	@ManyToOne
	@JoinColumn(name = "TEACHER_ID", nullable = true)
	private Teacher teacher;

	//@NotNull
	@Column(name = "STATUS")
	private int status;
	
	@Column(name = "COMMENT")
	private String comment;
	
	@Transient
	private String statusDescription;
	
	@Override
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Curriculum getCurriculum() {
		return curriculum;
	}

	public void setCurriculum(Curriculum curriculum) {
		this.curriculum = curriculum;
	}

	public LevelClass getLevelClass() {
		return levelClass;
	}

	public void setLevelClass(LevelClass levelClass) {
		this.levelClass = levelClass;
	}

	public Teacher getTeacher() {
		return teacher;
	}

	public void setTeacher(Teacher teacher) {
		this.teacher = teacher;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}

	public String getStatusDescription() {
		return statusDescription;
	}

	public void setStatusDescription(String statusDescription) {
		this.statusDescription = statusDescription;
	}
	
	@Transient
	public String getShortComment() {
		return comment != null && comment.length() > 200 ? comment.substring(0, 200) + " ... " : comment;
	}

	@Transient
	public boolean getShowCommentLink() {
		return comment != null && comment.length() > 200 ? true : false;
	}
}
