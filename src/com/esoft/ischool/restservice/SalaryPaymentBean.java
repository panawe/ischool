package com.esoft.ischool.restservice;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.esoft.ischool.model.BaseEntity;
import com.esoft.ischool.model.SalaryPayment;
import com.esoft.ischool.model.Teacher;
import com.esoft.ischool.service.BaseService;

@Component("salaryPaymentBean")
@Scope("session")
public class SalaryPaymentBean extends BaseBean {

	@Autowired
	@Qualifier("baseService")
	private BaseService baseService;
	private Long rowCount;
	private List<BaseEntity> salaryPayments;
	private SalaryPayment salaryPayment = new SalaryPayment();
	
	public String validate() {
		return "succes";
	}
	
	@Override
	public String clear() {
		salaryPayment = new SalaryPayment();
		return "Success";
	}

	public String delete() {
		clearMessages();
		try {
			baseService.delete(getIdParameter(), SalaryPayment.class);
			getTeacherSalaryPayments();
			clear();
			setSuccessMessage(getResourceBundle().getString("DELETE_SUCCESSFULLY"));
		} catch (Exception ex) {
			setErrorMessage(getResourceBundle().getString("DELETE_UNSUCCESSFULL"));
		}


		return "Success";
	}
	
	public String insert() {
		clearMessages();
		Long id = salaryPayment.getId();
		try {			
			Teacher teacher = (Teacher)baseService.getById(Teacher.class, new Long(getSessionParameter("currentTeacherId").toString()));
			salaryPayment.setTeacher(teacher);
			
			if (id == null || id == 0)
				baseService.save(salaryPayment,getCurrentUser());
			else
				baseService.update(salaryPayment,getCurrentUser());
			setSuccessMessage(getResourceBundle().getString("SAVED_SUCCESSFULLY"));
			getTeacherSalaryPayments();
		} catch (Exception ex) {
			salaryPayment.setId(id);
			setErrorMessage(ex,"Ce payment existe deja");
		}

		return "Success";
	}


	public String edit() {
		clearMessages();
		salaryPayment = (SalaryPayment) baseService.getById(SalaryPayment.class, getIdParameter());
		setSelectedTab("teacherSalaryPaymentDetails");
		return "Success";
	}
	
	private void getAll() {
		salaryPayments = baseService.loadAll(SalaryPayment.class,getCurrentUser().getSchool());
		setRowCount(new Long(salaryPayments.size()));

	}
	
	public String getTeacherSalaryPayments() {
		if(getSessionParameter("currentTeacherId")!=null){
			salaryPayments = baseService.loadByParentsIds(SalaryPayment.class, "teacher", new Long(getSessionParameter("currentTeacherId").toString())
				,"school" ,getCurrentUser().getSchool().getId());
			setRowCount(new Long(salaryPayments.size()));
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

	public List<BaseEntity> getSalaryPayments() {
		return salaryPayments;
	}

	public void setSalaryPayments(List<BaseEntity> salaryPayments) {
		this.salaryPayments = salaryPayments;
	}

	public SalaryPayment getSalaryPayment() {
		return salaryPayment;
	}

	public void setSalaryPayment(SalaryPayment salaryPayment) {
		this.salaryPayment = salaryPayment;
	}
}
