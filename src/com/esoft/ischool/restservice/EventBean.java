package com.esoft.ischool.restservice;
import java.util.ArrayList;
import java.util.List;

import javax.faces.event.ActionEvent;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.esoft.ischool.model.BaseEntity;
import com.esoft.ischool.service.BaseService;
import com.esoft.ischool.util.MenuIdEnum;

@Component("eventBean")
@Scope("session")
public class EventBean extends BaseBean {

	@Autowired
	@Qualifier("baseService")
	private BaseService baseService;
	private Long rowCount;
	private List<BaseEntity> events = new ArrayList<BaseEntity>();
	private String selectedDate;
	
	public String validate() {
		return "succes";
	}
	
	public String findDateEvents(ActionEvent actionEvent) {
		System.out.println("This is the date: " + selectedDate );
		return "";
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

	
	public List<BaseEntity> getEvents() {
		return events;
	}


	public void setEvents(List<BaseEntity> events) {
		this.events = events;
	}


	public String getSelectedDate() {
		return selectedDate;
	}


	public void setSelectedDate(String selectedDate) {
		this.selectedDate = selectedDate;
	}


	public boolean isUserHasWriteAccess() {
		return isUserHasWriteAccess(MenuIdEnum.AUTRE.getValue());
	}
}
