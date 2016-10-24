package com.esoft.ischool.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;

@Entity
@Table(name="STUDENT_TUITION")
public class StudentTuition extends BaseEntity {
	
	@Id
	@Column(name="STUDENT_TUITION_ID")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	//@NotNull
	@ManyToOne
	@JoinColumn(name = "STUDENT_ID", nullable = true)
	private Student student;


	@OneToOne
	@JoinColumn(name = "TUITION_ID", nullable = true)
	private Tuition tuition;


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


	public Tuition getTuition() {
		return tuition;
	}


	public void setTuition(Tuition tuition) {
		this.tuition = tuition;
	}

}
