package com.esoft.ischool.restservice;

import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.esoft.ischool.model.BaseEntity;
import com.esoft.ischool.model.Course;
import com.esoft.ischool.model.Exemption;
import com.esoft.ischool.model.Student;
import com.esoft.ischool.model.StudentEnrollment;
import com.esoft.ischool.model.Teacher;
import com.esoft.ischool.service.ExamService;

@Component("exemptionBean")
@Scope("session")
public class ExemptionBean extends BaseBean {

	@Autowired
	@Qualifier("examService")
	private ExamService examService;
	private Long rowCount;
	private List<BaseEntity> exemptions;
	private String levelClassName;
	private String subjectName;
	private Teacher teacher;
	private Double random;
	private List<String> subjectsForLevel = new ArrayList<String>();
	
	private Exemption exemption = new Exemption();

	public String validate() {
		return "succes";
	}

	public List<String> getSubjectsForLevel() {
		return subjectsForLevel;
	}

	public void setSubjectsForLevel(List<String> subjectsForLevel) {
		this.subjectsForLevel = subjectsForLevel;
	}

	@Override
	public String clear() {
		exemption = new Exemption();
		return "Success";
	}

	@PostConstruct
	public String getAll() {
		if(getSessionParameter("currentStudentId")!=null){
			exemptions = examService.loadAllByParentId(Exemption.class, "student", "id", new Long(getSessionParameter("currentStudentId").toString()));
			setRowCount(new Long(exemptions.size()));
		}
		return "Success";
	}

	
	public String delete() {
		clearMessages();
		try {
			examService.delete(getIdParameter(), Exemption.class);
			getAll();
			clear();
			setSuccessMessage(getResourceBundle().getString("DELETE_SUCCESSFULLY"));
		} catch (Exception ex) {
			setErrorMessage(getResourceBundle().getString("DELETE_UNSUCCESSFULL"));
		}

		return "Success";
	}
	
	public String fetchTeacher() {
		teacher = null;
		random = Math.random();
		Student student = (Student) examService.getById(Student.class, new Long(getSessionParameter("currentStudentId").toString()));		
		StudentEnrollment studentEnrollment = student.getCurrentEnrollment();
		if(studentEnrollment!=null)
			levelClassName = studentEnrollment.getLevelClass().getName();
		
		if (levelClassName != null && subjectName != null) {
			List<Teacher> teachers = examService.getTeacher(levelClassName,
					subjectName);
			for (Teacher t : teachers) {
				teacher = t;
			}
		}
		return "Success";
	}

	@Override
	public void paint(OutputStream stream, Object object) throws IOException {
		if (teacher != null) {
			stream.write(teacher.getImage());
		} else {
			stream.write(new byte[] {});
		}
	}
	
	public String insert() {
		clearMessages();
		Long id = exemption.getId();
		try {
			Student student = (Student) examService.getById(Student.class, new Long(getSessionParameter("currentStudentId").toString()));
			exemption.setStudent(student);
			
			StudentEnrollment studentEnrollment = student.getCurrentEnrollment();
			Course course = examService.getCourse(studentEnrollment.getLevelClass().getName(), subjectName);
			
			if (course == null) {
				setErrorMessage("Le cours choisit n'est pas dispense dans la classe choisie");
				return "Error";
			}
			
			exemption.setCourse(course);
			
			exemption.setExemptionReasonId(1);
			if (id == null || id == 0)
				examService.save(exemption,getCurrentUser());
			else
				examService.update(exemption,getCurrentUser());
			setSuccessMessage(getResourceBundle().getString("SAVED_SUCCESSFULLY")); 
		} catch (Exception ex) {
			exemption.setId(id);
			setErrorMessage(ex,"Cette exemption exist deja. ");
		}	
		clear();
		getAll();
		return "Success";
	}
	
	public String edit() {
		clearMessages();
		exemption = (Exemption) examService.getById(Exemption.class, getIdParameter());
		//setCourseName(courseName);
		return "Success";
	}

	
	public ExamService getExamService() {
		return examService;
	}

	public void setExamService(ExamService examService) {
		this.examService = examService;
	}

	public Long getRowCount() {
		return rowCount;
	}

	public void setRowCount(Long rowCount) {
		this.rowCount = rowCount;
	}

	public String getAllExemptions() {
		if(getSessionParameter("currentStudentId")!=null){
			exemptions = examService.loadAllByParentId(Exemption.class, "student", "id", new Long(getSessionParameter("currentStudentId").toString()));
			Student student = (Student) examService.getById(Student.class,  new Long(getSessionParameter("currentStudentId").toString()));
			StudentEnrollment studentEnrollment = student.getCurrentEnrollment();
			if(studentEnrollment!=null){
				subjectsForLevel= examService.getSubjectsForLevel(studentEnrollment.getLevelClass());		
			}
			setRowCount(new Long(exemptions.size()));
		}
		return "Success";
	}

	public List<BaseEntity> getExemptions() {
		return exemptions;
	}

	public void setExemptions(List<BaseEntity> exemptions) {
		this.exemptions = exemptions;
	}
	
	public String getLevelClassName() {
		return levelClassName;
	}

	public void setLevelClassName(String levelClassName) {
		this.levelClassName = levelClassName;
	}

	public Exemption getExemption() {
		return exemption;
	}

	public void setExemption(Exemption exemption) {
		this.exemption = exemption;
	}

	public String getSubjectName() {
		return subjectName;
	}

	public void setSubjectName(String subjectName) {
		this.subjectName = subjectName;
	}

	public Teacher getTeacher() {
		return teacher;
	}

	public void setTeacher(Teacher teacher) {
		this.teacher = teacher;
	}

	@Override
	public Double getRandom() {
		return random;
	}

	@Override
	public void setRandom(Double random) {
		this.random = random;
	}
	
	
}