package com.esoft.ischool.stocks.model;

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
@Table(name="PRODUCT")
public class Product extends BaseEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "PRODUCT_ID")
	private Long id;
	
	//@Length(max = 50)
	@Column(name = "PRODUCT_CODE")
	private String productCode;
	
	//@NotNull
	//@Length(max = 75)
	@Column(name = "NAME")
	private String name;
	
	//@Length(max = 75)
	@Column(name = "AUTHOR")
	private String author;

	@ManyToOne
	@JoinColumn(name = "BRAND_ID", nullable = true)
	private Brand brand;
	
	@ManyToOne
	@JoinColumn(name = "CATEGORY_ID", nullable = true)
	private Category category;

	@Column(name = "PICTURE")
	private byte[] picture;
	
	@Length(max = 50)
	@Column(name = "ISBN")
	private String isbn;
	
	@Length(max = 50)
	@Column(name = "BAR_CODE")
	private String barCode;
	
	private Integer quantity;
	
	@Column(name = "QUANTITY_IN_STOCK")
	private Integer quantityInStock;

	@Column(name = "MINQTY_TO_ORDER")
	private Integer minimumQuantityToOrder;

	@Column(name = "RETURNABLE")
	private String returnable;
	
	@Transient
	private Integer quantityOrdered;
	
	@Transient
	private Double totalAmount;
	
	@Transient
	private Double discountAmount;
	
	@Override
	public Long getId() {
		return id;
	}

	public String getAuthor() {
		return author;
	}

	public void setAuthor(String author) {
		this.author = author;
	}

	public Double getTotalAmount() {
		return totalAmount;
	}

	public void setTotalAmount(Double totalAmount) {
		this.totalAmount = totalAmount;
	}

	public Double getDiscountAmount() {
		return discountAmount;
	}

	public void setDiscountAmount(Double discountAmount) {
		this.discountAmount = discountAmount;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getProductCode() {
		return productCode;
	}

	public void setProductCode(String productCode) {
		this.productCode = productCode;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Brand getBrand() {
		return brand;
	}

	public void setBrand(Brand brand) {
		this.brand = brand;
	}

	public Category getCategory() {
		return category;
	}

	public void setCategory(Category category) {
		this.category = category;
	}

	public byte[] getPicture() {
		return picture;
	}

	public void setPicture(byte[] picture) {
		this.picture = picture;
	}

	public String getIsbn() {
		return isbn;
	}

	public void setIsbn(String isbn) {
		this.isbn = isbn;
	}

	public String getBarCode() {
		return barCode;
	}

	public void setBarCode(String barCode) {
		this.barCode = barCode;
	}

	public Integer getQuantityInStock() {
		return quantityInStock;
	}

	public void setQuantityInStock(Integer quantityInStock) {
		this.quantityInStock = quantityInStock;
	}

	public Integer getMinimumQuantityToOrder() {
		return minimumQuantityToOrder;
	}

	public void setMinimumQuantityToOrder(Integer minimumQuantityToOrder) {
		this.minimumQuantityToOrder = minimumQuantityToOrder;
	}

	public Integer getQuantityOrdered() {
		return quantityOrdered;
	}

	public void setQuantityOrdered(Integer quantityOrdered) {
		this.quantityOrdered = quantityOrdered;
	}

	public String getReturnable() {
		return returnable;
	}

	public void setReturnable(String returnable) {
		this.returnable = returnable;
	}

	public Integer getQuantity() {
		return quantity;
	}

	public void setQuantity(Integer quantity) {
		this.quantity = quantity;
	}
	
	
}
