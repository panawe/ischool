package com.esoft.ischool.restservice;

import java.io.IOException;
import java.io.OutputStream;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import javax.annotation.PostConstruct;
import javax.faces.context.FacesContext;
import javax.faces.model.SelectItem;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.esoft.ischool.job.Config;
import com.esoft.ischool.model.BaseEntity;
import com.esoft.ischool.model.Country;
import com.esoft.ischool.model.SchoolReligion;
import com.esoft.ischool.model.Teacher;
import com.esoft.ischool.security.bean.UserBean;
import com.esoft.ischool.service.BaseService;
import com.esoft.ischool.util.MenuIdEnum;
import com.esoft.ischool.vo.StudentVO;
import com.esoft.ischool.vo.TeacherVO;

@Component("teacherBean")
@Scope("session")
public class TeacherBean extends BaseBean {

	@Autowired
	@Qualifier("baseService")
	private BaseService baseService;
	private Long rowCount;
	private List<TeacherVO> teachers;
	private String cityName;
	private String countryName;
	private String birthCityName;
	private String birthCountryName;
	private Teacher teacher = new Teacher();
	private List<SelectItem> tList = new ArrayList<SelectItem>();
	private Object fullName;
	private boolean displayHeader;
	private boolean teacherSelected = false;
	private boolean success = true;
	private Long teacherId;
	private String matricule;
	private String religionName;
	private List<BaseEntity> medicalVisits;

	private boolean selectAllTeachers;

	public String getReligionName() {
		return religionName;
	}

	public List<BaseEntity> getMedicalVisits() {
		return medicalVisits;
	}

	public void setMedicalVisits(List<BaseEntity> medicalVisits) {
		this.medicalVisits = medicalVisits;
	}

	public void setReligionName(String religionName) {
		this.religionName = religionName;
	}

	public String getMatricule() {
		return matricule;
	}

	public void setMatricule(String matricule) {
		this.matricule = matricule;
	}

	public Long getTeacherId() {
		return teacherId;
	}

	public void setTeacherId(Long teacherId) {
		this.teacherId = teacherId;
	}

	public boolean isSuccess() {
		return success;
	}

	public void setSuccess(boolean success) {
		this.success = success;
	}

	public boolean isTeacherSelected() {
		return teacherSelected;
	}

	public void setTeacherSelected(boolean teacherSelected) {
		this.teacherSelected = teacherSelected;
	}

	public List<SelectItem> getTeacherList() {

		getAll();
		tList = new ArrayList<SelectItem>();
		if (teachers == null || teachers.size() == 0)
			return tList;

		if (teachers != null && !teachers.isEmpty()) {
			for (TeacherVO b : teachers) {
				TeacherVO t = (TeacherVO) b;
				tList.add(new SelectItem(t.getId(), t.getLastName() + " "
						+ t.getFirstName() + " " + t.getNickName()));
			}
		}
		return tList;
	}

	public List<String> getTeacherMLF() {

		getAll();
		ArrayList<String> teachersMLF = new ArrayList<String>();
		if (teachers == null || teachers.size() == 0)
			return teachersMLF;

		for (TeacherVO b : teachers) {
			TeacherVO t = (TeacherVO) b;
			teachersMLF.add(t.getMatricule() + ", " + t.getLastName() + ", "
					+ t.getFirstName());
		}

		return teachersMLF;
	}

	public void paintTeacher(OutputStream stream, Object object)
			throws IOException {
		if (teacher != null && teacher.getImage() != null) {
			stream.write(teacher.getImage());
		} else {
			stream.write(new byte[] {});
		}
	}

	public void paintTeacherById(OutputStream stream, Object object)
			throws IOException {
		Long id = (Long)object;
		if(id>0){
			Teacher t=(Teacher)baseService.getById(Teacher.class, id);
			if(t!=null &&t.getImage()!=null){
				stream.write(t.getImage());
			}else{
				stream.write(new byte[] {});
			}
		}
	}

	public String validate() {
		return "succes";
	}

	@Override
	public String clear() {
		teacher = new Teacher();
		cityName = "";
		countryName = "";
		birthCityName = "";
		birthCountryName = "";
		fullName = "";
		religionName = "";
		countryName = "";
		birthCountryName = "";
		setPicture(null);
		teacherSelected = false;
		return "Success";
	}

	public String delete() {
		clearMessages();
		try {
			baseService.delete(getIdParameter(), Teacher.class);
			int i = 0;
			for (TeacherVO be : teachers) {
				if (be.getId().equals(getIdParameter())) {
					teachers.remove(i);
					break;
				}
				i++;
			}
			setSuccessMessage(getResourceBundle().getString(
					"DELETE_SUCCESSFULLY"));
		} catch (Exception ex) {
			if (ex.getMessage() != null
					&& ex.getCause().getCause().getMessage()
							.contains("Cannot delete or update a parent row"))
				setErrorMessage("La suppression du professeur n'a pas marche parce qu'il est lie a d'autre donnees dans le system");
			else
				setErrorMessage(getResourceBundle().getString(
						"DELETE_UNSUCCESSFULL"));
		}

		return "Success";
	}

	public String insert() {
		clearMessages();
		Long id = teacher.getId();
		try {

			Country country = (Country) baseService.findByColumn(Country.class,
					"name", countryName);
			if (country == null) {
				setSuccessMessage(getResourceBundle().getString(
						"RESIDENCE_COUNTRY_NOT_FOUND"));
				return "ERROR";
			}
			teacher.setCountry(country);

			Country birthCountry = (Country) baseService.findByColumn(
					Country.class, "name", birthCountryName);
			if (birthCountry == null) {
				setSuccessMessage(getResourceBundle().getString(
						"BIRTH_COUNTRY_NOT_FOUND"));
				return "ERROR";
			}
			teacher.setBirthCountry(birthCountry);

			SchoolReligion religion = (SchoolReligion) baseService.findByName(
					SchoolReligion.class, religionName,
					baseService.getDefaultSchool());

/*			if (religion == null) {
				setErrorMessage(getErrorMessage() + ". "
						+ getResourceBundle().getString("RELIGION_NOT_FOUND"));
			}*/

			if (teacher.getBirthDate().after(new Date())) {
				setErrorMessage(getErrorMessage() + ". "
						+ getResourceBundle().getString("BIRTH_IS_FUTURE"));
			}
			if (teacher.getBirthDate().after(teacher.getHiredDate())) {
				setErrorMessage(getErrorMessage() + ". "
						+ getResourceBundle().getString("BIRTH_AFTER_HIRE"));
			}

			if (StringUtils.isNotEmpty(getErrorMessage()))
				return "ERROR";

			teacher.setReligion(religion);

			// get picture
			if (getPicture() != null) {
				teacher.setImage(getPicture());
				if (teacher.getImage().length > MAX_FILE_SIZE) {
					setErrorMessage(MAX_SIZE_EXCEEDED);
					return "ERROR";
				}
			}

			FacesContext context = FacesContext.getCurrentInstance();
			ApplicationContext ctx = org.springframework.web.jsf.FacesContextUtils
					.getWebApplicationContext(context);
			Config config = (Config) ctx.getBean("config");

			if (id == null || id == 0) {
				if (getCurrentUser().getSchool().getGenerateMatricule()) {
					teacher.setMatricule(baseService.generateMatricule(
							Teacher.class, teacher.getFirstName(),
							teacher.getLastName(), teacher.getBirthDate()));
				}
				baseService.saveTeacher(teacher, config, getCurrentUser());
			} else {
				baseService.update(teacher, getCurrentUser());
			}
			setSuccessMessage(getResourceBundle().getString(
					"SAVED_SUCCESSFULLY"));
			setSessionAttribute("currentTeacherId", teacher.getId());
			fullName = teacher.getLastName().toUpperCase() + " "
					+ teacher.getFirstName();
			displayHeader = true;
			teacherSelected = true;
		} catch (Exception ex) {
			ex.printStackTrace();
			teacher.setId(id);
			setErrorMessage(ex, "Cet enseignant exist deja. ");
		}

		return "Success";
	}

	public Object getFullName() {
		return fullName;
	}

	public void setFullName(Object fullName) {
		this.fullName = fullName;
	}

	public boolean isDisplayHeader() {
		return displayHeader;
	}

	public void setDisplayHeader(boolean displayHeader) {
		this.displayHeader = displayHeader;
	}

	public String edit() {
		clearMessages();
		setSelectedTab("teacherDetails");
		UserBean bean = (UserBean) getSessionParameter("userBean");
		bean.setPositionName(null);
		teacher = (Teacher) baseService
				.getById(Teacher.class, getIdParameter());
		religionName = teacher.getReligion()==null?null:teacher.getReligion().getName();
		fullName = teacher.getLastName().toUpperCase() + " "
				+ teacher.getFirstName();
		displayHeader = true;
		countryName = teacher.getCountry() == null ? "" : teacher.getCountry()
				.getName();
		birthCountryName = teacher.getBirthCountry() == null ? "" : teacher
				.getBirthCountry().getName();

		if (teacher.getUser().getPosition() != null) {
			bean.setPositionName(teacher.getUser().getPosition().getName());
		}

		setPicture(teacher.getImage());
		setSessionAttribute("currentTeacherId", getIdParameter());

		differentSchool = !getCurrentUser().getSchool().getId()
				.equals(teacher.getSchool().getId());
		teacherSelected = true;
		return "Success";
	}

	private boolean differentSchool;
	private String lastName;
	private String firstName;

	public boolean isDifferentSchool() {
		return differentSchool;
	}

	public void setDifferentSchool(boolean differentSchool) {
		this.differentSchool = differentSchool;
	}

	public String getSearchedTeachers() {
		if (selectAllTeachers)
			getAll();
		else if (StringUtils.isNotBlank(matricule)
				|| StringUtils.isNotBlank(lastName)
				|| StringUtils.isNotBlank(firstName))
			teachers = baseService.getTeachers(Teacher.class, matricule,
					lastName, firstName, getCurrentUser());
		rowCount = new Long((teachers != null) ? teachers.size() : 0);
		return "Success";
	}

	public String updateSearchCriteria() {
		return "";
	}

	private void getAll() {
		teachers = baseService.getTeachers(Teacher.class, null, null, null, getCurrentUser());
		setRowCount(new Long(teachers.size()));

	}

	@PostConstruct
	public void getAllTeachers() {
		if (getCurrentUser() == null) {
			teachers = baseService.getTeachersOrderByPosition(Teacher.class, null, null, null, getCurrentUser(), true, baseService.getDefaultSchool());
			int i=1;
			for(TeacherVO t : teachers){
				TeacherVO tt = (TeacherVO)t;
				if(i%4 == 0){
					tt.setLast("last");
				}else{
					tt.setLast("first");
				}
				i++;
			}
			setRowCount(new Long(teachers.size()));
		}
	}

	public void generateCSVReport ()
	{
		try
		{
			FacesContext context = getContext();
			HttpServletResponse response = (HttpServletResponse) context
					.getExternalContext().getResponse();
	
			ServletOutputStream ouputStream = response.getOutputStream();
			
			HttpServletRequest request = (HttpServletRequest) context
					.getExternalContext().getRequest();
			
			Locale locale = request.getLocale();
			DateFormat df = DateFormat.getDateInstance(DateFormat.LONG, locale);
			
			String delimiter = getDelimiter(locale);
			
			StringBuffer totalString =new StringBuffer();
			StringBuffer header=new StringBuffer();
			header.append(getResourceBundle().getString("matricule") + delimiter);
			header.append(getResourceBundle().getString("lastName") + delimiter);
			header.append(getResourceBundle().getString("firstName") + delimiter);
			header.append(getResourceBundle().getString("middleName") + delimiter);
			header.append(getResourceBundle().getString("nickName") + delimiter);
			header.append(getResourceBundle().getString("hiredDate") + delimiter);
			header.append(getResourceBundle().getString("address") + delimiter);
			header.append(getResourceBundle().getString("residenceCity") + delimiter);
			header.append(getResourceBundle().getString("residenceCountry") + delimiter);
			header.append(getResourceBundle().getString("email") + delimiter);
			header.append(getResourceBundle().getString("phone") + delimiter);
			header.append(getResourceBundle().getString("cellPhone") + delimiter);
			header.append(getResourceBundle().getString("birthDate") + delimiter);
			header.append(getResourceBundle().getString("birthCity") + delimiter);
			header.append(getResourceBundle().getString("birthCountry") + delimiter);
			header.append(getResourceBundle().getString("comments") + delimiter);
			header.append(getResourceBundle().getString("schoolReligion") + delimiter);
			header.append(getResourceBundle().getString("sex"));

			StringBuffer body=new StringBuffer();
			for (TeacherVO teacher : teachers) {
				body.append(printString(teacher.getMatricule()));
				body.append(delimiter);
				body.append(printString(teacher.getLastName()));
				body.append(delimiter);
				body.append(printString(teacher.getFirstName()));
				body.append(delimiter);
				body.append(printString(teacher.getMiddleName()));
				body.append(delimiter);
				body.append(printString(teacher.getNickName()));
				body.append(delimiter);
				body.append(printString(teacher.getHireDate(), df));
				body.append(delimiter);
				body.append(printString(teacher.getAddress()));
				body.append(delimiter);
				body.append(printString(teacher.getCity()));
				body.append(delimiter);
				body.append(printString(teacher.getCountryName()));
				body.append(delimiter);
				body.append(printString(teacher.getEmail()));
				body.append(delimiter);
				body.append(printString(teacher.getPhone()));
				body.append(delimiter);
				body.append(printString(teacher.getCellPhone()));
				body.append(delimiter);
				body.append(printString(teacher.getBirthDate(), df));
				body.append(delimiter);
				body.append(printString(teacher.getBirthCity()));
				body.append(delimiter);
				body.append(printString(teacher.getBirthCountry()));
				body.append(delimiter);
				body.append(printString(teacher.getComments()));
				body.append(delimiter);
				body.append(printString(teacher.getReligion()));
				body.append(delimiter);
				body.append(printString(teacher.getSex()));
				body.append("\n");
			}

			totalString.append(header.toString());
			totalString.append("\n");
			totalString.append(body.toString());

			response.addHeader("Content-disposition",
					"attachment;filename=professeur"+ "-"+getStringDate()+".csv");
			response.setContentType("application/csv");
			ouputStream.write(totalString.toString().getBytes());
			ouputStream.flush();
			ouputStream.close();
			context.responseComplete();
	}
		catch (Exception exception)
		{
			exception.printStackTrace() ;
		}
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

	public String getCountryName() {
		return countryName;
	}

	public void setCountryName(String countryName) {
		this.countryName = countryName;
	}

	public List<TeacherVO> getTeachers() {
		return teachers;
	}

	public void setTeachers(List<TeacherVO> teachers) {
		this.teachers = teachers;
	}

	public String getCityName() {
		return cityName;
	}

	public void setCityName(String cityName) {
		this.cityName = cityName;
	}

	public Teacher getTeacher() {
		return teacher;
	}

	public void setTeacher(Teacher teacher) {
		this.teacher = teacher;
	}

	public String getBirthCityName() {
		return birthCityName;
	}

	public void setBirthCityName(String birthCityName) {
		this.birthCityName = birthCityName;
	}

	public String getBirthCountryName() {
		return birthCountryName;
	}

	public void setBirthCountryName(String birthCountryName) {
		this.birthCountryName = birthCountryName;
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

	public String selectTeacher() {
		teacherId = getIdParameter();
		return "success";
	}

	public String importTeacherInfo() {
		clearMessages();
		try {
			success = true;
			Teacher st = (Teacher) baseService
					.getById(Teacher.class, teacherId);
			st.setSchool(getCurrentUser().getSchool());
			st.setStatus((short) 1);
			baseService.update(st, getCurrentUser());
			teacher = st;
			// update the current list
			for (TeacherVO stt : teachers) {
				if (stt.getId().equals(st.getId())) {
					((TeacherVO) stt).setStatus((short) 1);
					((TeacherVO) stt).setSchoolName(getCurrentUser().getSchool().getName());
					break;
				}
			}
			setSuccessMessage(getResourceBundle().getString(
					"SAVED_SUCCESSFULLY"));
		} catch (Exception e) {
			e.printStackTrace();
			success = false;
			setErrorMessage(e.getMessage());
		}
		return "success";
	}

	public String freeupTeacher() {
		clearMessages();
		try {
			success = true;
			Teacher st = (Teacher) baseService
					.getById(Teacher.class, teacherId);
			if (st.getSchool().equals(getCurrentUser().getSchool())) {
				st.setStatus((short) 0);
				baseService.update(st, getCurrentUser());
			} else {
				success = false;
				setErrorMessage(CANNOT_CHANGE_TEACHER_STATUS
						+ (st.getSchool().getPhone() != null
								&& !st.getSchool().getPhone().equals("") ? ". \nVeuillez contacter son etablissement actuel au numero "
								+ st.getSchool().getPhone()
								+ " pour obtenir sa liberation"
								: ""));
				return "error";
			}
			// update the current list
			for (TeacherVO stt : teachers) {
				if (stt.getId().equals(st.getId())) {
					((TeacherVO) stt).setStatus((short) 0);
					break;
				}
			}
			setSuccessMessage(getResourceBundle().getString(
					"SAVED_SUCCESSFULLY"));
		} catch (Exception e) {
			e.printStackTrace();
			setErrorMessage(e.getMessage());
			success = false;
		}
		return "success";
	}

	public boolean isUserHasWriteAccess() {
		return isUserHasWriteAccess(MenuIdEnum.TEACHER.getValue());
	}

	@PostConstruct
	public String loadLoggedTeacher() {

		try {
			if(getCurrentUser()!=null){
			Teacher teach = baseService.getTeacher(getCurrentUser());
			if (teach != null) {
				teacher = teach;
				teachers = new ArrayList<TeacherVO>();
				teachers.add(baseService.getTeacherVOFromTeacher(teacher));
				rowCount = 1L;
				teacherSelected = true;
				fullName = teacher.getLastName().toUpperCase() + " "
						+ teacher.getFirstName();
				setSessionAttribute("currentTeacherId", teacher.getId());
			}
			}
		} catch (Exception e) {
			e.printStackTrace();
			return "error";
		}
		return "success";
	}

	public boolean isSelectAllTeachers() {
		return selectAllTeachers;
	}

	public void setSelectAllTeachers(boolean selectAllTeachers) {
		this.selectAllTeachers = selectAllTeachers;
	}
}
