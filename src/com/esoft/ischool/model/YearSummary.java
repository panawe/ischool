package com.esoft.ischool.model;

import javax.persistence.*;


@Table(name="YEAR_SUMMARY")
@Entity
public class YearSummary extends BaseEntity implements Comparable<YearSummary>{
	@Id
	@Column(name = "SUMMARY_ID")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@ManyToOne
	@JoinColumn(name = "SCHOOLYEAR_ID", nullable = false)
	private SchoolYear schoolYear;

	//@NotNull
	@Column(name="DECISION")
	private String decision;
	
	@Column(name="DECISION_CODE")
	private Integer decisionCode;	

	@ManyToOne
	@JoinColumn(name = "STUDENT_ID", nullable = false)
	private Student student;

	@ManyToOne
	@JoinColumn(name = "LEVEL_ID", nullable = false)
	private Level level;

	//@NotNull
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

	@Column(name ="comment")
	private String comment;
	
	public YearSummary(){}

	@Transient
	private Integer nbrTerms;
	
	

	public Integer getNbrTerms() {
		return nbrTerms;
	}


	public void setNbrTerms(Integer nbrTerms) {
		this.nbrTerms = nbrTerms;
	}


	public String getComment() {
		return comment;
	}


	public void setComment(String comment) {
		this.comment = comment;
	}


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

	public String getDecision() {
		return decision;
	}


	public void setDecision(String decision) {
		this.decision = decision;
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


	public int compareTo(YearSummary other) {
		// TODO Auto-generated method stub
		if(other !=null ){
			return other.getMark().compareTo(mark);
		}
		return 0;
	}


	public Integer getDecisionCode() {
		return decisionCode;
	}


	public void setDecisionCode(Integer decisionCode) {
		this.decisionCode = decisionCode;
	}
	
	
}
