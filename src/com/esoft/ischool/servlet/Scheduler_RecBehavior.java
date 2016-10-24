package com.esoft.ischool.servlet;

/*
 * Copyright (c) 2009 - DHTMLX, All rights reserved
 */

import java.text.SimpleDateFormat;
import java.util.Date;

import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;

import com.dhtmlx.connector.ConnectorBehavior;
import com.dhtmlx.connector.ConnectorOperationException;
import com.dhtmlx.connector.DBDataWrapper;
import com.dhtmlx.connector.DataAction;
import com.dhtmlx.connector.LogManager;
import com.dhtmlx.connector.SchedulerConnector;
import com.esoft.ischool.security.model.User;

/**
 * The Class SchedulerRecBehavior.
 */
public class Scheduler_RecBehavior extends ConnectorBehavior {
	
	/** The connector. */
	SchedulerConnector connector;
	
	/**
	 * Instantiates a new scheduler rec behavior.
	 * 
	 * @param connector the connector
	 */
	public Scheduler_RecBehavior(SchedulerConnector connector){
		this.connector = connector;
	}
	
	/* (non-Javadoc)
	 * @see com.dhtmlx.connector.ConnectorBehavior#beforeProcessing(com.dhtmlx.connector.DataAction)
	 */
	@Override
	public void beforeProcessing(DataAction action) {
		String status = action.get_status();
		String type = action.get_value("rec_type");
		String pid = action.get_value("EVENT_PID");
		
		User user = (User)RequestContextHolder.currentRequestAttributes().getAttribute("user", RequestAttributes.SCOPE_SESSION);
		
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm");
		
		action.set_value("EVENT_PID", action.get_value("event_pid"));
		action.set_value("EVENT_LENGTH", action.get_value("event_length"));
		//action.set_value("REC_TYPE", action.get_value("rec_type"));
		action.set_value("DESCRIPTION", action.get_value("DESCRIPTION"));
		action.set_value("SCHOOL_ID", user.getSchool().getId() + "");
		action.set_value("MOD_BY", user.getId() + "");
		action.set_value("CREATE_DATE", formatter.format(new Date()));
		action.set_value("MOD_DATE", formatter.format(new Date()));
		action.set_value("END_TIME", action.get_value("END_TIME").replaceAll("9999", "2030"));
		
		if (pid == null)
			action.set_value("EVENT_PID", getNextEventPid());
		try {
			//when series changed or deleted we need to remove all linked events
			if ((status.equals("deleted") || status.equals("updated")) && !type.isEmpty()){
				((DBDataWrapper)connector.sql).query("DELETE FROM EVENTS_REC WHERE EVENT_PID='"+((DBDataWrapper)connector.sql).escape(action.get_id())+"'");
			}
			if (status.equals("deleted") && !pid.isEmpty() && !pid.equals("0")){
				((DBDataWrapper)connector.sql).query("UPDATE EVENTS_REC SET REC_TYPE='none' WHERE EVENT_ID='"+((DBDataWrapper)connector.sql).escape(action.get_id())+"'");
				action.success();
			}
		} catch (ConnectorOperationException e) {
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
		if (status.equals("inserted") && type.equals("none"))
			action.set_status("deleted");
		else
			super.afterProcessing(action);
	}
	
	private String getNextEventPid() {
		String nextEventPid = null;
		try {
			nextEventPid =  ((DBDataWrapper)connector.sql).query("SELECT max(EVENT_PID) + 1 FROM EVENT").get_last_id();		
		} catch (ConnectorOperationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return nextEventPid != null ? nextEventPid : "1";
	}
}
