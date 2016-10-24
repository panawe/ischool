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
import com.esoft.ischool.stocks.model.Supplier;
import com.esoft.ischool.util.MenuIdEnum;

@Component("supplierBean")
@Scope("session")
public class SupplierBean extends BaseBean {

	@Autowired
	@Qualifier("baseService")
	private BaseService baseService;
	private Long rowCount;
	private List<BaseEntity> suppliers = new ArrayList<BaseEntity>();
	private List<String> allSuppliers = new ArrayList<String>();
	
	private Supplier supplier = new Supplier();
	private String countryName = "";

	public List<Supplier> autoComplete(Object suggest) {
		String pref = (String) suggest;
		List<Supplier> result = new ArrayList<Supplier>();
		allSuppliers.clear();
		
		for (BaseEntity entity : suppliers) {
			Supplier b = (Supplier) entity;
			if ((b.getName() != null && b.getName().toLowerCase().indexOf(
					pref.toLowerCase()) == 0)
					|| "".equals(pref))
				result.add(b);
			
			allSuppliers.add(b.getName());
		}

		return result;
	}

	public String validate() {
		return "succes";
	}
	
	@Override
	public String clear() {
		setCountryName("");
		supplier = new Supplier();
		return "Success";
	}

	public String delete() {
		clearMessages();
		try {
			baseService.delete(getIdParameter(), Supplier.class);
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
		Long id = supplier.getId();
		try {
			Country country = (Country) baseService.findByColumn(Country.class, "name", countryName);
			supplier.setCountry(country);
			
			if (id == null || id == 0)
				baseService.save(supplier,getCurrentUser());
			else
				baseService.update(supplier,getCurrentUser());
			setSuccessMessage(getResourceBundle().getString("SAVED_SUCCESSFULLY"));
		} catch (Exception ex) {
			supplier.setId(id);
			setErrorMessage(ex,"Ce fournisseur existe deja");
		}

		
		clear();
		getAll();
		return "Success";
	}


	public String edit() {
		clearMessages();
		supplier = (Supplier) baseService.getById(Supplier.class, getIdParameter());
		countryName=supplier.getCountry()==null?"":supplier.getCountry().getName();
		return "Success";
	}
	
	@PostConstruct
	private void getAll() {
		suppliers = baseService.loadAll(Supplier.class, getCurrentUser().getSchool());
		setRowCount(new Long(suppliers.size()));

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

	public List<BaseEntity> getSuppliers() {
		return suppliers;
	}

	public void setSuppliers(List<BaseEntity> suppliers) {
		this.suppliers = suppliers;
	}

	public List<String> getAllSuppliers() {
		autoComplete("");
		return allSuppliers;
	}

	public void setAllSuppliers(List<String> allSuppliers) {
		this.allSuppliers = allSuppliers;
	}

	public Supplier getSupplier() {
		return supplier;
	}

	public void setSupplier(Supplier supplier) {
		this.supplier = supplier;
	}


	public String getCountryName() {
		return countryName;
	}

	public void setCountryName(String countryName) {
		this.countryName = countryName;
	}
	
	public boolean isUserHasWriteAccess() {
		return isUserHasWriteAccess(MenuIdEnum.STOCK_CONFIGURATION.getValue());
	}

	
}
