package com.esoft.ischool.job;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.esoft.ischool.restservice.BaseBean;
import com.esoft.ischool.model.BaseEntity;
import com.esoft.ischool.model.Configuration;
import com.esoft.ischool.model.School;
import com.esoft.ischool.model.Student;
import com.esoft.ischool.model.Teacher;
import com.esoft.ischool.security.model.User;
import com.esoft.ischool.service.BaseService;
import com.esoft.ischool.util.SimpleMail;

@Component("config")
@Scope("singleton")
public class Config extends BaseBean{

	private Map<String, String> configMap;
	private Map<String, List<Configuration>> configurationListMap;
	
	@Autowired
	@Qualifier("baseService")
	private BaseService baseService;

	public Map<String, String> getConfigMap() {
		if (configMap == null) {
			configMap = new ConcurrentHashMap<String, String>();
			List<BaseEntity> configurations = baseService.loadAll(Configuration.class,getCurrentUser().getSchool());
			for (BaseEntity be : configurations) {
				configMap.put(((Configuration) be).getName(),
						((Configuration) be).getValue());
			}
		}
		
		return configMap;
	}
	
	public Map<String, String> getConfigMap(School school) {
		if (configMap == null) {
			configMap = new ConcurrentHashMap<String, String>();
			List<BaseEntity> configurations = baseService.loadAll(Configuration.class, school);
			for (BaseEntity be : configurations) {
				configMap.put(((Configuration) be).getName(),
						((Configuration) be).getValue());
			}
		}
		
		return configMap;
	}
	
	public BaseService getBaseService() {return baseService;}
	public void setBaseService(BaseService baseService) {this.baseService = baseService;}

	public void setConfigMap(Map<String, String> configMap) {
		this.configMap = configMap;
	}

	public String getConfigPropertyValue(String propertyName) {
		if (configMap == null)
			getConfigMap();
		return configMap.get(propertyName);
	}
	
	public void clearConfigMap() {
		configMap = null;
		configurationListMap = null;
	}
	
	public void sendMail(List<Student> selectedStudents,
			List<Teacher> selectedTeachers, String subject, String message) {
		StringBuffer sb = new StringBuffer();
		for (Student student : selectedStudents) {
			setStudentEmail(student, sb);
		}
		for (Teacher teacher : selectedTeachers) {
			setTeacherEmail(teacher, sb);
		}

		try {
			if (sb.length() > 0) {
				String to = sb.toString();
				if (configMap != null) {
					SimpleMail.sendMail(subject, message, configMap.get("SCHOOL_SENDER_EMAIL"), to.substring(0, to
									.length() - 1), configMap.get("SCHOOL_SMTP_SERVER"), configMap.get("SCHOOL_MAIL_SERVER_USER"), 
									configMap.get("SCHOOL_MAIL_SERVER_PASSWORD"));
				}
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
	
	public void sendMail(String emailAddresses, String subject, String message, School school) {

		try {
			if (emailAddresses.length() > 0) {
				String to = emailAddresses.toString();
				if (getConfigMap(school) != null) {
					SimpleMail.sendMail(subject, message, configMap.get("SCHOOL_SENDER_EMAIL"), to, configMap.get("SCHOOL_SMTP_SERVER"), configMap.get("SCHOOL_MAIL_SERVER_USER"), 
									configMap.get("SCHOOL_MAIL_SERVER_PASSWORD"));
				}
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public void setStudentEmail(Student st, StringBuffer sb) {
		Student student = (Student) baseService.getById(Student.class, st
				.getId());

		if (student.getEmail() != null && !student.getEmail().equals(""))
			sb.append(student.getEmail());
	}

	private void setTeacherEmail(Teacher teacher, StringBuffer sb) {
		Teacher t = (Teacher) baseService.getById(Teacher.class, teacher
				.getId());

		if (t.getEmail() != null && !t.getEmail().equals("")) {
			sb.append(t.getEmail() + ",");
		}
	}
	
	public void setUserEmail(User user, StringBuffer sb) {
		User u = (User) baseService.getById(User.class, user
				.getId());

		if (u.getEmail() != null && !u.getEmail().equals("")) {
			sb.append(u.getEmail() + ",");
		}
	}
	
	public List<Configuration> getConfigurationListByGroup(String groupName) {
		if (configurationListMap == null) 
			configurationListMap = new ConcurrentHashMap<String, List<Configuration>>();
		
		if (configurationListMap.get(groupName) == null) {
			configurationListMap.put(groupName, baseService.getConfigurationListByGroup(Configuration.class, groupName, getCurrentUser()==null?baseService.getDefaultSchool():getCurrentUser().getSchool()));
		}
		
		return configurationListMap.get(groupName);
	}
	
	
	
	public Map<String, List<Configuration>> getConfigurationListMap() {
		return configurationListMap;
	}

	public void setConfigurationListMap(
			Map<String, List<Configuration>> configurationListMap) {
		this.configurationListMap = configurationListMap;
	}

	public String getConfigurationByGroupAndKey(String groupName, String itemKey) {
		String value = "";
		for (Configuration configuration : getConfigurationListByGroup(groupName)) {
			if (itemKey.equalsIgnoreCase(configuration.getValue()))
				value = configuration.getName();
		}
		return value;
	}
	
	public List<Configuration> getConfigurationListByGroup(String groupName, BaseService baseService, School school) {
		if (configurationListMap == null) 
			configurationListMap = new ConcurrentHashMap<String, List<Configuration>>();
		
		if (configurationListMap.get(groupName) == null) {
			configurationListMap.put(groupName, baseService.getConfigurationListByGroup(Configuration.class, groupName, school));
		}
		
		return configurationListMap.get(groupName);
	}
	
	public String getConfigurationByGroupAndName(String groupName, String name) {
		String value = "";
		for (Configuration configuration : getConfigurationListByGroup(groupName)) {
			if (name.equalsIgnoreCase(configuration.getName()))
				value = configuration.getValue();
		}
		return value;
	}
	
	public String getConfigurationByGroupAndKey(String groupName, String itemKey, BaseService baseService, School school) {
		String name = "";
		for (Configuration configuration : getConfigurationListByGroup(groupName, baseService, school	)) {
			if (itemKey.equalsIgnoreCase(configuration.getValue()))
				name= configuration.getName();
		}
		return name;
	}
	
}
