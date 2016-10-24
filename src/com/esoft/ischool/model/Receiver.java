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
@Table(name = "RECEIVER")
public class Receiver extends BaseEntity {

	@Id
	@Column(name = "RECEIVER_ID")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@ManyToOne
	@JoinColumn(name = "STUDENT_ID", nullable = true)
	private Student student;

	@ManyToOne
	@JoinColumn(name = "TEACHER_ID", nullable = true)
	private Teacher teacher;

	@ManyToOne
	@JoinColumn(name = "CORRESPONDANCE_ID", nullable = true)
	private Correspondence correspondence = new Correspondence();

	@Override
	public Long getId() {
		return id;
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

	public Teacher getTeacher() {
		return teacher;
	}

	public void setTeacher(Teacher teacher) {
		this.teacher = teacher;
	}

	public Correspondence getCorrespondence() {
		return correspondence;
	}

	public void setCorrespondence(Correspondence correspondence) {
		this.correspondence = correspondence;
	}
}
