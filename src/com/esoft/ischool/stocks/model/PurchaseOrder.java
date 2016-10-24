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

@Entity
@Table(name="PURCHASE_ORDER")
public class PurchaseOrder extends BaseEntity {

	@Id
	@Column(name = "ORDER_ID")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@ManyToOne
	@JoinColumn(name = "SUPPLIER_ID", nullable = true)
	private Supplier supplier;

	@ManyToOne
	@JoinColumn(name = "SHIPMENT_ID", nullable = true)
	private Shipment shipment;

	//@NotNull
	@Column(name = "ORDER_DATE")
	private Date orderDate;

	@Column(name = "POS_DELIVERY_DATE")
	private Date possibleDeliveryDate;
	
	//@Length(max = 20)
	@Column(name = "CLIENT_NUMBER")
	private String clientNumber;
	
	@Column(name = "DISCOUNT_RATE")
	private Double discountRate;
	
	@Column(name = "DISCOUNT_AMOUNT")
	private Double discountAmount = new Double(0);
	
	@Column(name = "TOTAL_AMOUNT")
	private Double totalAmount = new Double(0);

	@Column(name = "STATUS")
	private Integer status;
	
	@Transient
	private String statusDesc;

	@Override
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Supplier getSupplier() {
		return supplier;
	}

	public void setSupplier(Supplier supplier) {
		this.supplier = supplier;
	}

	public Date getOrderDate() {
		return orderDate;
	}

	public void setOrderDate(Date orderDate) {
		this.orderDate = orderDate;
	}

	public Date getPossibleDeliveryDate() {
		return possibleDeliveryDate;
	}

	public void setPossibleDeliveryDate(Date possibleDeliveryDate) {
		this.possibleDeliveryDate = possibleDeliveryDate;
	}

	public String getClientNumber() {
		return clientNumber;
	}

	public void setClientNumber(String clientNumber) {
		this.clientNumber = clientNumber;
	}

	public Double getDiscountRate() {
		return discountRate;
	}

	public void setDiscountRate(Double discountRate) {
		this.discountRate = discountRate;
	}

	public Double getDiscountAmount() {
		return discountAmount;
	}

	public void setDiscountAmount(Double discountAmount) {
		this.discountAmount = discountAmount;
	}

	public Double getTotalAmount() {
		return totalAmount;
	}

	public void setTotalAmount(Double totalAmount) {
		this.totalAmount = totalAmount;
	}

	public Shipment getShipment() {
		return shipment;
	}

	public void setShipment(Shipment shipment) {
		this.shipment = shipment;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public String getStatusDesc() {
		return statusDesc;
	}

	public void setStatusDesc(String statusDesc) {
		this.statusDesc = statusDesc;
	}
}
