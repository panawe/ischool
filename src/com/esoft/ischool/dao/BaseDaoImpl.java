package com.esoft.ischool.dao;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.NumberUtils;
import org.apache.commons.lang.StringUtils;
import org.hibernate.Hibernate;
import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;
import org.springframework.stereotype.Repository;

import com.esoft.ischool.model.Alert;
import com.esoft.ischool.model.AlertReceiver;
import com.esoft.ischool.model.AssignmentFile;
import com.esoft.ischool.model.Averages;
import com.esoft.ischool.model.BaseEntity;
import com.esoft.ischool.model.Bulletin;
import com.esoft.ischool.model.Configuration;
import com.esoft.ischool.model.Course;
import com.esoft.ischool.model.CourseAssignmentFile;
import com.esoft.ischool.model.Exam;
import com.esoft.ischool.model.ExamType;
import com.esoft.ischool.model.Files;
import com.esoft.ischool.model.LevelClass;
import com.esoft.ischool.model.MedicalVisit;
import com.esoft.ischool.model.News;
import com.esoft.ischool.model.Parent;
import com.esoft.ischool.model.Payment;
import com.esoft.ischool.model.Receiver;
import com.esoft.ischool.model.School;
import com.esoft.ischool.model.SchoolYear;
import com.esoft.ischool.model.Student;
import com.esoft.ischool.model.StudentEnrollment;
import com.esoft.ischool.model.StudentTuition;
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
import com.esoft.ischool.model.YearSummary;
import com.esoft.ischool.security.model.Menu;
import com.esoft.ischool.security.model.User;
import com.esoft.ischool.util.AverageStatus;
import com.esoft.ischool.vo.CourseVO;
import com.esoft.ischool.vo.ExamVO;
import com.esoft.ischool.vo.FileSearchVO;
import com.esoft.ischool.vo.MarkVO;
import com.esoft.ischool.vo.StudentSearchVO;
import com.esoft.ischool.vo.StudentVO;
import com.esoft.ischool.vo.TeacherVO;

@Repository("baseDao")
@Scope("singleton")
public class BaseDaoImpl<T extends BaseEntity> extends HibernateDaoSupport
		implements BaseDao {

	public BaseDaoImpl() {

	}

	public School getDefaultSChool() {
		return (School) getHibernateTemplate().get(School.class, 1L);
	}

	@Autowired
	public BaseDaoImpl(SessionFactory sessionFactory) {
		setSessionFactory(sessionFactory);
	}

	public void delete(BaseEntity entity) {
		getHibernateTemplate().delete(entity);
	}

	public void delete(School entity) {
		getHibernateTemplate().delete(entity);
	}

	public BaseEntity getById(Class cl, final Long id) {
		return (BaseEntity) getHibernateTemplate().get(cl, id);
	}

	public School getSChool(final Long id) {
		return (School) getHibernateTemplate().get(School.class, id);
	}

	public void update(BaseEntity entity, User user) {
		entity.setModDate(new Date());
		entity.setSchool(user.getSchool());
		entity.setModifiedBy(user.getId());
		getHibernateTemplate().update(entity);
	}

	public void save(BaseEntity entity, User user) {
		entity.setCreateDate(new Date());
		entity.setModDate(new Date());
		entity.setSchool(user.getSchool());
		entity.setModifiedBy(user.getId());
		getHibernateTemplate().save(entity);
	}

	public void save(BaseEntity entity, School school, User user) {
		entity.setCreateDate(new Date());
		entity.setModDate(new Date());
		entity.setSchool(school);
		entity.setModifiedBy(user.getId());
		getHibernateTemplate().save(entity);
	}

	public BaseEntity load(BaseEntity entity) {
		getHibernateTemplate().load(entity, entity.getId());
		return entity;
	}

	@SuppressWarnings("unchecked")
	public List<BaseEntity> loadAll(Class<? extends BaseEntity> cl) {
		return getHibernateTemplate().loadAll(cl);
	}

	@SuppressWarnings("unchecked")
	public List<BaseEntity> loadAll(Class<? extends BaseEntity> cl,
			School school) {
		DetachedCriteria crit = DetachedCriteria.forClass(cl);
		crit.createCriteria("school")
				.add(Restrictions.eq("id", school.getId()));
		return getHibernateTemplate().findByCriteria(crit);
	}

	public BaseEntity findByName(Class cl, String name, School school) {
		BaseEntity entity = null;
		DetachedCriteria crit = DetachedCriteria.forClass(cl);
		crit.add(Restrictions.eq("name", name));
		crit.add(Restrictions.eq("school.id", school.getId()));

		List l = getHibernateTemplate().findByCriteria(crit);

		if (l.size() > 0)
			entity = (BaseEntity) l.get(0);

		return entity;
	}

	public BaseEntity findByName(Class cl, String name, String parentName,
			String parentProperty, String parentPropertyValue) {
		BaseEntity entity = null;
		DetachedCriteria crit = DetachedCriteria.forClass(cl);
		crit.add(Restrictions.eq("name", name)).createCriteria(parentName)
				.add(Restrictions.eq(parentProperty, parentPropertyValue));
		// crit.add(Restrictions.eq(parentName, parentValue));

		List l = getHibernateTemplate().findByCriteria(crit);

		if (l.size() > 0)
			entity = (BaseEntity) l.get(0);

		return entity;
	}
	
	public School findSchoolByName(String name){
		School entity = null;
		DetachedCriteria crit = DetachedCriteria.forClass(School.class);
		crit.add(Restrictions.eq("name", name));
		List l = getHibernateTemplate().findByCriteria(crit);
		if (l.size() > 0)
			entity = (School) l.get(0);
		return entity;
	}

	public BaseEntity findByParents(Class cl, String firstParent,
			String firstParentName, String secondParent, String secondParentName) {

		BaseEntity entity = null;
		DetachedCriteria crit = DetachedCriteria.forClass(cl);
		
		if (firstParent != null)
			crit.createCriteria(firstParent).add(Restrictions.eq("name", firstParentName));
		
		if (secondParent != null)
			crit.createCriteria(secondParent).add(Restrictions.eq("name", secondParentName));

		List l = getHibernateTemplate().findByCriteria(crit);

		if (l.size() > 0)
			entity = (BaseEntity) l.get(0);

		return entity;

	}

	public BaseEntity findByParents(Class cl, String firstParent,
			Long firstParentId, String secondParent, Long secondParentId) {

		BaseEntity entity = null;
		DetachedCriteria crit = DetachedCriteria.forClass(cl);
		
		if (firstParent != null)
			crit.createCriteria(firstParent).add(Restrictions.eq("id", firstParentId));
		
		if (secondParent != null)
			crit.createCriteria(secondParent).add(Restrictions.eq("id", secondParentId));

		List l = getHibernateTemplate().findByCriteria(crit);

		if (l.size() > 0)
			entity = (BaseEntity) l.get(0);

		return entity;

	}

	public void deleteByParentIds(String cl, Map<String, Long> parentNameIds) {
		String sql = "DELETE FROM " + cl + " WHERE (1 = 1) ";

		for (String name : parentNameIds.keySet()) {
			sql += " AND " + name + ".id = " + parentNameIds.get(name);
		}

		Query query = getHibernateTemplate().getSessionFactory()
				.getCurrentSession().createQuery(sql);
		int rowCount = query.executeUpdate();
	}
	
	public int executeDeleteQuery(String sqlQuery) {
		Query query = getHibernateTemplate().getSessionFactory()
				.getCurrentSession().createQuery(sqlQuery);
		return query.executeUpdate();
	}
	
	public int deleteExamMark(Long examId) {
		String sql = "DELETE FROM Mark m WHERE m.exam.id = "+examId;
		Query query = getHibernateTemplate().getSessionFactory()
				.getCurrentSession().createQuery(sql);
		return query.executeUpdate();
	}
	

	public List<BaseEntity> findByParentsIds(Class<? extends BaseEntity> c,
			String firstParentName, Long firstParentId,
			String secondParentName, Long secondParentId) {
		BaseEntity entity = null;
		DetachedCriteria crit = DetachedCriteria.forClass(c);
		
		if (firstParentName != null)
			crit.createCriteria(firstParentName).add(Restrictions.eq("id", firstParentId));
		if (secondParentName != null)
			crit.createCriteria(secondParentName).add(Restrictions.eq("id", secondParentId));

		List l = getHibernateTemplate().findByCriteria(crit);

		return l;
	}

	public List<BaseEntity> findByParentsIds(Class<? extends BaseEntity> c,
			String firstParentName, Long firstParentId,
			String secondParentName, Long secondParentId,
			String thirdParentName, Long thirdParentId) {
		BaseEntity entity = null;
		DetachedCriteria crit = DetachedCriteria.forClass(c);
		crit.createCriteria(firstParentName).add(
				Restrictions.eq("id", firstParentId));
		crit.createCriteria(secondParentName).add(
				Restrictions.eq("id", secondParentId));
		crit.createCriteria(thirdParentName).add(
				Restrictions.eq("id", thirdParentId));
		
		List l = getHibernateTemplate().findByCriteria(crit);

		return l;
	}
	
	public BaseEntity findByColumn(Class cl, String columnName,
			Integer columnValue, School school) {
		BaseEntity entity = null;
		DetachedCriteria crit = DetachedCriteria.forClass(cl);
		crit.add(Restrictions.eq(columnName, columnValue));
		crit.add(Restrictions.eq("school.id", school.getId()));

		List l = getHibernateTemplate().findByCriteria(crit);

		if (l.size() > 0)
			entity = (BaseEntity) l.get(0);

		return entity;
	}

	public BaseEntity findByColumn(Class cl, String columnName,
			Long columnValue, School school) {
		BaseEntity entity = null;
		DetachedCriteria crit = DetachedCriteria.forClass(cl);
		crit.add(Restrictions.eq(columnName, columnValue));
		crit.add(Restrictions.eq("school.id", school.getId()));

		List l = getHibernateTemplate().findByCriteria(crit);

		if (l.size() > 0)
			entity = (BaseEntity) l.get(0);

		return entity;
	}
	
	public List<Files> getStudentFiles(Student student) {
		DetachedCriteria crit = DetachedCriteria.forClass(Files.class);
		crit.add(Restrictions.eq("student.id", student.getId()));
		crit.add(Restrictions.eq("school.id", student.getSchool().getId()));
		return getHibernateTemplate().findByCriteria(crit);
	}

	public BaseEntity findByColumn(Class cl, String columnName,
			String columnValue, School school) {
		BaseEntity entity = null;
		DetachedCriteria crit = DetachedCriteria.forClass(cl);
		crit.add(Restrictions.eq(columnName, columnValue));
		crit.add(Restrictions.eq("school.id", school.getId()));

		List l = getHibernateTemplate().findByCriteria(crit);

		if (l.size() > 0)
			entity = (BaseEntity) l.get(0);

		return entity;
	}

	public BaseEntity findByColumn(Class cl, String columnName,
			String columnValue ) {
		BaseEntity entity = null;
		DetachedCriteria crit = DetachedCriteria.forClass(cl);
		crit.add(Restrictions.eq(columnName, columnValue));
		List l = getHibernateTemplate().findByCriteria(crit);

		if (l.size() > 0)
			entity = (BaseEntity) l.get(0);

		return entity;
	}
	
	public List<BaseEntity> findByColumns(Class cl, List<String> columnNames,
			List<String> columnValues) {
		BaseEntity entity = null;
		DetachedCriteria crit = DetachedCriteria.forClass(cl);
		int i = 0;
		for (String columnName : columnNames) {
			crit.add(Restrictions.eq(columnName, columnValues.get(i)));
			i++;
		}
		
		List l = getHibernateTemplate().findByCriteria(crit);
		return l;
	}
	
	public List<BaseEntity> findByColumnsLike(Class cl, List<String> columnNames,
			List<String> columnValues) {
		BaseEntity entity = null;
		DetachedCriteria crit = DetachedCriteria.forClass(cl);
		int i = 0;
		for (String columnName : columnNames) {
			crit.add(Restrictions.like(columnName, columnValues.get(i)));
			i++;
		}
		
		List l = getHibernateTemplate().findByCriteria(crit);
		return l;
	}
	
	
	public List<BaseEntity> loadAllByParentId(Class<? extends BaseEntity> c,
			String parentName, String parentProperty, Long parentPropertyValue) {
		DetachedCriteria crit = DetachedCriteria.forClass(c);
		crit.createCriteria(parentName).add(
				Restrictions.eq(parentProperty, parentPropertyValue));

		List l = getHibernateTemplate().findByCriteria(crit);

		return l;
	}
	
	public List<BaseEntity> loadEntitiesByParentAndDateRange(Class<? extends BaseEntity> c,
			String parentName, String parentProperty, Long parentPropertyValue, String dateColumn,  Date beginDate, Date endDate) {		
		DetachedCriteria crit = DetachedCriteria.forClass(c);
		crit.createCriteria(parentName).add(
				Restrictions.eq(parentProperty, parentPropertyValue));
		if (beginDate != null)
			crit.add(Restrictions.ge(dateColumn, beginDate));
		if (endDate != null)
			crit.add(Restrictions.le(dateColumn, endDate));
		
		crit.addOrder(Order.desc(dateColumn));
		List l = getHibernateTemplate().findByCriteria(crit);
		return l;
	}
	
	public List<BaseEntity> loadEntitiesByPropertyAndDateRange(Class<? extends BaseEntity> c,
			String property, String propertyValue, String dateColumn,  Date beginDate, Date endDate) {
		DetachedCriteria crit = DetachedCriteria.forClass(c);
		crit.add(Restrictions.eq(property, propertyValue));
		if (beginDate != null)
			crit.add(Restrictions.ge(dateColumn, beginDate));
		if (endDate != null)
			crit.add(Restrictions.le(dateColumn, endDate));
		
		crit.addOrder(Order.desc(dateColumn));
		List l = getHibernateTemplate().findByCriteria(crit);
		return l;
	}

	public List<Student> searchStudents(boolean selectAllStudents,
			String className, String year, School school) {
		List<Student> students = null;

		if (selectAllStudents) {
			final String[] paramNames = { "schoolId" };
			final Object[] paramValues = new Object[] { school.getId() };

			students = getHibernateTemplate().findByNamedQueryAndNamedParam(
					"getActiveStudents", paramNames, paramValues);
		} else if (className != null && !className.equals("") && year != null
				&& !year.equals("")) {
			final String[] paramNames = { "className", "year", "schoolId" };
			final Object[] paramValues = new Object[] { className, year,
					school.getId() };

			students = getHibernateTemplate().findByNamedQueryAndNamedParam(
					"getStudentsForClassYear", paramNames, paramValues);

		} else if (className != null && !className.equals("")) {
			final String[] paramNames = { "className", "schoolId" };
			final Object[] paramValues = new Object[] { className,
					school.getId() };

			students = getHibernateTemplate().findByNamedQueryAndNamedParam(
					"getStudentsForClass", paramNames, paramValues);
		} else if (year != null && !year.equals("")) {
			final String[] paramNames = { "year", "schoolId" };
			final Object[] paramValues = new Object[] { year, school.getId() };

			students = getHibernateTemplate().findByNamedQueryAndNamedParam(
					"getStudentsForYear", paramNames, paramValues);
		}

		return students;
	}

	public List<Student> searchStudents(boolean selectAllStudents,
			String className, String year, String lastName, School school) {
		List<Student> students = new ArrayList<Student>();

		if (selectAllStudents) {
			final String[] paramNames = { "schoolId" };
			final Object[] paramValues = new Object[] { school.getId() };

			students = getHibernateTemplate().findByNamedQueryAndNamedParam(
					"getActiveStudents", paramNames, paramValues);
		} else if ((className != null && !className.equals("")) 
					|| (year != null && !year.equals(""))
					|| (lastName != null && !lastName.equals(""))
				) {
			/*DetachedCriteria crit = DetachedCriteria.forClass(Student.class);
			if (className != null && !className.equals(""))
				crit.createCriteria("currentEnrollment.levelClass").add(Restrictions.eq("name", className));
			if (year != null && !year.equals(""))
				crit.createCriteria("currentEnrollment.schoolYear").add(Restrictions.eq("year", year));
			if (lastName != null && !lastName.equals(""))
				crit.add(Restrictions.like("lastName", "%" + lastName + "%"));
			crit.createCriteria("school").add(Restrictions.eq("id", school.getId()));
			crit.add(Restrictions.ne("status", (short) 0));
			
			List l = getHibernateTemplate().findByCriteria(crit);*/
			
			String sqlQuery = "SELECT * FROM ( "
					+ "SELECT DISTINCT ST.STUDENT_ID, ST.MATRICULE, ST.FIRST_NAME, ST.LAST_NAME FROM STUDENT ST "
					+ "INNER JOIN SENROLLMENT SM ON ST.STUDENT_ID = SM.STUDENT_ID "
					+ "INNER JOIN SCHOOLYEAR SY ON SM.SCHOOLYEAR_ID = SY.SCHOOLYEAR_ID "
					+ "@@CLASS@@"
					+ "WHERE (1 = 1) @@YEAR_WHERE@@ @@CLASS_WHERE@@  @@LASTNAME_WHERE@@) t";
			
			if (className != null && !className.equals("")) {
				sqlQuery = sqlQuery.replace("@@CLASS@@", " INNER JOIN CLASS ON SM.CLASS_ID = CLASS.CLASS_ID ");
				sqlQuery = sqlQuery.replace("@@CLASS_WHERE@@", " AND CLASS.NAME LIKE '%" + className + "%'");
			}
			else {
				sqlQuery = sqlQuery.replace("@@CLASS@@", " ");
				sqlQuery = sqlQuery.replace("@@CLASS_WHERE@@", " ");
			}
				
			
			if (lastName != null && !lastName.equals("")) {
				sqlQuery = sqlQuery.replace("@@LASTNAME_WHERE@@", " AND ST.LAST_NAME LIKE '%" + lastName + "%'");
			}
			else {
				sqlQuery = sqlQuery.replace("@@LASTNAME_WHERE@@", " ");
			}
			
			if (year != null && !year.equals("")) {
				sqlQuery = sqlQuery.replace("@@YEAR_WHERE@@", " AND SY.NAME = '" + year + "'");
			}
			else {
				sqlQuery = sqlQuery.replace("@@YEAR_WHERE@@", " ");
			}
			
			//int parameterIndex = 0;
			Session session = getHibernateTemplate().getSessionFactory().openSession();
			Query query = session.createSQLQuery(sqlQuery);
			//query.setParameter(parameterIndex, year);
			
			List<Object[]> objects = query.list();
			Student student;
			for (Object[] obj : objects) {
				student = new Student();
				student.setId(obj[0] == null ? null : new Long(obj[0].toString()));
				student.setMatricule(obj[1] == null ? null : obj[1].toString());
				student.setFirstName(obj[2] == null ? null : obj[2].toString());
				student.setLastName(obj[3] == null ? null : obj[3].toString());
				
				students.add(student);
			}
		}

		return students;
	}
	
	public List<Teacher> searchTeachers(boolean selectAllTeachers,
			String className, String year, School school) {
		List<Teacher> teachers = null;

		if (selectAllTeachers) {
			final String[] paramNames = { "schoolId" };
			final Object[] paramValues = new Object[] { school.getId() };

			teachers = getHibernateTemplate().findByNamedQueryAndNamedParam(
					"getActiveTeachers", paramNames, paramValues);
		}
		return teachers;
	}

	public Receiver getStudentCorrespondance(long studentId,
			long correspondanceId) {
		List<Receiver> receivers = null;

		final String[] paramNames = { "studentId", "correspondanceId" };
		final Object[] paramValues = new Object[] { studentId, correspondanceId };

		receivers = getHibernateTemplate().findByNamedQueryAndNamedParam(
				"getStudentCorrespondance", paramNames, paramValues);

		return receivers != null && receivers.size() > 0 ? receivers.get(0)
				: null;
	}
	
	public User getAdminUser(School school) {
	List<User> users=null;
		final String[] paramNames = { "schoolId" };
		final Object[] paramValues = new Object[] { school.getId()};

		users = getHibernateTemplate().findByNamedQueryAndNamedParam(
				"getAdminUser", paramNames, paramValues);

		return users != null && users.size() > 0 ? users.get(0)
				: null;
	}

	public Receiver getTeacherCorrespondance(long teacherId,
			long correspondanceId) {
		List<Receiver> receivers = null;

		final String[] paramNames = { "teacherId", "correspondanceId" };
		final Object[] paramValues = new Object[] { teacherId, correspondanceId };

		receivers = getHibernateTemplate().findByNamedQueryAndNamedParam(
				"getTeacherCorrespondance", paramNames, paramValues);

		return receivers != null && receivers.size() > 0 ? receivers.get(0)
				: null;
	}

	public StudentEnrollment getStudentEnrollment(long studentId,
			long levelClassId, long schoolYearId) {
		List<StudentEnrollment> studentEnrollments = null;

		final String[] paramNames = { "studentId", "levelClassId",
				"schoolYearId" };
		final Object[] paramValues = new Object[] { studentId, levelClassId,
				schoolYearId };

		studentEnrollments = getHibernateTemplate()
				.findByNamedQueryAndNamedParam("getStudentEnrollment",
						paramNames, paramValues);

		return studentEnrollments != null && studentEnrollments.size() > 0 ? studentEnrollments
				.get(0) : null;
	}

	public TeacherEnrollment getTeacherEnrollment(long teacherId,
			long levelClassId, long schoolYearId) {
		List<TeacherEnrollment> teacherEnrollments = null;

		final String[] paramNames = { "teacherId", "levelClassId" };
		final Object[] paramValues = new Object[] { teacherId, levelClassId };

		teacherEnrollments = getHibernateTemplate()
				.findByNamedQueryAndNamedParam("getTeacherEnrollment",
						paramNames, paramValues);

		return teacherEnrollments != null && teacherEnrollments.size() > 0 ? teacherEnrollments
				.get(0) : null;
	}

	public List<BaseEntity> getTimeTablesByTeacherId(Class cl, Long teacherId, Long yearId, Long termId) {
		DetachedCriteria crit = DetachedCriteria.forClass(cl);
		if (teacherId != null)
			crit.createCriteria("course.teacher").add(Restrictions.eq("id", teacherId));

		if (yearId != null)
			crit.createCriteria("schoolYear").add(Restrictions.eq("id", yearId));

		if (termId != null) 
			crit.createCriteria("term").add(Restrictions.eq("id", termId));
		
		List l = getHibernateTemplate().findByCriteria(crit);
		return l;
	}

	public List<Tuition> getStudentsPaymentsByTuitionType(List<Long> studentIds, School school, Long tuitionTypeId, Long yearId) {
		List<Tuition> tuitions = null;
			tuitions = new ArrayList<Tuition>();
			final String[] paramNames = { "studentIds", "schoolId", "tuitionTypeId", "schoolYearId" };
			final Object[] paramValues = new Object[] {
					studentIds, school.getId(), tuitionTypeId, yearId};
			List<Object[]> objects;
			objects = getHibernateTemplate().findByNamedQueryAndNamedParam(
					"getStudentPaymentsByTuitionType", paramNames, paramValues);

			Tuition tu;
			
			for (Object[] obj : objects) {
				Tuition tuition = (Tuition) obj[0];
				tu = new Tuition();
				tu.setId(tuition.getId());
				tu.setAmount(tuition.getAmount());
				tu.setDueDate(tuition.getDueDate());
				
				tu.setPaid((Double) obj[1]);
				tu.setRebate((Double) obj[2]);
				tu.setStudentId((Long) obj[3]);
				tu.setEnrollmentId((Long) obj[4]);
				tuitions.add(tu);
			}

		
		return tuitions;
	}

	
	public List<Payment> getParentPaymentsByStudents(List<Long> studentIds, School school, Long yearId) {
		List<Payment> payments = null;
		payments = new ArrayList<Payment>();
		
		String sqlQuery = "SELECT * FROM ( " +
						"SELECT P.PAYMENT_ID AS PAYMENTID, P.AMOUNT AS PAYMENT_AMOUNT, P.REBATE AS PAYMENT_REBATE, " +
						"P.PAYMENT_DATE AS PAYMENTDATE, P.COMMENT AS PAYMENT_COMMENT, "
						+ "T.DESCRIPTION, T.DUE_DATE, T.AMOUNT AS TUITION_AMOUNT, T.TUITION_TYPE_ID, TT.NAME, "
						+ "SE.STUDENT_ID, SE.ENROLLMENT_ID, T.TUITION_ID "
						+ "FROM PAYMENT P "
						+ "INNER JOIN TUITION T ON P.TUITION_ID = T.TUITION_ID "
						+ "INNER JOIN TUITION_TYPE TT ON T.TUITION_TYPE_ID = TT.TUITION_TYPE_ID "
						+ "INNER JOIN SENROLLMENT SE ON P.ENROLLMENT_ID = SE.ENROLLMENT_ID "
						+ "AND SE.STUDENT_ID IN (@@STUDENT_IDS@@) AND SE.SCHOOLYEAR_ID = ? ) t"
		+ " UNION "
						+ "SELECT * FROM ( " +
						"SELECT 0 AS PAYMENTID, 0 AS PAYMENT_AMOUNT, 0 AS PAYMENT_REBATE, " +
						"null AS PAYMENTDATE, '' AS PAYMENT_COMMENT, "
						+ "			T.DESCRIPTION, T.DUE_DATE, T.AMOUNT AS TUITION_AMOUNT, "
			            + "T.TUITION_TYPE_ID, TT.NAME, "
						+ "			S.STUDENT_ID, S.ENROLLMENT_ID, T.TUITION_ID "
						+ "			FROM TUITION T "
						+ "			INNER JOIN TUITION_TYPE TT ON T.TUITION_TYPE_ID = TT.TUITION_TYPE_ID "
			            + "INNER JOIN STUDENT_TUITION ST ON T.TUITION_ID = ST.TUITION_ID "
			            + "INNER JOIN STUDENT S ON ST.STUDENT_ID = S.STUDENT_ID "
						+ "			AND S.STUDENT_ID IN (@@STUDENT_IDS@@) AND T.SCHOOLYEAR_ID = ? " 
			            + "INNER JOIN SENROLLMENT SE ON SE.STUDENT_ID = S.STUDENT_ID AND SE.SCHOOLYEAR_ID = T.SCHOOLYEAR_ID "
						+ " WHERE NOT EXISTS (SELECT PAYMENT_ID FROM PAYMENT WHERE PAYMENT.TUITION_ID = T.TUITION_ID AND PAYMENT.ENROLLMENT_ID = SE.ENROLLMENT_ID )) t ";
         
		int parameterIndex = 0;
		Session session = getHibernateTemplate().getSessionFactory().openSession();
		sqlQuery = sqlQuery.replaceAll("@@STUDENT_IDS@@", studentIds.toString().replaceAll("\\[", "").replaceAll("\\]", ""));
		Query query = session.createSQLQuery(sqlQuery);
		query.setParameter(parameterIndex, yearId);
		parameterIndex ++;
		query.setParameter(parameterIndex, yearId);
		
		List<Object[]> objects = query.list();
		Payment payment;
		for (Object[] obj : objects) {
			payment = new Payment();
			payment.setId(obj[0] == null ? null : new Long(obj[0].toString()));
			payment.setAmount(obj[1] == null ? null : new Double(obj[1].toString()));
			payment.setRebate(obj[2] == null ? null : new Double(obj[2].toString()));
			payment.setPaymentDate(obj[3] == null ? null : (Date)obj[3]);
			payment.setComment(obj[4] == null ? null : obj[4].toString());	
			payment.setTuitionDescription(obj[5] == null ? null : obj[5].toString());
			payment.setTuitionDueDate(obj[6] == null ? null : (Date)obj[6]);
			payment.setTuitionAmount(obj[7] == null ? null : new Double(obj[7].toString()));
			payment.setTuitionTypeId(obj[8] == null ? null : new Long(obj[8].toString()));
			payment.setTuitionTypeName(obj[9] == null ? null : obj[9].toString());	
			payment.setStudentId(obj[10] == null ? null : new Long(obj[10].toString()));
			payment.setEnrollmentId(obj[11] == null ? null : new Long(obj[11].toString()));
			payment.setTuitionId(obj[12] == null ? null : new Long(obj[12].toString()));
			
			payments.add(payment);
		}

		return payments;
	}
	
	
	public List<Tuition> getStudentPayments(Student student, School school) {
		List<Tuition> tuitions = null;
		if (student.getCurrentEnrollment() != null) {
			tuitions = new ArrayList<Tuition>();
			final String[] paramNames = { "enrollmentId", "studentId", "schoolId" };
			final Object[] paramValues = new Object[] {
					student.getCurrentEnrollment().getId(),
					student.getId(), school.getId() };
			List<Object[]> objects;
			objects = getHibernateTemplate().findByNamedQueryAndNamedParam(
					"getStudentPayments", paramNames, paramValues);

			for (Object[] obj : objects) {
				Tuition tuition = (Tuition) obj[0];
				tuition.setPaid((Double) obj[1]);
				tuition.setRebate((Double) obj[2]);
				tuitions.add(tuition);
			}

		}
		return tuitions;
	}

	public List<TuitionEnrollment> getStudentPaymentsForTuition(
			Student student, Tuition t, School school) {
		List<TuitionEnrollment> tuitions = null;
		if (student.getCurrentEnrollment() != null) {
			tuitions = new ArrayList<TuitionEnrollment>();
			final String[] paramNames = { "enrollmentId", "studentId",
					"tuitionId", "schoolId" };
			final Object[] paramValues = new Object[] {
					student.getCurrentEnrollment().getId(),
					student.getId(), t.getId(), school.getId() };
			List<Object[]> objects;
			objects = getHibernateTemplate().findByNamedQueryAndNamedParam(
					"getStudentPaymentsForTuition", paramNames, paramValues);
			int i = 0;
			for (Object[] obj : objects) {
				Tuition tuition = (Tuition) obj[0];
				TuitionEnrollment tEnroll = new TuitionEnrollment();
				tEnroll.setPaid((Double) obj[1]);
				tEnroll.setComment((String) obj[2]);
				tEnroll.setPayDate((Date) obj[3]);
				tEnroll.setReceivedBy((String) obj[4]);
				tEnroll.setRebate((Double) obj[5]);
				tEnroll.setTuition(tuition);
				tEnroll.setId(i++);
				tuitions.add(tEnroll);
			}

		}
		return tuitions;
	}

	public Payment getStudentPayment(Student student, Tuition tuition,
			School school) {
		// TODO Auto-generated method stub
		List<Payment> tuitions = null;
		if (student.getCurrentEnrollment() != null) {
			tuitions = new ArrayList<Payment>();
			final String[] paramNames = { "enrollmentId", "tuitionId",
					"schoolId" };
			final Object[] paramValues = new Object[] {
					student.getCurrentEnrollment().getId(), tuition.getId(),
					school.getId() };
			List<Object[]> objects;
			tuitions = getHibernateTemplate().findByNamedQueryAndNamedParam(
					"getStudentPayment", paramNames, paramValues);

		}
		return tuitions != null && tuitions.size() > 0 ? tuitions.get(0) : null;
	}

	public List<Payment> getPaymentsForStudents(List<Long> studentIds,  Long tuitionTypeId, 
			Long schoolYearId, School school) {
		
		List<Payment> payments = null;
		final String[] paramNames = { "studentIds", "schoolYearId", "tuitionTypeId", "schoolId" };
		final Object[] paramValues = new Object[] {studentIds, schoolYearId, tuitionTypeId, school.getId() };
		List<Object[]> objects;
		payments = getHibernateTemplate().findByNamedQueryAndNamedParam("getStudentsPayments", paramNames, paramValues);

		return payments;
	}
	
	public List<StudentVO> getStudents(Class cl, StudentSearchVO studentSearch, User user) {
		String sqlQuery = "SELECT * FROM (SELECT S.STUDENT_ID, S.LAST_NAME, S.FIRST_NAME, S.NICK_NAME, S.MATRICULE, S.ADDRESS, S.EMAIL, S.CITY_OF_BIRTH, " +
				"S.CITY, S.COMMENTS, SCHOOL.NAME SCHOOLNAME, " +
				"CLASS.NAME CLASSNAME, S.PHONE, S.CELL_PHONE, S.STATUS, S.COUNTRY_OF_BIRTH, RESIDENCE_COUNTRY.NAME COUNTRYNAME, S.SCH_RELIGION_ID, " +
				"SCHOOL_RELIGION.NAME RELIGIONNAME, S.SCHOOL_ID, S.USER_ID, BIRTH_DATE, S.SEX, SENROLLMENT.ENROLLMENT_DATE, SY.NAME, S.MIDDLE_NAME, S.ALLERGY "
			+ "FROM STUDENT S "
			+ " @@YEAR_OUTCOME@@ "
			+ "LEFT OUTER JOIN SENROLLMENT ON S.STUDENT_ID = SENROLLMENT.STUDENT_ID " +
					"AND S.ENROLLMENT_ID = SENROLLMENT.ENROLLMENT_ID "
			+ "LEFT OUTER JOIN SCHOOLYEAR SY ON SENROLLMENT.SCHOOLYEAR_ID = SY.SCHOOLYEAR_ID "
			+ "LEFT OUTER JOIN CLASS ON CLASS.CLASS_ID = SENROLLMENT.CLASS_ID "
			+ "INNER JOIN SCHOOL ON S.SCHOOL_ID = SCHOOL.SCHOOL_ID "
			+ "LEFT OUTER JOIN COUNTRY RESIDENCE_COUNTRY ON S.COUNTRY_ID = RESIDENCE_COUNTRY.COUNTRY_ID "
			+ "LEFT OUTER JOIN SCHOOL_RELIGION ON S.SCH_RELIGION_ID = SCHOOL_RELIGION.SCH_RELIGION_ID "
			+ "WHERE (1 = 1) AND SCHOOL.SCHOOL_ID = ? ";
		
		if (studentSearch.getLastName() != null && !studentSearch.getLastName().trim().equals("")) {
			sqlQuery += " AND LAST_NAME LIKE ?";
		}

		if (studentSearch.getFirstName() != null && !studentSearch.getFirstName().trim().equals("")) {
			sqlQuery += " AND FIRST_NAME LIKE ?";
		}
			

		if (studentSearch.getMatricule() != null && !studentSearch.getMatricule().trim().equals("")) {
			sqlQuery += " AND MATRICULE LIKE ?";
		}

		if (studentSearch.getClassName() != null && !studentSearch.getClassName().trim().equals("")) {
			sqlQuery += " AND CLASS.NAME LIKE ? ";
		}

		if (studentSearch.getStatus() != null && !studentSearch.getStatus().equals("")) {
			sqlQuery += " AND S.STATUS = ? ";
		}
		
		if (studentSearch.getSex() != null && !studentSearch.getSex().trim().equals("")) {
			sqlQuery += " AND S.SEX = ? ";
		}
		
		if (studentSearch.getBeginAge() != null && !studentSearch.getBeginAge().trim().equals("")) {
			sqlQuery += " AND YEAR(curdate()) - YEAR(birth_date) - (right(curdate(),5) < right(birth_date,5)) >= ? ";
		}
		if (studentSearch.getEndAge() != null && !studentSearch.getEndAge().trim().equals("")) {
			sqlQuery += " AND YEAR(curdate()) - YEAR(birth_date) - (right(curdate(),5) < right(birth_date,5)) <= ? ";
		}
		
		if (studentSearch.getYear() != null && !studentSearch.getYear().trim().equals("")) {
			sqlQuery = sqlQuery.replace("@@YEAR_OUTCOME@@", "INNER JOIN YEAR_SUMMARY YS ON S.STUDENT_ID = YS.STUDENT_ID ");
			sqlQuery += " AND YS.SCHOOLYEAR_ID = ? ";
			if (studentSearch.getDecision() != null && !studentSearch.getDecision().equals("")) {
				sqlQuery += " AND YS.DECISION_CODE = ? ";
			}
		}
		else 
			sqlQuery = sqlQuery.replace("@@YEAR_OUTCOME@@", "");
		
		sqlQuery += ") t" ;
		
		int parameterIndex = 0;
		Session session = getHibernateTemplate().getSessionFactory().openSession();
		SQLQuery query = session.createSQLQuery(sqlQuery);
		query.addScalar("STUDENT_ID", Hibernate.LONG);
		query.addScalar("LAST_NAME", Hibernate.STRING);
		query.addScalar("FIRST_NAME", Hibernate.STRING);
		query.addScalar("NICK_NAME", Hibernate.STRING);
		query.addScalar("MATRICULE", Hibernate.STRING);
		query.addScalar("ADDRESS", Hibernate.STRING);
		query.addScalar("EMAIL", Hibernate.STRING);
		query.addScalar("CITY_OF_BIRTH", Hibernate.STRING);
		query.addScalar("CITY", Hibernate.STRING);
		query.addScalar("COMMENTS", Hibernate.STRING);
		query.addScalar("SCHOOLNAME", Hibernate.STRING);
		query.addScalar("CLASSNAME", Hibernate.STRING);
		query.addScalar("PHONE", Hibernate.STRING);
		query.addScalar("CELL_PHONE", Hibernate.STRING);
		query.addScalar("STATUS", Hibernate.STRING);
		query.addScalar("COUNTRY_OF_BIRTH", Hibernate.STRING);
		query.addScalar("COUNTRYNAME", Hibernate.STRING);
		query.addScalar("SCH_RELIGION_ID", Hibernate.LONG);
		query.addScalar("RELIGIONNAME", Hibernate.STRING);
		query.addScalar("SCHOOL_ID", Hibernate.LONG);
		query.addScalar("USER_ID", Hibernate.LONG);
		query.addScalar("BIRTH_DATE", Hibernate.DATE);
		query.addScalar("SEX", Hibernate.STRING);
		query.addScalar("ENROLLMENT_DATE", Hibernate.DATE);
		query.addScalar("NAME", Hibernate.STRING);
		query.addScalar("MIDDLE_NAME", Hibernate.STRING);
		query.addScalar("ALLERGY", Hibernate.STRING);
		
		query.setParameter(parameterIndex, user.getSchool().getId());
		parameterIndex ++;
		
		if (studentSearch.getLastName() != null && !studentSearch.getLastName().trim().equals("")) {
			query.setParameter(parameterIndex, studentSearch.getLastName() + "%");
			parameterIndex ++;
		}

		if (studentSearch.getFirstName() != null && !studentSearch.getFirstName().trim().equals("")) {
			query.setParameter(parameterIndex, studentSearch.getFirstName() + "%");
			parameterIndex ++;
		}
			

		if (studentSearch.getMatricule() != null && !studentSearch.getMatricule().trim().equals("")) {
			query.setParameter(parameterIndex, studentSearch.getMatricule() + "%" );
			parameterIndex ++;
		}

		if (studentSearch.getClassName() != null && !studentSearch.getClassName().trim().equals("")) {
			query.setParameter(parameterIndex, studentSearch.getClassName()+ "%");
			parameterIndex ++;
		}

		if (studentSearch.getStatus() != null && !studentSearch.getStatus().trim().equals("")) {
			query.setParameter(parameterIndex, studentSearch.getStatus() );
			parameterIndex ++;
		}
		
		if (studentSearch.getSex() != null && !studentSearch.getSex().trim().equals("")) {
			query.setParameter(parameterIndex, studentSearch.getSex() );
			parameterIndex ++;
		}
		
		if (studentSearch.getBeginAge() != null && !studentSearch.getBeginAge().trim().equals("")) {
			query.setParameter(parameterIndex, studentSearch.getBeginAge() );
			parameterIndex ++;
		}
		
		if (studentSearch.getEndAge() != null && !studentSearch.getEndAge().trim().equals("")) {
			query.setParameter(parameterIndex, studentSearch.getEndAge() );
			parameterIndex ++;
		}
		
		if (studentSearch.getYear() != null &&!studentSearch.getYear().trim().equals("")) {
			SchoolYear schoolYear = (SchoolYear) findByColumn(SchoolYear.class, "year", studentSearch.getYear(), user.getSchool());
			
			query.setParameter(parameterIndex, schoolYear.getId() );
			parameterIndex ++;
			if (studentSearch.getDecision() != null && !studentSearch.getDecision().equals("")) {
				query.setParameter(parameterIndex, studentSearch.getDecision() );
				parameterIndex ++;
			}
		}
		List<Object[]> objects = query.list();
		List<StudentVO> students = new ArrayList<StudentVO>();
		
		for (Object[] obj : objects) {
			StudentVO student = new StudentVO();
			student.setId(obj[0] == null ? null : new Long(obj[0].toString()));
			student.setLastName(obj[1] == null ? null : obj[1].toString());
			student.setFirstName(obj[2] == null ? null : obj[2].toString());
			student.setNickName(obj[3] == null ? null : obj[3].toString());
			student.setMatricule(obj[4] == null ? null : obj[4].toString());
			student.setAddress(obj[5] == null ? null : obj[5].toString());
			student.setEmail(obj[6] == null ? null : obj[6].toString());
			student.setCityOfBirth(obj[7] == null ? null : obj[7].toString());
			student.setCity(obj[8] == null ? null : obj[8].toString());
			student.setComments(obj[9] == null ? null : obj[9].toString());
			student.setSchoolName(obj[10] == null ? null : obj[10].toString());
			student.setLevelClassName(obj[11] == null ? null : obj[11].toString());
			student.setPhone(obj[12] == null ? null : obj[12].toString());
			student.setCellPhone(obj[13] == null ? null : obj[13].toString());
			student.setStatus(obj[14] == null ? null : new Short(obj[14].toString()));
			student.setCountryId(obj[15] == null ? null : new Long(obj[15].toString()));
			student.setCountryName(obj[16] == null ? null : obj[16].toString());
			student.setReligionId(obj[17] == null ? null : new Long(obj[17].toString()));
			student.setReligionName(obj[18] == null ? null : obj[18].toString());
			student.setSchoolId(obj[19] == null ? null : new Long(obj[19].toString()));
			student.setUserId(obj[20] == null ? null : new Long(obj[20].toString()));
			student.setBirthDate(obj[21] == null ? null : (Date)(obj[21]));
			student.setSex(obj[22] == null ? null : obj[22].toString());
			student.setEnrollmentDate(obj[23] == null ? null : (Date)(obj[23]));
			student.setSchoolYear(obj[24] == null ? null : obj[24].toString());
			student.setMiddleName(obj[25] == null ? null : obj[25].toString());
			student.setAllergy(obj[26] == null ? null : obj[26].toString());
			
			students.add(student);
		}
		
		session.close();
		return students;
	}

	public List<BaseEntity> getUsers(Class cl, String userName,
			String lastName, String firstName, User user) {
		DetachedCriteria crit = DetachedCriteria.forClass(cl);
		if (lastName != null && !lastName.trim().equals(""))
			crit.add(Restrictions.like("lastName", lastName + "%"));

		if (firstName != null && !firstName.trim().equals("")) {
			crit.add(Restrictions.like("firstName", firstName + "%"));
		}

		if (userName != null && !userName.trim().equals("")) {
			crit.add(Restrictions.like("userName", userName + "%"));
		}

		crit.createCriteria("school").add(Restrictions.eq("id", user.getSchool().getId()));
		
		List l = getHibernateTemplate().findByCriteria(crit);
		return l;
	}

	public List<BaseEntity> getMenus(Class cl, String menuName) {
		DetachedCriteria crit = DetachedCriteria.forClass(cl);
		if (menuName != null && !menuName.trim().equals("")) {
			crit.add(Restrictions.like("name", menuName + "%"));
		}

		List l = getHibernateTemplate().findByCriteria(crit);
		return l;
	}

	public Student getStudent(User user) {
		if(user==null)
			return null;
		DetachedCriteria crit = DetachedCriteria.forClass(Student.class);
		crit.createCriteria("user").add(Restrictions.eq("id", user.getId()));
		List l = getHibernateTemplate().findByCriteria(crit);
		if (l.size() > 0) {
			return (Student) l.get(0);
		}
		return null;
	}
	
	public Teacher getTeacher(User user) {

		DetachedCriteria crit = DetachedCriteria.forClass(Teacher.class);
		crit.createCriteria("user").add(Restrictions.eq("id", user.getId()));
		List l = getHibernateTemplate().findByCriteria(crit);
		if (l.size() > 0) {
			return (Teacher) l.get(0);
		}
		return null;
	}
	
	public List<TeacherVO> getTeachersOrderByPosition(Class cl, String matricule,
			String lastName, String firstName, User user, boolean orderByPosition, School school) {
		String sqlQuery = "SELECT T.TEACHER_ID, T.LAST_NAME, T.FIRST_NAME, T.MATRICULE, SCHOOL.NAME SNAME, " +
		"T.PHONE, T.CELL_PHONE, T.E_MAIL, T.SCHOOL_ID, U.USER_ID, T.RESUME, U.POSITION_ID, P.NAME NAME, "
		+ "T.CITY, T.CITY_OF_BIRTH, RESIDENCE_COUNTRY.NAME COUNTRY_OF_RESIDENCE, BIRTH_COUNTRY.NAME COUNTRY_OF_BIRTH, "
		+ "T.MIDDLE_NAME, T.NICK_NAME, T.BIRTH_DATE, T.ADDRESS, T.COMMENTS, T.STATUS, T.HIRED_DATE, T.SEX, SCHOOL_RELIGION.NAME RELIGIONNAME "
		+ "FROM TEACHER T "
		+ "INNER JOIN USERS U ON T.USER_ID = U.USER_ID "
		+ "INNER JOIN SCHOOL ON T.SCHOOL_ID = SCHOOL.SCHOOL_ID " +
				" LEFT OUTER JOIN POSITION P ON U.POSITION_ID= P.POSITION_ID "
		+ "LEFT OUTER JOIN COUNTRY RESIDENCE_COUNTRY ON T.COUNTRY_ID = RESIDENCE_COUNTRY.COUNTRY_ID "
		+ "LEFT OUTER JOIN COUNTRY BIRTH_COUNTRY ON T.COUNTRY_ID = BIRTH_COUNTRY.COUNTRY_ID "
		+ "LEFT OUTER JOIN SCHOOL_RELIGION ON T.SCH_RELIGION_ID = SCHOOL_RELIGION.SCH_RELIGION_ID "
		+ "WHERE (1 = 1) AND T.STATUS = 1 AND SCHOOL.SCHOOL_ID = ? ";
	
		if (lastName != null && !lastName.trim().equals("")) {
			sqlQuery += " AND T.LAST_NAME LIKE ?";
		}
		
		if (firstName != null && !firstName.trim().equals("")) {
			sqlQuery += " AND T.FIRST_NAME LIKE ?";
		}
			
		
		if (matricule != null && !matricule.trim().equals("")) {
			sqlQuery += " AND T.MATRICULE LIKE ?";
		}
	
		if (orderByPosition)
			sqlQuery += " ORDER BY P.POSITION_ID, T.LAST_NAME, T.FIRST_NAME";
		
		int parameterIndex = 0;
		Session session = getHibernateTemplate().getSessionFactory().openSession();
		Query query = session.createSQLQuery(sqlQuery);
		
		if (orderByPosition)
			query.setParameter(parameterIndex, school.getId());
		else
			query.setParameter(parameterIndex, user.getSchool().getId());
		
		parameterIndex ++;
	
		if (lastName != null && !lastName.trim().equals("")) {
			query.setParameter(parameterIndex, lastName + "%");
			parameterIndex ++;
		}
		
		if (firstName != null && !firstName.trim().equals("")) {
			query.setParameter(parameterIndex, firstName + "%");
			parameterIndex ++;
		}
			
		
		if (matricule != null && !matricule.trim().equals("")) {
			query.setParameter(parameterIndex, matricule + "%" );
			parameterIndex ++;
		}
	
		List<Object[]> objects = query.list();
		List<TeacherVO> teachers = new ArrayList<TeacherVO>();
		
		for (Object[] obj : objects) {
			TeacherVO teacher = new TeacherVO();
			teacher.setId(obj[0] == null ? null : new Long(obj[0].toString()));
			teacher.setLastName(obj[1] == null ? null : obj[1].toString());
			teacher.setFirstName(obj[2] == null ? null : obj[2].toString());
			teacher.setMatricule(obj[3] == null ? null : obj[3].toString());
			teacher.setSchoolName(obj[4] == null ? null : obj[4].toString());
			teacher.setPhone(obj[5] == null ? null : obj[5].toString());
			teacher.setCellPhone(obj[6] == null ? null : obj[6].toString());
			teacher.setEmail(obj[7] == null ? null : obj[7].toString());
			teacher.setSchoolId(obj[8] == null ? null : new Long(obj[8].toString()));
			teacher.setUserId(obj[9] == null ? null : new Long(obj[9].toString()));
			teacher.setResume(obj[10]== null ? null : obj[10].toString().trim());
			teacher.setPosition(obj[12]== null ? null : obj[12].toString());
			teacher.setCity(obj[13]== null ? null : obj[13].toString());
			teacher.setBirthCity(obj[14]== null ? null : obj[14].toString());
			teacher.setCountryName(obj[15]== null ? null : obj[15].toString());
			teacher.setBirthCountry(obj[16]== null ? null : obj[16].toString());
			teacher.setMiddleName(obj[17]== null ? null : obj[17].toString());
			teacher.setNickName(obj[18]== null ? null : obj[18].toString());
			teacher.setBirthDate(obj[19]== null ? null : (Date)obj[19]);
			teacher.setAddress(obj[20]== null ? null : obj[20].toString());
			teacher.setComments(obj[21]== null ? null : obj[21].toString());
			teacher.setStatus(obj[22]== null ? null : new Short(obj[22].toString()));
			teacher.setHireDate(obj[23]== null ? null : (Date)obj[23]);
			teacher.setSex(obj[24]== null ? null : obj[24].toString());
			teacher.setReligionName(obj[25]== null ? null : obj[25].toString());
			teachers.add(teacher);
		}
	
		session.close();
		return teachers;
	}
	
	public List<TeacherVO> getTeachers(Class cl, String matricule,
			String lastName, String firstName, User user) {
		return getTeachersOrderByPosition(cl, matricule,	lastName, firstName, user, false, null);
	}
	
	public List<BaseEntity> getTeachersSortedByPosition(School school){
		final String[] paramNames = {"schoolId" };
		final Object[] paramValues = new Object[] {school.getId() };
		return  getHibernateTemplate().findByNamedQueryAndNamedParam(
				"getTeachers", paramNames, paramValues);
	}

	public TermResult fetchTermResults(SchoolYear schoolYear,
			LevelClass lclass, Term term) {

		TermResult termResult = null;
		final String[] paramNames = { "classId", "termId", "yearId" };
		final Object[] paramValues = new Object[] { lclass.getId(),
				term.getId(), schoolYear.getId() };
		List<Object[]> objects;
		objects = getHibernateTemplate().findByNamedQueryAndNamedParam(
				"fetchTermResults", paramNames, paramValues);
		boolean first = true;
		Integer previousStudentId = 0;
		TermResultSummary trs = null;
		for (Object[] o : objects) {

			if (first) {
				termResult = new TermResult();
				String year = (String) o[0];
				String termName = (String) o[1];
				Integer termId = (Integer) o[2];
				String className = (String) o[3];
				Integer nbrOfStudent = (Integer) o[4];
				termResult.setClassName(className);
				termResult.setYearName(year);
				termResult.setTermId(termId);
				termResult.setNbrOfStudent(nbrOfStudent);
				termResult.setTermName(termName);
				first = false;
			}

			Integer studentId = (Integer) o[7];

			if (!previousStudentId.equals(studentId)) {
				byte[] image = (byte[]) o[16];
				String studentName = (String) o[9];
				Integer enrollmentId = (Integer) o[8];
				if(trs!=null){
					trs.calculateMoyenne();
				}
				trs = new TermResultSummary();
				trs.setPicture(image);
				trs.setStudentId(studentId);
				trs.setStudentName(studentName);
				trs.setEnrollmentId(enrollmentId);
				termResult.addSmry(trs);
				previousStudentId = studentId;

			}

			String subjectName = (String) o[5];
			String teacherName = (String) o[6];
			Integer maxMark = (Integer) o[10];
			Double moyenneClasse = (Double) o[11];
			Double ratioClass = (Double) o[12];
			Double moyenneExam = (Double) o[13];
			Double ratioExam = (Double) o[14];
			Double moyenne = (Double) o[15];
			Integer teacherId =(Integer)o[17];

			TermResultDtl dtl = new TermResultDtl();
			dtl.setMoyenneClasse(moyenneClasse);
			dtl.setMoyenneExam(moyenneExam);
			dtl.setRatioClass(ratioClass);
			dtl.setRatioExam(ratioExam);
			dtl.setMoyenne(moyenne);
			dtl.setSubjectName(subjectName);
			dtl.setTeacherName(teacherName);
			dtl.setMaxMark(maxMark);
			dtl.setTeacherId(teacherId);
			trs.addDtl(dtl);
		}
		if (termResult != null)
			termResult.rank();

		return termResult;
	}
	
	public List<TuitionEnrollment> getAllPaymentsDueInDays(Class cl,
			Integer numberOfDays) {

		Calendar cal = Calendar.getInstance();
		cal.add(Calendar.DATE, -numberOfDays);

		Date date = cal.getTime();

		List<TuitionEnrollment> tuitionEnrollments = new ArrayList<TuitionEnrollment>();
		final String[] paramNames = { "projectedDueDate" };
		final Object[] paramValues = new Object[] { date };

		List<Object[]> objects;
		objects = getHibernateTemplate().findByNamedQueryAndNamedParam(
				"getOverDueTuition", paramNames, paramValues);

		for (Object[] obj : objects) {

			TuitionEnrollment tuitionEnrollment = new TuitionEnrollment();
			Tuition tuition = (Tuition) obj[0];
			tuition.setPaid((Double) obj[2]);
			tuitionEnrollment.setTuition(tuition);
			tuitionEnrollment.setEnrollment((StudentEnrollment) obj[1]);
			tuitionEnrollments.add(tuitionEnrollment);
		}
		return tuitionEnrollments;
	}

	public List<BaseEntity> getAllProductConsumersDueInDays(Class cl,
			Integer numberOfDays) {

		Calendar cal = Calendar.getInstance();
		cal.add(Calendar.DATE, numberOfDays);

		List<BaseEntity> productConsumers = new ArrayList<BaseEntity>();
		final String[] paramNames = { "projectedDueDate" };
		final Object[] paramValues = new Object[] { cal };

		productConsumers = getHibernateTemplate()
				.findByNamedQueryAndNamedParam("getOverDueProductConsumer",
						paramNames, paramValues);

		return productConsumers;
	}

	public List<BaseEntity> getStudentMarks(Student student) {
		final String[] paramNames = { "studentId" };
		final Object[] paramValues = new Object[] { student.getId() };
		return getHibernateTemplate().findByNamedQueryAndNamedParam("getMarks",
				paramNames, paramValues);
	}
	
	public Double getCummulatedRatio(Long schoolYearId, Long termId, Long courseId, Long examId) {
		final String[] paramNames = { "schoolYearId","termId","courseId","examId" };
		final Object[] paramValues = new Object[] { schoolYearId,termId,courseId,examId };
		 List<Double> rr =getHibernateTemplate().findByNamedQueryAndNamedParam("getCummulatedRatio",
				paramNames, paramValues);
		 if(rr!=null&&rr.size()>0)
			 return (Double)rr.get(0);
		 return null;
	}
	
	public List<BaseEntity> getStudentTop10Marks(Student student) {
		final String[] paramNames = { "studentId" };
		final Object[] paramValues = new Object[] { student.getId() };
		return getHibernateTemplate().findByNamedQueryAndNamedParam("getTop10Marks",
				paramNames, paramValues);
	}


	public List<Alert> getAllActiveAlert(Class cl, String alertTypeCode) {
		DetachedCriteria crit = DetachedCriteria.forClass(cl);
		crit.add(Restrictions.ne("status", "0"));

		if (alertTypeCode != null)
			crit.add(Restrictions.ne("alertTypeCode", alertTypeCode));

		List l = getHibernateTemplate().findByCriteria(crit);
		return l;
	}

	public Integer getReceivedStudentAlertCount(Class cl, Long alertId,
			Long tuitionId, Long userId) {
		Integer count = 0;
		final String[] paramNames = { "alertId", "tuitionId", "userId" };
		final Object[] paramValues = new Object[] { alertId, tuitionId, userId };

		List<Integer> objects;
		objects = getHibernateTemplate().findByNamedQueryAndNamedParam(
				"getReceivedStudentAlertCount", paramNames, paramValues);

		for (Integer obj : objects) {
			count = obj;
		}
		return count;
	}
	
	public Term getCurrentTermForClass(SchoolYear year, LevelClass lClass) {
 
		final String[] paramNames = { "schoolYearId", "classId"};
		final Object[] paramValues = new Object[] { year.getId(), lClass.getId() };

		List<Term> terms;
		terms = getHibernateTemplate().findByNamedQueryAndNamedParam(
				"getCurrentTermForClass", paramNames, paramValues);		
		return terms!=null&&terms.size()>0?terms.get(0):null;
	}

	public List<AlertReceiver> getAlertReceiver(Class cl, Long alertId,
			Long tuitionId, Long userId) {
		DetachedCriteria crit = DetachedCriteria.forClass(cl);
		crit.createCriteria("alert").add(Restrictions.eq("id", alertId));
		if (tuitionId != null)
			crit.createCriteria("tuition")
					.add(Restrictions.eq("id", tuitionId));
		crit.createCriteria("user").add(Restrictions.eq("id", userId));
		crit.addOrder(Order.desc("createDate"));

		List l = getHibernateTemplate().findByCriteria(crit);
		return l;
	}

	public List<Configuration> getConfigurationListByGroup(Class cl,
			String groupName, School school) {
		DetachedCriteria crit = DetachedCriteria.forClass(cl);

		crit.add(Restrictions.eq("groupName", groupName));
		crit.createCriteria("school")
				.add(Restrictions.eq("id", school.getId()));
		return getHibernateTemplate().findByCriteria(crit);

	}

	public List<BaseEntity> getEntityByPropertyComparison(Class cl,
			String propertyName1, String propertyName2) {
		DetachedCriteria crit = DetachedCriteria.forClass(cl);
		// crit.add(Restrictions.ne("status", "0"));
		crit.add(Restrictions.leProperty(propertyName1, propertyName2));

		List l = getHibernateTemplate().findByCriteria(crit);
		return l;
	}

	public <T> List<BaseEntity> findByColumnValues(Class cl, String columnName,
			List<T> columnValues, Long schoolId) {

		DetachedCriteria crit = DetachedCriteria.forClass(cl);
		crit.add(Restrictions.in(columnName, columnValues));
		crit.createCriteria("school").add(Restrictions.eq("id", schoolId));
		List l = getHibernateTemplate().findByCriteria(crit);
		return l;
	}

	public List<String> getSubjectsForLevel(LevelClass levelClass) {
		DetachedCriteria dcrit = DetachedCriteria.forClass(Course.class);
		dcrit.createCriteria("levelClass").add(
				Restrictions.eq("id", levelClass.getId()));
		List<BaseEntity> l = getHibernateTemplate().findByCriteria(dcrit);
		List<String> subjects = new ArrayList<String>();
		for (BaseEntity be : l) {
			subjects.add(((Course) be).getSubject().getName());
		}
		return subjects;
	}

	public List<User> loadAllUsers(School school) {
		List<User> users = null;

		final String[] paramNames = {};
		final Object[] paramValues = new Object[] {};

		users = getHibernateTemplate().findByNamedQueryAndNamedParam(
				"getActiveUsers", paramNames, paramValues);

		return users;
	}

	public List<Menu> loadAllMenus(School school) {
		List<Menu> menus = null;
		final String[] paramNames = {};
		final Object[] paramValues = new Object[] {};

		menus = getHibernateTemplate().findByNamedQueryAndNamedParam(
				"getActiveMenus", paramNames, paramValues);
		return menus;
	}
	
	public void flush(){
		getHibernateTemplate().flush();
	}
	
	public Averages getOneAverage(SchoolYear schoolYear, LevelClass lclass,
			Term term){
		
/*		DetachedCriteria crit = DetachedCriteria.forClass(Averages.class);
		crit.createCriteria("school").add(Restrictions.eq("id", lclass.getSchool().getId()));
		crit.createCriteria("schoolYear").add(Restrictions.eq("id", schoolYear.getId()));
		crit.createCriteria("term").add(Restrictions.eq("id",term.getId()));
		crit.add(Restrictions.eq("className", lclass.getName()));
		List l = getHibernateTemplate().findByCriteria(crit, 0, 1);*/
		
	 	final String[] paramNames = { "schoolId", "schoolYearId", "termId","className"};
		final Object[] paramValues = new Object[] { lclass.getSchool().getId(), schoolYear.getId(),term.getId(),lclass.getName() };

		List<Averages> avgs;
		avgs = getHibernateTemplate().findByNamedQueryAndNamedParam(
				"getOneAverage", paramNames, paramValues);		
	
		if(avgs.size()>0){
			return (Averages)avgs.get(0);
		}
		return null;
	}
	
	public List<Averages> getAverages(SchoolYear schoolYear, LevelClass lclass,
			Term term){
	/*	DetachedCriteria crit = DetachedCriteria.forClass(Averages.class);
		crit.createCriteria("school").add(Restrictions.eq("id", lclass.getSchool().getId()));
		crit.createCriteria("schoolYear").add(Restrictions.eq("id", schoolYear.getId()));
		crit.createCriteria("term").add(Restrictions.eq("id",term.getId()));
		crit.add(Restrictions.eq("className", lclass.getName()));
		List l = getHibernateTemplate().findByCriteria(crit);
		return l; 
		*/
	 	final String[] paramNames = { "schoolId", "schoolYearId", "termId","className"};
		final Object[] paramValues = new Object[] { lclass.getSchool().getId(), schoolYear.getId(),term.getId(),lclass.getName() };

		List<Averages> avgs;
		avgs = getHibernateTemplate().findByNamedQueryAndNamedParam(
				"getAverages", paramNames, paramValues);		
		return avgs;
		 
	}
	
	public List<Averages> getAverages(SchoolYear schoolYear, LevelClass lclass,
			Term term, boolean isNew){
	 	final String[] paramNames = { "schoolId", "schoolYearId", "termId","className"};
		final Object[] paramValues = new Object[] { lclass.getSchool().getId(), schoolYear.getId(),term.getId(),lclass.getName() };

		List<Averages> avgs;
		avgs = getHibernateTemplate().findByNamedQueryAndNamedParam(
				"getAverages", paramNames, paramValues);		
		return avgs;
		 
	}
	
	public List<Averages> getAverages(SchoolYear schoolYear, LevelClass lclass,
			Term term, Long studentId){
	 	final String[] paramNames = { "schoolId", "schoolYearId", "termId","className", "studentId"};
		final Object[] paramValues = new Object[] { lclass.getSchool().getId(), schoolYear.getId(),term.getId(),lclass.getName(), studentId };

		List<Averages> avgs;
		avgs = getHibernateTemplate().findByNamedQueryAndNamedParam(
				"getAveragesByStudentAndTerm", paramNames, paramValues);		
		return avgs;
		 
	}
	
	
	public List<Averages> getAverages(SchoolYear schoolYear, LevelClass lclass,
			Long studentId){
	 	final String[] paramNames = { "schoolId", "schoolYearId", "className", "studentId"};
		final Object[] paramValues = new Object[] { lclass.getSchool().getId(), schoolYear.getId(), lclass.getName(), studentId };

		List<Averages> avgs;
		avgs = getHibernateTemplate().findByNamedQueryAndNamedParam(
				"getAveragesByStudent", paramNames, paramValues);		
		return avgs;
		 
	}
	
	public List<BaseEntity> getEntitiesByQueryAndParameters(School school, String queryName, 
			String [] paramNames, Object[] paramValues) {
		return getHibernateTemplate().findByNamedQueryAndNamedParam(queryName, paramNames, paramValues);
	}
	
	public List<Bulletin> getBulletins(SchoolYear schoolYear,
			LevelClass lclass, Term term){
		
		DetachedCriteria crit = DetachedCriteria.forClass(Bulletin.class);
		crit.createCriteria("school").add(Restrictions.eq("id", lclass.getSchool().getId()));
		crit.createCriteria("schoolYear").add(Restrictions.eq("id", schoolYear.getId()));
		crit.createCriteria("term").add(Restrictions.eq("id",term.getId()));
		crit.add(Restrictions.eq("className", lclass.getName()));
		List l = getHibernateTemplate().findByCriteria(crit);
		return l;
	}
	
	public List<BaseEntity> getMedicalVisits(Date beginVisitDate, Date endVisitDate) {		
		DetachedCriteria crit = DetachedCriteria.forClass(MedicalVisit.class);
		if (beginVisitDate != null)
			crit.add(Restrictions.ge("visitDate", beginVisitDate));
		if (endVisitDate != null)
			crit.add(Restrictions.le("visitDate", endVisitDate));
		
		List l = getHibernateTemplate().findByCriteria(crit);
		return l;
	}
	
	public List<BaseEntity> getStudentBulletins(Student student){
		
		DetachedCriteria crit = DetachedCriteria.forClass(Bulletin.class);
		crit.createCriteria("student").add(Restrictions.eq("id", student.getId()));
		crit.add(Restrictions.in("status",new Object[] {AverageStatus.PUBLISHED_LOCKED.getValue(),AverageStatus.PUBLISHED_UNLOCKED.getValue()}));
		return getHibernateTemplate().findByCriteria(crit);
	}

	public List<Bulletin> getBulletins(Long studentId, String year){
		
		DetachedCriteria crit = DetachedCriteria.forClass(Bulletin.class);
		crit.createCriteria("student").add(Restrictions.eq("id", studentId));
		crit.createCriteria("schoolYear").add(Restrictions.eq("year", year));
		return getHibernateTemplate().findByCriteria(crit);
	}


	public List<Object[]> getReportParameterValues(String sql) {
		List<Object[]> objects;
		objects = getHibernateTemplate().getSessionFactory().getCurrentSession().createSQLQuery(sql).list();

		return objects;
	}

	public YearSummary getOneYearSummary(SchoolYear schoolYear, User user) {
		// TODO Auto-generated method stub
		DetachedCriteria crit = DetachedCriteria.forClass(YearSummary.class);
		crit.createCriteria("school").add(Restrictions.eq("id", user.getSchool().getId()));
		crit.createCriteria("schoolYear").add(Restrictions.eq("id", schoolYear.getId()));
		List l = getHibernateTemplate().findByCriteria(crit, 0, 1);
		
		if(l.size()>0){
			return (YearSummary)l.get(0);
		}
		return null;
	}

	public List<YearSummary> getYearSummaries(SchoolYear schoolYear,
			User currentUser) {
		// TODO Auto-generated method stub
		DetachedCriteria crit = DetachedCriteria.forClass(YearSummary.class);
		crit.createCriteria("school").add(Restrictions.eq("id", currentUser.getSchool().getId()));
		crit.createCriteria("schoolYear").add(Restrictions.eq("id", schoolYear.getId()));
		List l = getHibernateTemplate().findByCriteria(crit);
		return l;
	}


	public List<Bulletin> getAllBulletins(SchoolYear schoolYear,
			User currentUser) {
		// TODO Auto-generated method stub
		DetachedCriteria crit = DetachedCriteria.forClass(Bulletin.class);
		crit.createCriteria("school").add(Restrictions.eq("id", currentUser.getSchool().getId()));
		crit.createCriteria("schoolYear").add(Restrictions.eq("id", schoolYear.getId()));
		crit.addOrder(Order.asc("student.id"));
		List l = getHibernateTemplate().findByCriteria(crit);
		return l;
	}

	public List<YearSummary> getYearSummaries(SchoolYear schoolYear,
			LevelClass lclass, School school) {
		DetachedCriteria crit = DetachedCriteria.forClass(YearSummary.class);
		crit.createCriteria("school").add(Restrictions.eq("id", school.getId()));
		crit.createCriteria("schoolYear").add(Restrictions.eq("id", schoolYear.getId()));
		crit.add(Restrictions.eq("className", lclass.getName()));
		crit.addOrder(Order.asc("rankNbr"));
		List l = getHibernateTemplate().findByCriteria(crit);
		return l;
	}

	public List<News> getAllSortedNews(School school) {
		// TODO Auto-generated method stub
		DetachedCriteria crit = DetachedCriteria.forClass(News.class);
		crit.createCriteria("school").add(Restrictions.eq("id", school.getId()));
		crit.addOrder(Order.desc("newsDate"));
		List l = getHibernateTemplate().findByCriteria(crit);
		return l;

	}

	public List<BaseEntity> getThisYearCurriculum(School defaultSchool) {
		// TODO Auto-generated method stub
	 	final String[] paramNames = { "schoolId"};
		final Object[] paramValues = new Object[] { defaultSchool.getId() };
		return getHibernateTemplate().findByNamedQueryAndNamedParam(
				"getCurriculumsForCurrentYear", paramNames, paramValues);		
		 
	}

	public List<BaseEntity> fetchCurrentYearTuitions(School defaultSchool) {
		// TODO Auto-generated method stub
	 	final String[] paramNames = { "schoolId"};
		final Object[] paramValues = new Object[] { defaultSchool.getId() };
		return getHibernateTemplate().findByNamedQueryAndNamedParam(
				"fetchCurrentYearTuitions", paramNames, paramValues);	
	}

	public List<BaseEntity> getThisYearFurnitures(School defaultSchool) {
		// TODO Auto-generated method stub
	 	final String[] paramNames = { "schoolId"};
		final Object[] paramValues = new Object[] { defaultSchool.getId() };
		return getHibernateTemplate().findByNamedQueryAndNamedParam(
				"getThisYearFurnitures", paramNames, paramValues);	
	}
	
	
	public List<MarkVO> getMarks(Long studentId) {
		// TODO Auto-generated method stub
		List<MarkVO> marks=new ArrayList<MarkVO>();
		final String[] paramNames = {"studentId"};
		final Object[] paramValues = new Object[] {studentId};
		List<Object[]> objects;
		objects = getHibernateTemplate().findByNamedQueryAndNamedParam(
				"getPublishedMarkVOs", paramNames, paramValues);
		for (Object[] obj : objects) {
			MarkVO mark = new MarkVO();
			mark.setId((Long)obj[0]);
			mark.setStudentId((Long)obj[1]);
			mark.setStudentEnrollmentId((Long)obj[2]);
			mark.setMark((Double)obj[3]);
			mark.setMaxMark((Double)obj[4]);
			mark.setSchoolYear((String)obj[5]);
			mark.setSchool((String)obj[6]);
			mark.setSubject((String)obj[7]);
			mark.setStudentFirstName((String)obj[8]);
			mark.setStudentLastName((String)obj[9]);
			mark.setExamName((String)obj[10]);
			mark.setExamDate((Date)obj[11]);
			mark.setClassName((String)obj[12]);
			mark.setExamType((String)obj[13]);
			mark.setGrade((String)obj[14]);
			mark.setApprovedBy((Long)obj[15]);
			
			marks.add(mark);
		}
		
		return marks;
	}

	public SchoolYear getSchoolYear(Date eventDate, School defaultSchool) {
	 	final String[] paramNames = { "eventDate","schoolId"};
		final Object[] paramValues = new Object[] { eventDate,defaultSchool.getId() };
		List<SchoolYear> schoolYears= getHibernateTemplate().findByNamedQueryAndNamedParam(
				"getSchoolYearForDate", paramNames, paramValues);	
		return schoolYears!=null&&schoolYears.size()>0?schoolYears.get(0):null;
	}
	public Integer countActiveStudent() {
	 	final String[] paramNames = { };
		final Object[] paramValues = new Object[] { };
		List<Integer> objects;
		objects =  getHibernateTemplate().findByNamedQueryAndNamedParam(
				"getActiveStudentCount", paramNames, paramValues);	
		for (Integer obj : objects) {
			return  obj;			
		}
		return 0;
	}
	

	public List<TermGroupSummary> getTermGroupSummaries(SchoolYear schoolYear,
			TermGroup termGroup, User currentUser) {
		DetachedCriteria crit = DetachedCriteria.forClass(TermGroupSummary.class);
		crit.createCriteria("school").add(Restrictions.eq("id", currentUser.getSchool().getId()));
		crit.createCriteria("schoolYear").add(Restrictions.eq("id", schoolYear.getId()));
		crit.createCriteria("termGroup").add(Restrictions.eq("id", termGroup.getId()));
		crit.addOrder(Order.asc("student.id"));
		List l = getHibernateTemplate().findByCriteria(crit);
		return l;
	}
	
	public List<TermGroupSummary> getTermGroupSummaries(SchoolYear schoolYear, LevelClass lclass,
			TermGroup termGroup, User currentUser) {
		DetachedCriteria crit = DetachedCriteria.forClass(TermGroupSummary.class);
		crit.createCriteria("school").add(Restrictions.eq("id", currentUser.getSchool().getId()));
		crit.createCriteria("schoolYear").add(Restrictions.eq("id", schoolYear.getId()));
		crit.createCriteria("termGroup").add(Restrictions.eq("id", termGroup.getId()));
		crit.add(Restrictions.eq("className", lclass.getName()));
		crit.addOrder(Order.asc("rankNbr"));
		List l = getHibernateTemplate().findByCriteria(crit);
		return l;
	}
	
	public Exam getExam(ExamType examType, Term term, SchoolYear schoolYear, Course course, LevelClass levelClass, User currentUser) {
		DetachedCriteria crit = DetachedCriteria.forClass(Exam.class);
		crit.createCriteria("school").add(Restrictions.eq("id", currentUser.getSchool().getId()));
		crit.createCriteria("schoolYear").add(Restrictions.eq("id", schoolYear.getId()));
		crit.createCriteria("term").add(Restrictions.eq("id", term.getId()));
		crit.createCriteria("course").add(Restrictions.eq("id", course.getId()));
		crit.createCriteria("examType").add(Restrictions.eq("id", examType.getId()));
		
		List l = getHibernateTemplate().findByCriteria(crit);

		if (l.size() > 0) {
			return (Exam)l.get(0);
		}
		return null;
		
	}
	
	public List<Bulletin> getAllBulletins(SchoolYear schoolYear,
			TermGroup termGroup, User currentUser) {
		DetachedCriteria crit = DetachedCriteria.forClass(Bulletin.class);
		crit.createCriteria("school").add(Restrictions.eq("id", currentUser.getSchool().getId()));
		crit.createCriteria("schoolYear").add(Restrictions.eq("id", schoolYear.getId()));
		crit.createCriteria("term.termGroup").add(Restrictions.eq("id",termGroup.getId()));
		crit.addOrder(Order.asc("student.id"));
		List l = getHibernateTemplate().findByCriteria(crit);
		return l;
	}

	@Override
	public List<Tuition> fetchTuitionsForYear(School defaultSchool,
			String year) {
		// TODO Auto-generated method stub
	 	final String[] paramNames = { "schoolId","year"};
		final Object[] paramValues = new Object[] { defaultSchool.getId(),year };
		return getHibernateTemplate().findByNamedQueryAndNamedParam(
				"fetchTuitionsForYear", paramNames, paramValues);	
	}
	
	@Override
	public List<Tuition> fetchSortedTuitions(School defaultSchool, String schoolYearName) {
		// TODO Auto-generated method stub
	 	final String[] paramNames = { "schoolId", "schoolYearName"};
		final Object[] paramValues = new Object[] { defaultSchool.getId(), schoolYearName};
		return getHibernateTemplate().findByNamedQueryAndNamedParam(
				"fetchSortedTuitions", paramNames, paramValues);	
	}

	@Override
	public StudentTuition getStudentTuition(Long studentId, Long tuitionId) {
		// TODO Auto-generated method stub
	 	final String[] paramNames = { "studentId","tuitionId"};
			final Object[] paramValues = new Object[] { studentId,tuitionId };
			List l = getHibernateTemplate().findByNamedQueryAndNamedParam(
					"getStudentTuition", paramNames, paramValues);	
			
			if(l.size()>0){
				return (StudentTuition)l.get(0);
			}
			return null;
	}
	
	public List<BaseEntity> searchParents(Parent parent, School school) {
		DetachedCriteria crit = DetachedCriteria.forClass(Parent.class);
		crit.createCriteria("school").add(Restrictions.eq("id", school.getId()));
		
		if (StringUtils.isNotBlank(parent.getLastName()))
				crit.add(Restrictions.like("lastName", parent.getLastName() + "%"));
		
		if (StringUtils.isNotBlank(parent.getFirstName()))
				crit.add(Restrictions.like("firstName", parent.getFirstName() + "%"));
		
		List l = getHibernateTemplate().findByCriteria(crit);
		return l;
	}
	
	
	public Parent searchParent(Parent parent, School school) {
		DetachedCriteria crit = DetachedCriteria.forClass(Parent.class);
		crit.createCriteria("school").add(Restrictions.eq("id", school.getId()));
		crit.add(Restrictions.eq("lastName", parent.getLastName()));
		crit.add(Restrictions.eq("firstName", parent.getFirstName()));
		//crit.add(Restrictions.eq("middleName", parent.getMiddleName()));
		List l = getHibernateTemplate().findByCriteria(crit);
		
		if (l.size() > 0)
			return (Parent) l.get(0);
		
		return null;
	}
	
	public List<Parent> searchParentsToAssign(Parent parent, School school) {
		DetachedCriteria crit = DetachedCriteria.forClass(Parent.class);
		crit.createCriteria("school").add(Restrictions.eq("id", school.getId()));
		
		if (StringUtils.isNotBlank(parent.getLastName()))
			crit.add(Restrictions.like("lastName", parent.getLastName() + "%"));
		if (StringUtils.isNotBlank(parent.getFirstName()))
			crit.add(Restrictions.eq("firstName", parent.getFirstName() + "%"));
		
		List l = getHibernateTemplate().findByCriteria(crit);
		
		return l;
	}
	
	public Integer getStudentsCountByLevelClassAndYear(Long schoolYearId, Long levelClassId, School school) {
		BigInteger numberOfStudents = null;
		
		String sqlQuery = "SELECT count(STUDENT_ID) "
						+ "FROM SENROLLMENT SE "
						+ "INNER JOIN CLASS C ON C.CLASS_ID = SE.CLASS_ID "
						+ "INNER JOIN SCHOOLYEAR SY ON SY.SCHOOLYEAR_ID = SE.SCHOOLYEAR_ID "
						+ "WHERE SE.SCHOOL_ID = ? AND SY.SCHOOLYEAR_ID = ? AND C.CLASS_ID = ? ";
		
		Session session = getHibernateTemplate().getSessionFactory().openSession();
		Query query = session.createSQLQuery(sqlQuery);
		
		query.setParameter(0, school.getId());
		query.setParameter(1, schoolYearId);
		query.setParameter(2, levelClassId);
		
		Object object = query.uniqueResult();
		numberOfStudents = (BigInteger) (object != null ? object : 0);		
		session.close();
		return new Integer(numberOfStudents.toString());
	}

	public List<Bulletin> getAllBulletins (SchoolYear schoolYear,
			User currentUser, String className) {
		List<Bulletin> bulletins = new ArrayList<Bulletin>();
		String sql = null;
		Query query = null;
		Object object = null;
		
		Session session = getHibernateTemplate().getSessionFactory().openSession();		
		String sqlQuery = "SELECT B.BULLETIN_ID, B.CLASS_NAME, B.NBR_STUDENT, B.LEVEL_ID, B.STUDENT_ID, B.MARK "
				+ "FROM BULLETIN B " 
				+ "WHERE B.SCHOOL_ID = ? AND B.SCHOOLYEAR_ID = ? ";
		
		if (StringUtils.isNotEmpty(className))
			sqlQuery += "AND B.CLASS_NAME = ? ";
		
		sqlQuery += "ORDER BY B.STUDENT_ID ";
						
		query = session.createSQLQuery(sqlQuery);
		query.setParameter(0, currentUser.getSchool().getId());
		query.setParameter(1, schoolYear.getId());
		
		if (StringUtils.isNotEmpty(className))
			query.setParameter(2, className);

		List<Object[]> objects = query.list();

		for(Object[] obj:objects){
			Bulletin bulletin = new Bulletin();
			bulletin.setId(new Long(obj[0].toString()));
			bulletin.setClassName((String)obj[1]);
			bulletin.setNbrStudent(new Integer(obj[2].toString()));
			bulletin.setLevelId(new Long(obj[3].toString()));
			bulletin.setStudentId(new Long(obj[4].toString()));
			bulletin.setMark(new Double(obj[5].toString()));			
			bulletins.add(bulletin);			 
		}
		session.close();
		return bulletins;
	}
	
	public List<Bulletin> getAllBulletins (SchoolYear schoolYear,
			TermGroup termGroup, User currentUser, String className) {
		List<Bulletin> bulletins = new ArrayList<Bulletin>();
		String sql = null;
		Query query = null;
		Object object = null;
		
		Session session = getHibernateTemplate().getSessionFactory().openSession();		
		String sqlQuery = "SELECT B.BULLETIN_ID, B.CLASS_NAME, B.NBR_STUDENT, B.LEVEL_ID, B.STUDENT_ID, B.MARK "
				+ "FROM BULLETIN B "
				+ "INNER JOIN TERM T ON B.TERM_ID = T.TERM_ID "
				+ "INNER JOIN TERM_GROUP TG ON T.TERM_GROUP_ID = TG.TERM_GROUP_ID "
				+ "WHERE B.SCHOOL_ID = ? AND B.SCHOOLYEAR_ID = ? AND TG.TERM_GROUP_ID = ? ";
		
		if (StringUtils.isNotEmpty(className))
			sqlQuery += "AND B.CLASS_NAME = ? ";
		
		sqlQuery += " ORDER BY B.STUDENT_ID ";
						
		query = session.createSQLQuery(sqlQuery);
		query.setParameter(0, currentUser.getSchool().getId());
		query.setParameter(1, schoolYear.getId());
		query.setParameter(2, termGroup.getId());

		if (StringUtils.isNotEmpty(className))
			query.setParameter(3, className);
		
		List<Object[]> objects = query.list();

		for(Object[] obj:objects){
			Bulletin bulletin = new Bulletin();
			bulletin.setId(new Long(obj[0].toString()));
			bulletin.setClassName((String)obj[1]);
			bulletin.setNbrStudent(new Integer(obj[2].toString()));
			bulletin.setLevelId(new Long(obj[3].toString()));
			bulletin.setStudentId(new Long(obj[4].toString()));
			bulletin.setMark(new Double(obj[5].toString()));			
			bulletins.add(bulletin);			 
		}
		session.close();
		return bulletins;
	}
	
	
	public List<AssignmentFile> getCourseAssignmentFiles(School school, FileSearchVO fileSearch) {
		List<AssignmentFile> files = new ArrayList<AssignmentFile>();
		Long yearId = null;
		Long classId = null;
		Long subjectId = null;
		String sql = null;
		Query query = null;
		Object object = null;
		
		Session session = getHibernateTemplate().getSessionFactory().openSession();
		yearId = getId("SCHOOLYEAR", fileSearch.getYearName(), school.getId(), session);
		classId = getId("CLASS", fileSearch.getClassName(), school.getId(), session);
		subjectId = getId("SUBJECT", fileSearch.getSubjectName(), school.getId(), session);
		
		String sqlQuery = "SELECT AF.ASSIGNMENT_FILE_ID, NAME, DESCRIPTION, UPLOAD_BY, FILE_TYPE " 
				+ "FROM ASSIGNMENT_FILE AF ";
				
		if (StringUtils.isNotBlank(fileSearch.getYearName()) || StringUtils.isNotBlank(fileSearch.getClassName()) || StringUtils.isNotBlank(fileSearch.getSubjectName())) {		
			sqlQuery += "INNER JOIN COURSE_ASSIGNMENT_FILE CAF ON AF.ASSIGNMENT_FILE_ID = CAF.ASSIGNMENT_FILE_ID ";
		}
		sqlQuery += "WHERE AF.SCHOOL_ID = ? ";
		
		if (yearId != null) {
			sqlQuery += "AND CAF.SCHOOLYEAR_ID = ? ";
		}
		
		if (subjectId != null) {
			sqlQuery += "AND CAF.SUBJECT_ID = ? ";
		}
		
		if (classId !=  null) {
			sqlQuery += "AND CAF.CLASS_ID = ? ";
		}
		
		if (StringUtils.isNotBlank(fileSearch.getFileName())) {
			sqlQuery += "AND AF.NAME LIKE ? ";
		}
		
		query = session.createSQLQuery(sqlQuery);
		int parameterIndex = 0;
		
		query.setParameter(parameterIndex, school.getId());
		parameterIndex ++;
		
		if (yearId != null) {
			query.setParameter(parameterIndex, yearId);
			parameterIndex ++;
		}
		
		if (subjectId != null) {
			query.setParameter(parameterIndex, subjectId);
			parameterIndex ++;
		}
		
		if (classId !=  null) {
			query.setParameter(parameterIndex, classId);
			parameterIndex ++;
		}
		
		if (StringUtils.isNotBlank(fileSearch.getFileName())) {
			query.setParameter(parameterIndex, fileSearch.getFileName() + "%");
			parameterIndex ++;
		}

		List<Object[]> objects = query.list();

		for(Object[] obj:objects){
			AssignmentFile file = new AssignmentFile();
			file.setId(new Long(obj[0].toString()));
			file.setName((String)obj[1]);
			file.setDescription((String)obj[2]);
			file.setUploadBy(obj[3] != null ? new Long(obj[3].toString()) : null);
			file.setFileType((String)obj[4]);
			
			files.add(file);			 
		}
		session.close();
		return files;
	}
	
	public List<AssignmentFile> searchCourseAssignmentFiles(School school, FileSearchVO fileSearch) {
		List<AssignmentFile> files = new ArrayList<AssignmentFile>();
		Long yearId = null;
		Long classId = null;
		Long subjectId = null;
		Long examTypeId = null;
		String sql = null;
		Query query = null;
		Object object = null;
		
		Session session = getHibernateTemplate().getSessionFactory().openSession();
		yearId = getId("SCHOOLYEAR", fileSearch.getYearName(), school.getId(), session);
		classId = getId("CLASS", fileSearch.getClassName(), school.getId(), session);
		subjectId = getId("SUBJECT", fileSearch.getSubjectName(), school.getId(), session);
		examTypeId = getId("EXAM_TYPE", fileSearch.getExamTypeName(), school.getId(), session);
		
		String sqlQuery = "SELECT * FROM ( SELECT SY.NAME YEARNAME, C.NAME CLASSNAME, S.NAME SUBJECTNAME, AF.ASSIGNMENT_FILE_ID, AF.NAME, AF.DESCRIPTION, UPLOAD_BY, " 
				+ "ET.NAME EXAMTYPENAME, CAF.EXAM_DATE, CAF.DUE_DATE, FILE_TYPE "
				+ "FROM ASSIGNMENT_FILE AF "
				+ "LEFT OUTER JOIN COURSE_ASSIGNMENT_FILE CAF ON AF.ASSIGNMENT_FILE_ID = CAF.ASSIGNMENT_FILE_ID "
				+ "LEFT OUTER JOIN SCHOOLYEAR SY ON SY.SCHOOLYEAR_ID = CAF.SCHOOLYEAR_ID "
				+ "LEFT OUTER JOIN SUBJECT S ON S.SUBJECT_ID = CAF.SUBJECT_ID "
				+ "LEFT OUTER JOIN CLASS C ON C.CLASS_ID = CAF.CLASS_ID "
				+ "LEFT OUTER JOIN EXAM_TYPE ET ON ET.EXAM_TYPE_ID = CAF.EXAM_TYPE_ID "
				+ "WHERE AF.SCHOOL_ID = ? ";
		
		if (yearId != null) {
			sqlQuery += "AND CAF.SCHOOLYEAR_ID = ? ";
		}
		
		if (subjectId != null) {
			sqlQuery += "AND CAF.SUBJECT_ID = ? ";
		}
		
		if (classId !=  null) {
			sqlQuery += "AND CAF.CLASS_ID = ? ";
		}
		
		if (examTypeId !=  null) {
			sqlQuery += "AND CAF.EXAM_TYPE_ID = ? ";
		}
		
		if (fileSearch.getExamDate() !=  null) {
			sqlQuery += "AND CAF.EXAM_DATE = ? ";
		}
		
		if (fileSearch.getReturnDate() !=  null) {
			sqlQuery += "AND CAF.DUE_DATE = ? ";
		}
		
		if (StringUtils.isNotBlank(fileSearch.getFileName())) {
			sqlQuery += "AND AF.NAME LIKE ? ";
		}
		
		sqlQuery += ") t" ;
		
		query = session.createSQLQuery(sqlQuery);
		int parameterIndex = 0;
		
		query.setParameter(parameterIndex, school.getId());
		parameterIndex ++;
		
		if (yearId != null) {
			query.setParameter(parameterIndex, yearId);
			parameterIndex ++;
		}
		
		if (subjectId != null) {
			query.setParameter(parameterIndex, subjectId);
			parameterIndex ++;
		}
		
		if (classId !=  null) {
			query.setParameter(parameterIndex, classId);
			parameterIndex ++;
		}
		
		if (examTypeId !=  null) {
			query.setParameter(parameterIndex, examTypeId);
			parameterIndex ++;
		}
		
		if (fileSearch.getExamDate() !=  null) {
			query.setParameter(parameterIndex, fileSearch.getExamDate());
			parameterIndex ++;
		}
		
		if (fileSearch.getReturnDate() !=  null) {
			query.setParameter(parameterIndex, fileSearch.getReturnDate());
			parameterIndex ++;
		}
		
		if (StringUtils.isNotBlank(fileSearch.getFileName())) {
			query.setParameter(parameterIndex, fileSearch.getFileName() + "%");
			parameterIndex ++;
		}

		List<Object[]> objects = query.list();

		for(Object[] obj:objects){
			AssignmentFile file = new AssignmentFile();
			file.setYearName((String)obj[0]);
			file.setClassName((String)obj[1]);
			file.setSubjectName((String)obj[2]);
			file.setId(new Long(obj[3].toString()));
			file.setName((String)obj[4]);
			file.setDescription((String)obj[5]);
			file.setUploadBy(obj[6] != null ? new Long(obj[6].toString()) : null);
			file.setExamTypeName((String)obj[7]);
			file.setExamDate((Date)obj[8]);
			file.setReturnDate((Date)obj[9]);
			file.setFileType((String)obj[10]);
			
			files.add(file);			 
		}
		session.close();
		return files;
	}
	
	public void addCourseAssignmentFiles(FileSearchVO examFile, List<AssignmentFile> assignedFiles, User user) {
		Long yearId = null;
		Long classId = null;
		Long subjectId = null;
		String sql = null;
		Query query = null;
		Object object = null;
		
		Session session = getHibernateTemplate().getSessionFactory().openSession();
		yearId = getId("SCHOOLYEAR", examFile.getYearName(), user.getSchool().getId(), session);
		classId = getId("CLASS", examFile.getClassName(), user.getSchool().getId(), session);
		subjectId = getId("SUBJECT", examFile.getSubjectName(), user.getSchool().getId(), session);
		Long examTypeId = getId("EXAM_TYPE", examFile.getExamTypeName(), user.getSchool().getId(), session);
		
		for (AssignmentFile file : assignedFiles) {
			if (file.getId() == null)
				save(file, user);
			CourseAssignmentFile caf = new CourseAssignmentFile(classId, yearId, subjectId, examTypeId,
					examFile.getExamDate(), examFile.getReturnDate(), file.getId());
			save(caf, user);
		}
	}
	
	public void save(List<TermGroupSummary> entities, User user) throws Exception {
		Session session = null;
		Transaction tx = null;
		
		try {
			session = getHibernateTemplate().getSessionFactory().openSession();
			tx = session.beginTransaction();
			int i = 0;
					
			for (BaseEntity entity : entities) {
				entity.setCreateDate(new Date());
				entity.setModDate(new Date());
				entity.setSchool(user.getSchool());
				entity.setModifiedBy(user.getId());
				session.save(entity);
				
				if ( i == 25 ) { //25, same as the JDBC batch size
			        //flush a batch of inserts and release memory:
			        session.flush();
			        session.clear();
			        i = 0;
			    }
				else 
					i++;
			}
			
			tx.commit();
		
		}
		catch(Exception ex) {
			ex.printStackTrace();
			tx.rollback();
			throw new Exception();
		}
		finally {
			session.close();
		}
	}

	private Long getId(String tableName, String value, Long schoolId, Session session) {
		String sql = null;
		Query query = null;
		Object object = null;
		Long id = null;
		
		if (StringUtils.isNotBlank(value)) {
			sql = "SELECT " + tableName + "_ID FROM " + tableName + " WHERE NAME = '" + value + "' AND SCHOOL_ID = " + schoolId;
			query = session.createSQLQuery(sql);
			object = query.uniqueResult();
			id = (object != null ? new Long(object.toString()) : null);	
		}
		
		return id;
	}
	
	public List<CourseVO> getCourses(School school) {
		// TODO Auto-generated method stub
		List<CourseVO> courses=new ArrayList<CourseVO>();
		final String[] paramNames = {"schoolId"};
		final Object[] paramValues = new Object[] {school.getId()};
		List<Object[]> objects;
		objects = getHibernateTemplate().findByNamedQueryAndNamedParam(
				"getCourseVOs", paramNames, paramValues);
		for (Object[] obj : objects) {
			CourseVO course = new CourseVO();
			course.setId((Long)obj[0]);
			course.setSubject((String)obj[1]);
			course.setTeacherFirstName((String)obj[2]);
			course.setTeacherLastName((String)obj[3]);
			course.setClassName((String)obj[4]);
			course.setBeginDate((Date)obj[5]);
			course.setEndDate((Date)obj[6]);
			course.setGroupCode((String)obj[7]);
			course.setMaxMark((Double)obj[8]);
			course.setTeacherId((Long)obj[9]);

			courses.add(course);
		}
		
		return courses;
	}
	
	public List<CourseVO> getCourses(School school, Long teacherId) {
		// TODO Auto-generated method stub
		List<CourseVO> courses=new ArrayList<CourseVO>();
		final String[] paramNames = {"schoolId","teacherId"};
		final Object[] paramValues = new Object[] {school.getId(),teacherId};
		List<Object[]> objects;
		objects = getHibernateTemplate().findByNamedQueryAndNamedParam(
				"getTeacherCourseVOs", paramNames, paramValues);
		for (Object[] obj : objects) {
			CourseVO course = new CourseVO();
			course.setId((Long)obj[0]);
			course.setSubject((String)obj[1]);
			course.setTeacherFirstName((String)obj[2]);
			course.setTeacherLastName((String)obj[3]);
			course.setClassName((String)obj[4]);
			course.setBeginDate((Date)obj[5]);
			course.setEndDate((Date)obj[6]);
			course.setGroupCode((String)obj[7]);
			course.setMaxMark((Double)obj[8]);
			course.setTeacherId((Long)obj[9]);

			courses.add(course);
		}
		
		return courses;
	}
	
	public List<CourseVO> getCourses(Long classId,School school) {
		// TODO Auto-generated method stub
		List<CourseVO> courses=new ArrayList<CourseVO>();
		final String[] paramNames = {"schoolId","classId"};
		final Object[] paramValues = new Object[] {school.getId(),classId};
		List<Object[]> objects;
		objects = getHibernateTemplate().findByNamedQueryAndNamedParam(
				"getClassCourseVOs", paramNames, paramValues);
		for (Object[] obj : objects) {
			CourseVO course = new CourseVO();
			course.setId((Long)obj[0]);
			course.setSubject((String)obj[1]);
			course.setTeacherFirstName((String)obj[2]);
			course.setTeacherLastName((String)obj[3]);
			course.setClassName((String)obj[4]);
			course.setBeginDate((Date)obj[5]);
			course.setEndDate((Date)obj[6]);
			course.setGroupCode((String)obj[7]);
			course.setMaxMark((Double)obj[8]);
			course.setTeacherId((Long)obj[9]);

			courses.add(course);
		}
		
		return courses;
	}

}
