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

import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

@Entity
@Table(name="SCHOOL")
public class School {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "SCHOOL_ID")
	private Long id;

	//@NotNull
	@Column(name = "CITY")
	private String city;
	
	//@NotNull
	@ManyToOne
	@JoinColumn(name = "COUNTRY_ID")
	private Country country;
	
	//@NotNull
	@ManyToOne
	@JoinColumn(name = "SCH_TYPE_ID", nullable = true)
	@Fetch(FetchMode.JOIN)
	private SchoolType schoolType;
	
	//@NotNull
	@ManyToOne
	@JoinColumn(name = "SCH_RELIGION_ID", nullable = true)
	@Fetch(FetchMode.JOIN)
	private SchoolReligion schoolReligion;
	
	//@NotNull
	@ManyToOne
	@JoinColumn(name = "SCH_LEVEL_ID", nullable = true)
	@Fetch(FetchMode.JOIN)
	private SchoolLevel schoolLevel;
	
	//@NotNull
	//@Length(max = 100)
	private String email;
	
	//@NotNull
	//@Length(max = 100)
	@Column(name = "NAME")
	private String name;
	
	//@NotNull
	//@Length(max = 100)
	@Column(name = "ADDRESS")
	private String address;
	
	//@NotNull
	//@Length(max = 15)
	@Column(name = "SHORT_NAME")
	private String shortName;
	
	//@NotNull
	@Column(name = "PHONE")
	private String phone;
	
	@Column(name = "LOGO")
	private byte[] logo;
	
	@Column(name = "ID_CARD_BACK")
	private byte[] idCardBack;
	
	@Column(name="REPORT_HDR_LEFT")
	private byte[] reportHdrLeft;
	
	@Column(name="REPORT_HDR_RIGHT")
	private byte[] reportHdrRight;
	

	@Column(name="REPORT_HEADER")
	private String reportHeader;
	
	@Column(name="LEFT_REPORT_HEADER")
	private String leftReportHeader;
	
	@Column(name="DIRECTOR")
	private String director;
	
	@Column(name="SLOGAN")
	private String slogan;
	
	@Column(name="INSPECTOR")
	private String inspector;
	
	@Column(name="SHOW_REPORT_HEADER")
	private Short showReportHeader=1;
	
	@Column(name="SHOW_DEFAULT_PASSWORD")
	private Short showDefaultPassword=0;
	
	@Column(name="GENERATE_RANDOM_PASSWORD")
	private Short generateRandomPassword=0;
	
	@Column(name="GENERATE_MATRICULE")
	private Short generateMatricule=0;
	
	@Column(name="DISPLAY_RANG")
	private Short displayRang=0;
	
	@Column(name = "CREATE_DATE")
	private Date modDate = new Date();
	@Column(name = "MOD_DATE")
	private Date createDate = new Date();
	@Column(name ="MOD_BY")	
	private Integer modifiedBy;
	
	@Column(name="BAREM_PRIMAIRE")
	private Integer baremPrimaire=10;
	
	@Column(name="BAREM_COLLEGE")
	private Integer baremCollege=20;
	
	
	public String getSlogan() {
		return slogan;
	}

	public void setSlogan(String slogan) {
		this.slogan = slogan;
	}

	public boolean getDisplayRang() {
		return displayRang==1?true:false;
	}

	public void setDisplayRang(boolean displayRang) {
		this.displayRang = displayRang==true? (short) 1 :0;
	}

	public Integer getBaremPrimaire() {
		return baremPrimaire;
	}

	public void setBaremPrimaire(Integer baremPrimaire) {
		this.baremPrimaire = baremPrimaire;
	}

	public Integer getBaremCollege() {
		return baremCollege;
	}

	public void setBaremCollege(Integer baremCollege) {
		this.baremCollege = baremCollege;
	}

	public byte[] getReportHdrLeft() {
		return reportHdrLeft;
	}

	public void setReportHdrLeft(byte[] reportHdrLeft) {
		this.reportHdrLeft = reportHdrLeft;
	}

	public byte[] getReportHdrRight() {
		return reportHdrRight;
	}

	public void setReportHdrRight(byte[] reportHdrRight) {
		this.reportHdrRight = reportHdrRight;
	}

	public String getShortName() {
		return shortName;
	}

	public void setShortName(String shortName) {
		this.shortName = shortName;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public Date getModDate() {
		return modDate;
	}

	public void setModDate(Date modDate) {
		this.modDate = modDate;
	}

	public Date getCreateDate() {
		return createDate;
	}

	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}

	public Integer getModifiedBy() {
		return modifiedBy;
	}

	public void setModifiedBy(Integer modifiedBy) {
		this.modifiedBy = modifiedBy;
	}

	public String getReportHeader() {
		return reportHeader;
	}

	public void setReportHeader(String reportHeader) {
		this.reportHeader = reportHeader;
	}

	public String getLeftReportHeader() {
		return leftReportHeader;
	}

	public void setLeftReportHeader(String leftReportHeader) {
		this.leftReportHeader = leftReportHeader;
	}

	public String getDirector() {
		return director;
	}

	public void setDirector(String director) {
		this.director = director;
	}

	public String getInspector() {
		return inspector;
	}

	public void setInspector(String inspector) {
		this.inspector = inspector;
	}

	public boolean getShowReportHeader() {
		return showReportHeader==1?true:false;
	}

	public void setShowReportHeader(boolean showReportHeader) {
		this.showReportHeader = showReportHeader==true? (short) 1 :0;
	}

	
	public boolean getGenerateMatricule() {
		return generateMatricule==1?true:false;
	}

	public void setGenerateMatricule(boolean generateMat) {
		this.generateMatricule = generateMat==true? (short) 1 :0;
	}
	
	public boolean getShowDefaultPassword() {
		return showDefaultPassword==1?true:false;
	}

	public void setShowDefaultPassword(boolean showDefaultPassword) {
		this.showDefaultPassword = showDefaultPassword==true? (short) 1 :0;
	}

	public boolean getGenerateRandomPassword() {
		return generateRandomPassword==1?true:false;
	}

	public void setGenerateRandomPassword(boolean generateRandomPassword) {
		this.generateRandomPassword = generateRandomPassword==true? (short) 1 :0;
	}

	public byte[] getLogo() {
		return logo;
	}

	public void setLogo(byte[] logo) {
		this.logo = logo;
	}


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

	public Country getCountry() {
		return country;
	}

	public void setCountry(Country country) {
		this.country = country;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public SchoolType getSchoolType() {
		return schoolType;
	}

	public void setSchoolType(SchoolType schoolType) {
		this.schoolType = schoolType;
	}

	public SchoolReligion getSchoolReligion() {
		return schoolReligion;
	}

	public void setSchoolReligion(SchoolReligion schoolReligion) {
		this.schoolReligion = schoolReligion;
	}

	public SchoolLevel getSchoolLevel() {
		return schoolLevel;
	}

	public void setSchoolLevel(SchoolLevel schoolLevel) {
		this.schoolLevel = schoolLevel;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Override
	public boolean equals(Object obj) {
		// TODO Auto-generated method stub
		if(obj instanceof School){
			return ((School)obj).getId().equals(id);
		}
		return false;
	}

	public byte[] getIdCardBack() {
		return idCardBack;
	}

	public void setIdCardBack(byte[] idCardBack) {
		this.idCardBack = idCardBack;
	}
	
	
}
