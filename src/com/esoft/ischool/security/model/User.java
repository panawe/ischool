package com.esoft.ischool.security.model;

import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.esoft.ischool.model.BaseEntity;
import com.esoft.ischool.model.Country;
import com.esoft.ischool.model.Position;

@Entity
@Table(name = "USERS")
public class User extends BaseEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "USER_ID", unique = true, nullable = false)
	private Long id;

	@ManyToOne
	@JoinColumn(name = "POSITION_ID", nullable = true)
	private Position position;

	@Column(name = "USER_NAME")
	private String userName;

	@Column(name = "PASSWORD")
	private String password;

	@Column(name = "FIRST_NAME")
	private String firstName;

	@Column(name = "LAST_NAME")
	private String lastName;

	@Column(name = "E_MAIL")
	private String email;

	@Column(name = "PHONE")
	private String phone;

	@Column(name = "ADDRESS")
	private String address;

	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "MM/dd/yyyy")
	@Column(name = "MEMBERSHIP_DATE")
	private Date membershipDate;
	
	@Column(name = "MEMBERSHIP_RENEW_DATE")
	private Date membershipRenewDate;	
	
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "MM/dd/yyyy")
	@Column(name="BIRTH_DATE")
	private Date birthDate;	

	@ManyToOne
	@JoinColumn(name = "COUNTRY_ORIGIN", nullable = true)
	private Country countryOrigin;
	
	@ManyToOne
	@JoinColumn(name = "COUNTRY_RESIDENCE", nullable = true)
	private Country countryResidence;
	
	@ManyToOne
	@JoinColumn(name = "MUM", nullable = true)
	private User mum;
	@ManyToOne
	@JoinColumn(name = "DAD", nullable = true)
	private User dad;
	
	@Column(name = "SEX")
	private String sex;
	
	@Column(name ="ALIVE")
	private Short alive=1;	

	@Column(name = "CITY_ORIGIN")
	private String cityOrigin;
	
	@Column(name = "CITY_RESIDENCE")
	private String cityResidence;
	
	@Transient
	private boolean isOnline;
	
	@Transient
	private String data;
	
	
	
	public String getData() {
		return data;
	}

	public void setData(String data) {
		this.data = data;
	}

	public Date getBirthDate() {
		return birthDate;
	}

	public void setBirthDate(Date birthDate) {
		this.birthDate = birthDate;
	}

	public Country getCountryOrigin() {
		return countryOrigin;
	}

	public void setCountryOrigin(Country countryOrigin) {
		this.countryOrigin = countryOrigin;
	}

	public Country getCountryResidence() {
		return countryResidence;
	}

	public void setCountryResidence(Country countryResidence) {
		this.countryResidence = countryResidence;
	}

	public String getCityOrigin() {
		return cityOrigin;
	}

	public void setCityOrigin(String cityOrigin) {
		this.cityOrigin = cityOrigin;
	}

	public String getCityResidence() {
		return cityResidence;
	}

	public void setCityResidence(String cityResidence) {
		this.cityResidence = cityResidence;
	}

	public String getSex() {
		return sex;
	}

	public void setSex(String sex) {
		this.sex = sex;
	}

	//Deceiced on the UI
	public boolean getAlive() {
		return alive == 1 ? false : true;
	}
	//Deceiced on the UI
	public void setAlive(boolean deceived) {
		this.alive = deceived == true ? (short) 0 : 1;
	}

	public User getMum() {
		return mum;
	}

	public void setMum(User mum) {
		this.mum = mum;
	}

	public User getDad() {
		return dad;
	}

	public void setDad(User dad) {
		this.dad = dad;
	}

	public Date getMembershipRenewDate() {
		return membershipRenewDate;
	}

	public void setMembershipRenewDate(Date membershipRenewDate) {
		this.membershipRenewDate = membershipRenewDate;
	}

	public Date getMembershipDate() {
		return membershipDate;
	}

	public void setMembershipDate(Date membershipDate) {
		this.membershipDate = membershipDate;
	}


	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}


	@Column(name = "CAN_APPROVE")
	private Short canApprove = 0;

	@Transient
	private Double fee=0.0;

	public Double getFee() {
		return fee;
	}

	public void setFee(Double fee) {
		this.fee = fee;
	}

	@Transient
	private byte[] image;

	public byte[] getImage() {
		return image;
	}


	public void setImage(byte[] image) {
		this.image = image;
	}

	public boolean getCanApprove() {
		return canApprove == 1 ? true : false;
	}

	public void setCanApprove(boolean canApprove) {
		this.canApprove = canApprove == true ? (short) 1 : 0;
	}



	@Override
	public Long getId() {
		return id;
	}
	

	private Short status = 0;

	private String pageSkin;

	public String getPageSkin() {
		return pageSkin;
	}

	public void setPageSkin(String pageSkin) {
		this.pageSkin = pageSkin;
	}

	public Short getStatus() {
		return status;
	}

	public void setStatus(Short status) {
		this.status = status;
	}


	public void setId(Long id) {
		this.id = id;
	}

	public Position getPosition() {
		return position;
	}

	public void setPosition(Position position) {
		this.position = position;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
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

	public boolean isOnline() {
		return isOnline;
	}

	public void setOnline(boolean isOnline) {
		this.isOnline = isOnline;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		User other = (User) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}

	
	@Override
	public String toString() {
		return id + " " + userName + " " + firstName + " " + lastName;
	}

	public User minimize(){
		if(this.mum!=null){
			this.mum.mum=null;
			this.mum.dad=null;
		}
		if(this.dad!=null){
			this.dad.mum=null;
			this.dad.dad=null;
		}
		return this;
	}
	
}
