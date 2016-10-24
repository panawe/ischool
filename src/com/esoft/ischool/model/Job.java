package com.esoft.ischool.model;

import javax.persistence.*;


@Table(name="JOB")
@Entity
public class Job extends BaseEntity{

	@Column(name="JOB_ID")
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Long id;
	
	//@NotNull
	private Short status;
	
	private String detail;
	
	private String message;

	
	public Short getStatus() {
		return status;
	}


	public void setStatus(Short status) {
		this.status = status;
	}


	public String getDetail() {
		return detail;
	}


	public void setDetail(String detail) {
		this.detail = detail;
	}


	public String getMessage() {
		return message;
	}


	public void setMessage(String message) {
		this.message = message;
	}


	public void setId(Long id) {
		this.id = id;
	}


	@Override
	public Long getId() {
		// TODO Auto-generated method stub
		return id;
	}
	

}
