package com.esoft.ischool.util;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.ConverterException;
import org.apache.commons.lang.NumberUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.context.ApplicationContext;

import com.esoft.ischool.security.model.Menu;
import com.esoft.ischool.service.BaseService;

public class MenuConverter implements Converter{

	public Object getAsObject(FacesContext context, UIComponent component, String value) {
	    if (StringUtils.isEmpty(value)){ return null;}

	    Menu menu = new Menu();
	    Menu parentMenu = new Menu();
	    
	    String [] menuArray = StringUtils.split(value," ,()-");

	    String id = menuArray[0];
	    String name = menuArray[1];
	    String parentName = "";
	    
	    if (menuArray.length > 2)
	    	parentName = menuArray[2];
	    
	    if (NumberUtils.isNumber(id))
	    	menu.setId(new Long(id));
	    menu.setName(name);
	    parentMenu.setName(parentName);
	    menu.setMenuParent(parentMenu);
	    
	    return menu;
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
