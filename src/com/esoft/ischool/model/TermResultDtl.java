package com.esoft.ischool.model;

public class TermResultDtl {
	private String subjectName;
	private String teacherName;
	private Double moyenneClasse;
	private Double moyenneExam;
	private Double ratioClass;
	private Double ratioExam;
	private Double moyenne;
	private Integer maxMark;
	private Integer teacherId;

	public Integer getTeacherId() {
		return teacherId;
	}

	public void setTeacherId(Integer teacherId) {
		this.teacherId = teacherId;
	}

	public Integer getMaxMark() {
		return maxMark;
	}

	public void setMaxMark(Integer maxMark) {
		this.maxMark = maxMark;
	}

	public Double getMoyenne() {
		return moyenne;

	}

	public void setMoyenne(Double moyenne) {
		this.moyenne = moyenne;
	}

	public String getSubjectName() {
		return subjectName;
	}

	public void setSubjectName(String subjectName) {
		this.subjectName = subjectName;
	}

	public String getTeacherName() {
		return teacherName;
	}

	public void setTeacherName(String teacherName) {
		this.teacherName = teacherName;
	}

	public Double getMoyenneClasse() {
		return moyenneClasse;

	}

	public void setMoyenneClasse(Double moyenneClasse) {
		this.moyenneClasse = moyenneClasse;
	}

	public Double getMoyenneExam() {
		return moyenneExam;

	}

	public void setMoyenneExam(Double moyenneExam) {
		this.moyenneExam = moyenneExam;
	}

	public Double getRatioClass() {
		return ratioClass;

	}

	public void setRatioClass(Double ratioClass) {
		this.ratioClass = ratioClass;
	}

	public Double getRatioExam() {
		return ratioExam;

	}

	public void setRatioExam(Double ratioExam) {
		this.ratioExam = ratioExam;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		else
			return false;

	}

	public int compareTo(TermResultDtl other) {

		if (other != null
				&& other.getSubjectName().equalsIgnoreCase(subjectName)) {
			return other.getMoyenne().compareTo(moyenne);
		} else {
			return subjectName.compareTo(other.getSubjectName());
		}
	}
}
