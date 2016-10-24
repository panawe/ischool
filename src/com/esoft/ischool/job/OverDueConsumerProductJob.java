package com.esoft.ischool.job;

import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.concurrent.Callable;

import org.apache.commons.httpclient.util.DateUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.esoft.ischool.restservice.BaseBean;
import com.esoft.ischool.model.Alert;
import com.esoft.ischool.model.AlertReceiver;
import com.esoft.ischool.model.BaseEntity;
import com.esoft.ischool.security.model.User;
import com.esoft.ischool.service.BaseService;
import com.esoft.ischool.stocks.model.ProductConsumer;
import com.esoft.ischool.util.Constants;
import com.mysql.jdbc.StringUtils;

@Component("overDueConsumerProductJob")
@Scope("singleton")
public class OverDueConsumerProductJob extends BaseBean implements Callable{

	@Autowired
	@Qualifier("baseService")
	private BaseService baseService;
	
	private Alert alert;
	
	private Config config;
	
	public Integer call() {
		
		Integer numberOfDays = alert.getMoment() == 0 ? alert.getNumberOfDays() : -1 * alert.getNumberOfDays();
		List<BaseEntity> entities = getBaseService().getAllProductConsumersDueInDays(ProductConsumer.class, numberOfDays);
		
		User user = new User();
		user.setId(new Long(1));
		
		StringBuffer messageToSend = new StringBuffer();

		for (BaseEntity entity : entities) {
			ProductConsumer productConsumer = (ProductConsumer) entity;
			AlertReceiver alertReceiver = new AlertReceiver();
			
			alertReceiver.setAlert(alert);
			user.setSchool(alert.getSchool());
			alertReceiver.setUser(user);
			
			List<AlertReceiver> alertReceived = baseService.getAlertReceiver(AlertReceiver.class, alert.getId(), null, user.getId());
			if (alertReceived != null && alertReceived.size() < alert.getNumberOfRepeat()) {
				Calendar cal = Calendar.getInstance(); 
				cal.add(Calendar.DATE, -1 * alert.getRepeatInterval()); 
				
				if (alertReceived != null && (alertReceived.size() == 0 || (alertReceived.size() > 0 && (cal.getTime().compareTo(alertReceived.get(0).getCreateDate()) > 0)))) {
					baseService.save(alertReceiver,user);
					messageToSend.append(cleanMessage(productConsumer));
				}
			}
		}
		
		if (messageToSend.length() > 0) {
			if (!StringUtils.isNullOrEmpty(alert.getSendTo())) {
				
				String [] addressEmails = alert.getSendTo().split(",");
				
				for (String addressEmail : addressEmails) 
					config.sendMail(addressEmail, alert.getSubject(), messageToSend.toString(), user.getSchool());
			}
			StringBuffer sb = new StringBuffer();
			config.setUserEmail(user, sb);
			config.sendMail(sb.toString(), alert.getSubject(), messageToSend.toString(), user.getSchool());
		}
		
		return 0;
	}


	private String cleanMessage(ProductConsumer productConsumer) {
		String toReturn = alert.getMessage().replaceAll(Constants.PRODUCT_NAME, (productConsumer.getProduct().getName()));
		toReturn = toReturn.replaceAll(Constants.REQUESTED_DATE, productConsumer.getRequestDate().toString());
		toReturn = toReturn.replaceAll(Constants.PICKED_UP_DATE, productConsumer.getPickupDate().toString());
		toReturn = toReturn.replaceAll(Constants.DUE_DATE, productConsumer.getReturnDate().toString());
		toReturn = toReturn.replaceAll(Constants.SCHOOL_NAME, productConsumer.getSchool().getName());
		toReturn = toReturn.replaceAll(Constants.TODAY_DATE, DateUtil.formatDate(new Date(), "dd/MM/yyyy"));
		toReturn = toReturn.replaceAll(Constants.SUBJECT_NAME, alert.getSubject());
		toReturn = toReturn.replaceAll(Constants.SCHOOL_WEBSITE, new Config().getConfigurationByGroupAndKey("SCHOOL", "SCHOOL_WEBSITE"));
		toReturn = toReturn.replaceAll(Constants.QUANTITY_TO_BE_RETURNED, productConsumer.getQuantityToBeReturned().toString());
		toReturn = toReturn.replaceAll(Constants.QUANTITY_RETURNED, productConsumer.getQuantityReturned().toString());
		return toReturn + "\n";
	}
	
	public BaseService getBaseService() {return baseService;}
	public void setBaseService(BaseService baseService) {this.baseService = baseService;}

	public Alert getAlert() {return alert;}
	public void setAlert(Alert alert) {this.alert = alert;}

	@Override
	public Config getConfig() {return config;}
	@Override
	public void setConfig(Config config) {this.config = config;}
}
