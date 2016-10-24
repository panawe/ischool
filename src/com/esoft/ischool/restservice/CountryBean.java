package com.esoft.ischool.restservice;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.annotation.PostConstruct;

import org.apache.commons.beanutils.BeanComparator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.esoft.ischool.model.BaseEntity;
import com.esoft.ischool.model.Country;
import com.esoft.ischool.service.BaseService;
import com.esoft.ischool.util.MenuIdEnum;

@Component("countryBean")
@Scope("session")
public class CountryBean extends BaseBean {

	@Autowired
	@Qualifier("baseService")
	private BaseService baseService;
	private Long rowCount;
	private List<BaseEntity> countries = new ArrayList<BaseEntity>();
	private List<String> allCountries = new ArrayList<String>();
	
	private Country country = new Country();

	public List<Country> autoComplete(Object suggest) {
		String pref = (String) suggest;
		List<Country> result = new ArrayList<Country>();
		allCountries.clear();
		
		for (BaseEntity entity : countries) {
			Country c = (Country) entity;
			if ((c.getName() != null && c.getName().toLowerCase().indexOf(
					pref.toLowerCase()) == 0)
					|| "".equals(pref))
				result.add(c);
			
			allCountries.add(c.getName());
		}

		return result;
	}

	public String validate() {
		return "succes";
	}
	
	@Override
	public String clear() {
		country = new Country();
		return "Success";
	}

	public String delete() {
		clearMessages();
		try {
			baseService.delete(getIdParameter(), Country.class);
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
		Long id = country.getId();
		try {
			if (id == null || id == 0)
				baseService.save(country,getCurrentUser());
			else
				baseService.update(country,getCurrentUser());
			setSuccessMessage(getResourceBundle().getString("SAVED_SUCCESSFULLY"));
		} catch (Exception ex) {
			country.setId(id);
			setErrorMessage(ex,"Ce pays existe deja");
		}

		
		clear();
		getAll();
		return "Success";
	}


	public String edit() {
		clearMessages();
		country = (Country) baseService.getById(Country.class, getIdParameter());
		return "Success";
	}
	
	@PostConstruct
	private void getAll() {
		countries = baseService.loadAll(Country.class,baseService.getDefaultSchool());
		BeanComparator beanComparator = new BeanComparator("name"); 
		Collections.sort(countries, beanComparator);
		setRowCount(new Long(countries.size()));

	}

	public BaseService getBaseService() {
		return baseService;
	}

	public void setBaseService(BaseService baseService) {
		this.baseService = baseService;
	}

	public Country getCountry() {
		return country;
	}

	public void setCountry(Country country) {
		this.country = country;
	}

	public Long getRowCount() {
		return rowCount;
	}

	public void setRowCount(Long rowCount) {
		this.rowCount = rowCount;
	}

	public List<BaseEntity> getCountries() {
		return countries;
	}

	public void setCountries(List<BaseEntity> countries) {
		this.countries = countries;
	}

	public List<String> getAllCountries() {
	
		autoComplete("");
		return allCountries;
	}

	public void setAllCountries(List<String> allCountries) {
		
		this.allCountries = allCountries;
	}	
	
	public boolean isUserHasWriteAccess() {
		return isUserHasWriteAccess(MenuIdEnum.AUTRE.getValue());
	}
}
