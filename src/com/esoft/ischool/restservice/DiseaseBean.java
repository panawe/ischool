package com.esoft.ischool.restservice;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.esoft.ischool.model.BaseEntity;
import com.esoft.ischool.model.Disease;
import com.esoft.ischool.service.BaseService;
import com.esoft.ischool.util.MenuIdEnum;

@Component("diseaseBean")
@Scope("session")
public class DiseaseBean extends BaseBean {

	@Autowired
	@Qualifier("baseService")
	private BaseService baseService;
	private Long rowCount;
	private List<BaseEntity> diseases = new ArrayList<BaseEntity>();
	private List<String> allDiseases = new ArrayList<String>();
	
	private Disease disease = new Disease();

	public List<Disease> autoComplete(Object suggest) {
		String pref = (String) suggest;
		List<Disease> result = new ArrayList<Disease>();
		allDiseases.clear();
		
		for (BaseEntity entity : diseases) {
			Disease d = (Disease) entity;
			if ((d.getName() != null && d.getName().toLowerCase().indexOf(
					pref.toLowerCase()) == 0)
					|| "".equals(pref))
				result.add(d);
			
			allDiseases.add(d.getName());
		}

		return result;
	}

	public String validate() {
		return "succes";
	}
	
	@Override
	public String clear() {
		disease = new Disease();
		return "Success";
	}

	public String delete() {
		clearMessages();
		try {
			baseService.delete(getIdParameter(), Disease.class);
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
		Long id = disease.getId();
		try {
			if (id == null || id == 0)
				baseService.save(disease, getCurrentUser());
			else
				baseService.update(disease, getCurrentUser());
			setSuccessMessage(getResourceBundle().getString("SAVED_SUCCESSFULLY"));
		} catch (Exception ex) {
			disease.setId(id);
			setErrorMessage(ex,"Cette maladie existe deja");
		}

		
		clear();
		getAll();
		return "Success";
	}


	public String edit() {
		clearMessages();
		disease = (Disease) baseService.getById(Disease.class, getIdParameter());
		return "Success";
	}
	
	@PostConstruct
	private void getAll() {
		diseases = baseService.loadAll(Disease.class, baseService.getDefaultSchool());
		setRowCount(new Long(diseases.size()));

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

	public List<BaseEntity> getDiseases() {
		return diseases;
	}

	public void setDiseases(List<BaseEntity> diseases) {
		this.diseases = diseases;
	}

	public List<String> getAllDiseases() {
		autoComplete("");
		return allDiseases;
	}

	public void setAllDiseases(List<String> allDiseases) {
		this.allDiseases = allDiseases;
	}

	public Disease getDisease() {
		return disease;
	}

	public void setDisease(Disease disease) {
		this.disease = disease;
	}

	public boolean isUserHasWriteAccess() {
		return isUserHasWriteAccess(MenuIdEnum.INFIRMERIE_CONFIG.getValue());
	}
}
