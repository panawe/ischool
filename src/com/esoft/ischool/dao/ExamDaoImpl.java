package com.esoft.ischool.dao;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.hibernate.Hibernate;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Repository;

import com.esoft.ischool.model.Course;
import com.esoft.ischool.model.Exam;
import com.esoft.ischool.model.LevelClass;
import com.esoft.ischool.model.Mark;
import com.esoft.ischool.model.School;
import com.esoft.ischool.model.Student;
import com.esoft.ischool.model.StudentEnrollment;
import com.esoft.ischool.model.Teacher;
import com.esoft.ischool.vo.ExamSearchVO;
import com.esoft.ischool.vo.ExamVO;
import com.esoft.ischool.vo.MarkSearchVO;
import com.esoft.ischool.vo.MarkVO;

@Repository("examDao")
@Scope("session")
public class ExamDaoImpl extends BaseDaoImpl {

	@Autowired
	public ExamDaoImpl(SessionFactory sessionFactory) {
		setSessionFactory(sessionFactory);
	}
	
	@SuppressWarnings("unchecked")
	public List<Teacher> getTeacher(String className, String subjectName){
		
		List<Teacher> teachers = null;
		final String[] paramNames = { "className","subjectName"};
		final Object[] paramValues = new Object[] {className,subjectName};

		teachers = getHibernateTemplate().findByNamedQueryAndNamedParam(
				"getTeacher", paramNames, paramValues);

		return teachers;		
		
	}
	@SuppressWarnings("unchecked")
	public List<Course> getCourses(String className, String subjectName){
		
		List<Course> courses = null;
		final String[] paramNames = { "className","subjectName"};
		final Object[] paramValues = new Object[] {className,subjectName};

		courses = getHibernateTemplate().findByNamedQueryAndNamedParam(
				"getCourse", paramNames, paramValues);

		return courses;		
		
	}

	public List<Student> getStudents(Exam exam) {
		// TODO Auto-generated method stub
		List<Student> students=new ArrayList<Student>();
		LevelClass lclass = exam.getCourse().getLevelClass();
		final String[] paramNames = { "classId","examId"};
		final Object[] paramValues = new Object[] {lclass.getId(),exam.getId()};
		List<Object[]> objects;
		objects = getHibernateTemplate().findByNamedQueryAndNamedParam(
				"getStatudentOfAClass", paramNames, paramValues);
		
		
		for(Object[] obj:objects){
			Student st=(Student)obj[0];
			st.setMark((Double)obj[1] );
			st.setGrade((String)obj[2] );
			st.setApprovedBy(obj[3]==null?0:(Long)obj[3]);
			students.add(st);
		}
		
		return students;
	}

	public Mark getMark(Exam exam, StudentEnrollment studentEnrollment) {
		// TODO Auto-generated method stub
		List<Mark> marks=null;
		LevelClass lclass = exam.getCourse().getLevelClass();
		final String[] paramNames = { "examId","enrollmentId"};
		final Object[] paramValues = new Object[] {exam.getId(),studentEnrollment.getId()};

		marks = getHibernateTemplate().findByNamedQueryAndNamedParam(
				"getMark", paramNames, paramValues);
		
		return marks==null||marks.size()<1?null:marks.get(0);
	}

	public List<ExamVO> getAllExamVOs(School school) {
		// TODO Auto-generated method stub
		List<ExamVO> examVOs=new ArrayList<ExamVO>();
		final String[] paramNames = {"schoolId"};
		final Object[] paramValues = new Object[] {school.getId()};
		List<Object[]> objects;
		objects = getHibernateTemplate().findByNamedQueryAndNamedParam(
				"getAllExamVOs", paramNames, paramValues);

		for(Object[] obj:objects){
			ExamVO ev= new ExamVO();
			ev.setId((Long)obj[0]);
			ev.setName(obj[1]==null?null:(String)obj[1]);
			ev.setExamDate((Date)obj[2]);
			ev.setSchoolYear((String)obj[3]);
			ev.setExamType((String)obj[4]);
			ev.setTerm((String)obj[5]);
			ev.setCourse((String)obj[6]);
			ev.setRatio((Integer)obj[7]);
			ev.setEvaluationType((Short)obj[8]);
			ev.setPublishMarks((Short)obj[9]);
			ev.setMaxMark((Double)obj[10]);
			ev.setClassName((String)obj[11]);
			
			examVOs.add(ev);			 
		}
		return examVOs;
	}
	
	public List<ExamVO> getAllExamVOs(ExamSearchVO examSearch, School school) {
		// TODO Auto-generated method stub
		List<ExamVO> examVOs=new ArrayList<ExamVO>();
		final String[] paramNames = {"schoolId"};
		final Object[] paramValues = new Object[] {school.getId()};
		List<Object[]> objects;
		
		String sqlQuery = " SELECT * FROM (SELECT E.EXAM_ID id, E.NAME name, E.EXAM_DATE examDate, SY.NAME schoolYear, ET.NAME examType, "
				+ "T.NAME term, S.NAME course, RATIO ratio, EVALUATION_TYPE evaluationType, PUBLISH_MARKS publishMarks, "
				+ "E.MAX_MARK maxMark, CL.NAME className "
				+ "FROM EXAM E, SCHOOLYEAR SY,EXAM_TYPE ET, TERM T, COURSE C, SUBJECT S, CLASS CL "
				+ "WHERE E.SCHOOLYEAR_ID = SY.SCHOOLYEAR_ID AND E.EXAM_TYPE_ID = ET.EXAM_TYPE_ID "
				+ "AND E.TERM_ID = T.TERM_ID AND E.COURSE_ID = C.COURSE_ID AND S.SUBJECT_ID = C.SUBJECT_ID "
				+ "AND C.CLASS_ID = CL.CLASS_ID AND E.SCHOOL_ID = ? "; 
		
		if (StringUtils.isNotBlank(examSearch.getClassName())) {
			sqlQuery += " AND CL.NAME LIKE ? ";
		}
		
		if (StringUtils.isNotBlank(examSearch.getExamName())) {
			sqlQuery += " AND E.NAME LIKE ? ";
		}
		
		if (StringUtils.isNotBlank(examSearch.getYear())) {
			sqlQuery += " AND SY.NAME LIKE ? ";
		}
		
		if (StringUtils.isNotBlank(examSearch.getTermName())) {
			sqlQuery += " AND T.NAME LIKE ? ";
		}
		
		if (StringUtils.isNotBlank(examSearch.getSubjectName())) {
			sqlQuery += " AND S.NAME LIKE ? ";
		}
		
		if (examSearch.getExamDate() != null) {
			sqlQuery += " AND E.EXAM_DATE = ? ";
		}
		
		
		
		sqlQuery += ") t" ;
		
		int parameterIndex = 0;
		Session session = getHibernateTemplate().getSessionFactory().openSession();
		SQLQuery query = session.createSQLQuery(sqlQuery);
		query.addScalar("id", Hibernate.LONG);
		query.addScalar("name", Hibernate.STRING);
		query.addScalar("examDate", Hibernate.DATE);
		query.addScalar("schoolYear", Hibernate.STRING);
		query.addScalar("examType", Hibernate.STRING);
		query.addScalar("term", Hibernate.STRING);
		query.addScalar("course", Hibernate.STRING);
		query.addScalar("ratio", Hibernate.INTEGER);
		query.addScalar("evaluationType", Hibernate.SHORT);
		query.addScalar("publishMarks", Hibernate.SHORT);
		query.addScalar("maxMark", Hibernate.DOUBLE);
		query.addScalar("className", Hibernate.STRING);
		
		query.setParameter(parameterIndex, school.getId());
		parameterIndex ++;

		if (StringUtils.isNotBlank(examSearch.getClassName())) {
			query.setParameter(parameterIndex, examSearch.getClassName() + "%");
			parameterIndex ++;
		}

		if (StringUtils.isNotBlank(examSearch.getExamName())) {
			query.setParameter(parameterIndex, examSearch.getExamName() + "%");
			parameterIndex ++;
		}

		if (StringUtils.isNotBlank(examSearch.getYear())) {
			query.setParameter(parameterIndex, examSearch.getYear() + "%");
			parameterIndex ++;
		}
		
		if (StringUtils.isNotBlank(examSearch.getTermName())) {
			query.setParameter(parameterIndex, examSearch.getTermName() + "%");
			parameterIndex ++;
		}
		
		if (StringUtils.isNotBlank(examSearch.getSubjectName())) {
			query.setParameter(parameterIndex, examSearch.getSubjectName()+ "%");
			parameterIndex ++;
		}

		if (examSearch.getExamDate() != null) {
			query.setParameter(parameterIndex, examSearch.getExamDate());
			parameterIndex ++;
		}
		
		objects = query.list();

		for(Object[] obj:objects){
			ExamVO ev= new ExamVO();
			ev.setId((Long)obj[0]);
			ev.setName(obj[1] == null ? null : (String)obj[1]);
			ev.setExamDate((Date)obj[2]);
			ev.setSchoolYear((String)obj[3]);
			ev.setExamType((String)obj[4]);
			ev.setTerm((String)obj[5]);
			ev.setCourse((String)obj[6]);
			ev.setRatio((Integer)obj[7]);
			ev.setEvaluationType((Short)obj[8]);
			ev.setPublishMarks((Short)obj[9]);
			ev.setMaxMark((Double)obj[10]);
			ev.setClassName((String)obj[11]);
			
			examVOs.add(ev);			 
		}
		session.close();
		return examVOs;
	}
	
	
	public List<MarkVO> getMarkVOs(MarkSearchVO markSearch, School school) {
		// TODO Auto-generated method stub
		List<MarkVO> markVOs = new ArrayList<MarkVO>();
		final String[] paramNames = {"schoolId"};
		final Object[] paramValues = new Object[] {school.getId()};
		List<Object[]> objects;
		
		String sqlQuery = " SELECT * FROM ( "
				+ "SELECT M.MARK_ID MARK_ID, M.MARK MARK, M.GRADE_ID GRADE_ID, M.ENROLLMENT_ID ENROLLMENT_ID, M.EXAM_ID EXAM_ID, ST.STUDENT_ID STUDENT_ID, "
				+ "E.EXAM_DATE EXAM_DATE, E.NAME EXAM_NAME, E.TERM_ID TERM_ID, E.SCHOOLYEAR_ID SCHOOLYEAR_ID, E.EXAM_TYPE_ID EXAM_TYPE_ID, E.COURSE_ID COURSE_ID, "
				+ "T.NAME TERM_NAME, SY.NAME SCHOOLYEAR_NAME, ET.NAME EXAM_TYPE_NAME, C.MAX_MARK MAX_MARK, S.NAME SUBJECT_NAME, CL.NAME CLASS_NAME, "
				+ "ST.MATRICULE STUDENT_MATRICULE, ST.LAST_NAME STUDENT_LAST_NAME, ST.FIRST_NAME STUDENT_FIRST_NAME, ST.MIDDLE_NAME STUDENT_MIDDLE_NAME, ST.PHONE STUDENT_PHONE, "
				+ "	G.NAME GRADE_NAME, M.APPROVED_BY APPROVED_BY "
				+ "FROM MARK M "
				+ "INNER JOIN EXAM E ON E.EXAM_ID = M.EXAM_ID "
				+ "INNER JOIN TERM T ON T.TERM_ID = E.TERM_ID "
				+ "INNER JOIN SCHOOLYEAR SY ON SY.SCHOOLYEAR_ID = E.SCHOOLYEAR_ID "
				+ "INNER JOIN EXAM_TYPE ET ON ET.EXAM_TYPE_ID = E.EXAM_TYPE_ID "
				+ "INNER JOIN COURSE C ON C.COURSE_ID = E.COURSE_ID "
				+ "INNER JOIN SUBJECT S ON C.SUBJECT_ID = S.SUBJECT_ID "
				+ "INNER JOIN CLASS CL ON C.CLASS_ID = CL.CLASS_ID "
				+ "INNER JOIN SENROLLMENT SE ON M.ENROLLMENT_ID = SE.ENROLLMENT_ID "
				+ "INNER JOIN STUDENT ST ON ST.STUDENT_ID = SE.STUDENT_ID "
				+ "INNER JOIN GRADE G ON M.GRADE_ID = G.GRADE_ID "
				+ "WHERE E.SCHOOL_ID = ? "; 
		
		if (StringUtils.isNotBlank(markSearch.getMatricule())) {
			sqlQuery += " AND ST.MATRICULE LIKE ? ";
		}
		
		if (StringUtils.isNotBlank(markSearch.getLastName())) {
			sqlQuery += " AND ST.LAST_NAME LIKE ? ";
		}
		
		if (StringUtils.isNotBlank(markSearch.getFirstName())) {
			sqlQuery += " AND ST.FIRST_NAME LIKE ? ";
		}
		
		if (StringUtils.isNotBlank(markSearch.getClassName())) {
			sqlQuery += " AND CL.NAME = ? ";
		}
		
		if (StringUtils.isNotBlank(markSearch.getExamName())) {
			sqlQuery += " AND E.NAME LIKE ? ";
		}
		
		if (StringUtils.isNotBlank(markSearch.getYear())) {
			sqlQuery += " AND SY.NAME = ? ";
		}
		
		if (StringUtils.isNotBlank(markSearch.getTermName())) {
			sqlQuery += " AND T.NAME = ? ";
		}
		
		if (StringUtils.isNotBlank(markSearch.getSubjectName())) {
			sqlQuery += " AND S.NAME = ? ";
		}
		
		if (markSearch.getExamDate() != null) {
			sqlQuery += " AND E.EXAM_DATE = ? ";
		}
		
		
		
		sqlQuery += ") t" ;
		
		int parameterIndex = 0;
		Session session = getHibernateTemplate().getSessionFactory().openSession();
		SQLQuery query = session.createSQLQuery(sqlQuery);
		query.addScalar("MARK_ID", Hibernate.LONG);
		query.addScalar("MARK", Hibernate.DOUBLE);
		query.addScalar("GRADE_ID", Hibernate.LONG);
		query.addScalar("ENROLLMENT_ID", Hibernate.LONG);
		query.addScalar("EXAM_ID", Hibernate.LONG);
		query.addScalar("STUDENT_ID", Hibernate.LONG);
		query.addScalar("EXAM_DATE", Hibernate.DATE);
		query.addScalar("EXAM_NAME", Hibernate.STRING);
		query.addScalar("TERM_ID", Hibernate.LONG);
		query.addScalar("SCHOOLYEAR_ID", Hibernate.LONG);
		query.addScalar("EXAM_TYPE_ID", Hibernate.LONG);
		query.addScalar("COURSE_ID", Hibernate.LONG);
		query.addScalar("TERM_NAME", Hibernate.STRING);
		query.addScalar("SCHOOLYEAR_NAME", Hibernate.STRING);
		query.addScalar("EXAM_TYPE_NAME", Hibernate.STRING);
		query.addScalar("MAX_MARK", Hibernate.DOUBLE);
		query.addScalar("SUBJECT_NAME", Hibernate.STRING);
		query.addScalar("CLASS_NAME", Hibernate.STRING);
		query.addScalar("STUDENT_MATRICULE", Hibernate.STRING);
		query.addScalar("STUDENT_LAST_NAME", Hibernate.STRING);
		query.addScalar("STUDENT_FIRST_NAME", Hibernate.STRING);
		query.addScalar("STUDENT_MIDDLE_NAME", Hibernate.STRING);
		query.addScalar("STUDENT_PHONE", Hibernate.STRING);
		query.addScalar("GRADE_NAME", Hibernate.STRING);
		query.addScalar("APPROVED_BY", Hibernate.LONG);
		
		
		query.setParameter(parameterIndex, school.getId());
		parameterIndex ++;

		if (StringUtils.isNotBlank(markSearch.getMatricule())) {
			query.setParameter(parameterIndex, markSearch.getMatricule() + "%");
			parameterIndex ++;
		}
		
		if (StringUtils.isNotBlank(markSearch.getLastName())) {
			query.setParameter(parameterIndex, markSearch.getLastName() + "%");
			parameterIndex ++;
		}
		
		if (StringUtils.isNotBlank(markSearch.getFirstName())) {
			query.setParameter(parameterIndex, markSearch.getFirstName() + "%");
			parameterIndex ++;
		}

		if (StringUtils.isNotBlank(markSearch.getClassName())) {
			query.setParameter(parameterIndex, markSearch.getClassName());
			parameterIndex ++;
		}

		if (StringUtils.isNotBlank(markSearch.getExamName())) {
			query.setParameter(parameterIndex, markSearch.getExamName() + "%");
			parameterIndex ++;
		}

		if (StringUtils.isNotBlank(markSearch.getYear())) {
			query.setParameter(parameterIndex, markSearch.getYear());
			parameterIndex ++;
		}
		
		if (StringUtils.isNotBlank(markSearch.getTermName())) {
			query.setParameter(parameterIndex, markSearch.getTermName());
			parameterIndex ++;
		}
		
		if (StringUtils.isNotBlank(markSearch.getSubjectName())) {
			query.setParameter(parameterIndex, markSearch.getSubjectName());
			parameterIndex ++;
		}

		if (markSearch.getExamDate() != null) {
			query.setParameter(parameterIndex, markSearch.getExamDate());
			parameterIndex ++;
		}
		
		objects = query.list();

		for(Object[] obj:objects){
			MarkVO markVO= new MarkVO();
			markVO.setId((Long)obj[0]);
			markVO.setMark((Double)obj[1]);
			markVO.setGradeId((Long)obj[2]);
			markVO.setStudentEnrollmentId((Long)obj[3]);
			markVO.setExamId((Long)obj[4]);
			markVO.setStudentId((Long)obj[5]);
			markVO.setExamDate((Date)obj[6]);
			markVO.setExamName((String)obj[7]);
			markVO.setTermId((Long)obj[8]);
			markVO.setSchoolYearId((Long)obj[9]);
			markVO.setExamTypeId((Long)obj[10]);
			markVO.setCourseId((Long)obj[11]);
			markVO.setTermName((String)obj[12]);
			markVO.setSchoolYear((String)obj[13]);
			markVO.setExamType((String)obj[14]);
			markVO.setMaxMark((Double)obj[15]);
			markVO.setSubject((String)obj[16]);
			markVO.setClassName((String)obj[17]);
			markVO.setMatricule((String)obj[18]);
			markVO.setStudentLastName((String)obj[19]);
			markVO.setStudentFirstName((String)obj[20]);
			markVO.setStudentMiddleName((String)obj[21]);
			markVO.setStudentPhone((String)obj[22]);
			markVO.setGrade((String)obj[23]);
			markVO.setApprovedBy( (Long)obj[24] != null ? (Long)obj[24] : 0);
			
			markVOs.add(markVO);			 
		}
		session.close();
		return markVOs;
	}
	
}
