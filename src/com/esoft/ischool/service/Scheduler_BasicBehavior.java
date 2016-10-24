package com.esoft.ischool.service;

/*
 * Copyright (c) 2009 - DHTMLX, All rights reserved
 */

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;

import com.dhtmlx.connector.ConnectorBehavior;
import com.dhtmlx.connector.DBDataWrapper;
import com.dhtmlx.connector.DataAction;
import com.dhtmlx.connector.LogManager;
import com.dhtmlx.connector.SchedulerConnector;
import com.esoft.ischool.security.model.User;
import com.esoft.ischool.util.AppContext;

/**
 * The Class SchedulerRecBehavior.
 */
@Service("schedulerBasicBehavior")
public class Scheduler_BasicBehavior extends ConnectorBehavior  {
	
	/** The connector. */
	SchedulerConnector connector;
	
	@Autowired
	@Qualifier("baseService")
	private BaseService baseService;
	
	public Scheduler_BasicBehavior(){
	}
	
	/**
	 * Instantiates a new scheduler rec behavior.
	 * 
	 * @param connector the connector
	 */
	public Scheduler_BasicBehavior(SchedulerConnector connector){
		this.connector = connector;
	}
	
	/* (non-Javadoc)
	 * @see com.dhtmlx.connector.ConnectorBehavior#beforeProcessing(com.dhtmlx.connector.DataAction)
	 */
	@Override
	public void beforeProcessing(DataAction action) {
		String status = action.get_status();
		String description = action.get_value("description");
		String title = action.get_value("TITLE");
		String beginDateTime = action.get_value("BEGIN_TIME");
		String endDateTime = action.get_value("END_TIME");
		String id = action.get_value("EVENT_ID");
		Date beginDate = null;
		Date endDate = null;
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm");
		try {
			beginDate = (Date)formatter.parseObject(beginDateTime);
			endDate = (Date)formatter.parseObject(endDateTime);
		} catch (ParseException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}  

		  
		com.esoft.ischool.model.Event myEvent = new com.esoft.ischool.model.Event();
		myEvent.setTitle(title);
		myEvent.setDescription(description);
		myEvent.setBeginDateTime(beginDate);
		myEvent.setEndDateTime(endDate);
		myEvent.setId(new Long(id));
		
		try {
			ApplicationContext ctx = AppContext.getApplicationContext();   
			baseService = (BaseService) ctx.getBean("baseService"); 
			User user = (User)RequestContextHolder.currentRequestAttributes().getAttribute("user", RequestAttributes.SCOPE_SESSION); 

			//when series changed or deleted we need to remove all linked events
			if (status.equals("deleted")){
				//baseService.delete(new Long(id), com.esoft.ischool.model.Event.class);
				//((DBDataWrapper)connector.sql).query("DELETE FROM EVENT WHERE EVENT_ID = " + id);
			}
			else if (status.equals("updated")){				
				baseService.update(myEvent, user);
				action.success();
			}
			else if (status.equals("inserted")) {
				baseService.save(myEvent, user);
				action.success();
			}
		} catch (Exception e) {
			LogManager.getInstance().log("Reccuring event error \n"+e.getMessage());
		}
		
		super.beforeProcessing(action);
	}

	/* (non-Javadoc)
	 * @see com.dhtmlx.connector.ConnectorBehavior#afterProcessing(com.dhtmlx.connector.DataAction)
	 */
	@Override
	public void afterProcessing(DataAction action) {
		String status = action.get_status();
		String type = action.get_value("REC_TYPE");
		if (status.equals("inserted") && type != null && type.equals("none"))
			action.set_status("deleted");
		else
			super.afterProcessing(action);
	}
	
	public BaseService getBaseService() {return baseService;}
	public void setBaseService(BaseService baseService) {this.baseService = baseService;}

}
