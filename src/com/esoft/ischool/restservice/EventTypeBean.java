package com.esoft.ischool.restservice;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.esoft.ischool.model.BaseEntity;
import com.esoft.ischool.model.EventType;
import com.esoft.ischool.service.BaseService;
import com.esoft.ischool.util.MenuIdEnum;

@Component("eventTypeBean")
@Scope("session")
public class EventTypeBean extends BaseBean {

	@Autowired
	@Qualifier("baseService")
	private BaseService baseService;
	private Long rowCount;
	private List<BaseEntity> eventTypes;
	private List<String> allEventTypes = new ArrayList<String>();
	private EventType eventType = new EventType();

	public List<EventType> autoComplete(Object suggest) {
		String pref = (String) suggest;
		List<EventType> result = new ArrayList<EventType>();
		allEventTypes.clear();
		
		for (BaseEntity entity : eventTypes) {
			EventType d = (EventType) entity;
			if ((d.getName() != null && d.getName().toLowerCase().indexOf(
					pref.toLowerCase()) == 0)
					|| "".equals(pref))
				result.add(d);
			
			allEventTypes.add(d.getName());
		}

		return result;
	}

	public String validate() {
		return "succes";
	}
	
	@Override
	public String clear() {
		eventType = new EventType();
		return "Success";
	}

	public String delete() {
		clearMessages();
		try {
			baseService.delete(getIdParameter(), EventType.class);
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
		Long id = eventType.getId();
		try {
			if (id == null || id == 0)
				baseService.save(eventType,getCurrentUser());
			else
				baseService.update(eventType,getCurrentUser());
			setSuccessMessage(getResourceBundle().getString("SAVED_SUCCESSFULLY"));
		} catch (Exception ex) {
			eventType.setId(id);
			setErrorMessage(ex,"Ce pays existe deja");
		}

		
		clear();
		getAll();
		return "Success";
	}


	public String edit() {
		clearMessages();
		eventType = (EventType) baseService.getById(EventType.class, getIdParameter());
		return "Success";
	}
	
	@PostConstruct
	private void getAll() {
		eventTypes = baseService.loadAll(EventType.class,baseService.getDefaultSchool());
		setRowCount(new Long(eventTypes.size()));
		allEventTypes.clear();
		for(BaseEntity entity : eventTypes){
			EventType eventType = (EventType) entity;
			allEventTypes.add(eventType.getName());
		}

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

	public List<BaseEntity> getEventTypes() {
		return eventTypes;
	}

	public void setEventTypes(List<BaseEntity> eventTypes) {
		this.eventTypes = eventTypes;
	}
	

	public List<String> getAllEventTypes() {
		return allEventTypes;
	}

	public void setAllEventTypes(List<String> allEventTypes) {
		this.allEventTypes = allEventTypes;
	}

	public EventType getEventType() {
		return eventType;
	}

	public void setEventType(EventType eventType) {
		this.eventType = eventType;
	}
	
	public boolean isUserHasWriteAccess() {
		return isUserHasWriteAccess(MenuIdEnum.AUTRE.getValue());
	}

}
