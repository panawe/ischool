package com.esoft.ischool.restservice;

import java.io.IOException;
import java.io.OutputStream;
import java.sql.Connection;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import javax.faces.component.html.HtmlInputText;
import javax.faces.component.html.HtmlSelectManyListbox;
import javax.faces.component.html.HtmlSelectOneMenu;
import javax.faces.context.FacesContext;
import javax.faces.model.SelectItem;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.sql.DataSource;

import org.apache.commons.lang.StringUtils;
import org.richfaces.component.html.HtmlCalendar;
import org.richfaces.component.html.HtmlTab;
import org.richfaces.event.UploadEvent;
import org.richfaces.model.UploadItem;
import org.springframework.context.ApplicationContext;

import com.esoft.ischool.job.Config;
import com.esoft.ischool.model.Configuration;
import com.esoft.ischool.model.Search;
import com.esoft.ischool.security.model.Menu;
import com.esoft.ischool.security.model.User;
import com.esoft.ischool.util.Constants;

public class BaseBean implements Constants {

	private String errorMessage;
	private String successMessage;
	protected static String generalErrorMessage = "Le system ne peut pas accomplir votre operation actuellement."
			+ " Contacter l'administrateur pour plus d'information.";
	private String link;
	protected int rowCount;
	protected Config config;
	private Search search;
	private byte[] picture;
	private Double random;
    private int uploadsAvailable = 1;
    private boolean autoUpload = true;
    private boolean useFlash = true;
    private boolean canModify;
    private ResourceBundle resourceBundle;

	public int getUploadsAvailable() {
		return uploadsAvailable;
	}

	public void setUploadsAvailable(int uploadsAvailable) {
		this.uploadsAvailable = uploadsAvailable;
	}

	public boolean isAutoUpload() {
		return autoUpload;
	}

	public void setAutoUpload(boolean autoUpload) {
		this.autoUpload = autoUpload;
	}

	public boolean isUseFlash() {
		return useFlash;
	}

	public void setUseFlash(boolean useFlash) {
		this.useFlash = useFlash;
	}

	public Double getRandom() {
		return Math.random();
	}

	public void setRandom(Double random) {
		this.random = random;
	}
	
    public void listener(UploadEvent event) throws Exception{
        UploadItem item = event.getUploadItem();
        picture = item.getData();
    }  
	public void paint(OutputStream stream, Object object)
			throws IOException {
		if (picture != null) {
			stream.write(picture);
		} else {
			stream.write(new byte[] {});
		}
	}

	public String doNothing() {
		return "";
	}
	
	public byte[] getPicture() {
		return picture;
	}

	public void setPicture(byte[] picture) {
		this.picture = picture;
	}

	public void setRowCount(int rowCount) {
		this.rowCount = rowCount;
	}

	public String getLink() {
		return link;
	}

	public String clear() {
		return "";
	}

	public void setLink(String link) {
		this.link = link;
	}

	public String getSuccessMessage() {
		return successMessage;
	}

	public void setSuccessMessage(String successMessage) {
		this.successMessage = successMessage;
	}

	protected List<SelectItem> leftPickList = new ArrayList<SelectItem>();
	protected List<SelectItem> pickListResult = new ArrayList<SelectItem>();
	protected Map<Long, SelectItem> allItemMap = new HashMap<Long, SelectItem>();

	private List<String> selectLeftValues;
	private List<String> selectRightValues;

	public String moveToRight() {
		for (String selectLeftValue : selectLeftValues) {
			leftPickList.remove(allItemMap.get(new Long(selectLeftValue)));
			pickListResult.add(allItemMap.get(new Long(selectLeftValue)));
		}
		return "Success";
	}

	public String moveToLeft() {
		for (String selectRightValue : selectRightValues) {
			pickListResult.remove(allItemMap.get(new Long(selectRightValue)));
			leftPickList.add(allItemMap.get(new Long(selectRightValue)));
		}
		return "Success";
	}

	public String moveALLToRight() {
		leftPickList.clear();
		pickListResult.clear();
		int mapsize = allItemMap.size();

		Iterator keyValuePairs1 = allItemMap.entrySet().iterator();
		for (int i = 0; i < mapsize; i++) {
			Map.Entry entry = (Map.Entry) keyValuePairs1.next();
			pickListResult.add((SelectItem) entry.getValue());
		}
		return "Success";
	}

	public String moveALLToLeft() {
		leftPickList.clear();
		pickListResult.clear();
		int mapsize = allItemMap.size();

		Iterator keyValuePairs1 = allItemMap.entrySet().iterator();
		for (int i = 0; i < mapsize; i++) {
			Map.Entry entry = (Map.Entry) keyValuePairs1.next();
			leftPickList.add((SelectItem) entry.getValue());
		}
		return "Success";
	}

	public String getErrorMessage() {
		return errorMessage != null ? errorMessage : "";
	}

	public void setErrorMessage(String errorMessage) {
		this.errorMessage = errorMessage;
	}

	public void clearMessages() {
		errorMessage = "";
		successMessage = "";
	}

	protected Long getIdParameter() {
		FacesContext context = FacesContext.getCurrentInstance();
		String idString = context.getExternalContext().getRequestParameterMap()
				.get("id");
		return new Long(idString);
	}

	protected String getParameter(String paramName) {
		FacesContext context = FacesContext.getCurrentInstance();
		return context.getExternalContext().getRequestParameterMap()
				.get(paramName);
	}

	protected Object getSessionParameter(String paramName) {
		FacesContext context = FacesContext.getCurrentInstance();
		return context == null ? null : context.getExternalContext()
				.getSessionMap().get(paramName);
	}

	protected Object getApplicationParameter(String paramName) {
		FacesContext context = FacesContext.getCurrentInstance();
		return context == null ? null : context.getExternalContext()
				.getInitParameter(paramName);
	}

	protected Object getRequestParameter(String paramName) {
		FacesContext context = FacesContext.getCurrentInstance();
		return context.getExternalContext().getRequestParameterMap()
				.get(paramName);
	}

	public Long getCurrentUserId() {

		return getSessionParameter("user") != null ? ((User) getSessionParameter("user"))
				.getId() : new Long(0);
	}

	public User getCurrentUser() {
		return getSessionParameter("user") != null ? ((User) getSessionParameter("user"))
				: null;
	}

	public String getRessourceProperty(String propertyName) {
		FacesContext context = FacesContext.getCurrentInstance();

		return context.getApplication().getResourceBundle(context, "msg")
				.getString(propertyName);

	}

	protected void setSessionAttribute(String attributeName,
			String attributeValue) {
		HttpServletRequest request = (HttpServletRequest) FacesContext
				.getCurrentInstance().getExternalContext().getRequest();
		request.getSession().setAttribute(attributeName, attributeValue);
	}
	
	protected void removeSessionAttribute(String attributeName) {
		HttpServletRequest request = (HttpServletRequest) FacesContext
				.getCurrentInstance().getExternalContext().getRequest();
		request.getSession().removeAttribute(attributeName);
	}

	protected void setSessionAttribute(String attributeName,
			Object attributeValue) {
		HttpServletRequest request = (HttpServletRequest) FacesContext
				.getCurrentInstance().getExternalContext().getRequest();
		request.getSession().setAttribute(attributeName, attributeValue);
	}

	protected void setErrorMessage(Exception ex, String doublonMessage) {
		try {
			if (ex != null
					&& ex.getCause() != null
					&& ex.getCause().getMessage() != null
					&& (ex.getCause().getMessage()
							.contains("unique constraint") || ex.getCause()
							.getMessage().contains("Duplicate entry")))
				setErrorMessage(doublonMessage);
			else
				setErrorMessage(getResourceBundle().getString("SAVED_UNSUCCESSFULL") + " " + getResourceBundle().getString("Error") + " "
						+ ex.getMessage());
		} catch (Exception e) {
			setErrorMessage(ex.getMessage());
			e.printStackTrace();
		}
	}

	public List<SelectItem> getLeftPickList() {
		return leftPickList;
	}

	public void setLeftPickList(List<SelectItem> leftPickList) {
		this.leftPickList = leftPickList;
	}

	public List<SelectItem> getPickListResult() {
		return pickListResult;
	}

	public void setPickListResult(List<SelectItem> pickListResult) {
		this.pickListResult = pickListResult;
	}

	public Map<Long, SelectItem> getAllItemMap() {
		return allItemMap;
	}

	public void setAllItemMap(Map<Long, SelectItem> allItemMap) {
		this.allItemMap = allItemMap;
	}

	public List<String> getSelectLeftValues() {
		return selectLeftValues;
	}

	public void setSelectLeftValues(List<String> selectLeftValues) {
		this.selectLeftValues = selectLeftValues;
	}

	public List<String> getSelectRightValues() {
		return selectRightValues;
	}

	public void setSelectRightValues(List<String> selectRightValues) {
		this.selectRightValues = selectRightValues;
	}

	public void addToSession(String string, Object user2) {
		// TODO Auto-generated method stub

	}

	private String selectedTab;

	public String getSelectedTab() {
		return selectedTab;
	}

	public void setSelectedTab(String selectedTab) {
		this.selectedTab = selectedTab;
	}

	private String subSelectedTab = "";
	
	
	public String getSubSelectedTab() {
		return subSelectedTab;
	}

	public void setSubSelectedTab(String subSelectedTab) {
		this.subSelectedTab = subSelectedTab;
	}

	public Config getConfig() {
		return config;
	}

	public void setConfig(Config config) {
		this.config = config;
	}

	public ApplicationContext getApplicationContext() {
		FacesContext context = FacesContext.getCurrentInstance();
		ApplicationContext ctx = org.springframework.web.jsf.FacesContextUtils.getWebApplicationContext(context);
		return ctx;	
	}
	
	
	public List<SelectItem> getConfigurationsByGroupName() {
		FacesContext context = FacesContext.getCurrentInstance();
		ApplicationContext ctx = org.springframework.web.jsf.FacesContextUtils
				.getWebApplicationContext(context);

		String groupName = "";
		if (myValue != null)
			groupName = (String) myValue.getAttributes().get("groupName");
		else if (myManyValue != null)
			groupName = (String) myManyValue.getAttributes().get("groupName");
		
		Config config = (Config) ctx.getBean("config");

		List<Configuration> configs = config
				.getConfigurationListByGroup(groupName);
		List<SelectItem> selecteds = new ArrayList<SelectItem>();

		for (Configuration conf : configs) {
			if (conf.getSchool().equals(getCurrentUser().getSchool()))
				selecteds.add(new SelectItem(conf.getValue(), conf.getName()));
		}
		
		return selecteds;
	}
	
	public List<SelectItem> getConfigsByGroupName() {
		FacesContext context = FacesContext.getCurrentInstance();
		ApplicationContext ctx = org.springframework.web.jsf.FacesContextUtils
				.getWebApplicationContext(context);

		String groupName = "";
		if (mySecondValue != null)
			groupName = (String) mySecondValue.getAttributes().get("groupName");
		
		Config config = (Config) ctx.getBean("config");

		List<Configuration> configs = config
				.getConfigurationListByGroup(groupName);
		List<SelectItem> selecteds = new ArrayList<SelectItem>();

		for (Configuration conf : configs) {
			if (conf.getSchool().equals(getCurrentUser().getSchool()))
				selecteds.add(new SelectItem(conf.getValue(), conf.getName()));
		}
		
		return selecteds;
	}
	
	public List<SelectItem> getThirdConfigsByGroupName() {
		FacesContext context = FacesContext.getCurrentInstance();
		ApplicationContext ctx = org.springframework.web.jsf.FacesContextUtils
				.getWebApplicationContext(context);

		String groupName = "";
		if (myThirdValue != null)
			groupName = (String) myThirdValue.getAttributes().get("groupName");
		
		Config config = (Config) ctx.getBean("config");

		List<Configuration> configs = config
				.getConfigurationListByGroup(groupName);
		List<SelectItem> selecteds = new ArrayList<SelectItem>();

		for (Configuration conf : configs) {
			if (conf.getSchool().equals(getCurrentUser().getSchool()))
				selecteds.add(new SelectItem(conf.getValue(), conf.getName()));
		}
		
		return selecteds;
	}
	public void clearConfigMap() {
		Config config = (Config) getApplicationContext().getBean("config");
		config.clearConfigMap();
	}
	
	public int getLocalesSize() {	
		FacesContext context = FacesContext.getCurrentInstance();
		ApplicationContext ctx = org.springframework.web.jsf.FacesContextUtils.getWebApplicationContext(context);
		
		Config config = (Config) ctx.getBean("config");
		List<Configuration> configs = config.getConfigurationListByGroup("LOCALE");
		return configs != null ? configs.size() : 0;
	}

	protected HtmlSelectOneMenu myValue;
	protected HtmlSelectOneMenu mySecondValue;
	protected HtmlSelectOneMenu myThirdValue;
	
	
	public HtmlSelectOneMenu getMyThirdValue() {
		return myThirdValue;
	}

	public void setMyThirdValue(HtmlSelectOneMenu myThirdValue) {
		this.myThirdValue = myThirdValue;
	}

	public HtmlSelectOneMenu getMySecondValue() {
		return mySecondValue;
	}

	public void setMySecondValue(HtmlSelectOneMenu mySecondValue) {
		this.mySecondValue = mySecondValue;
	}

	public HtmlSelectOneMenu getMyValue() {
		return myValue;
	}

	public void setMyValue(HtmlSelectOneMenu myValue) {
		this.myValue = myValue;
	}

	protected HtmlSelectManyListbox myManyValue;

	public HtmlSelectManyListbox getMyManyValue() {
		return myManyValue;
	}

	public void setMyManyValue(HtmlSelectManyListbox myManyValue) {
		this.myManyValue = myManyValue;
	}

	protected HtmlInputText myInputValue;

	
	public HtmlInputText getMyInputValue() {
		return myInputValue;
	}

	public void setMyInputValue(HtmlInputText myInputValue) {
		this.myInputValue = myInputValue;
	}

	protected HtmlCalendar myCalendarValue;
	
	public HtmlCalendar getMyCalendarValue() {
		return myCalendarValue;
	}

	public void setMyCalendarValue(HtmlCalendar myCalendarValue) {
		this.myCalendarValue = myCalendarValue;
	}

	private HtmlTab myTab;

	
	public HtmlTab getMyTab() {
		return myTab;
	}

	public void setMyTab(HtmlTab myTab) {
		this.myTab = myTab;
	}

	public Search getSearch() {
		return search;
	}

	public void setSearch(Search search) {
		this.search = search;
	}

	public Connection getConnection() {
		Connection jdbcConnection = null;

		FacesContext context = FacesContext.getCurrentInstance();

		ApplicationContext ctx = org.springframework.web.jsf.FacesContextUtils
				.getWebApplicationContext(context);

		DataSource dataSource = (DataSource) ctx.getBean("dataSource");
		try {
			jdbcConnection = dataSource.getConnection();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return jdbcConnection;
	}
	
	
	public boolean isUserHasTabAccess() {
		String tabName = (String) myTab.getAttributes().get("tabName");
		Map<String, Menu> tabMenus = (Map<String, Menu>) getSessionParameter("tabMenus");
		Menu menu = tabMenus.get(tabName);
		return menu != null;
	}
	
	public boolean isUserHasWriteAccess(Long securityCode) {
		Set<Menu> userMenus = (Set<Menu>) getSessionParameter("menus");
		Menu foundMenu = null;
		for (Menu menu : userMenus) {
			if ( securityCode.toString().equals(menu.getSecurityCode().toString()) && menu.getAccessLevel() != null) {
				foundMenu = menu;
				break;
			}
		}
		return foundMenu != null && "1".equals(foundMenu.getAccessLevel().toString());
	}

	public String getReportsDirPath() {
		FacesContext context = FacesContext.getCurrentInstance();
		String reportsDirPath = ((ServletContext) context
				.getExternalContext().getContext())
				.getRealPath("/reports/");
		return reportsDirPath;
	}
	
	public FacesContext getContext() {
		return FacesContext.getCurrentInstance();
	}

	public boolean isCanModify() {
		return getSessionParameter("modifyEvent") != null && getSessionParameter("modifyEvent").equals("true") ? true : false;
	}

	public void setCanModify(boolean canModify) {
		this.canModify = canModify;
	}
	
	public ResourceBundle getResourceBundle() {
		if (resourceBundle == null)
			resourceBundle  = ResourceBundle.getBundle("/com.esoft.ischool.config.MessageRessources", getContext().getViewRoot().getLocale());
		
		return resourceBundle;
	}
	
	public void setResourceBundle(Locale locale) {
		if (resourceBundle == null)
			resourceBundle  = ResourceBundle.getBundle("/com.esoft.ischool.config.MessageRessources", locale);
	}
	
	public String justDoIt() {
		selectedTab = getParameter("selectedTab");
		subSelectedTab = getParameter("subSelectedTab");
		return "";
	}
	
	private boolean open;

	public boolean isOpen() {
		return open;
	}

	public void setOpen(boolean open) {
		this.open = open;
	}
	
	protected String printString(String toPrint) {
		return toPrint == null ? "" : "\"" + toPrint + "\"";
	}
	
	protected String printString(Date toPrint, DateFormat df) {
		if (toPrint == null) return "";
		String formattedDate = df.format(toPrint);
		return toPrint == null ? "" : "\"" + formattedDate + "\"";
	}
	
	protected String getDelimiter(Locale locale) {
		String delimiter = getCurrentUser().getCsvDelimiter();
		if (StringUtils.isBlank(delimiter)) {
			delimiter = config.getConfigurationByGroupAndKey("OPERATING_SYSTEM","LIST_SEPARATOR");
			if (StringUtils.isBlank(delimiter)) {
				if ("fr_FR".equals(locale.toString()))
					delimiter = ";";
				else 
					delimiter = ",";
			}
		}
		return delimiter;
	}
	
	protected String getStringDate(){
		Date laDate = new Date();
		SimpleDateFormat sdf = new SimpleDateFormat("YYYYMdhhmms");	
		return sdf.format(laDate);
	}
}
