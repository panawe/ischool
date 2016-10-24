package com.esoft.ischool.restservice;

import java.util.Date;
import java.util.List;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.esoft.ischool.model.BaseEntity;
import com.esoft.ischool.model.LevelClass;
import com.esoft.ischool.model.SchoolYear;
import com.esoft.ischool.model.Student;
import com.esoft.ischool.model.StudentEnrollment;
import com.esoft.ischool.service.BaseService;
import com.mysql.jdbc.StringUtils;

@Component("studentEnrollmentBean")
@Scope("session")
public class StudentEnrollmentBean extends BaseBean {

	@Autowired
	@Qualifier("baseService")
	private BaseService baseService;
	private Long rowCount;
	private List<BaseEntity> studentEnrollments;
	private String levelClassName;	
	private StudentEnrollment studentEnrollment = new StudentEnrollment();
	private String year;
	private String selectedTab;
	
	@Override
	public String getSelectedTab() {
		return selectedTab;
	}

	@Override
	public void setSelectedTab(String selectedTab) {
		this.selectedTab = selectedTab;
	}

	public String validate() {
		return "succes";
	}

	public String getYear() {
		return year;
	}

	public void setYear(String year) {
		this.year = year;
	}

	@Override
	public String clear() {
		studentEnrollment = new StudentEnrollment();
		year = null;
		levelClassName = null;
		return "Success";
	}

	@PostConstruct
	public String getAll() {
		if(getSessionParameter("currentStudentId") != null){
			studentEnrollments = baseService.loadAllByParentId(StudentEnrollment.class, "student", "id", new Long(getSessionParameter("currentStudentId").toString()));
			setRowCount(new Long(studentEnrollments.size()));
		}
		SchoolYear sy = baseService.getSchoolYear(new Date(), baseService.getDefaultSchool());
		year = sy == null ? year : sy.getYear();
		return "Success";
	}

	
	public String delete() {
		clearMessages();
		try {
			
			Student student = (Student) baseService.getById(Student.class, new Long(getSessionParameter("currentStudentId").toString()));
			if(student!=null && student.getCurrentEnrollment()!=null){
				if(student.getCurrentEnrollment().getId().equals(getIdParameter())){
					student.setCurrentEnrollment(null);
					baseService.update(student, getCurrentUser());
				}
			}
			
			StudentEnrollment senrol = (StudentEnrollment) baseService.getById(StudentEnrollment.class, getIdParameter());
			LevelClass levelClass = senrol.getLevelClass();

			if (levelClass != null) {
				Integer actuallyClassNumberOfStudents = baseService.getStudentsCountByLevelClassAndYear(senrol.getSchoolYear().getId(), levelClass.getId(), baseService.getDefaultSchool());
				levelClass.setNbrStudents(actuallyClassNumberOfStudents - 1);
				baseService.update(levelClass, getCurrentUser());
			}
			
			baseService.delete(getIdParameter(), StudentEnrollment.class);
			getAll();
			clear();
			setSuccessMessage(getResourceBundle().getString("DELETE_SUCCESSFULLY"));
		} catch (Exception ex) {
			ex.printStackTrace();
			setErrorMessage(getResourceBundle().getString("DELETE_UNSUCCESSFULL"));
		}

		return "Success";
	}
	
	public String insert() {
		clearMessages();
		Long id = studentEnrollment.getId();
		try {
			
			LevelClass levelClass = (LevelClass) baseService.findByName(LevelClass.class, levelClassName,getCurrentUser().getSchool());
			studentEnrollment.setLevelClass(levelClass);
			
			Student student = (Student) baseService.getById(Student.class, new Long(getSessionParameter("currentStudentId").toString()));
			studentEnrollment.setStudent(student);
			
			SchoolYear schoolYear = (SchoolYear) baseService.findByColumn(SchoolYear.class, "year", year,baseService.getDefaultSchool());		
			studentEnrollment.setSchoolYear(schoolYear);
			
			if(levelClass == null  || StringUtils.isNullOrEmpty(levelClass.getName()))
				setErrorMessage(getResourceBundle().getString("LEVEL_CLASS_NOT_FOUND"));
			
			if(schoolYear == null)
				setErrorMessage(getErrorMessage() + ". " + getResourceBundle().getString("SCHOOL_YEAR_NOT_FOUND"));
			
			if (studentEnrollment.getEnrollmentDate() == null || "".equals(studentEnrollment.getEnrollmentDate().toString()))
				setErrorMessage(getErrorMessage() + ". " + getResourceBundle().getString("STUDENT_ENROLLMENT_DATE_NOT_FOUND"));
			
			
			
			if (!StringUtils.isNullOrEmpty(getErrorMessage()))
				return "ERROR";
			
			if (id == null || id == 0){
				
				Integer actuallyClassNumberOfStudents = baseService.getStudentsCountByLevelClassAndYear(schoolYear.getId(), levelClass.getId(), baseService.getDefaultSchool());
				
				if(levelClass.getCapacity() < actuallyClassNumberOfStudents + 1){
					setErrorMessage(getResourceBundle().getString("CAPACITY_EXCEEDED") + " : " + levelClass.getCapacity()+" < " + actuallyClassNumberOfStudents + 1);
					return "Error";
				}
				
				baseService.save(studentEnrollment,getCurrentUser());
			
				levelClass.setNbrStudents(actuallyClassNumberOfStudents + 1);
				baseService.update(levelClass, getCurrentUser());
			
			}else
				baseService.update(studentEnrollment,getCurrentUser());
			
			student.setCurrentEnrollment(studentEnrollment);
			baseService.update(student,getCurrentUser());
			
			setSuccessMessage(getResourceBundle().getString("SAVED_SUCCESSFULLY")); 
			clear();
			getAll();
		} catch (Exception ex) {
			ex.printStackTrace();
			studentEnrollment.setId(id);
			setErrorMessage(ex,"Cet enrollment exist deja. ");
		}	

		return "Success";
	}
	
	public String edit() {
		clearMessages();
		studentEnrollment = (StudentEnrollment) baseService.getById(StudentEnrollment.class, getIdParameter());
		year=studentEnrollment.getSchoolYear().getYear();
		setLevelClassName(studentEnrollment.getLevelClass().getName());
		selectedTab="studentEnrollmentDetails";

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
			studentEnrollments = baseService.loadAllByParentId(StudentEnrollment.class, "student", "id", new Long(getSessionParameter("currentStudentId").toString()));
			setRowCount(new Long(studentEnrollments.size()));
		}
		return "Success";
	}

	public List<BaseEntity> getStudentEnrollments() {
		return studentEnrollments;
	}

	public void setStudentEnrollments(List<BaseEntity> studentEnrollments) {
		this.studentEnrollments = studentEnrollments;
	}

	public String getLevelClassName() {
		return levelClassName;
	}

	public void setLevelClassName(String levelClassName) {
		this.levelClassName = levelClassName;
	}

	public StudentEnrollment getStudentEnrollment() {
		return studentEnrollment;
	}

	public void setStudentEnrollment(StudentEnrollment studentEnrollment) {
		this.studentEnrollment = studentEnrollment;
	}
}
