package com.esoft.ischool.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

@Entity
@Table(name = "QUESTION")
public class Question extends BaseEntity {

	@Id
	@Column(name = "QUESTION_ID")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@Column(name = "NBR_ANSWER")
	private Integer nbrAnswer=0;
	
	@Column(name = "BODY")
	private String body;
	
	@Column(name = "DISPLAY_NBR_ANSWER")
	private Short displayNbrAnswer=0;
	
	@Column(name = "ANSWER_EXPLANATION")
	private String answerExplannation;
	
	@Column(name = "IS_PUBLIC")
	private Short isPublic=0;

	@Column(name = "SCORE")
	private Integer score;

	@ManyToOne
	@JoinColumn(name = "RATING_ID")
	@Fetch(FetchMode.JOIN)
	private Rating rating;
	
	@ManyToOne
	@JoinColumn(name = "SUBJECT_ID")
	@Fetch(FetchMode.JOIN)
	private Subject subject;
	
	@ManyToOne
	@JoinColumn(name = "LEVEL_ID")
	@Fetch(FetchMode.JOIN)
	private Level level;
	
	
	public Level getLevel() {
		return level;
	}

	public void setLevel(Level level) {
		this.level = level;
	}

	public Subject getSubject() {
		return subject;
	}

	public void setSubject(Subject subject) {
		this.subject = subject;
	}


	public Rating getRating() {
		return rating;
	}

	public void setRating(Rating rating) {
		this.rating = rating;
	}


	@Override
	public Long getId() {
		return id;
	}

	public void setId(Long questionId) {
		this.id = questionId;
	}

	public Integer getNbrAnswer() {
		return nbrAnswer;
	}

	public void setNbrAnswer(Integer nbrAnswer) {
		this.nbrAnswer = nbrAnswer;
	}

	public String getBody() {
		return body;
	}

	public void setBody(String body) {
		this.body = body;
	}

 
	public String getAnswerExplannation() {
		return answerExplannation;
	}

	public void setAnswerExplannation(String answerExplannation) {
		this.answerExplannation = answerExplannation;
	}

 

	public boolean getDisplayNbrAnswer() {
		return displayNbrAnswer==1?true:false;
	}

	public void setDisplayNbrAnswer(boolean displayNbrAnswer) {
		this.displayNbrAnswer = (short) (displayNbrAnswer==true?1:0);
	}

	public boolean getIsPublic() {
		return isPublic==1?true:false;
	}

	public void setIsPublic(boolean isPublic) {
		this.isPublic = (short) (isPublic==true?1:0);
	}

	public Integer getScore() {
		return score;
	}

	public void setScore(Integer score) {
		this.score = score;
	}

	@Override
	public String toString() {
		return "Question [answerExplannation=" + answerExplannation
				+ ",  body=" + body
				+ ", displayNbrAnswer=" + displayNbrAnswer + ", isPublic="
				+ isPublic + ", nbrAnswer=" + nbrAnswer + ", questionId=" + id
				+ ", score=" + score + ", getCreateDate()=" + getCreateDate()
				+ ", getModDate()=" + getModDate() + ", getModifiedBy()="
				+ getModifiedBy() + "]";
	}

}
