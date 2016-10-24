package com.esoft.ischool.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="CONFIGURATION")
public class Configuration extends BaseEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "CONFIG_ID")
	private Long id;


	//@Length(max = 45)
	@Column(name = "GROUP_NAME")
	private String groupName;

	//@NotNull
	//@Length(max = 50)
	private String name;

	//@Length(max = 200)
	private String value;
	
	//@Length(max=200)
	private String description;

	
	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Configuration() {
	}

	public Configuration(Configuration conf) {
		// TODO Auto-generated constructor stub
		this.name=conf.getName();
		this.value=conf.getValue();
		this.description=conf.getDescription();
		this.groupName=conf.getGroupName();
	}

		@Override
		public Long getId() {
		return id;
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

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

		@Override
	public String toString() {
		return name;
	}

	public String getGroupName() {
		return groupName;
	}

	public void setGroupName(String groupName) {
		this.groupName = groupName;
	}
}
