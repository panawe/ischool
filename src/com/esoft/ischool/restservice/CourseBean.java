package com.esoft.ischool.restservice;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.servlet.ServletContext;

import org.apache.commons.lang.StringUtils;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.richfaces.event.UploadEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.esoft.ischool.job.Config;
import com.esoft.ischool.model.BaseEntity;
import com.esoft.ischool.model.Course;
import com.esoft.ischool.model.CourseHistory;
import com.esoft.ischool.model.LevelClass;
import com.esoft.ischool.model.Subject;
import com.esoft.ischool.model.Teacher;
import com.esoft.ischool.service.BaseService;
import com.esoft.ischool.util.MenuIdEnum;
import com.esoft.ischool.vo.CourseVO;

@Component("courseBean")
@Scope("session")
public class CourseBean extends BaseBean {

	@Autowired
	@Qualifier("baseService")
	private BaseService baseService;
	private Long rowCount;
	private List<CourseVO> courses;
	private List<BaseEntity> pastCourses;
	private List<String> allCourses = new ArrayList<String>();
	private List<String> allTeacherClasses = new ArrayList<String>();
	private Long pastCourseCnt;
	private String teacherName;
	private String levelClassName;
	private String subjectName;
	
	@Autowired
	@Qualifier("config")
	private Config config;
	
	private Course course = new Course();

	private String selectedTabCourse = "courseDetail";

	public String getTeacherName() {
		return teacherName;
	}

	public void setTeacherName(String teacherName) {
		this.teacherName = teacherName;
	}

	public Long getPastCourseCnt() {
		return pastCourseCnt;
	}

	public void setPastCourseCnt(Long pastCourseCnt) {
		this.pastCourseCnt = pastCourseCnt;
	}

	public List<BaseEntity> getPastCourses() {
		return pastCourses;
	}

	public void setPastCourses(List<BaseEntity> pastCourses) {
		this.pastCourses = pastCourses;
	}

	public String getSelectedTabCourse() {
		return selectedTabCourse;
	}

	public void setSelectedTabCourse(String selectedTabCourse) {
		this.selectedTabCourse = selectedTabCourse;
	}

	public String validate() {
		return "succes";
	}

	@Override
	public String clear() {
		course = new Course();
		levelClassName = "";
		subjectName = "";
		teacherName = "";
		return "Success";
	}

	public List<CourseVO> autoComplete(Object suggest) {
		String pref = (String) suggest;
		List<CourseVO> result = new ArrayList<CourseVO>();
		allCourses.clear();
		allTeacherClasses.clear();

		for (CourseVO aCourse : courses) {
			 
			if ((aCourse.getSubject() != null && aCourse.getSubject()
					.toLowerCase().indexOf(pref.toLowerCase()) == 0)
					|| "".equals(pref))
				result.add(aCourse);

			allCourses.add(aCourse.getSubject());
			if (getSessionParameter("currentTeacherId") != null
					&& getSessionParameter("currentTeacherId").toString() != null
					&& aCourse.getTeacherId().toString().equals(getSessionParameter("currentTeacherId").toString()) 
					&& !allTeacherClasses.contains(aCourse.getClassName()))
			allTeacherClasses.add(aCourse.getClassName());
		}

		return result;
	}

	public String delete() {
		clearMessages();
		try {
			baseService.delete(getIdParameter(), Course.class);
			getAll();
			setSuccessMessage(getResourceBundle().getString("DELETE_SUCCESSFULLY"));
		} catch (Exception ex) {
			ex.printStackTrace();
			setErrorMessage(getResourceBundle().getString("DELETE_UNSUCCESSFULL"));
		}

		return "Success";
	}

	public String deleteTeacherCourse() {
		clearMessages();
		try {
			baseService.delete(getIdParameter(), Course.class);
			getTeacherCourses();
			setSuccessMessage(getResourceBundle().getString("DELETE_SUCCESSFULLY"));
		} catch (Exception e) {
			e.printStackTrace();
			setErrorMessage(getResourceBundle().getString("DELETE_UNSUCCESSFULL"));
			return "Error";
		}
		return "Success";
	}

	public String insert() {
		clearMessages();
		Long id = course.getId();

		LevelClass levelClass = (LevelClass) baseService.findByName(
				LevelClass.class, levelClassName, getCurrentUser().getSchool());
		if (levelClass == null)
			setErrorMessage(getResourceBundle().getString("LEVEL_CLASS_NOT_FOUND"));

		Subject subject = (Subject) baseService.findByName(Subject.class,
				subjectName, baseService.getDefaultSchool());
		if (subject == null)
			setErrorMessage(getErrorMessage() + getResourceBundle().getString("SUBJECT_NOT_FOUND"));

		Teacher teacher;
		if ("course".equals(getParameter("link")) || (teacherName != null&&!teacherName.equals(""))) {
			teacher = (Teacher) baseService.findByColumn(Teacher.class,
					"matricule", teacherName.split(",")[0]);
		} else {
			teacher = (Teacher) baseService.getById(Teacher.class, new Long(
					getSessionParameter("currentTeacherId").toString()));
		}

		if (teacher == null) {
			setErrorMessage(getErrorMessage() + getResourceBundle().getString("INVALID_TEACHER"));
			return "ERROR";
		}

		course.setLevelClass(levelClass);
		course.setSubject(subject);
		course.setTeacher(teacher);

		try {
			if (id == null || id == 0)
				baseService.save(course, getCurrentUser());
			else {

				Course aCourse = (Course) baseService.getById(Course.class,
						course.getId());
				if (!aCourse.getTeacher().getMatricule()
						.equals(course.getTeacher().getMatricule())) {
					CourseHistory ch = new CourseHistory(aCourse);
					ch.setEndDate(course.getBeginDate());
					baseService.save(ch, getCurrentUser());
				}
				baseService.update(course, getCurrentUser());
			}
			setSuccessMessage(getResourceBundle().getString("SAVED_SUCCESSFULLY"));
			clear();
		} catch (Exception ex) {
			course.setId(id);
			setErrorMessage(ex, "Ce course existe deja");
		}

		if ("course".equals(getParameter("link")) || teacherName != null)
			getAll();
		else
			getTeacherCourses();

		return "Success";
	}

	public String edit() {
		clearMessages();
		course = (Course) baseService.getById(Course.class, getIdParameter());
		levelClassName = course.getLevelClass().getName();
		subjectName = course.getSubject().getName();
		teacherName = course.getTeacher().getMatricule() + ", "
				+ course.getTeacher().getLastName() + ", "
				+ course.getTeacher().getFirstName();
		selectedTabCourse = "courseDetail";
		setSelectedTab("studentSchoolingDetails");
		return "Success";
	}

	@PostConstruct
	public void getAll() {
		courses = baseService.getCourses(getCurrentUser().getSchool());
		setRowCount(new Long(courses.size()));
		for (CourseVO aCourse : courses) {
			allCourses.add(aCourse.getSubject());
			if (aCourse.getGroupCode() != null)
				aCourse.setCourseGroupDesc(config.getConfigurationByGroupAndKey(
					"COURSE_GROUP", aCourse.getGroupCode()));
		}
	}

	public String getTeacherCourses() {
		if (getSessionParameter("currentTeacherId") != null) {
			courses = baseService
					.getCourses(getCurrentUser()
									.getSchool() ,
									new Long(getSessionParameter("currentTeacherId")
											.toString()));
			
			for (CourseVO aCourse : courses) {
				allCourses.add(aCourse.getSubject());
				if (aCourse.getGroupCode() != null)
					aCourse.setCourseGroupDesc(config.getConfigurationByGroupAndKey(
						"COURSE_GROUP", aCourse.getGroupCode()));
			}
			
			setRowCount(new Long(courses.size()));
		}
		return "Success";
	}

	
	
	public String getTeacherPastCourses() {
		if (getSessionParameter("currentTeacherId") != null) {
			pastCourses = baseService
					.loadAllByParentId(CourseHistory.class, "teacher", "id",
							new Long(getSessionParameter("currentTeacherId")
									.toString()));
			
			pastCourseCnt = new Long(courses.size());
		}
		return "Success";
	}
	
	public String initializeCourseImport() {
		return "";
	}
	
	public void pieceslistener(UploadEvent event) throws Exception {
		byte[] content = event.getUploadItem().getData();
		ServletContext ctx = (ServletContext) getContext() .getExternalContext().getContext(); 
		File tempDir = new File(ctx.getRealPath("") + "/temp");   
		File tempFile = File.createTempFile("courseList", ".xlsx", tempDir);   
		    
		setErrorMessage("");
		OutputStream out = new FileOutputStream(tempFile);
		out.write(content);
		out.close();
		
		FileInputStream file = new FileInputStream(tempFile);
         
		//Get the workbook instance for XLS file
		XSSFWorkbook workbook = new XSSFWorkbook(file);
		 
		//Get first sheet from the workbook
		XSSFSheet sheet = workbook.getSheetAt(0);
		
		Date beginDate = null;
    	String subjectValue = null;
    	String classValue = null;
    	double maxMarkValue = 0;
    	String profLastName = null;
    	String profFirstName = null;
    	
    	Course course = null;
    	LevelClass levelClass = null;
    	Subject subject = null;
    	Teacher teacher = null;

    	
		//Iterate through each rows from first sheet
	    Iterator<Row> rowIterator = sheet.iterator();
	    while(rowIterator.hasNext()) {
	        Row row = rowIterator.next();
	        if (row.getRowNum() >= 1) {
	        	
	        	beginDate = null;
	        	subjectValue = null;
	        	classValue = null;
	        	maxMarkValue = 0;
	        	profLastName = null;
	        	profFirstName = null;
	        	
	        	course = null;
	        	levelClass = null;
	        	subject = null;
	        	teacher = null;

	        	
	        	//For each row, iterate through each columns
		        Iterator<Cell> cellIterator = row.cellIterator();
		        int i = 1;
		        while(cellIterator.hasNext()) {
		            Cell cell = cellIterator.next();
		            if (i == 1) {
		            	beginDate = cell.getDateCellValue();
		            }
		            else if (i == 2) {
		            	subjectValue = cell.getStringCellValue();
		            	subject = (Subject) baseService.findByName(Subject.class, subjectValue, getCurrentUser().getSchool());
		            	if (subject == null) {
		            		setErrorMessage(getErrorMessage() + " Cette matiere " + subjectValue + " n'est pas valide. ");
		            	}
		            }
		            else if (i == 3) {
		            	classValue = cell.getStringCellValue();
		            	levelClass = (LevelClass) baseService.findByName(LevelClass.class, classValue, getCurrentUser().getSchool());
		            	if (levelClass == null) {
		            		setErrorMessage(getErrorMessage() + " Cette classe " + classValue + " n'est pas valide. ");
		            	}
		            }
		            else if (i == 4) {
		            	maxMarkValue = cell.getNumericCellValue();
		            	if (maxMarkValue <= 0) {
		            		setErrorMessage(getErrorMessage() + " Ce maximum: " + maxMarkValue + " n'est pas valide. ");
		            	}
		            }
		            else if (i == 5) {
		            	profLastName = cell.getStringCellValue();
		            	if (StringUtils.isEmpty(profLastName)) {
		            		setErrorMessage(getErrorMessage() + " Le nom du professeur est obligatoire. ");
		            	}
		            }
		            else if (i == 6) {
		            	profFirstName = cell.getStringCellValue();
		            	if (StringUtils.isEmpty(profLastName)) {
		            		setErrorMessage(getErrorMessage() + " Le prenom du professeur est obligatoire. ");
		            	}
		            }

		            if (StringUtils.isNotBlank(profFirstName) && StringUtils.isNotBlank(profLastName)) {
			        	List<String> columnNames = new ArrayList<String>();
			        	columnNames.add("lastName");
			        	columnNames.add("firstName");
			        	
			        	List<String> columnValues = new ArrayList<String>();
			        	columnValues.add(profLastName);
			        	columnValues.add(profFirstName);
			        	
			        	List<BaseEntity> teachers = baseService.findByColumns(Teacher.class, columnNames, columnValues);
			        	if (teachers == null || teachers.isEmpty())
			        		setErrorMessage(getErrorMessage() + getResourceBundle().getString("INVALID_FIRST_AND_LAST_NAMES") + ": " + profLastName + " " + profFirstName + "\n");
			        	else 
			        		teacher = (Teacher) teachers.get(0);
			        }		            
		            i++;
		        }
		        
		        if (StringUtils.isNotBlank(getErrorMessage()))
		        	break;
		        else {
		        	course = new Course();
		        	course.setBeginDate(beginDate);
		        	course.setMaxMark(maxMarkValue);
		        	course.setLevelClass(levelClass);
		        	course.setSubject(subject);
		        	course.setTeacher(teacher);
		        	baseService.save(course, getCurrentUser());
		        }
	        }
	    }
	    
	    file.close();
	    getAll();

	    if (!tempFile.delete()) {
	    	setErrorMessage("Fichier " + tempFile.getName() + " n'est pas ete supprime. ");
	    }
	}
	
	

	public boolean isUserHasWriteAccess() {
		if (((String) getSessionParameter("link")).equals("student")) {
			return isUserHasWriteAccess(MenuIdEnum.STUDENT.getValue());
		} else if (((String) getSessionParameter("link")).equals("teacher")) {
			return isUserHasWriteAccess(MenuIdEnum.TEACHER.getValue());
		} else if (((String) getSessionParameter("link")).equals("course")) {
			return isUserHasWriteAccess(MenuIdEnum.COURSE.getValue());
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

	public List<CourseVO> getCourses() {
		if ("school".equals(getSessionParameter("link"))) {
			LevelClassBean bean = (LevelClassBean) getSessionParameter("levelClassBean"); 
			Long id = bean.getLevelClass().getId();
			
			if(id!=null){
			courses =  baseService.getCourses(id, getCurrentUser().getSchool());
			}
		}
		
		return courses;
	}

	public void setCourses(List<CourseVO> courses) {
		this.courses = courses;
	}

	public Course getCourse() {
		return course;
	}

	public void setCourse(Course course) {
		this.course = course;
	}

	public List<String> getAllCourses() {
		autoComplete("");
		return allCourses;
	}

	public void setAllCourses(List<String> allCourses) {
		this.allCourses = allCourses;
	}

	
	public List<String> getAllTeacherClasses() {
		autoComplete("");
		return allTeacherClasses;
	}

	public void setAllTeacherClasses(List<String> allTeacherClasses) {
		this.allTeacherClasses = allTeacherClasses;
	}

	public String getLevelClassName() {
		return levelClassName;
	}

	public void setLevelClassName(String levelClassName) {
		this.levelClassName = levelClassName;
	}

	public String getSubjectName() {
		return subjectName;
	}

	public void setSubjectName(String subjectName) {
		this.subjectName = subjectName;
	}

	public Config getConfig() {
		return config;
	}

	public void setConfig(Config config) {
		this.config = config;
	}

}