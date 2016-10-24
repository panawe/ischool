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
import javax.persistence.Transient;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.esoft.ischool.util.Utils;

@Entity
@Table(name = "ADVERTISEMENT")
public class Advertisement extends BaseEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "ADVERTISEMENT_ID")
	private Long id;

	@ManyToOne
	@JoinColumn(name = "SPONSOR_ID")
	private Sponsor sponsor;

	@Transient
	private String sponsorId;
	
	@Column(name = "DESCRIPTION")
	private String description;
	
	@Column(name="LINK")
	private String link;

	@Column(name = "STATUS")
	private Short status;
	
	@JsonFormat(pattern = "MM/dd/yyyy")
	@Column(name = "BEGIN_DATE")
	private Date beginDate;

	@JsonFormat(pattern = "MM/dd/yyyy")
	@Column(name = "END_DATE")
	private Date endDate;
	
	@Column(name = "RATING")
	private Integer rating;

	@Column(name = "AMOUNT")
	private Double amount;
	
	@Transient
	private boolean hasImage;
	
	@Transient
	private String imagePath;

	public boolean getStatus() {
		return status == 1 ? true : false;
	}

	public void setStatus(boolean status) {
		this.status = status == true ? (short) 1 : 0;
	}
	
	public String getLink() {
		return link;
	}

	public void setLink(String link) {
		this.link = link;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Sponsor getSponsor() {
		return sponsor;
	}

	public void setSponsor(Sponsor sponsor) {
		this.sponsor = sponsor;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}
	
	public Date getBeginDate() {
		return beginDate;
	}

	public void setBeginDate(Date beginDate) {
		this.beginDate = beginDate;
	}

	public Date getEndDate() {
		return endDate;
	}

	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}

	public Integer getRating() {
		return rating;
	}

	public void setRating(Integer rating) {
		this.rating = rating;
	}

	public Double getAmount() {
		return amount;
	}

	public void setAmount(Double amount) {
		this.amount = amount;
	}

	public String getSponsorId() {
		return sponsorId;
	}

	public void setSponsorId(String sponsorId) {
		this.sponsorId = sponsorId;
	}
	
	@Transient
	public String getStatusDescription() {
		String desc = "";
		if (status == 1)
			desc = "Active";
		else if (status == 0)
			desc = "Desactive";
		
		return desc;
	}

	public boolean isHasImage() {
		return hasImage;
	}

	public void setHasImage(boolean hasImage) {
		this.hasImage = hasImage;
	}

	public String getImagePath() {
		return imagePath;
	}

	public void setImagePath(String imagePath) {
		this.imagePath = imagePath;
	}
}
