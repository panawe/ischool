package com.esoft.ischool.model;

import java.util.Date;

import javax.persistence.*;

@Entity
@Table (name="PAYMENT")
public class Payment extends BaseEntity{

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name ="PAYMENT_ID")
	private Long id;
	
	@Column(name="PAYMENT_DATE")
	private Date paymentDate;
	
	@Column(name="AMOUNT")
	private Double amount;
	
	@Column (name ="REBATE")
	private Double rebate=0D;
	
	private String comment;
	
	//@NotNull
	@ManyToOne
	@JoinColumn(name = "TUITION_ID", nullable = true)
	private Tuition tuition;
	
	//@NotNull
	@ManyToOne
	@JoinColumn(name = "ENROLLMENT_ID", nullable = true)
	private StudentEnrollment studentEnrollment;

	@Transient
	private Long tuitionId;
	
	@Transient
	private String tuitionDescription;
	
	@Transient
	private Date tuitionDueDate;
	
	@Transient 
	private Double tuitionAmount;
	
	@Transient
	private Long tuitionTypeId;
	
	@Transient 
	private String tuitionTypeName;
	
	@Transient 
	private Long studentId;
	
	@Transient
	private Long enrollmentId;
	
	
	@Override
	public Long getId() {
		return id;
	}

	public Double getRebate() {
		return rebate;
	}

	public void setRebate(Double rebate) {
		this.rebate = rebate;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Date getPaymentDate() {
		return paymentDate;
	}

	public void setPaymentDate(Date paymentDate) {
		this.paymentDate = paymentDate;
	}

	public Double getAmount() {
		return amount;
	}

	public void setAmount(Double amount) {
		this.amount = amount;
	}

	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}

	public Tuition getTuition() {
		return tuition;
	}

	public void setTuition(Tuition tuition) {
		this.tuition = tuition;
	}

	public StudentEnrollment getStudentEnrollment() {
		return studentEnrollment;
	}

	public void setStudentEnrollment(StudentEnrollment studentEnrollment) {
		this.studentEnrollment = studentEnrollment;
	}

	public Long getTuitionId() {
		return tuitionId;
	}

	public void setTuitionId(Long tuitionId) {
		this.tuitionId = tuitionId;
	}

	public String getTuitionDescription() {
		return tuitionDescription;
	}

	public void setTuitionDescription(String tuitionDescription) {
		this.tuitionDescription = tuitionDescription;
	}

	public Date getTuitionDueDate() {
		return tuitionDueDate;
	}

	public void setTuitionDueDate(Date tuitionDueDate) {
		this.tuitionDueDate = tuitionDueDate;
	}

	public Double getTuitionAmount() {
		return tuitionAmount;
	}

	public void setTuitionAmount(Double tuitionAmount) {
		this.tuitionAmount = tuitionAmount;
	}

	public Long getTuitionTypeId() {
		return tuitionTypeId;
	}

	public void setTuitionTypeId(Long tuitionTypeId) {
		this.tuitionTypeId = tuitionTypeId;
	}

	public String getTuitionTypeName() {
		return tuitionTypeName;
	}

	public void setTuitionTypeName(String tuitionTypeName) {
		this.tuitionTypeName = tuitionTypeName;
	}

	public Long getStudentId() {
		return studentId;
	}

	public void setStudentId(Long studentId) {
		this.studentId = studentId;
	}

	public Long getEnrollmentId() {
		return enrollmentId;
	}

	public void setEnrollmentId(Long enrollmentId) {
		this.enrollmentId = enrollmentId;
	}	
}
