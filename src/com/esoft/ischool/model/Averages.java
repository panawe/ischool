package com.esoft.ischool.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.hibernate.validator.NotNull;

@Table(name = "AVERAGES")
@Entity
public class Averages extends BaseEntity implements Comparable {

	@Id
	@Column(name = "AVERAGE_ID")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@ManyToOne
	@JoinColumn(name = "SCHOOLYEAR_ID", nullable = false)
	private SchoolYear schoolYear;

	@ManyToOne
	@JoinColumn(name = "TERM_ID", nullable = false)
	private Term term;

	@ManyToOne
	@JoinColumn(name = "STUDENT_ID", nullable = false)
	private Student student;

	@ManyToOne
	@JoinColumn(name = "TEACHER_ID", nullable = false)
	private Teacher teacher;

	@ManyToOne
	@JoinColumn(name = "LEVEL_ID", nullable = false)
	private Level level;

	@ManyToOne
	@JoinColumn(name = "SUBJECT_ID", nullable = false)
	private Subject subject;

	@NotNull
	@Column(name = "NBR_STUDENT")
	private Integer nbrStudent;

	@Column(name = "CLASS_NAME")
	private String className;

	@Column(name = "CLASS_MARK")
	private Double classMark;

	@Column(name = "CLASS_RATIO")
	private Double classRatio;

	@Column(name = "EXAM_MARK")
	private Double examMark;

	@Column(name = "EXAM_RATIO")
	private Double examRatio;

	@Column(name = "AVERAGE_MARK")
	private Double averageMark;

	@Column(name = "GRADE_NAME")
	private String gradeName;
	

	@Column(name = "SUBJECT_GROUP")
	private String subjectGroup;


	@Column(name = "RANK_NBR")
	private Integer rankNbr;

	@Column(name = "STATUS")
	private Short status;

	@Column(name = "MAX_MARK")
	private Integer maxMark;

	@Transient
	private String subjectName;
	
	@Transient 
	private String teacherName;
	
	public String getSubjectGroup() {
		return subjectGroup;
	}

	public void setSubjectGroup(String subjectGroup) {
		this.subjectGroup = subjectGroup;
	}

	public Subject getSubject() {
		return subject;
	}

	public void setSubject(Subject subject) {
		this.subject = subject;
	}

	public Integer getMaxMark() {
		return maxMark;
	}

	public void setMaxMark(Integer maxMark) {
		this.maxMark = maxMark;
	}

	public Short getStatus() {
		return status;
	}

	public void setStatus(Short status) {
		this.status = status;
	}

	public SchoolYear getSchoolYear() {
		return schoolYear;
	}

	public void setSchoolYear(SchoolYear schoolYear) {
		this.schoolYear = schoolYear;
	}

	public Term getTerm() {
		return term;
	}

	public void setTerm(Term term) {
		this.term = term;
	}

	public Student getStudent() {
		return student;
	}

	public void setStudent(Student student) {
		this.student = student;
	}

	public Teacher getTeacher() {
		return teacher;
	}

	public void setTeacher(Teacher teacher) {
		this.teacher = teacher;
	}

	public Level getLevel() {
		return level;
	}

	public void setLevel(Level level) {
		this.level = level;
	}

	public Integer getNbrStudent() {
		return nbrStudent;
	}

	public void setNbrStudent(Integer nbrStudent) {
		this.nbrStudent = nbrStudent;
	}

	public String getClassName() {
		return className;
	}

	public void setClassName(String className) {
		this.className = className;
	}

	public Double getClassMark() {
		return classMark;
	}

	public void setClassMark(Double classMark) {
		this.classMark = classMark;
	}

	public Double getClassRatio() {
		return classRatio;
	}

	public void setClassRatio(Double classRatio) {
		this.classRatio = classRatio;
	}

	public Double getExamMark() {
		return examMark;
	}

	public void setExamMark(Double examMark) {
		this.examMark = examMark;
	}

	public Double getExamRatio() {
		return examRatio;
	}

	public void setExamRatio(Double examRatio) {
		this.examRatio = examRatio;
	}

	public Double getAverageMark() {
		return averageMark;
	}

	public void setAverageMark(Double averageMark) {
		this.averageMark = averageMark;
	}

	public String getGradeName() {
		return gradeName;
	}

	public void setGradeName(String gradeName) {
		this.gradeName = gradeName;
	}

	public Integer getRankNbr() {
		return rankNbr;
	}

	public void setRankNbr(Integer rankNbr) {
		this.rankNbr = rankNbr;
	}

	public void setId(Long id) {
		this.id = id;
	}

	@Override
	public Long getId() {
		// TODO Auto-generated method stub
		return id;
	}

	
	public String getSubjectName() {
		return subject.getName();
	}

	public void setSubjectName(String subjectName) {
		this.subjectName = subjectName;
	}

	public String getTeacherName() {
		return teacher.getLastName() + " " + teacher.getMiddleName() + " " + teacher.getFirstName();
	}

	public void setTeacherName(String teacherName) {
		this.teacherName = teacherName;
	}

	/*	public int compareTo(Object obj) {
		Averages avg=(Averages)(obj);
		if (this.getStudent().getId() == avg.getStudent().getId()) {
			if (this.getSubject().getId() > avg.getSubject().getId()) {
				return 1;
			} else {
				return -1;
			}
		} else if (this.getStudent().getId() > avg.getStudent().getId()) {
			return 1;
		} else {
			return -1;
		}
	}
*/
	public int compareTo(Object o) {
		Averages other =(Averages)o;
		if (other != null
				&& other.getSubject().getName().equals(subject.getName())) {
			return other.getAverageMark().compareTo(averageMark);
		} else {
			return other.getSubject().getName().compareTo(subject.getName());
		}
	}
}
