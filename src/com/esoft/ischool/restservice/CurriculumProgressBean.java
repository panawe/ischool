package com.esoft.ischool.restservice;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.esoft.ischool.job.Config;
import com.esoft.ischool.model.BaseEntity;
import com.esoft.ischool.model.Course;
import com.esoft.ischool.model.CourseHistory;
import com.esoft.ischool.model.Curriculum;
import com.esoft.ischool.model.CurriculumProgress;
import com.esoft.ischool.model.Level;
import com.esoft.ischool.model.LevelClass;
import com.esoft.ischool.model.SchoolYear;
import com.esoft.ischool.model.Subject;
import com.esoft.ischool.model.Teacher;
import com.esoft.ischool.service.BaseService;
import com.esoft.ischool.stocks.model.Product;
import com.esoft.ischool.stocks.model.ProductConsumer;
import com.esoft.ischool.util.MenuIdEnum;

@Component("curriculumProgressBean")
@Scope("session")
public class CurriculumProgressBean extends BaseBean {

	@Autowired
	@Qualifier("baseService")
	private BaseService baseService;
	
	@Autowired
	@Qualifier("config")
	private Config config;

	private Long rowCount;
	private List<BaseEntity> curriculumProgresses;
	private String teacherName;
	private String levelClassName;

	private CurriculumProgress curriculumProgress = new CurriculumProgress();

	private String selectedTabCurriculumProgress = "curriculumProgressDetail";

	@Override
	public String clear() {
		levelClassName = "";
		curriculumProgress.setComment("");
		return "Success";
	}
	
	public String clearComment() {
		curriculumProgress.setComment("");
		return "Success";
	}

	public String delete() {
		clearMessages();
		try {
			baseService.delete(getIdParameter(), CurriculumProgress.class);
			getAll();
			setSuccessMessage(getResourceBundle().getString("DELETE_SUCCESSFULLY"));
		} catch (Exception ex) {
			ex.printStackTrace();
			setErrorMessage(getResourceBundle().getString("DELETE_UNSUCCESSFULL"));
		}

		return "Success";
	}

	public String insert() {
		clearMessages();
		Long id = curriculumProgress.getId();

		if (teacherName != null) {
			Teacher teacher = (Teacher) baseService.findByColumn(Teacher.class, "matricule", teacherName.split(",")[0]);
			curriculumProgress.setTeacher(teacher);
		}
			
		LevelClass levelClass = (LevelClass) baseService.findByName(LevelClass.class, levelClassName, getCurrentUser().getSchool());
		if (levelClass == null) {
			setErrorMessage(getResourceBundle().getString("LEVELCLASS_NOT_FOUND"));
			return "ERROR";
		}

		Curriculum curriculum = (Curriculum) baseService.getById(Curriculum.class, curriculumProgress.getCurriculum().getId());
		Course course = (Course) baseService.findByParents(Course.class, "subject", curriculum.getSubject().getId()
				, "levelClass", levelClass.getId());
		
		if (course == null) {
			setErrorMessage(getResourceBundle().getString("SUBJECT_NOT_TEACHED_IN_CLASS"));
			return "ERROR";
		}
		
		curriculumProgress.setLevelClass(levelClass);
		curriculumProgress.setTeacher(course.getTeacher());

		try {
			if (id == null || id == 0)
				baseService.save(curriculumProgress, getCurrentUser());
			else 
				baseService.update(curriculumProgress, getCurrentUser());
			setSuccessMessage(getResourceBundle().getString("SAVED_SUCCESSFULLY"));
			clear();
		} catch (Exception ex) {
			curriculumProgress.setId(id);
			setErrorMessage(ex, "Ce progres de curriculum existe deja");
			ex.printStackTrace();
		}

		selectedTabCurriculumProgress="curriculumProgress";
		getAll();
		return "Success";
		
	}

	public String edit() {
		clearMessages();
		curriculumProgress = (CurriculumProgress) baseService.getById(CurriculumProgress.class, getIdParameter());
		levelClassName = curriculumProgress.getLevelClass().getName();
		teacherName = curriculumProgress.getTeacher().getMatricule() + ", "
						+ curriculumProgress.getTeacher().getLastName() + ", "
						+ curriculumProgress.getTeacher().getFirstName();
		
		selectedTabCurriculumProgress = "curriculumProgressDetail";
		return "Success";
	}

	@PostConstruct
	public void getAll() {
		String[] paramNames = null;
		Object[] paramValues = null;
		
		ApplicationContext ctx  = getApplicationContext();
		
		if (((String) getSessionParameter("link")).equals("student")) {
		 	paramNames = new String[] { "schoolId", "studentId"};
			paramValues = new Object[] { getCurrentUser().getSchool().getId(), getSessionParameter("currentStudentId") };
			curriculumProgresses = baseService.getEntitiesByQueryAndParameters(getCurrentUser().getSchool(), "getCurriculumProgressesForStudent", paramNames, paramValues);
			StudentBean studentBean = (StudentBean) ctx.getBean("studentBean");
			studentBean.justDoIt();
		} else if (((String) getSessionParameter("link")).equals("teacher")) {
		 	paramNames = new String[] { "schoolId", "teacherId"};
			paramValues = new Object[] { getCurrentUser().getSchool().getId(), getSessionParameter("currentTeacherId") };
			curriculumProgresses = baseService.getEntitiesByQueryAndParameters(getCurrentUser().getSchool(), "getCurriculumProgressesForTeacher", paramNames, paramValues);
			TeacherBean teacherBean = (TeacherBean) ctx.getBean("teacherBean");
			teacherBean.justDoIt();
		} else if (((String) getSessionParameter("link")).equals("school")) {
			curriculumProgresses = baseService.loadAll(CurriculumProgress.class, getCurrentUser().getSchool());
			SchoolBean schoolBean = (SchoolBean) ctx.getBean("schoolBean");
			schoolBean.justDoIt();
		}		
		
		if(curriculumProgresses!=null){
		for (BaseEntity cp : curriculumProgresses) {
			CurriculumProgress curriculumProgress = (CurriculumProgress) cp;
			curriculumProgress.setStatusDescription(config.getConfigurationByGroupAndKey(
					"CURRICULUM_PROGRESS_STATUS", curriculumProgress.getStatus() + ""));
		}
		setRowCount(new Long(curriculumProgresses.size()));
		}else{
			setRowCount(new Long(0));
		}
	}

	public boolean isUserHasWriteAccess() {
		if (((String) getSessionParameter("link")).equals("student")) {
			return isUserHasWriteAccess(MenuIdEnum.STUDENT.getValue());
		} else if (((String) getSessionParameter("link")).equals("teacher")) {
			return isUserHasWriteAccess(MenuIdEnum.TEACHER.getValue());
		} else if (((String) getSessionParameter("link")).equals("school")) {
			return isUserHasWriteAccess(MenuIdEnum.SCHOOL.getValue());
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

	public List<BaseEntity> getCurriculumProgresses() {
		return curriculumProgresses;
	}

	public void setCurriculumProgresses(List<BaseEntity> curriculumProgresses) {
		this.curriculumProgresses = curriculumProgresses;
	}

	public String getTeacherName() {
		return teacherName;
	}

	public void setTeacherName(String teacherName) {
		this.teacherName = teacherName;
	}

	public String getLevelClassName() {
		return levelClassName;
	}

	public void setLevelClassName(String levelClassName) {
		this.levelClassName = levelClassName;
	}

	public CurriculumProgress getCurriculumProgress() {
		return curriculumProgress;
	}

	public void setCurriculumProgress(CurriculumProgress curriculumProgress) {
		this.curriculumProgress = curriculumProgress;
	}

	public String getSelectedTabCurriculumProgress() {
		return selectedTabCurriculumProgress;
	}

	public void setSelectedTabCurriculumProgress(
			String selectedTabCurriculumProgress) {
		this.selectedTabCurriculumProgress = selectedTabCurriculumProgress;
	}
	
	public String selectCurriculum() {
		setSessionAttribute("curriculumPageProvenence", "CURRICULUM_PROGRESS");
		return "";
	}

	public String addCurriculum() {
		Curriculum curriculum = (Curriculum) baseService.getById(Curriculum.class, getIdParameter());
		curriculumProgress.setCurriculum(curriculum);
		return "";
	}
	
	
	public String levelClassChanged() {
		LevelClass levelClass = (LevelClass) baseService.findByName(LevelClass.class, levelClassName, getCurrentUser().getSchool());
		if (levelClass == null)
			setErrorMessage(getResourceBundle().getString("LEVELCLASS_NOT_FOUND"));

		Curriculum curriculum = (Curriculum) baseService.getById(Curriculum.class, curriculumProgress.getCurriculum().getId());
		Course course = (Course) baseService.findByParents(Course.class, "subject", curriculum.getSubject().getId()
				, "levelClass", levelClass.getId());
		if (course != null) {
			List<BaseEntity> curriculumPs =  baseService.loadByParentsIds(CurriculumProgress.class, 
												"curriculum", curriculumProgress.getCurriculum().getId(),
												"levelClass", levelClass.getId(), "teacher", course.getTeacher().getId());
	
			if (curriculumPs != null && curriculumPs.size() > 0) {
				curriculumProgress = (CurriculumProgress) curriculumPs.get(0);
			}
			else {
				curriculumProgress = new CurriculumProgress();
				curriculumProgress.setLevelClass(levelClass);
				curriculumProgress.setCurriculum(curriculum);
			}
		}		
		else {
			curriculumProgress = new CurriculumProgress();
			curriculumProgress.setLevelClass(levelClass);
			curriculumProgress.setCurriculum(curriculum);
		}
		
		return "";
	}
}