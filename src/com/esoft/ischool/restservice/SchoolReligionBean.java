package com.esoft.ischool.restservice;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.esoft.ischool.model.BaseEntity;
import com.esoft.ischool.model.SchoolReligion;
import com.esoft.ischool.service.BaseService;
import com.esoft.ischool.util.MenuIdEnum;

@Component("schoolReligionBean")
@Scope("session")
public class SchoolReligionBean extends BaseBean {

	@Autowired
	@Qualifier("baseService")
	private BaseService baseService;
	private Long rowCount;
	private List<BaseEntity> schoolReligions;
	private List<String> allSchoolReligions = new ArrayList<String>();
	
	private SchoolReligion schoolReligion = new SchoolReligion();

	public List<SchoolReligion> autoComplete(Object suggest) {
		String pref = (String) suggest;
		List<SchoolReligion> result = new ArrayList<SchoolReligion>();
		allSchoolReligions.clear();
		
		for (BaseEntity entity : schoolReligions) {
			SchoolReligion c = (SchoolReligion) entity;
			if ((c.getName() != null && c.getName().toLowerCase().indexOf(
					pref.toLowerCase()) == 0)
					|| "".equals(pref))
				result.add(c);
			
			allSchoolReligions.add(c.getName());
		}

		return result;
	}
	
	public String validate() {
		return "succes";
	}
	
	@Override
	public String clear() {
		schoolReligion = new SchoolReligion();
		getAll();
		return "Success";
	}

	public String delete() {
		clearMessages();
		try {
			baseService.delete(getIdParameter(), SchoolReligion.class);
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
		Long id = schoolReligion.getId();
		try {
			if (id == null || id == 0)
				baseService.save(schoolReligion,getCurrentUser());
			else
				baseService.update(schoolReligion,getCurrentUser());
			setSuccessMessage(getResourceBundle().getString("SAVED_SUCCESSFULLY"));
		} catch (Exception ex) {
			schoolReligion.setId(id);
			setErrorMessage(ex,getRessourceProperty("insertSuccess"));
		}

		
		clear();
		getAll();
		return "Success";
	}


	public String edit() {
		clearMessages();
		schoolReligion = (SchoolReligion) baseService.getById(SchoolReligion.class, getIdParameter());
		return "Success";
	}
	
	@PostConstruct
	private void getAll() {
		schoolReligions = baseService.loadAll(SchoolReligion.class,baseService.getDefaultSchool());
		setRowCount(new Long(schoolReligions.size()));

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

	public List<BaseEntity> getSchoolReligions() {
		return schoolReligions;
	}

	public void setSchoolReligions(List<BaseEntity> schoolReligions) {
		this.schoolReligions = schoolReligions;
	}

	public SchoolReligion getSchoolReligion() {
		return schoolReligion;
	}

	public void setSchoolReligion(SchoolReligion schoolReligion) {
		this.schoolReligion = schoolReligion;
	}

	public List<String> getAllSchoolReligions() {
		autoComplete("");
		return allSchoolReligions;
	}

	public void setAllSchoolReligions(List<String> allSchoolReligions) {
		this.allSchoolReligions = allSchoolReligions;
	}
	
	public boolean isUserHasWriteAccess() {
		return isUserHasWriteAccess(MenuIdEnum.AUTRE.getValue());
	}

}
