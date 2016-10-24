package com.esoft.ischool.restservice;

import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.faces.context.FacesContext;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;

import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperRunManager;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import com.esoft.ischool.model.BaseEntity;
import com.esoft.ischool.model.Furniture;
import com.esoft.ischool.model.Level;
import com.esoft.ischool.model.SchoolYear;
import com.esoft.ischool.model.Subject;
import com.esoft.ischool.service.BaseService;
import com.esoft.ischool.util.MenuIdEnum;

@Component("furnitureBean")
@Scope("session")
public class FurnitureBean extends BaseBean {

	@Autowired
	@Qualifier("baseService")
	private BaseService baseService;
	private Long rowCount;
	private Long rowCountY;
	private List<BaseEntity> furnitures;
	private List<BaseEntity> currentYearFurnitures;
	private String subjectName;
	private String schoolYearName;
	private String levelName;

	private Furniture furniture = new Furniture();

	private String selectedTabFurniture = "furnitureList";

	public List<BaseEntity> getCurrentYearFurnitures() {
		return currentYearFurnitures;
	}

	public void setCurrentYearFurnitures(
			List<BaseEntity> currentYearFurnitures) {
		this.currentYearFurnitures = currentYearFurnitures;
	}

	public Long getRowCountY() {
		return rowCountY;
	}

	public void setRowCountY(Long rowCountY) {
		this.rowCountY = rowCountY;
	}

	@Override
	public String clear() {
		furniture = new Furniture();
		levelName = "";
		subjectName = "";
		schoolYearName = "";
		return "Success";
	}

	public String delete() {
		clearMessages();
		try {
			baseService.delete(getIdParameter(), Furniture.class);
			getAll();
			setSuccessMessage(getResourceBundle().getString(
					"DELETE_SUCCESSFULLY"));
		} catch (Exception ex) {
			ex.printStackTrace();
			setErrorMessage(getResourceBundle().getString(
					"DELETE_UNSUCCESSFULL"));
		}

		return "Success";
	}

	public String insert() {
		clearMessages();
		Long id = furniture.getId();

		Level level = (Level) baseService.findByName(Level.class, levelName,
				getCurrentUser().getSchool());
		if (level == null)
			setErrorMessage(getResourceBundle().getString("LEVEL_NOT_FOUND"));

		Subject subject = (Subject) baseService.findByName(Subject.class,
				subjectName, baseService.getDefaultSchool());
		if (subject == null)
			setErrorMessage(getErrorMessage()
					+ getResourceBundle().getString("SUBJECT_NOT_FOUND"));

		SchoolYear schoolYear = (SchoolYear) baseService.findByColumn(
				SchoolYear.class, "year", schoolYearName,
				baseService.getDefaultSchool());
		if (subject == null)
			setErrorMessage(getErrorMessage()
					+ getResourceBundle().getString("SCHOOLYEAR_NOT_FOUND"));

		furniture.setLevel(level);
		furniture.setSubject(subject);
		furniture.setSchoolYear(schoolYear);

		try {
			if (id == null || id == 0)
				baseService.save(furniture, getCurrentUser());
			else
				baseService.update(furniture, getCurrentUser());
			setSuccessMessage(getResourceBundle().getString(
					"SAVED_SUCCESSFULLY"));
			clear();
		} catch (Exception ex) {
			furniture.setId(id);
			setErrorMessage(ex, "Ce furniture existe deja");
		}

		getAll();

		return "Success";
	}

	public String edit() {
		clearMessages();
		furniture = (Furniture) baseService.getById(Furniture.class,
				getIdParameter());
		levelName = furniture.getLevel().getName();
		subjectName = furniture.getSubject().getName();
		schoolYearName = furniture.getSchoolYear().getYear();
		selectedTabFurniture = "furnitureDetail";
		return "Success";
	}

	@PostConstruct
	public void getAll() {
		String[] paramNames = null;
		Object[] paramValues = null;

		ApplicationContext ctx  = getApplicationContext();
		if (getSessionParameter("link") != null) {
			if (((String) getSessionParameter("link")).equals("student")) {
				paramNames = new String[] { "schoolId", "studentId" };
				paramValues = new Object[] {
						getCurrentUser().getSchool().getId(),
						getSessionParameter("currentStudentId") };
				furnitures = baseService.getEntitiesByQueryAndParameters(
						getCurrentUser().getSchool(),
						"getFurnituresForStudent", paramNames, paramValues);
				
				StudentBean studentBean = (StudentBean) ctx.getBean("studentBean");
				studentBean.justDoIt();
				
			} else if (((String) getSessionParameter("link")).equals("teacher")) {
				paramNames = new String[] { "schoolId", "teacherId" };
				paramValues = new Object[] {
						getCurrentUser().getSchool().getId(),
						getSessionParameter("currentTeacherId") };
				furnitures = baseService.getEntitiesByQueryAndParameters(
						getCurrentUser().getSchool(),
						"getFurnituresForTeacher", paramNames, paramValues);
				TeacherBean teacherBean = (TeacherBean) ctx.getBean("teacherBean");
				teacherBean.justDoIt();
			} else if (((String) getSessionParameter("link")).equals("school")) {
				furnitures = baseService.loadAll(Furniture.class,
						getCurrentUser().getSchool());
				SchoolBean schoolBean = (SchoolBean) ctx.getBean("schoolBean");
				schoolBean.justDoIt();
			}
		}
		setRowCount(new Long(furnitures == null ? 0 : furnitures.size()));

	}

	@PostConstruct
	public void getThisYearFurniture() {
		currentYearFurnitures = baseService.getThisYearFurnitures(baseService
				.getDefaultSchool());
		setRowCountY(new Long(currentYearFurnitures == null ? 0
				: currentYearFurnitures.size()));
	}

	public boolean isUserHasWriteAccess() {
		if (((String) getSessionParameter("link")).equals("student")) {
			return isUserHasWriteAccess(MenuIdEnum.STUDENT.getValue());
		} else if (((String) getSessionParameter("link")).equals("teacher")) {
			return isUserHasWriteAccess(MenuIdEnum.TEACHER.getValue());
		} else if (((String) getSessionParameter("link")).equals("school")) {
			return isUserHasWriteAccess(MenuIdEnum.SCHOOL.getValue());
		}
		return false;
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

	public List<BaseEntity> getFurnitures() {
		return furnitures;
	}

	public void setFurnitures(List<BaseEntity> furnitures) {
		this.furnitures = furnitures;
	}

	public String getSubjectName() {
		return subjectName;
	}

	public void setSubjectName(String subjectName) {
		this.subjectName = subjectName;
	}

	public String getSchoolYearName() {
		return schoolYearName;
	}

	public void setSchoolYearName(String schoolYearName) {
		this.schoolYearName = schoolYearName;
	}

	public String getLevelName() {
		return levelName;
	}

	public void setLevelName(String levelName) {
		this.levelName = levelName;
	}

	public Furniture getFurniture() {
		return furniture;
	}

	public void setFurniture(Furniture furniture) {
		this.furniture = furniture;
	}

	public String getSelectedTabFurniture() {
		return selectedTabFurniture;
	}

	public void setSelectedTabFurniture(String selectedTabFurniture) {
		this.selectedTabFurniture = selectedTabFurniture;
	}

	public boolean isPageComingFromProgress() {
		return "CURRICULUM_PROGRESS"
				.equals(getSessionParameter("furniturePageProvenence"));
	}
	public String print(){
		try {
			
			FacesContext context = getContext();
			Map<String, Serializable>		parameterValueMap = new HashMap<String, Serializable>();
			HttpServletResponse response = (HttpServletResponse) context
					.getExternalContext().getResponse();
	
			InputStream reportStream = context.getExternalContext()
					.getResourceAsStream("/reports/fourniture.jasper");
	
			ServletOutputStream ouputStream = response.getOutputStream();
			response.addHeader("Content-disposition",
					"attachment;filename=fourniture"+ "-"+getStringDate()+".pdf");
	
			if (parameterValueMap.get("SUBREPORT_DIR") != null)
				parameterValueMap.put("SUBREPORT_DIR", getReportsDirPath() + java.io.File.separator);
			
			JasperRunManager.runReportToPdfStream(reportStream,
					ouputStream, parameterValueMap, getConnection());
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

}