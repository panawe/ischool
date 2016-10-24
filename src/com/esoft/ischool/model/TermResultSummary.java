package com.esoft.ischool.model;

import java.util.ArrayList;
import java.util.List;

public class TermResultSummary implements Comparable<TermResultSummary>{
	
	private String studentName;
	private Integer studentId;
	private Integer enrollmentId;
	private Integer rank;
	private Double moyenne=0.0;
	private byte[] picture;
	private List<TermResultDtl> termResultDtl = new ArrayList<TermResultDtl>();
	
	public void calculateMoyenne(){
		double maxMark=0;
		moyenne=0.0;
		for(TermResultDtl dtl:termResultDtl){
			moyenne+=dtl.getMoyenne();
			maxMark+=dtl.getMaxMark();
		}
		moyenne=moyenne*20/maxMark;
	}
	public void addDtl(TermResultDtl dtl){
		termResultDtl.add(dtl);
	}
	public List<TermResultDtl> getTermResultDtl() {
		return termResultDtl;
	}
	public void setTermResultDtl(List<TermResultDtl> termResultDtl) {
		this.termResultDtl = termResultDtl;
	}
	public Integer getEnrollmentId() {
		return enrollmentId;
	}
	public void setEnrollmentId(Integer enrollmentId) {
		this.enrollmentId = enrollmentId;
	}
	public Integer getStudentId() {
		return studentId;
	}
	public void setStudentId(Integer studentId) {
		this.studentId = studentId;
	}
	public String getStudentName() {
		return studentName;
	}
	public void setStudentName(String studentName) {
		this.studentName = studentName;
	}
	public Integer getRank() {
		return rank;
	}
	public void setRank(Integer rank) {
		this.rank = rank;
	}
	public Double getMoyenne() {
		return moyenne;
	}
	public void setMoyenne(Double moyenne) {
		this.moyenne = moyenne;
	}
	public byte[] getPicture() {
		return picture;
	}
	public void setPicture(byte[] picture) {
		this.picture = picture;
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((studentId == null) ? 0 : studentId.hashCode());
		return result;
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		TermResultSummary other = (TermResultSummary) obj;
		if (studentId == null) {
			if (other.studentId != null)
				return false;
		} else if (!studentId.equals(other.studentId))
			return false;
		return true;
	}

	public int compareTo(TermResultSummary other){
		
		if(other !=null ){
			return other.getMoyenne().compareTo(moyenne);
		}
		return 0;
	}
	
}
