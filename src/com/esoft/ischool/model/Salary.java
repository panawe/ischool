package com.esoft.ischool.model;

import java.math.BigDecimal;
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
@Table(name="SALARY")
public class Salary extends BaseEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "SALARY_ID", unique = true, nullable = false)
	private Long id;

	//@NotNull
	@ManyToOne
	@JoinColumn(name = "TEACHER_ID")
	private Teacher teacher;

	//@NotNull
	@ManyToOne
	@JoinColumn(name = "PAY_TYPE_ID")
	private PayType payType;

	@Column(name="COMMENT")
	private String comment;

	@Column(name = "EFFECTIVE_DATE")
	private Date effectiveDate;
	
	@NotNull
	@Range(min=1, max=999999999)
	private BigDecimal amount;
	
	@Override
	public Long getId() {return id;}
	public void setId(Long id) {this.id = id;}
	public Teacher getTeacher() {
		return teacher;
	}
	public void setTeacher(Teacher teacher) {
		this.teacher = teacher;
	}
	public PayType getPayType() {
		return payType;
	}
	public void setPayType(PayType payType) {
		this.payType = payType;
	}
	public String getComment() {
		return comment;
	}
	public void setComment(String comment) {
		this.comment = comment;
	}
	public Date getEffectiveDate() {
		return effectiveDate;
	}
	public void setEffectiveDate(Date effectiveDate) {
		this.effectiveDate = effectiveDate;
	}
	public BigDecimal getAmount() {
		return amount;
	}
	public void setAmount(BigDecimal amount) {
		this.amount = amount;
	}
}
