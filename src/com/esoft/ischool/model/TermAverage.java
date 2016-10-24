package com.esoft.ischool.model;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.List;

import javax.faces.context.FacesContext;
import javax.servlet.ServletContext;
import javax.sql.DataSource;

import org.springframework.context.ApplicationContext;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import com.esoft.ischool.service.BaseService;

public class TermAverage {
	private String termName;
	private Bulletin  bulletin;
	private String formattedMark;
	private List<Averages> averages;
	
	
	public String getTermName() {
		return termName;
	}
	public void setTermName(String termName) {
		this.termName = termName;
	}
	
	public Bulletin getBulletin() {
		return bulletin;
	}
	public void setBulletin(Bulletin bulletin) {
		this.bulletin = bulletin;
	}
	
	public String getFormattedMark() {
		FacesContext context = FacesContext.getCurrentInstance();
		ApplicationContext ctx = org.springframework.web.jsf.FacesContextUtils.getWebApplicationContext(context);
		Config config = (Config) ctx.getBean("config");
		DecimalFormat df = new DecimalFormat(config.getConfigurationByGroupAndName("SCHOOL", "MARK_FORMAT"));
		return df.format(bulletin.getMark());
	}
	public void setFormattedMark(String formattedMark) {
		this.formattedMark = formattedMark;
	}
	public List<Averages> getAverages() {
		return averages;
	}
	public void setAverages(List<Averages> averages) {
		this.averages = averages;
	}
}
