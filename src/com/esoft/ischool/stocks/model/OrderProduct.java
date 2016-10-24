package com.esoft.ischool.stocks.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import com.esoft.ischool.model.BaseEntity;

@Entity
@Table(name="ORDER_PRODUCT")
public class OrderProduct extends BaseEntity {

	@Id
	@Column(name = "ORDER_PRODUCT_ID")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	//@NotNull
	@ManyToOne
	@JoinColumn(name = "ORDER_ID", nullable = true)
	private PurchaseOrder order;
	
	//@NotNull
	@ManyToOne
	@JoinColumn(name = "PRODUCT_ID", nullable = true)
	private Product product;

	@Column(name = "DISCOUNT_RATE")
	private Double discountRate;
	
	@Column(name = "DISCOUNT_AMOUNT")
	private Double discountAmount;
	
	@Column(name = "TOTAL_AMOUNT")
	private Double totalAmount;
	
	@Column(name = "TAX_RATE")
	private Double taxRate;
	
	@Column(name = "TOTAL_TAX")
	private Double totalTax;

	@Column(name = "QTY_ORDERED")
	private Integer quantityOrdered;

	@Column(name = "QTY_RECEIVED")
	private Integer quantityReceived;
	
	@Column(name = "QTY_DAMAGED")
	private Integer quantityDamaged;
	
	//@Length(max = 400)
	@Column(name = "COMMENT")
	private String comment;
	
	@Override
	public Long getId() {
		return id;
	}

	public Double getTotalAmount() {
		return totalAmount;
	}

	public void setTotalAmount(Double totalAmount) {
		this.totalAmount = totalAmount;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public PurchaseOrder getOrder() {
		return order;
	}

	public void setOrder(PurchaseOrder order) {
		this.order = order;
	}

	public Product getProduct() {
		return product;
	}

	public void setProduct(Product product) {
		this.product = product;
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

	public Double getTaxRate() {
		return taxRate;
	}

	public void setTaxRate(Double taxRate) {
		this.taxRate = taxRate;
	}

	public Double getTotalTax() {
		return totalTax;
	}

	public void setTotalTax(Double totalTax) {
		this.totalTax = totalTax;
	}

	
	public Integer getQuantityOrdered() {
		return quantityOrdered;
	}

	public void setQuantityOrdered(Integer quantityOrdered) {
		this.quantityOrdered = quantityOrdered;
	}

	public Integer getQuantityReceived() {
		return quantityReceived;
	}

	public void setQuantityReceived(Integer quantityReceived) {
		this.quantityReceived = quantityReceived;
	}

	public Integer getQuantityDamaged() {
		return quantityDamaged;
	}

	public void setQuantityDamaged(Integer quantityDamaged) {
		this.quantityDamaged = quantityDamaged;
	}

	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}
}
