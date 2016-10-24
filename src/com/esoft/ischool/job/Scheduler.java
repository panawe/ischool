package com.esoft.ischool.job;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.FutureTask;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.esoft.ischool.model.Alert;
import com.esoft.ischool.service.BaseService;
import com.esoft.ischool.util.AlertType;

@Component("scheduler")
@Scope("singleton")
public class Scheduler {

	@Autowired
	@Qualifier("tuitionPaymentJobProcess")
	private TuitionPaymentProcessJob tuitionPaymentJobProcess;
	
	@Autowired
	@Qualifier("baseService")
	private BaseService baseService;

	@Autowired
	@Qualifier("config")
	private Config config;


	// properties and collaborators
	public void doIt() {
		ExecutorService es = Executors.newFixedThreadPool(5);
		
		for (Alert alert : baseService.getAllActiveAlert(Alert.class, null)) {
			if (alert.getAlertTypeCode().equals(AlertType.TUITION_PAYMENT_OVER_DUE.getValue())) {
				TuitionPaymentProcessJob tuitionPaymentProcessJob = new TuitionPaymentProcessJob();
				tuitionPaymentProcessJob.setAlert(alert);
				tuitionPaymentProcessJob.setConfig(config);
				tuitionPaymentProcessJob.setBaseService(baseService);
				FutureTask task = new FutureTask (tuitionPaymentProcessJob);
				es.submit (task);
				  try {
					Object result = task.get();
				} catch (InterruptedException e) {
					e.printStackTrace();
				} catch (ExecutionException e) {
					e.printStackTrace();
				}

			}
			if (alert.getAlertTypeCode().equals(AlertType.PRODUCT_MINIMUM_STOCK_HIT.getValue())) {
				MinimumQuantityHitJob minimumQuantityHitJob = new MinimumQuantityHitJob();
				minimumQuantityHitJob.setAlert(alert);
				minimumQuantityHitJob.setConfig(config);
				minimumQuantityHitJob.setBaseService(baseService);
				FutureTask task = new FutureTask (minimumQuantityHitJob);
				es.submit (task);
				  try {
					Object result = task.get();
				} catch (InterruptedException e) {
					e.printStackTrace();
				} catch (ExecutionException e) {
					e.printStackTrace();
				}
			}
			if (alert.getAlertTypeCode().equals(AlertType.PRODUCT_CONSUMER_OVER_DUE.getValue())) {
				OverDueConsumerProductJob overDueConsumerProductJob = new OverDueConsumerProductJob();
				overDueConsumerProductJob.setAlert(alert);
				overDueConsumerProductJob.setConfig(config);
				overDueConsumerProductJob.setBaseService(baseService);
				FutureTask task = new FutureTask (overDueConsumerProductJob);
				es.submit (task);
				try {
					Object result = task.get();
				} catch (InterruptedException e) {
					e.printStackTrace();
				} catch (ExecutionException e) {
					e.printStackTrace();
				}
			}
		}
		
		es.shutdown ();
	}
	
	public TuitionPaymentProcessJob getTuitionPaymentJobProcess() {return tuitionPaymentJobProcess;}
	public void setTuitionPaymentJobProcess(TuitionPaymentProcessJob tuitionPaymentJobProcess) {this.tuitionPaymentJobProcess = tuitionPaymentJobProcess;}	
	
	public BaseService getBaseService() {return baseService;}
	public void setBaseService(BaseService baseService) {this.baseService = baseService;}

	public Config getConfig() {return config;}
	public void setConfig(Config config) {this.config = config;}
}