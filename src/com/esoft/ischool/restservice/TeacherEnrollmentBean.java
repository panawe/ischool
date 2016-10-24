package com.esoft.ischool.restservice;

import java.util.List;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.esoft.ischool.model.BaseEntity;
import com.esoft.ischool.model.Teacher;
import com.esoft.ischool.model.TeacherEnrollment;
import com.esoft.ischool.service.BaseService;

@Component("teacherEnrollmentBean")
@Scope("session")
public class TeacherEnrollmentBean extends BaseBean {

	@Autowired
	@Qualifier("baseService")
	private BaseService baseService;
	private Long rowCount;
	private List<BaseEntity> teacherEnrollments;
	private String schoolName;
	private String schoolYearName;
	
	private TeacherEnrollment teacherEnrollment = new TeacherEnrollment();

	public String validate() {
		return "succes";
	}

	@Override
	public String clear() {
		teacherEnrollment = new TeacherEnrollment();
		return "Success";
	}

	@PostConstruct
	public String getAll() {
		if(getSessionParameter("currentTeacherId")!=null){
			teacherEnrollments = baseService.loadAllByParentId(TeacherEnrollment.class, "teacher", "id", new Long(getSessionParameter("currentTeacherId").toString()));
			setRowCount(new Long(teacherEnrollments.size()));
		}
		return "Success";
	}

	
	public String delete() {
		clearMessages();
		try {
			baseService.delete(getIdParameter(), TeacherEnrollment.class);
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
		Long id = teacherEnrollment.getId();
		try {
			Teacher teacher = (Teacher) baseService.getById(Teacher.class, new Long(getSessionParameter("currentTeacherId").toString()));
			teacherEnrollment.setTeacher(teacher);
			//School school = (School) baseService.findByName(School.class, schoolName);
			//teacherEnrollment.setSchool(school);
			if (id == null || id == 0)
				baseService.save(teacherEnrollment,getCurrentUser());
			else
				baseService.update(teacherEnrollment,getCurrentUser());
			setSuccessMessage(getResourceBundle().getString("SAVED_SUCCESSFULLY")); 
		} catch (Exception ex) {
			teacherEnrollment.setId(id);
			setErrorMessage(ex,"Cet enrollment exist deja. ");
		}	
		clear();
		getAll();
		return "Success";
	}
	
	public String edit() {
		clearMessages();
		teacherEnrollment = (TeacherEnrollment) baseService.getById(TeacherEnrollment.class, getIdParameter());
		setSchoolName(schoolName);
		setSchoolYearName(schoolYearName);

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

	public String getAllStudentEnrollments() {
		if(getSessionParameter("currentStudentId")!=null){
			teacherEnrollments = baseService.loadAllByParentId(TeacherEnrollment.class, "teacher", "id", new Long(getSessionParameter("currentTeacherId").toString()));
			setRowCount(new Long(teacherEnrollments.size()));
		}
		return "Success";
	}
	
	

	public List<BaseEntity> getTeacherEnrollments() {
		return teacherEnrollments;
	}

	public void setTeacherEnrollments(List<BaseEntity> teacherEnrollments) {
		this.teacherEnrollments = teacherEnrollments;
	}

	public String getSchoolName() {
		return schoolName;
	}

	public void setSchoolName(String schoolName) {
		this.schoolName = schoolName;
	}

	public String getSchoolYearName() {
		return schoolYearName;
	}

	public void setSchoolYearName(String schoolYearName) {
		this.schoolYearName = schoolYearName;
	}

	public TeacherEnrollment getTeacherEnrollment() {
		return teacherEnrollment;
	}

	public void setTeacherEnrollment(TeacherEnrollment teacherEnrollment) {
		this.teacherEnrollment = teacherEnrollment;
	}
}
