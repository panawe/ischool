package com.esoft.ischool.model;

import java.util.Date;

public class Relance {
	
	private String message;
	private String studentName;
	private String parentName;
	private String matricule;
	private String classe;
	private String tranche;
	private Double montantPaye=0.0;
	private Double montantRestant=0.0;
	private Double montantDu=0.0;
	private Double remise=0.0;
	private Date dateLimite;

	
	
	public Double getRemise() {
		return remise;
	}
	public void setRemise(Double remise) {
		this.remise = remise;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	public String getStudentName() {
		return studentName;
	}
	public void setStudentName(String studentName) {
		this.studentName = studentName;
	}
	public String getParentName() {
		return parentName;
	}
	public void setParentName(String parentName) {
		this.parentName = parentName;
	}
	public String getMatricule() {
		return matricule;
	}
	public void setMatricule(String matricule) {
		this.matricule = matricule;
	}
	public String getClasse() {
		return classe;
	}
	public void setClasse(String classe) {
		this.classe = classe;
	}
	public String getTranche() {
		return tranche;
	}
	public void setTranche(String tranche) {
		this.tranche = tranche;
	}
	public Double getMontantPaye() {
		return montantPaye;
	}
	public void setMontantPaye(Double montantPaye) {
		this.montantPaye = montantPaye;
	}
	public Double getMontantRestant() {
		return montantRestant;
	}
	public void setMontantRestant(Double montantRestant) {
		this.montantRestant = montantRestant;
	}
	public Double getMontantDu() {
		return montantDu;
	}
	public void setMontantDu(Double montantDu) {
		this.montantDu = montantDu;
	}
	public Date getDateLimite() {
		return dateLimite;
	}
	public void setDateLimite(Date dateLimite) {
		this.dateLimite = dateLimite;
	}	

}
