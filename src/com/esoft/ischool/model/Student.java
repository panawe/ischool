package com.esoft.ischool.model;

import java.io.IOException;
import java.io.OutputStream;
import java.io.Serializable;
import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.apache.commons.lang.StringUtils;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import com.esoft.ischool.security.model.User;
import com.esoft.ischool.util.Constants;

@Entity
@Table(name = "STUDENT")
public class Student extends BaseEntity implements Serializable {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "STUDENT_ID", unique = true, nullable = false)
	private Long id;

	//@NotNull
	@Column(name = "CITY")
	private String city="Lome";

	//@NotNull
	@Length(max = 10)
	private String matricule;

	//@NotNull
	@Column(name = "CITY_OF_BIRTH")
	private String birthCity="Lome";
	
	//@NotNull
	@ManyToOne
	@JoinColumn(name = "COUNTRY_ID")
	private Country country;

	//@NotNull
	@ManyToOne
	@JoinColumn(name = "COUNTRY_OF_BIRTH")
	private Country birthCountry;
		
	//@NotNull
	//@Length(max = 50)
	@Column(name = "FIRST_NAME")
	private String firstName;

	//@NotNull
	//@Length(max = 50)
	@Column(name = "LAST_NAME")
	private String lastName;

	//@Length(max = 50)
	@Column(name = "MIDDLE_NAME")
	private String middleName;

	//@Length(max = 50)
	@Column(name = "NICK_NAME")
	private String nickName;

	@Column(name = "BIRTH_DATE")
	private Date birthDate;

	//@NotNull
	//@Length(max = 100)
	@Column(name = "ADDRESS")
	private String address;

	//@Length(max = 50)
	@Column(name = "EMAIL")
	private String email;

	@Column(name = "PHONE")
	private String phone;

	@Column(name = "CELL_PHONE")
	private String cellPhone;

	@Column(name = "COMMENTS")
	private String comments;

	@Column(name = "DECISION")
	private String decision;

	@OneToOne
	@JoinColumn(name = "USER_ID", nullable = true)
	private User user;

	@Column(name = "image")
	private byte[] image;

	@Transient
	private Double mark;

	@Transient
	private String grade;

	@Transient
	private Long approvedBy;

	@Column(name = "STATUS")
	private Short status = 1;

	@OneToOne
	@JoinColumn(name = "ENROLLMENT_ID", nullable = true)
	private StudentEnrollment currentEnrollment;

	@Column(name = "ALLERGY")
	private String allergy;


	@ManyToOne
	@JoinColumn(name = "SCH_RELIGION_ID", nullable = true)
	@Fetch(FetchMode.JOIN)
	private SchoolReligion religion;

	@Column(name = "SEX")
	private String sex;

	@Transient
	private String statusText;
	
	@Transient
	private String parentType;

	@ManyToOne
	@Fetch(FetchMode.JOIN)
	@JoinColumn(name = "LEVEL_ID", nullable = true)
	private Level level;

	@Transient
	private String birthCountryName;
	
	@Transient
	private String countryName;
	
	@Transient
	private String levelClassName;
	
	@Transient
	private String schoolYearName;
	
	@Transient 
	private String schoolReligionName;
	
	
	@Transient
	private String fatherLastName;
	@Transient
	private String fatherFirstName;
	@Transient
	private String fatherMiddleName;
	@Transient
	private String fatherProfession;
	@Transient
	private String fatherWorkPlace;
	@Transient
	private String fatherAddress;
	@Transient
	private String fatherCity;
	@Transient
	private String fatherCountry;
	@Transient
	private String fatherEmail;
	@Transient
	private String fatherPhone;
	

	@Transient
	private String motherLastName;
	@Transient
	private String motherFirstName;
	@Transient
	private String motherMiddleName;
	@Transient
	private String motherProfession;
	@Transient
	private String motherWorkPlace;
	@Transient
	private String motherAddress;
	@Transient
	private String motherCity;
	@Transient
	private String motherCountry;
	@Transient
	private String motherEmail;
	@Transient
	private String motherPhone;
			

	@Transient
	private String tutorLastName;
	@Transient
	private String tutorFirstName;
	@Transient
	private String tutorMiddleName;
	@Transient
	private String tutorProfession;
	@Transient
	private String tutorWorkPlace;
	@Transient
	private String tutorAddress;
	@Transient
	private String tutorCity;
	@Transient
	private String tutorCountry;
	@Transient
	private String tutorEmail;
	@Transient
	private String tutorPhone;
	
	@Transient
	private boolean selectedForPayment;
	
	@Transient
	private List<Tuition> tuitions;
	
	@Transient
	private Long tuitionCount;
	
	@Transient
	private List<TuitionType> tuitionTypes;
	
	@Transient
	private Long tuitionTypeCount;
	
	@Transient
	private double totalAmount;
	
	@Transient
	private double totalPaid;
	
	@Transient
	private double totalRebate;
	
	@Transient
	private double totalBalance;
	
	@Transient
	private double totalAmountByType;
	
	@Transient
	private double totalPaidByType;
	
	@Transient
	private double totalRebateByType;
	
	@Transient
	private double totalBalanceByType;
	
	public Student() {}
	
	public Student(Long id) {
		this.id = id;
	}
	
	public Level getLevel() {
		return level;
	}

	public void setLevel(Level level) {
		this.level = level;
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

	public void setStatusText(String statusText) {
		this.statusText = statusText;
	}

	public String getDecision() {
		return decision;
	}

	public void setDecision(String decision) {
		this.decision = decision;
	}

	public String getSex() {
		return sex;
	}

	public void setSex(String sex) {
		this.sex = sex;
	}

	public SchoolReligion getReligion() {
		return religion;
	}

	public void setReligion(SchoolReligion religion) {
		this.religion = religion;
	}

	public String getAllergy() {
		return allergy;
	}

	public void setAllergy(String allergy) {
		this.allergy = allergy;
	}

	public String getMatricule() {
		return matricule;
	}

	public void setMatricule(String matricule) {
		this.matricule = matricule;
	}

	public Short getStatus() {
		return status;
	}

	public void setStatus(Short status) {
		this.status = status;
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
	}

	public Long getApprovedBy() {
		return approvedBy;
	}

	public void setApprovedBy(Long approvedBy) {
		this.approvedBy = approvedBy;
	}

	public StudentEnrollment getCurrentEnrollment() {
		return currentEnrollment;
	}

	public void setCurrentEnrollment(StudentEnrollment currentEnrollment) {
		this.currentEnrollment = currentEnrollment;
	}

	public String getGrade() {
		return grade;
	}

	public void setGrade(String grade) {
		this.grade = grade;
	}

	public Double getMark() {
		return mark;
	}

	public void setMark(Double mark) {
		this.mark = mark;
	}

	public byte[] getImage() {
		return image;
	}

	public void setImage(byte[] image) {
		this.image = image;
	}

	@Override
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
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

	public String getComments() {
		return comments;
	}

	public void setComments(String comments) {
		this.comments = comments;
	}

	@Override
	public String toString() {
		return matricule + "|" + firstName + "|" + lastName + "|" + parentType + "|" + id;
	}

	@Override
	public boolean equals(Object obj) {
		Student other = (Student) obj;
		if (this.getId() != null && other.getId() != null) {
			if (this.getId().equals(other.getId()))
				return true;
		} else {
			if (this.getMatricule() != null && other.getMatricule() != null)
				if (this.getMatricule().equals(other.getMatricule()))
					return true;
		}

		return false;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getBirthCity() {
		return birthCity;
	}

	public void setBirthCity(String birthCity) {
		this.birthCity = birthCity;
	}

	public Country getCountry() {
		return country;
	}

	public void setCountry(Country country) {
		this.country = country;
	}

	public Country getBirthCountry() {
		return birthCountry;
	}

	public void setBirthCountry(Country birthCountry) {
		this.birthCountry = birthCountry;
	}
	
	public String getParentType() {
		return parentType;
	}

	public void setParentType(String parentType) {
		this.parentType = parentType;
	}

	public String getBirthCountryName() {
		return birthCountryName;
	}

	public void setBirthCountryName(String birthCountryName) {
		this.birthCountryName = birthCountryName;
	}

	public String getCountryName() {
		return countryName;
	}

	public void setCountryName(String countryName) {
		this.countryName = countryName;
	}

	public String getLevelClassName() {
		return levelClassName;
	}

	public void setLevelClassName(String levelClassName) {
		this.levelClassName = levelClassName;
	}

	public String getSchoolYearName() {
		return schoolYearName;
	}

	public void setSchoolYearName(String schoolYearName) {
		this.schoolYearName = schoolYearName;
	}

	public String getSchoolReligionName() {
		return schoolReligionName;
	}

	public void setSchoolReligionName(String schoolReligionName) {
		this.schoolReligionName = schoolReligionName;
	}
	
	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public String validateForExport () {
		StringBuffer buffer = new StringBuffer();
		buffer.append(validate(matricule, "Matricule ")); 
		buffer.append(validate(lastName, "Nom ")); 
		buffer.append(validate(firstName, "Prenom "));
		buffer.append(validate(fatherLastName, "Nom du pere ")); 
		buffer.append(validate(fatherFirstName, "Prenom du pere"));
		buffer.append(validate(motherLastName, "Nom de la mere ")); 
		buffer.append(validate(motherFirstName, "Prenom de la mere"));
		buffer.append(validate(sex, "Sexe "));
		buffer.append(validate(birthDate, "Date de naissance "));
		buffer.append(validate(birthCity, "Ville de Naissance "));
		buffer.append(validate(birthCountryName, "Pays de naissance ")); 
		buffer.append(validate(city, "Ville de residence ")); 
		buffer.append(validate(countryName, "Pays de residence "));
		buffer.append(validate(address, "Adresse "));
		buffer.append(validate(levelClassName, "Classe ")); 
		buffer.append(validate(schoolYearName, "Annee scolaire ")); 
				
		return buffer.toString();
	}
	
	private String validate(Object value, String field) {
		String message = "";
		if (value== null || StringUtils.isEmpty(value.toString())) {
			message = field + "is required.";
		}
		return message;
	}

	public String getFatherLastName() {
		return fatherLastName;
	}

	public void setFatherLastName(String fatherLastName) {
		this.fatherLastName = fatherLastName;
	}

	public String getFatherFirstName() {
		return fatherFirstName;
	}

	public void setFatherFirstName(String fatherFirstName) {
		this.fatherFirstName = fatherFirstName;
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

	public String getFatherCountry() {
		return fatherCountry;
	}

	public void setFatherCountry(String fatherCountry) {
		this.fatherCountry = fatherCountry;
	}

	public String getFatherEmail() {
		return fatherEmail;
	}

	public void setFatherEmail(String fatherEmail) {
		this.fatherEmail = fatherEmail;
	}

	public String getFatherPhone() {
		return fatherPhone;
	}

	public void setFatherPhone(String fatherPhone) {
		this.fatherPhone = fatherPhone;
	}

	public String getMotherLastName() {
		return motherLastName;
	}

	public void setMotherLastName(String motherLastName) {
		this.motherLastName = motherLastName;
	}

	public String getMotherFirstName() {
		return motherFirstName;
	}

	public void setMotherFirstName(String motherFirstName) {
		this.motherFirstName = motherFirstName;
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

	public String getMotherCountry() {
		return motherCountry;
	}

	public void setMotherCountry(String motherCountry) {
		this.motherCountry = motherCountry;
	}

	public String getMotherEmail() {
		return motherEmail;
	}

	public void setMotherEmail(String motherEmail) {
		this.motherEmail = motherEmail;
	}

	public String getMotherPhone() {
		return motherPhone;
	}

	public void setMotherPhone(String motherPhone) {
		this.motherPhone = motherPhone;
	}

	public String getTutorLastName() {
		return tutorLastName;
	}

	public void setTutorLastName(String tutorLastName) {
		this.tutorLastName = tutorLastName;
	}

	public String getTutorFirstName() {
		return tutorFirstName;
	}

	public void setTutorFirstName(String tutorFirstName) {
		this.tutorFirstName = tutorFirstName;
	}

	public String getTutorProfession() {
		return tutorProfession;
	}

	public void setTutorProfession(String tutorProfession) {
		this.tutorProfession = tutorProfession;
	}

	public String getTutorWorkPlace() {
		return tutorWorkPlace;
	}

	public void setTutorWorkPlace(String tutorWorkPlace) {
		this.tutorWorkPlace = tutorWorkPlace;
	}

	public String getTutorAddress() {
		return tutorAddress;
	}

	public void setTutorAddress(String tutorAddress) {
		this.tutorAddress = tutorAddress;
	}

	public String getTutorCity() {
		return tutorCity;
	}

	public void setTutorCity(String tutorCity) {
		this.tutorCity = tutorCity;
	}

	public String getTutorCountry() {
		return tutorCountry;
	}

	public void setTutorCountry(String tutorCountry) {
		this.tutorCountry = tutorCountry;
	}

	public String getTutorEmail() {
		return tutorEmail;
	}

	public void setTutorEmail(String tutorEmail) {
		this.tutorEmail = tutorEmail;
	}

	public String getTutorPhone() {
		return tutorPhone;
	}

	public void setTutorPhone(String tutorPhone) {
		this.tutorPhone = tutorPhone;
	}

	public String getFatherMiddleName() {
		return fatherMiddleName;
	}

	public void setFatherMiddleName(String fatherMiddleName) {
		this.fatherMiddleName = fatherMiddleName;
	}

	public String getMotherMiddleName() {
		return motherMiddleName;
	}

	public void setMotherMiddleName(String motherMiddleName) {
		this.motherMiddleName = motherMiddleName;
	}

	public String getTutorMiddleName() {
		return tutorMiddleName;
	}

	public void setTutorMiddleName(String tutorMiddleName) {
		this.tutorMiddleName = tutorMiddleName;
	}

	public boolean isSelectedForPayment() {
		return selectedForPayment;
	}

	public void setSelectedForPayment(boolean selectedForPayment) {
		this.selectedForPayment = selectedForPayment;
	}

	public List<Tuition> getTuitions() {
		return tuitions;
	}

	public void setTuitions(List<Tuition> tuitions) {
		this.tuitions = tuitions;
	}

	public List<TuitionType> getTuitionTypes() {
		return tuitionTypes;
	}

	public void setTuitionTypes(List<TuitionType> tuitionTypes) {
		this.tuitionTypes = tuitionTypes;
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

	public Long getTuitionCount() {
		return tuitions != null ? new Long(tuitions.size()) : new Long(0);
	}

	public void setTuitionCount(Long tuitionCount) {
		this.tuitionCount = tuitionCount;
	}
	
	public Long getTuitionTypeCount() {
		return tuitionTypes != null ? new Long(tuitionTypes.size()) : new Long(0);
	}

	public void setTuitionTypeCount(Long tuitionTypeCount) {
		this.tuitionTypeCount = tuitionTypeCount;
	}

	public double getTotalAmountByType() {
		return totalAmountByType;
	}

	public void setTotalAmountByType(double totalAmountByType) {
		this.totalAmountByType = totalAmountByType;
	}

	public double getTotalPaidByType() {
		return totalPaidByType;
	}

	public void setTotalPaidByType(double totalPaidByType) {
		this.totalPaidByType = totalPaidByType;
	}

	public double getTotalRebateByType() {
		return totalRebateByType;
	}

	public void setTotalRebateByType(double totalRebateByType) {
		this.totalRebateByType = totalRebateByType;
	}

	public double getTotalBalanceByType() {
		return totalBalanceByType;
	}

	public void setTotalBalanceByType(double totalBalanceByType) {
		this.totalBalanceByType = totalBalanceByType;
	}
}
