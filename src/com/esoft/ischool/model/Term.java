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

@Entity
@Table(name="TERM")
public class Term extends BaseEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "TERM_ID")
	private Long id;
	
	//@NotNull
	@ManyToOne
	@JoinColumn(name = "TERM_GROUP_ID")
	private TermGroup termGroup;
	
	//@NotNull
	//@Length(max = 50)
	@Column(name = "NAME")
	private String name;
	

	//@Length(max = 100)
	@Column(name = "DESCRIPTION")
	private String description;


	//@Length(max = 1)
	@Column(name = "SHOW_FINAL_RANK")
	private String showFinalRank;
	
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public Term(){}
	public Term(Term term) {
		// TODO Auto-generated constructor stub
		this.name=term.getName();
	}

	public Term(Long id) {
		this.id = id;
	}

	@Override
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	public String getShowFinalRank() {
		return showFinalRank;
	}
	public void setShowFinalRank(String showFinalRank) {
		this.showFinalRank = showFinalRank;
	}
	public TermGroup getTermGroup() {
		return termGroup;
	}
	public void setTermGroup(TermGroup termGroup) {
		this.termGroup = termGroup;
	}
}
