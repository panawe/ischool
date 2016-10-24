package com.esoft.ischool.security.model;

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
@Table(name = "SESSION_HISTORY")
public class SessionHistory extends BaseEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "SESSION_HISTORY_ID")
	private Long id;

	@ManyToOne
	@JoinColumn(name = "USER_ID", nullable = true)
	private User user;

	@Column(name = "BEGIN_DATE")
	private Date beginDate;

	@Column(name = "END_DATE")
	private Date endDate;

	@Column(name = "SESSION_ID")
	private String sessionId;

	@Column(name = "HOST_NAME")
	private String hostName;

	@Column(name = "HOST_IP")
	private String hostIp;

	@Column(name = "BROWSER")
	private String browser;

	@Column(name = "LANGUAGE")
	private String language;

	@Column(name = "OSUSER")
	private String osuser;

	public String getOsuser() {
		return osuser;
	}

	public void setOsuser(String osuser) {
		this.osuser = osuser;
	}

	public String getHostIp() {
		return hostIp;
	}

	public void setHostIp(String hostIp) {
		this.hostIp = hostIp;
	}

	public String getBrowser() {
		return browser;
	}

	public void setBrowser(String browser) {
		this.browser = browser;
	}

	public String getLanguage() {
		return language;
	}

	public void setLanguage(String language) {
		this.language = language;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
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

	public String getSessionId() {
		return sessionId;
	}

	public void setSessionId(String sessionId) {
		this.sessionId = sessionId;
	}

	public String getHostName() {
		return hostName;
	}

	public void setHostName(String hostName) {
		this.hostName = hostName;
	}

	@Override
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

}
