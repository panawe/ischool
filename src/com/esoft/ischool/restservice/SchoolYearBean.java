package com.esoft.ischool.restservice;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import com.esoft.ischool.model.BaseEntity;
import com.esoft.ischool.model.SchoolYear;
import com.esoft.ischool.service.BaseService;

@Component("schoolYearBean")
@Scope("session")
public class SchoolYearBean extends BaseBean {

	@Autowired
	@Qualifier("baseService")
	private BaseService baseService;
	private Long rowCount;
	private List<BaseEntity> schoolYears;
	private List<String> allSchoolYears = new ArrayList<String>();
	
	
	public List<String> getAllSchoolYears() {
		return allSchoolYears;
	}

	public void setAllSchoolYears(List<String> allSchoolYears) {
		this.allSchoolYears = allSchoolYears;
	}

	private SchoolYear schoolYear = new SchoolYear();

	public List<SchoolYear> autoComplete(Object suggest) {
		String pref = (String) suggest;
		List<SchoolYear> result = new ArrayList<SchoolYear>();
		allSchoolYears.clear();
		
		for (BaseEntity entity : schoolYears) {
			SchoolYear l = (SchoolYear) entity;
			if ((l.getYear() != null && l.getYear().toString().toLowerCase().indexOf(
					pref.toLowerCase()) == 0)
					|| "".equals(pref))
				result.add(l);
			
			allSchoolYears.add(l.getYear());
		}

		return result;
	}

	public String validate() {
		return "succes";
	}
	
	@Override
	public String clear() {
		schoolYear = new SchoolYear();
		return "Success";
	}

	public String delete() {
		clearMessages();
		try {
			baseService.delete(getIdParameter(), SchoolYear.class);
			getAll();
			clear();
			setSuccessMessage(getResourceBundle().getString("DELETE_SUCCESSFULLY"));
		} catch (Exception ex) {
			setErrorMessage(getResourceBundle().getString("DELETE_UNSUCCESSFULL"));
		}


		return "Success";
	}
	
	public String insert() {
		clearMessages();
		Long id = schoolYear.getId();
		try {
			if (id == null || id == 0)
				baseService.save(schoolYear,getCurrentUser());
			else
				baseService.update(schoolYear,getCurrentUser());
			setSuccessMessage(getResourceBundle().getString("SAVED_SUCCESSFULLY"));
		} catch (Exception ex) {
			schoolYear.setId(id);
			setErrorMessage(ex,"Ce pays existe deja");
		}

		
		clear();
		getAll();
		return "Success";
	}


	public String edit() {
		clearMessages();
		schoolYear = (SchoolYear) baseService.getById(SchoolYear.class, getIdParameter());
		return "Success";
	}
	
	@PostConstruct
	private void getAll() {
		schoolYears = baseService.loadAll(SchoolYear.class,baseService.getDefaultSchool());
		setRowCount(new Long(schoolYears.size()));
		for(BaseEntity entity:schoolYears){
			SchoolYear schoolYear = (SchoolYear) entity;
			allSchoolYears.add(schoolYear.getYear());
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

	public List<BaseEntity> getSchoolYears() {
		return schoolYears;
	}

	public void setSchoolYears(List<BaseEntity> schoolYears) {
		this.schoolYears = schoolYears;
	}

	public SchoolYear getSchoolYear() {
		return schoolYear;
	}

	public void setSchoolYear(SchoolYear schoolYear) {
		this.schoolYear = schoolYear;
	}
	
	
}
