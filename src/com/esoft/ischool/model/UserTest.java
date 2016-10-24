package com.esoft.ischool.model;

import java.util.Date;
import javax.persistence.*;

import com.esoft.ischool.security.model.User;

@Entity
@Table(name="USERS_TEST")
public class UserTest extends BaseEntity{
	
	@Id
	@Column(name="UT_ID")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@Column(name="BEGIN_DATE")
	private Date beginDate;

	@Column(name="END_DATE")
	private Date endDate;
	
	@Column(name="DURATION")
	private Integer duration=0;
	
	@Column(name ="RIGHT_ANSWER")
	private Integer right=0;
	
	@Column(name="WRONG_ANSWER")
	private Integer wrong=0;

	@ManyToOne
	@JoinColumn(name="USER_ID")
	private User user;
	
	@ManyToOne
	@JoinColumn(name="TEST_ID")
	private Test test;
	
	@Column(name="COMPLETED")
	private Short completed=0;
	
	private Integer score=0;
	
	

	public Integer getScore() {
		return score;
	}


	public void setScore(Integer score) {
		this.score = score;
	}


	@Override
	public Long getId() {
		return id;
	}


	public void setId(Long id) {
		this.id = id;
	}


	public Integer getDuration() {
		return duration;
	}


	public void setDuration(Integer duration) {
		this.duration = duration;
	}


	public Integer getRight() {
		return right;
	}


	public void setRight(Integer right) {
		this.right = right;
	}


	public Integer getWrong() {
		return wrong;
	}


	public void setWrong(Integer wrong) {
		this.wrong = wrong;
	}


	public boolean getCompleted() {
		return completed==1?true:false;
	}


	public void setCompleted(boolean completed) {
		this.completed = (short) (completed==true?1:0);
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

	public User getUser() {
		return user;
	}


	public void setUser(User user) {
		this.user = user;
	}


	public Test getTest() {
		return test;
	}


	public void setTest(Test test) {
		this.test = test;
	}


	@Override
	public String toString() {
		return "UserTest [beginDate=" + beginDate + ", completed=" + completed
				+ ", duration=" + duration + ", endDate=" + endDate + ", right=" + right + ", test=" + test + ", user="
				+ user + ", utId=" + id + ", wrong=" + wrong
				+ ", getCreateDate()=" + getCreateDate() +  ", getModDate()=" + getModDate()
				+ ", getModifiedBy()=" + getModifiedBy() + "]";
	}




}
