package com.esoft.ischool.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import com.esoft.ischool.dao.ExamDaoImpl;
import com.esoft.ischool.model.Course;
import com.esoft.ischool.model.Exam;
import com.esoft.ischool.model.Grade;
import com.esoft.ischool.model.Mark;
import com.esoft.ischool.model.School;
import com.esoft.ischool.model.Student;
import com.esoft.ischool.model.StudentEnrollment;
import com.esoft.ischool.model.Teacher;
import com.esoft.ischool.security.model.User;
import com.esoft.ischool.vo.ExamSearchVO;
import com.esoft.ischool.vo.ExamVO;
import com.esoft.ischool.vo.MarkSearchVO;
import com.esoft.ischool.vo.MarkVO;

@Service("examService")
public class ExamServiceImpl extends BaseServiceImpl implements ExamService {

	@Autowired
	@Qualifier("examDao")
	private ExamDaoImpl examDao;

	public List<Teacher> getTeacher(String className, String subjectName) {
		return examDao.getTeacher(className, subjectName);
	}

	public Course getCourse(String className, String subjectName) {
		List <Course> courses = examDao.getCourses(className, subjectName);
		
		if (courses != null && courses.size() > 0)
			return courses.get(0);
		
		return null;
	}

	public List<Student> getStudents(Exam exam) {
		// TODO Auto-generated method stub
		return examDao.getStudents(exam);
	}

	public Mark getMark(Exam exam, StudentEnrollment studentEnrollment) {
		// TODO Auto-generated method stub
		return examDao.getMark(exam, studentEnrollment);
	}
	public StudentEnrollment getStudentEnrollment(long studentId,
			long levelClassId, long schoolYearId){
		return examDao.getStudentEnrollment(studentId,levelClassId, schoolYearId);
	}

	public List<ExamVO> getAllExamVOs(School school) {
		// TODO Auto-generated method stub
		return examDao.getAllExamVOs(school);
	}
	
	public void recalculateAllNoteForExam(Exam exam) {
		List<Student> students = getStudents(exam);
		for (Student student : students) {
			Mark mark = getMark(exam, getStudentEnrollment(student.getId(),
					exam.getCourse().getLevelClass().getId(), exam.getSchoolYear().getId()));
			if (mark != null) {
				mark.setExam(exam);
				mark.setMark(student.getMark());
				normalize(mark, exam.getMaxMark(), exam.getCourse());
				Grade grade = findGrade(student.getUser().getSchool(), mark.getMark(),
								exam.getCourse().getMaxMark().intValue());
				if (grade != null)
					student.setGrade(grade.getName());
				mark.setGrade(grade);
				mark.setApprovedBy(null);
				update(mark, student.getUser());
			}
		}
	}
	
	private void normalize(Mark mark, Double examMaxMark, Course course) {
		// TODO Auto-generated method stub
		mark.setMark(mark.getMark()*course.getMaxMark()/examMaxMark);
	}
	
	public List<ExamVO> getExamNotes(Class cl, ExamSearchVO examSearch, User user) {
		return examDao.getAllExamVOs(examSearch, user.getSchool());
	}
	
	public List<MarkVO> getMarkVOs(Class cl, MarkSearchVO markSearch, User user) {
		return examDao.getMarkVOs(markSearch, user.getSchool());
	}

}
