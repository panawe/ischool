package com.esoft.ischool.restservice;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.faces.context.FacesContext;
import javax.servlet.ServletContext;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
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
import com.esoft.ischool.model.Exam;
import com.esoft.ischool.model.ExamType;
import com.esoft.ischool.model.Files;
import com.esoft.ischool.model.Grade;
import com.esoft.ischool.model.LevelClass;
import com.esoft.ischool.model.Mark;
import com.esoft.ischool.model.SchoolYear;
import com.esoft.ischool.model.Student;
import com.esoft.ischool.model.StudentEnrollment;
import com.esoft.ischool.model.Subject;
import com.esoft.ischool.model.Teacher;
import com.esoft.ischool.model.Term;
import com.esoft.ischool.model.Test;
import com.esoft.ischool.model.UserTest;
import com.esoft.ischool.security.model.User;
import com.esoft.ischool.service.BaseService;
import com.esoft.ischool.service.ExamService;
import com.esoft.ischool.util.MenuIdEnum;
import com.esoft.ischool.vo.ExamSearchVO;
import com.esoft.ischool.vo.ExamVO;
import com.esoft.ischool.vo.MarkSearchVO;
import com.esoft.ischool.vo.MarkVO;
import com.esoft.ischool.vo.TeacherVO;

@Component("examBean")
@Scope("session")
public class ExamBean extends BaseBean {

	@Autowired
	@Qualifier("examService")
	private ExamService examService;
	private Long rowCount;
	private List<ExamVO> exams;
	private List<MarkVO> marks;

	private String examTypeName;
	private String termName;
	private String levelClassName;
	private String subjectName;
	private Teacher teacher = new Teacher();
	private Double random;
	private String selectedTab = "examList";
	private List<Student> students = new ArrayList<Student>();
	private List<Student> searchStudents = new ArrayList<Student>();
	private Student student = new Student();
	private String year;
	private List<Files> examFiles;
	
	private Mark mark = new Mark();
	
	private Long idExam=null;
	// Search criaterias
	private ExamSearchVO examSearch = new ExamSearchVO();
	private MarkSearchVO markSearch = new MarkSearchVO();
	
	private boolean deleteOldMarks;
	
	public String getYear() {
		return year;
	}

	public void setYear(String year) {
		this.year = year;
	}

	public Student getStudent() {
		return student;
	}

	public void setStudent(Student student) {
		this.student = student;
	}

	public List<Student> getStudents() {
		return students;
	}

	public void setStudents(List<Student> students) {
		this.students = students;
	}

	@Override
	public String getSelectedTab() {
		return selectedTab;
	}

	@Override
	public void setSelectedTab(String selectedTab) {
		this.selectedTab = selectedTab;
	}

	@Override
	public Double getRandom() {
		return random;
	}

	@Override
	public void setRandom(Double random) {
		this.random = random;
	}

	@Override
	public void paint(OutputStream stream, Object object) throws IOException {
		if (teacher != null && teacher.getImage() != null) {
			stream.write(teacher.getImage());
		} else {
			stream.write(new byte[] {});
		}
	}

	public void paintStudent(OutputStream stream, Object object)
			throws IOException {
		if (student != null && student.getImage() != null) {
			stream.write(student.getImage());
		} else {
			stream.write(new byte[] {});
		}
	}

	public ExamService getExamService() {
		return examService;
	}

	public void setExamService(ExamService examService) {
		this.examService = examService;
	}

	public void setTeacher(Teacher teacher) {
		this.teacher = teacher;
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

	public String getExamTypeName() {
		return examTypeName;
	}

	public void setExamTypeName(String examTypeName) {
		this.examTypeName = examTypeName;
	}

	public String getTermName() {
		return termName;
	}

	public void setTermName(String termName) {
		this.termName = termName;
	}

	private Exam exam = new Exam();

	public String validate() {
		return "succes";
	}
	
	public String getSearchedNotes() {
			
		if (examSearch.containsNotEmptyField()) {
			exams = examService.getExamNotes(Exam.class, examSearch, getCurrentUser());
		}
		rowCount = new Long((exams != null) ? exams.size() : 0);
		return "Success";
	}
	
	public String getSearchedMarks() {
		
		if (markSearch.containsNotEmptyField()) {
			marks = examService.getMarkVOs(Mark.class, markSearch, getCurrentUser());
		}
		rowCount = new Long((marks != null) ? marks.size() : 0);
		return "Success";
	}

	@Override
	public String clear() {
		exam = new Exam();
		examTypeName = "";
		termName = "";
		levelClassName = "";
		subjectName = "";
		teacher = null;
		return "Success";
	}

	public Teacher getTeacher() {
		return teacher;
	}

	public String fetchTeacher() {
		teacher = null;
		random = Math.random();
		if (levelClassName != null && subjectName != null) {
			List<Teacher> teachers = examService.getTeacher(levelClassName,
					subjectName);
			for (Teacher t : teachers) {
				teacher = t;
			}
		}
		return "Success";
	}
	
	public void beforeDelete(){
		idExam=getIdParameter();
	}

	public void noDelete(){
		idExam=null;
	}
	public String delete() {
		clearMessages();
		try {			 		
			examService.delete(idExam, Exam.class);
			for(ExamVO e:exams){
				if(e.getId().equals(idExam)){
					exams.remove(e);
					break;
				}
			}
			idExam=null;
			clear();
			setSuccessMessage(getResourceBundle().getString("DELETE_SUCCESSFULLY"));
		} catch (Exception ex) {
			setErrorMessage(getResourceBundle().getString("DELETE_UNSUCCESSFULL"));
		}

		return "Success";
	}

	public String insert() {
		clearMessages();
		Long id = exam.getId();
		try {
			ExamType examType = (ExamType) examService.findByName(
					ExamType.class, examTypeName,
					examService.getDefaultSchool());
			Course course = examService.getCourse(levelClassName, subjectName);

			if (course == null) {
				setErrorMessage(getResourceBundle().getString("COURSE_CLASS_MISMATCH"));
				return "Error";

			}

			Term term = (Term) examService.findByName(Term.class, termName,
					examService.getDefaultSchool());
			SchoolYear schoolYear = (SchoolYear) examService.findByColumn(
					SchoolYear.class, "year", year,
					examService.getDefaultSchool());
			Double cumRatio = examService.getCummulatedRatio(schoolYear.getId(), term.getId(), course.getId(),exam.getId()==null?0:exam.getId());
			if((cumRatio==null?0:cumRatio)+(exam.getRatio()==null?0:exam.getRatio())>100.0){
				setErrorMessage(getResourceBundle().getString("MAX_RATIO_EXCEEDED")+" : "+((cumRatio==null?0:cumRatio)+(exam.getRatio()==null?0:exam.getRatio()))+"% > 100%");
				return "Error";
			}
			exam.setSchoolYear(schoolYear);
			exam.setCourse(course);
			exam.setExamType(examType);
			exam.setTerm(term);
			if(exam.getMaxMark()==null ||exam.getMaxMark()==0){
				exam.setMaxMark(course.getMaxMark());
			}
			if (id == null || id == 0){
				examService.save(exam, getCurrentUser());
			}else{
				examService.update(exam, getCurrentUser());
			}
			setSuccessMessage(getResourceBundle().getString("SAVED_SUCCESSFULLY"));
			
			students = examService.getStudents(exam);
			for(Student st:students){
				if(st.getMark()!=null)
					st.setMark(denormalize(st.getMark(), exam.getMaxMark(), exam.getCourse()));
			}
			//getAll();
		} catch (Exception ex) {
			ex.printStackTrace();
			exam.setId(id);
			setErrorMessage(ex, "Ce exam existe deja");
		}

		return "Success";
	}

	public String edit() {
		clearMessages();
		exam = (Exam) examService.getById(Exam.class, getIdParameter());
		examTypeName = exam.getExamType().getName();
		termName = exam.getTerm().getName();
		levelClassName = exam.getCourse().getLevelClass().getName();
		subjectName = exam.getCourse().getSubject().getName();
		teacher = exam.getCourse().getTeacher();
		year = exam.getSchoolYear().getYear();
		selectedTab = "examDetail";
		if (exam != null){
			students = examService.getStudents(exam);
			for(Student st:students){
				if(st.getMark()!=null)
					st.setMark(denormalize(st.getMark(), exam.getMaxMark(), exam.getCourse()));
			}
		}
		return "Success";
	}

	public String editForCSV() {
		exam = (Exam) examService.getById(Exam.class, getIdParameter());
		if (exam != null){
			students = examService.getStudents(exam);
			for(Student st:students){
				if(st.getMark()!=null)
					st.setMark(denormalize(st.getMark(), exam.getMaxMark(), exam.getCourse()));
			}
		}
		return "Success";
	}
	
	public String loadMark() {
		mark = (Mark) examService.getById(Mark.class, getIdParameter());
		return "Success";
	}
	
	public String copyExam() {
		clearMessages();
		exam = (Exam) examService.getById(Exam.class, exam.getId());
		exam.setId(null);
		examService.save(exam, getCurrentUser());
		setSuccessMessage(getResourceBundle().getString("SAVED_SUCCESSFULLY"));
		
		students = examService.getStudents(exam);
		for(Student st:students){
			if(st.getMark()!=null)
				st.setMark(denormalize(st.getMark(), exam.getMaxMark(), exam.getCourse()));
		}
		//getAll();
		return "Success";
	}
	
	public String importExamMark() {

		try {
			Test test = (Test) examService
					.getById(Test.class, getIdParameter());
			List<BaseEntity> testResults = examService.loadAllByParentId(
					UserTest.class, "test", "id", test.getId());

			if (testResults != null) {

				for (BaseEntity be : testResults) {
					UserTest uTest = (UserTest) be;

					User user = uTest.getUser();
					Student stud = examService.getStudent(user);
					if (stud != null) {

						Mark mark = examService.getMark(exam, examService.getStudentEnrollment(stud.getId(),
								exam.getCourse().getLevelClass().getId(), exam.getSchoolYear().getId()));
						if (mark == null) {
							mark = new Mark();
							mark.setExam(exam);
							mark.setMark(uTest.getScore().doubleValue());
							normalize(mark, exam.getMaxMark(), exam.getCourse());
							Grade grade = examService.findGrade(
									getCurrentUser().getSchool(),
									mark.getMark(), exam.getCourse()
											.getMaxMark().intValue());
							mark.setGrade(grade);
							mark.setStudentEnrollment(stud
									.getCurrentEnrollment());
							mark.setApprovedBy(null);
							
							examService.save(mark, getCurrentUser());
						} else {
							mark.setExam(exam);
							mark.setMark(uTest.getScore().doubleValue());
							normalize(mark, exam.getMaxMark(), exam.getCourse());
							Grade grade = examService.findGrade(
									getCurrentUser().getSchool(),
									mark.getMark(), exam.getCourse()
											.getMaxMark().intValue());
							mark.setGrade(grade);
							mark.setApprovedBy(null);
							examService.update(mark, getCurrentUser());
						}
					}
					students = examService.getStudents(exam);
				}
			}
			setSuccessMessage(getResourceBundle().getString("SAVED_SUCCESSFULLY"));
		} catch (Exception ex) {
			ex.printStackTrace();
			setErrorMessage(ex, getResourceBundle().getString("SAVED_UNSUCCESSFULL"));
			return "Error";
		}
		return "success";
	}

	private void normalize(Mark mark, Double examMaxMark, Course course) {
		// TODO Auto-generated method stub
		mark.setMark(mark.getMark()*course.getMaxMark()/examMaxMark);
	}
	
	private Double denormalize(Double mark, Double examMaxMark, Course course) {
		// TODO Auto-generated method stub
		return mark*examMaxMark/course.getMaxMark();
	}

	private void getAll() {
		SchoolYear sy = examService.getSchoolYear(new Date(),examService.getDefaultSchool());
		year=sy==null?year:sy.getYear();
		exams = examService.getAllExamVOs(getCurrentUser().getSchool());
		setRowCount(new Long(exams.size()));

	}

	public BaseService getBaseService() {
		return examService;
	}

	public void setBaseService(ExamService examService) {
		this.examService = examService;
	}

	public Long getRowCount() {
		return rowCount;
	}

	public void setRowCount(Long rowCount) {
		this.rowCount = rowCount;
	}



	public List<ExamVO> getExams() {
		return exams;
	}

	public void setExams(List<ExamVO> exams) {
		this.exams = exams;
	}

	public Exam getExam() {
		return exam;
	}

	public void setExam(Exam exam) {
		this.exam = exam;
	}

/*	public void setAllExams(List<String> allExams) {
		this.allExams = allExams;
	}
*/
	public String getStudentAndMark() {
		student = getCurrentStudent(getIdParameter());
		return "Success";
	}

	public String getStudentsAndNotes() {
		// exam = (Exam) examService.getById(Exam.class, getIdParameter());
		if (exam != null)
			students = examService.getStudents(exam);

		return "Success";
	}

	public String approveMark() {
		clearMessages();
		try {
			student = getCurrentStudent(getIdParameter());
			Mark mark = examService.getMark(exam, examService.getStudentEnrollment(student.getId(),
					exam.getCourse().getLevelClass().getId(), exam.getSchoolYear().getId()));
			if (mark == null) {
				// do nothing!!!!
			} else {
				mark.setApprovedBy(getCurrentUserId());
				examService.update(mark, getCurrentUser());
				students = examService.getStudents(exam);
			}

			setSuccessMessage(getResourceBundle().getString("APPROVED_SUCCESSFULLY"));
		} catch (Exception e) {
			e.printStackTrace();
			setErrorMessage(e.getMessage());
		}
		return "Success";
	}

	public String approveThisMark() {
		clearMessages();
		try {
			mark = (Mark) examService.getById(Mark.class, getIdParameter());
			mark.setApprovedBy(getCurrentUserId());
			examService.update(mark, getCurrentUser());
			setSuccessMessage(getResourceBundle().getString("APPROVED_SUCCESSFULLY"));
		} catch (Exception e) {
			e.printStackTrace();
			setErrorMessage(e.getMessage());
		}
		getSearchedMarks();
		return "Success";
	}
	
	
	public String approveAllMarks() {
		clearMessages();
		try {
			for (Student st : students) {
				Mark mark = examService.getMark(exam, examService.getStudentEnrollment(st.getId(),
						exam.getCourse().getLevelClass().getId(), exam.getSchoolYear().getId()));
				if (mark == null) {
					// do nothing!!!!
				} else {
					mark.setApprovedBy(getCurrentUserId());
					examService.update(mark, getCurrentUser());
					students = examService.getStudents(exam);
				}
			}
			setSuccessMessage(getResourceBundle().getString("APPROVED_SUCCESSFULLY"));
		} catch (Exception e) {
			e.printStackTrace();
			setErrorMessage(e.getMessage());
		}
		return "Success";
	}
	
	public String recalculateAllMarks() {
		clearMessages();
		try {
			examService.recalculateAllNoteForExam(exam);
			students = examService.getStudents(exam);
			for(Student st:students){
				if(st.getMark()!=null)
					st.setMark(denormalize(st.getMark(), exam.getMaxMark(), exam.getCourse()));
			}
			setSuccessMessage(getResourceBundle().getString("ALL_NOTES_RECALCULATED"));
		} catch (Exception e) {
			e.printStackTrace();
			setErrorMessage(e.getMessage());
		}
		return "Success";
	}

	public String saveNote() {
		Mark mark = examService.getMark(exam, examService.getStudentEnrollment(student.getId(),
				exam.getCourse().getLevelClass().getId(), exam.getSchoolYear().getId()));
		if (mark == null) {
			mark = new Mark();
			mark.setExam(exam);
			mark.setMark(student.getMark());
			normalize(mark, exam.getMaxMark(), exam.getCourse());
			Grade grade = examService
					.findGrade(getCurrentUser().getSchool(), mark.getMark(),
							exam.getCourse().getMaxMark().intValue());
			if (grade != null)
				student.setGrade(grade.getName());
			mark.setGrade(grade);
			mark.setStudentEnrollment(examService.getStudentEnrollment(student.getId(),
					exam.getCourse().getLevelClass().getId(), exam.getSchoolYear().getId()));
			mark.setApprovedBy(null);
			examService.save(mark, getCurrentUser());
		} else {
			mark.setExam(exam);
			mark.setMark(student.getMark());
			normalize(mark, exam.getMaxMark(), exam.getCourse());
			Grade grade = examService
					.findGrade(getCurrentUser().getSchool(), mark.getMark(),
							exam.getCourse().getMaxMark().intValue());
			if (grade != null)
				student.setGrade(grade.getName());
			mark.setGrade(grade);
			mark.setApprovedBy(null);
			examService.update(mark, getCurrentUser());
		}
		students = examService.getStudents(exam);
		for(Student st:students){
			if(st.getMark()!=null)
				st.setMark(denormalize(st.getMark(), exam.getMaxMark(), exam.getCourse()));
		}
		return "Success";
	}

	
	
	public String saveMark() {
		normalize(mark, mark.getExam().getMaxMark(), mark.getExam().getCourse());
		Grade grade = examService
				.findGrade(getCurrentUser().getSchool(), mark.getMark(),
						mark.getExam().getCourse().getMaxMark().intValue());
		mark.setGrade(grade);
		mark.setApprovedBy(null);
		examService.update(mark, getCurrentUser());
		
		getSearchedMarks();
		
		return "Success";
	}
	
	
	
	public String initializeMarkImport() {
		deleteOldMarks = false;
		return "";
	}
	
	
	public void pieceslistener(UploadEvent event) throws Exception {
		File tempFile=null;
		try{
		byte[] content = event.getUploadItem().getData();
		ServletContext ctx = (ServletContext) getContext() .getExternalContext().getContext(); 
		File tempDir = new File(ctx.getRealPath("") + "/temp");   
		tempFile = File.createTempFile("examNote", ".xlsx", tempDir);   
		    
		setErrorMessage("");
		OutputStream out = new FileOutputStream(tempFile);
		out.write(content);
		out.close();
		
		FileInputStream file = new FileInputStream(tempFile);
         
		//Get the workbook instance for XLS file
		XSSFWorkbook workbook = new XSSFWorkbook(file);
		 		
		Date examDate = null;
    	String description = null;
    	String examTypeValue = null;
    	String examTermValue = null;
    	String schoolYearValue = null;
    	String subjectValue = null;
    	String singleLineSubjectValue = null;
    	String classValue = null;
    	double ratioValue = 0;
    	String maxMarkValue = "";
    	double maxMarkValueDouble = 0;
    	String evaluationTypeValue = "";
    	String publishValue = "";
    	ExamType examType = null;
    	Term term = null;
    	SchoolYear schoolYear = null;
    	Course course = null;
    	LevelClass levelClass = null;
    	Subject subject = null;
    	
    	Map<String, Exam> examMap = new HashMap<String, Exam>();
    	Map<Integer, String> subjectMap = new HashMap<Integer, String>();
    	Map<String, Student> studentMap = new HashMap<String, Student>();
    	
    	int numberOfWorkSheets =  workbook.getNumberOfSheets();
    	
    	for (int index = 0; index < numberOfWorkSheets - 1; index++) {
    		
    		//Get first sheet from the workbook
    		XSSFSheet sheet = workbook.getSheetAt(index);

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
			            if (i == 2) {
			            	examDate = cell.getDateCellValue();
			            	if (examDate.compareTo(new Date()) > 0) {
			            		setErrorMessage(getErrorMessage() + " La Date de l'examen ne peut pas etre superieure a la date d'aujourd'hui. ");
			            	}
			            	break;
			            }
			            i++;
			        }
		        }
		        else if (row.getRowNum() == 1) {
		        	//For each row, iterate through each columns
			        Iterator<Cell> cellIterator = row.cellIterator();
			        int i = 1;
			        while(cellIterator.hasNext()) {
			            Cell cell = cellIterator.next();
			            if (i == 2) {
			            	description = cell.getStringCellValue(); 
			            	break;
			            }
			            i++;
			        }
		        }
		        else if (row.getRowNum() == 2) {
		        	//For each row, iterate through each columns
			        Iterator<Cell> cellIterator = row.cellIterator();
			        int i = 1;
			        while(cellIterator.hasNext()) {
			            Cell cell = cellIterator.next();
			            if (i == 2) {
			            	examTypeValue = cell.getStringCellValue();
			            	examType = (ExamType) examService.findByName(ExamType.class, examTypeValue, getCurrentUser().getSchool());
			            	if (examType == null) {
			            		setErrorMessage(getErrorMessage() + " La Date de l'examen ne peut pas etre superieure a la date d'aujourd'hui. ");
			            	}
			            	break;
			            }
			            i++;
			        }
		        }
		        else if (row.getRowNum() == 3) {
		        	//For each row, iterate through each columns
			        Iterator<Cell> cellIterator = row.cellIterator();
			        int i = 1;
			        while(cellIterator.hasNext()) {
			            Cell cell = cellIterator.next();
			            if (i == 2) {
				            examTermValue = cell.getStringCellValue();
			            	term = (Term) examService.findByName(Term.class, examTermValue, getCurrentUser().getSchool());
			            	if (term == null) {
			            		setErrorMessage(getErrorMessage() + " Le Mois/Trimestre n'est pas valide. ");
			            	}
			            	break;
			            }
			            i++;
			        }
		        }
		        else if (row.getRowNum() == 4) {
		        	//For each row, iterate through each columns
			        Iterator<Cell> cellIterator = row.cellIterator();
			        int i = 1;
			        while(cellIterator.hasNext()) {
			            Cell cell = cellIterator.next();
			            if (i == 2) {
				            schoolYearValue = cell.getStringCellValue();
			            	schoolYear = (SchoolYear) examService.findByColumn(
			    					SchoolYear.class, "year", schoolYearValue,
			    					examService.getDefaultSchool());
			            	if (schoolYear == null) {
			            		setErrorMessage(getErrorMessage() + " L'annee scolaire n'est pas valide. ");
			            	}
			            	break;
			            }
			            i++;
			        }
		        }
		        else if (row.getRowNum() == 5) {
		        	//For each row, iterate through each columns
			        Iterator<Cell> cellIterator = row.cellIterator();
			        int i = 1;
			        while(cellIterator.hasNext()) {
			            Cell cell = cellIterator.next();
			            if (i == 2) {
				            classValue = cell.getStringCellValue();
			            	levelClass = (LevelClass) examService.findByName(LevelClass.class, classValue, getCurrentUser().getSchool());
			            	if (levelClass == null) {
			            		setErrorMessage(getErrorMessage() + " Cette classe " + classValue + " n'est pas valide. ");
			            	}
			            	break;
			            }
			            i++;
			        }
		        }
		        else if (row.getRowNum() == 6) {
		        	//For each row, iterate through each columns
			        Iterator<Cell> cellIterator = row.cellIterator();
			        int i = 1;
			        while(cellIterator.hasNext()) {
			            Cell cell = cellIterator.next();
			            if (i == 2) {
				            ratioValue = cell.getNumericCellValue();
			            	if (ratioValue <= 0) {
			            		setErrorMessage(getErrorMessage() + " Cette note sur: " + ratioValue + " n'est pas valide. ");
			            	}
			            	break;
			            }
			            i++;
			        }
		        }
		        else if (row.getRowNum() == 7) {
		        	//For each row, iterate through each columns
			        Iterator<Cell> cellIterator = row.cellIterator();
			        int i = 1;
			        while(cellIterator.hasNext()) {
			            Cell cell = cellIterator.next();
			            if (i == 2) {
				            evaluationTypeValue = cell.getStringCellValue();
			            	if (!"Note d'Examen".equals(evaluationTypeValue) && !"Note de Classe".equals(evaluationTypeValue)) {
			            		setErrorMessage(getErrorMessage() + " Ce compte pour: " + evaluationTypeValue + " n'est pas valide. ");
			            	}
			            	break;
			            }
			            i++;
			        }
		        }
		        else if (row.getRowNum() == 8) {
		        	//For each row, iterate through each columns
			        Iterator<Cell> cellIterator = row.cellIterator();
			        int i = 1;
			        while(cellIterator.hasNext()) {
			            Cell cell = cellIterator.next();
			            if (i == 2) {
				            publishValue = cell.getStringCellValue();
			            	if (!"Oui".equals(publishValue) && !"Non".equals(publishValue)) {
			            		setErrorMessage(getErrorMessage() + " Cette valeur pour Publier? : " + publishValue + " n'est pas valide. ");
			            	}
			            	break;
			            }
			            i++;
			        }
		        }
	
		        if (row.getRowNum() == 8 &&  StringUtils.isNotBlank(getErrorMessage()))
		        	break;
		        
		        if (row.getRowNum() == 9) {
		        	Iterator<Cell> cellIterator = row.cellIterator();
			        int i = 1;
			        while(cellIterator.hasNext()) {
			            Cell cell = cellIterator.next();
			            if (i > 3) {
				            
				            subjectValue = cell.getStringCellValue();
				            subjectValue =  subjectValue.toUpperCase();
				            if (StringUtils.isNotEmpty(subjectValue)) {
					            subjectMap.put(i, subjectValue);
				            	course = examService.getCourse(classValue, subjectValue);
				            	if (course == null) {
				            		setErrorMessage(getErrorMessage() + " Cette combination classe/matiere " + classValue + "/" + subjectValue + " n'est pas valide. ");
				            	}
				            	else if (course != null) {
							        exam = examService.getExam(examType, term, schoolYear, course, levelClass, getCurrentUser());
							        if (exam != null && exam.getExamDate().compareTo(examDate) == 0 && isDeleteOldMarks()) {
							        	examService.deleteMarksByExam(exam);
							        }
							        
							        if (exam == null || exam.getExamDate().compareTo(examDate) != 0) {
							        	exam = new Exam();
							        	exam.setExamDate(examDate);
							        	exam.setExamType(examType);
							        	exam.setTerm(term);
							        	exam.setSchoolYear(schoolYear);
							        	exam.setCourse(course);
							        	exam.setEvaluationType("Note d'Examen".equals(evaluationTypeValue) ? new Short("1") : 0);
							        	exam.setMaxMark(maxMarkValueDouble <= 0 ? course.getMaxMark() : maxMarkValueDouble);
							        	exam.setRatio(new Double(ratioValue).intValue());
							        	exam.setPublishMarks("Oui".equals(publishValue));
							        	exam.setName(description);
							        	
							        	examService.save(exam, getCurrentUser());
							        }
							        examMap.put(subjectValue, exam);
						        }
			            	}
			            }
			            i++;
			        }
		        }     
			        
		        else if (row.getRowNum() > 9) {
			        String studentMatricule = "";
			        String lastName = "";
			        String firstName = "";
			        double examScore = 0;
			        
			        //For each row, iterate through each columns
			        Iterator<Cell> cellIterator = row.cellIterator();
			        int i = 1;
			        while(cellIterator.hasNext()) {
			            Cell cell = cellIterator.next();
			            if (i == 1) {
			            	try{
			            		studentMatricule = cell.getStringCellValue();
			            	}catch(Exception e){
			            		setErrorMessage(getErrorMessage() + "Matricule invalide: " + cell.getNumericCellValue() + ". Le matricule doit etre un text\n");	            		
			            		e.printStackTrace();
			            		i++;
			            		continue;
			            	}
			            }
			            if (i == 2) {
			            	try{
			            		lastName = cell.getStringCellValue();
			            	}catch(Exception e){
			            		setErrorMessage(getErrorMessage() + "Nom invalide: " + cell.getNumericCellValue() + ". Le nom doit etre un text\n");	            		
			            		e.printStackTrace();
			            		i++;
			            		continue;
			            	}
			            }
			            if (i == 3) {
			            	try{
			            		firstName = cell.getStringCellValue();
			            	}catch(Exception e){
			            		setErrorMessage(getErrorMessage() + "Prenom invalide: " + cell.getNumericCellValue() + ". Le prenom doit etre un text\n");	            		
			            		e.printStackTrace();
			            		i++;
			            		continue;
			            	}
			            }
			            else if (i >= 4) {
			            	try {
			            		
			            		examScore  = cell.getNumericCellValue();
			            	}
			            	catch (Exception ex) {
			            		try{
			            		examScore  = Double.valueOf(cell.getStringCellValue());
			            		}catch(Exception e){
			            			setErrorMessage(getErrorMessage() + getResourceBundle().getString("INVALID_NOTE") + " " + cell.getStringCellValue() + " pour " + studentMatricule + "\n");
			            		}
			            	}
			            	
			            	exam = examMap.get(subjectMap.get(i));
			            
		            		
		            		if (exam != null) {
			            		if (examScore > exam.getCourse().getMaxMark()) {
			            			setErrorMessage(getErrorMessage() + " La note " + examScore + " ne peut etre plus grande que la note maximale du cours: " + exam.getCourse().getMaxMark() + "\n");
			            		}
	
		    			        Student stud = null;
		    			        if (StringUtils.isNotBlank(studentMatricule)) {
		    			        	stud = studentMap.get(studentMatricule);
		    			        	if (stud == null) {
		    			        		stud = (Student) examService.findByColumn(Student.class, "matricule", studentMatricule);
		    			        		studentMap.put(studentMatricule, stud);
		    			        	}
		    			        	
		    			        	if (stud == null)
		    			        		setErrorMessage(getErrorMessage() + getResourceBundle().getString("INVALID_MATRICULE") + " " + studentMatricule + "\n");
		    			        	
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
			    			        	List<BaseEntity> studs = examService.findByColumns(Student.class, columnNames, columnValues);
			    			        	for (BaseEntity st : studs) {
			    			        		Student stu = (Student)st;
			    			        		if (stu.getCurrentEnrollment().getSchoolYear().getYear().equals(schoolYear.getYear())) {
			    			        			stud = stu;
			    			        			studentMap.put(firstName + lastName, stud);
			    			        			break;
			    			        		}
			    			        	}
		    			        	}
		    			        	
		    			        	if (stud == null)
		    			        		setErrorMessage(getErrorMessage() + getResourceBundle().getString("INVALID_FIRST_AND_LAST_NAMES") + ": " + lastName + " " + firstName + "\n");
		    			        }
		    			        
		    			       if (stud != null) {
		    			        	StudentEnrollment ste=examService.getStudentEnrollment(stud.getId(),
		    								exam.getCourse().getLevelClass().getId(), exam.getSchoolYear().getId());
		    			        	if(ste==null){
		    			        		setErrorMessage(getErrorMessage() + " Cet Eleve n'est pas inscrit a cette classe: Matricule:" + studentMatricule + "\n");
		    			        	}else{
		    				            Mark mark = examService.getMark(exam, ste);
		    							if (mark == null) {
		    								mark = new Mark();
		    								mark.setExam(exam);
		    								mark.setMark(new Double(examScore));
		    								normalize(mark, exam.getMaxMark(), exam.getCourse());
		    								Grade grade = examService.findGrade(
		    										getCurrentUser().getSchool(),
		    										mark.getMark(), exam.getCourse()
		    												.getMaxMark().intValue());
		    								mark.setGrade(grade);
		    								mark.setStudentEnrollment(ste);
		    								mark.setApprovedBy(null);
		    								
		    								examService.save(mark, getCurrentUser());
		    							} else {
		    								mark.setExam(exam);
		    								mark.setMark(new Double(examScore));
		    								normalize(mark, exam.getMaxMark(), exam.getCourse());
		    								Grade grade = examService.findGrade(
		    										getCurrentUser().getSchool(),
		    										mark.getMark(), exam.getCourse()
		    												.getMaxMark().intValue());
		    								mark.setGrade(grade);
		    								mark.setApprovedBy(null);
		    								examService.update(mark, getCurrentUser());
		    							}
		    			        	}
		    			       }
		    		        }
			            }
			    
						i++;
			        }
		        }
		    }	    
    	}
    	
	    file.close();
	    //getAll();

	    if (!tempFile.delete()) {
	    	setErrorMessage("Fichier " + tempFile.getName() + " n'est pas ete supprime. ");
	    }
		}catch(Exception e){
			e.printStackTrace();
			setErrorMessage("Une erreur est survenue lors de l'importation des notes " +e.getMessage());
		}
	}

	
	public void generateCSVReport ()
	{
		try
		{
			editForCSV();
			
			FacesContext context = getContext();
			HttpServletResponse response = (HttpServletResponse) context
					.getExternalContext().getResponse();
	
			ServletOutputStream ouputStream = response.getOutputStream();
			
			HttpServletRequest request = (HttpServletRequest) context
					.getExternalContext().getRequest();
			
			Locale locale = request.getLocale();
			DateFormat df = DateFormat.getDateInstance(DateFormat.LONG, locale);
			
			String delimiter = getDelimiter(locale);
			
			StringBuffer totalString = new StringBuffer();
			StringBuffer header = new StringBuffer();
			StringBuffer title = new StringBuffer();
			
			ExamVO examVO = new ExamVO();
			
			for (ExamVO ex : exams) {
				if (ex.getId().equals(getIdParameter())) {
					examVO = ex;
					break;
				}
			}
			
			title.append(printString("Examen") + delimiter);
			title.append(printString(examVO.getName()) + delimiter);
			title.append(printString("Mois") + delimiter);
			title.append(printString(examVO.getTerm()) + "\n");
			title.append(printString("Annee") + delimiter);
			title.append(printString(examVO.getSchoolYear()) + delimiter);
			title.append(printString("Type d'Examen") + delimiter);
			title.append(printString(examVO.getExamType()) + "\n");
			title.append(printString("Matiere") + delimiter);
			title.append(printString(examVO.getCourse()) + delimiter);
			title.append(printString("Niveau") + delimiter);
			title.append(printString(examVO.getClassName()) + "\n");
			title.append(printString("Date d'Examen") + delimiter);
			title.append(printString(examVO.getExamDate(), df) + "\n");
			
			header.append(getResourceBundle().getString("matricule") + delimiter);
			header.append(getResourceBundle().getString("lastName") + delimiter);
			header.append(getResourceBundle().getString("firstName") + delimiter);
			header.append(getResourceBundle().getString("middleName") + delimiter);
			header.append(getResourceBundle().getString("mark") + delimiter);
			header.append(getResourceBundle().getString("maxMarkForEntry") + delimiter);
			header.append(getResourceBundle().getString("grade"));

			StringBuffer body=new StringBuffer();
			for (Student student : students) {
				body.append(printString(student.getMatricule()));
				body.append(delimiter);
				body.append(printString(student.getLastName()));
				body.append(delimiter);
				body.append(printString(student.getFirstName()));
				body.append(delimiter);
				body.append(printString(student.getMiddleName()));
				body.append(delimiter);
				body.append(printString(student.getMark() == null ? null : student.getMark().toString()));
				body.append(delimiter);
				body.append(printString(exam.getMaxMark() == null ? null : exam.getMaxMark().toString()));
				body.append(delimiter);
				body.append(printString(student.getGrade() == null ? null : student.getGrade().toString()));
				body.append("\n");
			}

			totalString.append(title.toString());
			totalString.append(header.toString());
			totalString.append("\n");
			totalString.append(body.toString());
			response.addHeader("Content-disposition",
					"attachment;filename=notes" + "-"+getStringDate()+".csv");
			response.setContentType("application/csv");
			ouputStream.write(totalString.toString().getBytes());
			ouputStream.flush();
			ouputStream.close();
			context.responseComplete();
	}
		catch (Exception exception)
		{
			exception.printStackTrace() ;
		}
	}
	
	private Student getCurrentStudent(Long id) {
		random = Math.random();
		for (Student st : students)
			if (st.getId().equals(id))
				return st;
		return null;
	}

	public boolean isUserHasWriteAccess() {
		return isUserHasWriteAccess(MenuIdEnum.EXAM.getValue());
	}

	public List<Files> getExamFiles() {
		return examFiles;
	}

	public void setExamFiles(List<Files> examFiles) {
		this.examFiles = examFiles;
	}

	public boolean isDeleteOldMarks() {
		return deleteOldMarks;
	}

	public void setDeleteOldMarks(boolean deleteOldMarks) {
		this.deleteOldMarks = deleteOldMarks;
	}

	public ExamSearchVO getExamSearch() {
		return examSearch;
	}

	public void setExamSearch(ExamSearchVO examSearch) {
		this.examSearch = examSearch;
	}

	public List<Student> getSearchStudents() {
		return searchStudents;
	}

	public void setSearchStudents(List<Student> searchStudents) {
		this.searchStudents = searchStudents;
	}

	public List<MarkVO> getMarks() {
		return marks;
	}

	public void setMarks(List<MarkVO> marks) {
		this.marks = marks;
	}

	public MarkSearchVO getMarkSearch() {
		return markSearch;
	}

	public void setMarkSearch(MarkSearchVO markSearch) {
		this.markSearch = markSearch;
	}

	public Mark getMark() {
		return mark;
	}

	public void setMark(Mark mark) {
		this.mark = mark;
	}
	
}
