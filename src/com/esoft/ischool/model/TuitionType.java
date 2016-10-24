package com.esoft.ischool.model;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

@Entity

@Table(name="TUITION_TYPE")
public class TuitionType extends BaseEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name="TUITION_TYPE_ID")
	private Long id;
	
	@Column(name="NAME")
	private String name;
	
	@Transient
	private List<Tuition> tuitions;
	
	@Transient
	private double totalAmount;
	
	@Transient
	private double totalPaid;
	
	@Transient
	private double totalRebate;
	
	@Transient
	private double totalBalance;
	
	@Transient
	private boolean showDetails;
	
	@Transient
	private boolean hasPastDueAmount;

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

	public List<Tuition> getTuitions() {
		return tuitions;
	}

	public void setTuitions(List<Tuition> tuitions) {
		this.tuitions = tuitions;
	}

	public double getTotalAmount() {
		return totalAmount;
	}

	public void setTotalAmount(double totalAmount) {
		this.totalAmount = totalAmount;
	}

	public double getTotalPaid() {
		return totalPaid;
	}

	public void setTotalPaid(double totalPaid) {
		this.totalPaid = totalPaid;
	}

	public double getTotalRebate() {
		return totalRebate;
	}

	public void setTotalRebate(double totalRebate) {
		this.totalRebate = totalRebate;
	}

	public double getTotalBalance() {
		return totalBalance;
	}

	public void setTotalBalance(double totalBalance) {
		this.totalBalance = totalBalance;
	}

	public boolean isShowDetails() {
		return showDetails;
	}

	public boolean isHasPastDueAmount() {
		return hasPastDueAmount;
	}

	public void setHasPastDueAmount(boolean hasPastDueAmount) {
		this.hasPastDueAmount = hasPastDueAmount;
	}

	public void setShowDetails(boolean showDetails) {
		this.showDetails = showDetails;
	}

	@Override
	public String toString() {
		return "Tuition [name=" + name + ", Id=" + id
				+ ", getCreateDate()=" + getCreateDate() + ", getModDate()=" + getModDate()
				+ ", getModifiedBy()=" + getModifiedBy() + "]";
	}

}
