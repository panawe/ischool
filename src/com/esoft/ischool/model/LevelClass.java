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
@Table(name="CLASS")
public class LevelClass extends BaseEntity {
	
	@Id
	@Column(name="CLASS_ID")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@Column(name="NAME")
	private String name;

	//@NotNull
	@ManyToOne
	@JoinColumn(name = "LEVEL_ID", nullable = true)
	private Level level;


	@OneToOne
	@JoinColumn(name = "RESPONSIBLE_TEACHER_ID", nullable = true)
	private Teacher responsibleTeacher;

	//@NotNull
	@Column(name ="NBR_STUDENTS")
	private Integer nbrStudents;
	
	//@NotNull
	@Column(name="CAPACITY")
	private Integer capacity;
	
	
	public Integer getCapacity() {
		return capacity;
	}
	public void setCapacity(Integer capacity) {
		this.capacity = capacity;
	}
	public LevelClass(){}
	public LevelClass(LevelClass lc, Level level2) {
		this.name=lc.getName();
		this.nbrStudents=lc.getNbrStudents();
		this.level=level2;
	}

	@Override
	public Long getId() {
		return id;
	}

	public Integer getNbrStudents() {
		return nbrStudents;
	}

	public void setNbrStudents(Integer nbrStudents) {
		this.nbrStudents = nbrStudents;
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

	public Level getLevel() {
		return level;
	}

	public void setLevel(Level level) {
		this.level = level;
	}
	
	public Teacher getResponsibleTeacher() {
		return responsibleTeacher;
	}
	
	public void setResponsibleTeacher(Teacher responsibleTeacher) {
		this.responsibleTeacher = responsibleTeacher;
	}
}
