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

import com.paypal.api.payments.Payment;
import com.paypal.api.payments.RelatedResources;
import com.esoft.ischool.security.model.User;
import com.esoft.ischool.util.Utils;

@Entity
@Table(name = "PAYMENT_HISTORY")
public class PaymentHistory extends BaseEntity {

	public PaymentHistory() {
		super();
	};

	public PaymentHistory(Payment pay) {
		payerId = pay.getId();
		paymentMethod = pay.getPayer().getPaymentMethod();
		payerStatus = pay.getPayer().getStatus();

		if (pay.getPayer().getPayerInfo() != null) {
			email = pay.getPayer().getPayerInfo().getEmail();
			firstName = pay.getPayer().getPayerInfo().getFirstName();
			lastName = pay.getPayer().getPayerInfo().getLastName();
			payerId = pay.getPayer().getPayerInfo().getPayerId();
			phone = pay.getPayer().getPayerInfo().getPhone();
			countryCode = pay.getPayer().getPayerInfo().getCountryCode();
			if (pay.getPayer().getPayerInfo().getBillingAddress() != null) {
				baLine1 = pay.getPayer().getPayerInfo().getBillingAddress().getLine1();
				baLine2 = pay.getPayer().getPayerInfo().getBillingAddress().getLine2();
				baCity = pay.getPayer().getPayerInfo().getBillingAddress().getCity();
				baCountryCode = pay.getPayer().getPayerInfo().getBillingAddress().getCountryCode();
				baState = pay.getPayer().getPayerInfo().getBillingAddress().getState();
				baPostalCode = pay.getPayer().getPayerInfo().getBillingAddress().getPostalCode();
			}
			if (pay.getPayer().getPayerInfo().getShippingAddress() != null) {
				spLine1 = pay.getPayer().getPayerInfo().getShippingAddress().getLine1();
				spLine2 = pay.getPayer().getPayerInfo().getShippingAddress().getLine2();
				spCity = pay.getPayer().getPayerInfo().getShippingAddress().getCity();
				spCountryCode = pay.getPayer().getPayerInfo().getShippingAddress().getCountryCode();
				spState = pay.getPayer().getPayerInfo().getShippingAddress().getState();
				spPostalCode = pay.getPayer().getPayerInfo().getShippingAddress().getPostalCode();
			}
		}
		cart = pay.getCart();
		com.paypal.api.payments.Transaction t = pay.getTransactions().get(0);
		amount = new Double(t.getAmount().getTotal());
		currency = t.getAmount().getCurrency();
		RelatedResources rr = t.getRelatedResources().get(0);
		paymentMode = rr.getSale().getPaymentMode();
		transactionState = rr.getSale().getState();
		protectionEligibility = rr.getOrder() == null ? null : rr.getOrder().getProtectionEligibility();
		protectionEligibilityType = rr.getOrder() == null ? null : rr.getOrder().getProtectionEligibilityType();
		fee = new Double(rr.getSale().getTransactionFee().getValue());
		merchantId = pay.getPayee() == null ? null : pay.getPayee().getMerchantId();
		description = t.getDescription();

	}

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "PAYMENT_HISTORY_ID")
	private Long id;

	@Column(name = "PAYMENT_ID")
	private String paymentId;

	@Column(name = "PAYMENT_METHOD")
	private String paymentMethod;

	@Column(name = "PAYER_STATUS")
	private String payerStatus;

	@Column(name = "EMAIL")
	private String email;

	@Column(name = "FIRST_NAME")
	private String firstName;

	@Column(name = "LAST_NAME")
	private String lastName;

	@Column(name = "PAYER_ID")
	private String payerId;

	@Column(name = "PHONE")
	private String phone;

	@Column(name = "COUNTRY_CODE")
	private String countryCode;

	@Column(name = "BA_LINE1")
	private String baLine1;

	@Column(name = "BA_LINE2")
	private String baLine2;

	@Column(name = "BA_CITY")
	private String baCity;

	@Column(name = "BA_COUNTRY_CODE")
	private String baCountryCode;

	@Column(name = "BA_POSTAL_CODE")
	private String baPostalCode;

	@Column(name = "BA_STATE")
	private String baState;

	@Column(name = "SP_LINE1")
	private String spLine1;

	@Column(name = "SP_LINE2")
	private String spLine2;

	@Column(name = "SP_CITY")
	private String spCity;

	@Column(name = "SP_COUNTRY_CODE")
	private String spCountryCode;

	@Column(name = "SP_POSTAL_CODE")
	private String spPostalCode;

	@Column(name = "SP_STATE")
	private String spState;

	@Column(name = "CART")
	private String cart;

	@Column(name = "CURRENCY")
	private String currency;

	@Column(name = "TRANSACTION_STATE")
	private String transactionState;

	@Column(name = "PAYMENT_MODE")
	private String paymentMode;

	@Column(name = "PROTECTION_ELIGIBILITY")
	private String protectionEligibility;

	@Column(name = "PROTECTION_ELIGIBILITY_TYPE")
	private String protectionEligibilityType;

	@Column(name = "MERCHANT_ID")
	private String merchantId;

	@Column(name = "DESCRIPTION")
	private String description;

	@Column(name = "PAYMENT_STATE")
	private String paymentState;

	@Column(name = "AMOUNT")
	private Double amount;

	@Column(name = "FEE")
	private Double fee;

	@ManyToOne
	@JoinColumn(name = "USER_ID", nullable = true)
	private User user;

	@ManyToOne
	@JoinColumn(name = "PAYMENT_TYPE_ID")
	private PaymentType paymentType;

	@ManyToOne
	@JoinColumn(name = "PROJECT_ID")
	private Project project;

	public Project getProject() {
		return project;
	}

	public void setProject(Project project) {
		this.project = project;
	}

	@Override
	public Long getId() {
		// TODO Auto-generated method stub
		return id;
	}

	public String getPaymentId() {
		return paymentId;
	}

	public void setPaymentId(String paymentId) {
		this.paymentId = paymentId;
	}

	public String getPaymentMethod() {
		return paymentMethod;
	}

	public void setPaymentMethod(String paymentMethod) {
		this.paymentMethod = paymentMethod;
	}

	public String getPayerStatus() {
		return payerStatus;
	}

	public void setPayerStatus(String payerStatus) {
		this.payerStatus = payerStatus;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getPayerId() {
		return payerId;
	}

	public void setPayerId(String payerId) {
		this.payerId = payerId;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getCountryCode() {
		return countryCode;
	}

	public void setCountryCode(String countryCode) {
		this.countryCode = countryCode;
	}

	public String getBaLine1() {
		return baLine1;
	}

	public void setBaLine1(String baLine1) {
		this.baLine1 = baLine1;
	}

	public String getBaLine2() {
		return baLine2;
	}

	public void setBaLine2(String baLine2) {
		this.baLine2 = baLine2;
	}

	public String getBaCity() {
		return baCity;
	}

	public void setBaCity(String baCity) {
		this.baCity = baCity;
	}

	public String getBaCountryCode() {
		return baCountryCode;
	}

	public void setBaCountryCode(String baCountryCode) {
		this.baCountryCode = baCountryCode;
	}

	public String getBaPostalCode() {
		return baPostalCode;
	}

	public void setBaPostalCode(String baPostalCode) {
		this.baPostalCode = baPostalCode;
	}

	public String getBaState() {
		return baState;
	}

	public void setBaState(String baState) {
		this.baState = baState;
	}

	public String getSpLine1() {
		return spLine1;
	}

	public void setSpLine1(String spLine1) {
		this.spLine1 = spLine1;
	}

	public String getSpLine2() {
		return spLine2;
	}

	public void setSpLine2(String spLine2) {
		this.spLine2 = spLine2;
	}

	public String getSpCity() {
		return spCity;
	}

	public void setSpCity(String spCity) {
		this.spCity = spCity;
	}

	public String getSpCountryCode() {
		return spCountryCode;
	}

	public void setSpCountryCode(String spCountryCode) {
		this.spCountryCode = spCountryCode;
	}

	public String getSpPostalCode() {
		return spPostalCode;
	}

	public void setSpPostalCode(String spPostalCode) {
		this.spPostalCode = spPostalCode;
	}

	public String getSpState() {
		return spState;
	}

	public void setSpState(String spState) {
		this.spState = spState;
	}

	public String getCart() {
		return cart;
	}

	public void setCart(String cart) {
		this.cart = cart;
	}

	public String getCurrency() {
		return currency;
	}

	public void setCurrency(String currency) {
		this.currency = currency;
	}

	public String getTransactionState() {
		return transactionState;
	}

	public void setTransactionState(String transactionState) {
		this.transactionState = transactionState;
	}

	public String getProtectionEligibility() {
		return protectionEligibility;
	}

	public void setProtectionEligibility(String protectionEligibility) {
		this.protectionEligibility = protectionEligibility;
	}

	public String getProtectionEligibilityType() {
		return protectionEligibilityType;
	}

	public void setProtectionEligibilityType(String protectionEligibilityType) {
		this.protectionEligibilityType = protectionEligibilityType;
	}

	public String getMerchantId() {
		return merchantId;
	}

	public void setMerchantId(String merchantId) {
		this.merchantId = merchantId;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getPaymentState() {
		return paymentState;
	}

	public void setPaymentState(String paymentState) {
		this.paymentState = paymentState;
	}

	public Double getAmount() {
		return amount;
	}

	public void setAmount(Double amount) {
		this.amount = amount;
	}

	public Double getFee() {
		return fee;
	}

	public void setFee(Double fee) {
		this.fee = fee;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public PaymentType getPaymentType() {
		return paymentType;
	}

	public void setPaymentType(PaymentType paymentType) {
		this.paymentType = paymentType;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getPaymentMode() {
		return paymentMode;
	}

	public void setPaymentMode(String paymentMode) {
		this.paymentMode = paymentMode;
	}

	@Override
	public String toString() {
		return "PaymentHistory [id=" + id + ", paymentId=" + paymentId + ", paymentMethod=" + paymentMethod
				+ ", payerStatus=" + payerStatus + ", email=" + email + ", firstName=" + firstName + ", lastName="
				+ lastName + ", payerId=" + payerId + ", phone=" + phone + ", countryCode=" + countryCode + ", baLine1="
				+ baLine1 + ", baLine2=" + baLine2 + ", baCity=" + baCity + ", baCountryCode=" + baCountryCode
				+ ", baPostalCode=" + baPostalCode + ", baState=" + baState + ", spLine1=" + spLine1 + ", spLine2="
				+ spLine2 + ", spCity=" + spCity + ", spCountryCode=" + spCountryCode + ", spPostalCode=" + spPostalCode
				+ ", spState=" + spState + ", cart=" + cart + ", currency=" + currency + ", transactionState="
				+ transactionState + ", paymentMode=" + paymentMode + ", protectionEligibility=" + protectionEligibility
				+ ", protectionEligibilityType=" + protectionEligibilityType + ", merchantId=" + merchantId
				+ ", description=" + description + ", paymentState=" + paymentState + ", amount=" + amount + ", fee="
				+ fee + ", user=" + user + ", paymentType=" + paymentType + "]";
	}

}
