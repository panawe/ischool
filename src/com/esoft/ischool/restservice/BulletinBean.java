package com.esoft.ischool.restservice;

import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.sql.Connection;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.faces.context.FacesContext;
import javax.servlet.ServletContext;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;

import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperRunManager;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.esoft.ischool.job.Config;
import com.esoft.ischool.model.Averages;
import com.esoft.ischool.model.LevelClass;
import com.esoft.ischool.model.SchoolYear;
import com.esoft.ischool.model.Term;
import com.esoft.ischool.model.TermGroup;
import com.esoft.ischool.model.YearSummary;
import com.esoft.ischool.service.BaseService;
import com.esoft.ischool.util.AverageStatus;
import com.esoft.ischool.util.MenuIdEnum;

@Component("bulletinBean")
@Scope("session")
public class BulletinBean extends BaseBean {

	@Autowired
	@Qualifier("baseService")
	private BaseService baseService;
	private Long rowCount;
	private String year;
	private String className;
	private String termName;
	private String termGroupName;

	public String getTermGroupName() {
		return termGroupName;
	}

	public void setTermGroupName(String termGroupName) {
		this.termGroupName = termGroupName;
	}

	public String getTermName() {
		return termName;
	}

	public void setTermName(String termName) {
		this.termName = termName;
	}

	public String getYear() {
		return year;
	}

	public void setYear(String year) {
		this.year = year;
	}

	public String getClassName() {
		return className;
	}

	public void setClassName(String className) {
		this.className = className;
	}

	public String validate() {
		return "succes";
	}

	public BaseService getBaseService() {
		return baseService;
	}

	public void setBaseService(BaseService baseService) {
		this.baseService = baseService;
	}

	public Long getRowCount() {
		return rowCount;
	}

	public void setRowCount(Long rowCount) {
		this.rowCount = rowCount;
	}

	public String doNothing() {
		return "Success";
	}

	public String calculateTermAverages() {
		clearMessages();
		try {
			if (year != null && !year.equals("")) {
				SchoolYear schoolYear = (SchoolYear) baseService.findByColumn(
						SchoolYear.class, "year", year,
						baseService.getDefaultSchool());
				YearSummary yearSummary = baseService.getOneYearSummary(schoolYear,getCurrentUser());
				FacesContext context = FacesContext.getCurrentInstance();
				ApplicationContext ctx = org.springframework.web.jsf.FacesContextUtils
						.getWebApplicationContext(context);
				Config config = (Config) ctx.getBean("config");
				if (yearSummary != null) { // delete an calculate
					if (yearSummary.getStatus()
							.equals(AverageStatus.CREATED_LOCKED.getValue())
							|| yearSummary.getStatus().equals(
									AverageStatus.PUBLISHED_LOCKED.getValue())) {// locked
						setErrorMessage(AVERAGE_CALCULATION_FAILED+" "+RESULTS_LOCKED);
						return "Error";
					}else{
						baseService.calculateYearSummary(schoolYear,getCurrentUser(), config, className);
						setSuccessMessage(AVERAGE_CALCULATION_SUCCESSFUL);
					}
				} else {
					baseService.calculateYearSummary(schoolYear,getCurrentUser(), config, className);
					setSuccessMessage(AVERAGE_CALCULATION_SUCCESSFUL);
				}
			} else {
				setErrorMessage(ALL_FIELDS_REQUIRED);
				return "error";
			}
		} catch (Exception ex) {
			ex.printStackTrace();
			setErrorMessage(ex, getResourceBundle().getString("SAVED_UNSUCCESSFULL"));
			return "error";
		}
		return "success";
	}
	
	public String calculateYearAverages() {
		clearMessages();
		try {
			if (year != null && !year.equals("")) {
				SchoolYear schoolYear = (SchoolYear) baseService.findByColumn(
						SchoolYear.class, "year", year,
						baseService.getDefaultSchool());
				YearSummary yearSummary = baseService.getOneYearSummary(schoolYear,getCurrentUser());
				FacesContext context = FacesContext.getCurrentInstance();
				ApplicationContext ctx = org.springframework.web.jsf.FacesContextUtils
						.getWebApplicationContext(context);
				Config config = (Config) ctx.getBean("config");
				if (yearSummary != null) { // delete an calculate
					if (yearSummary.getStatus()
							.equals(AverageStatus.CREATED_LOCKED.getValue())
							|| yearSummary.getStatus().equals(
									AverageStatus.PUBLISHED_LOCKED.getValue())) {// locked
						setErrorMessage(AVERAGE_CALCULATION_FAILED+" "+RESULTS_LOCKED);
						return "Error";
					}else{
						baseService.calculateYearSummary(schoolYear,getCurrentUser(),config, className);
						setSuccessMessage(AVERAGE_CALCULATION_SUCCESSFUL);
					}
				} else {
					baseService.calculateYearSummary(schoolYear,getCurrentUser(),config, className);
					setSuccessMessage(AVERAGE_CALCULATION_SUCCESSFUL);
				}
			} else {
				setErrorMessage(ALL_FIELDS_REQUIRED);
				return "error";
			}
		} catch (Exception ex) {
			ex.printStackTrace();
			setErrorMessage(ex, getResourceBundle().getString("SAVED_UNSUCCESSFULL"));
			return "error";
		}
		return "success";
	}
	
	public String calculateGroupAverages() {
		clearMessages();
		try {
			if (year != null && !year.equals("")) {
				SchoolYear schoolYear = (SchoolYear) baseService.findByColumn(
						SchoolYear.class, "year", year,
						baseService.getDefaultSchool());

				TermGroup termGroup = (TermGroup) baseService.findByColumn(TermGroup.class, "name",
						 termGroupName, baseService.getDefaultSchool());
				YearSummary yearSummary = baseService.getOneYearSummary(schoolYear,getCurrentUser());
				FacesContext context = FacesContext.getCurrentInstance();
				ApplicationContext ctx = org.springframework.web.jsf.FacesContextUtils
						.getWebApplicationContext(context);
				Config config = (Config) ctx.getBean("config");
				if (yearSummary != null) { // delete an calculate
					if (yearSummary.getStatus()
							.equals(AverageStatus.CREATED_LOCKED.getValue())
							|| yearSummary.getStatus().equals(
									AverageStatus.PUBLISHED_LOCKED.getValue())) {// locked
						setErrorMessage(AVERAGE_CALCULATION_FAILED+" "+RESULTS_LOCKED);
						return "Error";
					}else{
						baseService.calculateGroupSummary(schoolYear, termGroup, getCurrentUser(), config, className);
						setSuccessMessage(AVERAGE_CALCULATION_SUCCESSFUL);
					}
				} else {
					baseService.calculateGroupSummary(schoolYear, termGroup, getCurrentUser(), config, className);
					setSuccessMessage(AVERAGE_CALCULATION_SUCCESSFUL);
				}
			} else {
				setErrorMessage(ALL_FIELDS_REQUIRED);
				return "error";
			}
		} catch (Exception ex) {
			ex.printStackTrace();
			setErrorMessage(ex, getResourceBundle().getString("SAVED_UNSUCCESSFULL"));
			return "error";
		}
		return "success";
	}
	
	public String calculateAverages() {
		clearMessages();
		try {
			if (year != null && !year.equals("") && termName != null
					&& !termName.equals("") && className != null
					&& !className.equals("")) {
				SchoolYear schoolYear = (SchoolYear) baseService.findByColumn(
						SchoolYear.class, "year", year,
						baseService.getDefaultSchool());
				LevelClass lclass = (LevelClass) baseService.findByColumn(
						LevelClass.class, "name", className, getCurrentUser()
								.getSchool());

				Term term = (Term) baseService.findByColumn(Term.class, "name",
						termName, baseService.getDefaultSchool());

				Averages average = baseService.getOneAverage(schoolYear,
						lclass, term);
				FacesContext context = FacesContext.getCurrentInstance();
				ApplicationContext ctx = org.springframework.web.jsf.FacesContextUtils
						.getWebApplicationContext(context);
				Config config = (Config) ctx.getBean("config");
				if (average != null) { // delete an calculate

					if (average.getStatus()
							.equals(AverageStatus.CREATED_LOCKED.getValue())
							|| average.getStatus().equals(
									AverageStatus.PUBLISHED_LOCKED.getValue())) {// locked
						setErrorMessage(AVERAGE_CALCULATION_FAILED+" "+RESULTS_LOCKED);
						return "Error";
					}else{
						baseService.calculateAverages(schoolYear,
						lclass, term, getCurrentUser(),config);
						setSuccessMessage(AVERAGE_CALCULATION_SUCCESSFUL);
					}

				} else {
					baseService.calculateAverages(schoolYear,
							lclass, term, getCurrentUser(),config);
					setSuccessMessage(AVERAGE_CALCULATION_SUCCESSFUL);
				}

			} else {
				setErrorMessage(ALL_FIELDS_REQUIRED);
				return "error";
			}

		
		} catch (Exception ex) {
			ex.printStackTrace();
			setErrorMessage(ex, getResourceBundle().getString("SAVED_UNSUCCESSFULL"));
			return "error";
		}
		return "success";
	}

	public String publishResults() {
		try {
			clearMessages();
			if (year != null && !year.equals("") && termName != null
					&& !termName.equals("") && className != null
					&& !className.equals("")) {
				SchoolYear schoolYear = (SchoolYear) baseService.findByColumn(
						SchoolYear.class, "year", year,
						baseService.getDefaultSchool());
				LevelClass lclass = (LevelClass) baseService.findByColumn(
						LevelClass.class, "name", className, getCurrentUser()
								.getSchool());

				Term term = (Term) baseService.findByColumn(Term.class, "name",
						termName, baseService.getDefaultSchool());

				Averages average = baseService.getOneAverage(schoolYear,
						lclass, term);
				if (average != null) { 

					if (average.getStatus()
							.equals(AverageStatus.PUBLISHED_UNLOCKED.getValue())
							|| average.getStatus().equals(
									AverageStatus.PUBLISHED_LOCKED.getValue())) {// locked
						setErrorMessage(RESULTS_ALREADY_PUBLISHED);
						return "Error";
					}else{
						baseService.publishResults(schoolYear,
						lclass, term, getCurrentUser());
						setSuccessMessage(RESULTS_PUBLISHED_SUCCESSFULLY);
					}

				} else {
					setErrorMessage(NO_RESULT_FOUND);
					return "error";
				}

			} else {
				setErrorMessage(ALL_FIELDS_REQUIRED);
				return "error";
			}

		} catch (Exception ex) {
			ex.printStackTrace();

			return "Error";
		}
		return "success";
	}

	public String lockUnlockResults() {
		try {
			clearMessages();
			if (year != null && !year.equals("") && termName != null
					&& !termName.equals("") && className != null
					&& !className.equals("")) {
				SchoolYear schoolYear = (SchoolYear) baseService.findByColumn(
						SchoolYear.class, "year", year,
						baseService.getDefaultSchool());
				LevelClass lclass = (LevelClass) baseService.findByColumn(
						LevelClass.class, "name", className, getCurrentUser()
								.getSchool());

				Term term = (Term) baseService.findByColumn(Term.class, "name",
						termName, baseService.getDefaultSchool());
				Averages average = baseService.getOneAverage(schoolYear,
						lclass, term);
				if (average != null) { // delete an calculate

					if (average.getStatus()
							.equals(AverageStatus.CREATED_LOCKED.getValue())
							|| average.getStatus().equals(
									AverageStatus.PUBLISHED_LOCKED.getValue())) {// locked
						baseService.lockUnlockResults(schoolYear,
								lclass, term, getCurrentUser());
						setSuccessMessage(RESULTS_UNLOCKED);
 
					}else{
						baseService.lockUnlockResults(schoolYear,
						lclass, term, getCurrentUser());
						setSuccessMessage(RESULTS_LOCKED);
					}

				} else {
					setErrorMessage(NO_RESULT_FOUND);
					return "error";
				}

			} else {
				setErrorMessage(ALL_FIELDS_REQUIRED);
				return "error";
			}


		} catch (Exception ex) {
			ex.printStackTrace();
			return "error";
		}
		return "success";
	}

	public String printBulletin() {
		try {

			Map<String, Serializable> parameters = new HashMap<String, Serializable>();
			rowCount = 0L;
			if (year != null && !year.equals("") && termName != null
					&& !termName.equals("") && className != null
					&& !className.equals("")) {
				SchoolYear schoolYear = (SchoolYear) baseService.findByColumn(
						SchoolYear.class, "year", year,
						baseService.getDefaultSchool());
				LevelClass lclass = (LevelClass) baseService.findByColumn(
						LevelClass.class, "name", className, getCurrentUser()
								.getSchool());

				Term term = (Term) baseService.findByColumn(Term.class, "name",
						termName, baseService.getDefaultSchool());

				rowCount = 1L;

				parameters.put("termId", term.getId());
				parameters.put("className", lclass.getName());
				parameters.put("yearId", schoolYear.getId());
				parameters.put("schoolId", getCurrentUser().getSchool().getId());

				FacesContext context = FacesContext.getCurrentInstance();
				String reportsDirPath = ((ServletContext) context
						.getExternalContext().getContext())
						.getRealPath("/reports/");

				parameters.put("SUBREPORT_DIR", reportsDirPath
						+ java.io.File.separator);
				HttpServletResponse response = (HttpServletResponse) context
						.getExternalContext().getResponse();

				InputStream reportStream = context.getExternalContext()
						.getResourceAsStream("/reports/bulletin.jasper");

				ServletOutputStream ouputStream = response.getOutputStream();

				response.addHeader("Content-disposition",
						"attachment;filename=" + termName + " " + year + " "
								+ className + "-"+getStringDate()+".pdf");

				JasperRunManager.runReportToPdfStream(reportStream,
						ouputStream, parameters, getConnection());
				response.setContentType("application/pdf");
				ouputStream.flush();
				ouputStream.close();
				context.responseComplete();
			}
		} catch (JRException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return "success";
	}

	@Override
	public Connection getConnection() {
		Connection jdbcConnection = null;

		FacesContext context = FacesContext.getCurrentInstance();

		ApplicationContext ctx = org.springframework.web.jsf.FacesContextUtils
				.getWebApplicationContext(context);

		DataSource dataSource = (DataSource) ctx.getBean("dataSource");
		try {
			jdbcConnection = dataSource.getConnection();
		} catch (Exception e) {
			e.printStackTrace();
		}

		return jdbcConnection;
	}

	public boolean isUserHasWriteAccess() {
		if ( ((String)getSessionParameter("link")).equals("impressionBulletin")) {
			return isUserHasWriteAccess(MenuIdEnum.IMPRIMER_BULLETIN.getValue());
		}else if ( ((String)getSessionParameter("link")).equals("imprimerBulletin")) {
			return isUserHasWriteAccess(MenuIdEnum.CALCUL_MOYENNE.getValue());
		}
		return false;
	}
	
	@PostConstruct
	public String doPost(){
	SchoolYear sy = baseService.getSchoolYear(new Date(),baseService.getDefaultSchool());
	year=sy==null?year:sy.getYear();
	return "";
	}
}
