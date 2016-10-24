package com.esoft.ischool.model;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

@Entity
@Table(name="RESULT_TYPE")
public class ResultType extends BaseEntity{

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name="RESULT_TYPE_ID")
	private Long id;
	
	@Column(name="NAME")
	private String name;
	
	@Column(name ="DESCRIPTION")
	private String description;
	
	@Column(name="IS_CUSTOM")
	private Short isCustom;
	
	@OneToMany
	@JoinColumn(name="RESULT_TYPE_ID")
	@Fetch(FetchMode.JOIN)
	private List<ResultRange> resultRange;
	
	public ResultType(){}

	public ResultType(ResultType rat) {
		// TODO Auto-generated constructor stub
		this.name= rat.getName();
		this.description= rat.getDescription();
		this.isCustom=rat.getIsCustom();
		
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


	public String getDescription() {
		return description;
	}


	public void setDescription(String description) {
		this.description = description;
	}


	public Short getIsCustom() {
		return isCustom;
	}


	public void setIsCustom(Short isCustom) {
		this.isCustom = isCustom;
	}



	public List<ResultRange> getResultRange() {
		return resultRange;
	}


	public void setResultRange(List<ResultRange> resultRange) {
		this.resultRange = resultRange;
	}


	@Override
	public String toString() {
		return "ResultType [description=" + description + ", isCustom="
				+ isCustom + ", name=" + name + ", resultRange=" + resultRange
				+ ", resultTypeId=" + id + ", getCreateDate()="
				+ getCreateDate()
				+ ", getModDate()=" + getModDate() + ", getModifiedBy()="
				+ getModifiedBy() + "]";
	}
}
