package com.esoft.ischool.restservice;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.esoft.ischool.model.BaseEntity;
import com.esoft.ischool.model.Receiver;
import com.esoft.ischool.model.SchoolYear;
import com.esoft.ischool.model.Student;
import com.esoft.ischool.model.Correspondence;
import com.esoft.ischool.model.Teacher;
import com.esoft.ischool.service.BaseService;
import com.esoft.ischool.util.MenuIdEnum;
import com.esoft.ischool.util.SimpleMail;
import com.mysql.jdbc.StringUtils;

@Component("correspondenceBean")
@Scope("session")
public class CorrespondenceBean extends BaseBean {

	@Autowired
	@Qualifier("baseService")
	private BaseService baseService;
	private Long rowCount;
	private Long correspondanceCount;
	private Long receivedCorrespondenceCount;
	private List<BaseEntity> correspondences;
	private List<Student> availableStudents = new ArrayList<Student>();
	private List<Student> selectedStudents = new ArrayList<Student>();

	private List<Teacher> availableTeachers = new ArrayList<Teacher>();
	private List<Teacher> selectedTeachers = new ArrayList<Teacher>();

	private Correspondence correspondence = new Correspondence();
	private Receiver receivedCorrespondence = new Receiver();

	private List<BaseEntity> receivedCorrespondences;

	private String year;
	private String className;
	private boolean selectAllTeachers;
	private boolean selectAllStudents;
	private boolean boxChecked;
	private boolean individual;
	private boolean sendEmail;
	private String selectedStudentTab="studentCorrespondenceDetails";
	private String selectedTeacherTab="teacherCorrespondenceDetails";
	
	

	public String getSelectedStudentTab() {
		return selectedStudentTab;
	}

	public void setSelectedStudentTab(String selectedStudentTab) {
		this.selectedStudentTab = selectedStudentTab;
	}

	public String getSelectedTeacherTab() {
		return selectedTeacherTab;
	}

	public void setSelectedTeacherTab(String selectedTeacherTab) {
		this.selectedTeacherTab = selectedTeacherTab;
	}

	private String selectedTab = "correspondanceDetails";

	@Override
	public String getSelectedTab() {
		return selectedTab;
	}

	public Long getCorrespondanceCount() {
		return correspondanceCount;
	}

	public void setCorrespondanceCount(Long correspondanceCount) {
		this.correspondanceCount = correspondanceCount;
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
		correspondence = new Correspondence();
		availableTeachers = null;
		availableStudents = null;
		selectedStudents=new ArrayList<Student>();
		selectedTeachers=new ArrayList<Teacher>();
		return "Success";
	}
	
	public String clearStudentCorrespondence() {
		receivedCorrespondence = new Receiver();
		return "Success";
	}
	
	public String clearTeacherCorrespondence() {
		receivedCorrespondence = new Receiver();
		return "Success";
	}
	
	public String getShowAll() {
		getAll();
		return "Success";
	}

	public String delete() {
		clearMessages();
		try {
			baseService.delete(getIdParameter(), Correspondence.class);
			getAll();
			clear();
			setSuccessMessage(getResourceBundle().getString("DELETE_SUCCESSFULLY"));
		} catch (Exception ex) {
			setErrorMessage(getResourceBundle().getString("DELETE_UNSUCCESSFULL"));
		}

		return "Success";
	}

	public String deleteStudentCorrespondence() {
		clearMessages();
		try {
			baseService.delete(getIdParameter(), Receiver.class);
			getStudentCorrespondences();
			clear();
			setSuccessMessage(getResourceBundle().getString("DELETE_SUCCESSFULLY"));
		} catch (Exception ex) {
			setErrorMessage(getResourceBundle().getString("DELETE_UNSUCCESSFULL"));
		}

		return "Success";
	}


	public String insert() {
		clearMessages();
		Long id = correspondence.getId();
		correspondence.setCorrespondenceDate(new Date());
		try {
			if (id == null || id == 0) {
				baseService.save(correspondence,getCurrentUser());
			} else {
				baseService.update(correspondence,getCurrentUser());
			}

			// Save Student

			baseService.saveStudentsCorrespondance(correspondence,
					selectedStudents,getCurrentUser());

			baseService.saveTeachersCorrespondance(correspondence,
					selectedTeachers,getCurrentUser());

			// send e-mail
			if (sendEmail){
				correspondence.setSent((short) 1);
				sendMail(selectedStudents, selectedTeachers);
			}
			setSuccessMessage(getResourceBundle().getString("SAVED_SUCCESSFULLY") + (sendEmail ? getResourceBundle().getString("aCopyHasBeenSent") : ""));
		} catch (Exception ex) {
			correspondence.setId(id);
			setErrorMessage(ex, "Cette correspondence exist deja. ");
			ex.printStackTrace();
		}

		clear();
		getAll();
		return "Success";
	}

	public String insertStudentCorrespondence() {
		clearMessages();
		Long id = receivedCorrespondence.getId();
		correspondence = receivedCorrespondence.getCorrespondence();
		
		if(correspondence == null || StringUtils.isNullOrEmpty(correspondence.getDescription()))
			setErrorMessage(getResourceBundle().getString("NotNullMessage") + getResourceBundle().getString("message"));
				
		if (!StringUtils.isNullOrEmpty(getErrorMessage()))
			return "ERROR";
		
		try {
			if (id == null || id == 0) {
				if (correspondence.getId() == null||correspondence.getId()==0) {
					correspondence.setCorrespondenceDate(new Date());
					baseService.save(correspondence,getCurrentUser());
				}
			}
			else {				
				baseService.delete(receivedCorrespondence.getId(), Receiver.class);
				correspondence.setId(null);
				baseService.save(correspondence,getCurrentUser());
			}

			
			if (((String)getSessionParameter("link")).equals("student")) {
				// Save Student
				if (getSessionParameter("currentStudentId") != null) {
					List<Student> students = new ArrayList<Student>();
					students.add((Student) baseService.getById(Student.class,
							new Long(getSessionParameter("currentStudentId")
									.toString())));
					baseService
							.saveStudentsCorrespondance(correspondence, students,getCurrentUser());
					// send e-mail
					if (sendEmail){
						correspondence.setSent((short) 1);
						sendMail(students, new ArrayList<Teacher>());
					}
				}
			}
			else if (((String)getSessionParameter("link")).equals("teacher")) {
				// Save Teacher
				if (getSessionParameter("currentTeacherId") != null) {
					List<Teacher> teachers = new ArrayList<Teacher>();
					teachers.add((Teacher) baseService.getById(Teacher.class,
							new Long(getSessionParameter("currentTeacherId")
									.toString())));
					baseService
							.saveTeachersCorrespondance(correspondence, teachers,getCurrentUser());
					if (sendEmail){
						correspondence.setSent((short) 1);
						sendMail(new ArrayList<Student>(), teachers);
					}
				}
			}
			setSuccessMessage(getResourceBundle().getString("SAVED_SUCCESSFULLY") + (sendEmail ? getResourceBundle().getString("aCopyHasBeenSent") : ""));
		} catch (Exception ex) {
			correspondence.setId(id);
			setErrorMessage(ex, "Cette correspondence exist deja. ");
			ex.printStackTrace();
		}

		clear();
		getStudentCorrespondences();
		return "Success";
	}


	public void sendMail(List<Student> selectedStudents,
			List<Teacher> selectedTeachers) {
		StringBuffer sb = new StringBuffer();
		for (Student student : selectedStudents) {
			setStudentEmail(student, sb);
		}
		for (Teacher teacher : selectedTeachers) {
			setTeacherEmail(teacher, sb);
		}

		try {
			if (sb.length() > 0) {
				String to = sb.toString();
				Map<String, String> config = (Map<String,String>) getSessionParameter("configuration");
				if (config != null) {
					SimpleMail.sendMail(correspondence.getSubject(),
							correspondence.getDescription(), config.get("SCHOOL_SENDER_EMAIL"), to.substring(0, to
									.length() - 1), config.get("SCHOOL_SMTP_SERVER"), config.get("SCHOOL_MAIL_SERVER_USER"), 
									config.get("SCHOOL_MAIL_SERVER_PASSWORD"));
				}
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	private void setStudentEmail(Student st, StringBuffer sb) {
		Student student= (Student) baseService.findByColumn(Student.class, "matricule", st.getMatricule(), getCurrentUser().getSchool());
		if (student.getEmail() != null && !student.getEmail().equals(""))
			sb.append(student.getEmail() + ",");
	}

	private void setTeacherEmail(Teacher teacher, StringBuffer sb) {
		Teacher aTeacher= (Teacher) baseService.findByColumn(Teacher.class, "matricule", teacher.getMatricule(), getCurrentUser().getSchool());


		if (aTeacher.getEmail() != null && !aTeacher.getEmail().equals("")) {
			sb.append(aTeacher.getEmail() + ",");
		}
	}

	public String edit() {
		clearMessages();
		correspondence = (Correspondence) baseService.getById(
				Correspondence.class, getIdParameter());
		selectedTab = "correspondanceDetails";
		return "Success";
	}
	
	public String editStudentCorrespondence() {
		clearMessages();
		receivedCorrespondence = (Receiver) baseService.getById(Receiver.class, getIdParameter());
		setSelectedTab();
		return "Success";
	}

	private void setSelectedTab() {
		if (((String)getSessionParameter("link")).equals("student"))
			selectedStudentTab="studentCorrespondenceDetails";
		else if (((String)getSessionParameter("link")).equals("teacher"))
			selectedStudentTab="teacherCorrespondenceDetails";
	}
	
	public String editTeacherCorrespondence() {
		clearMessages();
		receivedCorrespondence = (Receiver) baseService.getById(Receiver.class, getIdParameter());
		return "Success";
	}

	@PostConstruct
	private void getAll() {
		correspondences = baseService.loadAll(Correspondence.class,getCurrentUser().getSchool());
		setCorrespondanceCount(new Long(correspondences.size()));
		SchoolYear sy = baseService.getSchoolYear(new Date(),baseService.getDefaultSchool());
		year=sy==null?year:sy.getYear();
	}

	public String getStudentCorrespondences() {
		setIndividual(true);
		
		if (((String)getSessionParameter("link")).equals("student")) {
			if (getSessionParameter("currentStudentId") != null) {
				receivedCorrespondences = baseService
						.loadAllByParentId(Receiver.class, "student", "id",
								new Long(getSessionParameter("currentStudentId")
										.toString()));
				setRowCount(new Long(receivedCorrespondences.size()));
			}
		}
		else if (((String)getSessionParameter("link")).equals("teacher")) {
			if (getSessionParameter("currentTeacherId") != null) {
				receivedCorrespondences = baseService
						.loadByParentsIds(Receiver.class, "teacher", new Long(getSessionParameter("currentTeacherId")
										.toString()), "school", getCurrentUser().getSchool().getId());
				setRowCount(new Long(receivedCorrespondences.size()));
			}
		}
		return "Success";
	}

	public boolean isUserHasWriteAccess() {
		if ( ((String)getSessionParameter("link")).equals("student")) {
			return isUserHasWriteAccess(MenuIdEnum.STUDENT.getValue());
		} else if ( ((String)getSessionParameter("link")).equals("teacher")) {
			return isUserHasWriteAccess(MenuIdEnum.TEACHER.getValue());
		} else if (((String)getSessionParameter("link")).equals("correspondance")) {
			return isUserHasWriteAccess(MenuIdEnum.CORRESPONDENCE.getValue());
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

	public List<BaseEntity> getCorrespondences() {
		return correspondences;
	}

	public void setCorrespondences(List<BaseEntity> correspondences) {
		this.correspondences = correspondences;
	}

	public Correspondence getCorrespondence() {
		return correspondence;
	}

	public void setCorrespondence(Correspondence correspondence) {
		this.correspondence = correspondence;
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

	public String search() {
		availableTeachers = null;
		availableStudents = null;
		selectedStudents=new ArrayList<Student>();
		selectedTeachers=new ArrayList<Teacher>();
		if (boxChecked) {
			if (selectAllStudents)
				availableStudents = baseService.searchStudents(
						selectAllStudents, className, year, getCurrentUser().getSchool());
			else
				availableTeachers = baseService.searchTeachers(
						selectAllTeachers, className, year, getCurrentUser().getSchool());
		} else {
			availableStudents = baseService.searchStudents(selectAllStudents,
					className, year, getCurrentUser().getSchool());
		}

		rowCount = new Long(availableTeachers != null ? availableTeachers
				.size() : (availableStudents != null ? availableStudents.size()
				: 0));
		
		return "Success";
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

	public List<BaseEntity> getReceivedCorrespondences() {
		return receivedCorrespondences;
	}

	public void setReceivedCorrespondences(
			List<BaseEntity> receivedCorrespondences) {
		this.receivedCorrespondences = receivedCorrespondences;
	}

	public Long getReceivedCorrespondenceCount() {
		return receivedCorrespondenceCount;
	}

	public void setReceivedCorrespondenceCount(Long receivedCorrespondenceCount) {
		this.receivedCorrespondenceCount = receivedCorrespondenceCount;
	}

	public boolean isSendEmail() {
		return sendEmail;
	}

	public void setSendEmail(boolean sendEmail) {
		this.sendEmail = sendEmail;
	}

	public Receiver getReceivedCorrespondence() {
		return receivedCorrespondence;
	}

	public void setReceivedCorrespondence(Receiver receivedCorrespondence) {
		this.receivedCorrespondence = receivedCorrespondence;
	}
	
	
}
