package com.esoft.ischool.model;

import java.util.Date;

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

import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import org.hibernate.validator.Length;
import org.hibernate.validator.NotNull;
import org.hibernate.validator.Past;
import com.esoft.ischool.security.model.User;

@Entity
@Table(name="TEACHER")
public class Teacher extends BaseEntity {

	@Id
	@Column(name = "TEACHER_ID", unique = true, nullable = false)
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@NotNull
	@Column(name = "CITY")
	private String city;

	@NotNull
	@Column(name = "CITY_OF_BIRTH")
	private String birthCity;

	@NotNull
	@ManyToOne
	@JoinColumn(name = "COUNTRY_ID")
	private Country country;

	@NotNull
	@ManyToOne
	@JoinColumn(name = "COUNTRY_OF_BIRTH")
	private Country birthCountry;
	
	
	@NotNull
	@Length(max = 50)
	@Column(name = "FIRST_NAME")
	private String firstName;

	@NotNull
	@Length(max = 50)
	@Column(name = "LAST_NAME")
	private String lastName;

	@NotNull
	@Length(max = 10)
	@Column(name = "MATRICULE")
	private String matricule;

	@Length(max = 50)
	@Column(name = "MIDDLE_NAME")
	private String middleName;

	@Length(max = 50)
	@Column(name = "NICK_NAME")
	private String nickName;

	@NotNull
	@Past
	@Column(name = "BIRTH_DATE")
	private Date birthDate;

	@Length(max = 100)
	@NotNull
	@Column(name = "ADDRESS")
	private String address;

	@Length(max = 50)
	@Column(name = "E_MAIL")
	private String email;

	@NotNull
	@Column(name = "PHONE")
	private String phone;

	@Column(name = "CELL_PHONE")
	private String cellPhone;

	@Column(name = "COMMENTS")
	private String comments;
	
	@Column(name="RESUME")
	private String resume;

	@OneToOne
	@JoinColumn(name = "USER_ID", nullable = true)
	private User user;

	@Column(name = "image")
	private byte[] image;

	private Short status = 1;

	@Column(name = "HIRED_DATE")
	private Date hiredDate;
	
	@Column(name="ALLERGY")
	private String allergy;
	
	@ManyToOne
	@JoinColumn(name = "SCH_RELIGION_ID", nullable = true)
	@Fetch(FetchMode.JOIN)
	private SchoolReligion religion;
	
	@NotNull
	@Column(name="SEX")
	private String sex;
	
	//I use this only for display
	@Transient
	private String last;

	public Teacher () {}
	
	public Teacher(Long id) {
		this.id = id;
	}
	public String getLast() {
		return last;
	}

	public void setLast(String last) {
		this.last = last;
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

	public String getResume() {
		return resume;
	}

	public void setResume(String resume) {
		this.resume = resume;
	}

	public String getMatricule() {
		return matricule;
	}

	public void setMatricule(String matricule) {
		this.matricule = matricule;
	}

	public Date getHiredDate() {
		return hiredDate;
	}

	public void setHiredDate(Date hiredDate) {
		this.hiredDate = hiredDate;
	}

	public Short getStatus() {
		return status;
	}

	public void setStatus(Short status) {
		this.status = status;
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

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public String getComments() {
		return comments;
	}

	public void setComments(String comments) {
		this.comments = comments;
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

	@Override
	public String toString() {
		return matricule + " " + firstName + " " + lastName +" " + id;
	}

	@Override
	public boolean equals(Object obj) {
		Teacher other = (Teacher) obj;

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
}
