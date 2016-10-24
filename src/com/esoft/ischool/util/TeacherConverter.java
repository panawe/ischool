package com.esoft.ischool.util;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.ConverterException;

import org.apache.commons.lang.StringUtils;

import com.esoft.ischool.model.Teacher;

public class TeacherConverter implements Converter{

	public Object getAsObject(FacesContext context, UIComponent component, String value) {
	    if (StringUtils.isEmpty(value)){ return null;}

	    Teacher teacher = new Teacher();

	    String [] teacherArray = StringUtils.split(value," ,()-");

	    String matricule = teacherArray[0];
	    String firstName = teacherArray[1];
	    for(int i=2; i<teacherArray.length-3; i++){
	    	firstName= firstName+teacherArray[i];
	    }
    
	    String lastName = teacherArray[teacherArray.length-2];
	    Long id = new Long(teacherArray[teacherArray.length-1]);
	    
	    teacher.setMatricule(matricule);
	    teacher.setFirstName(firstName);
	    teacher.setLastName(lastName);
	    teacher.setId(id);
	    return teacher;
	}

	public String getAsString(FacesContext arg0, UIComponent arg1, Object value)
			throws ConverterException {
		// TODO Auto-generated method stub
		return value.toString();
	}	
}
