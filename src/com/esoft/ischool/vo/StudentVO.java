package com.esoft.ischool.vo;

import java.util.Date;
import java.util.List;

import javax.persistence.Transient;

import org.apache.commons.lang.StringUtils;

import com.esoft.ischool.model.Tuition;
import com.esoft.ischool.model.TuitionType;
import com.esoft.ischool.util.Constants;

public class StudentVO {

	private Long id;
	private String city;
	private String matricule;
	private String birthCity;
	private String firstName;
	private String lastName;
	private String middleName;
	private String nickName;
	private Date birthDate;
	private String address;
	private String email;
	private String cityOfBirth;
	private String phone;
	private String cellPhone;
	private String fatherFullName;
	private String fatherProfession;
	private String fatherWorkPlace;
	private String fatherAddress;
	private String fatherCity;
	private String fatherEmail;
	private String motherFullName;
	private String motherProfession;
	private String motherWorkPlace;
	private String motherAddress;
	private String motherCity;
	private String motherEmail;
	private String tutorFullName;
	private String tutorAddress;
	private String tutorCountry;
	private String tutorCity;
	private String tutorEmail;
	private String fatherPhone;
	private String motherPhone;
	private String tutorPhone;
	private String comments;
	private String decision;
	private byte[] image;
	private Double mark;
	private String grade;
	private Long approvedBy;
	private Short status = 1;
	private String allergy;
	private String sex;
	private String statusText;
	private Date enrollmentDate;
	private String schoolYear;
	private String motherCountry;
	private String fatherCountry;
	
	private Long schoolId;
	private String schoolName;
	private String levelClassName;
	private Long countryId;
	private String countryName;
	private String countryOfBirth;
	private Long religionId;
	private String religionName;
	private Long userId;
	private String userName;
	
	private List<Tuition> tuitions;
	private List<TuitionType> tuitionTypes;
	private double totalAmount;
	private double totalPaid;
	private double totalRebate;
	private double totalBalance;
	private boolean showDetails;
	private boolean hasPastDueAmount;

	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getCity() {
		return city;
	}
	public void setCity(String city) {
		this.city = city;
	}
	public String getMatricule() {
		return matricule;
	}
	public void setMatricule(String matricule) {
		this.matricule = matricule;
	}
	public String getBirthCity() {
		return birthCity;
	}
	public void setBirthCity(String birthCity) {
		this.birthCity = birthCity;
	}
	public String getFirstName() {
		return firstName;
	}
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}
	public String getLastName() {
		return lastName;
	}
	public void setLastName(String lastName) {
		this.lastName = lastName;
	}
	public String getMiddleName() {
		return middleName;
	}
	public void setMiddleName(String middleName) {
		this.middleName = middleName;
	}
	public String getNickName() {
		return nickName;
	}
	public void setNickName(String nickName) {
		this.nickName = nickName;
	}
	public Date getBirthDate() {
		return birthDate;
	}
	public void setBirthDate(Date birthDate) {
		this.birthDate = birthDate;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	
	public String getCityOfBirth() {
		return cityOfBirth;
	}
	public void setCityOfBirth(String cityOfBirth) {
		this.cityOfBirth = cityOfBirth;
	}
	public String getPhone() {
		return phone;
	}
	public void setPhone(String phone) {
		this.phone = phone;
	}
	public String getCellPhone() {
		return cellPhone;
	}
	public void setCellPhone(String cellPhone) {
		this.cellPhone = cellPhone;
	}
	public String getFatherFullName() {
		return fatherFullName;
	}
	public void setFatherFullName(String fatherFullName) {
		this.fatherFullName = fatherFullName;
	}
	
	public String getFatherProfession() {
		return fatherProfession;
	}
	public void setFatherProfession(String fatherProfession) {
		this.fatherProfession = fatherProfession;
	}
	
	public String getFatherWorkPlace() {
		return fatherWorkPlace;
	}
	public void setFatherWorkPlace(String fatherWorkPlace) {
		this.fatherWorkPlace = fatherWorkPlace;
	}
	public String getFatherAddress() {
		return fatherAddress;
	}
	public void setFatherAddress(String fatherAddress) {
		this.fatherAddress = fatherAddress;
	}
	public String getFatherCity() {
		return fatherCity;
	}
	public void setFatherCity(String fatherCity) {
		this.fatherCity = fatherCity;
	}
	public String getFatherEmail() {
		return fatherEmail;
	}
	public void setFatherEmail(String fatherEmail) {
		this.fatherEmail = fatherEmail;
	}
	public String getMotherFullName() {
		return motherFullName;
	}
	public void setMotherFullName(String motherFullName) {
		this.motherFullName = motherFullName;
	}
	
	public String getMotherProfession() {
		return motherProfession;
	}
	public void setMotherProfession(String motherProfession) {
		this.motherProfession = motherProfession;
	}
	
	public String getMotherWorkPlace() {
		return motherWorkPlace;
	}
	public void setMotherWorkPlace(String motherWorkPlace) {
		this.motherWorkPlace = motherWorkPlace;
	}
	public String getMotherAddress() {
		return motherAddress;
	}
	public void setMotherAddress(String motherAddress) {
		this.motherAddress = motherAddress;
	}
	public String getMotherCity() {
		return motherCity;
	}
	public void setMotherCity(String motherCity) {
		this.motherCity = motherCity;
	}
	public String getMotherEmail() {
		return motherEmail;
	}
	public void setMotherEmail(String motherEmail) {
		this.motherEmail = motherEmail;
	}
	public String getTutorFullName() {
		return tutorFullName;
	}
	public void setTutorFullName(String tutorFullName) {
		this.tutorFullName = tutorFullName;
	}
	public String getTutorAddress() {
		return tutorAddress;
	}
	public void setTutorAddress(String tutorAddress) {
		this.tutorAddress = tutorAddress;
	}
	
	public String getTutorCountry() {
		return tutorCountry;
	}
	public void setTutorCountry(String tutorCountry) {
		this.tutorCountry = tutorCountry;
	}
	public String getTutorCity() {
		return tutorCity;
	}
	public void setTutorCity(String tutorCity) {
		this.tutorCity = tutorCity;
	}
	public String getTutorEmail() {
		return tutorEmail;
	}
	public void setTutorEmail(String tutorEmail) {
		this.tutorEmail = tutorEmail;
	}
	public String getFatherPhone() {
		return fatherPhone;
	}
	public void setFatherPhone(String fatherPhone) {
		this.fatherPhone = fatherPhone;
	}
	public String getMotherPhone() {
		return motherPhone;
	}
	public void setMotherPhone(String motherPhone) {
		this.motherPhone = motherPhone;
	}
	public String getTutorPhone() {
		return tutorPhone;
	}
	public void setTutorPhone(String tutorPhone) {
		this.tutorPhone = tutorPhone;
	}
	public String getComments() {
		return comments;
	}
	public void setComments(String comments) {
		this.comments = comments;
	}
	public String getDecision() {
		return decision;
	}
	public void setDecision(String decision) {
		this.decision = decision;
	}
	public byte[] getImage() {
		return image;
	}
	public void setImage(byte[] image) {
		this.image = image;
	}
	public Double getMark() {
		return mark;
	}
	public void setMark(Double mark) {
		this.mark = mark;
	}
	public String getGrade() {
		return grade;
	}
	public void setGrade(String grade) {
		this.grade = grade;
	}
	public Long getApprovedBy() {
		return approvedBy;
	}
	public void setApprovedBy(Long approvedBy) {
		this.approvedBy = approvedBy;
	}
	public Short getStatus() {
		return status;
	}
	public void setStatus(Short status) {
		this.status = status;
	}
	public String getAllergy() {
		return allergy;
	}
	public void setAllergy(String allergy) {
		this.allergy = allergy;
	}
	public String getSex() {
		return sex;
	}
	public void setSex(String sex) {
		this.sex = sex;
	}
	public void setStatusText(String statusText) {
		this.statusText = statusText;
	}
	
	public String getStatusText() {
		if (status != null)
			switch (status) {
			case 0:
				statusText = Constants.TEXT_INACTIVE_STUDENT;
				break;
			case 1:
				statusText = Constants.TEXT_ACTIVE_STATUS;
				break;
			case 2:
				statusText = Constants.TEXT_TEMP_STUDENT;
				break;
			case 3:
				statusText = Constants.TEXT_TEMP_REJECTED_STUDENT;
				break;
			case 4:
				statusText = Constants.TEXT_TEMP_ACCEPTED_STUDENT;
				break;
			default:
				statusText = Constants.TEXT_ACTIVE_STATUS;
				break;
			}

		return statusText;
	}
	public String getSchoolName() {
		return schoolName;
	}
	public void setSchoolName(String schoolName) {
		this.schoolName = schoolName;
	}
	public String getLevelClassName() {
		return levelClassName;
	}
	public void setLevelClassName(String levelClassName) {
		this.levelClassName = levelClassName;
	}
	public Long getCountryId() {
		return countryId;
	}
	public void setCountryId(Long countryId) {
		this.countryId = countryId;
	}
	public String getCountryName() {
		return countryName;
	}
	public void setCountryName(String countryName) {
		this.countryName = countryName;
	}
	
	public String getCountryOfBirth() {
		return countryOfBirth;
	}
	public void setCountryOfBirth(String countryOfBirth) {
		this.countryOfBirth = countryOfBirth;
	}
	public Long getReligionId() {
		return religionId;
	}
	public void setReligionId(Long religionId) {
		this.religionId = religionId;
	}
	public String getReligionName() {
		return religionName;
	}
	public void setReligionName(String religionName) {
		this.religionName = religionName;
	}
	public Long getSchoolId() {
		return schoolId;
	}
	public void setSchoolId(Long schoolId) {
		this.schoolId = schoolId;
	}
	public Long getUserId() {
		return userId;
	}
	public void setUserId(Long userId) {
		this.userId = userId;
	}
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public Date getEnrollmentDate() {
		return enrollmentDate;
	}
	public void setEnrollmentDate(Date enrollmentDate) {
		this.enrollmentDate = enrollmentDate;
	}
	public String getSchoolYear() {
		return schoolYear;
	}
	public void setSchoolYear(String schoolYear) {
		this.schoolYear = schoolYear;
	}
	public String getMotherCountry() {
		return motherCountry;
	}
	public void setMotherCountry(String motherCountry) {
		this.motherCountry = motherCountry;
	}
	public String getFatherCountry() {
		return fatherCountry;
	}
	public void setFatherCountry(String fatherCountry) {
		this.fatherCountry = fatherCountry;
	}
	public List<Tuition> getTuitions() {
		return tuitions;
	}
	public void setTuitions(List<Tuition> tuitions) {
		this.tuitions = tuitions;
	}
	public double getTotalAmount() {
		return totalAmount;
	}
	public void setTotalAmount(double totalAmount) {
		this.totalAmount = totalAmount;
	}
	public double getTotalPaid() {
		return totalPaid;
	}
	public void setTotalPaid(double totalPaid) {
		this.totalPaid = totalPaid;
	}
	public double getTotalRebate() {
		return totalRebate;
	}
	public void setTotalRebate(double totalRebate) {
		this.totalRebate = totalRebate;
	}
	public double getTotalBalance() {
		return totalBalance;
	}
	public void setTotalBalance(double totalBalance) {
		this.totalBalance = totalBalance;
	}
	public boolean isShowDetails() {
		return showDetails;
	}
	public void setShowDetails(boolean showDetails) {
		this.showDetails = showDetails;
	}
	
	public boolean isHasPastDueAmount() {
		return hasPastDueAmount;
	}
	public void setHasPastDueAmount(boolean hasPastDueAmount) {
		this.hasPastDueAmount = hasPastDueAmount;
	}
	public List<TuitionType> getTuitionTypes() {
		return tuitionTypes;
	}
	public void setTuitionTypes(List<TuitionType> tuitionTypes) {
		this.tuitionTypes = tuitionTypes;
	}
}
