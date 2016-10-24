package com.esoft.ischool.restservice;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import com.esoft.ischool.model.BaseEntity;
import com.esoft.ischool.model.StudentTuition;
import com.esoft.ischool.service.BaseService;

@Component("studentTuitionBean")
@Scope("session")
public class StudentTuitionBean extends BaseBean {

	@Autowired
	@Qualifier("baseService")
	private BaseService baseService;
	private Long rowCount;
	private List<BaseEntity> studentTuitions;
	private List<String> allSchoolLevels = new ArrayList<String>();
	
	private StudentTuition studentTuition = new StudentTuition();


	public String validate() {
		return "succes";
	}
	
	@Override
	public String clear() {
		studentTuition = new StudentTuition();
		getAll();
		return "Success";
	}

	public String delete() {
		clearMessages();
		try {
			baseService.delete(getIdParameter(), StudentTuition.class);
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
		Long id = studentTuition.getId();
		try {
			if (id == null || id == 0)
				baseService.save(studentTuition,getCurrentUser());
			else
				baseService.update(studentTuition,getCurrentUser());
			setSuccessMessage(getResourceBundle().getString("SAVED_SUCCESSFULLY"));
		} catch (Exception ex) {
			studentTuition.setId(id);
			setErrorMessage(ex,getRessourceProperty("insertSuccess"));
		}

		
		clear();
		getAll();
		return "Success";
	}


	public String edit() {
		clearMessages();
		studentTuition = (StudentTuition) baseService.getById(StudentTuition.class, getIdParameter());
		return "Success";
	}
	
	@PostConstruct
	private void getAll() {
		studentTuitions = baseService.loadAll(StudentTuition.class,baseService.getDefaultSchool());
		setRowCount(new Long(studentTuitions.size()));

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
		return studentTuitions;
	}

	public void setSchoolLevels(List<BaseEntity> studentTuitions) {
		this.studentTuitions = studentTuitions;
	}

	public StudentTuition getSchoolLevel() {
		return studentTuition;
	}

	public void setSchoolLevel(StudentTuition studentTuition) {
		this.studentTuition = studentTuition;
	}

	public List<String> getAllSchoolLevels() {
		return allSchoolLevels;
	}

	public void setAllSchoolLevels(List<String> allSchoolLevels) {
		this.allSchoolLevels = allSchoolLevels;
	}
}
