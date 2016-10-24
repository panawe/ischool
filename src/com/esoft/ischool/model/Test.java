package com.esoft.ischool.model;

import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "TEST")
public class Test extends BaseEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "TEST_ID")
	private Long id;

	@Column(name = "DESCRIPTION")
	private String description;

	@Column(name = "TITLE")
	private String title;

	@Column(name = "BEGIN_MESSAGE")
	private String beginMessage;

	@Column(name = "END_MESSAGE")
	private String endMessage;

	@Column(name = "PRORATE_SCORE")
	private Short prorateScore = 0;

	@Column(name = "SCORE")
	private Integer score=0;

	@Column(name = "CERTIFICATE_SCORE")
	private Integer certificateScore;

	@Column(name = "PASSWORD")
	private String password;

	@Column(name = "IS_PUBLIC")
	private Short isPublic = 0;

	@Column(name = "SHOW_EACH_ANSWER")
	private Short showEachAnswer = 0;

	@Column(name = "SHOW_ALL_ANSWERS")
	private Short showAllAnswers = 0;
	
	@Column(name = "SHOW_POINTS")
	private Short showPoints = 0;

	@Column(name = "CAN_PRINT")
	private Short canPrint = 0;

	@Column(name = "RESULT_DAYS")
	private Integer resultDays;

	@Column(name = "SHOW_RATING")
	private Short showRating=0;

	@Column(name = "QUESTION_PER_PAGE")
	private Integer questionPerPage=1;

	@ManyToOne
	@JoinColumn(name = "SUBJECT_ID")
	private Subject subject;

	@ManyToOne
	@JoinColumn(name = "LEVEL_ID")
	private Level level;
	
	@Column(name="SHOW_LIVE_SCORE")
	private Short showLiveScore=0;
	
	@Column(name="ALLOW_MULTIPLE_TRIAL")
	private Short allowMultipleTrial =0;

	@Column(name ="CAN_CONTINUE_LATER")
	private Short canContinueLater=0;
	
	public boolean getCanContinueLater() {
		return canContinueLater==1?true:false;
	}

	public void setCanContinueLater(boolean canContinueLater) {
		this.canContinueLater = (short) (canContinueLater==true?1:0);
	}

	public boolean getAllowMultipleTrial() {
		return allowMultipleTrial==1?true:false;
	}

	public boolean getShowPoints() {
		return showPoints==1?true:false;
	}

	public void setShowPoints(boolean showPoints) {
		this.showPoints = (short)(showPoints==true?1:0);
	}

	public void setAllowMultipleTrial(boolean allowMultipleTrial) {
		this.allowMultipleTrial = (short) (allowMultipleTrial==true?1:0);
	}

	public boolean getShowLiveScore() {
		return showLiveScore==1?true:false;
	}

	public void setShowLiveScore(boolean showLiveScore) {
		this.showLiveScore = (short) (showLiveScore==true?1:0);
	}

	public Level getLevel() {
		return level;
	}

	public boolean getShowRating() {
		return showRating == 1 ? true : false;
	}

	public void setShowRating(boolean showRating) {
		this.showRating = (short) (showRating == true ? 1 : 0);
	}

	public void setLevel(Level level) {
		this.level = level;
	}

	@Column(name = "DURATION")
	private Integer duration;

	/**
	 * 0= Random System generated, 1= User directed
	 */
	@Column(name = "SYSTEM_GENERATED")
	private Short systemGenerated=0;

	@Column(name = "DUE_DATE")
	private Date dueDate;

	public Date getDueDate() {
		return dueDate;
	}

	public void setDueDate(Date dueDate) {
		this.dueDate = dueDate;
	}

	@Override
	public Long getId() {
		return id;
	}

	public void setTestId(Long testId) {
		this.id = testId;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Integer getDuration() {
		return duration;
	}

	public void setDuration(Integer duration) {
		this.duration = duration;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getBeginMessage() {
		return beginMessage;
	}

	public void setBeginMessage(String beginMessage) {
		this.beginMessage = beginMessage;
	}

	public String getEndMessage() {
		return endMessage;
	}

	public void setEndMessage(String endMessage) {
		this.endMessage = endMessage;
	}

	public boolean getProrateScore() {
		return prorateScore == 1 ? true : false;
	}

	public void setProrateScore(boolean prorateScore) {
		this.prorateScore = (short) (prorateScore == true ? 1 : 0);
	}

	public Integer getScore() {
		return score;
	}

	public void setScore(Integer score) {
		this.score = score;
	}

	public Integer getCertificateScore() {
		return certificateScore;
	}

	public void setCertificateScore(Integer certificateScore) {
		this.certificateScore = certificateScore;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public boolean getIsPublic() {
		return isPublic == 1 ? true : false;
	}

	public void setIsPublic(boolean isPublic) {
		this.isPublic = (short) (isPublic == true ? 1 : 0);
	}

	public boolean getShowEachAnswer() {
		return showEachAnswer == 1 ? true : false;
	}

	public void setShowEachAnswer(boolean showEachAnswer) {
		this.showEachAnswer = (short) (showEachAnswer == true ? 1 : 0);
	}

	public boolean getShowAllAnswers() {
		return showAllAnswers == 1 ? true : false;
	}

	public void setShowAllAnswers(boolean showAllAnswers) {
		this.showAllAnswers = (short) (showAllAnswers == true ? 1 : 0);
	}

	public boolean getCanPrint() {
		return canPrint == 1 ? true : false;
	}

	public void setCanPrint(boolean canPrint) {
		this.canPrint = (short) (canPrint == true ? 1 : 0);
	}

	public Integer getResultDays() {
		return resultDays;
	}

	public void setResultDays(Integer resultDays) {
		this.resultDays = resultDays;
	}

	public Integer getQuestionPerPage() {
		return questionPerPage;
	}

	public void setQuestionPerPage(Integer questionPerPage) {
		this.questionPerPage = questionPerPage;
	}

	public Subject getSubject() {
		return subject;
	}

	public boolean getSystemGenerated() {
		return systemGenerated == 1 ? true : false;
	}

	public void setSystemGenerated(boolean systemGenerated) {
		this.systemGenerated = (short) (systemGenerated == true ? 1 : 0);
	}

	public void setSubject(Subject subject) {
		this.subject = subject;
	}

	@Override
	public String toString() {
		return "Test [beginMessage=" + beginMessage + ", canPrint=" + canPrint
				+ ", certificateScore=" + certificateScore + ", description="
				+ description + ", dueDate=" + dueDate + ", duration="
				+ duration + ", endMessage=" + endMessage + ", isPublic="
				+ isPublic + ", password=" + password + ", prorateScore="
				+ prorateScore + ", questionPerPage=" + questionPerPage
				+ ", resultDays=" + resultDays + ", score=" + score
				+ ", showAllAnswers=" + showAllAnswers + ", showEachAnswer="
				+ showEachAnswer + ", subject=" + subject + ", testId=" + id
				+ ", title=" + title + ", type=" + systemGenerated
				+ ", getCreateDate()=" + getCreateDate() + ", getModDate()="
				+ getModDate() + ", getModifiedBy()=" + getModifiedBy() + "]";
	}

}
