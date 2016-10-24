package com.esoft.ischool.restservice;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.beanutils.BeanComparator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.esoft.ischool.job.Config;
import com.esoft.ischool.model.BaseEntity;
import com.esoft.ischool.model.UsersFeedback;
import com.esoft.ischool.security.model.User;
import com.esoft.ischool.service.BaseService;
import com.esoft.ischool.util.Constants;
import com.esoft.ischool.util.MenuIdEnum;
import com.esoft.ischool.util.SimpleMail;

@Component("usersFeedbackBean")
@Scope("session")
public class UsersFeedbackBean extends BaseBean {

	@Autowired
	@Qualifier("baseService")
	private BaseService baseService;
	private Long rowCount;
	private List<BaseEntity> usersUsersFeedbacks = new ArrayList<BaseEntity>();
	private List<String> allUsersFeedbacks = new ArrayList<String>();

	private UsersFeedback usersFeedback = new UsersFeedback();

	@Override
	public String clear() {
		usersFeedback = new UsersFeedback();
		return "Success";
	}

	public String delete() {
		clearMessages();
		try {
			baseService.delete(getIdParameter(), UsersFeedback.class);
			getAll();
			clear();
			setSuccessMessage(getResourceBundle().getString(
					"DELETE_SUCCESSFULLY"));
		} catch (Exception ex) {
			setErrorMessage(getResourceBundle().getString(
					"DELETE_UNSUCCESSFULL"));
		}

		return "Success";
	}

	public String insert() {
		clearMessages();
		Long id = usersFeedback.getId();
		try {
			HttpServletRequest request = (HttpServletRequest) (FacesContext
					.getCurrentInstance().getExternalContext().getRequest());
			usersFeedback.setUserDetails("RemoteAddr="
					+ request.getRemoteAddr() + ", RemoteHost"
					+ request.getRemoteHost() + ", LocalName="
					+ request.getLocalName() + ", Other= "
					+ request.getHeader("User-Agent"));
			if (id == null || id == 0) {
				User user = (User) baseService.getById(User.class, 1L);
				setSessionAttribute("user", user);
				baseService.save(usersFeedback, user);
				try {
					FacesContext context = FacesContext.getCurrentInstance();
					ApplicationContext ctx = org.springframework.web.jsf.FacesContextUtils
							.getWebApplicationContext(context);
					Config config = (Config) ctx.getBean("config");
					if (config != null) {
						SimpleMail
								.sendMail(
										Constants.REGISTRATION_RECEIVED,
										usersFeedback.toString(),
										config.getConfigPropertyValue("SCHOOL_SENDER_EMAIL"),
										config.getConfigPropertyValue("SCHOOL_SENDER_EMAIL"),
										config.getConfigPropertyValue("SCHOOL_SMTP_SERVER"),
										config.getConfigPropertyValue("SCHOOL_MAIL_SERVER_USER"),
										config.getConfigPropertyValue("SCHOOL_MAIL_SERVER_PASSWORD"));

					}
				} catch (Exception e) {
					e.printStackTrace();
				}
				setSuccessMessage(getResourceBundle().getString(
						"FEEDBACK_RECEIVED"));
			} else {
				baseService.update(usersFeedback, getCurrentUser());
				setSuccessMessage(getResourceBundle().getString(
						"SAVED_SUCCESSFULLY"));
			}
			clear();
		} catch (Exception ex) {
			usersFeedback.setId(id);
			setErrorMessage(ex, ex.getMessage());			 
		}

		return "Success";
	}

	public String redirect(){
		
		if(getErrorMessage()!=null&&!getErrorMessage().equals("")){
			return "echec";
		}else{
			return "succes";
		}
	}
	public String edit() {
		clearMessages();
		usersFeedback = (UsersFeedback) baseService.getById(
				UsersFeedback.class, getIdParameter());
		return "Success";
	}

	private void getAll() {
		usersUsersFeedbacks = baseService.loadAll(UsersFeedback.class,
				baseService.getDefaultSchool());
		BeanComparator beanComparator = new BeanComparator("modDate");
		Collections.reverseOrder();
		Collections.sort(usersUsersFeedbacks, beanComparator);
		setRowCount(new Long(usersUsersFeedbacks.size()));

	}

	public BaseService getBaseService() {
		return baseService;
	}

	public void setBaseService(BaseService baseService) {
		this.baseService = baseService;
	}

	public UsersFeedback getUsersFeedback() {
		return usersFeedback;
	}

	public void setUsersFeedback(UsersFeedback usersFeedback) {
		this.usersFeedback = usersFeedback;
	}

	public Long getRowCount() {
		return rowCount;
	}

	public void setRowCount(Long rowCount) {
		this.rowCount = rowCount;
	}

	public List<BaseEntity> getUsersFeedbacks() {
		return usersUsersFeedbacks;
	}

	public void setUsersFeedbacks(List<BaseEntity> usersUsersFeedbacks) {
		this.usersUsersFeedbacks = usersUsersFeedbacks;
	}

	public List<String> getAllUsersFeedbacks() {
		return allUsersFeedbacks;
	}

	public void setAllUsersFeedbacks(List<String> allUsersFeedbacks) {

		this.allUsersFeedbacks = allUsersFeedbacks;
	}

	public boolean isUserHasWriteAccess() {
		return isUserHasWriteAccess(MenuIdEnum.AUTRE.getValue());
	}
}
