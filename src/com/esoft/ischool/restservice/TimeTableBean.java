package com.esoft.ischool.restservice;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.esoft.ischool.job.Config;
import com.esoft.ischool.model.BaseEntity;
import com.esoft.ischool.model.Configuration;
import com.esoft.ischool.model.Course;
import com.esoft.ischool.model.SchoolYear;
import com.esoft.ischool.model.LevelClass;
import com.esoft.ischool.model.Student;
import com.esoft.ischool.model.Subject;
import com.esoft.ischool.model.Term;
import com.esoft.ischool.model.TimeTable;
import com.esoft.ischool.model.WeekDays;
import com.esoft.ischool.service.BaseService;
import com.esoft.ischool.util.Constants;
import com.esoft.ischool.util.MenuIdEnum;

@Component("timeTableBean")
@Scope("session")
public class TimeTableBean extends BaseBean {

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
	private List<WeekDays> weekDays;
	private String levelClassName;
	private String schoolYearName;
	private String termName;
	private String subjectName;
	private TimeTable timeTable = new TimeTable();

	private String selectedTab = "timerTableList";
	private String monday;
	private String tuesday;
	private String wednesday;
	private String thursday;
	private String friday;
	private String saturday;
	private String sunday;

	private List<BaseEntity> searchTimeTables;
	private List<TimeTable> mondayTimeTables;
	private List<TimeTable> tuesdayTimeTables;
	private List<TimeTable> wednesdayTimeTables;
	private List<TimeTable> thursdayTimeTables;
	private List<TimeTable> fridayTimeTables;
	private List<TimeTable> saturdayTimeTables;
	private List<TimeTable> sundayTimeTables;

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
					.getTimeTablesByTeacherId(TimeTable.class,
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
								TimeTable.class, "levelClass",
								levelClass.getId(), "schoolYear",
								schoolYear.getId());
					} else {
						searchTimeTables = baseService.loadByParentsIds(
								TimeTable.class, "levelClass",
								levelClass.getId(), "schoolYear",
								schoolYear.getId(), "term", aTerm.getId());
					}
				} else {
					searchTimeTables = baseService.loadByParentsIds(
							TimeTable.class, "levelClass", levelClass.getId(),
							"schoolYear", schoolYear.getId(), "term",
							term.getId());
				}

			}

			mondayTimeTables = new LinkedList<TimeTable>();
			tuesdayTimeTables = new LinkedList<TimeTable>();
			wednesdayTimeTables = new LinkedList<TimeTable>();
			thursdayTimeTables = new LinkedList<TimeTable>();
			fridayTimeTables = new LinkedList<TimeTable>();
			saturdayTimeTables = new LinkedList<TimeTable>();
			sundayTimeTables = new LinkedList<TimeTable>();

			for (BaseEntity entity : searchTimeTables) {
				TimeTable tt = (TimeTable) entity;
				if ("1".equals(tt.getDayOfWeek()))
					mondayTimeTables.add(tt);
				else if ("2".equals(tt.getDayOfWeek()))
					tuesdayTimeTables.add(tt);
				else if ("3".equals(tt.getDayOfWeek()))
					wednesdayTimeTables.add(tt);
				else if ("4".equals(tt.getDayOfWeek()))
					thursdayTimeTables.add(tt);
				else if ("5".equals(tt.getDayOfWeek()))
					fridayTimeTables.add(tt);
				else if ("6".equals(tt.getDayOfWeek()))
					saturdayTimeTables.add(tt);
				else if ("7".equals(tt.getDayOfWeek()))
					sundayTimeTables.add(tt);
			}
			clearMessages();
		} else {
			setErrorMessage(Constants.NOT_A_VALID_YEAR_OR_CLASS);
		}
		return "Success";
	}

	public String edit() {
		clearMessages();
		timeTable = (TimeTable) baseService.getById(TimeTable.class,
				getIdParameter());
		setSchoolYearName(timeTable.getSchoolYear().getYear());
		setSubjectName(timeTable.getCourse().getSubject().getName());
		setLevelClassName(timeTable.getLevelClass().getName());
		setTermName(timeTable.getTerm() != null ? timeTable.getTerm().getName() : "");

		return "Success";
	}

	@Override
	public String clear() {
		timeTable = new TimeTable();
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
			baseService.delete(getIdParameter(), TimeTable.class);
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
			setErrorMessage(ex, "Cet emploi tu temps exist deja. ");
		}

		return "S";
	}

	@PostConstruct
	private void getAll() {
		timeTables = baseService.loadAll(TimeTable.class, getCurrentUser()
				.getSchool());

		for (BaseEntity tt : timeTables) {
			TimeTable timeTable = (TimeTable) tt;
			timeTable.setDayOfWeekDescription(config
					.getConfigurationByGroupAndKey("DAY_OF_WEEK", timeTable
							.getDayOfWeek().toString()));
		}

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

	public TimeTable getTimeTable() {
		return timeTable;
	}

	public void setTimeTable(TimeTable timeTable) {
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

	public List<WeekDays> getWeekDays() {
		weekDays = new LinkedList<WeekDays>();
		List<Configuration> configuredDays = config
				.getConfigurationListByGroup("DAY_OF_WEEK");
		WeekDays weekDay = new WeekDays();

		for (Configuration c : configuredDays) {
			if ("1".equals(c.getValue()))
				setMonday(c.getName());
			else if ("2".equals(c.getValue()))
				setTuesday(c.getName());
			else if ("3".equals(c.getValue()))
				setWednesday(c.getName());
			else if ("4".equals(c.getValue()))
				setThursday(c.getName());
			else if ("5".equals(c.getValue()))
				setFriday(c.getName());
			else if ("6".equals(c.getValue()))
				setSaturday(c.getName());
			else if ("7".equals(c.getValue()))
				setSunday(c.getName());
		}

		weekDays.add(weekDay);
		return weekDays;
	}

	public void setWeekDays(List<WeekDays> weekDays) {
		this.weekDays = weekDays;
	}

	public String getMonday() {
		return monday;
	}

	public void setMonday(String monday) {
		this.monday = monday;
	}

	public String getTuesday() {
		return tuesday;
	}

	public void setTuesday(String tuesday) {
		this.tuesday = tuesday;
	}

	public String getWednesday() {
		return wednesday;
	}

	public void setWednesday(String wednesday) {
		this.wednesday = wednesday;
	}

	public String getThursday() {
		return thursday;
	}

	public void setThursday(String thursday) {
		this.thursday = thursday;
	}

	public String getFriday() {
		return friday;
	}

	public void setFriday(String friday) {
		this.friday = friday;
	}

	public String getSaturday() {
		return saturday;
	}

	public void setSaturday(String saturday) {
		this.saturday = saturday;
	}

	public String getSunday() {
		return sunday;
	}

	public void setSunday(String sunday) {
		this.sunday = sunday;
	}

	public List<TimeTable> getMondayTimeTables() {
		if (!"school".equals(getSessionParameter("link")))
			searchTimeTable();
		return mondayTimeTables;
	}

	public void setMondayTimeTables(List<TimeTable> mondayTimeTables) {
		this.mondayTimeTables = mondayTimeTables;
	}

	public List<TimeTable> getTuesdayTimeTables() {
		return tuesdayTimeTables;
	}

	public void setTuesdayTimeTables(List<TimeTable> tuesdayTimeTables) {
		this.tuesdayTimeTables = tuesdayTimeTables;
	}

	public List<TimeTable> getWednesdayTimeTables() {
		return wednesdayTimeTables;
	}

	public void setWednesdayTimeTables(List<TimeTable> wednesdayTimeTables) {
		this.wednesdayTimeTables = wednesdayTimeTables;
	}

	public List<TimeTable> getThursdayTimeTables() {
		return thursdayTimeTables;
	}

	public void setThursdayTimeTables(List<TimeTable> thursdayTimeTables) {
		this.thursdayTimeTables = thursdayTimeTables;
	}

	public List<TimeTable> getFridayTimeTables() {
		return fridayTimeTables;
	}

	public void setFridayTimeTables(List<TimeTable> fridayTimeTables) {
		this.fridayTimeTables = fridayTimeTables;
	}

	public List<TimeTable> getSaturdayTimeTables() {
		return saturdayTimeTables;
	}

	public void setSaturdayTimeTables(List<TimeTable> saturdayTimeTables) {
		this.saturdayTimeTables = saturdayTimeTables;
	}

	public List<TimeTable> getSundayTimeTables() {
		return sundayTimeTables;
	}

	public void setSundayTimeTables(List<TimeTable> sundayTimeTables) {
		this.sundayTimeTables = sundayTimeTables;
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
