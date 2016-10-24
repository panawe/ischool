package com.esoft.ischool.stocks.model;

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
import com.esoft.ischool.model.BaseEntity;
import com.esoft.ischool.security.model.User;

@Entity
@Table(name="PRODUCT_CONSUMER")
public class ProductConsumer extends BaseEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "PRODUCT_CONSUMER_ID")
	private Long id;
	
	@ManyToOne
	@JoinColumn(name = "USER_ID", nullable = true)
	private User consumer;
	
	@ManyToOne
	@JoinColumn(name = "APPROVED_BY", nullable = true)
	private User approvedBy;

	@ManyToOne
	@JoinColumn(name = "PRODUCT_ID", nullable = true)
	private Product product;
	
	@Column(name = "QTY_REQUESTED")
	private Integer quantityRequested;
	
	@Column(name = "QTY_PICKED")
	private Integer quantityAccepted=0;
	
	@Column(name = "STATUS")
	private Integer status;
	
	@Transient
	private String statusDesc;
	
	@Column(name = "REQUEST_DATE")
	private Date requestDate;

	@Column(name = "PICK_UP_DATE")
	private Date pickupDate;

	//@Length(max = 75)
	@Column(name = "RELEASED_BY")
	private String releasedBy;

	//@Length(max = 75)
	@Column(name = "PICKED_UP_BY")
	private String pickupBy;

	@Column(name = "QTY_TO_BE_RETURNED")
	private Integer quantityToBeReturned;
	
	@Column(name = "POSSIBLE_RETURNED_DATE")
	private Date possibleReturnedDate;
	
	@Column(name="RETURN_DATE")
	private Date returnDate;
	
	@Column(name = "QTY_RETURNED")
	private Integer quantityReturned=0;
	
	private String comment;
	
	

	public Date getReturnDate() {
		return returnDate;
	}

	public void setReturnDate(Date returnDate) {
		this.returnDate = returnDate;
	}

	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}

	@Override
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public User getConsumer() {
		return consumer;
	}

	public void setConsumer(User consumer) {
		this.consumer = consumer;
	}

	public Product getProduct() {
		return product;
	}

	public void setProduct(Product product) {
		this.product = product;
	}

	public Integer getQuantityRequested() {
		return quantityRequested;
	}

	public void setQuantityRequested(Integer quantityRequested) {
		this.quantityRequested = quantityRequested;
	}

	public Integer getQuantityAccepted() {
		return quantityAccepted;
	}

	public void setQuantityAccepted(Integer quantityAccepted) {
		this.quantityAccepted = quantityAccepted;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public Date getRequestDate() {
		return requestDate;
	}

	public void setRequestDate(Date requestDate) {
		this.requestDate = requestDate;
	}

	public Date getPickupDate() {
		return pickupDate;
	}

	public void setPickupDate(Date pickupDate) {
		this.pickupDate = pickupDate;
	}

	public String getReleasedBy() {
		return releasedBy;
	}

	public void setReleasedBy(String releasedBy) {
		this.releasedBy = releasedBy;
	}

	public String getPickupBy() {
		return pickupBy;
	}

	public void setPickupBy(String pickupBy) {
		this.pickupBy = pickupBy;
	}

	public String getStatusDesc() {
		return statusDesc;
	}

	public void setStatusDesc(String statusDesc) {
		this.statusDesc = statusDesc;
	}

	public Integer getQuantityToBeReturned() {
		return quantityToBeReturned;
	}

	public void setQuantityToBeReturned(Integer quantityToBeReturned) {
		this.quantityToBeReturned = quantityToBeReturned;
	}

	public Date getPossibleReturnedDate() {
		return possibleReturnedDate;
	}

	public void setPossibleReturnedDate(Date possibleReturnedDate) {
		this.possibleReturnedDate = possibleReturnedDate;
	}

	public Integer getQuantityReturned() {
		return quantityReturned;
	}

	public void setQuantityReturned(Integer quantityReturned) {
		this.quantityReturned = quantityReturned;
	}

	public User getApprovedBy() {
		return approvedBy;
	}

	public void setApprovedBy(User approvedBy) {
		this.approvedBy = approvedBy;
	}
	
	
}
