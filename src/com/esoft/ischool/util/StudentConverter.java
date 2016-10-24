package com.esoft.ischool.util;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.ConverterException;

import org.apache.commons.lang.StringUtils;

import com.esoft.ischool.model.Parent;
import com.esoft.ischool.model.Student;

public class StudentConverter implements Converter{

	public Object getAsObject(FacesContext context, UIComponent component, String value) {
	    if (StringUtils.isEmpty(value)){ return null;}

	    Student student = new Student();

	    String [] studentArray = StringUtils.split(value,"|");

	    String matricule = studentArray[0];
	    String firstName = studentArray[1];
	    for(int i=2; i<studentArray.length-4; i++){
	    	firstName= firstName+studentArray[i];
	    }
	    
	    String lastName = studentArray[studentArray.length-3];
	    String parentType = studentArray[studentArray.length-2];
	    Long id = new Long(studentArray[studentArray.length-1]);

	    student.setMatricule(matricule);
	    student.setFirstName(firstName);
	    student.setLastName(lastName);
	    student.setParentType(parentType);
	    student.setId(id);
	    return student;
	}

	public String getAsString(FacesContext arg0, UIComponent arg1, Object value)
			throws ConverterException {
		// TODO Auto-generated method stub
		return value.toString();
	}	
}
