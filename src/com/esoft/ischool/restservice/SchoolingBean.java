package com.esoft.ischool.restservice;
import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.faces.context.FacesContext;
import javax.servlet.ServletContext;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;

import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperRunManager;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.esoft.ischool.model.BaseEntity;
import com.esoft.ischool.model.SchoolYear;
import com.esoft.ischool.model.Schooling;
import com.esoft.ischool.model.EventType;
import com.esoft.ischool.model.Student;
import com.esoft.ischool.model.Teacher;
import com.esoft.ischool.model.Term;
import com.esoft.ischool.service.BaseService;
import com.esoft.ischool.util.MenuIdEnum;

@Component("schoolingBean")
@Scope("session")
public class SchoolingBean extends BaseBean {

	@Autowired
	@Qualifier("baseService")
	private BaseService baseService;
	private Long rowCount;
	private List<BaseEntity> schoolings;
	private String eventTypeName;
	private String termName;
	private String selectedTeacherTab;
	
	private Schooling schooling = new Schooling();

	public String validate() {
		return "succes";
	}

	@Override
	public String clear() {
		schooling = new Schooling();
		eventTypeName="";
		termName="";
		return "Success";
	}

	public String getTermName() {
		return termName;
	}

	public void setTermName(String termName) {
		this.termName = termName;
	}

	public String getStudentSchooling() {
		if(getSessionParameter("currentStudentId")!=null){
			schoolings = baseService.loadAllByParentId(Schooling.class, "student", "id", new Long(getSessionParameter("currentStudentId").toString()));
			setRowCount(new Long(schoolings.size()));
		}
		return "Success";
	}
	
	public String getTeacherSchooling() {
		if(getSessionParameter("currentTeacherId")!=null){
			schoolings = baseService.loadByParentsIds(Schooling.class, "teacher", new Long(getSessionParameter("currentTeacherId").toString()), "school", getCurrentUser().getSchool().getId());
			setRowCount(new Long(schoolings.size()));
		}
		return "Success";
	}
	
	
	public String delete() {
		clearMessages();
		try {
			baseService.delete(getIdParameter(), Schooling.class);
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
		Long id = schooling.getId();
		try {
			EventType eventType = (EventType) baseService.findByName(EventType.class, eventTypeName,baseService.getDefaultSchool());
			Term term = (Term) baseService.findByName(Term.class, termName,baseService.getDefaultSchool());
			schooling.setTerm(term);
			
			SchoolYear sy = baseService.getSchoolYear(schooling.getEventDate(),baseService.getDefaultSchool());
			
			schooling.setSchoolYear(sy);
			if(eventType == null)
				setErrorMessage(getResourceBundle().getString("EVENT_TYPE_NOT_FOUND"));
			
			if (schooling.getEventDate() == null || "".equals(schooling.getEventDate().toString()))
				setErrorMessage(getErrorMessage() + ". " + getResourceBundle().getString("EVENT_DATE_NOT_FOUND"));
			
			if (StringUtils.isNotEmpty(getErrorMessage()))
				return "ERROR";
			
			schooling.setEventType(eventType);
			
			if ( ((String)getSessionParameter("link")).equals("student")) {
				Student student = (Student) baseService.getById(Student.class, new Long(getSessionParameter("currentStudentId").toString()));
				schooling.setStudent(student);
			} else if ( ((String)getSessionParameter("link")).equals("teacher")) {
				Teacher teacher = (Teacher) baseService.getById(Teacher.class, new Long(getSessionParameter("currentTeacherId").toString()));
				schooling.setTeacher(teacher);
			}

			if (id == null || id == 0)
				baseService.save(schooling,getCurrentUser());
			else
				baseService.update(schooling,getCurrentUser());
			setSuccessMessage(getResourceBundle().getString("SAVED_SUCCESSFULLY")); 
			getAll();
		} catch (Exception ex) {
			schooling.setId(id);
			setErrorMessage(ex,"Cet enseignant exist deja. ");
		}
		return "Success";
	}

	public String edit() {
		clearMessages();
		schooling = (Schooling) baseService.getById(Schooling.class, getIdParameter());
		setEventTypeName(schooling.getEventType().getName());
		setTermName(schooling.getTerm().getName());

		return "Success";
	}
	
	@PostConstruct
	private void getAll() {
		try{
		if ( ((String)getSessionParameter("link")).equals("student")) {
			if(getSessionParameter("currentStudentId")!=null){
				schoolings = baseService.loadAllByParentId(Schooling.class, "student", "id", new Long(getSessionParameter("currentStudentId").toString()));
				setRowCount(new Long(schoolings.size()));
			}
		} else if ( ((String)getSessionParameter("link")).equals("teacher")) {
			if(getSessionParameter("currentTeacherId")!=null){
				schoolings = baseService.loadByParentsIds(Schooling.class, "teacher", new Long(getSessionParameter("currentTeacherId").toString()), "school", getCurrentUser().getSchool().getId());
				setRowCount(new Long(schoolings.size()));
			}
		}
		}catch(Exception e){
			
			e.printStackTrace();
		}
	}
	
	public boolean isUserHasWriteAccess() {
		if ( ((String)getSessionParameter("link")).equals("student")) {
			return isUserHasWriteAccess(MenuIdEnum.STUDENT.getValue());
		} else if ( ((String)getSessionParameter("link")).equals("teacher")) {
			return isUserHasWriteAccess(MenuIdEnum.TEACHER.getValue());
		}
		return false;
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

	public List<BaseEntity> getSchoolings() {
		return schoolings;
	}

	public void setSchoolings(List<BaseEntity> schoolings) {
		this.schoolings = schoolings;
	}

	public String getEventTypeName() {
		return eventTypeName;
	}

	public void setEventTypeName(String eventTypeName) {
		this.eventTypeName = eventTypeName;
	}

	public Schooling getSchooling() {
		return schooling;
	}

	public void setSchooling(Schooling schooling) {
		this.schooling = schooling;
	}

	public String getSelectedTeacherTab() {
		return selectedTeacherTab;
	}

	public void setSelectedTeacherTab(String selectedTeacherTab) {
		this.selectedTeacherTab = selectedTeacherTab;
	}
}
