package com.esoft.ischool.restservice;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.esoft.ischool.model.BaseEntity;
import com.esoft.ischool.model.Country;
import com.esoft.ischool.model.Term;
import com.esoft.ischool.model.TermGroup;
import com.esoft.ischool.service.BaseService;
import com.esoft.ischool.util.MenuIdEnum;

@Component("termBean")
@Scope("session")
public class TermBean extends BaseBean {

	@Autowired
	@Qualifier("baseService")
	private BaseService baseService;
	private Long rowCount;
	private List<BaseEntity> terms;
	private List<String> allTerms = new ArrayList<String>();
	private String termGroupName;
	
	public List<String> getAllTerms() {
		return allTerms;
	}

	public void setAllTerms(List<String> allTerms) {
		this.allTerms = allTerms;
	}

	private Term term = new Term();

	public List<Term> autoComplete(Object suggest) {
		String pref = (String) suggest;
		List<Term> result = new ArrayList<Term>();
		allTerms.clear();
		
		for (BaseEntity entity : terms) {
			Term l = (Term) entity;
			if ((l.getName() != null && l.getName().toLowerCase().indexOf(
					pref.toLowerCase()) == 0)
					|| "".equals(pref))
				result.add(l);
			
			allTerms.add(l.getName());
		}

		return result;
	}

	public String validate() {
		return "succes";
	}
	
	@Override
	public String clear() {
		term = new Term();
		termGroupName = "";
		return "Success";
	}

	public String delete() {
		clearMessages();
		try {
			baseService.delete(getIdParameter(), Term.class);
			getAll();
			clear();
			setSuccessMessage(getResourceBundle().getString("DELETE_SUCCESSFULLY"));
		} catch (Exception ex) {
			setErrorMessage(getResourceBundle().getString("DELETE_UNSUCCESSFULL"));
		}


		return "Success";
	}
	
	public String insert() {
		clearMessages();
		Long id = term.getId();
		TermGroup termGroup = (TermGroup) baseService.findByColumn(TermGroup.class, "name", termGroupName);
		term.setTermGroup(termGroup);
		try {
			if (id == null || id == 0)
				baseService.save(term,getCurrentUser());
			else
				baseService.update(term,getCurrentUser());
			setSuccessMessage(getResourceBundle().getString("SAVED_SUCCESSFULLY"));
		} catch (Exception ex) {
			term.setId(id);
			setErrorMessage(ex,"Ce pays existe deja");
		}

		
		clear();
		getAll();
		return "Success";
	}


	public String edit() {
		clearMessages();
		term = (Term) baseService.getById(Term.class, getIdParameter());
		if (term.getTermGroup() != null)
			termGroupName = term.getTermGroup().getName();
		return "Success";
	}
	
	@PostConstruct
	private void getAll() {
		terms = baseService.loadAll(Term.class,baseService.getDefaultSchool());
		setRowCount(new Long(terms.size()));
		for(BaseEntity entity:terms){
			Term term = (Term) entity;
			allTerms.add(term.getName());
		}

	}

	public boolean isUserHasWriteAccess() {
		return isUserHasWriteAccess(MenuIdEnum.AUTRE.getValue());
	}

	public BaseService getBaseService() {
		return baseService;
	}

	public void setBaseService(BaseService baseService) {
		this.baseService = baseService;
	}

	public Long getRowCount() {
		return rowCount;
	}

	public void setRowCount(Long rowCount) {
		this.rowCount = rowCount;
	}

	public List<BaseEntity> getTerms() {
		return terms;
	}

	public void setTerms(List<BaseEntity> terms) {
		this.terms = terms;
	}

	public Term getTerm() {
		return term;
	}

	public void setTerm(Term term) {
		this.term = term;
	}

	public String getTermGroupName() {
		return termGroupName;
	}

	public void setTermGroupName(String termGroupName) {
		this.termGroupName = termGroupName;
	}

}
