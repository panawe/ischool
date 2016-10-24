package com.esoft.ischool.restservice;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Serializable;
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

import org.richfaces.model.selection.SimpleSelection;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.esoft.ischool.model.BaseEntity;
import com.esoft.ischool.model.Payment;
import com.esoft.ischool.model.Relance;
import com.esoft.ischool.model.Student;
import com.esoft.ischool.model.Tuition;
import com.esoft.ischool.model.TuitionEnrollment;
import com.esoft.ischool.service.BaseService;
import com.esoft.ischool.util.MenuIdEnum;
import com.mysql.jdbc.StringUtils;

@Component("paymentBean")
@Scope("session")
public class PaymentBean extends BaseBean {

	@Autowired
	@Qualifier("baseService")
	private BaseService baseService;
	private Long rowCount;
	private List<BaseEntity> payments;
	private Double random;
	private Payment payment = new Payment();
	private Tuition tuition = new Tuition();
	private Integer numberOfDays = 0;
	private Double newPayment;
	private Double newRebate;
	private TuitionEnrollment tEnrollment;
	private Long overDueCount;
	private Relance relance;
	
	
	
	public Relance getRelance() {
		return relance;
	}

	public void setRelance(Relance relance) {
		this.relance = relance;
	}

	public Long getOverDueCount() {
		return overDueCount;
	}

	public void setOverDueCount(Long overDueCount) {
		this.overDueCount = overDueCount;
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

	private Student student = new Student();
	private List<Tuition> tuitions = new ArrayList<Tuition>();
	private List<TuitionEnrollment> tuitionDetails = new ArrayList<TuitionEnrollment>();
	private List<TuitionEnrollment> tuitionEnrollments = new ArrayList<TuitionEnrollment>();
	private SimpleSelection selectedReceipts = new SimpleSelection();
	
	
	public SimpleSelection getSelectedReceipts() {
		return selectedReceipts;
	}

	public void setSelectedReceipts(SimpleSelection selectedReceipts) {
		this.selectedReceipts = selectedReceipts;
	}

	public List<TuitionEnrollment> getTuitionDetails() {
		return tuitionDetails;
	}

	public void setTuitionDetails(List<TuitionEnrollment> tuitionDetails) {
		this.tuitionDetails = tuitionDetails;
	}

	@Override
	public Double getRandom() {
		return random;
	}

	public Tuition getTuition() {
		return tuition;
	}

	public void setTuition(Tuition tuition) {
		this.tuition = tuition;
	}

	@Override
	public void setRandom(Double random) {
		this.random = random;
	}

	public Student getStudent() {
		return student;
	}

	public void setStudent(Student student) {
		this.student = student;
	}

	public void paintStudent(OutputStream stream, Object object)
			throws IOException {
		if (student != null & student.getImage() != null) {
			stream.write(student.getImage());
		} else {
			stream.write(new byte[] {});
		}
	}

	public String initiatePayment() {
		for (Tuition t : tuitions) {
			if (getIdParameter().equals(t.getId())){
				tuition = t;
				break;
			}
		}
		return "Success";
	}

	public String savePayment() {
		clearMessages();
		if (tuition != null) {
			double due=tuition.getAmount()-(tuition.getPaid()==null?0:tuition.getPaid());
			if(due<newPayment+(newRebate==null?0:newRebate)){
				setErrorMessage(getResourceBundle().getString("PAID_AMOUNT_EXCEEDS_DUE"));
				return "Error";
			}
			payment = new Payment();
			payment.setTuition(tuition);
			payment.setStudentEnrollment(student.getCurrentEnrollment());
			payment.setPaymentDate(new Date());
			payment.setAmount(newPayment);
			payment.setRebate(newRebate);
			payment.setComment(tuition.getComment());
			baseService.save(payment,getCurrentUser());
			tuitions = baseService.getStudentPayments(student, getCurrentUser().getSchool());
			tuition = null;
			newPayment = null;

		}
		return "Success";
	}

	public String viewPayments() {

		for (Tuition t : tuitions) {
			if (getIdParameter().equals(t.getId())){
				tuition = t;
				break;
			}
		}
		
		tuitionDetails = baseService.getStudentPaymentsForTuition(student,tuition, getCurrentUser().getSchool());
		
		return "";
	}
	
	
	public String  prePrintReminder(){
		
		relance= new Relance();
		
		for (Tuition t : tuitions) {
			if (getIdParameter().equals(t.getId())){
				tuition = t;
				break;
			}
		}
		
		relance.setMontantPaye(tuition.getPaid());
		relance.setMontantDu(tuition.getAmount());
		relance.setDateLimite(tuition.getRemindDate());
		relance.setRemise(tuition.getRebate());
		relance.setTranche(tuition.getDescription());
		relance.setMontantRestant(tuition.getAmount()-(tuition.getPaid()==null?0.0:tuition.getPaid())-(tuition.getRebate()==null?0.0:tuition.getRebate()));
		
		if (student != null) {

			String classe="";
			try{
				classe=student.getCurrentEnrollment().getLevelClass().getName();
			}catch (Exception e){
				e.printStackTrace();
				//never mind
			}
			String fullName = student.getLastName().toUpperCase() + " "
					+ student.getFirstName();
			
		relance.setClasse(classe);
		relance.setStudentName(fullName);
		relance.setParentName(student.getLastName());
		relance.setMatricule(student.getMatricule());
		 
		relance.setMessage("A d�faut de payement � la date limite ci-dessus, votre enfant ne sera plus accept� en classe.\n"+
"La comptabilit� est ouverte tous les jours du lundi au vendredi de 7h � 11h 30 et de 15h � 17h. "+
"Le r�glement peut �tre effectu� "+
"en esp�ces CFA ou par ch�que CFA libell� � l�ordre de �"+student.getSchool().getName()+" �.\n");
		 
		}
		
		
		return "";
		
	}
	
	public String printReminder() {

		try {

			Map<String, Serializable> parameters = new HashMap<String, Serializable>();
			
	
			
				//parameters.put("studentId", studentId);
				FacesContext context = FacesContext.getCurrentInstance();
				
				String reportsDirPath =( (ServletContext) context.getExternalContext().getContext()).getRealPath("/reports/");

				parameters.put("SUBREPORT_DIR", reportsDirPath+java.io.File.separator);
				HttpServletResponse response = (HttpServletResponse) context
						.getExternalContext().getResponse();

				parameters.put("parentName", relance.getParentName());
				parameters.put("classe", relance.getClasse());
				parameters.put("montantDu", relance.getMontantDu());
				parameters.put("message",relance.getMessage());
				parameters.put("montantRestant", relance.getMontantRestant());
				parameters.put("montantPaye", relance.getMontantPaye());
				parameters.put("studentName", relance.getStudentName());
				parameters.put("matricule", relance.getMatricule());
				parameters.put("remise", relance.getRemise());
				parameters.put("tranche", relance.getTranche());
				parameters.put("dateLimite", relance.getDateLimite());
				parameters.put("schoolId", getCurrentUser().getSchool().getId());
				
				InputStream reportStream = context.getExternalContext()
						.getResourceAsStream("/reports/derniereRelanceStudent.jasper");

				ServletOutputStream ouputStream = response.getOutputStream();

				response.addHeader("Content-disposition",
						"attachment;filename=relance"+relance.getStudentName()+ "-"+getStringDate()+".pdf");

				JasperRunManager.runReportToPdfStream(reportStream,
						ouputStream, parameters, getConnection());
				response.setContentType("application/pdf");
				ouputStream.flush();
				ouputStream.close();
				context.responseComplete();
		 
		} catch (JRException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return "success";

	}

	
	public String getSelectedPayment(){
		
		Integer selected = getIdParameter().intValue();
		for(TuitionEnrollment te:tuitionDetails){
			if(te.getId()==selected){
				tEnrollment=te;
				break;
			}
		}
		
		return "Success";
	}

	public String getStudentPayments() {

		if (getSessionParameter("currentStudentId") != null) {
			student = (Student) baseService.getById(Student.class, new Long(
					getSessionParameter("currentStudentId").toString()));
			random = Math.random();
			tuitions = baseService.getStudentPayments(student, getCurrentUser().getSchool());
		}
		rowCount = tuitions == null ? 0 : new Long(tuitions.size());

		return "Success";
	}

	public String getOverDuePayments() {
		if (numberOfDays != null
				&& StringUtils.isNullOrEmpty(numberOfDays.toString()))
			numberOfDays = 0;

		tuitionEnrollments = baseService.getAllPaymentsDueInDays(
				TuitionEnrollment.class, numberOfDays);
		overDueCount=(long) (tuitionEnrollments==null?0:tuitionEnrollments.size());
		return "Success";
	}

	public String validate() {
		return "succes";
	}

	@Override
	public String clear() {
		payment = new Payment();
		return "Success";
	}

	public String delete() {
		clearMessages();
		try {
			baseService.delete(getIdParameter(), Payment.class);
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
		Long id = payment.getId();
		try {
			if (id == null || id == 0)
				baseService.save(payment,getCurrentUser());
			else
				baseService.update(payment,getCurrentUser());
			setSuccessMessage(getResourceBundle().getString("SAVED_SUCCESSFULLY"));
		} catch (Exception ex) {
			payment.setId(id);
			setErrorMessage(ex, "Ce payment existe deja");
		}

		clear();
		getAll();
		return "Success";
	}

	public List<Tuition> getTuitions() {
		return tuitions;
	}

	public void setTuitions(List<Tuition> tuitions) {
		this.tuitions = tuitions;
	}

	public String edit() {
		clearMessages();
		payment = (Payment) baseService
				.getById(Payment.class, getIdParameter());
		return "Success";
	}

	@PostConstruct
	private void getAll() {
		payments = baseService.loadAll(Payment.class,getCurrentUser().getSchool());
		setRowCount(new Long(payments.size()));

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

	public List<BaseEntity> getPayments() {
		return payments;
	}

	public void setPayments(List<BaseEntity> payments) {
		this.payments = payments;
	}

	public Payment getPayment() {
		return payment;
	}

	public void setPayment(Payment payment) {
		this.payment = payment;
	}

	public List<TuitionEnrollment> getTuitionEnrollments() {
		return tuitionEnrollments;
	}

	public void setTuitionEnrollments(List<TuitionEnrollment> tuitionEnrollments) {
		this.tuitionEnrollments = tuitionEnrollments;
	}

	public Integer getNumberOfDays() {
		return numberOfDays;
	}

	public void setNumberOfDays(Integer numberOfDays) {
		this.numberOfDays = numberOfDays;
	}
	
	public String printReceipt() {

		try {

			Map<String, Serializable> parameters = new HashMap<String, Serializable>();
			
			if (student != null) {

				String classe="";
				try{
					classe=student.getCurrentEnrollment().getLevelClass().getName();
				}catch (Exception e){
					e.printStackTrace();
					//never mind
				}
				String fullName = student.getLastName().toUpperCase() + " "
						+ student.getFirstName();
			
				//parameters.put("studentId", studentId);
				FacesContext context = FacesContext.getCurrentInstance();
				
				String reportsDirPath =( (ServletContext) context.getExternalContext().getContext()).getRealPath("/reports/");

				parameters.put("SUBREPORT_DIR", reportsDirPath+java.io.File.separator);
				HttpServletResponse response = (HttpServletResponse) context
						.getExternalContext().getResponse();

				parameters.put("fullName", fullName);
				parameters.put("classe", classe);
				parameters.put("montant",  tEnrollment.getPaid());
				parameters.put("comment", tEnrollment.getComment());
				parameters.put("receivedBy", tEnrollment.getReceivedBy());
				parameters.put("payDate", tEnrollment.getPayDate());
				parameters.put("raison", tEnrollment.getTuition().getDescription());
				parameters.put("matricule", student.getMatricule());
				parameters.put("rebate", tEnrollment.getRebate());
				parameters.put("schoolId", getCurrentUser().getSchool().getId());
				
				InputStream reportStream = context.getExternalContext()
						.getResourceAsStream("/reports/recu.jasper");

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
		} catch (JRException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return "success";

	}

	public boolean isUserHasWriteAccess() {
		return isUserHasWriteAccess(MenuIdEnum.PAYMENT_DUE.getValue());
	}

}
