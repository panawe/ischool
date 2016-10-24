package com.esoft.ischool.model;

import java.util.Date;

import javax.faces.context.FacesContext;
import javax.persistence.Column;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.MappedSuperclass;

import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode; 
import org.springframework.context.ApplicationContext;

import com.esoft.ischool.dao.BaseDao;

@MappedSuperclass
public abstract class BaseEntity {

	 
	@ManyToOne
	@JoinColumn(name = "SCHOOL_ID", nullable = false)
	@Fetch(FetchMode.JOIN)
	private School school;

	@Column(name = "CREATE_DATE")
	private Date createDate = new Date();
	
	@Column(name = "MOD_DATE")
	private Date modDate = new Date();

	@Column(name = "MOD_BY")
	private Long modifiedBy;

	public abstract Long getId() ;

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

	public Long getModifiedBy() {
		return modifiedBy;
	}

	public void setModifiedBy(long modifiedBy) {
		this.modifiedBy = modifiedBy;
	}

	public School getSchool() {
		return school;
	}

	public void setSchool(School school) {
		this.school = school;
	}

	@Override
	public String toString() {
		return "BaseEntity [createDate=" + createDate + ", modDate=" + modDate
				+ ", modifiedBy=" + modifiedBy + "]";
	}

	
	public BaseDao getBaseDao() {
		FacesContext context = FacesContext.getCurrentInstance();
		ApplicationContext ctx = org.springframework.web.jsf.FacesContextUtils
		.getWebApplicationContext(context);
		BaseDao baseDao = (BaseDao) ctx.getBean("baseDao");
		return baseDao;
	}
}
