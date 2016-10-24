package com.esoft.ischool.restservice;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.esoft.ischool.model.BaseEntity;
import com.esoft.ischool.model.Term;
import com.esoft.ischool.model.TermGroup;
import com.esoft.ischool.service.BaseService;
import com.esoft.ischool.util.MenuIdEnum;

@Component("termGroupBean")
@Scope("session")
public class TermGroupBean extends BaseBean {

	@Autowired
	@Qualifier("baseService")
	private BaseService baseService;
	private Long rowCount;
	private List<BaseEntity> termGroups;
	private List<String> allTermGroups = new ArrayList<String>();
	private TermGroup termGroup = new TermGroup();

	
	public List<String> getAllTermGroups() {
		return allTermGroups;
	}

	public void setAllTermGroups(List<String> allTermGroups) {
		this.allTermGroups = allTermGroups;
	}

	public List<TermGroup> autoComplete(Object suggest) {
		String pref = (String) suggest;
		List<TermGroup> result = new ArrayList<TermGroup>();
		allTermGroups.clear();
		
		for (BaseEntity entity : termGroups) {
			TermGroup l = (TermGroup) entity;
			if ((l.getName() != null && l.getName().toLowerCase().indexOf(
					pref.toLowerCase()) == 0)
					|| "".equals(pref))
				result.add(l);
			
			allTermGroups.add(l.getName());
		}

		Collections.sort(allTermGroups);
		return result;
	}

	public String validate() {
		return "succes";
	}
	
	@Override
	public String clear() {
		termGroup = new TermGroup();
		return "Success";
	}

	public String delete() {
		clearMessages();
		try {
			baseService.delete(getIdParameter(), TermGroup.class);
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
		Long id = termGroup.getId();
		try {
			if (id == null || id == 0)
				baseService.save(termGroup, getCurrentUser());
			else
				baseService.update(termGroup, getCurrentUser());
			setSuccessMessage(getResourceBundle().getString("SAVED_SUCCESSFULLY"));
		} catch (Exception ex) {
			termGroup.setId(id);
			setErrorMessage(ex,"Ce group term existe deja");
		}

		
		clear();
		getAll();
		return "Success";
	}


	public String edit() {
		clearMessages();
		termGroup = (TermGroup) baseService.getById(TermGroup.class, getIdParameter());
		return "Success";
	}
	
	@PostConstruct
	private void getAll() {
		termGroups = baseService.loadAll(TermGroup.class,baseService.getDefaultSchool());
		setRowCount(new Long(termGroups.size()));
		allTermGroups = new ArrayList();
		for(BaseEntity entity : termGroups){
			TermGroup termGroup = (TermGroup) entity;
			allTermGroups.add(termGroup.getName());
		}
		
		Collections.sort(allTermGroups);
	}

	public boolean isUserHasWriteAccess() {
		return isUserHasWriteAccess(MenuIdEnum.TERM.getValue());
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

	public List<BaseEntity> getTermGroups() {
		return termGroups;
	}

	public void setTermGroups(List<BaseEntity> termGroups) {
		this.termGroups = termGroups;
	}

	public TermGroup getTermGroup() {
		return termGroup;
	}

	public void setTermGroup(TermGroup termGroup) {
		this.termGroup = termGroup;
	}
}
