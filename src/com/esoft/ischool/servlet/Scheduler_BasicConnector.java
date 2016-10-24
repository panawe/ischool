package com.esoft.ischool.servlet;

/*
 * Copyright (c) 2009 - DHTMLX, All rights reserved
 */
import java.sql.Connection;
import java.sql.SQLException;

import javax.servlet.ServletContext;
import javax.sql.DataSource;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.support.WebApplicationContextUtils;

import com.dhtmlx.connector.ConnectorServlet;
import com.dhtmlx.connector.SchedulerConnector;
import com.esoft.ischool.security.model.User;
import com.esoft.ischool.service.Scheduler_BasicBehavior;

/**
 * The Class SchedulerBasicConnector.
 */
public class Scheduler_BasicConnector extends ConnectorServlet {

	/* (non-Javadoc)
	 * @see com.dhtmlx.connector.ConnectorServlet#configure()
	 */
	@Override
	protected void configure() {
		Connection conn= getConnection();
		SchedulerConnector c = new SchedulerConnector(conn);
		c.event.attach(new Scheduler_BasicBehavior(c));
		User user = (User)RequestContextHolder.currentRequestAttributes().getAttribute("user", RequestAttributes.SCOPE_SESSION);
		
		//if (c.is_select_mode())
			c.render_sql("SELECT EVENT_ID, BEGIN_TIME,END_TIME,TITLE,DESCRIPTION FROM EVENT WHERE SCHOOL_ID = " + user.getSchool().getId(), "EVENT_ID", "BEGIN_TIME, END_TIME, TITLE, DESCRIPTION");
	//	else 
	//		c.render_table("event", "event_id", "begin_time,end_time,title,description", "", "");
			try {
				conn.commit();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	}

	private Connection getConnection() {
		Connection jdbcConnection = null;

		ServletContext context = getServletContext(); 
		WebApplicationContext applicationContext = WebApplicationContextUtils.getWebApplicationContext(context);   

		DataSource dataSource = (DataSource) applicationContext.getBean("dataSource");
		try {
			jdbcConnection = dataSource.getConnection();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return jdbcConnection;
	}
	
}
