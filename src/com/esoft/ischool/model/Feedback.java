package com.esoft.ischool.model;

import java.util.Date;

import javax.persistence.*;

import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import com.esoft.ischool.security.model.User;

@Entity
@Table (name ="FEEDBACK")
public class Feedback {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name ="FEEDBACK_ID")
	private Long id;
	
	@ManyToOne
	@JoinColumn(name="QUESTION_ID")
	private Question question;
	
	@ManyToOne
	@JoinColumn(name="USER_ID")
	@Fetch(FetchMode.JOIN)
	private User user;
	
	@Column(name="REMARK")
	private String remark;
	
	@ManyToOne
	@JoinColumn(name="USER_ID")
	@Fetch(FetchMode.JOIN)
	@Column(name="REVIEWED_BY")
	private User reviewedBy;
	
	@Column(name="REVIEW_DATE")
	private Date reviewDate;
	
	@Column(name="REVIEW_COMMENT")
	private String reviewComment;
	
	@Column(name="IS_VALID")
	private boolean isValid;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Question getQuestion() {
		return question;
	}

	public void setQuestion(Question question) {
		this.question = question;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public User getReviewedBy() {
		return reviewedBy;
	}

	public void setReviewedBy(User reviewedBy) {
		this.reviewedBy = reviewedBy;
	}

	public Date getReviewDate() {
		return reviewDate;
	}

	public void setReviewDate(Date reviewDate) {
		this.reviewDate = reviewDate;
	}

	public String getReviewComment() {
		return reviewComment;
	}

	public void setReviewComment(String reviewComment) {
		this.reviewComment = reviewComment;
	}

	public boolean isValid() {
		return isValid;
	}

	public void setValid(boolean isValid) {
		this.isValid = isValid;
	}
	

}
