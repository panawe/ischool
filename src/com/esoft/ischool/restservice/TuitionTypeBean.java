package com.esoft.ischool.restservice;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.esoft.ischool.model.BaseEntity;
import com.esoft.ischool.model.TuitionType;
import com.esoft.ischool.service.BaseService;

@Component("tuitionTypeBean")
@Scope("session")
public class TuitionTypeBean extends BaseBean {

	@Autowired
	@Qualifier("baseService")
	private BaseService baseService;
	private Long rowCount;
	private List<BaseEntity> tuitionTypes;
	private List<String> allTuitionTypes = new ArrayList<String>();
	
	private TuitionType tuitionType = new TuitionType();

	public String validate() {
		return "succes";
	}
	
	@Override
	public String clear() {
		tuitionType = new TuitionType();
		return "Success";
	}
	
	public List<TuitionType> autoComplete(Object suggest) {
		String pref = (String) suggest;
		List<TuitionType> result = new ArrayList<TuitionType>();
		allTuitionTypes.clear();
		
		for (BaseEntity entity : tuitionTypes) {
			TuitionType l = (TuitionType) entity;
			if ((l.getName() != null && l.getName().toLowerCase().indexOf(
					pref.toLowerCase()) == 0)
					|| "".equals(pref))
				result.add(l);
			
			allTuitionTypes.add(l.getName());
		}

		return result;
	}

	public String delete() {
		clearMessages();
		try {
			baseService.delete(getIdParameter(), TuitionType.class);
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
		Long id = tuitionType.getId();
		try {
			if (id == null || id == 0)
				baseService.save(tuitionType,getCurrentUser());
			else
				baseService.update(tuitionType,getCurrentUser());
			setSuccessMessage(getResourceBundle().getString("SAVED_SUCCESSFULLY"));
		} catch (Exception ex) {
			tuitionType.setId(id);
			setErrorMessage(ex,"Ce tuitionType existe deja");
		}

		
		clear();
		getAll();
		return "Success";
	}


	public String edit() {
		clearMessages();
		tuitionType = (TuitionType) baseService.getById(TuitionType.class, getIdParameter());
		return "Success";
	}
	
	@PostConstruct
	private void getAll() {
		tuitionTypes = baseService.loadAll(TuitionType.class,getCurrentUser().getSchool());
		setRowCount(new Long(tuitionTypes.size()));

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

	public List<BaseEntity> getTuitionTypes() {
		return tuitionTypes;
	}

	public void setTuitionTypes(List<BaseEntity> tuitionTypes) {
		this.tuitionTypes = tuitionTypes;
	}

	public TuitionType getTuitionType() {
		return tuitionType;
	}

	public void setTuitionTypes(TuitionType tuitionType) {
		this.tuitionType = tuitionType;
	}

	public List<String> getAllTuitionTypes() {
		autoComplete("");
		return allTuitionTypes;
	}

	public void setAllTuitionTypes(List<String> allTuitionTypes) {
		this.allTuitionTypes = allTuitionTypes;
	}
	
	
}
