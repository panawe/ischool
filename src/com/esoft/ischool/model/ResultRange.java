package com.esoft.ischool.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="RESULT_RANGE")
public class ResultRange extends BaseEntity{

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name="RESULT_RANGE_ID")
	private Long id;
	
	@Column(name="BEGIN_RANGE")
	private Long beginRange;
	
	@Column(name="END_RANGE")
	private Long endRange;
	
	public ResultRange(){}
	
	public ResultRange(ResultRange rr) {
		// TODO Auto-generated constructor stub
		this.beginRange=rr.getBeginRange();
		this.endRange=rr.getEndRange();
	}



	@Override
	public String toString() {
		return "ResultRange [beginRange=" + beginRange + ", endRange="
				+ endRange + ", resultRangeId=" + id
				+ ", getCreateDate()=" + getCreateDate() + ", getModDate()=" + getModDate()
				+ ", getModifiedBy()=" + getModifiedBy() + "]";
	}



	public Long getBeginRange() {
		return beginRange;
	}



	public void setBeginRange(Long beginRange) {
		this.beginRange = beginRange;
	}



	public void setId(Long id) {
		this.id = id;
	}



	public Long getEndRange() {
		return endRange;
	}



	public void setEndRange(Long endRange) {
		this.endRange = endRange;
	}



	@Override
	public Long getId() {
		// TODO Auto-generated method stub
		return null;
	}
}
