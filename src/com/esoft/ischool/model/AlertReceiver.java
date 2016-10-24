package com.esoft.ischool.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.esoft.ischool.security.model.User;

@Entity
@Table(name="ALERT_RECEIVER")
public class AlertReceiver extends BaseEntity {
	
	@Id
	@Column(name="RECEIVER_ID")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@ManyToOne
	@JoinColumn(name = "ALERT_ID", nullable = true)
	private Alert alert;
	
	@ManyToOne
	@JoinColumn(name = "USER_ID", nullable = true)
	private User user;
	
	@ManyToOne
	@JoinColumn(name = "TUITION_ID", nullable = true)
	private Tuition tuition;
	
	@Override
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Alert getAlert() {
		return alert;
	}

	public void setAlert(Alert alert) {
		this.alert = alert;
	}
	
	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public Tuition getTuition() {
		return tuition;
	}

	public void setTuition(Tuition tuition) {
		this.tuition = tuition;
	}
}
