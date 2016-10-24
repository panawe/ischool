package com.esoft.ischool.restservice;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.esoft.ischool.model.BaseEntity;
import com.esoft.ischool.model.Grade;
import com.esoft.ischool.service.BaseService;

@Component("gradeBean")
@Scope("session")
public class gradeBean extends BaseBean {

	@Autowired
	@Qualifier("baseService")
	private BaseService baseService;
	private Long rowCount;
	private List<BaseEntity> grades;
	private List<String> allGrades = new ArrayList<String>();
	
	private Grade grade = new Grade();

	public String validate() {
		return "succes";
	}
	
	@Override
	public String clear() {
		grade = new Grade();
		return "Success";
	}
	
	public List<Grade> autoComplete(Object suggest) {
		String pref = (String) suggest;
		List<Grade> result = new ArrayList<Grade>();
		allGrades.clear();
		
		for (BaseEntity entity : grades) {
			Grade l = (Grade) entity;
			if ((l.getName() != null && l.getName().toLowerCase().indexOf(
					pref.toLowerCase()) == 0)
					|| "".equals(pref))
				result.add(l);
			
			allGrades.add(l.getName());
		}

		return result;
	}

	public String delete() {
		clearMessages();
		try {
			baseService.delete(getIdParameter(), Grade.class);
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
		Long id = grade.getId();
		try {
			if (id == null || id == 0)
				baseService.save(grade,getCurrentUser());
			else
				baseService.update(grade,getCurrentUser());
			setSuccessMessage(getResourceBundle().getString("SAVED_SUCCESSFULLY"));
		} catch (Exception ex) {
			grade.setId(id);
			setErrorMessage(ex,"Ce grade existe deja");
		}

		
		clear();
		getAll();
		return "Success";
	}


	public String edit() {
		clearMessages();
		grade = (Grade) baseService.getById(Grade.class, getIdParameter());
		return "Success";
	}
	
	@PostConstruct
	private void getAll() {
		grades = baseService.loadAll(Grade.class,getCurrentUser().getSchool());
		setRowCount(new Long(grades.size()));

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

	public List<BaseEntity> getGrades() {
		return grades;
	}

	public void setGrades(List<BaseEntity> grades) {
		this.grades = grades;
	}

	public Grade getGrade() {
		return grade;
	}

	public void setGrade(Grade grade) {
		this.grade = grade;
	}

	public List<String> getAllGrades() {
		autoComplete("");
		return allGrades;
	}

	public void setAllGrades(List<String> allGrades) {
		this.allGrades = allGrades;
	}
	
	
}
