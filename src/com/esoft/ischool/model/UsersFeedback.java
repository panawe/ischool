package com.esoft.ischool.model;
import javax.persistence.*;

@Entity
@Table (name ="USERS_FEEDBACK")
public class UsersFeedback extends BaseEntity{
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name ="FEEDBACK_ID")
	private Long id;
	
	@Column(name="SCHOOL_NAME")
	private String schoolName;
	
	@Column(name="COMMENT")
	private String comment;

	@Column(name="EMAIL")
	private String email;
	
	@Column(name="FULL_NAME")
	private String fullName;
	
	@Column(name="USER_DETAILS")
	private String userDetails;
	
	@Column(name="OVERALL_RATING")
	private Short overallRating;
	
	@Column(name="PERFORMANCE_RATING")
	private Short performanceRating;
	
	@Column(name="INTERFACE_RATING")
	private Short interfaceRating;
	
	@Column(name="COST_RATING")
	private Short costRating;
	
	@Column(name="FUNCTIONALITY_RATING")
	private Short functionalityRating;
	
	@Column(name="SUPPORT_RATING")
	private Short supportRating;
	
	@Column(name="DOC_RATING")
	private Short docRating;
	
	@Column(name="USER_TYPE")
	private Short userType;
	
	public Long getId() {
		return id;
	}

	public Short getUserType() {
		return userType;
	}

	public void setUserType(Short userType) {
		this.userType = userType;
	}

	public Short getDocRating() {
		return docRating;
	}

	public void setDocRating(Short docRating) {
		this.docRating = docRating;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Short getSupportRating() {
		return supportRating;
	}

	public void setSupportRating(Short supportRating) {
		this.supportRating = supportRating;
	}

	public String getSchoolName() {
		return schoolName;
	}

	public void setSchoolName(String schoolName) {
		this.schoolName = schoolName;
	}

	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getFullName() {
		return fullName;
	}

	public void setFullName(String fullName) {
		this.fullName = fullName;
	}

	public String getUserDetails() {
		return userDetails;
	}

	public void setUserDetails(String userDetails) {
		this.userDetails = userDetails;
	}

	public Short getOverallRating() {
		return overallRating;
	}

	public void setOverallRating(Short overallRating) {
		this.overallRating = overallRating;
	}

	public Short getPerformanceRating() {
		return performanceRating;
	}

	public void setPerformanceRating(Short performanceRating) {
		this.performanceRating = performanceRating;
	}

	public Short getInterfaceRating() {
		return interfaceRating;
	}

	public void setInterfaceRating(Short interfaceRating) {
		this.interfaceRating = interfaceRating;
	}

	public Short getCostRating() {
		return costRating;
	}

	public void setCostRating(Short costRating) {
		this.costRating = costRating;
	}

	public Short getFunctionalityRating() {
		return functionalityRating;
	}

	public void setFunctionalityRating(Short functionalityRating) {
		this.functionalityRating = functionalityRating;
	}

	@Override
	public String toString() {
		return "UsersFeedback [schoolName=" + schoolName + ", comment="
				+ comment + ", email=" + email + ", fullName=" + fullName
				+ ", userDetails=" + userDetails + ", overallRating="
				+ overallRating + ", performanceRating=" + performanceRating
				+ ", interfaceRating=" + interfaceRating + ", costRating="
				+ costRating + ", functionalityRating=" + functionalityRating
				+ ", supportRating=" + supportRating + ", docRating="
				+ docRating + ", userType=" + userType + ", getModDate()="
				+ getModDate() + ", getCreateDate()=" + getCreateDate() + "]";
	}


}
