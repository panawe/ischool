package com.esoft.ischool.security.model;

import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import com.esoft.ischool.model.BaseEntity;
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
	
	@Column(name="CAN_APPROVE")
	private Short canApprove=0;
	
	@Column(name="CURRENT_LOCALE")
	private String currentLocale;

	@Column(name="CSV_DELIMITER")
	private String csvDelimiter;
	
	@Transient
	private boolean teacher=false;
	
	@Transient
	private boolean student=false;
	
	@Transient
	private String allergy;
	
	@Transient
	private byte[] image;

	public byte[] getImage() {
		return image;
	}

	public String getAllergy() {
		return allergy;
	}

	public void setAllergy(String allergy) {
		this.allergy = allergy;
	}

	public void setImage(byte[] image) {
		this.image = image;
	}

	public boolean getCanApprove() {
		return canApprove==1?true:false;
	}

	public void setCanApprove(boolean canApprove) {
		this.canApprove = canApprove==true?(short)1:0;
	}

	public boolean isTeacher() {
		return teacher;
	}

	public void setTeacher(boolean teacher) {
		this.teacher = teacher;
	}

	public boolean isStudent() {
		return student;
	}

	public void setStudent(boolean student) {
		this.student = student;
	}

	@Override
	public Long getId() {
		return id;
	}
	@OneToMany
	@JoinColumn(name = "USER_ID")
	@Fetch(FetchMode.SELECT)
	private Set<RolesUser> rolesUser;
	
	private Short status=1;
	
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

	public Set<RolesUser> getRolesUser() {
		return rolesUser;
	}

	public void setRolesUser(Set<RolesUser> rolesUser) {
		this.rolesUser = rolesUser;
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
	
	public String getCurrentLocale() {
		return currentLocale;
	}

	public void setCurrentLocale(String currentLocale) {
		this.currentLocale = currentLocale;
	}

	public String getCsvDelimiter() {
		return csvDelimiter;
	}

	public void setCsvDelimiter(String csvDelimiter) {
		this.csvDelimiter = csvDelimiter;
	}

	@Override
	public boolean equals(Object o) {
		
		if (this == o)
			return true;
		if (o == null)
			return false;
		if (!(o instanceof User))
			return false;
		User that = (User) o;
		return getId().equals(that.getId()); 
	}

	@Override
	public String toString(){
		return id + " " + userName + " " + firstName + " " + lastName;
	}

}
