package com.esoft.ischool.util;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.ConverterException;
import org.apache.commons.lang.StringUtils;
import org.springframework.context.ApplicationContext;

import com.esoft.ischool.security.model.User;
import com.esoft.ischool.service.BaseService;

public class UserConverter implements Converter{

	public Object getAsObject(FacesContext context, UIComponent component, String value) {
	    if (StringUtils.isEmpty(value)){ return null;}

	    User user = new User();

	    String [] userArray = StringUtils.split(value," ,()-");

	    Long id = new Long(userArray[0]);
	    String userName = userArray[1];
	    String firstName = userArray[2];
	    String lastName = userArray[3];
	    
	    user.setId(id);
	    user.setUserName(userName);
	    user.setFirstName(firstName);
	    user.setLastName(lastName);
	    
	    return user;
	}

	public String getAsString(FacesContext arg0, UIComponent arg1, Object value)
			throws ConverterException {
		// TODO Auto-generated method stub
		return value == null ? "" : value.toString();
	}	
	
	public BaseService getBaseService() {
		FacesContext context = FacesContext.getCurrentInstance();

		ApplicationContext ctx = org.springframework.web.jsf.FacesContextUtils
				.getWebApplicationContext(context);

		BaseService baseService = (BaseService) ctx.getBean("baseService");
		return baseService;
	}
}
