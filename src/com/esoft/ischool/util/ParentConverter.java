package com.esoft.ischool.util;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.ConverterException;

import org.apache.commons.lang.StringUtils;

import com.esoft.ischool.model.Parent;
import com.esoft.ischool.model.Student;

public class ParentConverter implements Converter{

	public Object getAsObject(FacesContext context, UIComponent component, String value) {
	    if (StringUtils.isEmpty(value)){ return null;}
	    Parent parent = new Parent();

	    String [] parentArray = StringUtils.split(value,"|");

	    String firstName = parentArray[0];
	    for(int i=1; i < parentArray.length-6; i++){
	    	firstName= firstName + parentArray[i];
	    }
	    
	    String lastName = parentArray[parentArray.length-5];
	    String profession = parentArray[parentArray.length-4];
	    String workPlace = parentArray[parentArray.length-3];
	    String parentType = parentArray[parentArray.length-2];
	    Long id = new Long(parentArray[parentArray.length-1]);

	    parent.setFirstName(firstName);
	    parent.setLastName(lastName);
	    parent.setProfession(profession);
	    parent.setWorkPlace(workPlace);
	    parent.setParentType(parentType);
	    parent.setId(id);
	    return parent;
	    
	}

	public String getAsString(FacesContext arg0, UIComponent arg1, Object value)
			throws ConverterException {
		// TODO Auto-generated method stub
		return value.toString();
	}	
}
