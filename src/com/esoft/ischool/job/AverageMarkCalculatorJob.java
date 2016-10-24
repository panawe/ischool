package com.esoft.ischool.job;

import java.util.concurrent.Callable;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.esoft.ischool.restservice.BaseBean;
import com.esoft.ischool.service.BaseService;

@Component("averageMarkCalculatorJob")
@Scope("singleton")
public class AverageMarkCalculatorJob extends BaseBean implements Callable {

	@Autowired
	@Qualifier("baseService")
	private BaseService baseService;
	
	public Object call() throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	public BaseService getBaseService() {
		return baseService;
	}

	public void setBaseService(BaseService baseService) {
		this.baseService = baseService;
	}

}
