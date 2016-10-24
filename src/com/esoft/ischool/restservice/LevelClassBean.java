package com.esoft.ischool.restservice;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
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
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.richfaces.event.UploadEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.esoft.ischool.model.BaseEntity;
import com.esoft.ischool.model.Course;
import com.esoft.ischool.model.SchoolYear;
import com.esoft.ischool.model.StudentEnrollment;
import com.esoft.ischool.model.Level;
import com.esoft.ischool.model.LevelClass;
import com.esoft.ischool.model.Student;
import com.esoft.ischool.model.Subject;
import com.esoft.ischool.model.Teacher;
import com.esoft.ischool.model.Tuition;
import com.esoft.ischool.service.BaseService;
import com.esoft.ischool.util.MenuIdEnum;

@Component("levelClassBean")
@Scope("session")
public class LevelClassBean extends BaseBean {

	@Autowired
	@Qualifier("baseService")
	private BaseService baseService;
	private Long rowCount;
	private Long searchRowCount;
	private Long levelClassCount;
	private Long enrollmentCount;
	private List<BaseEntity> levelClasses;
	private List<Student> availableStudents = new ArrayList<Student>();
	private List<Student> selectedStudents = new ArrayList<Student>();
	
	private List<Tuition> availableTuitions = new ArrayList<Tuition>();
	private List<Tuition> selectedTuitions = new ArrayList<Tuition>();

	private List<Teacher> availableTeachers = new ArrayList<Teacher>();
	private List<Teacher> selectedTeachers = new ArrayList<Teacher>();

	private List<String> ddLevelClasses;
	private List<String> allLevelClasses = new ArrayList<String>();
	private String levelName;
	private String schoolName;
	private String levelClassName;
	private LevelClass levelClass = new LevelClass();
	private Long levelClassId;
	private List<BaseEntity> enrollments;

	private String year;
	private String className;
	private String decision;
	private boolean selectAllTeachers;
	private boolean selectAllStudents;
	private boolean boxChecked;
	private boolean individual;
	private String message;

	private String selectedTab = "studentList";
	private String schoolYearName;
	private Date enrollmentDate;
	private String teacherName;
	
	private boolean assignTuition;
	

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public boolean isAssignTuition() {
		return assignTuition;
	}

	public void setAssignTuition(boolean assignTuition) {
		this.assignTuition = assignTuition;
	}

	public Long getLevelClassId() {
		return levelClassId;
	}

	public void setLevelClassId(Long levelClassId) {
		this.levelClassId = levelClassId;
	}
	
	public String selectLevelClass() {
		levelClassId = getIdParameter();
		return "success";
	}

	public String getLevelClassName() {
		return levelClassName;
	}

	public void setLevelClassName(String levelClassName) {
		this.levelClassName = levelClassName;
	}

	public Date getEnrollmentDate() {
		return enrollmentDate;
	}

	public void setEnrollmentDate(Date enrollmentDate) {
		this.enrollmentDate = enrollmentDate;
	}

	public List<String> autoComplete(Object suggest) {
		ddLevelClasses = new ArrayList<String>();
		allLevelClasses = new ArrayList<String>();
		for (BaseEntity entity : levelClasses) {
			LevelClass l = (LevelClass) entity;
			if (l.getLevel().getName().equalsIgnoreCase(suggest.toString()))
				ddLevelClasses.add(l.getName());

			allLevelClasses.add(l.getName());
		}
		return ddLevelClasses;
	}

	public Long getSearchRowCount() {
		return searchRowCount;
	}

	public void setSearchRowCount(Long searchRowCount) {
		this.searchRowCount = searchRowCount;
	}

	@Override
	public String getSelectedTab() {
		return selectedTab;
	}

	@Override
	public void setSelectedTab(String selectedTab) {
		this.selectedTab = selectedTab;
	}

	public boolean isBoxChecked() {
		return boxChecked;
	}

	public void setBoxChecked(boolean boxChecked) {
		this.boxChecked = boxChecked;
	}

	public boolean isSelectAllTeachers() {
		return selectAllTeachers;
	}

	public void setSelectAllTeachers(boolean selectAllTeachers) {
		this.selectAllTeachers = selectAllTeachers;
	}

	public boolean isSelectAllStudents() {
		return selectAllStudents;
	}

	public void setSelectAllStudents(boolean selectAllStudents) {
		this.selectAllStudents = selectAllStudents;
	}

	public boolean isSelectTeachers() {
		return selectAllTeachers;
	}

	public void setSelectTeachers(boolean selectAllTeachers) {
		this.selectAllTeachers = selectAllTeachers;
	}

	public String getYear() {
		return year;
	}

	public void setYear(String year) {
		this.year = year;
	}

	public String getClassName() {
		return className;
	}

	public void setClassName(String className) {
		this.className = className;
	}

	public String validate() {
		return "succes";
	}

	@Override
	public String clear() {
		levelClass = new LevelClass();
		setSchoolYearName("");
		setLevelName("");
		setSchoolName("");
		setTeacherName("");
		return "Success";
	}

	public String getShowAll() {
		getAll();
		return "Success";
	}

	public String delete() {
		clearMessages();
		try {
			baseService.delete(getIdParameter(), LevelClass.class);
			getAll();
			clear();
			setSuccessMessage(getResourceBundle().getString("DELETE_SUCCESSFULLY"));
		} catch (Exception ex) {
			setErrorMessage(getResourceBundle().getString("DELETE_UNSUCCESSFULL"));
		}

		return "Success";
	}

	public String deleteTeacherEnrollment() {
		clearMessages();
		try {
			baseService.delete(getIdParameter(), StudentEnrollment.class);
			getTeacherCorrespondences();
			clear();
			setSuccessMessage(getResourceBundle().getString("DELETE_SUCCESSFULLY"));
		} catch (Exception ex) {
			setErrorMessage(getResourceBundle().getString("DELETE_UNSUCCESSFULL"));
		}

		return "Success";
	}

	public String insert() {
		clearMessages();
		Long id = levelClass.getId();
		try {
			Level l = (Level) baseService.findByName(Level.class, levelName,baseService.getDefaultSchool());
			levelClass.setLevel(l);
			
			if (teacherName != null) {
				Teacher teacher = (Teacher) baseService.findByColumn(Teacher.class,
						"matricule", teacherName.split(",")[0]);
				
				levelClass.setResponsibleTeacher(teacher);
			}

			if (id == null || id == 0)
				baseService.save(levelClass,getCurrentUser());
			else
				baseService.update(levelClass,getCurrentUser());

			setSuccessMessage(getResourceBundle().getString("SAVED_SUCCESSFULLY"));
			clear();
			getAll();
		} catch (Exception ex) {
			ex.printStackTrace();
			levelClass.setId(id);
			setErrorMessage(ex, "Cette level classe exist deja. ");
		}

		return "S";
	}

	public String inscrire() {
		clearMessages();
		try {
			LevelClass lClasse=null;
			SchoolYear schoolYear=null;
			
			if(levelClassName!=null){
				lClasse = (LevelClass) baseService.findByName(LevelClass.class, levelClassName,getCurrentUser().getSchool());
			}
			

			
			if (schoolYearName != null) {
				schoolYear= (SchoolYear) baseService.findByColumn(
						SchoolYear.class, "year",  schoolYearName,baseService.getDefaultSchool());
			}
			if(lClasse != null && schoolYear != null && enrollmentDate != null){
				Integer actuallyClassNumberOfStudents = baseService.getStudentsCountByLevelClassAndYear(schoolYear.getId(), lClasse.getId(), baseService.getDefaultSchool());
				if(lClasse.getCapacity() < actuallyClassNumberOfStudents + selectedStudents.size()){
					setErrorMessage(getResourceBundle().getString("CAPACITY_EXCEEDED") + " : " + lClasse.getCapacity() + " < " + (actuallyClassNumberOfStudents + selectedStudents.size()));
					return "Error";
				}
				baseService.saveStudentsEnrollment(lClasse,
						selectedStudents, schoolYear, enrollmentDate, getCurrentUser());
				baseService.saveStudentTuitions(selectedStudents, selectedTuitions, getCurrentUser());
				setSuccessMessage(getResourceBundle().getString("SAVED_SUCCESSFULLY"));
				lClasse.setNbrStudents(actuallyClassNumberOfStudents + selectedStudents.size());
				baseService.update(lClasse, getCurrentUser());
				levelClassName = null;
				schoolYearName = null;
				enrollmentDate = null;
				selectedStudents = new ArrayList<Student>();
				availableStudents = null;
			}else{
				if(assignTuition){
					baseService.saveStudentTuitions(selectedStudents, selectedTuitions, getCurrentUser());
					setSuccessMessage(getResourceBundle().getString("SAVED_SUCCESSFULLY"));
				}else{
					setErrorMessage(getResourceBundle().getString("MISSING_REQUIRED_FIELD"));
				}
			}
			
		} catch (Exception ex) {
			ex.printStackTrace();
			setErrorMessage(ex, "Cette level classe exist deja. ");
		}

		return "Success";
	}

	public String insertTeacherLevelClass() {
		clearMessages();
		Long id = levelClass.getId();
		try {
			if (id == null || id == 0)
				baseService.save(levelClass,getCurrentUser());
			else
				baseService.update(levelClass,getCurrentUser());
			setSuccessMessage(getResourceBundle().getString("SAVED_SUCCESSFULLY"));
		} catch (Exception ex) {
			levelClass.setId(id);
			setErrorMessage(ex, "Cette level class exist deja. ");
		}

		clear();
		getTeacherCorrespondences();
		return "Success";
	}

	public String edit() {
		clearMessages();
		selectedTab = "studentDetails";
		levelClass = (LevelClass) baseService.getById(LevelClass.class,
				getIdParameter());
		if (levelClass.getResponsibleTeacher() != null) {
			teacherName = levelClass.getResponsibleTeacher().getMatricule() + ", "
						+ levelClass.getResponsibleTeacher().getLastName() + ", "
						+ levelClass.getResponsibleTeacher().getFirstName();
		}
		else
			teacherName = "";
		setLevelName(levelClass.getLevel().getName());
		setSchoolName(levelClass.getSchool().getName());

		return "Success";
	}

	
	public String printClassFinance() {

		try {

			Map<String, Serializable> parameters = new HashMap<String, Serializable>();

			if (levelClassId != null) {

				parameters.put("levelClassId", levelClassId);
				parameters
						.put("schoolId", getCurrentUser().getSchool().getId());
				FacesContext context = FacesContext.getCurrentInstance();

				String reportsDirPath = ((ServletContext) context
						.getExternalContext().getContext())
						.getRealPath("/reports/");

				parameters.put("SUBREPORT_DIR", reportsDirPath
						+ java.io.File.separator);
				HttpServletResponse response = (HttpServletResponse) context
						.getExternalContext().getResponse();

				InputStream reportStream = context.getExternalContext()
						.getResourceAsStream("/reports/etatFinancierClasse.jasper");

				ServletOutputStream ouputStream = response.getOutputStream();

				response.addHeader("Content-disposition",
						"attachment;filename=Etat Financier Classe"+ "-"+getStringDate()+".pdf");

				JasperRunManager.runReportToPdfStream(reportStream,
						ouputStream, parameters, getConnection());
				response.setContentType("application/pdf");
				ouputStream.flush();
				ouputStream.close();
				context.responseComplete();
				levelClassId  = null;
			}
		} catch (JRException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return "success";

	}


	public String printReminders() {

		try {

			Map<String, Serializable> parameters = new HashMap<String, Serializable>();

			if (levelClassId != null) {

				parameters.put("classId", levelClassId);
				parameters.put("message", message);
				parameters
						.put("schoolId", getCurrentUser().getSchool().getId());
				FacesContext context = FacesContext.getCurrentInstance();

				String reportsDirPath = ((ServletContext) context
						.getExternalContext().getContext())
						.getRealPath("/reports/");

				parameters.put("SUBREPORT_DIR", reportsDirPath
						+ java.io.File.separator);
				HttpServletResponse response = (HttpServletResponse) context
						.getExternalContext().getResponse();

				InputStream reportStream = context.getExternalContext()
						.getResourceAsStream("/reports/derniereRelance.jasper");

				ServletOutputStream ouputStream = response.getOutputStream();

				response.addHeader("Content-disposition",
						"attachment;filename=Dernieres Relances"+ "-"+getStringDate()+".pdf");

				JasperRunManager.runReportToPdfStream(reportStream,
						ouputStream, parameters, getConnection());
				response.setContentType("application/pdf");
				ouputStream.flush();
				ouputStream.close();
				context.responseComplete();
				levelClassId  = null;
			}
		} catch (JRException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return "success";

	}
	public String printStudentID() {

		try {

			Map<String, Serializable> parameters = new HashMap<String, Serializable>();

			if (levelClassId != null) {

				parameters.put("levelClassId", levelClassId);
				parameters
						.put("schoolId", getCurrentUser().getSchool().getId());
				FacesContext context = FacesContext.getCurrentInstance();

				String reportsDirPath = ((ServletContext) context
						.getExternalContext().getContext())
						.getRealPath("/reports/");

				parameters.put("SUBREPORT_DIR", reportsDirPath
						+ java.io.File.separator);
				HttpServletResponse response = (HttpServletResponse) context
						.getExternalContext().getResponse();

				InputStream reportStream = context.getExternalContext()
						.getResourceAsStream("/reports/idcards.jasper");

				ServletOutputStream ouputStream = response.getOutputStream();

				response.addHeader("Content-disposition",
						"attachment;filename=Carte d'identite"+ "-"+getStringDate()+".pdf");

				JasperRunManager.runReportToPdfStream(reportStream,
						ouputStream, parameters, getConnection());
				response.setContentType("application/pdf");
				ouputStream.flush();
				ouputStream.close();
				context.responseComplete();
				levelClassId = null;
			}
		} catch (JRException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return "success";

	}

	public String printStudentList() {

		try {

			Map<String, Serializable> parameters = new HashMap<String, Serializable>();

			if (levelClassId != null) {

				parameters.put("classId", levelClassId);
				parameters
						.put("schoolId", getCurrentUser().getSchool().getId());
				FacesContext context = FacesContext.getCurrentInstance();

				String reportsDirPath = ((ServletContext) context
						.getExternalContext().getContext())
						.getRealPath("/reports/");

				parameters.put("SUBREPORT_DIR", reportsDirPath
						+ java.io.File.separator);
				HttpServletResponse response = (HttpServletResponse) context
						.getExternalContext().getResponse();

				InputStream reportStream = context.getExternalContext()
						.getResourceAsStream("/reports/classStudentList.jasper");

				ServletOutputStream ouputStream = response.getOutputStream();

				response.addHeader("Content-disposition",
						"attachment;filename=Liste Nominative"+ "-"+getStringDate()+".pdf");

				JasperRunManager.runReportToPdfStream(reportStream,
						ouputStream, parameters, getConnection());
				response.setContentType("application/pdf");
				ouputStream.flush();
				ouputStream.close();
				context.responseComplete();
				levelClassId = null;
			}
		} catch (JRException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return "success";

	}

	@PostConstruct
	private void getAll() {
		SchoolYear sy = baseService.getSchoolYear(new Date(),baseService.getDefaultSchool());
		year=sy==null?year:sy.getYear();
		levelClasses = baseService.loadAll(LevelClass.class,getCurrentUser()!=null?getCurrentUser().getSchool():baseService.getDefaultSchool());
		setLevelClassCount(new Long(levelClasses.size()));
		allLevelClasses.clear();
		setRowCount(new Long(levelClasses.size()));
		for (BaseEntity entity : levelClasses) {
			LevelClass levelClass = (LevelClass) entity;
			allLevelClasses.add(levelClass.getName());
		}
		if(getCurrentUser()!=null)
		message="A d�faut de payement � la date limite ci-dessus, votre enfant ne sera plus accept� en classe.\n"+
				"La comptabilit� est ouverte tous les jours du lundi au vendredi de 7h � 11h 30 et de 15h � 17h. "+
				"Le r�glement peut �tre effectu� "+
				"en esp�ces CFA ou par ch�que CFA libell� � l�ordre de �"+getCurrentUser().getSchool().getName()+" �.\n";
		
	}

	public String selectedLevelName() {
		setSessionAttribute("levelName", levelName);
		autoComplete(levelName);
		return "";
	}

	public String getTeacherCorrespondences() {
		setIndividual(true);
		if (getSessionParameter("currentTeacherId") != null) {
			enrollments = baseService
					.loadAllByParentId(StudentEnrollment.class, "teacher",
							"id",
							new Long(getSessionParameter("currentTeacherId")
									.toString()));
			setRowCount(new Long(enrollments.size()));
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

	public List<Student> getAvailableStudents() {
		return availableStudents;
	}

	public void setAvailableStudents(List<Student> availableStudents) {
		this.availableStudents = availableStudents;
	}

	public List<Student> getSelectedStudents() {
		return selectedStudents;
	}

	public void setSelectedStudents(List<Student> selectedStudents) {
		this.selectedStudents = selectedStudents;
	}

	public List<Teacher> getAvailableTeachers() {
		return availableTeachers;
	}

	public List<String> getAllLevelClasses() {
		autoComplete("");
		return allLevelClasses;
	}

	public void setAvailableTeachers(List<Teacher> availableTeachers) {
		this.availableTeachers = availableTeachers;
	}

	public List<Teacher> getSelectedTeachers() {
		return selectedTeachers;
	}

	public void setSelectedTeachers(List<Teacher> selectedTeachers) {
		this.selectedTeachers = selectedTeachers;
	}

	public String updateTeacherSelection() {
		if (selectAllTeachers) {
			selectAllStudents = false;
			boxChecked = true;
		} else {
			boxChecked = false;
		}

		return "Success";
	}

	public String updateStudentSelection() {
		if (selectAllStudents) {
			selectAllTeachers = false;
			boxChecked = true;
		} else {
			boxChecked = false;
		}
		return "Success";
	}
	
	public String updateAssignTuition(){
		return "success";
	}
	
	public String search() {
		availableTeachers = null;
		availableStudents = null;
		selectedStudents = new ArrayList<Student>();
		selectedTeachers = new ArrayList<Teacher>();
		if (boxChecked) {
			if (selectAllStudents){
				availableStudents = baseService.searchStudents(
						selectAllStudents, className, year,getCurrentUser().getSchool());
			}else{
				availableTeachers = baseService.searchTeachers(
						selectAllTeachers, className, year,getCurrentUser().getSchool());
			}
		} else {
			availableStudents = baseService.searchStudents(selectAllStudents,
					className, year,getCurrentUser().getSchool());
		}

		searchRowCount = new Long(
				availableTeachers != null ? availableTeachers.size()
						: (availableStudents != null ? availableStudents.size()
								: 0));
		return "Success";
	}

	public String findTuitions() {
		selectedTuitions= new ArrayList<Tuition>();
		availableTuitions=baseService.fetchSortedTuitions(getCurrentUser().getSchool(), schoolYearName);
		return "Success";
	}
	
	public boolean isUserHasWriteAccess() {
		if ( ((String)getSessionParameter("link")).equals("student")) {
			return isUserHasWriteAccess(MenuIdEnum.STUDENT.getValue());
		} else if ( ((String)getSessionParameter("link")).equals("teacher")) {
			return isUserHasWriteAccess(MenuIdEnum.TEACHER.getValue());
		} else if (((String)getSessionParameter("link")).equals("inscriptions")) {
			return isUserHasWriteAccess(MenuIdEnum.INSCRIPTIONS.getValue());
		}
		return false;
	}
	
	public void uploadOldEnrollments(UploadEvent event) throws Exception {
		byte[] content = event.getUploadItem().getData();
		ServletContext ctx = (ServletContext) getContext() .getExternalContext().getContext(); 
		File tempDir = new File(ctx.getRealPath("") + "/temp");   
		File tempFile = File.createTempFile("oldEnrollments", ".xlsx", tempDir);   
		    
		setErrorMessage("");
		OutputStream out = new FileOutputStream(tempFile);
		out.write(content);
		out.close();
		
		FileInputStream file = new FileInputStream(tempFile);
         
		//Get the workbook instance for XLS file
		XSSFWorkbook workbook = new XSSFWorkbook(file);
		 
		//Get first sheet from the workbook
		XSSFSheet sheet = workbook.getSheetAt(0);
		
		String matricule = "";
		String lastName = "";
		String firstName = "";
		Map<Integer, String> yearMap = new HashMap<Integer, String>();
		Map<String, SchoolYear> schoolYearMap = new HashMap<String, SchoolYear>();
		Map<String, Student> studentMap = new HashMap<String, Student>();
		String levelClassValue = null;
		LevelClass levelClass = null;
    	
		//Iterate through each rows from first sheet
	    Iterator<Row> rowIterator = sheet.iterator();
	    while(rowIterator.hasNext()) {
	        Row row = rowIterator.next();
	        if (row.getRowNum() == 0) {
	        	//For each row, iterate through each columns
		        Iterator<Cell> cellIterator = row.cellIterator();
		        int i = 1;
		        while(cellIterator.hasNext()) {
		        	Cell cell = cellIterator.next();
		             if (i >= 4) {
		            	yearMap.put(i, cell.getStringCellValue());
		            	schoolYearMap.put(cell.getStringCellValue(), (SchoolYear) baseService.findByColumn(SchoolYear.class, "year", cell.getStringCellValue(), baseService.getDefaultSchool()));
		            }
		             
		            i++;
		        }
	        }
	        else if (row.getRowNum() >= 1) {
	        	matricule = "";
	    		lastName = "";
	    		firstName = "";
            	Student stud = null;
	        
	        	//For each row, iterate through each columns
		        Iterator<Cell> cellIterator = row.cellIterator();
		        int i = 1;
		        while(cellIterator.hasNext()) {
		            Cell cell = cellIterator.next();
		            if (i == 1) {
		            	matricule = cell.getStringCellValue();
		            }
		            else if (i == 2) {
		            	lastName = cell.getStringCellValue();
		            }
		            else if (i == 3) {
		            	firstName = cell.getStringCellValue();
		            	
				        if (StringUtils.isNotBlank(matricule)) {
				        	stud = studentMap.get(matricule);
				        	if (stud == null) {
				        		stud = (Student) baseService.findByColumn(Student.class, "matricule", matricule);
				        		studentMap.put(matricule, stud);
				        	}
				        	
				        	if (stud == null)
				        		setErrorMessage(getErrorMessage() + getResourceBundle().getString("INVALID_MATRICULE") + " " + matricule + "\n");
				        	
				        }
				        else if (StringUtils.isNotBlank(firstName) && StringUtils.isNotBlank(lastName)) {
				        	List<String> columnNames = new ArrayList<String>();
				        	columnNames.add("lastName");
				        	columnNames.add("firstName");
				        	
				        	List<String> columnValues = new ArrayList<String>();
				        	columnValues.add(lastName);
				        	columnValues.add(firstName);
				        	
				        	stud = studentMap.get(firstName + lastName);
				        	
				        	if (stud == null) {
	    			        	List<BaseEntity> studs = baseService.findByColumns(Student.class, columnNames, columnValues);
	    			        	if (studs != null && studs.size() > 0) {
	    			        		stud = (Student) studs.get(0);
	    			        	}
				        	}
				        	
				        	if (stud == null)
				        		setErrorMessage(getErrorMessage() + getResourceBundle().getString("INVALID_FIRST_AND_LAST_NAMES") + ": " + lastName + " " + firstName + "\n");
				        }
				        
		            }
		            
		            if (i >= 4 && stud != null) {
	            		levelClassValue = cell.getStringCellValue();
	            		if (StringUtils.isNotBlank(levelClassValue)) {
			            	levelClass = (LevelClass) baseService.findByName(LevelClass.class, levelClassValue, getCurrentUser().getSchool());
			            	if (levelClass == null) {
			            		setErrorMessage(getErrorMessage() + " Cette classe " + levelClassValue + " n'est pas valide. ");
			            	}
			            	else {
			            		if (baseService.getStudentEnrollment(stud.getId(), levelClass.getId(), schoolYearMap.get(yearMap.get(i)).getId()) == null) {
				            		StudentEnrollment studentEnrollment = new StudentEnrollment();
				            		studentEnrollment.setSchoolYear(schoolYearMap.get(yearMap.get(i)));
				            		studentEnrollment.setLevelClass(levelClass);
				            		studentEnrollment.setStudent(stud);
				            		studentEnrollment.setEnrollmentDate(new Date());
				            		//studentEnrollment.setComments("Inscription anterieure.");
				            		baseService.save(studentEnrollment, getCurrentUser());
			            		}
			            		else {
			            			setErrorMessage(getErrorMessage() + " Cette inscription " + stud.getLastName() + " " + stud.getFirstName() + "/"+ levelClassValue + "/" + yearMap.get(i) + " exists deja. \n");
			            		}
			            	}
	            		}
	            	}
		            
		            i++;
		        }
	        }
	    }
	    
	    file.close();

	    if (!tempFile.delete()) {
	    	setErrorMessage("Fichier " + tempFile.getName() + " n'est pas ete supprime. ");
	    }
	}
	
	public String doNothing() {
		return "Success";
	}

	public boolean isIndividual() {
		return individual;
	}

	public void setIndividual(boolean individual) {
		this.individual = individual;
	}

	public Long getLevelClassCount() {
		return levelClassCount;
	}

	public void setLevelClassCount(Long levelClassCount) {
		this.levelClassCount = levelClassCount;
	}

	public Long getEnrollmentCount() {
		return enrollmentCount;
	}

	public void setEnrollmentCount(Long enrollmentCount) {
		this.enrollmentCount = enrollmentCount;
	}

	public List<BaseEntity> getLevelClasses() {
		return levelClasses;
	}

	public void setLevelClasses(List<BaseEntity> levelClasses) {
		this.levelClasses = levelClasses;
	}

	public LevelClass getLevelClass() {
		return levelClass;
	}

	public void setLevelClass(LevelClass levelClass) {
		this.levelClass = levelClass;
	}

	public List<BaseEntity> getEnrollments() {
		return enrollments;
	}

	public void setEnrollments(List<BaseEntity> enrollments) {
		this.enrollments = enrollments;
	}

	public List<String> getDdLevelClasses() {
		return ddLevelClasses;
	}

	public void setDdLevelClasses(List<String> ddLevelClasses) {
		this.ddLevelClasses = ddLevelClasses;
	}

	public String getLevelName() {
		return levelName;
	}

	public void setLevelName(String levelName) {
		this.levelName = levelName;
	}

	public String getSchoolName() {
		return schoolName;
	}

	public void setSchoolName(String schoolName) {
		this.schoolName = schoolName;
	}

	public void setAllLevelClasses(List<String> allLevelClasses) {
		this.allLevelClasses = allLevelClasses;
	}

	public String getSchoolYearName() {
		return schoolYearName;
	}

	public void setSchoolYearName(String schoolYearName) {
		this.schoolYearName = schoolYearName;
	}

	public String getTeacherName() {
		return teacherName;
	}

	public void setTeacherName(String teacherName) {
		this.teacherName = teacherName;
	}

	public List<Tuition> getAvailableTuitions() {
		return availableTuitions;
	}

	public void setAvailableTuitions(List<Tuition> availableTuitions) {
		this.availableTuitions = availableTuitions;
	}

	public List<Tuition> getSelectedTuitions() {
		return selectedTuitions;
	}

	public void setSelectedTuitions(List<Tuition> selectedTuitions) {
		this.selectedTuitions = selectedTuitions;
	}

	public String getDecision() {
		return decision;
	}

	public void setDecision(String decision) {
		this.decision = decision;
	}
}
