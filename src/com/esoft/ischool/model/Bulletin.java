package com.esoft.ischool.model;

import javax.persistence.*;

import org.hibernate.validator.NotNull;

@Table(name="BULLETIN")
@Entity
public class Bulletin extends BaseEntity{
	@Id
	@Column(name = "BULLETIN_ID")
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

	@Transient
	private Long studentId;

	@ManyToOne
	@JoinColumn(name = "LEVEL_ID", nullable = false)
	private Level level;

	@Transient
	private Long levelId;

	@NotNull
	@Column(name = "NBR_STUDENT")
	private Integer nbrStudent;

	@Column(name = "CLASS_NAME")
	private String className;

	@Column(name = "MARK")
	private Double mark;

	@Column(name = "GRADE_NAME")
	private String gradeName;

	@Column(name = "RANK_NBR")
	private Integer rankNbr;

	@Column(name = "STATUS")
	private Short status;
	
	@Column(name="COMMENT")
	private String comment;
	
	public String getComment() {
		return comment;
	}


	public void setComment(String comment) {
		this.comment = comment;
	}


	public String getDecision() {
		return decision;
	}


	public void setDecision(String decision) {
		this.decision = decision;
	}

	@Column(name="DECISION")
	private String decision;

	@Column(name="DECISION_CODE")
	private Integer decisionCode;
	
	public Bulletin(){}


	@Override
	public Long getId() {
		// TODO Auto-generated method stub
		return id;
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

	public Double getMark() {
		return mark;
	}

	public void setMark(Double mark) {
		this.mark = mark;
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

	public Short getStatus() {
		return status;
	}

	public void setStatus(Short status) {
		this.status = status;
	}

	public void setId(Long id) {
		this.id = id;
	}


	public Integer getDecisionCode() {
		return decisionCode;
	}


	public void setDecisionCode(Integer decisionCode) {
		this.decisionCode = decisionCode;
	}


	public Long getStudentId() {
		return studentId;
	}


	public void setStudentId(Long studentId) {
		this.studentId = studentId;
	}


	public Long getLevelId() {
		return levelId;
	}


	public void setLevelId(Long levelId) {
		this.levelId = levelId;
	}
}
