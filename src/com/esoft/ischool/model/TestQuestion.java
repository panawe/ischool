package com.esoft.ischool.model;

import javax.persistence.*;

import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

@Entity
@Table(name="TEST_QUESTION")
public class TestQuestion extends BaseEntity {

	@Id
	@Column(name="TQ_ID")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@Column(name ="QUESTION_SEQ")
	private Integer sequence;
	
	@ManyToOne
	@JoinColumn(name="TEST_ID")
	private Test test;
	
	@ManyToOne
	@JoinColumn(name="QUESTION_ID")
	@Fetch(FetchMode.JOIN)
	private Question question;

	@Column(name ="SCORE")
	private Integer score;
	
	@Transient
	private Short rightAnswers;
	
	
	public Short getRightAnswers() {
		return rightAnswers;
	}


	public void setRightAnswers(Short rightAnswers) {
		this.rightAnswers = rightAnswers;
	}


	@Override
	public Long getId() {
		return id;
	}


	public void setId(Long tqId) {
		this.id = tqId;
	}


	public Integer getSequence() {
		return sequence;
	}


	public void setSequence(Integer sequence) {
		this.sequence = sequence;
	}


	public Test getTest() {
		return test;
	}


	public void setTest(Test test) {
		this.test = test;
	}


	public Question getQuestion() {
		return question;
	}


	public void setQuestion(Question question) {
		this.question = question;
	}

	public Integer getScore() {
		return score;
	}


	public void setScore(Integer score) {
		this.score = score;
	}


	@Override
	public String toString() {
		return "TestQuestion [question=" + question + ", score=" + score
				+ ", sequence=" + sequence + ", test=" + test + ", tqId="
				+ id + ", getCreateDate()=" + getCreateDate()
				 + ", getModDate()="
				+ getModDate() + ", getModifiedBy()=" + getModifiedBy() + "]";
	}


	
}
