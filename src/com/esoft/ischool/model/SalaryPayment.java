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
@Table(name="SALARY_PAYMENT")
public class SalaryPayment extends BaseEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "SALARY_ID", unique = true, nullable = false)
	private Long id;

	//@NotNull
	@ManyToOne
	@JoinColumn(name = "TEACHER_ID")
	private Teacher teacher;

	private String comment;

	@Column(name = "PAYMENT_DATE")
	private Date paymentDate;
	
	@Column(name = "PAYROLL_BEGIN_DATE")
	private Date payrollBeginDate;

	@Column(name = "PAYROLL_END_DATE")
	private Date payrollEndDate;

	//@NotNull
	//@Range(min=1, max=999999999)
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
	public String getComment() {
		return comment;
	}
	public void setComment(String comment) {
		this.comment = comment;
	}
	public Date getPaymentDate() {
		return paymentDate;
	}
	public void setPaymentDate(Date paymentDate) {
		this.paymentDate = paymentDate;
	}
	public Date getPayrollBeginDate() {
		return payrollBeginDate;
	}
	public void setPayrollBeginDate(Date payrollBeginDate) {
		this.payrollBeginDate = payrollBeginDate;
	}
	public Date getPayrollEndDate() {
		return payrollEndDate;
	}
	public void setPayrollEndDate(Date payrollEndDate) {
		this.payrollEndDate = payrollEndDate;
	}
	public BigDecimal getAmount() {
		return amount;
	}
	public void setAmount(BigDecimal amount) {
		this.amount = amount;
	}
}
