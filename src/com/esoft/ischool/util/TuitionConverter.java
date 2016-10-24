package com.esoft.ischool.util;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.ConverterException;

import org.apache.commons.lang.StringUtils;

import com.esoft.ischool.model.Student;
import com.esoft.ischool.model.Tuition;

public class TuitionConverter implements Converter{

	public Object getAsObject(FacesContext context, UIComponent component, String value) {
	    if (StringUtils.isEmpty(value)){ return null;}

	    Tuition tuition = new Tuition();

	    String [] tuitionArray = StringUtils.split(value,"|");

	    if(tuitionArray.length>=4){
	    	Long id = new Long(tuitionArray[3]);	    
	    	tuition.setId(id);
	    	tuition.setDescription(tuitionArray[1]);
	    	tuition.setAmount(new Double(tuitionArray[2]));
	    }

	    return tuition;
	}

	public String getAsString(FacesContext arg0, UIComponent arg1, Object value)
			throws ConverterException {
		// TODO Auto-generated method stub
		return value.toString();
	}	
}
