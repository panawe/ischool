package com.esoft.ischool.restservice;

import java.util.ArrayList;
import java.util.List;
import javax.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.esoft.ischool.model.Alert;
import com.esoft.ischool.model.BaseEntity;
import com.esoft.ischool.service.BaseService;
import com.esoft.ischool.util.AlertType;
import com.esoft.ischool.util.MenuIdEnum;

@Component("alertBean")
@Scope("session")
public class AlertBean extends BaseBean {

	@Autowired
	@Qualifier("baseService")
	private BaseService baseService;
	
	private Long rowCount;
	private List<BaseEntity> alerts;
	private List<String> allAlerts = new ArrayList<String>();
	private Alert alert = new Alert();

	private String alertTypeName;
	private String selectedTab = "alertList";
	
	private boolean showNumberOfDaysBeforeDueDate;
	
	@Override
	public String getSelectedTab() {
		return selectedTab;
	}

	@Override
	public void setSelectedTab(String selectedTab) {
		this.selectedTab = selectedTab;
	}
	

	public String validate() {
		return "succes";
	}

	@Override
	public String clear() {
		alert = new Alert();
		alertTypeName = "";
		return "Success";
	}

	public String delete() {
		clearMessages();
		try {
			baseService.delete(getIdParameter(), Alert.class);
			getAll();
			clear();
			setSuccessMessage(getResourceBundle().getString("DELETE_SUCCESSFULLY"));
		} catch (Exception ex) {
			setErrorMessage(getResourceBundle().getString("DELETE_UNSUCCESSFULL"));
		}

		return "Success";
	}

	public String insert() {
		clearMessages();
		Long id = alert.getId();
		try {
			if (id == null || id == 0) {
				alert.setEmailReceiver(1);
				alert.setStatus("1");
				baseService.save(alert,getCurrentUser());
			}
			else
				baseService.update(alert,getCurrentUser());
			setSuccessMessage(getResourceBundle().getString("SAVED_SUCCESSFULLY"));
			clear();
			getAll();
		} catch (Exception ex) {
			alert.setId(id);
			setErrorMessage(ex, "Cet alert existe deja");
		}


		return "Success";
	}

	public String edit() {
		clearMessages();
		alert = (Alert) baseService.getById(Alert.class, getIdParameter());
		selectedTab = "alertDetail";
		alertTypeChanged();
		return "Success";
	}

	public String alertTypeChanged() {
		if (AlertType.PRODUCT_CONSUMER_OVER_DUE.getValue().equals(alert.getAlertTypeCode()))
			showNumberOfDaysBeforeDueDate = true;
		else if (AlertType.TUITION_PAYMENT_OVER_DUE.getValue().equals(alert.getAlertTypeCode()))
			showNumberOfDaysBeforeDueDate = true;
		else
			showNumberOfDaysBeforeDueDate = false;
		return "Success";
	}
	
	@PostConstruct
	private void getAll() {
		alerts = baseService.loadAll(Alert.class,getCurrentUser().getSchool());
		setRowCount(new Long(alerts.size()));

	}

	public boolean isUserHasWriteAccess() {
		return isUserHasWriteAccess(MenuIdEnum.ALERTS.getValue());
	}

	public Long getRowCount() {
		return rowCount;
	}

	public void setRowCount(Long rowCount) {
		this.rowCount = rowCount;
	}

	public BaseService getBaseService() {
		return baseService;
	}

	public void setBaseService(BaseService baseService) {
		this.baseService = baseService;
	}

	public List<BaseEntity> getAlerts() {
		return alerts;
	}

	public void setAlerts(List<BaseEntity> alerts) {
		this.alerts = alerts;
	}

	public List<String> getAllAlerts() {
		return allAlerts;
	}

	public void setAllAlerts(List<String> allAlerts) {
		this.allAlerts = allAlerts;
	}

	public Alert getAlert() {
		return alert;
	}

	public void setAlert(Alert alert) {
		this.alert = alert;
	}

	public String getAlertTypeName() {
		return alertTypeName;
	}

	public void setAlertTypeName(String alertTypeName) {
		this.alertTypeName = alertTypeName;
	}

	public boolean getShowNumberOfDaysBeforeDueDate() {
		return showNumberOfDaysBeforeDueDate;
	}

	public void setShowNumberOfDaysBeforeDueDate(boolean showNumberOfDaysBeforeDueDate) {
		this.showNumberOfDaysBeforeDueDate = showNumberOfDaysBeforeDueDate;
	}
}
