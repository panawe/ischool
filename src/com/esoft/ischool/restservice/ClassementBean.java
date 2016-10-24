package com.esoft.ischool.restservice;

import java.io.IOException;
import java.io.OutputStream;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.context.FacesContext;

import org.apache.commons.beanutils.BeanComparator;
import org.apache.commons.collections.comparators.ComparatorChain;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.esoft.ischool.job.Config;
import com.esoft.ischool.model.Averages;
import com.esoft.ischool.model.Bulletin;
import com.esoft.ischool.model.LevelClass;
import com.esoft.ischool.model.School;
import com.esoft.ischool.model.SchoolYear;
import com.esoft.ischool.model.Student;
import com.esoft.ischool.model.Term;
import com.esoft.ischool.model.TermAverage;
import com.esoft.ischool.model.TermGroup;
import com.esoft.ischool.model.TermGroupSummary;
import com.esoft.ischool.model.TermResult;
import com.esoft.ischool.model.TermResultSummary;
import com.esoft.ischool.model.YearSummary;
import com.esoft.ischool.service.BaseService;
import com.esoft.ischool.util.MenuIdEnum;

@Component("classementBean")
@Scope("session")
public class ClassementBean extends BaseBean {

	@Autowired
	@Qualifier("baseService")
	private BaseService baseService;
	private Long rowCount = 0L;
	private Long rowCountTermGroup = 0L;
	private Long rowCountAnnual = 0L;
	private String year;
	private String className;
	private String termName;
	private String termGroupName;
	private TermResult termResult;
	private List<TermResultSummary> resultSummaries;
	private List<YearSummary> yearSummaries;
	private List<TermGroupSummary> termGroupSummaries;
	private List<Bulletin> termSummaries;
	private List<Averages> averages;
	private TermResultSummary resultSmry;
	private YearSummary yearSmry;
	private TermGroupSummary termGroupSmry;
	private Bulletin termSmry;
	private Double random = 0.0;
	private String schoolName;
	private Student student;
	private List<TermAverage> termAverages;
	
	public String doIt() {
		return "success";
	}

	public YearSummary getYearSmry() {
		return yearSmry;
	}

	public void setYearSmry(YearSummary yearSmry) {
		this.yearSmry = yearSmry;
	}

	public String getDetails() {
		Long studentId = getIdParameter();
		random = Math.random();
		for (TermResultSummary smry : resultSummaries) {
			if (smry.getStudentId().equals(studentId.intValue())) {
				resultSmry = smry;
				return "success";
			}
		}
		return "success";
	}

	
	public String getTermDetails() {
		Long id = getIdParameter();
		random = Math.random();
		termSmry = (Bulletin) baseService.getById(Bulletin.class, id);
		student=termSmry.getStudent();
		return "success";
	}
	
	public String getTermGroupDetails() {
		Long id = getIdParameter();
		random = Math.random();
		termGroupSmry = (TermGroupSummary) baseService.getById(TermGroupSummary.class, id);
		student=termGroupSmry.getStudent();
		return "success";
	}
	
	public String getAnnualDetails() {
		Long id = getIdParameter();
		random = Math.random();
		yearSmry = (YearSummary) baseService.getById(YearSummary.class, id);
		student=yearSmry.getStudent();
		return "success";
	}
	
	public String getTermMarks() {
		
		Long studentId = new Long(getParameter("studentId"));
		Long id = getIdParameter();
		termSmry = (Bulletin) baseService.getById(Bulletin.class, id);
		student = (Student) baseService.getById(Student.class, studentId);
		
		random = Math.random();
		SchoolYear schoolYear = (SchoolYear) baseService.findByColumn(
				SchoolYear.class, "year", year, baseService.getDefaultSchool());
		School school=null;
		if (schoolName != null && getCurrentUser() == null) {
			school = (School) baseService.findSchoolByName(schoolName);
			if (school == null) {
				setErrorMessage(getErrorMessage()
						+ getResourceBundle().getString("INVALID_SCHOOL"));
				return "ERROR";
			}
		} else	if (schoolName == null && getCurrentUser() == null) {
			school = (School) baseService.getDefaultSchool();
			if (school == null) {
				setErrorMessage(getErrorMessage()
						+ getResourceBundle().getString("INVALID_SCHOOL"));
				return "ERROR";
			}
		}
		LevelClass lclass = (LevelClass) baseService.findByColumn(
				LevelClass.class, "name", className,
				getCurrentUser() != null ? getCurrentUser().getSchool()
						: school);
		
		Term term = (Term) baseService.findByColumn(Term.class, "name",
				termName, baseService.getDefaultSchool());
				
		averages = baseService.getAverages(schoolYear, lclass, term, studentId);
		return "success";
	}
	
	
	public String getAnnualMarks() {
		
		Long studentId = new Long(getParameter("studentId"));
		Long id = getIdParameter();

		yearSmry = (YearSummary) baseService.getById(YearSummary.class, id);
		student = (Student) baseService.getById(Student.class, studentId);
		
		random = Math.random();
		SchoolYear schoolYear = (SchoolYear) baseService.findByColumn(
				SchoolYear.class, "year", year, baseService.getDefaultSchool());
		School school=null;
		if (schoolName != null && getCurrentUser() == null) {
			school = (School) baseService.findSchoolByName(schoolName);
			if (school == null) {
				setErrorMessage(getErrorMessage()
						+ getResourceBundle().getString("INVALID_SCHOOL"));
				return "ERROR";
			}
		} else	if (schoolName == null && getCurrentUser() == null) {
			school = (School) baseService.getDefaultSchool();
			if (school == null) {
				setErrorMessage(getErrorMessage()
						+ getResourceBundle().getString("INVALID_SCHOOL"));
				return "ERROR";
			}
		}
		LevelClass lclass = (LevelClass) baseService.findByColumn(
				LevelClass.class, "name", className,
				getCurrentUser() != null ? getCurrentUser().getSchool()
						: school);
						
		averages = baseService.getAverages(schoolYear, lclass, studentId);
		termAverages = new ArrayList<TermAverage>();
		boolean found = false;
		
		List<Bulletin> bulletins = baseService.getBulletins(studentId, year);
		
		for (Averages average : averages) {
			found = false;
			for (TermAverage termAvg : termAverages) {
				if (average.getTerm().getName().equals(termAvg.getTermName())) {
					found = true;
					List<Averages> avgs = new ArrayList<Averages>();
					avgs.addAll(termAvg.getAverages());
					avgs.add(average);
					termAvg.setAverages(avgs);
				}	
			}
			
			if (!found) {
				TermAverage termAverage = new TermAverage();
				termAverage.setTermName(average.getTerm().getName());
				for (Bulletin bulletin : bulletins) {
					if (bulletin.getTerm().getName().equals(average.getTerm().getName())) {
						termAverage.setBulletin(bulletin);
					}
				}
				List<Averages> avgs = new ArrayList<Averages>();
				avgs.add(average);
				termAverage.setAverages(avgs);
				termAverages.add(termAverage);
			}
		}
		setOpen(false);
		return "success";
	}
	
	
	
	public String getTermGroupMarks() {
		
		Long studentId = new Long(getParameter("studentId"));
		Long id = getIdParameter();
		termGroupSmry = (TermGroupSummary) baseService.getById(TermGroupSummary.class, id);
		
		student = (Student) baseService.getById(Student.class, studentId);
		
		random = Math.random();
		SchoolYear schoolYear = (SchoolYear) baseService.findByColumn(
				SchoolYear.class, "year", year, baseService.getDefaultSchool());
		School school=null;
		if (schoolName != null && getCurrentUser() == null) {
			school = (School) baseService.findSchoolByName(schoolName);
			if (school == null) {
				setErrorMessage(getErrorMessage()
						+ getResourceBundle().getString("INVALID_SCHOOL"));
				return "ERROR";
			}
		} else	if (schoolName == null && getCurrentUser() == null) {
			school = (School) baseService.getDefaultSchool();
			if (school == null) {
				setErrorMessage(getErrorMessage()
						+ getResourceBundle().getString("INVALID_SCHOOL"));
				return "ERROR";
			}
		}
		LevelClass lclass = (LevelClass) baseService.findByColumn(
				LevelClass.class, "name", className,
				getCurrentUser() != null ? getCurrentUser().getSchool()
						: school);
						
		averages = baseService.getAverages(schoolYear, lclass, studentId);
		termAverages = new ArrayList<TermAverage>();
		boolean found = false;
		List<Bulletin> bulletins = baseService.getBulletins(studentId, year);
		for (Averages average : averages) {
			found = false;
			for (TermAverage termAvg : termAverages) {
				if (average.getTerm().getName().equals(termAvg.getTermName())) {
					found = true;
					List<Averages> avgs = new ArrayList<Averages>();
					avgs.addAll(termAvg.getAverages());
					avgs.add(average);
					termAvg.setAverages(avgs);
				}	
			}
			
			if (!found) {
				if (average.getTerm().getTermGroup() == null 
						|| average.getTerm().getTermGroup().getName().equals(termGroupName)) {
					TermAverage termAverage = new TermAverage();
					termAverage.setTermName(average.getTerm().getName());
					for (Bulletin bulletin : bulletins) {
						if (bulletin.getTerm().getName().equals(average.getTerm().getName())) {
							termAverage.setBulletin(bulletin);
						}
					}
					List<Averages> avgs = new ArrayList<Averages>();
					avgs.add(average);
					termAverage.setAverages(avgs);
					termAverages.add(termAverage);
				}
			}
		}
		setOpen(false);
		return "success";
	}
	
	public TermResultSummary getResultSmry() {
		return resultSmry;
	}

	public void setResultSmry(TermResultSummary resultSmry) {
		this.resultSmry = resultSmry;
	}

	@Override
	public Double getRandom() {
		return random;
	}

	@Override
	public void setRandom(Double random) {
		this.random = random;
	}

	public void paintStudent(OutputStream stream, Object object)
			throws IOException {
		if (student != null && student.getImage() != null
				&& student.getImage().length > 0) {
			stream.write(student.getImage());
		} else {
			stream.write(new byte[] {});
		}
	}

	public void paintStudentAnnual(OutputStream stream, Object object)
			throws IOException {
		if (yearSmry != null && yearSmry.getStudent().getImage() != null
				&& yearSmry.getStudent().getImage().length > 0) {
			stream.write(yearSmry.getStudent().getImage());
		} else {
			stream.write(new byte[] {});
		}
	}

	@Override
	public void paint(OutputStream stream, Object object) throws IOException {
		if (object != null && ((byte[]) object).length > 0) {
			stream.write(((byte[]) object));
		} else {
			stream.write(new byte[] {});
		}
	}

	public TermResult getTermResult() {
		return termResult;
	}

	public void setTermResult(TermResult termResult) {
		this.termResult = termResult;
	}

	public List<TermResultSummary> getResultSummaries() {
		return resultSummaries;
	}

	public void setResultSummaries(List<TermResultSummary> resultSummaries) {
		this.resultSummaries = resultSummaries;
	}

	public List<YearSummary> getYearSummaries() {
		return yearSummaries;
	}

	public void setYearSummaries(List<YearSummary> yearSummaries) {
		this.yearSummaries = yearSummaries;
	}

	public String fetchTermResults() {
		SchoolYear schoolYear = (SchoolYear) baseService.findByColumn(
				SchoolYear.class, "year", year, baseService.getDefaultSchool());
		LevelClass lclass = (LevelClass) baseService.findByColumn(
				LevelClass.class, "name", className, getCurrentUser()
						.getSchool());
		rowCount = 0L;
		Term term = (Term) baseService.findByColumn(Term.class, "name",
				termName, baseService.getDefaultSchool());
		if (schoolYear != null && lclass != null && term != null)
			termResult = new TermResult(baseService.getAverages(schoolYear,lclass, term));
		if (termResult != null) {
			rowCount = new Long(termResult.getTermResultSmry().size());
			resultSummaries = termResult.getTermResultSmry();
		}
		return "success";
	}

	public String getSchoolName() {
		return schoolName;
	}

	public void setSchoolName(String schoolName) {
		this.schoolName = schoolName;
	}

	public String fetchAnnualResults() {
		SchoolYear schoolYear = (SchoolYear) baseService.findByColumn(
				SchoolYear.class, "year", year, baseService.getDefaultSchool());
		School school=null;
		if (schoolName != null && getCurrentUser() == null) {
			school = (School) baseService.findSchoolByName(schoolName);
			if (school == null) {
				setErrorMessage(getErrorMessage()
						+ getResourceBundle().getString("INVALID_SCHOOL"));
				return "ERROR";
			}
		} else	if (schoolName == null && getCurrentUser() == null) {
			school = (School) baseService.getDefaultSchool();
			if (school == null) {
				setErrorMessage(getErrorMessage()
						+ getResourceBundle().getString("INVALID_SCHOOL"));
				return "ERROR";
			}
		}
		LevelClass lclass = (LevelClass) baseService.findByColumn(
				LevelClass.class, "name", className,
				getCurrentUser() != null ? getCurrentUser().getSchool()
						: school);
		rowCountAnnual = 0L;
		if (lclass != null && schoolYear != null) {
			yearSummaries = baseService.getYearSummaries(schoolYear, lclass,
					getCurrentUser() != null ? getCurrentUser().getSchool()
							: school);
			rowCountAnnual = new Long(yearSummaries == null ? 0
					: yearSummaries.size());
			termResult = new TermResult();
			
			if (yearSummaries != null && yearSummaries.size() > 0) {
				ComparatorChain chainComparator = new ComparatorChain();
				BeanComparator rankComparator = new BeanComparator("rankNbr"); 
				BeanComparator lastNameComparator = new BeanComparator("student.lastName"); 
				BeanComparator firstNameComparator = new BeanComparator("student.firstName"); 
				chainComparator.addComparator(rankComparator);
				chainComparator.addComparator(lastNameComparator);
				chainComparator.addComparator(firstNameComparator);
				Collections.sort(yearSummaries, chainComparator);
				termResult.setClassName(yearSummaries.get(0).getClassName());
				termResult.setYearName(yearSummaries.get(0).getSchoolYear().getYear());
				setAllYearAverages(yearSummaries);
			}
		}
		return "success";
	}

	
	public String fetchTermGroupResults() {
		SchoolYear schoolYear = (SchoolYear) baseService.findByColumn(
				SchoolYear.class, "year", year, baseService.getDefaultSchool());
		School school=null;
		if (schoolName != null && getCurrentUser() == null) {
			school = (School) baseService.findSchoolByName(schoolName);
			if (school == null) {
				setErrorMessage(getErrorMessage()
						+ getResourceBundle().getString("INVALID_SCHOOL"));
				return "ERROR";
			}
		} else	if (schoolName == null && getCurrentUser() == null) {
			school = (School) baseService.getDefaultSchool();
			if (school == null) {
				setErrorMessage(getErrorMessage()
						+ getResourceBundle().getString("INVALID_SCHOOL"));
				return "ERROR";
			}
		}
		LevelClass lclass = (LevelClass) baseService.findByColumn(
				LevelClass.class, "name", className,
				getCurrentUser() != null ? getCurrentUser().getSchool()
						: school);
		
		TermGroup termGroup = (TermGroup) baseService.findByColumn(TermGroup.class, "name",
				termGroupName, baseService.getDefaultSchool());
		
		rowCountTermGroup = 0L;
		if (lclass != null && schoolYear != null && termGroup != null) {
			termGroupSummaries = baseService.getTermGroupSummaries(schoolYear, lclass, termGroup, getCurrentUser());
			rowCountTermGroup = new Long(termGroupSummaries == null ? 0 : termGroupSummaries.size());
			if (termGroupSummaries != null && termGroupSummaries.size() > 0) {
				ComparatorChain chainComparator = new ComparatorChain();
				BeanComparator rankComparator = new BeanComparator("rankNbr"); 
				BeanComparator lastNameComparator = new BeanComparator("student.lastName"); 
				BeanComparator firstNameComparator = new BeanComparator("student.firstName"); 
				chainComparator.addComparator(rankComparator);
				chainComparator.addComparator(lastNameComparator);
				chainComparator.addComparator(firstNameComparator);
				Collections.sort(termGroupSummaries, chainComparator);
				termResult = new TermResult();
				termResult.setClassName(termGroupSummaries.get(0).getClassName());
				termResult.setYearName(termGroupSummaries.get(0).getSchoolYear().getYear());
				setAllTermGroupAverages(termGroupSummaries);
			}
		}
		return "success";
	}
	
	@PostConstruct
	public String doPost(){
		SchoolYear sy = baseService.getSchoolYear(new Date(),baseService.getDefaultSchool());
		year=sy==null?year:sy.getYear();
		return "";
	}
	
	public String fetchTermsResults() {
		SchoolYear schoolYear = (SchoolYear) baseService.findByColumn(
				SchoolYear.class, "year", year, baseService.getDefaultSchool());
		School school=null;
		if (schoolName != null && getCurrentUser() == null) {
			school = (School) baseService.findSchoolByName(schoolName);
			if (school == null) {
				setErrorMessage(getErrorMessage()
						+ getResourceBundle().getString("INVALID_SCHOOL"));
				return "ERROR";
			}
		} else	if (schoolName == null && getCurrentUser() == null) {
			school = (School) baseService.getDefaultSchool();
			if (school == null) {
				setErrorMessage(getErrorMessage()
						+ getResourceBundle().getString("INVALID_SCHOOL"));
				return "ERROR";
			}
		}
		LevelClass lclass = (LevelClass) baseService.findByColumn(
				LevelClass.class, "name", className,
				getCurrentUser() != null ? getCurrentUser().getSchool()
						: school);
		
		Term term = (Term) baseService.findByColumn(Term.class, "name",
				termName, baseService.getDefaultSchool());
		
		rowCount = 0L;
		if (lclass != null && schoolYear != null && term != null) {
			termSummaries = baseService.getTermSummaries(schoolYear, lclass, term, getCurrentUser());
			rowCount = new Long(termSummaries == null ? 0 : termSummaries.size());
			if (termSummaries != null && termSummaries.size() > 0) {
				ComparatorChain chainComparator = new ComparatorChain();
				BeanComparator rankComparator = new BeanComparator("rankNbr"); 
				BeanComparator lastNameComparator = new BeanComparator("student.lastName"); 
				BeanComparator firstNameComparator = new BeanComparator("student.firstName"); 
				chainComparator.addComparator(rankComparator);
				chainComparator.addComparator(lastNameComparator);
				chainComparator.addComparator(firstNameComparator);
				Collections.sort(termSummaries, chainComparator);
				termResult = new TermResult();
				termResult.setClassName(termSummaries.get(0).getClassName());
				termResult.setYearName(termSummaries.get(0).getSchoolYear().getYear());
				setAllAverages(termSummaries);
			}
		}
		return "success";
	}
	
	private void setAllAverages(List<Bulletin> summaries) {
		int b = 0;
		Double averageMark = 0d;
		Double maxMark = 0d;
		Double minMark = 0d;
		
		FacesContext context = FacesContext.getCurrentInstance();
		ApplicationContext ctx = org.springframework.web.jsf.FacesContextUtils.getWebApplicationContext(context);
		Config config = (Config) ctx.getBean("config");
		DecimalFormat df = new DecimalFormat(config.getConfigurationByGroupAndName("SCHOOL", "MARK_FORMAT"));
		
		for (Bulletin smry : summaries) {
			if (b == 0) {
				maxMark = smry.getMark();
				minMark = smry.getMark();
			}
				
			if (smry.getMark() > maxMark)
				maxMark = smry.getMark();
			if (smry.getMark() < minMark)
				minMark = smry.getMark();
			++b;
			averageMark += smry.getMark();
		}
		averageMark /= b;
		
		termResult.setMoyenne(new Double(df.format(averageMark)));
		termResult.setMaxMoyenne(new Double(df.format(maxMark)));
		termResult.setMinMoyenne(new Double(df.format(minMark)));
	}
	
	
	private void setAllTermGroupAverages(List<TermGroupSummary> summaries) {
		int b = 0;
		Double averageMark = 0d;
		Double maxMark = 0d;
		Double minMark = 0d;
		
		FacesContext context = FacesContext.getCurrentInstance();
		ApplicationContext ctx = org.springframework.web.jsf.FacesContextUtils.getWebApplicationContext(context);
		Config config = (Config) ctx.getBean("config");
		DecimalFormat df = new DecimalFormat(config.getConfigurationByGroupAndName("SCHOOL", "MARK_FORMAT"));
		
		for (TermGroupSummary smry : summaries) {
			if (b == 0) {
				maxMark = smry.getMark();
				minMark = smry.getMark();
			}
				
			if (smry.getMark() > maxMark)
				maxMark = smry.getMark();
			if (smry.getMark() < minMark)
				minMark = smry.getMark();
			++b;
			averageMark += smry.getMark();
		}
		averageMark /= b;
		
		termResult.setMoyenne(new Double(df.format(averageMark)));
		termResult.setMaxMoyenne(new Double(df.format(maxMark)));
		termResult.setMinMoyenne(new Double(df.format(minMark)));
	}
	
	private void setAllYearAverages(List<YearSummary> summaries) {
		int b = 0;
		Double averageMark = 0d;
		Double maxMark = 0d;
		Double minMark = 0d;
		
		FacesContext context = FacesContext.getCurrentInstance();
		ApplicationContext ctx = org.springframework.web.jsf.FacesContextUtils.getWebApplicationContext(context);
		Config config = (Config) ctx.getBean("config");
		DecimalFormat df = new DecimalFormat(config.getConfigurationByGroupAndName("SCHOOL", "MARK_FORMAT"));
		
		for (YearSummary smry : summaries) {
			if (b == 0) {
				maxMark = smry.getMark();
				minMark = smry.getMark();
			}
				
			if (smry.getMark() > maxMark)
				maxMark = smry.getMark();
			if (smry.getMark() < minMark)
				minMark = smry.getMark();
			++b;
			averageMark += smry.getMark();
		}
		averageMark /= b;
		
		termResult.setMoyenne(new Double(df.format(averageMark)));
		termResult.setMaxMoyenne(new Double(df.format(maxMark)));
		termResult.setMinMoyenne(new Double(df.format(minMark)));
	}
	
	
	public String modifyAnnualDecision() {
		baseService.update(yearSmry, getCurrentUser());
		fetchAnnualResults();
		return "success";
	}
	
	public String modifyTermGroupDecision() {
		baseService.update(termGroupSmry, getCurrentUser());
		fetchTermGroupResults();
		return "success";
	}

	public String modifyTermDecision() {
		baseService.update(termSmry, getCurrentUser());
		fetchTermsResults();
		return "success";
	}
	
	public String selectYearSmry() {

		Long ySmryId = getIdParameter();
		for (YearSummary ySmry : yearSummaries) {
			if (ySmry.getId().equals(ySmryId)) {
				yearSmry = ySmry;
				break;
			}
		}
		return "success";
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

	public String getTermName() {
		return termName;
	}

	public void setTermName(String termName) {
		this.termName = termName;
	}

	public boolean isUserHasWriteAccess() {
		return isUserHasWriteAccess(MenuIdEnum.ONLINE_RANKING.getValue());
	}

	public boolean isUserHasWriteAccess2() {
		return isUserHasWriteAccess(MenuIdEnum.ANNUAL_RESULTS.getValue());
	}

	public List<TermGroupSummary> getTermGroupSummaries() {
		return termGroupSummaries;
	}

	public void setTermGroupSummaries(List<TermGroupSummary> termGroupSummaries) {
		this.termGroupSummaries = termGroupSummaries;
	}

	public String getTermGroupName() {
		return termGroupName;
	}

	public void setTermGroupName(String termGroupName) {
		this.termGroupName = termGroupName;
	}

	public List<Bulletin> getTermSummaries() {
		return termSummaries;
	}

	public void setTermSummaries(List<Bulletin> termSummaries) {
		this.termSummaries = termSummaries;
	}

	public TermGroupSummary getTermGroupSmry() {
		return termGroupSmry;
	}

	public void setTermGroupSmry(TermGroupSummary termGroupSmry) {
		this.termGroupSmry = termGroupSmry;
	}

	public Bulletin getTermSmry() {
		return termSmry;
	}

	public void setTermSmry(Bulletin termSmry) {
		this.termSmry = termSmry;
	}

	public List<Averages> getAverages() {
		return averages;
	}

	public void setAverages(List<Averages> averages) {
		this.averages = averages;
	}

	public Student getStudent() {
		return student;
	}

	public void setStudent(Student student) {
		this.student = student;
	}

	public Long getRowCountTermGroup() {
		return rowCountTermGroup;
	}

	public void setRowCountTermGroup(Long rowCountTermGroup) {
		this.rowCountTermGroup = rowCountTermGroup;
	}

	public Long getRowCountAnnual() {
		return rowCountAnnual;
	}

	public void setRowCountAnnual(Long rowCountAnnual) {
		this.rowCountAnnual = rowCountAnnual;
	}

	public List<TermAverage> getTermAverages() {
		return termAverages;
	}

	public void setTermAverages(List<TermAverage> termAverages) {
		this.termAverages = termAverages;
	}
	
	
}
