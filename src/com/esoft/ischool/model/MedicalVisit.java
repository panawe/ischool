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
import com.esoft.ischool.security.model.User;

@Entity
@Table(name="MEDICAL_VISIT")
public class MedicalVisit extends BaseEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name="MEDICAL_VISIT_ID")
	private Long id;
	
	//@NotNull
	@Column(name="VISIT_DATE")
	private Date visitDate;
	
	@Column(name="PRESCRIPTION")
	private String prescription;
	
	//@NotNull
	@Column(name="DIAGNOSIS")
	private String diagnosis;
	
	//@NotNull
	@Column(name="CONSULTED_BY")
	private String consultedBy;

	//@NotNull
	@ManyToOne
	@JoinColumn(name = "USER_ID")
	private User patient;

	@ManyToOne
	@JoinColumn(name = "DISEASE_ID", nullable = true)
	private Disease disease;

	public Disease getDisease() {
		return disease;
	}

	public void setDisease(Disease disease) {
		this.disease = disease;
	}


	public Date getVisitDate() {
		return visitDate;
	}

	public void setVisitDate(Date visitDate) {
		this.visitDate = visitDate;
	}

	public String getPrescription() {
		return prescription;
	}

	public void setPrescription(String prescription) {
		this.prescription = prescription;
	}

	public String getDiagnosis() {
		return diagnosis;
	}

	public void setDiagnosis(String diagnosis) {
		this.diagnosis = diagnosis;
	}

	public String getConsultedBy() {
		return consultedBy;
	}

	public void setConsultedBy(String consultedBy) {
		this.consultedBy = consultedBy;
	}

	public User getPatient() {
		return patient;
	}

	public void setPatient(User patient) {
		this.patient = patient;
	}

	@Override
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

}
