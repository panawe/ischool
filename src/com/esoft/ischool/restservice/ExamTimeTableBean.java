package com.esoft.ischool.restservice;

import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.annotation.PostConstruct;

import org.apache.commons.beanutils.BeanComparator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.esoft.ischool.job.Config;
import com.esoft.ischool.model.BaseEntity;
import com.esoft.ischool.model.Course;
import com.esoft.ischool.model.ExamDay;
import com.esoft.ischool.model.ExamTimeTable;
import com.esoft.ischool.model.SchoolYear;
import com.esoft.ischool.model.LevelClass;
import com.esoft.ischool.model.Student;
import com.esoft.ischool.model.Subject;
import com.esoft.ischool.model.Term;
import com.esoft.ischool.model.TimeTable;
import com.esoft.ischool.service.BaseService;
import com.esoft.ischool.util.Constants;
import com.esoft.ischool.util.MenuIdEnum;

@Component("examTimeTableBean")
@Scope("session")
public class ExamTimeTableBean extends BaseBean {

	@Autowired
	@Qualifier("baseService")
	private BaseService baseService;

	@Autowired
	@Qualifier("config")
	private Config config;

	private Long rowCount;
	private Long searchRowCount;
	private Long timeTableCount;
	private List<BaseEntity> timeTables;
	private List<Date> examDays;
	private String levelClassName;
	private String schoolYearName;
	private String termName;
	private String subjectName;
	private ExamTimeTable timeTable = new ExamTimeTable();

	private String selectedTab = "timerTableList";
	private Date firstDay;
	private Date secondDay;
	private Date thirdDay;
	private Date fourthDay;
	private Date fifthDay;
	private Date sixthDay;
	private Date seventhDay;

	private String firstDayDescription;
	private String secondDayDescription;
	private String thirdDayDescription;
	private String fourthDayDescription;
	private String fifthDayDescription;
	private String sixthDayDescription;
	private String seventhDayDescription;

	private List<BaseEntity> searchTimeTables;
	private List<ExamTimeTable> firstDayTimeTables;
	private List<ExamTimeTable> secondDayTimeTables;
	private List<ExamTimeTable> thirdDayTimeTables;
	private List<ExamTimeTable> fourthDayTimeTables;
	private List<ExamTimeTable> fifthDayTimeTables;
	private List<ExamTimeTable> sixthDayTimeTables;
	private List<ExamTimeTable> seventhDayTimeTables;

	private String searchLevelClassName;
	private String searchSubjectName;
	private String searchYearName;
	private String searchTermName;

	public String getLevelClassName() {
		return levelClassName;
	}

	public void setLevelClassName(String levelClassName) {
		this.levelClassName = levelClassName;
	}

	public List<String> autoComplete(Object suggest) {
		return null;
	}

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

	public String searchTimeTable() {
		LevelClass levelClass = null;
		SchoolYear schoolYear = null;
		Term term = null;
		
		Map <Date, List<ExamTimeTable>> examTimeTableMap = new LinkedHashMap<Date, List<ExamTimeTable>>();
		
		searchTimeTables = new ArrayList<BaseEntity>();
		if ("student".equals(getSessionParameter("link"))
				&& getSessionParameter("currentStudentId") != null) {
			Student student = (Student) baseService
					.getById(Student.class,
							new Long(getSessionParameter("currentStudentId")
									.toString()));
			if (student.getCurrentEnrollment() != null) {
				levelClass = student.getCurrentEnrollment().getLevelClass();
				schoolYear = student.getCurrentEnrollment().getSchoolYear();
				term = (Term) baseService.findByName(Term.class,
						searchTermName, getCurrentUser().getSchool());
			}
		} else if ("teacher".equals(getSessionParameter("link"))
				&& getSessionParameter("currentTeacherId") != null) {
			schoolYear = (SchoolYear) baseService.findByColumn(
					SchoolYear.class, "year", searchYearName, getCurrentUser()
							.getSchool());
			term = (Term) baseService.findByName(Term.class, searchTermName,
					getCurrentUser().getSchool());

			Long yearId = schoolYear != null ? schoolYear.getId() : null;
			Long termId = term != null ? term.getId() : null;

			searchTimeTables = baseService
					.getTimeTablesByTeacherId(ExamTimeTable.class,
							new Long(getSessionParameter("currentTeacherId")
									.toString()), yearId, termId);
		} else {
			levelClass = (LevelClass) baseService.findByName(LevelClass.class,
					searchLevelClassName, getCurrentUser().getSchool());
			schoolYear = (SchoolYear) baseService.findByColumn(
					SchoolYear.class, "year", searchYearName, getCurrentUser()
							.getSchool());
			term = (Term) baseService.findByName(Term.class, searchTermName,
					getCurrentUser().getSchool());
		}
		if ((levelClass != null && schoolYear != null)
				|| ("teacher".equals(getSessionParameter("link")))) {
			if (!("teacher".equals(getSessionParameter("link")))) {
				if (term == null) {
					Term aTerm = baseService.getCurrentTermForClass(schoolYear,
							levelClass);

					if (aTerm == null) {
						searchTimeTables = baseService.loadByParentsIds(
								ExamTimeTable.class, "levelClass",
								levelClass.getId(), "schoolYear",
								schoolYear.getId());
					} else {
						searchTimeTables = baseService.loadByParentsIds(
								ExamTimeTable.class, "levelClass",
								levelClass.getId(), "schoolYear",
								schoolYear.getId(), "term", aTerm.getId());
					}
				} else {
					searchTimeTables = baseService.loadByParentsIds(
							ExamTimeTable.class, "levelClass", levelClass.getId(),
							"schoolYear", schoolYear.getId(), "term",
							term.getId());
				}

			}

			 BeanComparator examDateComparator = new BeanComparator("examDate");
			 Collections.sort(searchTimeTables, examDateComparator);
			    
			for (BaseEntity entity : searchTimeTables) {
				ExamTimeTable examTimeTable = (ExamTimeTable) entity;
				List lookedUpTimeTables = examTimeTableMap.get(examTimeTable.getExamDate());
				if (lookedUpTimeTables == null) {
					List<ExamTimeTable> examTimeTables = new ArrayList<ExamTimeTable>();
					examTimeTables.add(examTimeTable);
					examTimeTableMap.put(examTimeTable.getExamDate(), examTimeTables);
				}
				else {
					lookedUpTimeTables.add(examTimeTable);
				}
			}
			
			firstDayTimeTables = new LinkedList<ExamTimeTable>();
			secondDayTimeTables = new LinkedList<ExamTimeTable>();
			thirdDayTimeTables = new LinkedList<ExamTimeTable>();
			fourthDayTimeTables = new LinkedList<ExamTimeTable>();
			fifthDayTimeTables = new LinkedList<ExamTimeTable>();
			sixthDayTimeTables = new LinkedList<ExamTimeTable>();
			seventhDayTimeTables = new LinkedList<ExamTimeTable>();
			
			firstDay = new Date();
			secondDay = null;
			thirdDay = null;
			fourthDay = null;
			fifthDay = null;
			sixthDay = null;
			seventhDay = null;
			
			firstDayDescription = "";
			secondDayDescription = "";
			thirdDayDescription = "";
			fourthDayDescription = "";
			fifthDayDescription = "";
			sixthDayDescription = "";
			seventhDayDescription = "";

			int i = 1;
			Format formatter = new SimpleDateFormat("dd/MM/yyyy");

			for (Entry<Date, List<ExamTimeTable>> entry : examTimeTableMap.entrySet()) { 
				Calendar cal = Calendar.getInstance(); 
				cal.setTime(entry.getKey());
				int dayNumber = cal.get(Calendar.DAY_OF_WEEK);
				if (dayNumber > 1)
					dayNumber --;
				else
					dayNumber = 7;
				
				if (i == 1) {
					firstDay = entry.getKey();
					firstDayDescription = config.getConfigurationByGroupAndKey("DAY_OF_WEEK", dayNumber + "")
											+ "\n" + formatter.format(cal.getTime());
					firstDayTimeTables = entry.getValue();
				}
				else if (i == 2) {
					secondDay = entry.getKey();
					secondDayDescription = config.getConfigurationByGroupAndKey("DAY_OF_WEEK", dayNumber + "")
										+ "\n" + formatter.format(cal.getTime());
					secondDayTimeTables = entry.getValue();
				}
				else if (i == 3) {
					thirdDay = entry.getKey();
					thirdDayDescription = config.getConfigurationByGroupAndKey("DAY_OF_WEEK", dayNumber + "")
											+ "\n" + formatter.format(cal.getTime());
					thirdDayTimeTables = entry.getValue();
				}
				else if (i == 4) {
					fourthDay = entry.getKey();
					fourthDayDescription = config.getConfigurationByGroupAndKey("DAY_OF_WEEK", dayNumber + "")
											+ "\n" + formatter.format(cal.getTime());
					fourthDayTimeTables = entry.getValue();
				}
				else if (i == 5) {
					fifthDay = entry.getKey();
					fifthDayDescription = config.getConfigurationByGroupAndKey("DAY_OF_WEEK", dayNumber + "")
											+ "\n" + formatter.format(cal.getTime());
					fifthDayTimeTables = entry.getValue();
				}
				else if (i == 6) {
					sixthDay = entry.getKey();
					sixthDayDescription = config.getConfigurationByGroupAndKey("DAY_OF_WEEK", dayNumber + "")
											+ "\n" + formatter.format(cal.getTime());
					sixthDayTimeTables = entry.getValue();
				}
				else if (i == 7) {
					seventhDay = entry.getKey();
					seventhDayDescription = config.getConfigurationByGroupAndKey("DAY_OF_WEEK", dayNumber + "")
												+ "\n" + formatter.format(cal.getTime());
					seventhDayTimeTables = entry.getValue();
				}
					
				++i;
			} 
	
			clearMessages();
		} else {
			setErrorMessage(Constants.NOT_A_VALID_YEAR_OR_CLASS);
		}
		return "Success";
	}

	public String edit() {
		clearMessages();
		timeTable = (ExamTimeTable) baseService.getById(ExamTimeTable.class,
				getIdParameter());
		setSchoolYearName(timeTable.getSchoolYear().getYear());
		setSubjectName(timeTable.getCourse().getSubject().getName());
		setLevelClassName(timeTable.getLevelClass().getName());
		setTermName(timeTable.getTerm().getName());

		return "Success";
	}

	@Override
	public String clear() {
		timeTable = new ExamTimeTable();
		schoolYearName = "";
		subjectName = "";
		levelClassName = "";
		termName = "";

		return "Success";
	}

	public String getShowAll() {
		getAll();
		return "Success";
	}

	public String delete() {
		clearMessages();
		try {
			baseService.delete(getIdParameter(), ExamTimeTable.class);
			getAll();
			clear();
			setSuccessMessage(getResourceBundle().getString(
					"DELETE_SUCCESSFULLY"));
		} catch (Exception ex) {
			setErrorMessage(getResourceBundle().getString(
					"DELETE_UNSUCCESSFULL"));
		}

		return "Success";
	}

	public String insert() {
		clearMessages();
		Long id = timeTable.getId();
		try {
			LevelClass levelClass = (LevelClass) baseService.findByName(
					LevelClass.class, levelClassName, getCurrentUser()
							.getSchool());

			SchoolYear schoolYear = (SchoolYear) baseService.findByColumn(
					SchoolYear.class, "year", schoolYearName, getCurrentUser()
							.getSchool());
			Subject subject = (Subject) baseService.findByName(Subject.class,
					subjectName, getCurrentUser().getSchool());
			Term term = (Term) baseService.findByName(Term.class, termName,
					getCurrentUser().getSchool());

			Course course = (Course) baseService.findByParents(Course.class,
					"levelClass", levelClassName, "subject", subjectName);

			if (levelClass != null && schoolYear != null) {
				if (course != null) {
					timeTable.setLevelClass(levelClass);
					timeTable.setCourse(course);
					timeTable.setSchoolYear(schoolYear);
					timeTable.setTerm(term);

					if (id == null || id == 0)
						baseService.save(timeTable, getCurrentUser());
					else
						baseService.update(timeTable, getCurrentUser());

					setSuccessMessage(getResourceBundle().getString(
							"SAVED_SUCCESSFULLY"));
					clear();
					getAll();
				} else {
					setErrorMessage(Constants.SUBJECT_NOT_TEACHED_IN_CLASS);
				}
			} else {
				setErrorMessage(Constants.NOT_A_VALID_YEAR_OR_CLASS);
			}
		} catch (Exception ex) {
			ex.printStackTrace();
			timeTable.setId(id);
			setErrorMessage(ex, "Cet emploi du temps exist deja. ");
		}

		return "S";
	}

	@PostConstruct
	private void getAll() {
		timeTables = baseService.loadAll(ExamTimeTable.class, getCurrentUser()
				.getSchool());

		setRowCount(new Long(timeTables.size()));
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

	public boolean isUserHasWriteAccess() {
		if (((String) getSessionParameter("link")).equals("student")) {
			return isUserHasWriteAccess(MenuIdEnum.STUDENT.getValue());
		} else if (((String) getSessionParameter("link")).equals("teacher")) {
			return isUserHasWriteAccess(MenuIdEnum.TEACHER.getValue());
		} else if (((String) getSessionParameter("link"))
				.equals("inscriptions")) {
			return isUserHasWriteAccess(MenuIdEnum.INSCRIPTIONS.getValue());
		}
		return false;
	}

	public String doNothing() {
		return "Success";
	}

	public Long getSearchRowCount() {
		return searchRowCount;
	}

	public void setSearchRowCount(Long searchRowCount) {
		this.searchRowCount = searchRowCount;
	}

	public Long getTimeTableCount() {
		return timeTableCount;
	}

	public void setTimeTableCount(Long timeTableCount) {
		this.timeTableCount = timeTableCount;
	}

	public List<BaseEntity> getTimeTables() {
		return timeTables;
	}

	public void setTimeTables(List<BaseEntity> timeTables) {
		this.timeTables = timeTables;
	}

	public String getSubjectName() {
		return subjectName;
	}

	public void setSubjectName(String subjectName) {
		this.subjectName = subjectName;
	}

	public ExamTimeTable getTimeTable() {
		return timeTable;
	}

	public void setTimeTable(ExamTimeTable timeTable) {
		this.timeTable = timeTable;
	}

	public String getSchoolYearName() {
		return schoolYearName;
	}

	public void setSchoolYearName(String schoolYearName) {
		this.schoolYearName = schoolYearName;
	}

	@Override
	public Config getConfig() {
		return config;
	}

	@Override
	public void setConfig(Config config) {
		this.config = config;
	}

	public List<Date> getExamDays() {
		examDays = new LinkedList<Date>();
		examDays.add(new Date());
		return examDays;
	}

	public void setExamDays(List<Date> examDays) {
		this.examDays = examDays;
	}
	


	public Date getFirstDay() {
		return firstDay;
	}

	public void setFirstDay(Date firstDay) {
		this.firstDay = firstDay;
	}

	public Date getSecondDay() {
		return secondDay;
	}

	public void setSecondDay(Date secondDay) {
		this.secondDay = secondDay;
	}

	public Date getThirdDay() {
		return thirdDay;
	}

	public void setThirdDay(Date thirdDay) {
		this.thirdDay = thirdDay;
	}

	public Date getFourthDay() {
		return fourthDay;
	}

	public void setFourthDay(Date fourthDay) {
		this.fourthDay = fourthDay;
	}

	public Date getFifthDay() {
		return fifthDay;
	}

	public void setFifthDay(Date fifthDay) {
		this.fifthDay = fifthDay;
	}

	public Date getSixthDay() {
		return sixthDay;
	}

	public void setSixthDay(Date sixthDay) {
		this.sixthDay = sixthDay;
	}

	public Date getSeventhDay() {
		return seventhDay;
	}

	public void setSeventhDay(Date seventhDay) {
		this.seventhDay = seventhDay;
	}

	public String getFirstDayDescription() {
		return firstDayDescription;
	}

	public void setFirstDayDescription(String firstDayDescription) {
		this.firstDayDescription = firstDayDescription;
	}

	public String getSecondDayDescription() {
		return secondDayDescription;
	}

	public void setSecondDayDescription(String secondDayDescription) {
		this.secondDayDescription = secondDayDescription;
	}

	public String getThirdDayDescription() {
		return thirdDayDescription;
	}

	public void setThirdDayDescription(String thirdDayDescription) {
		this.thirdDayDescription = thirdDayDescription;
	}

	public String getFourthDayDescription() {
		return fourthDayDescription;
	}

	public void setFourthDayDescription(String fourthDayDescription) {
		this.fourthDayDescription = fourthDayDescription;
	}

	public String getFifthDayDescription() {
		return fifthDayDescription;
	}

	public void setFifthDayDescription(String fifthDayDescription) {
		this.fifthDayDescription = fifthDayDescription;
	}

	public String getSixthDayDescription() {
		return sixthDayDescription;
	}

	public void setSixthDayDescription(String sixthDayDescription) {
		this.sixthDayDescription = sixthDayDescription;
	}

	public String getSeventhDayDescription() {
		return seventhDayDescription;
	}

	public void setSeventhDayDescription(String seventhDayDescription) {
		this.seventhDayDescription = seventhDayDescription;
	}

	public List<ExamTimeTable> getFirstDayTimeTables() {
		return firstDayTimeTables;
	}

	public void setFirstDayTimeTables(List<ExamTimeTable> firstDayTimeTables) {
		this.firstDayTimeTables = firstDayTimeTables;
	}

	public List<ExamTimeTable> getSecondDayTimeTables() {
		return secondDayTimeTables;
	}

	public void setSecondDayTimeTables(List<ExamTimeTable> secondDayTimeTables) {
		this.secondDayTimeTables = secondDayTimeTables;
	}

	public List<ExamTimeTable> getThirdDayTimeTables() {
		return thirdDayTimeTables;
	}

	public void setThirdDayTimeTables(List<ExamTimeTable> thirdDayTimeTables) {
		this.thirdDayTimeTables = thirdDayTimeTables;
	}

	public List<ExamTimeTable> getFourthDayTimeTables() {
		return fourthDayTimeTables;
	}

	public void setFourthDayTimeTables(List<ExamTimeTable> fourthDayTimeTables) {
		this.fourthDayTimeTables = fourthDayTimeTables;
	}

	public List<ExamTimeTable> getFifthDayTimeTables() {
		return fifthDayTimeTables;
	}

	public void setFifthDayTimeTables(List<ExamTimeTable> fifthDayTimeTables) {
		this.fifthDayTimeTables = fifthDayTimeTables;
	}

	public List<ExamTimeTable> getSixthDayTimeTables() {
		return sixthDayTimeTables;
	}

	public void setSixthDayTimeTables(List<ExamTimeTable> sixthDayTimeTables) {
		this.sixthDayTimeTables = sixthDayTimeTables;
	}

	public List<ExamTimeTable> getSeventhDayTimeTables() {
		return seventhDayTimeTables;
	}

	public void setSeventhDayTimeTables(List<ExamTimeTable> seventhDayTimeTables) {
		this.seventhDayTimeTables = seventhDayTimeTables;
	}

	public String getSearchLevelClassName() {
		return searchLevelClassName;
	}

	public void setSearchLevelClassName(String searchLevelClassName) {
		this.searchLevelClassName = searchLevelClassName;
	}

	public String getSearchSubjectName() {
		return searchSubjectName;
	}

	public void setSearchSubjectName(String searchSubjectName) {
		this.searchSubjectName = searchSubjectName;
	}

	public String getSearchYearName() {
		return searchYearName;
	}

	public void setSearchYearName(String searchYearName) {
		this.searchYearName = searchYearName;
	}

	public List<BaseEntity> getSearchTimeTables() {
		return searchTimeTables;
	}

	public void setSearchTimeTables(List<BaseEntity> searchTimeTables) {
		this.searchTimeTables = searchTimeTables;
	}

	public String getTermName() {
		return termName;
	}

	public void setTermName(String termName) {
		this.termName = termName;
	}

	public String getSearchTermName() {
		return searchTermName;
	}

	public void setSearchTermName(String searchTermName) {
		this.searchTermName = searchTermName;
	}

	public boolean getHasErrorMessage() {
		if (getErrorMessage() != null && getErrorMessage().length() > 0)
			return true;
		else
			return false;
	}
}
