package com.esoft.ischool.restservice;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Serializable;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Date;
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
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.esoft.ischool.job.Config;
import com.esoft.ischool.model.BaseEntity;
import com.esoft.ischool.model.Country;
import com.esoft.ischool.model.Parent;
import com.esoft.ischool.model.ParentStudent;
import com.esoft.ischool.model.Payment;
import com.esoft.ischool.model.School;
import com.esoft.ischool.model.SchoolYear;
import com.esoft.ischool.model.Student;
import com.esoft.ischool.model.Tuition;
import com.esoft.ischool.model.TuitionType;
import com.esoft.ischool.security.bean.UserBean;
import com.esoft.ischool.service.BaseService;
import com.esoft.ischool.util.MenuIdEnum;
import com.esoft.ischool.vo.StudentVO;

@Component("parentBean")
@Scope("session")
public class ParentBean extends BaseBean {

	@Autowired
	@Qualifier("baseService")
	private BaseService baseService;
	private Long rowCount;
	private Long selectedStudentsCount;
	private String countryName;
	private boolean parentSelected;
	private boolean selectAllStudents;
	private boolean boxChecked;
	private List<BaseEntity> parents;
	private List<String> allLevels = new ArrayList();
	private List<Student> availableStudents = new ArrayList<Student>();
	private List<Student> selectedStudents = new ArrayList<Student>();
	private String className;
	private String year;
	private Long searchRowCount;
	private String studentLastName;
	private Long parentId;
	private String firstName;
	private String lastName;
	
	private Parent parent = new Parent();
	private Tuition tuition = new Tuition();
	private String tuitionTypeName = "";
	private Double newPayment;
	private Double newRebate;
	private Long paymentId=0L;
	private Map<Long, Boolean> tuitionCollapsibleToggleMap = new HashMap<Long, Boolean>();

	public String validate() {
		return "succes";
	}
	
	public Long getPaymentId() {
		return paymentId;
	}

	public void setPaymentId(Long paymentId) {
		this.paymentId = paymentId;
	}

	@Override
	public String clear() {
		parent = new Parent();
		countryName = "";
		setPicture(null);
		parentSelected = false;
		selectedStudents = new ArrayList();
		selectedStudentsCount = 0L;
		return "Success";
	}
	
	public List<Parent> autoComplete(Object suggest) {
		String pref = (String) suggest;
		List<Parent> result = new ArrayList<Parent>();
		return result;
	}
	
	public String doNothing(){
		return "";
	}

	public String delete() {
		clearMessages();
		try {
			baseService.delete(getIdParameter(), Parent.class);
			search();
			clear();
			setSuccessMessage(getResourceBundle().getString("DELETE_SUCCESSFULLY"));
		} catch (Exception ex) {
			setErrorMessage(getResourceBundle().getString("DELETE_UNSUCCESSFULL"));
		}
		return "Success";
	}
	
	public String insert() {
		clearMessages();
		Long id = parent.getId();
		try {
			Country country = (Country) baseService.findByColumn(Country.class,
					"name", countryName);
			if (country == null) {
				setSuccessMessage(getResourceBundle().getString("RESIDENCE_COUNTRY_NOT_FOUND"));
				return "ERROR";
			}
			parent.setCountry(country);
			
			// get picture
			if (getPicture() != null) {
				parent.setImage(getPicture());
				if (parent.getImage().length > MAX_FILE_SIZE) {
					setErrorMessage(MAX_SIZE_EXCEEDED);
					return "ERROR";
				}
			}
						
			if (id == null || id == 0)
				baseService.save(parent, getCurrentUser());
			else
				baseService.update(parent, getCurrentUser());
			
			if ("student".equalsIgnoreCase((String) getSessionParameter("link"))) {
				StudentBean studentBean = (StudentBean) getSessionParameter("studentBean");
				FacesContext context = FacesContext.getCurrentInstance();
				ApplicationContext ctx = org.springframework.web.jsf.FacesContextUtils
						.getWebApplicationContext(context);
				Config config = (Config) ctx.getBean("config");
				parent.setParentTypeText(config.getConfigurationByGroupAndKey("PARENT_TYPE", 
						parent.getParentType() != null ? parent.getParentType().toString() : ""));
				studentBean.getSelectedParents().add(parent);
				Student student = studentBean.getStudent();
				if (student != null && student.getId() != null && student.getId() > 0) {
					baseService.assignParentsToStudent(student, studentBean.getSelectedParents(), getCurrentUser());
				}
				studentBean.setSelectedParentCounts(new Long(studentBean.getSelectedParents().size()));
			}
			setSuccessMessage(getResourceBundle().getString("SAVED_SUCCESSFULLY"));
		} catch (Exception ex) {
			parent.setId(id);
			ex.printStackTrace();
			setErrorMessage(ex,"Ce Parent existe deja");
		}		
		clear();
		search();
		return "Success";
	}

	public String selectParent() {
		parentId = getIdParameter();
		return "success";
	}

	private List<Long> studentWithTuitionToPay = new ArrayList<Long>();
	private long studentWithTuitionToPayCount = 0;
	private boolean showStudentTuitionsDetails = false;
	
	public List<Long> getStudentWithTuitionToPay() {
		return studentWithTuitionToPay;
	}

	public void setStudentWithTuitionToPay(List<Long> studentWithTuitionToPay) {
		this.studentWithTuitionToPay = studentWithTuitionToPay;
	}

	public Long getStudentWithTuitionToPayCount() {
		return new Long(studentWithTuitionToPay.size());
	}

	public void setStudentWithTuitionToPayCount(long studentWithTuitionToPayCount) {
		this.studentWithTuitionToPayCount = studentWithTuitionToPayCount;
	}

	public String updateStudentPaymentSelection() {
		Long studentId = getIdParameter();
		if (studentWithTuitionToPay.contains(studentId))
			studentWithTuitionToPay.remove(studentId);
		else
			studentWithTuitionToPay.add(studentId);
		
		return "";
	}
	
	public String initiatePayment() {
		boolean found = false;
		studentWithTuitionToPay = new ArrayList<Long>();
		parent.setStudentVOs(new ArrayList<StudentVO>());
		for (Student stu : selectedStudents) {
			if (stu.isSelectedForPayment()) {
				found = true;
				studentWithTuitionToPay.add(stu.getId());
			}
		}
		
		if (!found) {
			setErrorMessage("Vous devez selectionner au moins un etudiant.");
			setSuccessMessage("");
		}
		else {
			tuition = new Tuition();
			setErrorMessage("");
		}
		return "";
	}
	
	public String resetPayments() {
		studentWithTuitionToPay.clear();
		parent.setStudentVOs(new ArrayList<StudentVO>());
		return "";
	}
	
	public String getParentPaymentsByStudents() {
		if (parent==null) {
			setErrorMessage("Vous devez selectionner au moins un parent.");
			setSuccessMessage("");
			return "";
		}
		SchoolYear defaultYear = null;
		int i = 0;
		studentWithTuitionToPay.clear();
		for (Student stu : selectedStudents) {
			if (i == 0)
				defaultYear = selectedStudents.get(i).getCurrentEnrollment().getSchoolYear();
			i ++;
			studentWithTuitionToPay.add(stu.getId());
		}
		SchoolYear schoolYear = null;
		if (StringUtils.isBlank(year)) 
			schoolYear = defaultYear;
		else
			schoolYear = (SchoolYear) baseService.findByColumn(
					SchoolYear.class, "year", year, baseService.getDefaultSchool());

		List<Integer> errors = new ArrayList<Integer>();
		
		baseService.getParentPaymentsByStudents(parent, selectedStudents, studentWithTuitionToPay, 
					getCurrentUser().getSchool(), schoolYear.getId(), errors);
		
		return "";
	}
	
	public String savePaymentsForStudentsByTuitionType () {
		setErrorMessage("");
		setSuccessMessage("");
		TuitionType tuitionType = (TuitionType) baseService.findByName(TuitionType.class, 
				tuitionTypeName, getCurrentUser().getSchool());
		SchoolYear schoolYear = (SchoolYear) baseService.findByColumn(
				SchoolYear.class, "year", year, baseService.getDefaultSchool());
		List<Integer> errors = new ArrayList<Integer>();
		
		Payment payment = null;
		
		if (tuition.getAmount() >= 0)
			payment = baseService.saveStudentsPaymentsByTuitionType(studentWithTuitionToPay, 
				getCurrentUser().getSchool(), tuitionType.getId(), schoolYear.getId(), tuition, errors, getCurrentUser());
		else
			payment = baseService.saveStudentsNegativePaymentsByTuitionType(studentWithTuitionToPay, 
					getCurrentUser().getSchool(), tuitionType.getId(), schoolYear.getId(), tuition, errors, getCurrentUser());
		
		if(payment!=null){
			paymentId = payment.getId();
		}
		
		for (Integer error : errors) {
			if (error == 1) {
				setErrorMessage(getResourceBundle().getString("PAID_AMOUNT_EXCEEDS_DUE"));
				return "Error";
			}
			
			if (error == 2) {
				setErrorMessage(getResourceBundle().getString("RETURN_AMOUNT_EXCEED_PAID"));
				return "Error";
			}
			
			if (error == 3) {
				setErrorMessage(getResourceBundle().getString("RETURN_REBATE_EXCEED_PAID"));
				return "Error";
			}
		}
		
		getParentTuitionsByTuitionType();
		setSuccessMessage(getResourceBundle().getString("SAVED_SUCCESSFULLY"));
		return "";
	}
	
	public String getParentTuitionsByTuitionType() {
		if (StringUtils.isBlank(tuitionTypeName) || StringUtils.isBlank(year)) {
			setSuccessMessage("");
			setErrorMessage("Annee et Type de frais sont obligatoire");
			return "";
		}
		TuitionType tuitionType = (TuitionType) baseService.findByName(TuitionType.class, 
				tuitionTypeName, getCurrentUser().getSchool());
		SchoolYear schoolYear = (SchoolYear) baseService.findByColumn(
				SchoolYear.class, "year", year, baseService.getDefaultSchool());
		List<Integer> errors = new ArrayList<Integer>();
		baseService.getParentTuitionsByTuitionType(selectedStudents, parent, studentWithTuitionToPay,
				getCurrentUser().getSchool(), tuitionType.getId(), schoolYear.getId(), tuition, errors, getCurrentUser());
		return "";
	}
	
	
	public String printParentFinance() {

		try {

			Map<String, Serializable> parameters = new HashMap<String, Serializable>();
			if (parentId != null) {

				parameters.put("parentId", parentId);
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
						.getResourceAsStream("/reports/etatFinancier.jasper");

				ServletOutputStream ouputStream = response.getOutputStream();

				response.addHeader("Content-disposition",
						"attachment;filename=Etat Financier"+ "-"+getStringDate()+".pdf");

				JasperRunManager.runReportToPdfStream(reportStream,
						ouputStream, parameters, getConnection());
				response.setContentType("application/pdf");
				ouputStream.flush();
				ouputStream.close();
				context.responseComplete();
				parentId  = null;
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


	public String edit() {
		clearMessages();
		parent = (Parent) baseService.getById(Parent.class, getIdParameter());
		countryName = parent.getCountry() == null ? "" : parent.getCountry().getName();
		setPicture(parent.getImage());
		setStudentWithTuitionToPay(new ArrayList<Long>());
		selectedStudents = new ArrayList<Student>();
		List<BaseEntity> selecteds = baseService.loadAllByParentId(ParentStudent.class, "parent", "id", parent.getId());
		for (BaseEntity entity : selecteds) {
			ParentStudent ps = (ParentStudent)entity;
			selectedStudents.add(ps.getStudent());
		}
		selectedStudentsCount = new Long(selectedStudents.size());
		setSelectedTab("parentDetails");
		paymentId=0L;
		return "Success";
	}

	public void paintStudent(OutputStream stream, Object object)
			throws IOException {
		String studentId = getParameter("studentId");
		for (Student stud : selectedStudents) {
			if (stud.getId().toString().equals(studentId)) {
				if (stud.getImage() != null) {
					stream.write(stud.getImage());
				} else {
					stream.write(new byte[] {});
				}
			}
		}
	}
	
	public String openParentStudentDetails() {
		clearMessages();
		parent = (Parent) baseService.getById(Parent.class, getIdParameter());
		
		availableStudents = new ArrayList<Student>();
		selectedStudents = new ArrayList<Student>();
		List<Long> selectedIds = new ArrayList<Long>();
		List<BaseEntity> selecteds = baseService.loadAllByParentId(ParentStudent.class, "parent", "id", parent.getId());
		for (BaseEntity entity : selecteds) {
			ParentStudent ps = (ParentStudent)entity;
			ps.getStudent().setParentType(ps.getParentTypeId() != null ? ps.getParentTypeId().toString() : "");
			selectedStudents.add(ps.getStudent());
			selectedIds.add(ps.getStudent().getId());
		}
		
		
		return "Success";
	}
	
	public String searchStudents() {
		List<Student> availables = null;
		availableStudents = new ArrayList<Student>();
		selectedStudents = new ArrayList<Student>();
		List<Long> selectedIds = new ArrayList<Long>();
		List<BaseEntity> selecteds = baseService.loadAllByParentId(ParentStudent.class, "parent", "id", parent.getId());
		for (BaseEntity entity : selecteds) {
			ParentStudent ps = (ParentStudent)entity;
			ps.getStudent().setParentType(ps.getParentTypeId() != null ? ps.getParentTypeId().toString() : "");
			selectedStudents.add(ps.getStudent());
			selectedIds.add(ps.getStudent().getId());
		}
		
		if (boxChecked) {
			if (selectAllStudents){
				availables = baseService.searchStudents(selectAllStudents, className, year, 
						studentLastName, getCurrentUser().getSchool());
			}
		} else {
			availables = baseService.searchStudents(selectAllStudents, className, year,
					studentLastName, getCurrentUser().getSchool());
		}
		
		if (availables != null) {
			for (Student stud : availables) {
				if (!selectedIds.contains(stud.getId())) {
					availableStudents.add(stud);
				}
			}
		}

		searchRowCount = new Long(availableStudents != null ? availableStudents.size() : 0);
		return "Success";
	}
	
	public String assign() {
		clearMessages();
		try {
			if(parent != null){
				baseService.assignStudentsToParent(parent, selectedStudents, getCurrentUser());
				setSuccessMessage(getResourceBundle().getString("SAVED_SUCCESSFULLY"));

			}else{
				setErrorMessage(getResourceBundle().getString("MISSING_REQUIRED_FIELD"));
			}
			
		} catch (Exception ex) {
			ex.printStackTrace();
			setErrorMessage(ex, "Cette combinaison exist deja. ");
		}

		return "Success";
	}
	
	public String updateStudentSelection() {
		if (selectAllStudents) {
			boxChecked = true;
		} else {
			boxChecked = false;
		}
		return "Success";
	}
	
	public boolean isUserHasWriteAccess() {
		return isUserHasWriteAccess(MenuIdEnum.PARENT.getValue());
	}

	@PostConstruct
	private void getAll() {
		
		SchoolYear sy = baseService.getSchoolYear(new Date(),baseService.getDefaultSchool());
		year=sy==null?year:sy.getYear();
		//parents = baseService.loadAll(Parent.class, baseService.getDefaultSchool());
		//setRowCount(new Long(parents.size()));
	}
	
	public void search() {
		Parent p = new Parent();
		if (StringUtils.isNotBlank(lastName) || StringUtils.isNotBlank(firstName)) {
			p.setLastName(lastName);
			p.setFirstName(firstName);
			parents = baseService.searchParents(p, baseService.getDefaultSchool());
			setRowCount(new Long(parents.size()));
		}
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

	public List<BaseEntity> getParents() {
		return parents;
	}

	public void setParents(List<BaseEntity> parents) {
		this.parents = parents;
	}

	public Parent getParent() {
		return parent;
	}

	public void setParent(Parent parent) {
		this.parent = parent;
	}
	
	public String getCountryName() {
		return countryName;
	}

	public void setCountryName(String countryName) {
		this.countryName = countryName;
	}

	public boolean isParentSelected() {
		return parentSelected;
	}

	public void setParentSelected(boolean parentSelected) {
		this.parentSelected = parentSelected;
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

	public boolean isSelectAllStudents() {
		return selectAllStudents;
	}

	public void setSelectAllStudents(boolean selectAllStudents) {
		this.selectAllStudents = selectAllStudents;
	}

	public boolean isBoxChecked() {
		return boxChecked;
	}

	public void setBoxChecked(boolean boxChecked) {
		this.boxChecked = boxChecked;
	}

	public String getClassName() {
		return className;
	}

	public void setClassName(String className) {
		this.className = className;
	}

	public String getYear() {
		return year;
	}

	public void setYear(String year) {
		this.year = year;
	}

	public Long getSearchRowCount() {
		return searchRowCount;
	}

	public void setSearchRowCount(Long searchRowCount) {
		this.searchRowCount = searchRowCount;
	}

	public String getStudentLastName() {
		return studentLastName;
	}

	public void setStudentLastName(String studentLastName) {
		this.studentLastName = studentLastName;
	}

	public Long getSelectedStudentsCount() {
		return selectedStudentsCount;
	}

	public void setSelectedStudentsCount(Long selectedStudentsCount) {
		this.selectedStudentsCount = selectedStudentsCount;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public Tuition getTuition() {
		return tuition;
	}

	public void setTuition(Tuition tuition) {
		this.tuition = tuition;
	}

	public String getTuitionTypeName() {
		return tuitionTypeName;
	}

	public void setTuitionTypeName(String tuitionTypeName) {
		this.tuitionTypeName = tuitionTypeName;
	}

	public Double getNewPayment() {
		return newPayment;
	}

	public void setNewPayment(Double newPayment) {
		this.newPayment = newPayment;
	}

	public Double getNewRebate() {
		return newRebate;
	}

	public void setNewRebate(Double newRebate) {
		this.newRebate = newRebate;
	}

	public boolean isShowStudentTuitionsDetails() {
		Boolean showIt = tuitionCollapsibleToggleMap.get(getParameter("student_id"));
		return showIt;
	}

	public void setShowStudentTuitionsDetails(boolean showStudentTuitionsDetails) {
		this.showStudentTuitionsDetails = showStudentTuitionsDetails;
	}
	
	public String printReceipt() {

		try {

			Map<String, Serializable> parameters = new HashMap<String, Serializable>();
			
			if (parent != null&&parent.getTotalAmount()>0) {


				StringBuffer kidsName= new StringBuffer();
				StringBuffer kidsClass= new StringBuffer();
				StringBuffer kidsMatricule= new StringBuffer();
				List<StudentVO> kids = parent.getStudentVOs();
				
				if(kids!=null && kids.size()>0){
				int count=0;
				int size =kids.size();
				for(StudentVO sv:kids){
					kidsName.append(sv.getFirstName()+(count<size-1&&size>1?", ":""));
					kidsClass.append(sv.getLevelClassName()+(count<size-1&&size>1?", ":""));
					kidsMatricule.append(sv.getMatricule()+(count<size-1&&size>1?", ":""));
					count++;
				}
				
				//parameters.put("studentId", studentId);
				FacesContext context = FacesContext.getCurrentInstance();
				
				String reportsDirPath =( (ServletContext) context.getExternalContext().getContext()).getRealPath("/reports/");

				parameters.put("SUBREPORT_DIR", reportsDirPath+java.io.File.separator);
				HttpServletResponse response = (HttpServletResponse) context
						.getExternalContext().getResponse();
		
				parameters.put("parent", parent.getLastName());
				parameters.put("fullName", kidsName.toString());
				parameters.put("totalPaye", parent.getTotalPaid());
				parameters.put("matricule", kidsMatricule.toString());
				parameters.put("montant",  tuition.getAmount());
				parameters.put("comment", tuition.getComment());
				parameters.put("receivedBy", getCurrentUser().getFirstName()+" "+ getCurrentUser().getLastName());
				parameters.put("numero","No : "+ StringUtils.leftPad(paymentId.toString(), 8, "0") +" / "+year);
				parameters.put("raison", tuitionTypeName);
				parameters.put("reste", parent.getTotalBalance());
				parameters.put("schoolId", getCurrentUser().getSchool().getId());
				
				InputStream reportStream = context.getExternalContext()
						.getResourceAsStream("/reports/recuParent.jasper");

				ServletOutputStream ouputStream = response.getOutputStream();

				response.addHeader("Content-disposition",
						"attachment;filename=recu"+ "-"+getStringDate()+".pdf");

				JasperRunManager.runReportToPdfStream(reportStream,
						ouputStream, parameters, getConnection());
				response.setContentType("application/pdf");
				ouputStream.flush();
				ouputStream.close();
				context.responseComplete();
				}
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

}
