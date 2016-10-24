package com.esoft.ischool.restservice;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.esoft.ischool.model.BaseEntity;
import com.esoft.ischool.model.Configuration;
import com.esoft.ischool.service.BaseService;
import com.esoft.ischool.util.MenuIdEnum;
import com.esoft.ischool.util.Utils;

@Component("configurationBean")
@Scope("session")
public class ConfigurationBean extends BaseBean {

	@Autowired
	@Qualifier("baseService")
	private BaseService baseService;
	private List<BaseEntity> configurations;
	private Configuration configuration = new Configuration();
	private Long rowCount;
	private static Map<String, String> configMap = new HashMap<String, String>();

	public void setRowCount(Long rowCount) {
		this.rowCount = rowCount;
	}

	public List<Configuration> autoComplete(Object suggest) {
		String pref = (String) suggest;
		List<Configuration> result = new ArrayList<Configuration>();

		for (BaseEntity entity : configurations) {
			Configuration p = (Configuration) entity;
			if ((p.getName() != null && p.getName().toLowerCase()
					.indexOf(pref.toLowerCase()) == 0)
					|| "".equals(pref))
				result.add(p);
		}

		return result;
	}

	@Override
	public String clear() {
		configuration = new Configuration();
		return "Success";
	}

	public String delete() {
		clearMessages();
		try {
			baseService.delete(getIdParameter(), Configuration.class);
			loadAll();
			clear();
			setSuccessMessage(getResourceBundle().getString(
					"DELETE_SUCCESSFULLY"));
			clearConfigMap();
		} catch (Exception ex) {
			setErrorMessage(getResourceBundle().getString(
					"DELETE_UNSUCCESSFULL"));
		}

		return "Success";
	}

	public String edit() {
		configuration = (Configuration) baseService.getById(
				Configuration.class, getIdParameter());
		return "Success";
	}

	public BaseService getBaseService() {
		return baseService;
	}

	public Configuration getConfiguration() {
		return configuration;
	}

	public List<BaseEntity> getConfigurations() {
		return configurations;
	}

	public Long getRowCount() {
		return rowCount;
	}

	public String insert() {
		clearMessages();
		Long originalNum = configuration.getId();
		try {
			if (configuration.getId() == null || configuration.getId() == 0)
				baseService.save(configuration, getCurrentUser());
			else
				baseService.update(configuration, getCurrentUser());

			setSuccessMessage(getResourceBundle().getString(
					"SAVED_SUCCESSFULLY"));
			clearConfigMap();
		} catch (Exception ex) {
			configuration.setId(originalNum);
			setErrorMessage(ex, "Cette configuration existe deja");
			ex.printStackTrace();
		}

		clear();
		loadAll();

		return "Success";
	}

	public void setBaseService(BaseService baseService) {
		this.baseService = baseService;
	}

	@PostConstruct
	private void loadAll() {
		configurations = baseService.loadAll(Configuration.class,
				getCurrentUser().getSchool());
		rowCount = new Long(configurations.size());
		for (BaseEntity entity : configurations) {
			Configuration p = (Configuration) entity;
			Utils.configMap = new HashMap<String, String>();
			Utils.configMap.put(p.getName(), p.getValue());
		}

	}

	public void setConfiguration(Configuration configuration) {
		this.configuration = configuration;
	}

	public void setConfigurations(List<BaseEntity> configurations) {
		this.configurations = configurations;
	}

	public String getConfigMapValue(String key) {
		return configMap.get(key);
	}

	public String getDateFormat() {
		for (BaseEntity entity : configurations) {
			if (((Configuration) entity).getName().equalsIgnoreCase(
					"DATE_FORMAT")) {
				return ((Configuration) entity).getValue();
			}
		}
		return null;
	}

	public String getDateTimeFormat() {
		for (BaseEntity entity : configurations) {
			if (((Configuration) entity).getName().equalsIgnoreCase(
					"DATE_TIME_FORMAT")) {
				return ((Configuration) entity).getValue();
			}
		}
		return null;
	}

	public String getMoneyFormat() {
		for (BaseEntity entity : configurations) {
			if (((Configuration) entity).getName().equalsIgnoreCase(
					"MONEY_FORMAT")) {
				return ((Configuration) entity).getValue();
			}
		}
		return null;
	}

	public String getMarkFormat() {
		for (BaseEntity entity : configurations) {
			if (((Configuration) entity).getName().equalsIgnoreCase(
					"MARK_FORMAT")) {
				return ((Configuration) entity).getValue();
			}
		}
		return null;
	}

	public String getUseTermGroup() {
		for (BaseEntity entity : configurations) {
			if (((Configuration) entity).getName().equalsIgnoreCase(
					"USE_TERM_GROUP")) {
				return ((Configuration) entity).getValue();
			}
		}
		return null;
	}

	public String getMailServerPassword() {

		for (BaseEntity entity : configurations) {
			if (((Configuration) entity).getName().equalsIgnoreCase(
					"SCHOOL_MAIL_SERVER_PASSWORD")) {
				return ((Configuration) entity).getValue();
			}
		}
		return null;
	}

	public String getMailSMTP() {
		for (BaseEntity entity : configurations) {
			if (((Configuration) entity).getName().equalsIgnoreCase(
					"MAIL_SERVER")) {
				return ((Configuration) entity).getValue();
			}
		}
		return null;
	}

	public String getMailServerUser() {

		for (BaseEntity entity : configurations) {
			if (((Configuration) entity).getName().equalsIgnoreCase(
					"SCHOOL_MAIL_SERVER_USER")) {
				return ((Configuration) entity).getValue();
			}
		}
		return null;
	}

	public String getSenderMail() {

		for (BaseEntity entity : configurations) {
			if (((Configuration) entity).getName().equalsIgnoreCase(
					"SCHOOL_EMAIL")) {
				return ((Configuration) entity).getValue();
			}
		}
		return null;
	}

	public boolean isUserHasWriteAccess() {
		return isUserHasWriteAccess(MenuIdEnum.AUTRE.getValue());
	}

}
