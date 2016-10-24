package com.esoft.ischool.restservice;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.esoft.ischool.model.BaseEntity;
import com.esoft.ischool.model.SchoolLevel;
import com.esoft.ischool.service.BaseService;

@Component("schoolLevelBean")
@Scope("session")
public class SchoolLevelBean extends BaseBean {

	@Autowired
	@Qualifier("baseService")
	private BaseService baseService;
	private Long rowCount;
	private List<BaseEntity> schoolLevels;
	private List<String> allSchoolLevels = new ArrayList<String>();
	
	private SchoolLevel schoolLevel = new SchoolLevel();

	public List<SchoolLevel> autoComplete(Object suggest) {
		String pref = (String) suggest;
		List<SchoolLevel> result = new ArrayList<SchoolLevel>();
		allSchoolLevels.clear();
		
		for (BaseEntity entity : schoolLevels) {
			SchoolLevel c = (SchoolLevel) entity;
			if ((c.getName() != null && c.getName().toLowerCase().indexOf(
					pref.toLowerCase()) == 0)
					|| "".equals(pref))
				result.add(c);
			
			allSchoolLevels.add(c.getName());
		}

		return result;
	}

	public String validate() {
		return "succes";
	}
	
	@Override
	public String clear() {
		schoolLevel = new SchoolLevel();
		getAll();
		return "Success";
	}

	public String delete() {
		clearMessages();
		try {
			baseService.delete(getIdParameter(), SchoolLevel.class);
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
		Long id = schoolLevel.getId();
		try {
			if (id == null || id == 0)
				baseService.save(schoolLevel,getCurrentUser());
			else
				baseService.update(schoolLevel,getCurrentUser());
			setSuccessMessage(getResourceBundle().getString("SAVED_SUCCESSFULLY"));
		} catch (Exception ex) {
			schoolLevel.setId(id);
			setErrorMessage(ex,getRessourceProperty("insertSuccess"));
		}

		
		clear();
		getAll();
		return "Success";
	}


	public String edit() {
		clearMessages();
		schoolLevel = (SchoolLevel) baseService.getById(SchoolLevel.class, getIdParameter());
		return "Success";
	}
	
	@PostConstruct
	private void getAll() {
		schoolLevels = baseService.loadAll(SchoolLevel.class,baseService.getDefaultSchool());
		setRowCount(new Long(schoolLevels.size()));

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

	public List<BaseEntity> getSchoolLevels() {
		return schoolLevels;
	}

	public void setSchoolLevels(List<BaseEntity> schoolLevels) {
		this.schoolLevels = schoolLevels;
	}

	public SchoolLevel getSchoolLevel() {
		return schoolLevel;
	}

	public void setSchoolLevel(SchoolLevel schoolLevel) {
		this.schoolLevel = schoolLevel;
	}

	public List<String> getAllSchoolLevels() {
		autoComplete("");
		return allSchoolLevels;
	}

	public void setAllSchoolLevels(List<String> allSchoolLevels) {
		this.allSchoolLevels = allSchoolLevels;
	}
}
