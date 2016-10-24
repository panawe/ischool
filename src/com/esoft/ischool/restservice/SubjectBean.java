package com.esoft.ischool.restservice;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.esoft.ischool.model.BaseEntity;
import com.esoft.ischool.model.Subject;
import com.esoft.ischool.service.BaseService;

@Component("subjectBean")
@Scope("session")
public class SubjectBean extends BaseBean {

	@Autowired
	@Qualifier("baseService")
	private BaseService baseService;
	private Long rowCount;
	private List<BaseEntity> subjects;
	private List<String> allSubjects = new ArrayList<String>();
	
	private Subject subject = new Subject();

	public String validate() {
		return "succes";
	}
	
	public List<String> getAllSubjects() {
		Collections.sort(allSubjects);
		return allSubjects;
	}

	public void setAllSubjects(List<String> allSubjects) {
		this.allSubjects = allSubjects;
	}

	@Override
	public String clear() {
		subject = new Subject();
		return "Success";
	}

	public String delete() {
		clearMessages();
		try {
			baseService.delete(getIdParameter(), Subject.class);
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
		Long id = subject.getId();
		try {
			if (id == null || id == 0)
				baseService.save(subject,getCurrentUser());
			else
				baseService.update(subject,getCurrentUser());
			setSuccessMessage(getResourceBundle().getString("SAVED_SUCCESSFULLY"));
		} catch (Exception ex) {
			subject.setId(id);
			setErrorMessage(ex,getRessourceProperty("insertSuccess"));
		}

		
		clear();
		getAll();
		return "Success";
	}


	public String edit() {
		clearMessages();
		subject = (Subject) baseService.getById(Subject.class, getIdParameter());
		return "Success";
	}
	
	@PostConstruct
	private void getAll() {
		subjects = baseService.loadAll(Subject.class,baseService.getDefaultSchool());
		setRowCount(new Long(subjects.size()));
		allSubjects.clear();
		for(BaseEntity entity:subjects){
			Subject subject = (Subject) entity;
			allSubjects.add(subject.getName());
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

	public List<BaseEntity> getSubjects() {
		return subjects;
	}

	public void setSubjects(List<BaseEntity> subjects) {
		this.subjects = subjects;
	}

	public Subject getSubject() {
		return subject;
	}

	public void setSubject(Subject subject) {
		this.subject = subject;
	}	
}
