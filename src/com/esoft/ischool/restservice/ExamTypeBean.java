package com.esoft.ischool.restservice;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.esoft.ischool.model.BaseEntity;
import com.esoft.ischool.model.ExamType;
import com.esoft.ischool.service.BaseService;
import com.esoft.ischool.util.MenuIdEnum;

@Component("examTypeBean")
@Scope("session")
public class ExamTypeBean extends BaseBean {

	@Autowired
	@Qualifier("baseService")
	private BaseService baseService;
	private Long rowCount;
	private List<BaseEntity> examTypes;
	private List<String> allExamTypes = new ArrayList<String>();
	private ExamType examType = new ExamType();

	public List<ExamType> autoComplete(Object suggest) {
		String pref = (String) suggest;
		List<ExamType> result = new ArrayList<ExamType>();
		allExamTypes.clear();
		
		for (BaseEntity entity : examTypes) {
			ExamType l = (ExamType) entity;
			if ((l.getName() != null && l.getName().toLowerCase().indexOf(
					pref.toLowerCase()) == 0)
					|| "".equals(pref))
				result.add(l);
			
			allExamTypes.add(l.getName());
		}

		return result;
	}

	public List<String> getAllExamTypes() {
		return allExamTypes;
	}

	public void setAllExamTypes(List<String> allExamTypes) {
		this.allExamTypes = allExamTypes;
	}

	public String validate() {
		return "succes";
	}
	
	@Override
	public String clear() {
		examType = new ExamType();
		getAll();
		return "Success";
	}

	public String delete() {
		clearMessages();
		try {
			baseService.delete(getIdParameter(), ExamType.class);
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
		Long id = examType.getId();
		try {
			if (id == null || id == 0)
				baseService.save(examType,getCurrentUser());
			else
				baseService.update(examType,getCurrentUser());
			setSuccessMessage(getResourceBundle().getString("SAVED_SUCCESSFULLY"));
		} catch (Exception ex) {
			examType.setId(id);
			setErrorMessage(ex,getRessourceProperty("insertSuccess"));
		}

		
		clear();
		getAll();
		return "Success";
	}


	public String edit() {
		clearMessages();
		examType = (ExamType) baseService.getById(ExamType.class, getIdParameter());
		return "Success";
	}
	
	@PostConstruct
	private void getAll() {
		examTypes = baseService.loadAll(ExamType.class,baseService.getDefaultSchool());
		setRowCount(new Long(examTypes.size()));
		for(BaseEntity entity:examTypes){
			ExamType examType = (ExamType) entity;
			allExamTypes.add(examType.getName());
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

	public List<BaseEntity> getExamTypes() {
		return examTypes;
	}

	public void setExamTypes(List<BaseEntity> examTypes) {
		this.examTypes = examTypes;
	}

	public ExamType getExamType() {
		return examType;
	}

	public void setExamType(ExamType examType) {
		this.examType = examType;
	}
	
	public boolean isUserHasWriteAccess() {
		return isUserHasWriteAccess(MenuIdEnum.AUTRE.getValue());
	}

}
