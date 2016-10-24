package com.esoft.ischool.model;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.apache.commons.lang.StringUtils; 

import com.esoft.ischool.vo.StudentVO;

@Entity
@Table(name="PARENT")
public class Parent extends BaseEntity {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name="PARENT_ID")
	private Long id;
	
	@Column(name="LAST_NAME")
	private String lastName;

	@Column(name="FIRST_NAME")
	private String firstName;

	@Column(name="MIDDLE_NAME")
	private String middleName;

	@Column(name="ADDRESS")
	private String address;

	@Column(name="CITY")
	private String city;
	
	@Column(name="EMAIL")
	private String email;
	
	@Column(name="TELEPHONE")
	private String telephone;
	
	@Column(name="PROFESSION")
	private String profession;
	
	@Column(name="WORKPLACE")
	private String workPlace;

	@Column(name = "IMAGE")
	private byte[] image;

	@ManyToOne
	@JoinColumn(name = "COUNTRY_ID")
	private Country country;

	@Transient
	private String parentType;
	
	@Transient
	private String parentTypeText;
	
	@Transient 
	private double totalAmount;
	
	@Transient 
	private double totalPaid;
	
	@Transient 
	private double totalRebate;
	
	@Transient 
	private double totalBalance;
	
	@Transient
	private List<StudentVO> studentVOs;
	
	@Transient 
	private Long studentVOCount;
	
	public Parent(){}
	public Parent(String firstName, String lastName, String middleName, String address, String city, Country country, 
			String telephone, String email, String profession, String workPlace)
	{
		this.firstName = firstName;
		this.lastName = lastName;
		this.middleName = middleName;
		this.address = address;
		this.city = city;
		this.country = country;
		this.telephone = telephone;
		this.email = email;
		this.profession = profession;
		this.workPlace = workPlace;
	}

	@Override
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getMiddleName() {
		return middleName;
	}

	public void setMiddleName(String middleName) {
		this.middleName = middleName;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getTelephone() {
		return telephone;
	}

	public void setTelephone(String telephone) {
		this.telephone = telephone;
	}

	public String getProfession() {
		return profession;
	}

	public void setProfession(String profession) {
		this.profession = profession;
	}

	public String getWorkPlace() {
		return workPlace;
	}

	public void setWorkPlace(String workPlace) {
		this.workPlace = workPlace;
	}

	public Country getCountry() {
		return country;
	}

	public void setCountry(Country country) {
		this.country = country;
	}

	public byte[] getImage() {
		return image;
	}

	public void setImage(byte[] image) {
		this.image = image;
	}

	public String getParentType() {
		return parentType;
	}

	public void setParentType(String parentType) {
		this.parentType = parentType;
	}
	
	public String getParentTypeText() {
		return parentTypeText;
	}

	public void setParentTypeText(String parentTypeText) {
		this.parentTypeText = parentTypeText;
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
	
	
	public List<StudentVO> getStudentVOs() {
		return studentVOs;
	}
	public void setStudentVOs(List<StudentVO> studentVOs) {
		this.studentVOs = studentVOs;
	}
	
	
	
	public Long getStudentVOCount() {
		return studentVOs != null ? new Long(studentVOs.size()) : new Long(0);
	}
	public void setStudentVOCount(Long studentVOCount) {
		this.studentVOCount = studentVOCount;
	}
	@Override
	public String toString() {
		return toPrint(firstName) + "|" + toPrint(lastName) + "|" + toPrint(profession) + "|" + toPrint(workPlace) + "|" + toPrint(parentType) + "|" + id;
	}
	
	@Override
	public boolean equals(Object obj) {
		Parent other = (Parent) obj;
		if (this.getId() != null && other.getId() != null) {
			if (this.getId().equals(other.getId()))
				return true;
		} 

		return false;
	}

	
	private String toPrint(String value) {
		return StringUtils.isNotBlank(value) ? value : " ";
	}
}
