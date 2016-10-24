package com.esoft.ischool.restservice;

import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.annotation.PostConstruct;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.esoft.ischool.model.BaseEntity;
import com.esoft.ischool.model.Disease;
import com.esoft.ischool.model.MedicalVisit;
import com.esoft.ischool.model.Student;
import com.esoft.ischool.model.Teacher;
import com.esoft.ischool.security.model.User;
import com.esoft.ischool.service.BaseService;
import com.esoft.ischool.util.MenuIdEnum;

@Component("medicalVisitBean")
@Scope("session")
public class MedicalVisitBean extends BaseBean {

	@Autowired
	@Qualifier("baseService")
	private BaseService baseService;
	private Long rowCount;
	private List<BaseEntity> medicalVisits;
	private String diseaseName;
	private String selectedTab;
	private User patient = new User();
	
	private MedicalVisit medicalVisit = new MedicalVisit();
	private boolean selectAllMedicalVisits;
	private Date beginVisitDate;
	private Date endVisitDate;

	public String validate() {
		return "succes";
	}

	@Override
	public String clear() {
		medicalVisit = new MedicalVisit();
		patient=null;
		diseaseName=null;
		return "Success";
	}

	public String searchMedicalVisits() {
		if (selectAllMedicalVisits)
			getAll();
		else if (beginVisitDate != null || endVisitDate != null)
			medicalVisits = baseService.getMedicalVisits(beginVisitDate, endVisitDate);
		
		rowCount = new Long((medicalVisits != null) ? medicalVisits.size() : 0);
		return "Success";
	}
	
	@PostConstruct
	public String getAll() {
		//patient = new User(); 
		medicalVisits = new ArrayList<BaseEntity>();
		if ("student".equals(getSessionParameter("link")) && getSessionParameter("currentStudentId") != null && !"0".equals(getSessionParameter("currentStudentId").toString())) {
			Student student = (Student) baseService.getById(Student.class, new Long(getSessionParameter("currentStudentId").toString()));
			medicalVisits = baseService.loadAllByParentId(MedicalVisit.class, "patient", "id", student.getUser().getId());
		}
		else if ("teacher".equals(getSessionParameter("link")) && getSessionParameter("currentTeacherId") != null && !"0".equals(getSessionParameter("currentTeacherId").toString())) {
			Teacher teacher = (Teacher) baseService.getById(Teacher.class, new Long(getSessionParameter("currentTeacherId").toString()));
			medicalVisits = baseService.loadAllByParentId(MedicalVisit.class, "patient", "id", teacher.getUser().getId());
		}
		else if ("consultation".equals(getSessionParameter("link"))) 
			medicalVisits = baseService.loadAll(MedicalVisit.class, getCurrentUser().getSchool());
		
		setRowCount(new Long(medicalVisits.size()));

		return "Success";
	}
	
	public String delete() {
		clearMessages();
		try {
			baseService.delete(getIdParameter(), MedicalVisit.class);
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
		Long id = medicalVisit.getId();
		try {
			Disease disease = (Disease) baseService.findByName(Disease.class, diseaseName, getCurrentUser().getSchool());
			medicalVisit.setDisease(disease);
			medicalVisit.setPatient(patient);
			
			if (id == null || id == 0)
				baseService.save(medicalVisit, getCurrentUser());
			else
				baseService.update(medicalVisit, getCurrentUser());
			
			setSuccessMessage(getResourceBundle().getString("SAVED_SUCCESSFULLY")); 
		} catch (Exception ex) {
			medicalVisit.setId(id);
			setErrorMessage(getResourceBundle().getString("SAVED_UNSUCCESSFULL"));
		}	
		clear();
		getAll();
		return "Success";
	}
	
	public String edit() {
		clearMessages();
		medicalVisit = (MedicalVisit) baseService.getById(MedicalVisit.class, getIdParameter());
		patient=medicalVisit.getPatient();
		Teacher teach = baseService.getTeacher(patient);
		Student stud = null;
		if (teach != null) {
			patient.setImage(teach.getImage());
			patient.setAllergy(teach.getAllergy());
		} else {
			stud = baseService.getStudent(patient);
			if (stud != null) {
				patient.setImage(stud.getImage());
				patient.setAllergy(stud.getAllergy());
			}
		}
		diseaseName = medicalVisit.getDisease().getName();
		selectedTab="medicalVisitDetails";
		return "Success";
	}
	
	public void paint(OutputStream stream, Object object) throws IOException { 
		if (patient != null) {
			stream.write(patient.getImage());
		} else {
			stream.write(new byte[] {});
		}
	}

	public String addPatient() {
		setSessionAttribute("pageProvenence", "MEDICAL_CONSULTATION");
		return "";
	}
	
	public String updateSearchCriteria() {
		beginVisitDate = null;
		endVisitDate = null;
		
		return "";
	}
	
	public String assignPatientToMedicalVisit() {
		patient = (User) baseService.getById(User.class, getIdParameter());
		Teacher teach = baseService.getTeacher(patient);
		Student stud = null;
		if (teach != null) {
			patient.setImage(teach.getImage());
			patient.setAllergy(teach.getAllergy());
		} else {
			stud = baseService.getStudent(patient);
			if (stud != null) {
				patient.setImage(stud.getImage());
				patient.setAllergy(stud.getAllergy());
			}
		}
		medicalVisit.setPatient(patient);
		return "";
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

	public List<BaseEntity> getMedicalVisits() {
		return medicalVisits;
	}

	public void setMedicalVisits(List<BaseEntity> medicalVisits) {
		this.medicalVisits = medicalVisits;
	}

	public MedicalVisit getMedicalVisit() {
		return medicalVisit;
	}

	public void setMedicalVisit(MedicalVisit medicalVisit) {
		this.medicalVisit = medicalVisit;
	}

	public String getDiseaseName() {
		return diseaseName;
	}

	public void setDiseaseName(String diseaseName) {
		this.diseaseName = diseaseName;
	}
	
	public boolean isUserHasWriteAccess() {
		return isUserHasWriteAccess(MenuIdEnum.INFIRMERIE_CONSULTATION.getValue());
	}

	@Override
	public String getSelectedTab() {
		return selectedTab;
	}

	@Override
	public void setSelectedTab(String selectedTab) {
		this.selectedTab = selectedTab;
	}

	public User getPatient() {
		return patient;
	}

	public void setPatient(User patient) {
		this.patient = patient;
	}

	public boolean isSelectAllMedicalVisits() {
		return selectAllMedicalVisits;
	}

	public void setSelectAllMedicalVisits(boolean selectAllMedicalVisits) {
		this.selectAllMedicalVisits = selectAllMedicalVisits;
	}

	public Date getBeginVisitDate() {
		return beginVisitDate;
	}

	public void setBeginVisitDate(Date beginVisitDate) {
		this.beginVisitDate = beginVisitDate;
	}

	public Date getEndVisitDate() {
		return endVisitDate;
	}

	public void setEndVisitDate(Date endVisitDate) {
		this.endVisitDate = endVisitDate;
	}
}
