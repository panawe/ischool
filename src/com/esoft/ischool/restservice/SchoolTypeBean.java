package com.esoft.ischool.restservice;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.esoft.ischool.model.BaseEntity;
import com.esoft.ischool.model.SchoolType;
import com.esoft.ischool.service.BaseService;
import com.esoft.ischool.util.MenuIdEnum;

@Component("schoolTypeBean")
@Scope("session")
public class SchoolTypeBean extends BaseBean {

	@Autowired
	@Qualifier("baseService")
	private BaseService baseService;
	private Long rowCount;
	private List<BaseEntity> schoolTypes;
	private List<String> allSchoolTypes = new ArrayList<String>();
	
	private SchoolType schoolType = new SchoolType();

	public List<SchoolType> autoComplete(Object suggest) {
		String pref = (String) suggest;
		List<SchoolType> result = new ArrayList<SchoolType>();
		allSchoolTypes.clear();
		
		for (BaseEntity entity : schoolTypes) {
			SchoolType c = (SchoolType) entity;
			if ((c.getName() != null && c.getName().toLowerCase().indexOf(
					pref.toLowerCase()) == 0)
					|| "".equals(pref))
				result.add(c);
			
			allSchoolTypes.add(c.getName());
		}

		return result;
	}
	
	public String validate() {
		return "succes";
	}
	
	@Override
	public String clear() {
		schoolType = new SchoolType();
		getAll();
		return "Success";
	}

	public String delete() {
		clearMessages();
		try {
			baseService.delete(getIdParameter(), SchoolType.class);
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
		Long id = schoolType.getId();
		try {
			if (id == null || id == 0)
				baseService.save(schoolType,getCurrentUser());
			else
				baseService.update(schoolType,getCurrentUser());
			setSuccessMessage(getResourceBundle().getString("SAVED_SUCCESSFULLY"));
		} catch (Exception ex) {
			schoolType.setId(id);
			setErrorMessage(ex,getRessourceProperty("insertSuccess"));
		}

		
		clear();
		getAll();
		return "Success";
	}


	public String edit() {
		clearMessages();
		schoolType = (SchoolType) baseService.getById(SchoolType.class, getIdParameter());
		return "Success";
	}
	
	@PostConstruct
	private void getAll() {
		schoolTypes = baseService.loadAll(SchoolType.class,baseService.getDefaultSchool());
		setRowCount(new Long(schoolTypes.size()));

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

	public List<BaseEntity> getSchoolTypes() {
		return schoolTypes;
	}

	public void setSchoolTypes(List<BaseEntity> schoolTypes) {
		this.schoolTypes = schoolTypes;
	}

	public SchoolType getSchoolType() {
		return schoolType;
	}

	public void setSchoolType(SchoolType schoolType) {
		this.schoolType = schoolType;
	}

	public List<String> getAllSchoolTypes() {
		autoComplete("");
		return allSchoolTypes;
	}

	public void setAllSchoolTypes(List<String> allSchoolTypes) {
		this.allSchoolTypes = allSchoolTypes;
	}
	
	public boolean isUserHasWriteAccess() {
		return isUserHasWriteAccess(MenuIdEnum.AUTRE.getValue());
	}

}
