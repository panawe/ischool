package com.esoft.ischool.restservice;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.esoft.ischool.bean.BaseBean;
import com.esoft.ischool.model.BaseEntity;
import com.esoft.ischool.model.Country;
import com.esoft.ischool.service.BaseService;
import com.esoft.ischool.stocks.model.Carrier;
import com.esoft.ischool.util.MenuIdEnum;

@Component("carrierBean")
@Scope("session")
public class CarrierBean extends BaseBean {

	@Autowired
	@Qualifier("baseService")
	private BaseService baseService;
	private Long rowCount;
	private List<BaseEntity> carriers = new ArrayList<BaseEntity>();
	private List<String> allCarriers = new ArrayList<String>();
	private String countryName="";
	
	private Carrier carrier = new Carrier();

	public List<Carrier> autoComplete(Object suggest) {
		String pref = (String) suggest;
		List<Carrier> result = new ArrayList<Carrier>();
		allCarriers.clear();
		
		for (BaseEntity entity : carriers) {
			Carrier b = (Carrier) entity;
			if ((b.getName() != null && b.getName().toLowerCase().indexOf(
					pref.toLowerCase()) == 0)
					|| "".equals(pref))
				result.add(b);
			
			allCarriers.add(b.getName());
		}

		return result;
	}

	public String validate() {
		return "succes";
	}
	
	public String getCountryName() {
		return countryName;
	}

	public void setCountryName(String countryName) {
		this.countryName = countryName;
	}

	@Override
	public String clear() {
		carrier = new Carrier();
		countryName="";
		return "Success";
	}

	public String delete() {
		clearMessages();
		try {
			baseService.delete(getIdParameter(), Carrier.class);
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
		Long id = carrier.getId();
		try {
			Country country = (Country) baseService.findByColumn(Country.class, "name", countryName);
			carrier.setCountry(country);
			
			if (id == null || id == 0)
				baseService.save(carrier,getCurrentUser());
			else
				baseService.update(carrier,getCurrentUser());
			setSuccessMessage(getResourceBundle().getString("SAVED_SUCCESSFULLY"));
		} catch (Exception ex) {
			carrier.setId(id);
			setErrorMessage(ex,"Ce carrier existe deja");
		}

		
		clear();
		getAll();
		return "Success";
	}


	public String edit() {
		clearMessages();
		carrier = (Carrier) baseService.getById(Carrier.class, getIdParameter());
		return "Success";
	}
	
	@PostConstruct
	private void getAll() {
		carriers = baseService.loadAll(Carrier.class, getCurrentUser().getSchool());
		setRowCount(new Long(carriers.size()));

	}
	
	public boolean isUserHasWriteAccess() {
		return isUserHasWriteAccess(MenuIdEnum.STOCK_CONFIGURATION.getValue());
	}

	public BaseService getBaseService() {
		return baseService;
	}

	public void setBaseService(BaseService baseService) {
		this.baseService = baseService;
	}

	public Long getRowCount() {
		return rowCount;
	}

	public void setRowCount(Long rowCount) {
		this.rowCount = rowCount;
	}

	public List<BaseEntity> getCarriers() {
		return carriers;
	}

	public void setCarriers(List<BaseEntity> carriers) {
		this.carriers = carriers;
	}

	public List<String> getAllCarriers() {
		autoComplete("");
		return allCarriers;
	}

	public void setAllCarriers(List<String> allCarriers) {
		this.allCarriers = allCarriers;
	}

	public Carrier getCarrier() {
		return carrier;
	}

	public void setCarrier(Carrier carrier) {
		this.carrier = carrier;
	}
}
