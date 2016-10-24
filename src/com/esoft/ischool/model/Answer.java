package com.esoft.ischool.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

@Entity
@Table(name="ANSWER")
public class Answer extends BaseEntity {

	@Id
	@Column(name = "ANSWER_ID")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(name = "ANSWER_SEQ")
	private Integer seqAnswer;

	@Column(name = "BODY")
	private String body;

	@Column(name = "IS_CORRECT")
	private Short correct = 0;

	@Transient
	private boolean checked;
	
	@ManyToOne
	@JoinColumn(name = "QUESTION_ID")
	@Fetch(FetchMode.JOIN)
	private Question question;

	@Override
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public boolean isChecked() {
		return checked;
	}

	public void setChecked(boolean checked) {
		this.checked = checked;
	}

	public String getBody() {
		return body;
	}

	public void setBody(String body) {
		this.body = body;
	}

	public boolean getCorrect() {
		return correct == 1 ? true : false;
	}

	public void setCorrect(boolean isCorrect) {
		this.correct = (short) (isCorrect == true ? 1 : 0);
	}

	@Override
	public String toString() {
		return "Answer [answerId=" + id + ", body=" + body + ", isCorrect="
				+ correct + ", sequence=" + seqAnswer + ", getCreateDate()="
				+ getCreateDate() + ", getCreatedBy()=" + ", getModDate()="
				+ getModDate() + ", getModifiedBy()=" + getModifiedBy() + "]";
	}

	public Integer getSeqAnswer() {
		return seqAnswer;
	}

	public void setSeqAnswer(Integer seqAnswer) {
		this.seqAnswer = seqAnswer;
	}


	public Question getQuestion() {
		return question;
	}

	public void setQuestion(Question question) {
		this.question = question;
	}

}
