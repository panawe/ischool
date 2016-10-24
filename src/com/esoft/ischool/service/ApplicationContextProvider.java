package com.esoft.ischool.service;   

import org.springframework.beans.BeansException;   
import org.springframework.context.ApplicationContext;   
import org.springframework.context.ApplicationContextAware;   
import org.springframework.stereotype.Component;
import com.esoft.ischool.util.AppContext;

/** * This class provides an application-wide access to the  
 *  * Spring ApplicationContext! The ApplicationContext is  
 *  * injected in a static method of the class "AppContext".  
 *  * Use AppContext.getApplicationContext() to get access  
 *  * to all Spring Beans.  
 *  * @author Siegfried Bolz  
 *  */  
@Component("applicationContextProvider")
public class ApplicationContextProvider implements ApplicationContextAware {   
	public void setApplicationContext(ApplicationContext ctx) throws BeansException {   
		 // Wiring the ApplicationContext into a static method   
		 AppContext.setApplicationContext(ctx);   // .EOF  
	}
}
 