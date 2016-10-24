package com.esoft.ischool.restservice;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.annotation.PostConstruct;

import org.apache.commons.beanutils.BeanComparator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.esoft.ischool.model.BaseEntity;
import com.esoft.ischool.model.Event;
import com.esoft.ischool.service.BaseService;

@Component("schoolEventBean")
@Scope("session")
public class SchoolEventBean extends BaseBean {

	@Autowired
	@Qualifier("baseService")
	private BaseService baseService;
	private Long rowCount;
	private List<BaseEntity> schoolEvents;
	private List<BaseEntity> futureSchoolEvents;
	private Event schoolEvent = new Event();

	private String selectedTab;

	public String getShowAll() {
		getAll();
		return "Success";
	}

	public String delete() {
		clearMessages();
		try {
			baseService.delete(getIdParameter(), Event.class);
			getAll();
			clear();
			setSuccessMessage(getResourceBundle().getString(
					"DELETE_SUCCESSFULLY"));
		} catch (Exception ex) {
			setErrorMessage(getResourceBundle().getString(
					"DELETE_UNSUCCESSFULL"));
		}

		return "Success";
	}

	@Override
	public String clear() {
		schoolEvent = new Event();

		return "Success";
	}

	public String insert() {
		clearMessages();
		Long id = schoolEvent.getId();
		try {
			if (id == null || id == 0) {
				baseService.save(schoolEvent, getCurrentUser());
			} else {
				baseService.update(schoolEvent, getCurrentUser());
			}
			setSuccessMessage(getResourceBundle().getString(
					"SAVED_SUCCESSFULLY"));
		} catch (Exception ex) {
			schoolEvent.setId(id);
			setErrorMessage(ex, "Cet evenement exist deja. ");
			ex.printStackTrace();
		}

		clear();
		getAll();
		return "Success";
	}

	public String edit() {
		clearMessages();
		schoolEvent = (Event) baseService
				.getById(Event.class, getIdParameter());
		selectedTab = "correspondanceDetails";
		return "Success";
	}

	public String getAll() {
		String link = (String) getSessionParameter("link");
		schoolEvents = baseService.loadAll(Event.class, getCurrentUser()
				.getSchool());
		if (link != null && link.equalsIgnoreCase("school")) {
			setRowCount(new Long(schoolEvents.size()));
		} else {
			BeanComparator beanComparator = new BeanComparator("beginDateTime");
			Collections.sort(schoolEvents, beanComparator);
			futureSchoolEvents = new ArrayList();
			for (BaseEntity entity : schoolEvents) {
				Event schoolEvent = (Event) entity;
				if (schoolEvent.getInFuture())
					futureSchoolEvents.add(entity);
			}
			schoolEvents = null;
		}
		return "success";
	}

	public boolean isUserHasWriteAccess() {
		return false;
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

	public String doNothing() {
		return "Success";
	}

	public List<BaseEntity> getSchoolEvents() {
		return schoolEvents;
	}

	public void setSchoolEvents(List<BaseEntity> schoolEvents) {
		this.schoolEvents = schoolEvents;
	}

	public Event getSchoolEvent() {
		return schoolEvent;
	}

	public void setSchoolEvent(Event schoolEvent) {
		this.schoolEvent = schoolEvent;
	}

	@Override
	public String getSelectedTab() {
		return selectedTab;
	}

	@Override
	public void setSelectedTab(String selectedTab) {
		this.selectedTab = selectedTab;
	}

	public List<BaseEntity> getFutureSchoolEvents() {
		return futureSchoolEvents;
	}

	public void setFutureSchoolEvents(List<BaseEntity> futureSchoolEvents) {
		this.futureSchoolEvents = futureSchoolEvents;
	}
}
