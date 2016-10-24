package com.esoft.ischool.service;

import java.util.List;

import org.springframework.transaction.annotation.Transactional;

import com.esoft.ischool.model.Course;
import com.esoft.ischool.model.Exam;
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

public interface ExamService extends BaseService {
	@Transactional(readOnly = true)
	public  List<Teacher> getTeacher(String className, String subjectName);
	@Transactional(readOnly = true)
	public Course getCourse(String className, String subjectName);
	public List<Student> getStudents(Exam exam);
	public Mark getMark(Exam exam, StudentEnrollment studentEnrollment);
	public StudentEnrollment getStudentEnrollment(long studentId,
			long levelClassId, long schoolYearId);
	public List<ExamVO> getAllExamVOs(School school);
	public void recalculateAllNoteForExam(Exam exam);
	public List<ExamVO> getExamNotes(Class cl, ExamSearchVO examSearch, User user);
	public List<MarkVO> getMarkVOs(Class cl, MarkSearchVO markSearch, User user);
}
