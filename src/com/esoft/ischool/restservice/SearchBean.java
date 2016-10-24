package com.esoft.ischool.restservice;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.esoft.ischool.service.BaseService;


@Component("seachBean")
@Scope("session")
public class SearchBean extends BaseBean {

	@Autowired
	@Qualifier("baseService")
	private BaseService baseService;
	private String searchValue;
	public BaseService getBaseService() {
		return baseService;
	}
	public void setBaseService(BaseService baseService) {
		this.baseService = baseService;
	}
	public String getSearchValue() {
		return searchValue;
	}
	public void setSearchValue(String searchValue) {
		this.searchValue = searchValue;
	}
	
	public String search(){
		
		return "succes";
	}

}
