package com.esoft.ischool.restservice;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.esoft.ischool.model.BaseEntity;
import com.esoft.ischool.model.PayType;
import com.esoft.ischool.model.Salary;
import com.esoft.ischool.model.Teacher;
import com.esoft.ischool.service.BaseService;

@Component("salaryBean")
@Scope("session")
public class SalaryBean extends BaseBean {

	@Autowired
	@Qualifier("baseService")
	private BaseService baseService;
	private Long rowCount;
	private List<BaseEntity> salaries;
	private String payTypeName;
	private Salary salary = new Salary();
	
	public String validate() {
		return "succes";
	}
	
	@Override
	public String clear() {
		salary = new Salary();
		setPayTypeName("");
		return "Success";
	}

	public String delete() {
		clearMessages();
		try {
			baseService.delete(getIdParameter(), PayType.class);
			getTeacherSalaries();
			clear();
			setSuccessMessage(getResourceBundle().getString("DELETE_SUCCESSFULLY"));
		} catch (Exception ex) {
			setErrorMessage(getResourceBundle().getString("DELETE_UNSUCCESSFULL"));
		}


		return "Success";
	}
	
	public String insert() {
		clearMessages();
		Long id = salary.getId();
		try {
			PayType payType = (PayType) baseService.findByName(PayType.class, payTypeName,baseService.getDefaultSchool());
			salary.setPayType(payType);
			
			if (payType == null) {
				setErrorMessage(getResourceBundle().getString("INVALID_PAYTYPE"));
				return "ERROR";
			}
			
			Teacher teacher = (Teacher)baseService.getById(Teacher.class, new Long(getSessionParameter("currentTeacherId").toString()));
			salary.setTeacher(teacher);
			if (id == null || id == 0)
				baseService.save(salary,getCurrentUser());
			else
				baseService.update(salary,getCurrentUser());
			setSuccessMessage(getResourceBundle().getString("SAVED_SUCCESSFULLY"));
			getTeacherSalaries();
		} catch (Exception ex) {
			salary.setId(id);
			setErrorMessage(ex,"Ce Salaire existe deja");
		}

		return "Success";
	}


	public String edit() {
		clearMessages();
		salary = (Salary) baseService.getById(Salary.class, getIdParameter());
		setPayTypeName(salary.getPayType().getName());
		setSelectedTab("teacherSalaryDetails");
		return "Success";
	}
	
	private void getAll() {
		salaries = baseService.loadAll(Salary.class,getCurrentUser().getSchool());
		setRowCount(new Long(salaries.size()));

	}
	
	public String getTeacherSalaries() {
		if(getSessionParameter("currentTeacherId")!=null){
			salaries = baseService.loadByParentsIds(Salary.class, "teacher", new Long(getSessionParameter("currentTeacherId").toString())
				,"school" ,getCurrentUser().getSchool().getId());
			setRowCount(new Long(salaries.size()));
		}
		return "Success";
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

	public List<BaseEntity> getSalaries() {
		return salaries;
	}

	public void setSalaries(List<BaseEntity> salaries) {
		this.salaries = salaries;
	}

	public String getPayTypeName() {
		return payTypeName;
	}

	public void setPayTypeName(String payTypeName) {
		this.payTypeName = payTypeName;
	}

	public Salary getSalary() {
		return salary;
	}

	public void setSalary(Salary salary) {
		this.salary = salary;
	}
}
