package com.esoft.ischool.service;
import java.util.Date;
import java.util.List;

import org.springframework.transaction.annotation.Transactional;

import com.esoft.ischool.model.Averages;
import com.esoft.ischool.job.Config;
import com.esoft.ischool.model.Alert;
import com.esoft.ischool.model.AlertReceiver;
import com.esoft.ischool.model.AssignmentFile;
import com.esoft.ischool.model.BaseEntity;
import com.esoft.ischool.model.Bulletin;
import com.esoft.ischool.model.Configuration;
import com.esoft.ischool.model.Correspondence;
import com.esoft.ischool.model.Course;
import com.esoft.ischool.model.Exam;
import com.esoft.ischool.model.ExamType;
import com.esoft.ischool.model.Files;
import com.esoft.ischool.model.Grade;
import com.esoft.ischool.model.LevelClass;
import com.esoft.ischool.model.Mark;
import com.esoft.ischool.model.News;
import com.esoft.ischool.model.Parent;
import com.esoft.ischool.model.Payment;
import com.esoft.ischool.model.School;
import com.esoft.ischool.model.SchoolYear;
import com.esoft.ischool.model.Student;
import com.esoft.ischool.model.StudentEnrollment;
import com.esoft.ischool.model.Teacher;
import com.esoft.ischool.model.Term;
import com.esoft.ischool.model.TermGroup;
import com.esoft.ischool.model.TermGroupSummary;
import com.esoft.ischool.model.TermResult;
import com.esoft.ischool.model.Tuition;
import com.esoft.ischool.model.TuitionEnrollment;
import com.esoft.ischool.model.YearSummary;
import com.esoft.ischool.security.model.Menu;
import com.esoft.ischool.security.model.Roles;
import com.esoft.ischool.security.model.User;
import com.esoft.ischool.stocks.model.ProductConsumer;
import com.esoft.ischool.stocks.model.PurchaseOrder;
import com.esoft.ischool.vo.CourseVO;
import com.esoft.ischool.vo.FileSearchVO;
import com.esoft.ischool.vo.MarkSearchVO;
import com.esoft.ischool.vo.MarkVO;
import com.esoft.ischool.vo.StudentSearchVO;
import com.esoft.ischool.vo.StudentVO;
import com.esoft.ischool.vo.TeacherVO;


@Transactional(readOnly = true)
public interface BaseService {
	public Teacher getTeacher(User user);
	public Student getStudent(User user);
	@Transactional(readOnly = false)
	public void delete(Long id, Class cl);
	public BaseEntity getById(Class cl, Long id);
	public School getDefaultSchool();
	@Transactional(readOnly = false)
	public void update(BaseEntity entity, User user);
	@Transactional(readOnly = false)
	public void save(BaseEntity entity, User user);
	public BaseEntity load(BaseEntity entity);
	//public List<BaseEntity> loadAll(Class<?extends BaseEntity> cl);
	public List<BaseEntity> loadAll(Class<?extends BaseEntity> cl, School school);
	public List<Menu> loadAllMenus(School school);
	public List<User> loadAllUsers(School school);
	public List<BaseEntity> loadAllByParentId(Class<?extends BaseEntity> c, 
			String parentName, String parentProperty, Long parentPropertyValue);
	@Transactional(readOnly = false)
	public void deleteMarksByExam(final Exam exam);
	public BaseEntity findByName(Class cl, String name, School school);
	public BaseEntity findByColumn(Class cl, String columnName, Integer columnValue, School school);
	public BaseEntity findByColumn(Class cl, String columnName, String columnValue, School school);
	public <T> List<BaseEntity> findByColumnValues(Class cl, String columnName, List<T> columnValue, Long schoolId);
	public BaseEntity findByName(Class cl, String name, String parentName,
			String parentProperty, String parentPropertyValue);
	public List<Student> searchStudents(boolean selectAllStudents,
			String className, String year, School school);
	public List<Student> searchStudents(boolean selectAllStudents,
			String className, String year, String name, School school);
	public List<Teacher> searchTeachers(boolean selectAllStudents,
			String className, String year, School school);
	@Transactional(readOnly = false)
	public void saveStudentsCorrespondance(Correspondence correspondence,
			List<Student> selectedStudents, User user);
	@Transactional(readOnly = false)
	public void saveTeachersCorrespondance(Correspondence correspondence,
			List<Teacher> selectedTeachers, User user);
 
	public BaseEntity findByParents(Class cl, String firstParent, String firstParentName, 
			String secondParent, String secondParentName);

	public BaseEntity findByParents(Class cl, String firstParent, Long firstParentId, 
			String secondParent, Long secondParentId);

	public List<BaseEntity> loadByParentsIds(Class cl, String firstParent, Long firstParentId, 
			String secondParent, Long secondParentId);
	public List<BaseEntity> loadByParentsIds(Class cl, String firstParent, Long firstParentId, 
			String secondParent, Long secondParentId, String thirdParent, Long thirdParentId);
	
	@Transactional(readOnly = false)
	public void saveStudentsEnrollment(LevelClass levelClass,
			List<Student> selectedStudents, SchoolYear schoolYear, Date enrollmentDate, User user);
	@Transactional(readOnly = false)
	public void assignStudentsToParent(Parent parent, List<Student> selectedStudents, User user);
	@Transactional(readOnly = false)
	public void assignParentsToStudent(Student student, List<Parent> selectedParents, User user);
	@Transactional(readOnly = false)
	public void saveRoleMenus(Roles role, List<BaseEntity> selectedMenus, User user);

	@Transactional(readOnly = false)
	public void saveRoleUsers(Roles role, List<BaseEntity> selectedUsers, User user);
	
	@Transactional(readOnly = false)
	public void saveTeachersEnrollment(LevelClass levelClass,
			List<Teacher> selectedTeachers, SchoolYear schoolYear);
	public List<Tuition> getStudentPayments(Student student, School school);
	public Payment getStudentPayment(Student student, Tuition tuition, School school);
	public List<StudentVO> getStudents(Class cl, StudentSearchVO studentSearch, User user);
	
	public List<TeacherVO> getTeachers(Class cl,  String matricule,String lastName, String firstName, User user);
	public TermResult fetchTermResults(SchoolYear schoolYear, LevelClass lclass, Term term);
	public List<TuitionEnrollment> getAllPaymentsDueInDays(Class cl, Integer numberOfDays);
	public List<BaseEntity> getAllProductConsumersDueInDays(Class cl, Integer numberOfDays);
	public List<Alert> getAllActiveAlert(Class cl, String alertTypeCode);
	public Integer getReceivedStudentAlertCount(Class cl, Long alertId, Long tuitionId, Long userId);
	public List<AlertReceiver> getAlertReceiver(Class cl, Long alertId, Long tuitionId, Long userId);
	public List<Configuration> getConfigurationListByGroup(Class cl, String groupName,School school);
	public BaseEntity findByColumn(Class cl, String columnName,	String columnValue ) ;
	public List<BaseEntity> findByColumns(Class cl, List<String> columnNames,	List<String> columnValues ) ;
	public List<TeacherVO> getTeachersOrderByPosition(Class cl, String matricule, String lastName, String firstName, User user, boolean orderByPosition, School school);

	public School findSchoolByName(String columnName) ;
	
	@Transactional(readOnly = false)
	public void saveDeliveredOrder(PurchaseOrder purchaseOrder, User user);
	
	@Transactional(readOnly = false)
	public void saveProductConsumer(ProductConsumer productConsumer, Integer quantity, User user);

	public List<BaseEntity> getEntityByPropertyComparison(Class cl, String propertyName1, String propertyName2);

	public List<BaseEntity> findByColumnsLike(Class cl, List<String> columnNames,
			List<String> columnValues);
	
	@Transactional(readOnly = false)
	public void saveStudent(Student student, Config config, User user);
	
	@Transactional(readOnly = false)
	public String saveStudents(List<Student> students, Config config, User user);
	public User getAdminUser(School school);
	
	@Transactional(readOnly = false)
	public void saveTeacher(Teacher teacher, Config config, User user);
	
	public List<TuitionEnrollment> getStudentPaymentsForTuition(Student student, Tuition t, School school);
	public List<String> getSubjectsForLevel(LevelClass levelClass);
	public List<BaseEntity> getStudentMarks(Student student);
	public List getMenusFromRolesMenus(List<BaseEntity> rolesMenus);
	public List getUsersFromRolesUsers(List<BaseEntity> rolesUsers);
	public List<BaseEntity> getUsers(Class cl, String userName, String lastName, String firstName, User user);
	public List<BaseEntity> getMenus(Class cl, String menuName);
	public Averages getOneAverage(SchoolYear schoolYear, LevelClass lclass,
			Term term);
	public List<BaseEntity> getStudentTop10Marks(Student student);
	@Transactional(readOnly = false)
	public void calculateAverages(SchoolYear schoolYear, LevelClass lclass,
			Term term, User user,Config config);
	@Transactional(readOnly = false)
	public void publishResults(SchoolYear schoolYear, LevelClass lclass,
			Term term, User currentUser);
	@Transactional(readOnly = false)
	public void lockUnlockResults(SchoolYear schoolYear, LevelClass lclass,
			Term term, User currentUser);
	public Grade findGrade(List<BaseEntity> grades, School school, Double moyenne, Integer maxMark);
	public Grade findGrade(School school, Double moyenne, Integer maxMark);
	public List<Averages> getAverages(SchoolYear schoolYear, LevelClass lclass,
			Term term);
	public List<Averages> getAverages(SchoolYear schoolYear, LevelClass lclass,
			Term term, Long studentId);
	public List<Averages> getAverages(SchoolYear schoolYear, LevelClass lclass,
			Long studentId);
	public List<BaseEntity> getStudentBulletins(Student student);
	public List<Object[]> getReportParameterValues(String sql);
	public List<BaseEntity> getTimeTablesByTeacherId(Class cl, Long teacherId, Long yearId, Long termId);
	public String generateMatricule(Class cl,String firstName, String lastName,
			Date birthDate);
	public List<BaseEntity> getMedicalVisits(Date beginVisitDate, Date endVisitDate); 
	public List<Files> getStudentFiles(Student student);
	public Double getCummulatedRatio(Long schoolYearId, Long termId, Long courseId, Long examId);
	public Term getCurrentTermForClass(SchoolYear year, LevelClass lClass);
	public YearSummary getOneYearSummary(SchoolYear schoolYear, User user);
	@Transactional(readOnly = false)
	public void calculateYearSummary(SchoolYear schoolYear, User currentUser,Config config, String className);
	public List<YearSummary> getYearSummaries(SchoolYear schoolYear,
			LevelClass lclass, School school);
	public List<BaseEntity> getTeachersSortedByPosition(School school);
	public List<BaseEntity> getEntitiesByQueryAndParameters(School school, String queryName, 
			String [] paramNames, Object[] paramValues);
	public List<News> getAllSortedNews(School school);
	public List<BaseEntity> getThisYearCurriculum(School defaultSchool);
	public List<BaseEntity> fetchCurrentYearTuitions(School defaultSchool);
	public List<Tuition> fetchTuitionsForYear(School defaultSchool,String year);
	public List<BaseEntity> getThisYearFurnitures(School defaultSchool);
	public List<MarkVO> getMarks(Long studentId);
	public StudentVO getStudentVOFromStudent(Student student);
	public TeacherVO getTeacherVOFromTeacher(Teacher teacher);
	public SchoolYear getSchoolYear(Date eventDate, School defaultSchool);
	@Transactional(readOnly = false)
	public void calculateGroupSummary(SchoolYear schoolYear,
			TermGroup termGroup, User currentUser, Config config, String className) throws Exception;
	public List<TermGroupSummary> getTermGroupSummaries(SchoolYear schoolYear, LevelClass lClass,
			TermGroup termGroup, User currentUser);
	public List<Bulletin> getTermSummaries(SchoolYear schoolYear, LevelClass lClass,
			Term term, User currentUser);
	public List<Bulletin> getBulletins(Long studentId, String year);
	@Transactional(readOnly = false)
	public void saveStudentTuitions(List<Student> selectedStudents, List<Tuition> selectedTuitions, User currentUser);
	List<Tuition> fetchSortedTuitions(School defaultSchool, String schoolYearName);
	public List<BaseEntity> searchParents(Parent parent, School school);
	public List<Parent> searchParentsToAssign(Parent parent, School school);
	@Transactional(readOnly = false)
	public Payment saveStudentsPaymentsByTuitionType(List<Long> studentIds, School school, Long tuitionTypeId, 
			Long yearId, Tuition tuition, List<Integer> errors, User currentUser);
	@Transactional(readOnly = false)
	public Payment saveStudentsNegativePaymentsByTuitionType(List<Long> studentIds, School school, Long tuitionTypeId, 
			Long yearId, Tuition tuition, List<Integer> errors, User currentUser);
	public void getParentTuitionsByTuitionType(List<Student> students, Parent parent, List<Long> studentIds, 
			School school, Long tuitionTypeId, Long yearId, Tuition tuition, List<Integer> errors, User currentUser);
	public void getParentPaymentsByStudents(Parent parent, List<Student> students, List<Long> studentIds, 
			School school, Long yearId, List<Integer> errors);
	public void getStudentTuitionsByTuitionType(Student student, School school, Long tuitionTypeId, Long yearId);
	public void getStudentPaymentsByTuitionTypes(Student student, List<Long> studentIds, School school, Long yearId, List<Integer> errors);
	public Exam getExam(ExamType examType, Term term, SchoolYear schoolYear, Course course, LevelClass levelClass, User currentUser);
	public Integer getStudentsCountByLevelClassAndYear(Long schoolYearId, Long levelClassId, School school);
	public List<AssignmentFile> getCourseAssignmentFiles(School school, FileSearchVO fileSearch);
	@Transactional(readOnly = false)
	public void addCourseAssignmentFiles(FileSearchVO examFile, List<AssignmentFile> assignedFiles, User user);
	public List<AssignmentFile> searchCourseAssignmentFiles(School school, FileSearchVO fileSearch);
	public StudentEnrollment getStudentEnrollment(Long studentId, Long levelClassId, Long yearId);
	public List<CourseVO> getCourses(School school);
	public List<CourseVO> getCourses(School school, Long teacherId) ;
	public List<CourseVO> getCourses(Long classId,School school);
	@Transactional(readOnly = false)
	public int deleteExamMark(Long examId);
	public Integer countActiveStudent();
}
