package com.esoft.ischool.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="ALERT")
public class Alert extends BaseEntity {
	
	@Id
	@Column(name="ALERT_ID")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@Column(name="SUBJECT")
	private String subject;

	@Column(name="MESSAGE")
	private String message;

	@Column(name="NBR_DAYS")
	private Integer numberOfDays;

	@Column(name="MOMENT")
	private Integer moment;

	@Column(name="STATUS")
	private String status;

	@Column(name="NBR_REPEAT")
	private int numberOfRepeat;

	@Column(name="REPEAT_INTERVAL")
	private Integer repeatInterval;

	@Column(name="FIRST_RUN_TIME")
	private Date firstRunTime;

	@Column(name="LAST_RUN_TIME")
	private Date lastRunTime;

	@Column(name="RUN_COUNT")
	private Integer runCount;

	@Column(name="EMAILRECEIVER")
	private Integer emailReceiver;
	
	@Column(name="SENDTO")
	private String sendTo;
	
	@Column(name="DISPLAY")
	private Integer display;

	@Column(name="DAYS_DISPLAY")
	private Integer daysDisplay;

	@Column(name="alert_type_code")
	private String alertTypeCode;
	
	@Override
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getSubject() {
		return subject;
	}

	public void setSubject(String subject) {
		this.subject = subject;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public Integer getNumberOfDays() {
		return numberOfDays;
	}

	public void setNumberOfDays(Integer numberOfDays) {
		this.numberOfDays = numberOfDays;
	}

	public Integer getMoment() {
		return moment;
	}

	public void setMoment(Integer moment) {
		this.moment = moment;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public int getNumberOfRepeat() {
		return numberOfRepeat;
	}

	public void setNumberOfRepeat(int numberOfRepeat) {
		this.numberOfRepeat = numberOfRepeat;
	}

	public Integer getRepeatInterval() {
		return repeatInterval;
	}

	public void setRepeatInterval(Integer repeatInterval) {
		this.repeatInterval = repeatInterval;
	}

	public Date getFirstRunTime() {
		return firstRunTime;
	}

	public void setFirstRunTime(Date firstRunTime) {
		this.firstRunTime = firstRunTime;
	}

	public Date getLastRunTime() {
		return lastRunTime;
	}

	public void setLastRunTime(Date lastRunTime) {
		this.lastRunTime = lastRunTime;
	}

	public Integer getRunCount() {
		return runCount;
	}

	public void setRunCount(Integer runCount) {
		this.runCount = runCount;
	}

	public Integer getEmailReceiver() {
		return emailReceiver;
	}

	public void setEmailReceiver(Integer emailReceiver) {
		this.emailReceiver = emailReceiver;
	}

	public String getSendTo() {
		return sendTo;
	}

	public void setSendTo(String sendTo) {
		this.sendTo = sendTo;
	}

	public Integer getDisplay() {
		return display;
	}

	public void setDisplay(Integer display) {
		this.display = display;
	}

	public Integer getDaysDisplay() {
		return daysDisplay;
	}

	public void setDaysDisplay(Integer daysDisplay) {
		this.daysDisplay = daysDisplay;
	}

	public String getAlertTypeCode() {
		return alertTypeCode;
	}

	public void setAlertTypeCode(String alertTypeCode) {
		this.alertTypeCode = alertTypeCode;
	}
}
