package com.esoft.ischool.service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.hibernate.Query;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import com.esoft.ischool.dao.BaseDao;
import com.esoft.ischool.job.Config;
import com.esoft.ischool.model.Alert;
import com.esoft.ischool.model.AlertReceiver;
import com.esoft.ischool.model.AssignmentFile;
import com.esoft.ischool.model.Averages;
import com.esoft.ischool.model.BaseEntity;
import com.esoft.ischool.model.Bulletin;
import com.esoft.ischool.model.Configuration;
import com.esoft.ischool.model.Correspondence;
import com.esoft.ischool.model.Country;
import com.esoft.ischool.model.Course;
import com.esoft.ischool.model.Exam;
import com.esoft.ischool.model.ExamType;
import com.esoft.ischool.model.Files;
import com.esoft.ischool.model.Grade;
import com.esoft.ischool.model.Level;
import com.esoft.ischool.model.LevelClass;
import com.esoft.ischool.model.Mark;
import com.esoft.ischool.model.News;
import com.esoft.ischool.model.Parent;
import com.esoft.ischool.model.ParentStudent;
import com.esoft.ischool.model.Payment;
import com.esoft.ischool.model.Receiver;
import com.esoft.ischool.model.School;
import com.esoft.ischool.model.SchoolReligion;
import com.esoft.ischool.model.SchoolYear;
import com.esoft.ischool.model.Student;
import com.esoft.ischool.model.StudentEnrollment;
import com.esoft.ischool.model.StudentTuition;
import com.esoft.ischool.model.Subject;
import com.esoft.ischool.model.Teacher;
import com.esoft.ischool.model.TeacherEnrollment;
import com.esoft.ischool.model.Term;
import com.esoft.ischool.model.TermGroup;
import com.esoft.ischool.model.TermGroupSummary;
import com.esoft.ischool.model.TermResult;
import com.esoft.ischool.model.TermResultDtl;
import com.esoft.ischool.model.TermResultSummary;
import com.esoft.ischool.model.Tuition;
import com.esoft.ischool.model.TuitionEnrollment;
import com.esoft.ischool.model.TuitionType;
import com.esoft.ischool.model.YearSummary;
import com.esoft.ischool.security.model.Menu;
import com.esoft.ischool.security.model.Roles;
import com.esoft.ischool.security.model.RolesMenu;
import com.esoft.ischool.security.model.RolesUser;
import com.esoft.ischool.security.model.User;
import com.esoft.ischool.stocks.model.OrderProduct;
import com.esoft.ischool.stocks.model.Product;
import com.esoft.ischool.stocks.model.ProductConsumer;
import com.esoft.ischool.stocks.model.PurchaseOrder;
import com.esoft.ischool.util.AverageStatus;
import com.esoft.ischool.util.Constants;
import com.esoft.ischool.util.Utils;
import com.esoft.ischool.vo.CourseVO;
import com.esoft.ischool.vo.FileSearchVO;
import com.esoft.ischool.vo.MarkVO;
import com.esoft.ischool.vo.StudentSearchVO;
import com.esoft.ischool.vo.StudentVO;
import com.esoft.ischool.vo.TeacherVO;

@Service("baseService") 

public class BaseServiceImpl implements BaseService {

	@Autowired
	@Qualifier("baseDao")
	private BaseDao baseDao;

	public School getDefaultSchool() {
		return baseDao.getDefaultSChool();
	}

	public void delete(Long id, Class cl) {
		baseDao.deleteExamMark(id);
		baseDao.delete(getById(cl, id));
	}

	public Teacher getTeacher(User user) {
		return baseDao.getTeacher(user);
	}

	public Student getStudent(User user) {
		return baseDao.getStudent(user);
	}

	public BaseEntity getById(Class cl, Long id) {
		return baseDao.getById(cl, id);
	}

	public void update(BaseEntity entity, User user) {
		baseDao.update(entity, user);
	}

	public void save(BaseEntity entity, User user) {
		baseDao.save(entity, user);
	}

	public BaseEntity load(BaseEntity entity) {
		return baseDao.load(entity);

	}

	public List<BaseEntity> loadAll(Class<? extends BaseEntity> cl) {
		return baseDao.loadAll(cl);
	}

	public List<BaseEntity> loadAll(Class<? extends BaseEntity> cl,
			School school) {
		return baseDao.loadAll(cl, school);
	}

	public BaseEntity findByName(Class cl, String nom, School school) {
		return baseDao.findByName(cl, nom, school);
	}

	public BaseEntity findByColumn(Class cl, String columnName,
			Integer columnValue, School school) {
		return baseDao.findByColumn(cl, columnName, columnValue, school);
	}

	public BaseEntity findByColumn(Class cl, String columnName,
			String columnValue, School school) {
		return baseDao.findByColumn(cl, columnName, columnValue, school);
	}

	public BaseEntity findByColumn(Class cl, String columnName,
			String columnValue) {
		return baseDao.findByColumn(cl, columnName, columnValue);
	}
	
	public List<BaseEntity> findByColumns(Class cl, List<String> columnNames,	List<String> columnValues ) {
		return baseDao.findByColumns(cl, columnNames, columnValues);
	}

	public List<BaseEntity> findByColumnsLike(Class cl, List<String> columnNames,
			List<String> columnValues) {
		return baseDao.findByColumnsLike(cl, columnNames, columnValues);
	}
	
	public BaseEntity findByName(Class cl, String nom, String parentName,
			String parentProperty, String parentPropertyValue) {
		return baseDao.findByName(cl, nom, parentName, parentProperty,
				parentPropertyValue);
	}

	public List<BaseEntity> loadAllByParentId(Class<? extends BaseEntity> c,
			String parentName, String parentProperty, Long parentPropertyValue) {
		return baseDao.loadAllByParentId(c, parentName, parentProperty,
				parentPropertyValue);
	}

	public List<Student> searchStudents(boolean selectAllStudents,
			String className, String year, School school) {
		// TODO Auto-generated method stub
		return baseDao.searchStudents(selectAllStudents, className, year,
				school);
	}
	
	public List<Student> searchStudents(boolean selectAllStudents,
			String className, String year, String lastName, School school) {
		// TODO Auto-generated method stub
		return baseDao.searchStudents(selectAllStudents, className, year,
				lastName, school);
	}

	public List<Teacher> searchTeachers(boolean selectAllStudents,
			String className, String year, School school) {
		// TODO Auto-generated method stub
		return baseDao.searchTeachers(selectAllStudents, className, year,
				school);
	}

	public void saveStudentsCorrespondance(Correspondence correspondence,
			List<Student> selectedStudents, User user) {
		if (selectedStudents != null)
			for (Student student : selectedStudents) {

				Student aStudent = (Student) baseDao.findByColumn(
						Student.class, "matricule", student.getMatricule(),
						user.getSchool());
				if (baseDao.getStudentCorrespondance(aStudent.getId(),
						correspondence.getId()) == null) {
					Receiver receiver = new Receiver();
					receiver.setCorrespondence(correspondence);
					receiver.setStudent(aStudent);
					receiver.setCreateDate(new Date());
					receiver.setModDate(new Date());
					baseDao.save(receiver, user);
				}

			}
	}

	public void saveTeachersCorrespondance(Correspondence correspondence,
			List<Teacher> selectedTeachers, User user) {
		if (selectedTeachers != null)
			for (Teacher teacher : selectedTeachers) {
				Teacher aTeacher = (Teacher) baseDao.findByColumn(
						Teacher.class, "matricule", teacher.getMatricule(),
						user.getSchool());

				if (baseDao.getTeacherCorrespondance(aTeacher.getId(),
						correspondence.getId()) == null) {
					Receiver receiver = new Receiver();
					receiver.setCorrespondence(correspondence);
					receiver.setTeacher(aTeacher);
					receiver.setCreateDate(new Date());
					receiver.setModDate(new Date());
					baseDao.save(receiver, user);
				}

			}
	}

	public void saveStudentsEnrollment(LevelClass levelClass,
			List<Student> selectedStudents, SchoolYear schoolYear,
			Date enrollmentDate, User user) {
		if (selectedStudents != null)
			for (Student student : selectedStudents) {

				if (baseDao.getStudentEnrollment(student.getId(),
						levelClass.getId(), schoolYear.getId()) == null) {
					StudentEnrollment studentEnrollment = new StudentEnrollment();
					studentEnrollment.setLevelClass(levelClass);
					studentEnrollment.setStudent(student);
					studentEnrollment.setSchoolYear(schoolYear);
					studentEnrollment.setCreateDate(new Date());
					studentEnrollment.setModDate(new Date());
					studentEnrollment.setEnrollmentDate(enrollmentDate);
					baseDao.save(studentEnrollment, user);
					Student st = (Student) baseDao.getById(Student.class,
							student.getId());
					st.setCurrentEnrollment(studentEnrollment);
					baseDao.update(st, user);

				}
			}
	}

	public void assignStudentsToParent(final Parent parent, List<Student> selectedStudents, User user){
		baseDao.deleteByParentIds("ParentStudent", new HashMap<String, Long>() {
			{
				put("parent", parent.getId());
			}
		});
		
		if (selectedStudents != null) {
			for (Student student : selectedStudents) {
				ParentStudent parentStudent = (ParentStudent) baseDao.findByParents(ParentStudent.class, 
						"parent", parent.getId(), "student", student.getId());
				if (parentStudent == null) {
					parentStudent = new ParentStudent();
					parentStudent.setParent(parent);
					parentStudent.setStudent(student);
					parentStudent.setParentTypeId(new Long(student.getParentType()));
					parentStudent.setCreateDate(new Date());
					parentStudent.setModDate(new Date());
					baseDao.save(parentStudent, user);
				}
			}
		}
	}
	
	public void deleteMarksByExam(final Exam exam) {
		baseDao.deleteByParentIds("Mark", new HashMap<String, Long>() {
			{
				put("exam", exam.getId());
			}
		});
	}
	
	public void assignParentsToStudent(final Student student, List<Parent> selectedParents, User user) {
		baseDao.deleteByParentIds("ParentStudent", new HashMap<String, Long>() {
			{
				put("student", student.getId());
			}
		});
		
		if (selectedParents != null) {
			for (Parent parent : selectedParents) {
				ParentStudent parentStudent = (ParentStudent) baseDao.findByParents(ParentStudent.class, 
						"parent", parent.getId(), "student", student.getId());
				if (parentStudent == null) {
					parentStudent = new ParentStudent();
					parentStudent.setParent(parent);
					parentStudent.setStudent(student);
					parentStudent.setParentTypeId(new Long(parent.getParentType()));
					parentStudent.setCreateDate(new Date());
					parentStudent.setModDate(new Date());
					baseDao.save(parentStudent, user);
				}
			}
		}
	}
	public void saveRoleMenus(final Roles role, List<BaseEntity> selectedMenus,
			User user) {

		baseDao.deleteByParentIds("RolesMenu", new HashMap<String, Long>() {
			{
				put("roles", role.getId());
			}
		});

		for (BaseEntity entity : selectedMenus) {
			Menu menu = (Menu) entity;
			RolesMenu rolesMenu = new RolesMenu();
			rolesMenu.setRoles(role);
			rolesMenu.setMenu(menu);
			rolesMenu.setCreateDate(new Date());
			rolesMenu.setModDate(new Date());
			rolesMenu.setAccessLevel(menu.getAccessLevelCheck() ? 1 : 0);

			baseDao.save(rolesMenu, user);
		}
	}

	public void saveRoleUsers(final Roles role, List<BaseEntity> selectedUsers,
			User u) {

		baseDao.deleteByParentIds("RolesUser", new HashMap<String, Long>() {
			{
				put("roles", role.getId());
			}
		});

		for (BaseEntity entity : selectedUsers) {
			User user = (User) entity;
			RolesUser rolesUser = new RolesUser();
			rolesUser.setRoles(role);
			rolesUser.setUser(user);
			rolesUser.setCreateDate(new Date());
			rolesUser.setModDate(new Date());
			baseDao.save(rolesUser, u);
		}
	}

	public void saveTeachersEnrollment(LevelClass levelClass,
			List<Teacher> selectedTeachers, SchoolYear schoolYear) {
	}

	public List<Tuition> getStudentPayments(Student student, School school) {
		// TODO Auto-generated method stub

		return baseDao.getStudentPayments(student, school);
	}

	public Payment getStudentPayment(Student student, Tuition tuition,
			School school) {
		// TODO Auto-generated method stub
		return baseDao.getStudentPayment(student, tuition, school);
	}

	public List<StudentVO> getStudents(Class cl, StudentSearchVO studentSearch, User user) {
		return baseDao.getStudents(cl, studentSearch, user);
	}

	public List<TeacherVO> getTeachers(Class cl, String matricule,
			String lastName, String firstName, User user) {
		return baseDao.getTeachers(cl, matricule, lastName, firstName, user);
	}

	public BaseEntity findByParents(Class cl, String firstParent,
			String firstParentName, String secondParent, String secondParentName) {
		return baseDao.findByParents(cl, firstParent, firstParentName,
				secondParent, secondParentName);
	}

	public List<BaseEntity> loadByParentsIds(Class cl, String firstParentName,
			Long firstParentId, String secondParentName, Long secondParentId) {
		return baseDao.findByParentsIds(cl, firstParentName, firstParentId,
				secondParentName, secondParentId);
	}

	public List<BaseEntity> loadByParentsIds(Class cl, String firstParentName,
			Long firstParentId, String secondParentName, Long secondParentId,
			String thirdParentName, Long thirdParentId) {
		return baseDao.findByParentsIds(cl, firstParentName, firstParentId,
				secondParentName, secondParentId, thirdParentName,
				thirdParentId);
	}

	public BaseEntity findByParents(Class cl, String firstParent,
			Long firstParentId, String secondParent, Long secondParentId) {
		return baseDao.findByParents(cl, firstParent, firstParentId,
				secondParent, secondParentId);
	}

	public TermResult fetchTermResults(SchoolYear schoolYear,
			LevelClass lclass, Term term) {
		// TODO Auto-generated method stub
		return baseDao.fetchTermResults(schoolYear, lclass, term);
	}

	public List<TuitionEnrollment> getAllPaymentsDueInDays(Class cl,
			Integer numberOfDays) {
		return baseDao.getAllPaymentsDueInDays(cl, numberOfDays);
	}

	public List<BaseEntity> getAllProductConsumersDueInDays(Class cl,
			Integer numberOfDays) {
		return baseDao.getAllProductConsumersDueInDays(cl, numberOfDays);
	}

	public List<Alert> getAllActiveAlert(Class cl, String alertTypeCode) {
		return baseDao.getAllActiveAlert(cl, alertTypeCode);
	}

	public Integer getReceivedStudentAlertCount(Class cl, Long alertId,
			Long tuitionId, Long userId) {
		return baseDao.getReceivedStudentAlertCount(cl, alertId, tuitionId,
				userId);
	}

	public List<AlertReceiver> getAlertReceiver(Class cl, Long alertId,
			Long tuitionId, Long userId) {
		return baseDao.getAlertReceiver(cl, alertId, tuitionId, userId);
	}

	public List<Configuration> getConfigurationListByGroup(Class cl,
			String groupName, School school) {
		return baseDao.getConfigurationListByGroup(cl, groupName, school);
	}

	public void saveDeliveredOrder(PurchaseOrder purchaseOrder, User user) {
		// update the order itself.
		update(purchaseOrder, user);

		List<BaseEntity> orderProducts = loadAllByParentId(OrderProduct.class,
				"order", "id", purchaseOrder.getId());

		for (BaseEntity op : orderProducts) {
			OrderProduct orderProduct = (OrderProduct) op;
			Product product = (Product) getById(Product.class, orderProduct
					.getProduct().getId());
			product.setQuantityInStock(product.getQuantityInStock()
					+ orderProduct.getQuantityReceived());
			product.setQuantity(product.getQuantity()
					+ orderProduct.getQuantityReceived());
			// update the product's stock.
			update(product, user);
		}
	}

	public void saveProductConsumer(ProductConsumer productConsumer,
			Integer quantity, User user) {
		Product product = (Product) getById(Product.class, productConsumer
				.getProduct().getId());
		product.setQuantityInStock(product.getQuantityInStock() - quantity);

		// update the product's stock.
		update(product, user);

		// update the order itself.
		update(productConsumer, user);
	}

	public List<BaseEntity> getEntityByPropertyComparison(Class cl,
			String propertyName1, String propertyName2) {
		return baseDao.getEntityByPropertyComparison(cl, propertyName1,
				propertyName2);
	}

	public <T> List<BaseEntity> findByColumnValues(Class cl, String columnName,
			List<T> columnValue, Long schoolId) {
		return baseDao
				.findByColumnValues(cl, columnName, columnValue, schoolId);
	}

	public void saveStudent(Student student, Config config, User user1) {

		User user = createUser(student, config, user1);
		student.setUser(user);
		save(student, user1);
	}
	
	public String saveStudents(List<Student> students, Config config, User user1) {
		Map<String, Country> countryMap = new HashMap<String, Country>();
		Map<String, LevelClass> levelClassMap = new HashMap<String, LevelClass>();
		Map<String, SchoolYear> schoolYearMap = new HashMap<String, SchoolYear>();
		Map<String, SchoolReligion> schoolReligionMap = new HashMap<String, SchoolReligion>();
		
		StringBuffer errorMessage = new StringBuffer();
		List<Student> validateStudents = validateStudents(students, countryMap, levelClassMap, 
				schoolYearMap, schoolReligionMap, user1, errorMessage);
		
		Parent dummieParent = new Parent();
		for (Student student : validateStudents) {
			
			// Create user and student
			User user = createUser(student, config, user1);
			student.setUser(user);
			student.setCountry(countryMap.get(student.getCountryName()));
			student.setBirthCountry(countryMap.get(student.getBirthCountryName()));
			save(student, user1);
			
			// Assign father.
			dummieParent = new Parent(student.getFatherFirstName(), student.getFatherLastName(),
					student.getFatherMiddleName(), student.getFatherAddress(), student.getFatherCity(),
					countryMap.get(student.getFatherCountry()), student.getFatherPhone(),
					student.getFatherEmail(), student.getFatherProfession(), student.getFatherWorkPlace());
			exportParentToStudent(user1, countryMap, dummieParent, student, new Long(1));
			
			// Assign mother.
			dummieParent = new Parent(student.getMotherFirstName(), student.getMotherLastName(),
					student.getMotherMiddleName(), student.getMotherAddress(), student.getMotherCity(),
					countryMap.get(student.getMotherCountry()), student.getMotherPhone(),
					student.getMotherEmail(), student.getMotherProfession(), student.getMotherWorkPlace());
			exportParentToStudent(user1, countryMap, dummieParent, student, new Long(2));
			
			// Assign tutor.
			dummieParent = new Parent(student.getTutorFirstName(), student.getTutorLastName(),
					student.getTutorMiddleName(), student.getTutorAddress(), student.getTutorCity(),
					countryMap.get(student.getTutorCountry()), student.getTutorPhone(),
					student.getTutorEmail(), student.getTutorProfession(), student.getTutorWorkPlace());
			exportParentToStudent(user1, countryMap, dummieParent, student, new Long(5));
			
			// Enroll the student
			StudentEnrollment studentEnrollment = new StudentEnrollment();
			studentEnrollment.setLevelClass(levelClassMap.get(student.getLevelClassName()));
			studentEnrollment.setSchoolYear(schoolYearMap.get(student.getSchoolYearName()));
			studentEnrollment.setStudent(student);
			studentEnrollment.setEnrollmentDate(new Date());
			save(studentEnrollment, user1);
			
			//Update student current enrollment id;
			student.setCurrentEnrollment(studentEnrollment);
			save(student, user1);
		}
		
		return errorMessage.toString();
	}

	private void exportParentToStudent(User user1,
			Map<String, Country> countryMap, Parent dummieParent,
			Student student, Long parentType) {
		
		if (StringUtils.isNotBlank(dummieParent.getFirstName()) && StringUtils.isNotBlank(dummieParent.getLastName())) {
			Parent parent = findParent(dummieParent, user1.getSchool());
			if (parent == null) {
				parent = new Parent(dummieParent.getFirstName(), dummieParent.getLastName(), dummieParent.getMiddleName(), 
						dummieParent.getAddress(), dummieParent.getCity(), dummieParent.getCountry(), 
						dummieParent.getTelephone(), dummieParent.getEmail(), dummieParent.getProfession(), 
						dummieParent.getWorkPlace());
				baseDao.save(parent, user1);
			}
			
			// Create parent - student relation.
			ParentStudent parentStudent = new ParentStudent();
			parentStudent.setParent(parent);
			parentStudent.setStudent(student);
			parentStudent.setParentTypeId(parentType);
			baseDao.save(parentStudent, user1);
		}
	}
	
	private Parent findParent(Parent parent, School school) {
		return baseDao.searchParent(parent, school);
	}
	
	private List<Student> validateStudents(List<Student> students, Map<String, Country> countryMap, 
			Map<String, LevelClass> levelClassMap, Map<String, SchoolYear> schoolYearMap, 
			Map<String, SchoolReligion> schoolReligionMap, User user1, StringBuffer errorMessage) {
		List<Student> valideStudents = new ArrayList<Student>();
		for (Student stu : students) {
			if (findByColumn(Student.class, "matricule", stu.getMatricule()) != null) {
				errorMessage.append("Student with matricule " + stu.getMatricule() + " is already registered. \n");
				continue;
			}
			if (getLevelClass(levelClassMap, stu, user1, errorMessage) == null)
				continue;
			if (getSchoolYear(schoolYearMap, stu, user1, errorMessage) == null)
				continue;
			if (getCountry(countryMap, stu.getCountryName(), user1, errorMessage) == null)
				continue;
			if (getCountry(countryMap, stu.getBirthCountryName(), user1, errorMessage) == null)
				continue;
			if (StringUtils.isNotBlank(stu.getSchoolReligionName()) && getSchoolReligion(schoolReligionMap, stu, user1, errorMessage) == null)
				continue;
			if (StringUtils.isNotBlank(stu.getFatherCountry()) && getCountry(countryMap, stu.getFatherCountry(), user1, errorMessage) == null)
				continue;
			if (StringUtils.isNotBlank(stu.getMotherCountry()) && getCountry(countryMap, stu.getMotherCountry(), user1, errorMessage) == null)
				continue;
			valideStudents.add(stu);
		}
		
		return valideStudents;
	}

	private LevelClass getLevelClass(Map<String, LevelClass> levelClassMap, Student stu, User user1, StringBuffer errorMessage) {
		LevelClass levelClass = levelClassMap.get(stu.getLevelClassName());
		if (levelClass == null) {
			levelClass = (LevelClass) findByName(LevelClass.class, stu.getLevelClassName(), user1.getSchool());
			if (levelClass != null)
				levelClassMap.put(stu.getLevelClassName(), levelClass);
			else { 
				errorMessage.append(" Cette classe n'est pas dans la base: " + stu.getLevelClassName());
			}
		}
		return levelClass;
	}

	private SchoolYear getSchoolYear(Map<String, SchoolYear> schoolYearMap, Student stu, User user1, StringBuffer errorMessage) {
		SchoolYear schoolYear = schoolYearMap.get(stu.getSchoolYearName());
		if (schoolYear == null) {
			schoolYear = (SchoolYear) findByColumn(SchoolYear.class, "year", stu.getSchoolYearName(), user1.getSchool());
			if (schoolYear != null)
				schoolYearMap.put(stu.getSchoolYearName(), schoolYear);
			else { 
				errorMessage.append(" Cette annee scolaire n'est pas dans la base: " + stu.getSchoolYearName() + "\n");
			}
		}
		return schoolYear;
	}
	
	private SchoolReligion getSchoolReligion(Map<String, SchoolReligion> schoolReligionMap, Student stu, User user1, StringBuffer errorMessage) {
		SchoolReligion schoolReligion = schoolReligionMap.get(stu.getSchoolReligionName());
		if (schoolReligion == null) {
			schoolReligion = (SchoolReligion) findByName(SchoolReligion.class, stu.getSchoolReligionName(), user1.getSchool());
			if (schoolReligion != null)
				schoolReligionMap.put(stu.getSchoolReligionName(), schoolReligion);
			else { 
				errorMessage.append(" Cette religion n'est pas dans la base: " + stu.getSchoolReligionName() + "\n");
			}
		}
		return schoolReligion;
	}
	
	private Country getCountry(Map<String, Country> countryMap, String countryName, User user1, StringBuffer errorMessage) {
		Country country = countryMap.get(countryName);
		if (country == null) {
			country = (Country) findByName(Country.class, countryName, user1.getSchool());
			if (country != null)
				countryMap.put(countryName, country);
			else { 
				errorMessage.append(" Ce pays n'est pas dans la base: " + countryName + "\n");
			}
		}
		return country;
	}
	

	public void saveTeacher(Teacher teacher, Config config, User user1) {

		User user = createUser(teacher, config, user1);
		teacher.setUser(user);
		save(teacher, user1);

		TeacherEnrollment teacherEnrollment = new TeacherEnrollment();
		teacherEnrollment.setEnrollmentDate(new Date());
		teacherEnrollment.setTeacher((Teacher) baseDao.findByColumn(
				Teacher.class, "matricule", teacher.getMatricule(),
				teacher.getSchool()));
		save(teacherEnrollment, user1);

	}

	private User createUser(Student student, Config config, User user1) {
		User user = new User();
		user.setUserName(student.getMatricule());
		user.setFirstName(student.getFirstName());
		user.setLastName(student.getLastName());
		user.setEmail(student.getEmail());
		user.setPhone(student.getPhone());
		user.setPageSkin(config
				.getConfigPropertyValue("SCHOOL_DEFAULT_PAGE_SKIN") != null ? config
				.getConfigPropertyValue("SCHOOL_DEFAULT_PAGE_SKIN")
				: "emeraldTown");

		if (user1.getSchool().getGenerateRandomPassword()) {
			user.setPassword(Utils.getRandomString(Integer.parseInt(config
					.getConfigPropertyValue("SCHOOL_DEFAULT_PASSWORD_LENGTH") != null ? config
					.getConfigPropertyValue("SCHOOL_DEFAULT_PASSWORD_LENGTH")
					: "6")));
		} else {
			user.setPassword(config
					.getConfigPropertyValue("SCHOOL_DEFAULT_PASSWORD"));
		}

		user.setCsvDelimiter(config.getConfigurationByGroupAndKey("OPERATING_SYSTEM", "LIST_SEPERATOR"));
		
		if (user.getId() != null)
			update(user, user1);
		else {
			save(user, user1);
			RolesUser ru = new RolesUser();
			ru.setUser(user);
			ru.setRoles((Roles) baseDao.findByColumn(Roles.class, "roleCode",
					Constants.STUDENT_ROLE_CODE, user1.getSchool()));
			baseDao.save(ru, user1);
		}

		return user;
	}

	private User createUser(Teacher teacher, Config config, User user1) {
		User user = new User();
		user.setUserName(teacher.getMatricule());
		user.setFirstName(teacher.getFirstName());
		user.setLastName(teacher.getLastName());
		user.setEmail(teacher.getEmail());
		user.setPhone(teacher.getPhone());
		user.setPageSkin(config
				.getConfigPropertyValue("SCHOOL_DEFAULT_PAGE_SKIN") != null ? config
				.getConfigPropertyValue("SCHOOL_DEFAULT_PAGE_SKIN") : "emeraldTown");

		if (user1.getSchool().getGenerateRandomPassword()) {
			user.setPassword(Utils.getRandomString(Integer.parseInt(config
					.getConfigPropertyValue("SCHOOL_DEFAULT_PASSWORD_LENGTH") != null ? config
					.getConfigPropertyValue("SCHOOL_DEFAULT_PASSWORD_LENGTH")
					: "6")));
		} else {
			user.setPassword(config
					.getConfigPropertyValue("SCHOOL_DEFAULT_PASSWORD"));
		}

		if (user.getId() != null)
			update(user, user1);
		else {
			save(user, user1);
			RolesUser ru = new RolesUser();
			ru.setUser(user);
			ru.setRoles((Roles) baseDao.findByColumn(Roles.class, "roleCode",
					Constants.TEACHER_ROLE_CODE, user1.getSchool()));
			baseDao.save(ru, user1);
		}

		return user;
	}

	public List<TuitionEnrollment> getStudentPaymentsForTuition(
			Student student, Tuition t, School school) {
		// TODO Auto-generated method stub
		return baseDao.getStudentPaymentsForTuition(student, t, school);
	}

	public User getAdminUser(School school) {
		return baseDao.getAdminUser(school);
	}

	public List<String> getSubjectsForLevel(LevelClass levelClass) {
		// TODO Auto-generated method stub
		return baseDao.getSubjectsForLevel(levelClass);
	}

	public List<BaseEntity> getStudentMarks(Student student) {
		return baseDao.getStudentMarks(student);
	}

	public List<BaseEntity> getStudentTop10Marks(Student student) {
		return baseDao.getStudentTop10Marks(student);
	}

	public List<Menu> loadAllMenus(School school) {
		return baseDao.loadAllMenus(school);
	}

	public List<User> loadAllUsers(School school) {
		return baseDao.loadAllUsers(school);
	}

	public List getMenusFromRolesMenus(List<BaseEntity> rolesMenus) {
		ArrayList<Menu> selectedMenus = new ArrayList<Menu>();
		for (BaseEntity entity : rolesMenus) {
			RolesMenu rolesMenu = (RolesMenu) entity;
			Menu m = rolesMenu.getMenu();
			m.setAccessLevelCheck(rolesMenu.getAccessLevel().intValue() == 1 ? true
					: false);
			selectedMenus.add(m);
		}

		return selectedMenus;
	}

	public List getUsersFromRolesUsers(List<BaseEntity> rolesUsers) {
		ArrayList<User> selectedUsers = new ArrayList<User>();
		for (BaseEntity entity : rolesUsers) {
			RolesUser rolesUser = (RolesUser) entity;
			User u = rolesUser.getUser();
			selectedUsers.add(u);
		}

		return selectedUsers;
	}

	public List<BaseEntity> getUsers(Class cl, String userName,
			String lastName, String firstName, User user) {
		return baseDao.getUsers(cl, userName, lastName, firstName, user);
	}

	public List<BaseEntity> getMenus(Class cl, String menuName) {
		return baseDao.getMenus(cl, menuName);
	}

	public Averages getOneAverage(SchoolYear schoolYear, LevelClass lclass,
			Term term) {
		// TODO Auto-generated method stub
		return baseDao.getOneAverage(schoolYear, lclass, term);
	}

	public List<Averages> getAverages(SchoolYear schoolYear, LevelClass lclass,
			Term term) {

		return baseDao.getAverages(schoolYear, lclass, term);
	}

	public List<Averages> getAverages(SchoolYear schoolYear, LevelClass lclass,
			Term term, Long studentId) {

		return baseDao.getAverages(schoolYear, lclass, term, studentId);
	}
	
	public List<Averages> getAverages(SchoolYear schoolYear, LevelClass lclass,
			Long studentId) {
		return baseDao.getAverages(schoolYear, lclass, studentId);
	}
	
	public void calculateAverages(SchoolYear schoolYear, LevelClass lclass,
			Term term, User user,Config config) {

		//List<Averages> averages = baseDao.getAverages(schoolYear, lclass, term);
		
		String sqlQuery = "DELETE FROM Averages WHERE (1 = 1) "
				+ " AND schoolYear.id = " + schoolYear.getId()
				+ " AND term.id = " + term.getId()
				+ " AND school.id = " + lclass.getSchool().getId()
				+ " AND class_name = '" + lclass.getName().replace("'", "''") + "'";
		baseDao.executeDeleteQuery(sqlQuery);
		
		sqlQuery = "DELETE FROM Bulletin WHERE (1 = 1) "
				+ " AND schoolYear.id = " + schoolYear.getId()
				+ " AND term.id = " + term.getId()
				+ " AND school.id = " + lclass.getSchool().getId()
				+ " AND class_name = '" + lclass.getName().replace("'", "''") + "'";
		baseDao.executeDeleteQuery(sqlQuery);		
		baseDao.flush();
		
		Map<Integer, Term> termMap = new HashMap<Integer, Term>();
		Map<Integer, Student> studentMap = new HashMap<Integer, Student>();
		Map<Integer, Teacher> teacherMap = new HashMap<Integer, Teacher>();
		Map<String, Subject> subjectMap = new HashMap<String, Subject>();
		Map<String, Course> courseMap = new HashMap<String, Course>();

		TermResult tr = fetchTermResults(schoolYear, lclass, term);
		List<Averages> allAvgs= new ArrayList<Averages>();
		if (tr != null) {
			tr.rank();
			List<TermResultSummary> rss = tr.getTermResultSmry();
			List<BaseEntity> grades = loadAll(Grade.class, lclass.getSchool());

			for (TermResultSummary rs : rss) {

				Bulletin bull = new Bulletin();
				bull.setClassName(tr.getClassName());
				bull.setNbrStudent(tr.getNbrOfStudent());
				bull.setMark(rs.getMoyenne());
				bull.setStatus(AverageStatus.CREATED_UNLOCKED.getValue());
				
				if (termMap.get(tr.getTermId()) == null)
					termMap.put(tr.getTermId(), new Term(new Long(tr.getTermId())));
				bull.setTerm(termMap.get(tr.getTermId()));
				
				bull.setLevel(lclass.getLevel());
				bull.setSchoolYear(schoolYear);
				
				if (studentMap.get(rs.getStudentId()) == null)
					studentMap.put(rs.getStudentId(), new Student(rs.getStudentId().longValue()));			
				bull.setStudent(studentMap.get(rs.getStudentId()));		
				
				Grade ggg = findGrade(grades, lclass.getSchool(), rs.getMoyenne(), 20);
				bull.setGradeName(ggg == null ? null : ggg.getName());
				bull.setRankNbr(rs.getRank());
				bull.setSchool(lclass.getSchool());
				
				if (bull.getMark() < 10) {
					bull.setDecision("ECHEC");
					bull.setDecisionCode(2);
				} else {
					bull.setDecision("SUCCES");
					bull.setDecisionCode(1);
				}

				baseDao.save(bull, user);

				List<TermResultDtl> trDtls = rs.getTermResultDtl();

				for (TermResultDtl trDtl : trDtls) {
					Averages avg = new Averages();
					avg.setClassName(tr.getClassName());
					avg.setSchool(lclass.getSchool());
					avg.setSchoolYear(schoolYear);
					avg.setLevel(lclass.getLevel());
					avg.setStatus(AverageStatus.CREATED_UNLOCKED.getValue());
					
					if (termMap.get(tr.getTermId()) == null)
						termMap.put(tr.getTermId(), new Term(new Long(tr.getTermId())));
					
					avg.setTerm(termMap.get(tr.getTermId()));					
					avg.setNbrStudent(tr.getNbrOfStudent());
					avg.setAverageMark(trDtl.getMoyenne());
					avg.setClassMark(trDtl.getMoyenneClasse());
					avg.setClassRatio(trDtl.getRatioClass());
					avg.setExamMark(trDtl.getMoyenneExam());
					avg.setExamRatio(trDtl.getRatioExam());
					avg.setMaxMark(trDtl.getMaxMark());
					
					if (subjectMap.get(trDtl.getSubjectName()) == null)
						subjectMap.put(trDtl.getSubjectName(), (Subject) baseDao.findByName(Subject.class,
								trDtl.getSubjectName(), getDefaultSchool()));
					avg.setSubject(subjectMap.get(trDtl.getSubjectName()));	
					
					if (teacherMap.get(trDtl.getTeacherId()) == null)
						teacherMap.put(trDtl.getTeacherId(), new Teacher(trDtl.getTeacherId().longValue()));
					avg.setTeacher(teacherMap.get(trDtl.getTeacherId()));
					
					if (studentMap.get(rs.getStudentId()) == null)
						studentMap.put(rs.getStudentId(), new Student(rs.getStudentId().longValue()));
					avg.setStudent(studentMap.get(rs.getStudentId()));		

					if (courseMap.get(tr.getClassName() + trDtl.getSubjectName()) == null)
						courseMap.put(tr.getClassName() + trDtl.getSubjectName(), (Course) findByParents(Course.class,
								"levelClass", tr.getClassName(), "subject", trDtl.getSubjectName()));
					Course course =  courseMap.get(tr.getClassName() + trDtl.getSubjectName());				
					if(course != null)
						avg.setSubjectGroup(course.getGroupCode());
					
					Grade gg = findGrade(grades, lclass.getSchool(), trDtl.getMoyenne(), trDtl.getMaxMark());
					avg.setGradeName(gg == null ? null : gg.getName());
					allAvgs.add(avg);
					//baseDao.save(avg, user);
				}

			}

		}
		Collections.sort(allAvgs);
		int rank=1;
		String subject="";
		for(Averages avg:allAvgs){
			
			if(!avg.getSubject().getName().equals(subject)){
				rank=1;
			}
			avg.setRankNbr(rank++);
			baseDao.save(avg, user);
			subject=avg.getSubject().getName();
		}
	}

	public List<BaseEntity> getStudentBulletins(Student student) {

		return baseDao.getStudentBulletins(student);
	}

	public void publishResults(SchoolYear schoolYear, LevelClass lclass,
			Term term, User currentUser) {

		List<Averages> averages = baseDao.getAverages(schoolYear, lclass, term);

		for (Averages avg : averages) {
			if (avg.getStatus().equals(AverageStatus.CREATED_LOCKED.getValue())) {
				avg.setStatus(AverageStatus.PUBLISHED_LOCKED.getValue());
				baseDao.update(avg, currentUser);
			} else if (avg.getStatus().equals(
					AverageStatus.CREATED_UNLOCKED.getValue())) {
				avg.setStatus(AverageStatus.PUBLISHED_UNLOCKED.getValue());
				baseDao.update(avg, currentUser);
			}
		}

		List<Bulletin> bulls = baseDao.getBulletins(schoolYear, lclass, term);

		for (Bulletin bull : bulls) {
			if (bull.getStatus()
					.equals(AverageStatus.CREATED_LOCKED.getValue())) {
				bull.setStatus(AverageStatus.PUBLISHED_LOCKED.getValue());
				baseDao.update(bull, currentUser);
			} else if (bull.getStatus().equals(
					AverageStatus.CREATED_UNLOCKED.getValue())) {
				bull.setStatus(AverageStatus.PUBLISHED_UNLOCKED.getValue());
				baseDao.update(bull, currentUser);
			}
		}

	}

	public void lockUnlockResults(SchoolYear schoolYear, LevelClass lclass,
			Term term, User currentUser) {

		List<Averages> averages = baseDao.getAverages(schoolYear, lclass, term);

		for (Averages avg : averages) {
			if (avg.getStatus().equals(AverageStatus.CREATED_LOCKED.getValue())) {
				avg.setStatus(AverageStatus.CREATED_UNLOCKED.getValue());
				baseDao.update(avg, currentUser);
			} else if (avg.getStatus().equals(
					AverageStatus.CREATED_UNLOCKED.getValue())) {
				avg.setStatus(AverageStatus.CREATED_LOCKED.getValue());
				baseDao.update(avg, currentUser);
			} else if (avg.getStatus().equals(
					AverageStatus.PUBLISHED_LOCKED.getValue())) {
				avg.setStatus(AverageStatus.PUBLISHED_UNLOCKED.getValue());
				baseDao.update(avg, currentUser);
			} else if (avg.getStatus().equals(
					AverageStatus.PUBLISHED_UNLOCKED.getValue())) {
				avg.setStatus(AverageStatus.PUBLISHED_LOCKED.getValue());
				baseDao.update(avg, currentUser);
			}
		}

		List<Bulletin> bulls = baseDao.getBulletins(schoolYear, lclass, term);

		for (Bulletin bull : bulls) {
			if (bull.getStatus()
					.equals(AverageStatus.CREATED_LOCKED.getValue())) {
				bull.setStatus(AverageStatus.CREATED_UNLOCKED.getValue());
				baseDao.update(bull, currentUser);
			} else if (bull.getStatus().equals(
					AverageStatus.CREATED_UNLOCKED.getValue())) {
				bull.setStatus(AverageStatus.CREATED_LOCKED.getValue());
				baseDao.update(bull, currentUser);
			} else if (bull.getStatus().equals(
					AverageStatus.PUBLISHED_LOCKED.getValue())) {
				bull.setStatus(AverageStatus.PUBLISHED_UNLOCKED.getValue());
				baseDao.update(bull, currentUser);
			} else if (bull.getStatus().equals(
					AverageStatus.PUBLISHED_UNLOCKED.getValue())) {
				bull.setStatus(AverageStatus.PUBLISHED_LOCKED.getValue());
				baseDao.update(bull, currentUser);
			}
		}
	}

	public Grade findGrade(School school, Double moyenne, Integer maxMark) {
		// TODO Auto-generated method stub

		// ramener le resultat sur 20
		Double moyenneSur20 = moyenne * 20.0 / maxMark;

		List<BaseEntity> grades = loadAll(Grade.class, school);

		for (BaseEntity grade : grades) {

			Grade grd = (Grade) grade;
			if (moyenneSur20 >= grd.getBeginRange()
					&& moyenneSur20 <= grd.getEndRange()) {
				return grd;
			}
		}

		return null;
	}

	public Grade findGrade(List<BaseEntity> grades, School school, Double moyenne, Integer maxMark) {
		// TODO Auto-generated method stub

		// ramener le resultat sur 20
		Double moyenneSur20 = moyenne * 20.0 / maxMark;

		//List<BaseEntity> grades = loadAll(Grade.class, school);

		for (BaseEntity grade : grades) {

			Grade grd = (Grade) grade;
			if (moyenneSur20 >= grd.getBeginRange()
					&& moyenneSur20 <= grd.getEndRange()) {
				return grd;
			}
		}

		return null;
	}
	public List<Object[]> getReportParameterValues(String sql) {
		return baseDao.getReportParameterValues(sql);
	}

	public School findSchoolByName(String name) {
		return baseDao.findSchoolByName(name);
	}

	public String generateMatricule(Class cl, String firstName,
			String lastName, Date birthDate) {
		String mat = null;
		if (lastName != null && firstName != null) {
			if (lastName.length() >= 3) {
				mat = lastName.substring(0, 3) + firstName.substring(0, 1);
			} else if (firstName.length() >= 2) {
				mat = lastName + firstName.substring(0, 2);
			} else {
				mat = lastName + firstName;
			}
			mat = mat
					+ (birthDate.getDate() < 10 ? "0" + birthDate.getDate()
							: birthDate.getDate());
			mat = mat
					+ (birthDate.getMonth() + 1 < 10 ? "0"
							+ (birthDate.getMonth() + 1) : (birthDate
							.getMonth() + 1));
		}
		boolean exists = true;
		int i = 0;
		while (exists && i <= 99) {
			String matt = mat + (i < 10 ? "0" + i : i);
			i++;
			if (findByColumn(cl, "matricule", matt) == null) {
				exists = false;
				mat = matt;
			}
		}
		if (exists) {

			int ii = 0;
			while (exists && ii <= 999999999) {
				String matt = mat + Utils.getRandomString(2);
				ii++;
				if (findByColumn(cl, "matricule", matt) == null) {
					exists = false;
					mat = matt;
				}
			}
		}

		return mat == null ? null : mat.toUpperCase();
	}

	public List<BaseEntity> getTimeTablesByTeacherId(Class cl, Long teacherId,
			Long yearId, Long termId) {
		return baseDao.getTimeTablesByTeacherId(cl, teacherId, yearId, termId);
	}

	public List<BaseEntity> getMedicalVisits(Date beginVisitDate,
			Date endVisitDate) {
		return baseDao.getMedicalVisits(beginVisitDate, endVisitDate);
	}

	public List<Files> getStudentFiles(Student student) {
		return baseDao.getStudentFiles(student);
	}

	public Double getCummulatedRatio(Long schoolYearId, Long termId,
			Long courseId, Long examId) {
		return baseDao.getCummulatedRatio(schoolYearId, termId, courseId,
				examId);
	}

	public Term getCurrentTermForClass(SchoolYear year, LevelClass lClass) {
		return baseDao.getCurrentTermForClass(year, lClass);
	}

	public YearSummary getOneYearSummary(SchoolYear schoolYear, User user) {
		// TODO Auto-generated method stub
		return baseDao.getOneYearSummary(schoolYear, user);
	}

	public void calculateYearSummary(SchoolYear schoolYear, User currentUser,
			Config config, String className) {
		// TODO Auto-generated method stub
		//List<YearSummary> yearSummaries = baseDao.getYearSummaries(schoolYear,
		//		currentUser);

		//for (YearSummary smry : yearSummaries)
		//	baseDao.delete(smry);

		String sqlQuery = "DELETE FROM YearSummary WHERE (1 = 1) "
				+ "AND schoolYear.id = " + schoolYear.getId();
				
		if (StringUtils.isNotEmpty(className)) {
			sqlQuery += (" AND class_name = '" + className.replace("'", "''") + "'");
		}
				
		baseDao.executeDeleteQuery(sqlQuery);		
		baseDao.flush();

		List<Bulletin> bulletins = baseDao.getAllBulletins(schoolYear, currentUser, className);
		
		Map<Long, Level> levelMap = new HashMap<Long, Level>();
		Map<Long, Student> studentMap = new HashMap<Long, Student>();
		
		Long preStud = 0L;
		YearSummary ySmry = null;
		int nbrTerm = 0;
		List<YearSummary> ySmries = new ArrayList<YearSummary>();
		for (Bulletin bul : bulletins) {

			if (!(bul.getStudentId().toString().equals(preStud.toString()))) {
					nbrTerm = 0;
				
				ySmry = new YearSummary();
				ySmry.setClassName(bul.getClassName());
				ySmry.setNbrStudent(bul.getNbrStudent());
				ySmry.setSchoolYear(schoolYear);
				
				if (levelMap.get(bul.getLevelId()) == null)
					levelMap.put(bul.getLevelId(), new Level(bul.getLevelId()));
				
				ySmry.setLevel(levelMap.get(bul.getLevelId()));
				ySmry.setStatus(AverageStatus.CREATED_UNLOCKED.getValue());
				
				if (studentMap.get(bul.getStudentId()) == null)
					studentMap.put(bul.getStudentId(), new Student(bul.getStudentId()));

				ySmry.setStudent(studentMap.get(bul.getStudentId()));
				ySmry.setSchool(currentUser.getSchool());
				ySmry.setMark(bul.getMark());
				ySmries.add(ySmry);
				nbrTerm++;
			} else {
				ySmry.setMark(ySmry.getMark() + bul.getMark());
				nbrTerm++;
			}
			if(ySmry!=null){
				ySmry.setNbrTerms(nbrTerm);
			}
			preStud = bul.getStudentId();
		}
		Collections.sort(ySmries);
		int r = 1;
		List<BaseEntity> grades = loadAll(Grade.class, currentUser.getSchool());

		for (YearSummary smry : ySmries) {
			smry.setMark(smry.getMark() / smry.getNbrTerms());
			Grade gg = findGrade(grades, currentUser.getSchool(), smry.getMark(), 20);
			smry.setGradeName(gg == null ? null : gg.getName());
			if (smry.getMark() < 10) {
				smry.setDecision("ECHEC");
				smry.setDecisionCode(2);
			} else {
				smry.setDecision("SUCCES");
				smry.setDecisionCode(1);
			}
			smry.setRankNbr(r++);
			baseDao.save(smry, currentUser);
		}

	}

	public List<YearSummary> getYearSummaries(SchoolYear schoolYear,
			LevelClass lclass, School school) {
		// TODO Auto-generated method stub
		return baseDao.getYearSummaries(schoolYear, lclass, school);
	}

	public List<BaseEntity> getTeachersSortedByPosition(School school) {
		// TODO Auto-generated method stub
		return baseDao.getTeachersSortedByPosition(school);
	}

	public List<BaseEntity> getEntitiesByQueryAndParameters(School school,
			String queryName, String[] paramNames, Object[] paramValues) {
		return baseDao.getEntitiesByQueryAndParameters(school, queryName,
				paramNames, paramValues);
	}

	public List<News> getAllSortedNews(School school) {
		// TODO Auto-generated method stub
		return baseDao.getAllSortedNews(school);
	}

	public List<BaseEntity> getThisYearCurriculum(School defaultSchool) {
		// TODO Auto-generated method stub
		return baseDao.getThisYearCurriculum(defaultSchool);
	}

	public List<BaseEntity> fetchCurrentYearTuitions(School defaultSchool) {
		// TODO Auto-generated method stub
		return baseDao.fetchCurrentYearTuitions(defaultSchool);
	}
	
	public List<Tuition> fetchTuitionsForYear(School defaultSchool,String year) {
		// TODO Auto-generated method stub
		return baseDao.fetchTuitionsForYear(defaultSchool,year);
	}

	public List<BaseEntity> getThisYearFurnitures(School defaultSchool) {
		// TODO Auto-generated method stub
		return baseDao.getThisYearFurnitures(defaultSchool);
	}

	public List<MarkVO> getMarks(Long studentId) {
		return baseDao.getMarks(studentId);
	}

	public StudentVO getStudentVOFromStudent(Student student) {
		StudentVO studentVO = new StudentVO();
		studentVO.setId(student.getId());
		studentVO.setLastName(student.getLastName());
		studentVO.setFirstName(student.getFirstName());
		studentVO.setMatricule(student.getMatricule());
		studentVO.setSchoolName(student.getSchool().getName());
		studentVO.setLevelClassName(student.getCurrentEnrollment()
				.getLevelClass().getName());
		studentVO.setPhone(student.getPhone());
		studentVO.setCellPhone(student.getCellPhone());
		studentVO.setStatus(student.getStatus());
		studentVO.setCountryId(student.getCountry().getId());
		studentVO.setCountryName(student.getCountry().getName());
		studentVO.setReligionId(student.getReligion().getId());
		studentVO.setReligionName(student.getReligion().getName());
		studentVO.setSchoolId(student.getSchool().getId());

		return studentVO;
	}

	public TeacherVO getTeacherVOFromTeacher(Teacher teacher) {
		TeacherVO teacherVO = new TeacherVO();
		teacherVO.setId(teacher.getId());
		teacherVO.setLastName(teacher.getLastName());
		teacherVO.setFirstName(teacher.getFirstName());
		teacherVO.setMatricule(teacher.getMatricule());
		teacherVO.setSchoolName(teacher.getSchool().getName());
		teacherVO.setPhone(teacher.getPhone());
		teacherVO.setCellPhone(teacher.getCellPhone());
		teacherVO.setStatus(teacher.getStatus());
		teacherVO.setCountryId(teacher.getCountry().getId());
		teacherVO.setCountryName(teacher.getCountry().getName());
		teacherVO.setReligionId(teacher.getReligion().getId());
		teacherVO.setReligionName(teacher.getReligion().getName());
		teacherVO.setSchoolId(teacher.getSchool().getId());

		return teacherVO;
	}

	public List<TeacherVO> getTeachersOrderByPosition(Class cl,
			String matricule, String lastName, String firstName, User user,
			boolean orderByPosition, School school) {
		return baseDao.getTeachersOrderByPosition(cl, matricule, lastName,
				firstName, user, orderByPosition, school);
	}

	public SchoolYear getSchoolYear(Date eventDate, School defaultSchool) {
		// TODO Auto-generated method stub
		return baseDao.getSchoolYear(eventDate, defaultSchool);
	}

	public void calculateGroupSummary(SchoolYear schoolYear,
			TermGroup termGroup, User currentUser, Config config, String className) throws Exception {
		// TODO Auto-generated method stub
		//List<TermGroupSummary> groupSummaries = baseDao.getTermGroupSummaries(schoolYear,termGroup,
		//		currentUser);

		//for (TermGroupSummary smry : groupSummaries)
		//	baseDao.delete(smry);
		Map<String, Long> parentIds = new HashMap<String, Long>();
		parentIds.put("termGroup", termGroup.getId());
		baseDao.deleteByParentIds("TermGroupSummary", parentIds);
		baseDao.flush();
	

		List<Bulletin> bulletins = baseDao.getAllBulletins(schoolYear, termGroup, currentUser, className);
		
		Map<Long, Level> levelMap = new HashMap<Long, Level>();
		Map<Long, Student> studentMap = new HashMap<Long, Student>();
		
		Long preStud = 0L;
		TermGroupSummary ySmry = null;
		int nbrTerm = 0;
		List<TermGroupSummary> ySmries = new ArrayList<TermGroupSummary>();
		for (Bulletin bul : bulletins) {

			if (!(bul.getStudentId().toString().equals(preStud.toString()))) {
				
				nbrTerm = 0;
			
				ySmry = new TermGroupSummary();
				ySmry.setClassName(bul.getClassName());
				ySmry.setNbrStudent(bul.getNbrStudent());
				ySmry.setSchoolYear(schoolYear);
				
				if (levelMap.get(bul.getLevelId()) == null)
					levelMap.put(bul.getLevelId(), new Level(bul.getLevelId()));
				
				ySmry.setLevel(levelMap.get(bul.getLevelId()));
				ySmry.setStatus(AverageStatus.CREATED_UNLOCKED.getValue());
				
				if (studentMap.get(bul.getStudentId()) == null)
					studentMap.put(bul.getStudentId(), new Student(bul.getStudentId()));

				ySmry.setStudent(studentMap.get(bul.getStudentId()));
				ySmry.setSchool(currentUser.getSchool());
				ySmry.setMark(bul.getMark());
				ySmries.add(ySmry);
				nbrTerm++;
			} else {
				ySmry.setMark(ySmry.getMark() + bul.getMark());
				nbrTerm++;
			}
			
			if (ySmry != null) {
				ySmry.setNbrTerms(nbrTerm);
			}
			
			preStud = bul.getStudentId();
		}
		Collections.sort(ySmries);
		int r = 1;
		List<BaseEntity> grades = loadAll(Grade.class, currentUser.getSchool());
		for (TermGroupSummary smry : ySmries) {
			smry.setMark(smry.getMark() / smry.getNbrTerms());
			Grade gg = findGrade(grades, currentUser.getSchool(), smry.getMark(), 20);
			smry.setGradeName(gg == null ? null : gg.getName());
			if (smry.getMark() < 10) {
				smry.setDecision("ECHEC");
				smry.setDecisionCode(2);
			} else {
				smry.setDecision("SUCCES");
				smry.setDecisionCode(1);
			}
			smry.setRankNbr(r++);
			smry.setTermGroup(termGroup);
			baseDao.save(smry, currentUser);
		}

		//baseDao.save(ySmries, currentUser);
	}
	
	
	
	public List<TermGroupSummary> getTermGroupSummaries(SchoolYear schoolYear, LevelClass lClass,
			TermGroup termGroup, User currentUser) {
		return baseDao.getTermGroupSummaries(schoolYear, lClass, termGroup, currentUser);
	}
	
	public List<Bulletin> getTermSummaries(SchoolYear schoolYear, LevelClass lClass,
			Term term, User currentUser) {
		return baseDao.getBulletins(schoolYear, lClass, term);
	}
	
	public List<Bulletin> getBulletins(Long studentId, String year) {
		return baseDao.getBulletins(studentId, year);
	}

	@Override
	public void saveStudentTuitions(List<Student> selectedStudents,List<Tuition> selectedTuitions,
			User user) {
		// TODO Auto-generated method stub
		if (selectedTuitions != null)
			for (Student student : selectedStudents) {
				
				for(Tuition t:selectedTuitions){
					if (baseDao.getStudentTuition(student.getId(),
							t.getId()) == null) {
						StudentTuition st= new StudentTuition();
						st.setCreateDate(new Date());
						st.setModDate(new Date());
						st.setStudent(student);
						st.setTuition(t);
						
						baseDao.save(st, user);
					}
					
				}
			}
	}

	@Override
	public List<Tuition> fetchSortedTuitions(School defaultSchool, String schoolYearName) {
		// TODO Auto-generated method stub
		return  baseDao.fetchSortedTuitions(defaultSchool, schoolYearName);
	}
	
	public List<BaseEntity> searchParents(Parent parent, School school) {
		return  baseDao.searchParents(parent, school);
	}
	
	public List<Parent> searchParentsToAssign(Parent parent, School school) {
		return  baseDao.searchParentsToAssign(parent, school);
	}
	
	public void getStudentTuitionsByTuitionType(Student student, School school, Long tuitionTypeId, Long yearId) {
		List<Long> studentIds = new ArrayList<Long>();
		studentIds.add(student.getId());
		student.setTotalAmountByType(0);
		student.setTotalPaidByType(0);
		student.setTotalRebateByType(0);
		
		List<Tuition> tuitions = baseDao.getStudentsPaymentsByTuitionType(studentIds, school, tuitionTypeId, yearId);
		for (Tuition tu : tuitions) {
			double tuAmount  = tu.getAmount() != null ? tu.getAmount() : 0;
			double tuPaid = tu.getPaid() != null ? tu.getPaid() : 0;
			double tuRebate = tu.getRebate() != null ? tu.getRebate() : 0;
			tu.setPaid(tuPaid);
			tu.setRebate(tuRebate);
			tu.setBalance(tu.getAmount() - tu.getPaid() - tu.getRebate());
			student.setTotalAmountByType(student.getTotalAmountByType() + tu.getAmount());
			student.setTotalPaidByType(student.getTotalPaidByType() + tu.getPaid());
			student.setTotalRebateByType(student.getTotalRebateByType() + tu.getRebate());
		}
		student.setTotalBalanceByType(student.getTotalAmountByType() - student.getTotalPaidByType() - student.getTotalRebateByType());
		student.setTuitions(tuitions);
	}
	
	public void getStudentPaymentsByTuitionTypes(Student student, List<Long> studentIds, School school, Long yearId, List<Integer> errors) {
		List<Payment> payments = baseDao.getParentPaymentsByStudents(studentIds, school, yearId);
				
		List<TuitionType> studentTuitionTypes = new ArrayList<TuitionType>();
		Map<Long, TuitionType> tuitionTypeMap = new HashMap<Long, TuitionType>();
		TuitionType tuitionType;
		
		List<Tuition> tuitionTypeTuitions;
		Tuition tuition = null;
		
		List<Payment> tuitionPayments;
		
		for (Payment pmt : payments) {
			tuitionType = tuitionTypeMap.get(pmt.getTuitionTypeId());
			if (tuitionType == null) {
				tuitionType = new TuitionType();
				tuitionType.setId(pmt.getTuitionTypeId());
				tuitionType.setName(pmt.getTuitionTypeName());
				tuitionType.setTotalAmount(0);
				tuitionType.setTotalPaid(0);
				tuitionType.setTotalRebate(0);
				tuitionType.setTotalBalance(0);
				tuitionTypeTuitions = new ArrayList<Tuition>();
				tuitionType.setTuitions(tuitionTypeTuitions);
				tuitionTypeMap.put(tuitionType.getId(), tuitionType);
				studentTuitionTypes.add(tuitionType);
			}
				
			tuitionTypeTuitions = tuitionType.getTuitions();
			boolean tuitionFound = false;
			for (Tuition tu : tuitionTypeTuitions) {
				if (tu.getId().toString().equals(pmt.getTuitionId().toString())) {
					tuitionFound = true;
					tuition = tu;
				}
			}
			if (!tuitionFound) {
				tuition = new Tuition();
				tuition.setId(pmt.getTuitionId());
				tuition.setDescription(pmt.getTuitionDescription());
				tuition.setDueDate(pmt.getTuitionDueDate());
				tuition.setAmount(pmt.getTuitionAmount());
				tuition.setPayments(new ArrayList<Payment>());
				tuitionTypeTuitions.add(tuition);
			}
			tuition.getPayments().add(pmt);
		}	
		
		calculateTotals(student, studentTuitionTypes);
		student.setTuitionTypes(studentTuitionTypes);
	}
	
	private void calculateTotals(Student student, List<TuitionType> tuitionTypes) {
		student.setTotalAmount(0);
		student.setTotalPaid(0);
		student.setTotalRebate(0);
		student.setTotalBalance(0);
		
		for (TuitionType tt : tuitionTypes) {
			for (Tuition t : tt.getTuitions()) {
				for (Payment p : t.getPayments()) {
					t.setPaid(t.getPaid() + (p.getAmount() != null ? p.getAmount() : 0));
					t.setRebate(t.getRebate() + (p.getRebate() != null ? p.getRebate() : 0));
					t.setBalance(t.getAmount() - t.getPaid() - t.getRebate());
				}
				if (t.getDueDate().compareTo(new Date()) <= 0 && t.getBalance() > 0) {
					t.setHasPastDueAmount(true);
					tt.setHasPastDueAmount(true);
				}
				tt.setTotalAmount(tt.getTotalAmount() + t.getAmount());
				tt.setTotalPaid(tt.getTotalPaid() + t.getPaid());
				tt.setTotalRebate(tt.getTotalRebate() + t.getRebate());
				tt.setTotalBalance(tt.getTotalBalance() + t.getBalance());
			}
				
			student.setTotalAmount(student.getTotalAmount() + tt.getTotalAmount());
			student.setTotalPaid(student.getTotalPaid() + tt.getTotalPaid());
			student.setTotalRebate(student.getTotalRebate() + tt.getTotalRebate());
			student.setTotalBalance(student.getTotalBalance() + tt.getTotalBalance());
		}
	}
	
	public void getParentTuitionsByTuitionType(List<Student> students, Parent parent, List<Long> studentIds, 
			School school, Long tuitionTypeId, Long yearId, Tuition tuition, List<Integer> errors, User currentUser) {
		List<Tuition> tuitions = baseDao.getStudentsPaymentsByTuitionType(studentIds, school, tuitionTypeId, yearId);
		List<StudentVO> studentVOs = new ArrayList<StudentVO>();
		Map<Long, StudentVO> studentVOMap = new HashMap<Long, StudentVO>();
		StudentVO studentVO;
		List<Tuition> studentTuitions;
		
		parent.setTotalAmount(0);
		parent.setTotalPaid(0);
		parent.setTotalRebate(0);
		parent.setTotalBalance(0);
	
		for (Tuition tu : tuitions) {
			studentVO = studentVOMap.get(tu.getStudentId());
			if (studentVO == null) {
				for (Student student : students) {
					if (student.getId().toString().equals(tu.getStudentId().toString())) {
						studentVO = new StudentVO();
						studentVO.setMatricule(student.getMatricule());
						studentVO.setFirstName(student.getFirstName());
						studentVO.setLastName(student.getLastName());
						studentVO.setTotalAmount(0);
						studentVO.setTotalPaid(0);
						studentVO.setTotalRebate(0);
						studentVO.setTotalBalance(0);
						studentTuitions = new ArrayList<Tuition>();
						studentVO.setTuitions(studentTuitions);
						studentVOMap.put(student.getId(), studentVO);
						studentVOs.add(studentVO);
					}
				}
			}
			double tuAmount  = tu.getAmount() != null ? tu.getAmount() : 0;
			double tuPaid = tu.getPaid() != null ? tu.getPaid() : 0;
			double tuRebate = tu.getRebate() != null ? tu.getRebate() : 0;
			tu.setPaid(tuPaid);
			tu.setRebate(tuRebate);
			tu.setBalance(tuAmount - tuPaid - tuRebate);
			
			studentVO.setTotalAmount(studentVO.getTotalAmount() + tuAmount);
			studentVO.setTotalPaid(studentVO.getTotalPaid() + tuPaid);
			studentVO.setTotalRebate(studentVO.getTotalRebate() + tuRebate);
			studentVO.setTotalBalance(studentVO.getTotalBalance() + (tuAmount - tuPaid - tuRebate));
			
			studentVO.getTuitions().add(tu);
			
			parent.setTotalAmount(parent.getTotalAmount() + tuAmount);
			parent.setTotalPaid(parent.getTotalPaid() + tuPaid);
			parent.setTotalRebate(parent.getTotalRebate() + tuRebate);
			parent.setTotalBalance(parent.getTotalBalance() + (tuAmount - tuPaid - tuRebate));

		}	
		
		parent.setStudentVOs(studentVOs);
	}
	
	public void getParentPaymentsByStudents(Parent parent, List<Student> students, List<Long> studentIds, 
			School school, Long yearId, List<Integer> errors) {
		if(studentIds==null||studentIds.size()<=0){
			return;
		}
		List<Payment> payments = baseDao.getParentPaymentsByStudents(studentIds, school, yearId);
		
		List<StudentVO> studentVOs = new ArrayList<StudentVO>();
		Map<Long, StudentVO> studentVOMap = new HashMap<Long, StudentVO>();
		StudentVO studentVO;
		
		List<TuitionType> studentTuitionTypes;
		TuitionType tuitionType;
		
		List<Tuition> tuitionTypeTuitions;
		Tuition tuition = null;
		
		List<Payment> tuitionPayments;
		
		for (Payment pmt : payments) {
			studentVO = studentVOMap.get(pmt.getStudentId());
			if (studentVO == null) {
				for (Student student : students) {
					if (student.getId().toString().equals(pmt.getStudentId().toString())) {
						studentVO = new StudentVO();
						studentVO.setMatricule(student.getMatricule());
						studentVO.setFirstName(student.getFirstName());
						studentVO.setLastName(student.getLastName());
						studentVO.setTotalAmount(0);
						studentVO.setTotalPaid(0);
						studentVO.setTotalRebate(0);
						studentVO.setTotalBalance(0);
						studentTuitionTypes = new ArrayList<TuitionType>();
						studentVO.setTuitionTypes(studentTuitionTypes);
						studentVOMap.put(student.getId(), studentVO);
						studentVOs.add(studentVO);
					}
				}
			}
			
			studentTuitionTypes = studentVO.getTuitionTypes();
			boolean tuitionTypeFound = false;
			for (TuitionType tt : studentTuitionTypes) {
				if (tt.getId().toString().equals(pmt.getTuitionTypeId().toString())) {
					tuitionTypeFound = true;
					boolean tuitionFound = false;
					for (Tuition t : tt.getTuitions()) {
						if (t.getId().toString().equals(pmt.getTuitionId().toString())) {
							tuition = t;
							tuitionFound = true;
						}
					}
					if (!tuitionFound) {
						tuition = new Tuition();
						tuition.setId(pmt.getTuitionId());
						tuition.setDescription(pmt.getTuitionDescription());
						tuition.setDueDate(pmt.getTuitionDueDate());
						tuition.setAmount(pmt.getTuitionAmount());
						tuition.setPayments(new ArrayList<Payment>());
						tt.getTuitions().add(tuition);
					}
					tuition.getPayments().add(pmt);
				}
			}
			
			if (!tuitionTypeFound) {
				tuitionType = new TuitionType();
				tuitionType.setId(pmt.getTuitionTypeId());
				tuitionType.setName(pmt.getTuitionTypeName());
				tuitionType.setTuitions(new ArrayList<Tuition>());
				tuition = new Tuition();
				tuition.setId(pmt.getTuitionId());
				tuition.setDescription(pmt.getTuitionDescription());
				tuition.setDueDate(pmt.getTuitionDueDate());
				tuition.setAmount(pmt.getTuitionAmount());
				tuition.setPayments(new ArrayList<Payment>());
				tuition.getPayments().add(pmt);
				tuitionType.getTuitions().add(tuition);
				studentTuitionTypes.add(tuitionType);
			}
		}	
		
		calculateTotals(parent, studentVOs);
		parent.setStudentVOs(studentVOs);
	}

	private void calculateTotals(Parent parent, List<StudentVO> studentVOs) {
		parent.setTotalAmount(0);
		parent.setTotalPaid(0);
		parent.setTotalRebate(0);
		parent.setTotalBalance(0);
		
		for (StudentVO stVO : studentVOs) {
			for (TuitionType tt : stVO.getTuitionTypes()) {
				for (Tuition t : tt.getTuitions()) {
					for (Payment p : t.getPayments()) {
						t.setPaid(t.getPaid() + (p.getAmount() != null ? p.getAmount() : 0));
						t.setRebate(t.getRebate() + (p.getRebate() != null ? p.getRebate() : 0));
						t.setBalance(t.getAmount() - t.getPaid() - t.getRebate());
					}
					if (t.getDueDate().compareTo(new Date()) <= 0 && t.getBalance() > 0) {
						t.setHasPastDueAmount(true);
						tt.setHasPastDueAmount(true);
					}
					tt.setTotalAmount(tt.getTotalAmount() + t.getAmount());
					tt.setTotalPaid(tt.getTotalPaid() + t.getPaid());
					tt.setTotalRebate(tt.getTotalRebate() + t.getRebate());
					tt.setTotalBalance(tt.getTotalBalance() + t.getBalance());
				}
				
				if (tt.isHasPastDueAmount()) 
					stVO.setHasPastDueAmount(true);
				stVO.setTotalAmount(stVO.getTotalAmount() + tt.getTotalAmount());
				stVO.setTotalPaid(stVO.getTotalPaid() + tt.getTotalPaid());
				stVO.setTotalRebate(stVO.getTotalRebate() + tt.getTotalRebate());
				stVO.setTotalBalance(stVO.getTotalBalance() + tt.getTotalBalance());
			}
			parent.setTotalAmount(parent.getTotalAmount() + stVO.getTotalAmount());
			parent.setTotalPaid(parent.getTotalPaid() + stVO.getTotalPaid());
			parent.setTotalRebate(parent.getTotalRebate() + stVO.getTotalRebate());
			parent.setTotalBalance(parent.getTotalBalance() + stVO.getTotalBalance());
		}
	}
	
	public Payment saveStudentsPaymentsByTuitionType(List<Long> studentIds, School school, Long tuitionTypeId, 
			Long yearId, Tuition tuition, List<Integer> errors, User currentUser) {
		List<Tuition> tuitions = baseDao.getStudentsPaymentsByTuitionType(studentIds, school, tuitionTypeId, yearId);
		double totalLeftToPaid = 0;
		for (Tuition tu : tuitions) {
			double tuAmount  = tu.getAmount() != null ? tu.getAmount() : 0;
			double tuPaid = tu.getPaid() != null ? tu.getPaid() : 0;
			double tuRebate = tu.getRebate() != null ? tu.getRebate() : 0;
			totalLeftToPaid += tuAmount - tuPaid - tuRebate;
		}
		
		if (tuition.getAmount() + tuition.getRebate() > totalLeftToPaid) {
			errors.add(1);
			return null;
		}
		
		double amountLeft = tuition.getAmount();
		double rebateLeft = tuition.getRebate();
		Payment payment=null;
		StudentEnrollment studentEnrollment; 
		for (Tuition tu : tuitions) {
			if (amountLeft > 0) {
				payment = new Payment();
				payment.setTuition(tu);
				studentEnrollment = new StudentEnrollment();
				studentEnrollment.setId(tu.getEnrollmentId());
				payment.setStudentEnrollment(studentEnrollment);
				payment.setPaymentDate(new Date());
				
				double tuAmount  = tu.getAmount() != null ? tu.getAmount() : 0;
				double tuPaid = tu.getPaid() != null ? tu.getPaid() : 0;
				double tuRebate = tu.getRebate() != null ? tu.getRebate() : 0;
				double tuBalance = tuAmount - tuPaid - tuRebate;
				
				double rebateToBePaid = rebateLeft > tuBalance ? tuBalance : rebateLeft;
				
				double amountToBePaid = amountLeft + rebateToBePaid < tuBalance ? amountLeft : tuBalance - rebateToBePaid;

				rebateLeft -= rebateToBePaid;
				
				payment.setRebate(rebateToBePaid);
				payment.setAmount(amountToBePaid);
				
				amountLeft -= amountToBePaid;
				payment.setComment(tuition.getComment());
				
				if (payment.getAmount() > 0)
					save(payment, currentUser);
			}
		}
 
		
		return payment;
	}
	
	
	public Payment saveStudentsNegativePaymentsByTuitionType(List<Long> studentIds, School school, Long tuitionTypeId, 
			Long yearId, Tuition tuition, List<Integer> errors, User currentUser) {
		List<Payment> payments = baseDao.getPaymentsForStudents(studentIds, tuitionTypeId, yearId, school);
		
		double totalPaid = 0;
		double totalRebatePaid = 0;
		
		for (Payment pmt : payments) {
			totalPaid += pmt.getAmount();
			totalRebatePaid += pmt.getRebate();
		}
		
		if (-1 * tuition.getAmount() > totalPaid) {
			errors.add(2);
			return null;
		}
		if (-1 * tuition.getRebate() > totalRebatePaid) {
			errors.add(3);
			return null;
		}
		
		
		double amountLeft = tuition.getAmount() * -1;
		double rebateLeft = tuition.getRebate() * -1;
		Payment payment = null;
		for (Payment pmnt : payments) {
			if ((pmnt.getAmount() > 0 && amountLeft > 0) || (pmnt.getRebate() > 0 && rebateLeft > 0)) {
				payment = new Payment();
				payment.setTuition(pmnt.getTuition());
				payment.setStudentEnrollment(pmnt.getStudentEnrollment());
				payment.setPaymentDate(new Date());
				
				double amountToBePaid = amountLeft > pmnt.getAmount() ? pmnt.getAmount() : amountLeft;
				payment.setAmount(-1 * amountToBePaid);		
						
				double rebateToBePaid = rebateLeft > pmnt.getRebate() ? pmnt.getRebate() : rebateLeft;
				payment.setRebate(-1 * rebateToBePaid);
				
				rebateLeft -= rebateToBePaid;
				amountLeft -= amountToBePaid;
				
				payment.setComment(tuition.getComment());
				
				if (payment.getAmount() < 0 || payment.getRebate() < 0)
					save(payment, currentUser);
			}
		}
 
		return payment;
	}
	
	
	public Exam getExam(ExamType examType, Term term, SchoolYear schoolYear, Course course, LevelClass levelClass, User currentUser) {
		return baseDao.getExam(examType, term, schoolYear, course, levelClass, currentUser);
	}
	
	public Integer getStudentsCountByLevelClassAndYear(Long schoolYearId, Long levelClassId, School school) {
		return baseDao.getStudentsCountByLevelClassAndYear(schoolYearId, levelClassId, school);
	}
	
	public List<AssignmentFile> getCourseAssignmentFiles(School school, FileSearchVO fileSearch) {
		return baseDao.getCourseAssignmentFiles(school, fileSearch);
	}

	public void addCourseAssignmentFiles(FileSearchVO examFile, List<AssignmentFile> assignedFiles, User user) {
		baseDao.addCourseAssignmentFiles(examFile, assignedFiles, user);
	}
	
	public List<AssignmentFile> searchCourseAssignmentFiles(School school, FileSearchVO fileSearch) {
		return baseDao.searchCourseAssignmentFiles(school, fileSearch);
	}
	
	public StudentEnrollment getStudentEnrollment(Long studentId, Long levelClassId, Long yearId) {
		return baseDao.getStudentEnrollment(studentId, levelClassId, yearId);
	}
	
	public List<CourseVO> getCourses(School school) {
		return baseDao.getCourses(school);
	}
	
	public List<CourseVO> getCourses(School school, Long teacherId) {
		return baseDao.getCourses(school, teacherId);
	}
	
	public List<CourseVO> getCourses(Long classId,School school){
		return baseDao.getCourses(classId, school);
	}
	
	public int deleteExamMark(Long examId) {
		return baseDao.deleteExamMark(examId);
	}
	public Integer countActiveStudent() {
		return baseDao.countActiveStudent();
	}
}
 