package com.esoft.ischool.restservice;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.OutputStream;
import java.io.Serializable;
import java.lang.reflect.Method;
import java.math.BigInteger;
import java.security.InvalidKeyException;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.Security;
import java.security.interfaces.RSAPrivateKey;
import java.security.spec.RSAPrivateKeySpec;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.crypto.Cipher;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SealedObject;
import javax.faces.application.Application;
import javax.faces.component.html.HtmlPanelGrid;
import javax.faces.context.FacesContext;
import javax.servlet.ServletContext;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperRunManager;

import org.apache.commons.lang.StringUtils;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.richfaces.component.html.HtmlTab;
import org.richfaces.component.html.HtmlTabPanel;
import org.richfaces.event.UploadEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.esoft.ischool.job.Config;
import com.esoft.ischool.model.BaseEntity;
import com.esoft.ischool.model.Country;
import com.esoft.ischool.model.Course;
import com.esoft.ischool.model.Files;
import com.esoft.ischool.model.Grade;
import com.esoft.ischool.model.Level;
import com.esoft.ischool.model.LevelClass;
import com.esoft.ischool.model.Mark;
import com.esoft.ischool.model.Parent;
import com.esoft.ischool.model.ParentStudent;
import com.esoft.ischool.model.Payment;
import com.esoft.ischool.model.School;
import com.esoft.ischool.model.SchoolReligion;
import com.esoft.ischool.model.SchoolYear;
import com.esoft.ischool.model.Student;
import com.esoft.ischool.model.StudentEnrollment;
import com.esoft.ischool.model.StudentStatisticVO;
import com.esoft.ischool.model.Tuition;
import com.esoft.ischool.model.TuitionType;
import com.esoft.ischool.security.model.User;
import com.esoft.ischool.service.BaseService;
import com.esoft.ischool.util.Constants;
import com.esoft.ischool.util.License;
import com.esoft.ischool.util.MenuIdEnum;
import com.esoft.ischool.util.SimpleMail;
import com.esoft.ischool.util.Utils;
import com.esoft.ischool.vo.MarkVO;
import com.esoft.ischool.vo.StudentSearchVO;
import com.esoft.ischool.vo.StudentVO;

@Component("studentBean")
@Scope("session")
public class StudentBean extends BaseBean {

	@Autowired
	@Qualifier("baseService")
	private BaseService baseService;
	private Long rowCount;
	private List<StudentVO> students;
	private List<MarkVO> marks;
	private List<BaseEntity> bulletins;
	private String countryName = "Togo";
	private String fatherCountryName = "Togo";
	private String motherCountryName = "Togo";
	private String tutorCountryName;
	private String birthCountryName = "Togo";
	private List<Files> studentfiles;
	private String lastName;
	private String firstName;
	private String sexSearchCriteria;
	private String yearSearchCriteria;
	private String ageSearchCriteria;
	private String outcomeSearchCriteria;
	private StudentSearchVO studentSearch = new StudentSearchVO();
	private Short studentStatus;
	private Short status;
	private String table;
	private String order;
	private String selection;
	private String fullName;
	private boolean displayHeader;
	private String levelClassName;
	private String matricule;
	private boolean studentSelected = false;
	private boolean success = true;
	private Long studentId;
	private Long markCnt;
	private Long bullCnt;
	private Files files;
	private Long paymentId = 0L;
	private Integer maxStudents = 10000000;
	private List<Files> studentNewFiles;
	private Collection<StudentStatisticVO> countryStatistics;
	private Collection<StudentStatisticVO> religionStatistics;

	private List<Parent> availableParents = new ArrayList<Parent>();
	private List<Parent> selectedParents = new ArrayList<Parent>();
	private String parentFirstName;
	private String parentLastName;
	private String religionName;

	private String schoolName;

	private String levelName;

	private int numberOfBoys;
	private int numberOfGirls;
	private int averageAge;
	private int maximumAge;
	private int minimumAge;

	// scolarite variables
	private String travail;
	private String assiduite;
	private String motifDepart;
	private String conduite;

	private String testStyle;
	private Long parentCounts;
	private Long selectedParentCounts;

	private String year;
	private String tuitionTypeName;

	private void getMaxStudent() {
		try {
			FacesContext context = FacesContext.getCurrentInstance();
			String reportPath = ((ServletContext) context.getExternalContext()
					.getContext()).getRealPath("/license") + File.separator;

			String licenseFile = reportPath + "license.lic";

			Security.addProvider(new BouncyCastleProvider());

			Cipher cipher;

			cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding", "BC");

			KeyFactory keyFactory = KeyFactory.getInstance("RSA", "BC");

			// create the keys

			RSAPrivateKeySpec privKeySpec = new RSAPrivateKeySpec(
					new BigInteger(
							"b3f9a47ce4e363ab86922445caaf53395e6eba00d241f0b42fe225db0ae97c608c0747806737ff94e156db5d593d7281638f1b6aed5c428b8dc87ddf5297fa8dd9edf2c5ac9b7cdf658e6c756f77e05bd96d5783214153ecfbc9bb485e53b4e6a0058265e8a3a68469c22842a4e18bd994794904da1a7613ddd29c5bd9a13858308e51175e534166e68af3ea540bb72e98dfafc2c7e109e2cd966c0e1eb82e764c3986cdd27dd270a7a3bac8e7041be0106bf0428aae24b50a9465bce92d9f0c358101bcb36aaedeb057525e33cd9204a19b987036565db9ffb62459cb7e7c6bf77e35ad74cc681991a4fddbf49b6eda7d3ca932712288b392298b2c1d6d82d3",
							16),
					new BigInteger(
							"3d8e21cb31c1f0220769ce8c2c51a0e65b3d05d32816b38bfd609ccff9407870d113e049b383fc9f601f03f23e867cb67265ccdbda89169d8285d33f61916779c2d2d698f37b4ecf5d7dc3ecba8e46a7438b59461946adbcbd35771fbe5b64e108543a103eec5214b1d35d4fc5f2fa91156e12225db2753640453b8352c10872d4612ec3fa37e5dbd510bf09c869c9e873fd5055b2d05ed223646b7aa5da8f763770dbe6ffcad5a9dce5801e397640f63b4286441b3b5cad72af1679a8f864b867af80bebc4c8b39d9dd754c9f7c4d1a1fa5bbc09deef2a90a1a48b7ff0b8d87bc1d3077a2c809572450bf6e9f4033ba0c82d3a8e8a8709fb40788022f85fab9",
							16));

			RSAPrivateKey privKey = (RSAPrivateKey) keyFactory
					.generatePrivate(privKeySpec);

			// decryption step

			cipher.init(Cipher.DECRYPT_MODE, privKey);

			FileInputStream fileIn = new FileInputStream(licenseFile);
			ObjectInputStream in = new ObjectInputStream(fileIn);
			SealedObject ooo = (SealedObject) in.readObject();
			in.close();
			fileIn.close();

			License gg = (License) ooo.getObject(cipher);
			maxStudents = Integer.parseInt(gg.getNbrOfStudent());

			System.out.print("MAX STUDENT LICENCED=" + maxStudents);

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();

		}
	}

	public Long getPaymentId() {
		return paymentId;
	}

	public void setPaymentId(Long paymentId) {
		this.paymentId = paymentId;
	}

	public String getTravail() {
		return travail;
	}

	public void setTravail(String travail) {
		this.travail = travail;
	}

	public String getAssiduite() {
		return assiduite;
	}

	public void setAssiduite(String assiduite) {
		this.assiduite = assiduite;
	}

	public String getMotifDepart() {
		return motifDepart;
	}

	public void setMotifDepart(String motifDepart) {
		this.motifDepart = motifDepart;
	}

	public String getConduite() {
		return conduite;
	}

	public void setConduite(String conduite) {
		this.conduite = conduite;
	}

	public Files getFiles() {
		return files;
	}

	public void setFiles(Files files) {
		this.files = files;
	}

	public String getSchoolName() {
		return schoolName;
	}

	public String getLevelName() {
		return levelName;
	}

	public void setLevelName(String levelName) {
		this.levelName = levelName;
	}

	public void setSchoolName(String schoolName) {
		this.schoolName = schoolName;
	}

	public List<Files> getStudentfiles() {
		return studentfiles;
	}

	public void setStudentfiles(List<Files> studentfiles) {
		this.studentfiles = studentfiles;
	}

	public Long getBullCnt() {
		return bullCnt;
	}

	public void setBullCnt(Long bullCnt) {
		this.bullCnt = bullCnt;
	}

	public List<BaseEntity> getBulletins() {
		return bulletins;
	}

	public void setBulletins(List<BaseEntity> bulletins) {
		this.bulletins = bulletins;
	}

	public List<MarkVO> getMarks() {
		return marks;
	}

	public void setMarks(List<MarkVO> marks) {
		this.marks = marks;
	}

	public Long getMarkCnt() {
		return markCnt;
	}

	public void setMarkCnt(Long markCnt) {
		this.markCnt = markCnt;
	}

	public boolean isSuccess() {
		return success;
	}

	public void setSuccess(boolean success) {
		this.success = success;
	}

	public boolean isStudentSelected() {
		return studentSelected;
	}

	public void setStudentSelected(boolean studentSelected) {
		this.studentSelected = studentSelected;
	}

	public String getMatricule() {
		return matricule;
	}

	public void setMatricule(String matricule) {
		this.matricule = matricule;
	}

	public List<Files> getStudentNewFiles() {
		return studentNewFiles;
	}

	public void setStudentNewFiles(List<Files> studentNewFiles) {
		this.studentNewFiles = studentNewFiles;
	}

	private HtmlTabPanel tabPanel;

	public boolean isDisplayHeader() {
		return displayHeader;
	}

	public void setDisplayHeader(boolean displayHeader) {
		this.displayHeader = displayHeader;
	}

	public String getFullName() {
		return fullName;
	}

	public void setFullName(String fullName) {
		this.fullName = fullName;
	}

	public void pieceslistener(UploadEvent event) throws Exception {
		if (studentNewFiles == null) {
			studentNewFiles = new ArrayList<Files>();
		}
		Files files = new Files();
		files.setName(event.getUploadItem().getFileName());
		files.setContent(event.getUploadItem().getData());
		files.setStudent(student);
		studentNewFiles.add(files);
	}

	public String viewFile() {
		String fileId = (String) getRequestParameter("fileId");
		files = null;
		if (fileId != null) {
			files = (Files) baseService.getById(Files.class, new Long(fileId));
		}
		return "Success";
	}

	public String deleteFile() {
		String fileId = (String) getRequestParameter("fileId");
		if (fileId != null) {
			baseService.delete(new Long(fileId), Files.class);
			studentfiles = baseService.getStudentFiles(student);
		}
		return "Success";
	}

	public String viewFiles() {
		if (student != null)
			studentfiles = baseService.getStudentFiles(student);
		return "Success";
	}

	public void paintFile(OutputStream stream, Object object)
			throws IOException {
		if (files != null) {
			stream.write(files.getContent());
		} else {
			stream.write(new byte[] {});
		}
	}

	// map for selected stuff on the JSF page
	private Map<BaseEntity, Boolean> selectedStudents = new HashMap<BaseEntity, Boolean>();

	/* this is the list that will have the selected students */
	private List<BaseEntity> selectedStudentsList = null;

	public String getSearchedStudents() {

		if ("school".equals(getSessionParameter("link"))) {
			LevelClassBean bean = (LevelClassBean) getSessionParameter("levelClassBean");
			levelClassName = bean.getLevelClass().getName();
			if (studentStatus == null)
				studentStatus = 99;
			firstName = null;
			lastName = null;
			matricule = null;
		}

		if (StringUtils.isNotBlank(studentSearch.getLastName())
				|| StringUtils.isNotBlank(studentSearch.getFirstName())
				|| StringUtils.isNotBlank(studentSearch.getClassName())
				|| StringUtils.isNotBlank(studentSearch.getMatricule())
				|| StringUtils.isNotBlank(studentSearch.getSex())
				|| StringUtils.isNotBlank(studentSearch.getStatus())
				|| StringUtils.isNotBlank(studentSearch.getYear())
				|| studentSearch.getBeginAge() != null
				|| studentSearch.getEndAge() != null) {
			students = baseService.getStudents(Student.class, studentSearch,
					getCurrentUser());
		}
		rowCount = new Long((students != null) ? students.size() : 0);
		return "Success";
	}

	public String getStudentMarks() {
		if (student != null)
			marks = baseService.getMarks(student.getId());
		markCnt = marks == null ? 0L : marks.size();
		getStudentBulletins();
		return "success";
	}

	public String getStudentBulletins() {
		bulletins = baseService.getStudentBulletins(student);
		bullCnt = bulletins == null ? 0L : bulletins.size();
		return "success";
	}

	private void prepareSelectedList() {
		// reset the list
		setSelectedStudentsList(new ArrayList<BaseEntity>());
		for (BaseEntity student : getSelectedStudents().keySet()) {
			if (getSelectedStudents().get(student) == true) {
				// and this is the list of selected users
				getSelectedStudentsList().add(student);
			}
		}
	}

	Tuition tuition = null;

	public String initiatePayment() {
		tuition = new Tuition();
		setErrorMessage("");

		return "";
	}

	public String getStudentTuitionsByTuitionType() {
		if (StringUtils.isBlank(tuitionTypeName) || StringUtils.isBlank(year)) {
			setSuccessMessage("");
			setErrorMessage("Annee et Type de frais sont obligatoire");
			return "";
		}
		TuitionType tuitionType = (TuitionType) baseService.findByName(
				TuitionType.class, tuitionTypeName, getCurrentUser()
						.getSchool());
		SchoolYear schoolYear = (SchoolYear) baseService.findByColumn(
				SchoolYear.class, "year", year, baseService.getDefaultSchool());
		baseService.getStudentTuitionsByTuitionType(student, getCurrentUser()
				.getSchool(), tuitionType.getId(), schoolYear.getId());
		return "";
	}

	public String getStudentPaymentsByStudents() {
		SchoolYear defaultYear = student.getCurrentEnrollment().getSchoolYear();
		List<Long> studentIdList = new ArrayList<Long>();
		studentIdList.add(student.getId());
		SchoolYear schoolYear = null;
		if (StringUtils.isBlank(year))
			schoolYear = defaultYear;
		else
			schoolYear = (SchoolYear) baseService.findByColumn(
					SchoolYear.class, "year", year,
					baseService.getDefaultSchool());

		List<Integer> errors = new ArrayList<Integer>();

		baseService.getStudentPaymentsByTuitionTypes(student, studentIdList,
				getCurrentUser().getSchool(), schoolYear.getId(), errors);

		return "";
	}

	public String savePaymentsForStudentByTuitionType() {
		setErrorMessage("");
		setSuccessMessage("");
		TuitionType tuitionType = (TuitionType) baseService.findByName(
				TuitionType.class, tuitionTypeName, getCurrentUser()
						.getSchool());
		SchoolYear schoolYear = (SchoolYear) baseService.findByColumn(
				SchoolYear.class, "year", year, baseService.getDefaultSchool());
		List<Integer> errors = new ArrayList<Integer>();
		List<Long> studentIdList = new ArrayList<Long>();
		studentIdList.add(student.getId());

		Payment pay = null;
		if (tuition.getAmount() >= 0)
			pay = baseService.saveStudentsPaymentsByTuitionType(studentIdList,
					getCurrentUser().getSchool(), tuitionType.getId(),
					schoolYear.getId(), tuition, errors, getCurrentUser());
		else
			pay = baseService.saveStudentsNegativePaymentsByTuitionType(
					studentIdList, getCurrentUser().getSchool(),
					tuitionType.getId(), schoolYear.getId(), tuition, errors,
					getCurrentUser());

		for (Integer error : errors) {
			if (error == 1) {
				setErrorMessage(getResourceBundle().getString(
						"PAID_AMOUNT_EXCEEDS_DUE"));
				return "Error";
			}

			if (error == 2) {
				setErrorMessage(getResourceBundle().getString(
						"RETURN_AMOUNT_EXCEED_PAID"));
				return "Error";
			}

			if (error == 3) {
				setErrorMessage(getResourceBundle().getString(
						"RETURN_REBATE_EXCEED_PAID"));
				return "Error";
			}
		}

		if (pay != null)
			paymentId = pay.getId();

		getStudentTuitionsByTuitionType();
		getStudentPaymentsByStudents();
		setSuccessMessage(getResourceBundle().getString("SAVED_SUCCESSFULLY"));
		return "";
	}

	private Student student = new Student();

	public String validate() {
		return "succes";
	}

	@Override
	public String clear() {
		student = new Student();
		countryName = "";
		birthCountryName = "";
		fatherCountryName = "";
		motherCountryName = "";
		tutorCountryName = "";
		fullName = "";
		religionName = "";
		schoolName = "";
		levelName = "";
		studentfiles = null;
		files = null;
		studentSelected = false;
		setPicture(null);
		return "Success";
	}

	@PostConstruct
	public String getLoggedStudent() {
		try {
			getMaxStudent();
			SchoolYear sy = baseService.getSchoolYear(new Date(),
					baseService.getDefaultSchool());
			year = sy == null ? year : sy.getYear();
			Student stud = baseService.getStudent(getCurrentUser());

			if (stud != null) {
				student = stud;
				students = new ArrayList<StudentVO>();
				students.add(baseService.getStudentVOFromStudent(student));
				rowCount = 1L;
				studentSelected = true;
				String classe = "";
				try {
					classe = student.getCurrentEnrollment() == null ? ""
							: student.getCurrentEnrollment().getLevelClass()
									.getName();
				} catch (Exception e) {
					e.printStackTrace();
					// never mind
				}
				fullName = student.getLastName().toUpperCase()
						+ " "
						+ student.getFirstName()
						+ (classe == null || classe.equals("") ? "" : " | "
								+ classe);
				setSessionAttribute("currentStudentId", student.getId());
			}
		} catch (Exception e) {
			e.printStackTrace();
			return "error";
		}
		return "success";
	}

	public String delete() {
		clearMessages();
		try {
			Long idParam = getIdParameter();
			baseService.delete(getIdParameter(), Student.class);
			int i = 0;
			for (StudentVO be : students) {
				if (be.getId().equals(idParam)) {
					students.remove(i);
					break;
				}
				i++;
			}
			setSuccessMessage(getResourceBundle().getString(
					"DELETE_SUCCESSFULLY"));
			studentSelected = false;
			if (idParam.equals(getSessionParameter("currentStudentId")))
				setSessionAttribute("currentStudentId", null);
		} catch (Exception ex) {
			if (ex.getMessage() != null
					&& ex.getCause().getCause().getMessage()
							.contains("Cannot delete or update a parent row"))
				setErrorMessage("La suppression de l'etudiant n'a pas marche parce qu'il a des donnees dans le system");
			else
				setErrorMessage(getResourceBundle().getString(
						"DELETE_UNSUCCESSFULL"));
		}

		return "Success";
	}

	private String sendDecisionMessage(String statusText) {
		String message = "<p>Bonjour "
				+ student.getUser().getFirstName()
				+ ",</p><p>"
				+

				((student.getDecision() == null || student.getDecision().trim()
						.equals("")) ? " Decision: " + student.getStatusText()
						: student.getDecision()) + " <br/>" + "Remerciements."
				+ "<br><strong>Le Directeur</strong></p>";

		try {
			FacesContext context = FacesContext.getCurrentInstance();
			ApplicationContext ctx = org.springframework.web.jsf.FacesContextUtils
					.getWebApplicationContext(context);
			Config config = (Config) ctx.getBean("config");
			SimpleMail.sendMail(statusText, message, config
					.getConfigPropertyValue("SCHOOL_SENDER_EMAIL"), student
					.getEmail(), config
					.getConfigPropertyValue("SCHOOL_SMTP_SERVER"), config
					.getConfigPropertyValue("SCHOOL_MAIL_SERVER_USER"), config
					.getConfigPropertyValue("SCHOOL_MAIL_SERVER_PASSWORD"));
		} catch (Exception e) {

		}

		return message;

	}

	public String accept() {
		status = TEMP_ACCEPTED_STUDENT;
		String returnV = insert();
		if (returnV == null
				|| (returnV != null && !returnV.equalsIgnoreCase("ERROR"))) {
			sendDecisionMessage(TEXT_TEMP_ACCEPTED_STUDENT);
		}
		return returnV;
	}

	public String acceptFinal() {
		status = ACTIVE_STATUS;
		String returnV = insert();
		if (returnV == null
				|| (returnV != null && !returnV.equalsIgnoreCase("ERROR"))) {
			sendDecisionMessage(TEXT_ACTIVE_STATUS);
		}
		return returnV;
	}

	public String reject() {
		status = TEMP_REJECTED_STUDENT;
		String returnV = insert();
		if (returnV == null
				|| (returnV != null && !returnV.equalsIgnoreCase("ERROR"))) {
			sendDecisionMessage(TEXT_TEMP_REJECTED_STUDENT);
		}
		return returnV;
	}

	public String insert() {
		clearMessages();

		// Check license

/*		Integer studentCount = baseService.countActiveStudent();
		if (studentCount > maxStudents) {
			setErrorMessage("Votre license vous authorise seulement "
					+ maxStudents
					+ " eleves. Vous en avez actuellement "
					+ studentCount
					+ "\n"
					+ " Veuillez soit acheter plus d'utilisateurs soit rendre inactifs les eleves qui ne sont plus dans l'etablissement pour liberer la place.");
			return "ERROR";
		}
*/
		Long id = student.getId();
		try {

			Country country = (Country) baseService.findByColumn(Country.class,
					"name", countryName);
			if (country == null) {
				setSuccessMessage(getResourceBundle().getString(
						"RESIDENCE_COUNTRY_NOT_FOUND"));
				return "ERROR";
			}
			student.setCountry(country);

			Country birthCountry = (Country) baseService.findByColumn(
					Country.class, "name", birthCountryName);
			if (birthCountry == null) {
				setSuccessMessage(getResourceBundle().getString(
						"BIRTH_COUNTRY_NOT_FOUND"));
				return "ERROR";
			}
			student.setBirthCountry(birthCountry);

			SchoolReligion religion = (SchoolReligion) baseService.findByName(
					SchoolReligion.class, religionName,
					baseService.getDefaultSchool());

			/*
			 * if (religion == null) { setErrorMessage(getErrorMessage() + ". "
			 * + getResourceBundle().getString("RELIGION_NOT_FOUND")); return
			 * "ERROR"; }
			 */

			if (student.getBirthDate().after(new Date())) {
				setErrorMessage(getErrorMessage() + ". "
						+ getResourceBundle().getString("BIRTH_IS_FUTURE"));
				return "ERROR";
			}

			if (status != null) {
				student.setStatus(status);
			}
			student.setReligion(religion);
			boolean online = false;
			// get picture
			if (getPicture() != null) {
				student.setImage(getPicture());
				if (student.getImage().length > 1048576) {
					setErrorMessage(MAX_SIZE_EXCEEDED);
					return "ERROR";
				}
			}

			if (id == null || id == 0) {
				if (getCurrentUser() == null
						|| getCurrentUser().getSchool().getGenerateMatricule()) {
					student.setMatricule(baseService.generateMatricule(
							Student.class, student.getFirstName(),
							student.getLastName(), student.getBirthDate()));
				}
				if (student != null && student.getMatricule() != null) {
					BaseEntity be = baseService.findByColumn(Student.class,
							"matricule", student.getMatricule());
					if (be != null) {
						setErrorMessage(MATRICULE_EXISTS);
						return "ERROR";
					}
				}
				FacesContext context = FacesContext.getCurrentInstance();
				ApplicationContext ctx = org.springframework.web.jsf.FacesContextUtils
						.getWebApplicationContext(context);
				Config config = (Config) ctx.getBean("config");

				if (getCurrentUser() != null) {
					baseService.saveStudent(student, config, getCurrentUser());
					if (selectedParents != null && selectedParents.size() > 0)
						baseService.assignParentsToStudent(student,
								selectedParents, getCurrentUser());
				} else {
					online = true;
					School school = (School) baseService
							.findSchoolByName(schoolName);
					if (school == null) {
						setErrorMessage(getErrorMessage()
								+ getResourceBundle().getString(
										"INVALID_SCHOOL"));
						return "ERROR";
					}

					Level level = (Level) baseService.findByColumn(Level.class,
							"name", levelName);
					if (level == null) {
						setErrorMessage(getErrorMessage()
								+ getResourceBundle()
										.getString("INVALID_LEVEL"));
						return "ERROR";
					}
					student.setLevel(level);
					if (student.getEmail() == null) {
						setErrorMessage(getErrorMessage()
								+ getResourceBundle().getString(
										"E_MAIL_REQUIRED"));
						return "ERROR";
					}

					student.setSchool(school);
					student.setStatus(TEMP_STUDENT);
					// setDefaultUser( baseService.getAdminUser(school));
					setSessionAttribute("user",
							baseService.getAdminUser(school));
					baseService.saveStudent(student, config, getCurrentUser());

					if (studentNewFiles != null) {
						for (Files files : studentNewFiles) {
							if (files.getContent() != null) {
								if (files.getContent().length > 1048576) {
									setErrorMessage(MAX_SIZE_EXCEEDED + " : "
											+ files.getName());
									studentNewFiles = null;
									return "ERROR";
								}
							}

							baseService.save(files, getCurrentUser());
						}
					}
					User stUser = (User) baseService.findByColumn(User.class,
							"userName", student.getMatricule());
					String message = null;
					try {

						if (config != null) {
							message = createWelcomeMessage(
									config.getConfigPropertyValue("SCHOOL_WEBSITE"),
									stUser, school);
							SimpleMail
									.sendMail(
											Constants.REGISTRATION_RECEIVED,
											message,
											config.getConfigPropertyValue("SCHOOL_SENDER_EMAIL"),
											stUser.getEmail(),
											config.getConfigPropertyValue("SCHOOL_SMTP_SERVER"),
											config.getConfigPropertyValue("SCHOOL_MAIL_SERVER_USER"),
											config.getConfigPropertyValue("SCHOOL_MAIL_SERVER_PASSWORD"));
							setSuccessMessage(message);
						} else {
							setSuccessMessage(message);
						}
					} catch (Exception e) {
						e.printStackTrace();
						setSuccessMessage(message);
					}
					removeSessionAttribute("user");
				}
			} else {

				if (studentNewFiles != null) {
					for (Files files : studentNewFiles) {
						if (files.getContent() != null) {
							if (files.getContent().length > 1048576) {
								setErrorMessage(MAX_SIZE_EXCEEDED + " : "
										+ files.getName());
								studentNewFiles = null;
								return "ERROR";
							}
						}

						baseService.save(files, getCurrentUser());
					}
				}
				baseService.update(student, getCurrentUser());

			}
			if (!online) {
				setSessionAttribute("currentStudentId", student.getId());
				studentSelected = true;
				String classe = "";
				try {
					classe = student.getCurrentEnrollment() == null ? ""
							: student.getCurrentEnrollment().getLevelClass()
									.getName();
				} catch (Exception e) {
					e.printStackTrace();
					// never mind
				}
				fullName = student.getLastName().toUpperCase()
						+ " "
						+ student.getFirstName()
						+ (classe == null || classe.equals("") ? "" : " | "
								+ classe);
				displayHeader = true;
				setSuccessMessage(getResourceBundle().getString(
						"SAVED_SUCCESSFULLY"));
			} else {
				clear();
			}

		} catch (Exception ex) {
			student.setId(id);
			setErrorMessage(ex, "Cet etudiant exist deja. ");
			ex.printStackTrace();
			return "ERROR";

		} finally {
			status = null;
			if (student != null && student.getId() != null
					&& student.getId() > 0)
				studentfiles = baseService.getStudentFiles(student);
			studentNewFiles = null;
		}

		return "Success";
	}

	private String createWelcomeMessage(String url, User user, School school) {
		String message = "<p>Bonjour "
				+ user.getFirstName()
				+ ",</p><p>"
				+ "Nous sommes ravis de vous informer que nous avons bien recu votre demande d'inscription.<br/>"
				+ "Nous allons etudier votre dossier et vous tenir informer de la decision.<br/>"
				+ "Vous pouvez vous connecter pour modifier votre dossier ou consulter la progression de son etude"
				+ "<br>Pour vous y connecter rendez-vous a l'addresse suivante: "
				+ url + ".<br/>" + "Votre nom d'utilisateur est: "
				+ user.getUserName() + "\n" + "Votre mot de passe est: "
				+ user.getPassword() + "<br/>" + "Remerciements."
				+ "<br><strong>Le Directeur</strong></p>";

		return message;

	}

	public String selectStudent() {
		studentId = getIdParameter();
		return "success";
	}

	public String importStudentInfo() {
		clearMessages();
		try {
			success = true;
			Student st = (Student) baseService
					.getById(Student.class, studentId);
			st.setSchool(getCurrentUser().getSchool());
			st.setStatus((short) 1);
			baseService.update(st, getCurrentUser());
			student = st;
			// update the current list
			for (StudentVO stt : students) {
				if (stt.getId().equals(st.getId())) {
					((StudentVO) stt).setStatus((short) 1);
					((StudentVO) stt).setSchoolName(getCurrentUser()
							.getSchool().getName());
					break;
				}
			}
			setSuccessMessage(getResourceBundle().getString(
					"SAVED_SUCCESSFULLY"));
		} catch (Exception e) {
			e.printStackTrace();
			success = false;
			setErrorMessage(e.getMessage());
		}
		return "success";
	}

	public String freeupStudent() {
		clearMessages();
		try {
			success = true;
			Student st = (Student) baseService
					.getById(Student.class, studentId);
			if (st.getSchool().equals(getCurrentUser().getSchool())) {
				st.setStatus((short) 0);
				baseService.update(st, getCurrentUser());
			} else {
				success = false;
				setErrorMessage(CANNOT_CHANGE_STUDENT_STATUS
						+ (st.getSchool().getPhone() != null
								&& !st.getSchool().getPhone().equals("") ? ". \nVeuillez contacter son etablissement actuel au numero "
								+ st.getSchool().getPhone()
								+ " pour obtenir sa liberation"
								: ""));
				return "error";
			}
			// update the current list
			for (StudentVO stt : students) {
				if (stt.getId().equals(st.getId())) {
					((StudentVO) stt).setStatus((short) 0);
					break;
				}
			}
			setSuccessMessage(getResourceBundle().getString(
					"SAVED_SUCCESSFULLY"));
		} catch (Exception e) {
			e.printStackTrace();
			setErrorMessage(e.getMessage());
			success = false;
		}
		return "success";
	}

	public String edit() {
		clearMessages();
		setSelectedTab("studentDetails");
		student = (Student) baseService
				.getById(Student.class, getIdParameter());
		String classe = "";
		ParentBean bean = (ParentBean) getSessionParameter("parentBean");
		if (bean != null)
			bean.setSelectedTab("menuStudentDetails");

		try {
			classe = student.getCurrentEnrollment() == null ? "" : student
					.getCurrentEnrollment().getLevelClass().getName();
		} catch (Exception e) {
			e.printStackTrace();
			// never mind
		}
		studentfiles = baseService.getStudentFiles(student);
		religionName = student.getReligion() == null ? null : student
				.getReligion().getName();
		levelName = student.getLevel() == null ? "" : student.getLevel()
				.getName();
		fullName = student.getLastName().toUpperCase() + " "
				+ student.getFirstName()
				+ (classe == null || classe.equals("") ? "" : " | " + classe);
		displayHeader = true;
		setSessionAttribute("currentStudentId", getIdParameter());

		countryName = student.getCountry() == null ? "" : student.getCountry()
				.getName();
		birthCountryName = student.getBirthCountry() == null ? "" : student
				.getBirthCountry().getName();

		setPicture(student.getImage());

		studentSelected = true;
		paymentId = 0L;
		searchParents();
		return "Success";
	}

	public void intit() {
		if (student != null) {

			schoolName = student.getSchool().getName();
			levelName = student.getLevel() == null ? "" : student.getLevel()
					.getName();
			religionName = student.getReligion() == null ? null : student
					.getReligion().getName();
			countryName = student.getCountry() == null ? "" : student
					.getCountry().getName();
			birthCountryName = student.getBirthCountry() == null ? "" : student
					.getBirthCountry().getName();
			studentfiles = baseService.getStudentFiles(student);
			setPicture(student.getImage());

		}

	}

	public String openTabs() {
		return "tabs";
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

	public String getCountryName() {
		return countryName;
	}

	public void setCountryName(String countryName) {
		this.countryName = countryName;
	}

	public String getBirthCountryName() {
		return birthCountryName;
	}

	public void setBirthCountryName(String birthCountryName) {
		this.birthCountryName = birthCountryName;
	}

	public List<StudentVO> getStudents() {

		if ("school".equals(getSessionParameter("link"))) {
			LevelClassBean bean = (LevelClassBean) getSessionParameter("levelClassBean");
			levelClassName = bean.getLevelClass().getName();
			Map<Long, StudentStatisticVO> countryMap = new HashMap<Long, StudentStatisticVO>();
			Map<Long, StudentStatisticVO> religionMap = new HashMap<Long, StudentStatisticVO>();
			if (StringUtils.isNotBlank(levelClassName)
					&& (studentSearch.getClassName() == null || (studentSearch != null
							&& studentSearch.getClassName() != null && !studentSearch
							.getClassName().equals(levelClassName)))) {
				studentSearch = new StudentSearchVO();
				studentSearch.setClassName(levelClassName);
				students = baseService.getStudents(Student.class,
						studentSearch, getCurrentUser());
				rowCount = (long) (students == null ? 0 : students.size());
				numberOfBoys = 0;
				numberOfGirls = 0;
				int totalAge = 0;
				maximumAge = 0;
				minimumAge = 0;

				for (StudentVO entity : students) {
					StudentVO student = (StudentVO) entity;
					if ("M".equals(student.getSex()))
						numberOfBoys++;
					else
						numberOfGirls++;

					totalAge = totalAge + Utils.getAge(student.getBirthDate());

					if (maximumAge < Utils.getAge(student.getBirthDate()))
						maximumAge = Utils.getAge(student.getBirthDate());

					if (minimumAge == 0
							|| minimumAge > Utils
									.getAge(student.getBirthDate()))
						minimumAge = Utils.getAge(student.getBirthDate());

					setStatisticMap(countryMap, student);
					setReligionStatisticMap(religionMap, student);
				}

				averageAge = numberOfBoys + numberOfGirls > 0 ? totalAge
						/ (numberOfBoys + numberOfGirls) : 0;

				countryStatistics = new ArrayList<StudentStatisticVO>();
				for (StudentStatisticVO stu : countryMap.values())
					countryStatistics.add(stu);

				religionStatistics = new ArrayList<StudentStatisticVO>();
				for (StudentStatisticVO stu : religionMap.values())
					religionStatistics.add(stu);

			}
		}

		return students;
	}

	private void setStatisticMap(Map<Long, StudentStatisticVO> map,
			StudentVO student) {
		if (map.get(student.getCountryId()) == null) {
			StudentStatisticVO studentStatistic = new StudentStatisticVO();
			studentStatistic.setName(student.getCountryName());
			studentStatistic.setValue(1);
			map.put(student.getCountryId(), studentStatistic);
		} else {
			StudentStatisticVO studentStatistic = map.get(student
					.getCountryId());
			studentStatistic.setValue(studentStatistic.getValue() + 1);
			map.put(student.getCountryId(), studentStatistic);
		}
	}

	private void setReligionStatisticMap(Map<Long, StudentStatisticVO> map,
			StudentVO student) {
		if (map.get(student.getReligionId()) == null) {
			StudentStatisticVO studentStatistic = new StudentStatisticVO();
			studentStatistic.setName(student.getReligionName());
			studentStatistic.setValue(1);
			map.put(student.getReligionId(), studentStatistic);
		} else {
			StudentStatisticVO studentStatistic = map.get(student
					.getReligionId());
			studentStatistic.setValue(studentStatistic.getValue() + 1);
			map.put(student.getReligionId(), studentStatistic);
		}
	}

	public String assignParentsToStudent() {
		clearMessages();
		try {
			if (student != null && student.getId() != null
					&& student.getId() > 0) {
				baseService.assignParentsToStudent(student, selectedParents,
						getCurrentUser());
				searchParents();
				setSuccessMessage(getResourceBundle().getString(
						"SAVED_SUCCESSFULLY"));
			} else {
				setSuccessMessage("Sauvegarder l'etudiant pour finaliser l'assignation du parent");
			}

		} catch (Exception ex) {
			ex.printStackTrace();
			setErrorMessage(ex, "Cette combinaison exist deja. ");
		}

		return "Success";
	}

	public void setStudents(List<StudentVO> students) {
		this.students = students;
	}

	public String getFatherCountryName() {
		return fatherCountryName;
	}

	public void setFatherCountryName(String fatherCountryName) {
		this.fatherCountryName = fatherCountryName;
	}

	public String getMotherCountryName() {
		return motherCountryName;
	}

	public void setMotherCountryName(String motherCountryName) {
		this.motherCountryName = motherCountryName;
	}

	public String getTutorCountryName() {
		return tutorCountryName;
	}

	public void setTutorCountryName(String tutorCountryName) {
		this.tutorCountryName = tutorCountryName;
	}

	public Student getStudent() {
		return student;
	}

	public void setStudent(Student student) {
		this.student = student;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public Map<BaseEntity, Boolean> getSelectedStudents() {
		return selectedStudents;
	}

	public void setSelectedStudents(Map<BaseEntity, Boolean> selectedStudents) {
		this.selectedStudents = selectedStudents;
	}

	public List<BaseEntity> getSelectedStudentsList() {
		return selectedStudentsList;
	}

	public void setSelectedStudentsList(List<BaseEntity> selectedStudentsList) {
		this.selectedStudentsList = selectedStudentsList;
	}

	public String getTable() {
		return table;
	}

	public void setTable(String table) {
		this.table = table;
	}

	public String getOrder() {
		return order;
	}

	public void setOrder(String order) {
		this.order = order;
	}

	public String getSelection() {
		return selection;
	}

	public void setSelection(String selection) {
		this.selection = selection;
	}

	public String getLevelClassName() {
		return levelClassName;
	}

	public void setLevelClassName(String levelClassName) {
		this.levelClassName = levelClassName;
	}

	public HtmlTabPanel getTabPanel() {
		Application application = FacesContext.getCurrentInstance()
				.getApplication();
		HtmlTab tab = (HtmlTab) application
				.createComponent(HtmlTab.COMPONENT_TYPE);
		tab.setLabel("Nice Tab # ");
		tab.setName(1);
		tabPanel.getChildren().add(tab);
		return tabPanel;
	}

	public boolean isUserHasWriteAccess() {
		return isUserHasWriteAccess(MenuIdEnum.STUDENT.getValue());
	}

	private HtmlPanelGrid panelGrid;

	public void setPanelGrid(HtmlPanelGrid panelGrid) {
		this.panelGrid = panelGrid;
	}

	public void setTabPanel(HtmlTabPanel tabPanel) {
		this.tabPanel = tabPanel;
	}

	public String printStudentDetails() {

		try {

			Map<String, Serializable> parameters = new HashMap<String, Serializable>();

			if (studentId != null) {

				parameters.put("studentId", studentId);
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
						.getResourceAsStream("/reports/studentDetails.jasper");

				ServletOutputStream ouputStream = response.getOutputStream();

				response.addHeader("Content-disposition",
						"attachment;filename=Resultats de test" + "-"
								+ getStringDate() + ".pdf");

				JasperRunManager.runReportToPdfStream(reportStream,
						ouputStream, parameters, getConnection());
				response.setContentType("application/pdf");
				ouputStream.flush();
				ouputStream.close();
				context.responseComplete();
				studentId = null;
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

	public String printScolarite() {

		try {

			Map<String, Serializable> parameters = new HashMap<String, Serializable>();

			if (studentId != null) {

				parameters.put("studentId", studentId);
				parameters
						.put("schoolId", getCurrentUser().getSchool().getId());
				parameters.put("travail", travail);
				parameters.put("assiduite", assiduite);
				parameters.put("motifDepart", motifDepart);
				parameters.put("conduite", conduite);

				FacesContext context = FacesContext.getCurrentInstance();

				String reportsDirPath = ((ServletContext) context
						.getExternalContext().getContext())
						.getRealPath("/reports/");

				parameters.put("SUBREPORT_DIR", reportsDirPath
						+ java.io.File.separator);
				HttpServletResponse response = (HttpServletResponse) context
						.getExternalContext().getResponse();

				InputStream reportStream = context.getExternalContext()
						.getResourceAsStream(
								"/reports/certificatScolarite.jasper");

				ServletOutputStream ouputStream = response.getOutputStream();

				response.addHeader("Content-disposition",
						"attachment;filename=Certificat de scolarite.pdf");

				JasperRunManager.runReportToPdfStream(reportStream,
						ouputStream, parameters, getConnection());
				response.setContentType("application/pdf");
				ouputStream.flush();
				ouputStream.close();
				context.responseComplete();
				studentId = null;
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

			if (studentId != null) {

				parameters.put("studentId", studentId);
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
						.getResourceAsStream("/reports/idcard.jasper");

				ServletOutputStream ouputStream = response.getOutputStream();

				response.addHeader("Content-disposition",
						"attachment;filename=Carte d'identite" + "-"
								+ getStringDate() + ".pdf");

				JasperRunManager.runReportToPdfStream(reportStream,
						ouputStream, parameters, getConnection());
				response.setContentType("application/pdf");
				ouputStream.flush();
				ouputStream.close();
				context.responseComplete();
				studentId = null;
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

	public void uploadStudentData(UploadEvent event) throws Exception {
		FileInputStream file = null;
		File tempFile = null;
		Row row = null;
		Cell cell = null;
		try {
			// Check license

			Integer studentCount = baseService.countActiveStudent();
			if (studentCount > maxStudents) {
				setErrorMessage("Votre license vous authorise seulement "
						+ maxStudents
						+ " eleves. Vous en avez actuellement "
						+ studentCount
						+ "\n"
						+ " Veuillez soit acheter plus d'utilisateurs soit rendre inactifs les eleves qui ne sont plus dans l'etablissement pour liberer la place.");

			} else {
				byte[] content = event.getUploadItem().getData();
				ServletContext ctx = (ServletContext) getContext()
						.getExternalContext().getContext();
				File tempDir = new File(ctx.getRealPath("") + "/temp");
				if(!tempDir.exists()){
					tempDir.mkdir();
				}
				tempFile = File.createTempFile("studentData", ".xlsx", tempDir);

				OutputStream out = new FileOutputStream(tempFile);
				out.write(content);
				out.close();

				Map<Integer, String> methodMap = new HashMap<Integer, String>();
				methodMap.put(1, "setMatricule");
				methodMap.put(2, "setLastName");
				methodMap.put(3, "setFirstName");
				methodMap.put(4, "setSex");
				methodMap.put(5, "setBirthDate");
				methodMap.put(6, "setBirthCity");
				methodMap.put(7, "setBirthCountryName");
				methodMap.put(8, "setCity");
				methodMap.put(9, "setCountryName");
				methodMap.put(10, "setAddress");
				methodMap.put(11, "setFatherLastName");
				methodMap.put(12, "setFatherFirstName");
				methodMap.put(13, "setMotherLastName");
				methodMap.put(14, "setMotherFirstName");
				methodMap.put(15, "setLevelClassName");
				methodMap.put(16, "setSchoolYearName");
				methodMap.put(17, "setMiddleName");
				methodMap.put(18, "setNickName");
				methodMap.put(19, "setEmail");
				methodMap.put(20, "setPhone");
				methodMap.put(21, "setCellPhone");
				methodMap.put(22, "setSchoolReligionName");
				methodMap.put(23, "setFatherProfession");
				methodMap.put(24, "setFatherWorkPlace");
				methodMap.put(25, "setFatherAddress");
				methodMap.put(26, "setFatherCity");
				methodMap.put(27, "setFatherCountry");
				methodMap.put(28, "setFatherEmail");
				methodMap.put(29, "setFatherPhone");
				methodMap.put(30, "setMotherProfession");
				methodMap.put(31, "setMotherWorkPlace");
				methodMap.put(32, "setMotherAddress");
				methodMap.put(33, "setMotherCity");
				methodMap.put(34, "setMotherCountry");
				methodMap.put(35, "setMotherEmail");
				methodMap.put(36, "setMotherPhone");
				methodMap.put(37, "setTutorLastName");
				methodMap.put(38, "setTutorFirstName");
				methodMap.put(39, "setTutorAddress");
				methodMap.put(40, "setTutorCity");
				methodMap.put(41, "setTutorCountry");
				methodMap.put(42, "setTutorEmail");
				methodMap.put(43, "setTutorPhone");
				methodMap.put(44, "setAllergy");
				methodMap.put(45, "setComments");

				Map<String, String> parameterMap = new HashMap<String, String>();
				parameterMap.put("setBirthDate", "Date");
				setErrorMessage("");
				List<Student> students = new ArrayList<Student>();

				file = new FileInputStream(tempFile);

				// Get the workbook instance for XLS file
				XSSFWorkbook workbook = new XSSFWorkbook(file);

				// Get first sheet from the workbook
				XSSFSheet sheet = workbook.getSheetAt(0);

				// Iterate through each rows from first sheet
				Iterator<Row> rowIterator = sheet.iterator();
				while (rowIterator.hasNext()) {
					row = rowIterator.next();
					if (row.getRowNum() > 0) {

						Student student = new Student();

						// For each row, iterate through each columns
						Iterator<Cell> cellIterator = row.cellIterator();
						while (cellIterator.hasNext()) {
							cell = cellIterator.next();

							if (parameterMap.get(methodMap.get(cell
									.getColumnIndex() + 1)) == null
									|| "String".equalsIgnoreCase(parameterMap
											.get(methodMap.get(cell
													.getColumnIndex() + 1)))) {
								Method m = student.getClass()
										.getMethod(
												methodMap.get(cell
														.getColumnIndex() + 1),
												new Class[] { String.class });
								m.invoke(student, new Object[] { cell
										.getStringCellValue() });
							} else if ("Date"
									.equalsIgnoreCase(parameterMap
											.get(methodMap.get(cell
													.getColumnIndex() + 1)))) {
								Method m = student.getClass()
										.getMethod(
												methodMap.get(cell
														.getColumnIndex() + 1),
												new Class[] { Date.class });
								m.invoke(
										student,
										new Object[] { cell.getDateCellValue() });
							}
						}
						String validateMessage = student.validateForExport();
						if (StringUtils.isNotBlank(validateMessage))
							setErrorMessage(getErrorMessage() + validateMessage);
						else {
							students.add(student);
						}

					}
				}
				FacesContext context = FacesContext.getCurrentInstance();
				ApplicationContext ctx1 = org.springframework.web.jsf.FacesContextUtils
						.getWebApplicationContext(context);
				Config config = (Config) ctx1.getBean("config");
				setErrorMessage(getErrorMessage()
						+ baseService.saveStudents(students, config,
								getCurrentUser()));
				if (StringUtils.isEmpty((getErrorMessage())))
					setErrorMessage("Etudiants importes avec succes");
			}
		} catch (IOException e) {

			e.printStackTrace();

			setErrorMessage(e.getMessage() + ". Fichier: "+tempFile==null?" pas de fichier":tempFile.getAbsolutePath());
		} catch (IllegalStateException ex) {
			ex.printStackTrace();
			int columnIndex = cell==null?0:cell.getColumnIndex() + 1;
			int rowIndex = row==null?0:row.getRowNum() + 1;
			setErrorMessage(ex.getMessage() + ". La donnee presente a la ligne "
					+ rowIndex + " column " + columnIndex
					+ " n'est pas bien formate. ");
		} catch (Exception e) {

			e.printStackTrace();
			int columnIndex = cell==null?0:cell.getColumnIndex() + 1;
			int rowIndex = row==null?0:row.getRowNum() + 1;

			setErrorMessage(e.getMessage() + " . La donnee presente a la ligne "
					+ rowIndex + " column " + columnIndex
					+ " n'est pas bien formate. ");
		} finally {
			if (file != null)
				file.close();

			if (tempFile != null && !tempFile.delete()) {
				setErrorMessage("Fichier " + tempFile.getName()
						+ " n'a pas ete supprime. ");
			}
		}
	}

	public void generateCSVReport() {
		try {
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
			header.append("Matricule" + delimiter);
			header.append("Nom" + delimiter);
			header.append("Prenom" + delimiter);
			header.append("Sex" + delimiter);
			header.append("Date de naissance" + delimiter);
			header.append("Ville de naissance" + delimiter);
			header.append("Pays de naissance" + delimiter);
			header.append("Ville de residence" + delimiter);
			header.append("Pays de residence" + delimiter);

			header.append("Adresse" + delimiter);
			header.append("Nom du pere" + delimiter);
			header.append("Prenom du pere" + delimiter);
			header.append("Nom de la mere" + delimiter);
			header.append("Prenom de la mere" + delimiter);
			header.append("Classe" + delimiter);
			header.append("Annee scolaire" + delimiter);
			header.append("Initial" + delimiter);
			header.append("Surnom" + delimiter);
			header.append("Email" + delimiter);
			header.append("Phone" + delimiter);
			header.append("Cell Phone" + delimiter);
			header.append("Religion" + delimiter);
			header.append("Profession du pere" + delimiter);
			header.append("Lieu de travail du pere" + delimiter);
			header.append("Address du pere" + delimiter);
			header.append("Ville du pere" + delimiter);
			header.append("Pays du pere" + delimiter);
			header.append("Email du pere" + delimiter);
			header.append("Phone du pere" + delimiter);
			header.append("Profession de la mere" + delimiter);
			header.append("Lieu de travail de la mere" + delimiter);
			header.append("Address de la mere" + delimiter);
			header.append("Ville de la mere" + delimiter);
			header.append("Pays de la mere" + delimiter);
			header.append("Email de la mere" + delimiter);
			header.append("Phone de la mere" + delimiter);
			header.append("Nom du tuteur" + delimiter);
			header.append("Prenom du tuteur" + delimiter);
			header.append("Address du tuteur" + delimiter);
			header.append("Ville du tuteur" + delimiter);
			header.append("Pays du tuteur" + delimiter);
			header.append("Email du tuteur" + delimiter);
			header.append("Phone du tuteur" + delimiter);
			header.append("Allergy" + delimiter);
			header.append("Info Additionnelle" + delimiter);

			StringBuffer body = new StringBuffer();
			for (StudentVO student : students) {
				// Get Parents information
				List<BaseEntity> studentParents = baseService
						.loadAllByParentId(ParentStudent.class, "student",
								"id", student.getId());
				Parent father = new Parent();
				Parent mother = new Parent();
				Parent tutor = new Parent();

				for (BaseEntity entity : studentParents) {
					ParentStudent ps = (ParentStudent) entity;
					if (ps.getParentTypeId() == 1)
						father = ps.getParent();
					else if (ps.getParentTypeId() == 2)
						mother = ps.getParent();
					else
						tutor = ps.getParent();
				}

				body.append(printString("\"=\"" + student.getMatricule()));
				body.append(delimiter);
				body.append(printString(student.getLastName()));
				body.append(delimiter);
				body.append(printString(student.getFirstName()));
				body.append(delimiter);
				body.append(printString(student.getSex()));
				body.append(delimiter);
				body.append(printString(student.getBirthDate(), df));
				body.append(delimiter);
				body.append(printString(student.getBirthCity()));
				body.append(delimiter);
				body.append(printString(student.getCountryOfBirth()));
				body.append(delimiter);
				body.append(printString(student.getCity()));
				body.append(delimiter);
				body.append(printString(student.getCountryName()));
				body.append(delimiter);
				body.append(printString(student.getAddress()));
				body.append(delimiter);
				body.append(printString(father.getLastName() != null ? father
						.getLastName() : ""));
				body.append(delimiter);
				body.append(printString(father.getFirstName() != null ? father
						.getFirstName() : ""));
				body.append(delimiter);
				body.append(printString(mother.getLastName() != null ? mother
						.getLastName() : ""));
				body.append(delimiter);
				body.append(printString(mother.getFirstName() != null ? mother
						.getFirstName() : ""));
				body.append(delimiter);
				body.append(printString(student.getLevelClassName()));
				body.append(delimiter);
				body.append(printString(student.getSchoolYear()));
				body.append(delimiter);
				body.append(printString(student.getMiddleName()));
				body.append(delimiter);
				body.append(printString(student.getNickName()));
				body.append(delimiter);
				body.append(printString(student.getEmail()));
				body.append(delimiter);
				body.append(printString(student.getPhone()));
				body.append(delimiter);
				body.append(printString(student.getCellPhone()));
				body.append(delimiter);
				body.append(printString(student.getReligionName()));
				body.append(delimiter);
				body.append(printString(father.getProfession()));
				body.append(delimiter);
				body.append(printString(father.getWorkPlace()));
				body.append(delimiter);
				body.append(printString(father.getAddress()));
				body.append(delimiter);
				body.append(printString(father.getCity()));
				body.append(delimiter);
				body.append(printString(father.getCountry() != null ? father
						.getCountry().getName() : ""));
				body.append(delimiter);
				body.append(printString(father.getEmail()));
				body.append(delimiter);
				body.append(printString(father.getTelephone()));
				body.append(delimiter);

				body.append(printString(mother.getProfession()));
				body.append(delimiter);
				body.append(printString(mother.getWorkPlace()));
				body.append(delimiter);
				body.append(printString(mother.getAddress()));
				body.append(delimiter);
				body.append(printString(mother.getCity()));
				body.append(delimiter);
				body.append(printString(mother.getCountry() != null ? mother
						.getCountry().getName() : ""));
				body.append(delimiter);
				body.append(printString(mother.getEmail()));
				body.append(delimiter);
				body.append(printString(mother.getTelephone()));
				body.append(delimiter);

				body.append(printString(tutor.getLastName() != null ? tutor
						.getLastName() : ""));
				body.append(delimiter);
				body.append(printString(tutor.getFirstName() != null ? tutor
						.getFirstName() : ""));
				body.append(delimiter);
				body.append(printString(tutor.getAddress()));
				body.append(delimiter);
				body.append(printString(tutor.getCity()));
				body.append(delimiter);
				body.append(printString(tutor.getCountry() != null ? tutor
						.getCountry().getName() : ""));
				body.append(delimiter);
				body.append(printString(tutor.getEmail()));
				body.append(delimiter);
				body.append(printString(tutor.getTelephone()));
				body.append(delimiter);
				body.append(printString(student.getAllergy()));
				body.append(delimiter);
				body.append(printString(student.getComments()));
				body.append("\n");
			}

			totalString.append(header.toString());
			totalString.append("\n");
			totalString.append(body.toString());

			response.addHeader("Content-disposition",
					"attachment;filename=etudiant" + "-" + getStringDate()
							+ ".csv");
			response.setContentType("application/csv");
			ouputStream.write(totalString.toString().getBytes());
			ouputStream.flush();
			ouputStream.close();
			context.responseComplete();
		} catch (Exception exception) {
			exception.printStackTrace();
		}
	}

	public String searchParents() {
		List<Parent> availables = null;
		availableParents = new ArrayList<Parent>();
		selectedParents = new ArrayList<Parent>();
		List<Long> selectedIds = new ArrayList<Long>();
		List<BaseEntity> selecteds = baseService.loadAllByParentId(
				ParentStudent.class, "student", "id", student.getId());
		for (BaseEntity entity : selecteds) {
			ParentStudent ps = (ParentStudent) entity;
			ps.getParent().setParentType(
					ps.getParentTypeId() != null ? ps.getParentTypeId()
							.toString() : "");
			Parent p = ps.getParent();
			p.setParentType(ps.getParentTypeId() != null ? ps.getParentTypeId()
					.toString() : "");
			FacesContext context = FacesContext.getCurrentInstance();
			ApplicationContext ctx = org.springframework.web.jsf.FacesContextUtils
					.getWebApplicationContext(context);
			Config config = (Config) ctx.getBean("config");
			p.setParentTypeText(config.getConfigurationByGroupAndKey(
					"PARENT_TYPE", ps.getParentTypeId() != null ? ps
							.getParentTypeId().toString() : ""));
			selectedParents.add(p);
			selectedIds.add(ps.getParent().getId());
		}

		Parent p = new Parent();
		if (StringUtils.isNotBlank(parentLastName)
				|| StringUtils.isNotBlank(parentFirstName)) {
			p.setLastName(parentLastName);
			p.setFirstName(parentFirstName);
			availables = baseService.searchParentsToAssign(p, getCurrentUser()
					.getSchool());
			setRowCount(new Long(availables.size()));
		}

		if (availables != null) {
			for (Parent pa : availables) {
				if (!selectedIds.contains(pa.getId())) {
					availableParents.add(pa);
				}
			}
		}

		selectedParentCounts = new Long(
				selectedParents != null ? selectedParents.size() : 0);
		parentCounts = new Long(availables != null ? availables.size() : 0);
		return "Success";
	}

	public String doNothing() {
		return "";
	}

	public String getTestStyle() {
		return testStyle;
	}

	public void setTestStyle(String testStyle) {
		this.testStyle = testStyle;
	}

	public Long getStudentId() {
		return studentId;
	}

	public void setStudentId(Long studentId) {
		this.studentId = studentId;
	}

	public String getReligionName() {
		return religionName;
	}

	public void setReligionName(String religionName) {
		this.religionName = religionName;
	}

	public Short getStudentStatus() {
		return studentStatus;
	}

	public void setStudentStatus(Short studentStatus) {
		this.studentStatus = studentStatus;
	}

	public int getNumberOfBoys() {
		return numberOfBoys;
	}

	public void setNumberOfBoys(int numberOfBoys) {
		this.numberOfBoys = numberOfBoys;
	}

	public int getNumberOfGirls() {
		return numberOfGirls;
	}

	public void setNumberOfGirls(int numberOfGirls) {
		this.numberOfGirls = numberOfGirls;
	}

	public int getAverageAge() {
		return averageAge;
	}

	public void setAverageAge(int averageAge) {
		this.averageAge = averageAge;
	}

	public int getMaximumAge() {
		return maximumAge;
	}

	public void setMaximumAge(int maximumAge) {
		this.maximumAge = maximumAge;
	}

	public int getMinimumAge() {
		return minimumAge;
	}

	public void setMinimumAge(int minimumAge) {
		this.minimumAge = minimumAge;
	}

	public Collection<StudentStatisticVO> getCountryStatistics() {
		return countryStatistics;
	}

	public void setCountryStatistics(
			Collection<StudentStatisticVO> countryStatistics) {
		this.countryStatistics = countryStatistics;
	}

	public Collection<StudentStatisticVO> getReligionStatistics() {
		return religionStatistics;
	}

	public void setReligionStatistics(
			Collection<StudentStatisticVO> religionStatistics) {
		this.religionStatistics = religionStatistics;
	}

	public String getSexSearchCriteria() {
		return sexSearchCriteria;
	}

	public void setSexSearchCriteria(String sexSearchCriteria) {
		this.sexSearchCriteria = sexSearchCriteria;
	}

	public String getYearSearchCriteria() {
		return yearSearchCriteria;
	}

	public void setYearSearchCriteria(String yearSearchCriteria) {
		this.yearSearchCriteria = yearSearchCriteria;
	}

	public String getAgeSearchCriteria() {
		return ageSearchCriteria;
	}

	public void setAgeSearchCriteria(String ageSearchCriteria) {
		this.ageSearchCriteria = ageSearchCriteria;
	}

	public String getOutcomeSearchCriteria() {
		return outcomeSearchCriteria;
	}

	public void setOutcomeSearchCriteria(String outcomeSearchCriteria) {
		this.outcomeSearchCriteria = outcomeSearchCriteria;
	}

	public StudentSearchVO getStudentSearch() {
		return studentSearch;
	}

	public void setStudentSearch(StudentSearchVO studentSearch) {
		this.studentSearch = studentSearch;
	}

	public List<Parent> getAvailableParents() {
		return availableParents;
	}

	public void setAvailableParents(List<Parent> availableParents) {
		this.availableParents = availableParents;
	}

	public List<Parent> getSelectedParents() {
		return selectedParents;
	}

	public void setSelectedParents(List<Parent> selectedParents) {
		this.selectedParents = selectedParents;
	}

	public String getParentFirstName() {
		return parentFirstName;
	}

	public void setParentFirstName(String parentFirstName) {
		this.parentFirstName = parentFirstName;
	}

	public String getParentLastName() {
		return parentLastName;
	}

	public void setParentLastName(String parentLastName) {
		this.parentLastName = parentLastName;
	}

	public Long getParentCounts() {
		return parentCounts;
	}

	public void setParentCounts(Long parentCounts) {
		this.parentCounts = parentCounts;
	}

	public Long getSelectedParentCounts() {
		return selectedParentCounts;
	}

	public void setSelectedParentCounts(Long selectedParentCounts) {
		this.selectedParentCounts = selectedParentCounts;
	}

	public String getYear() {
		return year;
	}

	public void setYear(String year) {
		this.year = year;
	}

	public String getTuitionTypeName() {
		return tuitionTypeName;
	}

	public void setTuitionTypeName(String tuitionTypeName) {
		this.tuitionTypeName = tuitionTypeName;
	}

	public Tuition getTuition() {
		return tuition;
	}

	public void setTuition(Tuition tuition) {
		this.tuition = tuition;
	}

	public String printReceipt() {

		try {

			Map<String, Serializable> parameters = new HashMap<String, Serializable>();

			if (student != null && student.getTotalAmount() > 0) {

				FacesContext context = FacesContext.getCurrentInstance();

				String reportsDirPath = ((ServletContext) context
						.getExternalContext().getContext())
						.getRealPath("/reports/");

				parameters.put("SUBREPORT_DIR", reportsDirPath
						+ java.io.File.separator);
				HttpServletResponse response = (HttpServletResponse) context
						.getExternalContext().getResponse();

				parameters.put("parent", student.getLastName());
				parameters.put("fullName", student.getFirstName());
				parameters.put("totalPaye", student.getTotalPaid());
				parameters.put("matricule", student.getMatricule());
				parameters.put("montant", tuition.getAmount());
				parameters.put("comment", tuition.getComment());
				parameters.put("receivedBy", getCurrentUser().getFirstName()
						+ " " + getCurrentUser().getLastName());
				parameters.put(
						"numero",
						"No : "
								+ StringUtils.leftPad(paymentId.toString(), 8,
										"0") + " / " + year);
				parameters.put("raison", tuitionTypeName);
				parameters.put("reste", student.getTotalBalance());
				parameters
						.put("schoolId", getCurrentUser().getSchool().getId());

				InputStream reportStream = context.getExternalContext()
						.getResourceAsStream("/reports/recuParent.jasper");

				ServletOutputStream ouputStream = response.getOutputStream();

				response.addHeader("Content-disposition",
						"attachment;filename=recu" + "-" + getStringDate()
								+ ".pdf");

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

}
