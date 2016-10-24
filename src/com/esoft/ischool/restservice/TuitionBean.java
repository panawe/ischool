package com.esoft.ischool.restservice;

import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
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
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import com.esoft.ischool.model.BaseEntity;
import com.esoft.ischool.model.Level;
import com.esoft.ischool.model.SchoolYear;
import com.esoft.ischool.model.Tuition;
import com.esoft.ischool.model.TuitionType;
import com.esoft.ischool.service.BaseService;
import com.esoft.ischool.util.MenuIdEnum;

@Component("tuitionBean")
@Scope("session")
public class TuitionBean extends BaseBean {

	@Autowired
	@Qualifier("baseService")
	private BaseService baseService;
	private Long rowCount;
	private Long rowCountY;
	private List<BaseEntity> tuitions;
	private List<BaseEntity> currentYearTuitions;
	private String levelName;
	private String schoolYearName;
	private String tuitionTypeName;

	private String selectedTab = "tuitionList";

	public String getTuitionTypeName() {
		return tuitionTypeName;
	}

	public Long getRowCountY() {
		return rowCountY;
	}

	public void setRowCountY(Long rowCountY) {
		this.rowCountY = rowCountY;
	}

	public List<BaseEntity> getCurrentYearTuitions() {
		return currentYearTuitions;
	}

	public void setCurrentYearTuitions(List<BaseEntity> currentYearTuitions) {
		this.currentYearTuitions = currentYearTuitions;
	}

	public void setTuitionTypeName(String tuitionTypeName) {
		this.tuitionTypeName = tuitionTypeName;
	}

	@Override
	public String getSelectedTab() {
		return selectedTab;
	}

	@Override
	public void setSelectedTab(String selectedTab) {
		this.selectedTab = selectedTab;
	}

	private Tuition tuition = new Tuition();

	public String validate() {
		return "succes";
	}

	@Override
	public String clear() {
		tuition = new Tuition();
		setSchoolYearName("");
		setLevelName("");
		setTuitionTypeName("");
		return "Success";
	}

	public String delete() {
		clearMessages();
		try {
			baseService.delete(getIdParameter(), Tuition.class);
			getAll();
			clear();
			setSuccessMessage(getResourceBundle().getString("DELETE_SUCCESSFULLY"));
		} catch (Exception ex) {
			ex.printStackTrace();
			setErrorMessage(getResourceBundle().getString("DELETE_UNSUCCESSFULL"));
		}
		return "Success";
	}

	public String insert() {
		clearMessages();
		Long id = tuition.getId();
		try {
			TuitionType t = (TuitionType) baseService.findByName(
					TuitionType.class, tuitionTypeName, getCurrentUser()
							.getSchool());
			tuition.setTuitionType(t);

			SchoolYear schoolYear = (SchoolYear) baseService.findByColumn(
					SchoolYear.class, "year", schoolYearName,
					baseService.getDefaultSchool());
			tuition.setSchoolYear(schoolYear);
			if (id == null || id == 0)
				baseService.save(tuition, getCurrentUser());
			else
				baseService.update(tuition, getCurrentUser());
			setSuccessMessage(getResourceBundle().getString("SAVED_SUCCESSFULLY"));
		} catch (Exception ex) {
			tuition.setId(id);
			setErrorMessage(ex, "Ce tuition existe deja");
		}

		selectedTab = "tuitionList";
		clear();
		getAll();
		return "Success";
	}

	public String edit() {
		clearMessages();
		tuition = (Tuition) baseService
				.getById(Tuition.class, getIdParameter());
		setSchoolYearName(tuition.getSchoolYear().getYear().toString());
		setTuitionTypeName(tuition.getTuitionType().getName());
		selectedTab = "tuitionDetail";
		return "Success";
	}

	@PostConstruct
	private void getAll() {
		
		try {
			SchoolYear sy = baseService.getSchoolYear(new Date(),baseService.getDefaultSchool());
			schoolYearName=sy==null?schoolYearName:sy.getYear();
			if(getCurrentUser()!=null)
			tuitions = baseService.loadAll(Tuition.class, getCurrentUser()
					.getSchool());
			setRowCount(new Long(tuitions==null?0:tuitions.size()));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@PostConstruct
	public void fetchCurrentYearTuitions() {
		try {
			currentYearTuitions = baseService.fetchCurrentYearTuitions(baseService.getDefaultSchool());
			setRowCountY(new Long(currentYearTuitions==null?0:currentYearTuitions.size()));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public boolean isUserHasWriteAccess() {
		return isUserHasWriteAccess(MenuIdEnum.TUITION.getValue());
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

	public List<BaseEntity> getTuitions() {
		return tuitions;
	}

	public void setTuitions(List<BaseEntity> tuitions) {
		this.tuitions = tuitions;
	}

	public Tuition getTuition() {
		return tuition;
	}

	public void setTuition(Tuition tuition) {
		this.tuition = tuition;
	}

	public String getLevelName() {
		return levelName;
	}

	public void setLevelName(String levelName) {
		this.levelName = levelName;
	}

	public String getSchoolYearName() {
		return schoolYearName;
	}

	public void setSchoolYearName(String schoolYearName) {
		this.schoolYearName = schoolYearName;
	}
	public String print(){
		try {
			
			FacesContext context = getContext();
			Map<String, Serializable>		parameterValueMap = new HashMap<String, Serializable>();
			HttpServletResponse response = (HttpServletResponse) context
					.getExternalContext().getResponse();
	
			InputStream reportStream = context.getExternalContext()
					.getResourceAsStream("/reports/tuition.jasper");
	
			ServletOutputStream ouputStream = response.getOutputStream();
	
			response.addHeader("Content-disposition",
					"attachment;filename=frais_scolaires"+ "-"+getStringDate()+".pdf");
	
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
