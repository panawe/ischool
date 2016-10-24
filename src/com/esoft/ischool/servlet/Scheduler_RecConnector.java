/*
 * Copyright (c) 2009 - DHTMLX, All rights reserved
 */
package com.esoft.ischool.servlet;
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



// TODO: Auto-generated Javadoc
/**
 * The Class SchedulerRecConnector.
 */
public class Scheduler_RecConnector extends ConnectorServlet {

	/* (non-Javadoc)
	 * @see com.dhtmlx.connector.ConnectorServlet#configure()
	 */
	@Override
	protected void configure() {
		Connection conn= getConnection();
		
		SchedulerConnector c = new SchedulerConnector(conn);
		c.event.attach(new Scheduler_RecBehavior(c));

		User user = (User)RequestContextHolder.currentRequestAttributes().getAttribute("user", RequestAttributes.SCOPE_SESSION);
		c.render_sql("SELECT EVENT_ID, BEGIN_TIME,END_TIME,TITLE,DESCRIPTION,REC_TYPE,EVENT_PID,EVENT_LENGTH,SCHOOL_ID,MOD_DATE,MOD_BY,CREATE_DATE FROM EVENT WHERE SCHOOL_ID = " + user.getSchool().getId(), "EVENT_ID", "BEGIN_TIME,END_TIME,TITLE,DESCRIPTION,REC_TYPE,EVENT_PID,EVENT_LENGTH,SCHOOL_ID,MOD_DATE,MOD_BY,CREATE_DATE");

		try {
			conn.commit();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		finally {
			try {
				conn.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
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
