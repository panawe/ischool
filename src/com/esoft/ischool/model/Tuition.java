package com.esoft.ischool.model;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import javax.persistence.*;


@Entity
@Table(name="TUITION")
public class Tuition extends BaseEntity implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -586002799404191965L;

	@Id
	@Column(name = "TUITION_ID")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	//@NotNull
	private String description;

	//@NotNull
	@Column(name="DUE_DATE")
	private Date dueDate;
	
	@Column(name="REMIND_DATE")
	private Date remindDate;

	@NotNull
	private Double amount=0.0;

	@Transient
	private Double paid=0.0;
	
	@Transient
	private Double rebate=0.0;
	
	@Transient 
	private Double balance = 0.0;
	
	@Transient
	private String comment;
	
	@Transient 
	private Long enrollmentId;
	
	@Transient 
	private Long studentId;
	
	@Transient
	List<Payment> payments;
	
	@Transient
	private boolean showDetails;
	
	@Transient
	private boolean hasPastDueAmount;
	
	//@NotNull
	@ManyToOne
	@JoinColumn(name = "SCHOOLYEAR_ID", nullable = true)
	private SchoolYear schoolYear;
	
	//@NotNull
	@ManyToOne
	@JoinColumn(name = "TUITION_TYPE_ID", nullable = true)
	private TuitionType tuitionType;

	
	public Date getRemindDate() {
		return remindDate;
	}

	public void setRemindDate(Date remindDate) {
		this.remindDate = remindDate;
	}

	public TuitionType getTuitionType() {
		return tuitionType;
	}

	public void setTuitionType(TuitionType tuitionType) {
		this.tuitionType = tuitionType;
	}

	public String getComment() {
		return comment;
	}

	public Double getRebate() {
		return rebate;
	}

	public void setRebate(Double rebate) {
		this.rebate = rebate;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}

	public Double getPaid() {
		return paid;
	}

	public void setPaid(Double paid) {
		this.paid = paid;
	}

	@Override
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Date getDueDate() {
		return dueDate;
	}

	public void setDueDate(Date dueDate) {
		this.dueDate = dueDate;
	}

	public Double getAmount() {
		return amount;
	}

	public void setAmount(Double amount) {
		this.amount = amount;
	}

	public Double getBalance() {
		return balance;
	}

	public void setBalance(Double balance) {
		this.balance = balance;
	}

	public Long getEnrollmentId() {
		return enrollmentId;
	}

	public void setEnrollmentId(Long enrollmentId) {
		this.enrollmentId = enrollmentId;
	}

	public Long getStudentId() {
		return studentId;
	}

	public void setStudentId(Long studentId) {
		this.studentId = studentId;
	}

	public SchoolYear getSchoolYear() {
		return schoolYear;
	}

	public void setSchoolYear(SchoolYear schoolYear) {
		this.schoolYear = schoolYear;
	}
	
	public List<Payment> getPayments() {
		return payments;
	}

	public void setPayments(List<Payment> payments) {
		this.payments = payments;
	}

	public boolean isShowDetails() {
		return showDetails;
	}

	public void setShowDetails(boolean showDetails) {
		this.showDetails = showDetails;
	}

	public boolean isHasPastDueAmount() {
		return hasPastDueAmount;
	}

	public void setHasPastDueAmount(boolean hasPastDueAmount) {
		this.hasPastDueAmount = hasPastDueAmount;
	}

	@Override
	public String toString(){
		return  (schoolYear==null?"2014-2015":schoolYear.getYear())+"|"+description + "|" + amount+"|"+id;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((description == null) ? 0 : description.hashCode());
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		result = prime * result
				+ ((schoolYear == null) ? 0 : schoolYear.hashCode());
		result = prime * result
				+ ((tuitionType == null) ? 0 : tuitionType.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (!(obj instanceof Tuition))
			return false;
		Tuition other = (Tuition) obj;
			return getId().equals(other.getId());
		}
	
}
