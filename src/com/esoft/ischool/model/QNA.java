package com.esoft.ischool.model;

import javax.persistence.*;

@Entity
@Table(name="QNA")
public class QNA extends BaseEntity{

	@Id
	@Column(name="QNA_ID")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@ManyToOne
	@JoinColumn(name="ANSWER_ID")
	private Answer answer;
	
	@ManyToOne
	@JoinColumn(name="QUESTION_ID")
	private Question question;
	
	@ManyToOne
	@JoinColumn(name="UT_ID")
	private UserTest userTest;

	@Column(name="IS_CORRECT")
	private Short isCorrect;
	
	
	
	public boolean getIsCorrect() {
		return isCorrect==1?true:false;
	}


	public void setIsCorrect(boolean isCorrect) {
		this.isCorrect = (short) (isCorrect==true?1:0);
	}


	@Override
	public Long getId() {
		return id;
	}


	public void setId(Long id) {
		this.id = id;
	}


	public Answer getAnswer() {
		return answer;
	}


	public void setAnswer(Answer answer) {
		this.answer = answer;
	}


	public Question getQuestion() {
		return question;
	}


	public void setQuestion(Question question) {
		this.question = question;
	}


	public UserTest getUserTest() {
		return userTest;
	}


	public void setUserTest(UserTest userTest) {
		this.userTest = userTest;
	}

	@Override
	public String toString() {
		return "QNA [answer=" + answer + ", qnaId=" + id + ", question="
				+ question + ", userTest=" + userTest + ", getCreateDate()="
				+ getCreateDate() + ", getModDate()=" + getModDate() + ", getModifiedBy()="
				+ getModifiedBy() + "]";
	}
	
	
	
}
