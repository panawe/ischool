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
import com.esoft.ischool.model.BaseEntity;

@Entity
@Table(name="SHIPMENT")
public class Shipment extends BaseEntity {

	@Id
	@Column(name = "SHIPMENT_ID")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@ManyToOne
	@JoinColumn(name = "CARRIER_ID", nullable = true)
	private Carrier carrier;

	//@NotNull
	//@Length(max = 20)
	@Column(name = "SHIPMENT_NUMBER")
	private String shipmentNumber;

	
	//@NotNull
	//@Length(max = 75)
	@Column(name = "ORIGIN")
	private String origin;

	//@Length(max = 75)
	@Column(name = "DESTINATION")
	private String destination;
	
	@Column(name="DATE_OF_DISPATCH")
	private Date dateOfDispatch;

	@Column(name="DATE_OF_ARRIVAL")
	private Date dateOfArrival;
	
	//@Length(max = 75)
	@Column(name = "DELIVER_BY")
	private String deliveredBy;

	//@Length(max = 75)
	@Column(name = "RECEIVED_BY")
	private String receivedBy;

	@Override
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getOrigin() {
		return origin;
	}

	public void setOrigin(String origin) {
		this.origin = origin;
	}

	public String getDestination() {
		return destination;
	}

	public void setDestination(String destination) {
		this.destination = destination;
	}

	public Date getDateOfDispatch() {
		return dateOfDispatch;
	}

	public void setDateOfDispatch(Date dateOfDispatch) {
		this.dateOfDispatch = dateOfDispatch;
	}

	public Date getDateOfArrival() {
		return dateOfArrival;
	}

	public void setDateOfArrival(Date dateOfArrival) {
		this.dateOfArrival = dateOfArrival;
	}

	public String getDeliveredBy() {
		return deliveredBy;
	}

	public void setDeliveredBy(String deliveredBy) {
		this.deliveredBy = deliveredBy;
	}

	public String getReceivedBy() {
		return receivedBy;
	}

	public void setReceivedBy(String receivedBy) {
		this.receivedBy = receivedBy;
	}

	public Carrier getCarrier() {
		return carrier;
	}

	public void setCarrier(Carrier carrier) {
		this.carrier = carrier;
	}

	public String getShipmentNumber() {
		return shipmentNumber;
	}

	public void setShipmentNumber(String shipmentNumber) {
		this.shipmentNumber = shipmentNumber;
	}
}
