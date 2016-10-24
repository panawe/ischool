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

@Entity
@Table(name="SCHOOLING")
public class Schooling extends BaseEntity {

	@Id
	@Column(name = "SCHOOLING_ID")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

 
	@ManyToOne
	@JoinColumn(name = "STUDENT_ID", nullable = true)
	private Student student;
	
 
	@ManyToOne
	@JoinColumn(name = "TEACHER_ID", nullable = true)
	private Teacher teacher;

	//@NotNull
	@ManyToOne
	@JoinColumn(name = "EVENT_TYPE_ID", nullable = true)
	private EventType eventType;

	@ManyToOne
	@JoinColumn(name = "SCHOOLYEAR_ID", nullable = true)
	private SchoolYear schoolYear;

	@ManyToOne
	@JoinColumn(name = "TERM_ID", nullable = true)
	private Term term;

	//@Length(max = 5000)
	@Column(name = "DESCRIPTION")
	private String description;

	//@NotNull
	@Column(name = "EVENT_DATE")
	private Date eventDate;

	@Override
	public Long getId() {
		return id;
	}

	public SchoolYear getSchoolYear() {
		return schoolYear;
	}

	public void setSchoolYear(SchoolYear schoolYear) {
		this.schoolYear = schoolYear;
	}

	public Term getTerm() {
		return term;
	}

	public void setTerm(Term term) {
		this.term = term;
	}

	public Teacher getTeacher() {
		return teacher;
	}

	public void setTeacher(Teacher teacher) {
		this.teacher = teacher;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Student getStudent() {
		return student;
	}

	public void setStudent(Student student) {
		this.student = student;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public EventType getEventType() {
		return eventType;
	}

	public void setEventType(EventType eventType) {
		this.eventType = eventType;
	}

	public Date getEventDate() {
		return eventDate;
	}

	public void setEventDate(Date eventDate) {
		this.eventDate = eventDate;
	}

}
