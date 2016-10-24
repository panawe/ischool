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
@Table(name="PARENT_STUDENT")
public class ParentStudent extends BaseEntity {
	
	@Id
	@Column(name="PARENT_STUDENT_ID")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	//@NotNull
	@ManyToOne
	@JoinColumn(name = "PARENT_ID", nullable = true)
	private Parent parent;

	//@NotNull
	@ManyToOne
	@JoinColumn(name = "STUDENT_ID", nullable = true)
	private Student student;

	
	@Column(name = "PARENT_TYPE_ID", nullable = true)
	private Long parentTypeId;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Parent getParent() {
		return parent;
	}

	public void setParent(Parent parent) {
		this.parent = parent;
	}

	public Student getStudent() {
		return student;
	}

	public void setStudent(Student student) {
		this.student = student;
	}

	public Long getParentTypeId() {
		return parentTypeId;
	}

	public void setParentTypeId(Long parentTypeId) {
		this.parentTypeId = parentTypeId;
	}

	
}
