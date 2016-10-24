package com.esoft.ischool.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class TermResult {

	private String termName;
	private String yearName;
	private String className;
	private Integer termId;
	private Integer nbrOfStudent;
	private Double moyenne = 0.0;
	private Double minMoyenne = 0.0;
	private Double maxMoyenne = 0.0;
	private List<TermResultSummary> termResultSmry = new ArrayList<TermResultSummary>();

	public TermResult() {
	}

	public TermResult(List<Averages> averages) {

		if (averages != null) {
			Collections.sort(averages);
			boolean first = true;
			Integer previousStudentId = 0;
			TermResultSummary trs = null;
			for (Averages avg : averages) {

				if (first) {
					this.termName = avg.getTerm().getName();
					this.yearName = avg.getSchoolYear().getYear();
					this.className = avg.getClassName();
					this.termId = avg.getTerm().getId().intValue();
					this.nbrOfStudent = avg.getNbrStudent();
					first = false;
				}

				Integer studentId = avg.getStudent().getId().intValue();

				if (!previousStudentId.equals(studentId)) {
					trs = new TermResultSummary();
					trs.setPicture(avg.getStudent().getImage());
					trs.setStudentId(studentId);
					trs.setStudentName(avg.getStudent().getLastName()
							+ " "
							+ (avg.getStudent().getMiddleName() == null ? ""
									: avg.getStudent().getMiddleName()) + " "
							+ avg.getStudent().getFirstName());
					trs.setEnrollmentId(avg.getStudent().getCurrentEnrollment()
							.getId().intValue());
					termResultSmry.add(trs);
					previousStudentId = studentId;

				}

				TermResultDtl dtl = new TermResultDtl();
				dtl.setMoyenneClasse(avg.getClassMark());
				dtl.setMoyenneExam(avg.getExamMark());
				dtl.setRatioClass(avg.getClassRatio());
				dtl.setRatioExam(avg.getExamRatio());
				dtl.setMoyenne(avg.getAverageMark());
				dtl.setSubjectName(avg.getSubject().getName());
				dtl.setTeacherName(avg.getTeacher().getLastName()
						+ " "
						+ (avg.getTeacher().getMiddleName() == null ? "" : avg
								.getTeacher().getMiddleName()) + " "
						+ avg.getTeacher().getFirstName());
				dtl.setMaxMark(avg.getMaxMark());
				dtl.setTeacherId(avg.getTeacher().getId().intValue());
				trs.addDtl(dtl);
			}
			this.rank();
		}
	}

	public Double getMinMoyenne() {
		return  minMoyenne ;
	}

	public void setMinMoyenne(Double minMoyenne) {
		this.minMoyenne = minMoyenne;
	}

	public Double getMaxMoyenne() {
		return  maxMoyenne ;
	}

	public void setMaxMoyenne(Double maxMoyenne) {

		this.maxMoyenne = maxMoyenne;
	}

	public Double getMoyenne() {

		return  moyenne ;

	}

	public void setMoyenne(Double moyenne) {
		this.moyenne = moyenne;
	}

	public void rank() {

		for (TermResultSummary smry : termResultSmry) {
			smry.calculateMoyenne();
		}
		Collections.sort(termResultSmry);
		int b = 0;
		for (TermResultSummary smry : termResultSmry) {
			if (b == 0) {
				maxMoyenne = smry.getMoyenne();
			} else if (b == termResultSmry.size() - 1) {
				minMoyenne = smry.getMoyenne();
			}
			smry.setRank(++b);
			moyenne += smry.getMoyenne();
		}
		moyenne /= b;
	}

	public void addSmry(TermResultSummary smry) {
		termResultSmry.add(smry);
	}

	public List<TermResultSummary> getTermResultSmry() {
		return termResultSmry;
	}

	public void setTermResultSmry(List<TermResultSummary> termResultSmry) {
		this.termResultSmry = termResultSmry;
	}

	public String getTermName() {
		return termName;
	}

	public void setTermName(String termName) {
		this.termName = termName;
	}

	public String getYearName() {
		return yearName;
	}

	public void setYearName(String yearName) {
		this.yearName = yearName;
	}

	public String getClassName() {
		return className;
	}

	public void setClassName(String className) {
		this.className = className;
	}

	public Integer getTermId() {
		return termId;
	}

	public void setTermId(Integer termId) {
		this.termId = termId;
	}

	public Integer getNbrOfStudent() {
		return nbrOfStudent;
	}

	public void setNbrOfStudent(Integer nbrOfStudent) {
		this.nbrOfStudent = nbrOfStudent;
	}

}
