package com.esoft.ischool.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.hibernate.validator.NotNull;

import com.esoft.ischool.model.BaseEntity;

@Entity
@Table(name="ASSIGNMENT_FILE")
public class AssignmentFile extends BaseEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name="ASSIGNMENT_FILE_ID")
	private Long id;

	@NotNull
	@Column(name="NAME")
	private String name;

	@Column(name="DESCRIPTION")
	private String description;
	
	@NotNull
	@Column(name = "CONTENT")
	private byte[] content;

	@NotNull
	@Column(name = "UPLOAD_BY")
	private Long uploadBy;
	
	@Column(name="FILE_TYPE")
	private String fileType;

	@Transient
	private String yearName;
	
	@Transient
	private String className;
	
	@Transient
	private String subjectName;
	
	@Transient
	private String examTypeName;

	@Transient
	private Date examDate;

	@Transient
	private Date returnDate;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public byte[] getContent() {
		return content;
	}

	public void setContent(byte[] content) {
		this.content = content;
	}

	public Long getUploadBy() {
		return uploadBy;
	}

	public void setUploadBy(Long uploadBy) {
		this.uploadBy = uploadBy;
	}

	public String getFileType() {
		return fileType;
	}

	public void setFileType(String fileType) {
		this.fileType = fileType;
	}
	
	public String getYearName() {
		return yearName;
	}

	public void setYearName(String yearName) {
		this.yearName = yearName;
	}

	public String getClassName() {
		return className;
	}

	public void setClassName(String className) {
		this.className = className;
	}

	public String getSubjectName() {
		return subjectName;
	}

	public void setSubjectName(String subjectName) {
		this.subjectName = subjectName;
	}

	
	public String getExamTypeName() {
		return examTypeName;
	}

	public void setExamTypeName(String examTypeName) {
		this.examTypeName = examTypeName;
	}

	public Date getExamDate() {
		return examDate;
	}

	public void setExamDate(Date examDate) {
		this.examDate = examDate;
	}

	public Date getReturnDate() {
		return returnDate;
	}

	public void setReturnDate(Date returnDate) {
		this.returnDate = returnDate;
	}

	public String getFileTypeImagePath() {
		if ("xlsx".equalsIgnoreCase(getFileType())||"xls".equalsIgnoreCase(getFileType())) 
        	return "/images/excelType.jpg";
        else if ("doc".equalsIgnoreCase(getFileType())|"docx".equalsIgnoreCase(getFileType())) 
    		return "/images/wordType.jpg";
        else if ("pdf".equalsIgnoreCase(getFileType()))
        	return "/images/pdfType.jpg";
        else if ("txt".equalsIgnoreCase(getFileType()))
        	return "/images/textType.jpg";
        else
        	return "";
	}
	
	public String getContentType() {
		if ("xlsx".equalsIgnoreCase(getFileType())||"xls".equalsIgnoreCase(getFileType())) 
        	return "application/vnd.ms-excel";
        else if ("doc".equalsIgnoreCase(getFileType())|"docx".equalsIgnoreCase(getFileType())) 
    		return "application/vnd.ms-word";
        else if ("pdf".equalsIgnoreCase(getFileType()))
        	return "application/pdf";
        else if ("txt".equalsIgnoreCase(getFileType()))
        	return "text/plain";
        else 
        	return "";
	}
}
