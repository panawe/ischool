package com.esoft.ischool.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="ROLE")
public class Role extends BaseEntity {

	@Id
	@Column(name = "ROLE_ID")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	//@NotNull
	//@Length(max = 50)
	private String name;
	
	//@NotNull
	//@Length(max = 200)
	private String description;

	public Role(){}
	public Role(Role role) {
		// TODO Auto-generated constructor stub
		this.name=role.getName();
		this.description=role.getDescription();
	}
	@Override
	public Long getId() {return id;}
	public void setId(Long id) {this.id = id;}

	public String getName() {return name;}
	public void setName(String name) {this.name = name;}
	
	public String getDescription() { return description; }
	public void setDescription(String description) { this.description = description; }
}
