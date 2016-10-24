package com.esoft.ischool.restservice;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.faces.context.FacesContext;
import javax.servlet.ServletContext;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;

import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperRunManager;

import org.richfaces.event.UploadEvent;
import org.richfaces.model.UploadItem;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.esoft.ischool.model.Country;
import com.esoft.ischool.model.School;
import com.esoft.ischool.model.SchoolLevel;
import com.esoft.ischool.model.SchoolReligion;
import com.esoft.ischool.model.SchoolType;
import com.esoft.ischool.service.SchoolService;
import com.esoft.ischool.util.Constants;
import com.esoft.ischool.util.MenuIdEnum;
import com.esoft.ischool.util.SimpleMail;
import com.esoft.ischool.util.Utils;

@Component("schoolBean")
@Scope("session")
public class SchoolBean extends BaseBean {

	@Autowired
	@Qualifier("schoolService")
	private SchoolService schoolService;
	private Long rowCount;
	private List<School> schools;
	private List<String> allSchools = new ArrayList<String>();
	private String countryName;
	private String schoolTypeName;
	private String schoolReligionName;
	private String schoolLevelName;
	private Double random;
	private String selectedTab = "schoolDetail";
	private String subSelectedTab = "";
	private byte[] idCard;
	private byte[] reportHdrLeft;
	private byte[] reportHdrRight;
	
	
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

	@Override
	public String getSelectedTab() {
		return selectedTab;
	}

	@Override
	public void setSelectedTab(String selectedTab) {
		this.selectedTab = selectedTab;
	}

	@Override
	public Double getRandom() {
		return random;
	}

	@Override
	public void setRandom(Double random) {
		this.random = random;
	}

	private School school = new School();

	public String validate() {
		return "succes";
	}

	public String setSchoolLevelClassTab() {
		//setSelectedTab("schoolLevelClass");
		return "";
	}
	
	public List<School> autoComplete(Object suggest) {
		String pref = (String) suggest;
		List<School> result = new ArrayList<School>();
		allSchools.clear();

		for (School s : schools) {

			if ((s.getName() != null && s.getName().toLowerCase()
					.indexOf(pref.toLowerCase()) == 0)
					|| "".equals(pref))
				result.add(s);

			allSchools.add(s.getName());
		}

		return result;
	}

	@Override
	public String clear() {
		school = new School();
		countryName = "";
		schoolTypeName = "";
		schoolReligionName = "";
		schoolLevelName = "";
		setPicture(null);
		setIdCard(null);
		setReportHdrRight(null);
		setReportHdrLeft(null);
		return "Success";
	}

    public void listenerIdCard(UploadEvent event) throws Exception{
        UploadItem item = event.getUploadItem();
        idCard = item.getData();
    }  
	public void paintIdCard(OutputStream stream, Object object)
			throws IOException {
		if (idCard != null) {
			stream.write(idCard);
		} else {
			stream.write(new byte[] {});
		}
	}
	
    public void listenerReportHdrLeft(UploadEvent event) throws Exception{
        UploadItem item = event.getUploadItem();
        reportHdrLeft = item.getData();
    }  
	public void paintReportHdrLeft(OutputStream stream, Object object)
			throws IOException {
		if (reportHdrLeft != null) {
			stream.write(reportHdrLeft);
		} else {
			stream.write(new byte[] {});
		}
	}
	
    public void listenerReportHdrRight(UploadEvent event) throws Exception{
        UploadItem item = event.getUploadItem();
        reportHdrRight = item.getData();
    }  
	public void paintReportHdrRight(OutputStream stream, Object object)
			throws IOException {
		if (reportHdrRight != null) {
			stream.write(reportHdrRight);
		} else {
			stream.write(new byte[] {});
		}
	}


	public byte[] getIdCard() {
		return idCard;
	}

	public void setIdCard(byte[] idCard) {
		this.idCard = idCard;
	}

	public String getCountryName() {
		return countryName;
	}

	public void setCountryName(String countryName) {
		this.countryName = countryName;
	}

	public String delete() {
		clearMessages();
		try {
			if (schoolService.getSchool(getIdParameter()) != null) {
				schoolService.delete(schoolService.getSchool(getIdParameter()));
				schoolService.getTheFuckOutOfHereBastard(schoolService
						.getSchool(getIdParameter()));
				getAll();
				setSuccessMessage(getResourceBundle().getString("DELETE_SUCCESSFULLY"));
			}
		} catch (Exception ex) {
			ex.printStackTrace();
			setErrorMessage(SCHOOL_DELETE_UNSUCCESSFULL);
		}

		return "Success";
	}

	public String insert() {
		clearMessages();
		Long id = school.getId();
		try {

			Country country = (Country) schoolService.findByColumn(Country.class, "name", countryName);
			if (country == null) {
				setSuccessMessage(getResourceBundle().getString("RESIDENCE_COUNTRY_NOT_FOUND"));
				return "ERROR";
			}
			SchoolType schoolType = (SchoolType) schoolService.findByName(
					SchoolType.class, schoolTypeName,
					schoolService.getDefaultSchool());

			SchoolReligion schoolReligion = (SchoolReligion) schoolService
					.findByName(SchoolReligion.class, schoolReligionName,
							schoolService.getDefaultSchool());

			SchoolLevel schoolLevel = (SchoolLevel) schoolService.findByName(
					SchoolLevel.class, schoolLevelName,
					schoolService.getDefaultSchool());

			school.setCountry(country);
			school.setSchoolType(schoolType);
			school.setSchoolReligion(schoolReligion);
			school.setSchoolLevel(schoolLevel);

			if (getPicture() != null) {
				school.setLogo(getPicture());
				if(school.getLogo().length>MAX_FILE_SIZE){
					setErrorMessage(MAX_SIZE_EXCEEDED);
					return "ERROR";
				}
			}

			if (getIdCard() != null) {
				school.setIdCardBack(getIdCard());
				if(school.getIdCardBack().length>MAX_FILE_SIZE){
					setErrorMessage(MAX_SIZE_EXCEEDED);
					return "ERROR";
				}
			}
			
			if (getReportHdrLeft() != null) {
				school.setReportHdrLeft(getReportHdrLeft());
				if(school.getReportHdrLeft().length>MAX_FILE_SIZE){
					setErrorMessage(MAX_SIZE_EXCEEDED);
					return "ERROR";
				}
			}
			if (getReportHdrRight() != null) {
				school.setReportHdrRight(getReportHdrRight());
				if(school.getReportHdrRight().length>MAX_FILE_SIZE){
					setErrorMessage(MAX_SIZE_EXCEEDED);
					return "ERROR";
				}
			}
			if (id == null || id == 0)
				schoolService.saveSchool(school);
			else
				schoolService.updateSchool(school);
			setSuccessMessage(getResourceBundle().getString("SAVED_SUCCESSFULLY"));

		} catch (Exception ex) {
			school.setId(id);
			setErrorMessage(ex, ex.getMessage());
		}

		return "Success";
	}

	public String createSchool() {
		clearMessages();
		Long id = school.getId();
		try {

			Country country = (Country) schoolService.findByColumn(Country.class, "name", countryName);
			if (country == null) {
				setSuccessMessage(getResourceBundle().getString("RESIDENCE_COUNTRY_NOT_FOUND"));
				return "ERROR";
			}

			SchoolType schoolType = (SchoolType) schoolService.findByName(
					SchoolType.class, schoolTypeName,
					schoolService.getDefaultSchool());

			SchoolReligion schoolReligion = (SchoolReligion) schoolService
					.findByName(SchoolReligion.class, schoolReligionName,
							schoolService.getDefaultSchool());

			SchoolLevel schoolLevel = (SchoolLevel) schoolService.findByName(
					SchoolLevel.class, schoolLevelName,
					schoolService.getDefaultSchool());

			school.setCountry(country);
			school.setSchoolType(schoolType);
			school.setSchoolReligion(schoolReligion);
			school.setSchoolLevel(schoolLevel);

			if (getPicture() != null) {
				school.setLogo(getPicture());
				if(school.getLogo().length>MAX_FILE_SIZE){
					setErrorMessage(MAX_SIZE_EXCEEDED);
					return "ERROR";
				}
			}

			if (getIdCard() != null) {
				school.setIdCardBack(getIdCard());
				if(school.getIdCardBack().length>MAX_FILE_SIZE){
					setErrorMessage(MAX_SIZE_EXCEEDED);
					return "ERROR";
				}
			}
			
			if (getReportHdrLeft() != null) {
				school.setReportHdrLeft(getReportHdrLeft());
				if(school.getReportHdrLeft().length>MAX_FILE_SIZE){
					setErrorMessage(MAX_SIZE_EXCEEDED);
					return "ERROR";
				}
			}
			if (getReportHdrRight() != null) {
				school.setReportHdrRight(getReportHdrRight());
				if(school.getReportHdrRight().length>MAX_FILE_SIZE){
					setErrorMessage(MAX_SIZE_EXCEEDED);
					return "ERROR";
				}
			}
			if (id == null || id == 0) {

				String adminPassword = Utils.getRandomString(8);
				schoolService.copyGoldData(school, getCurrentUser(),
						adminPassword);
				try {
					Map<String, String> config = (Map<String, String>) getSessionParameter("configuration");
					if (config != null) {
						SimpleMail.sendMail(
								Constants.SCHOOL_CREATED,
								createWelcomeMessage(
										config.get("SCHOOL_WEBSITE"),
										adminPassword, school), config
										.get("SCHOOL_SENDER_EMAIL"), school
										.getEmail(), config
										.get("SCHOOL_SMTP_SERVER"), config
										.get("SCHOOL_MAIL_SERVER_USER"), config
										.get("SCHOOL_MAIL_SERVER_PASSWORD"));

						setSuccessMessage(SCHOOL_CREATED_WITH_MAIL);
					}
				} catch (Exception e) {

					setSuccessMessage(SCHOOL_CREATED_NO_MAIL);
				}

			} else {
				schoolService.updateSchool(school);
				setSuccessMessage(getResourceBundle().getString("SAVED_SUCCESSFULLY"));
			}

			getAll();
			clear();

		} catch (Exception ex) {
			ex.printStackTrace();
			school.setId(id);
			setErrorMessage(ex, ex.getMessage());
		}

		return "Success";
	}

	public String printSchoolFinance() {

		try {

			Map<String, Serializable> parameters = new HashMap<String, Serializable>();

				parameters
						.put("schoolId", getCurrentUser().getSchool().getId());
				FacesContext context = FacesContext.getCurrentInstance();

				String reportsDirPath = ((ServletContext) context
						.getExternalContext().getContext())
						.getRealPath("/reports/");

				parameters.put("SUBREPORT_DIR", reportsDirPath
						+ java.io.File.separator);
				HttpServletResponse response = (HttpServletResponse) context
						.getExternalContext().getResponse();

				InputStream reportStream = context.getExternalContext()
						.getResourceAsStream("/reports/etatFinancierSchool.jasper");

				ServletOutputStream ouputStream = response.getOutputStream();

				response.addHeader("Content-disposition",
						"attachment;filename=Etat Financier Ecole"+ "-"+getStringDate()+".pdf");

				JasperRunManager.runReportToPdfStream(reportStream,
						ouputStream, parameters, getConnection());
				response.setContentType("application/pdf");
				ouputStream.flush();
				ouputStream.close();
				context.responseComplete();
		
		} catch (JRException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return "success";

	}


	private String createWelcomeMessage(String url, String password,
			School school) {
		String message = "<p>Bonjour,<p>"
				+ "Nous sommes ravis de vous informer que votre Etablissement "
				+ school.getName()
				+ " est maintenant sur la plateforme iSchool.<br/>"
				+ "Pour vous y connecter rendez-vous a l'addresse suivante: "
				+ url + ".<br/>" + "Votre nom d'utilisateur est: "
				+ school.getEmail() + "\n" + "Votre mot de passe est: "
				+ password + "<br/>" + "Remerciements.";

		return message;

	}

	public String edit() {
		clearMessages();
		random = Math.random();
		school = schoolService.getBySchoolId(School.class,
				getIdParameter());
		if (school != null) {
			countryName=school.getCountry()==null?"":school.getCountry().getName();
			setPicture(school.getLogo());
			setIdCard(school.getIdCardBack());
			setReportHdrLeft(school.getReportHdrLeft());
			setReportHdrRight(school.getReportHdrRight());
			if (school.getSchoolLevel() != null)
				setSchoolLevelName(school.getSchoolLevel().getName());
			if (school.getSchoolType() != null)
				setSchoolTypeName(school.getSchoolType().getName());
			if (school.getSchoolReligion() != null)
				setSchoolReligionName(school.getSchoolReligion().getName());
		}
		selectedTab = "schoolsDetail";
		return "Success";
	}

	private void getAll() {
		schools = schoolService.loadAllSchool(School.class);
		setRowCount(new Long(schools.size()));

	}

	@PostConstruct
	public String getMySchool() {
		try {
			getAll();
			getAllSchools();
			if (getCurrentUser() == null)
				return "ERROR";
			school = getCurrentUser().getSchool();

			countryName=school.getCountry()==null?"":school.getCountry().getName();

			setPicture(school.getLogo());
			setIdCard(school.getIdCardBack());
			setReportHdrLeft(school.getReportHdrLeft());
			setReportHdrRight(school.getReportHdrRight());
			if (school.getSchoolLevel() != null)
				setSchoolLevelName(school.getSchoolLevel().getName());
			if (school.getSchoolType() != null)
				setSchoolTypeName(school.getSchoolType().getName());
			if (school.getSchoolReligion() != null)
				setSchoolReligionName(school.getSchoolReligion().getName());

		} catch (Exception e) {
			e.printStackTrace();
		}
		return "success";
	}

	public boolean isUserHasWriteAccess() {
		return isUserHasWriteAccess(MenuIdEnum.SCHOOL.getValue());
	}

	public SchoolService getSchoolService() {
		return schoolService;
	}

	public void setSchoolService(SchoolService schoolService) {
		this.schoolService = schoolService;
	}

	public Long getRowCount() {
		return rowCount;
	}

	public void setRowCount(Long rowCount) {
		this.rowCount = rowCount;
	}

	public List<School> getSchools() {
		return schools;
	}

	public void setSchools(List<School> schools) {
		this.schools = schools;
	}

	public String getSchoolTypeName() {
		return schoolTypeName;
	}

	public void setSchoolTypeName(String schoolTypeName) {
		this.schoolTypeName = schoolTypeName;
	}

	public String getSchoolReligionName() {
		return schoolReligionName;
	}

	public void setSchoolReligionName(String schoolReligionName) {
		this.schoolReligionName = schoolReligionName;
	}

	public String getSchoolLevelName() {
		return schoolLevelName;
	}

	public void setSchoolLevelName(String schoolLevelName) {
		this.schoolLevelName = schoolLevelName;
	}

	public School getSchool() {
		return school;
	}

	public void setSchool(School school) {
		this.school = school;
	}

	public List<String> getAllSchools() {
		autoComplete("");
		return allSchools;
	}

	public void setAllSchools(List<String> allSchools) {
		this.allSchools = allSchools;
	}

	public String getSubSelectedTab() {
		return subSelectedTab;
	}

	public void setSubSelectedTab(String subSelectedTab) {
		this.subSelectedTab = subSelectedTab;
	}

	
}
