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
import com.esoft.ischool.service.BaseService;
import com.esoft.ischool.stocks.model.Brand;
import com.esoft.ischool.util.MenuIdEnum;

@Component("brandBean")
@Scope("session")
public class BrandBean extends BaseBean {

	@Autowired
	@Qualifier("baseService")
	private BaseService baseService;
	private Long rowCount;
	private List<BaseEntity> brands = new ArrayList<BaseEntity>();
	private List<String> allBrands = new ArrayList<String>();
	
	private Brand brand = new Brand();

	public List<Brand> autoComplete(Object suggest) {
		String pref = (String) suggest;
		List<Brand> result = new ArrayList<Brand>();
		allBrands.clear();
		
		for (BaseEntity entity : brands) {
			Brand b = (Brand) entity;
			if ((b.getName() != null && b.getName().toLowerCase().indexOf(
					pref.toLowerCase()) == 0)
					|| "".equals(pref))
				result.add(b);
			
			allBrands.add(b.getName());
		}

		return result;
	}

	public String validate() {
		return "succes";
	}
	
	@Override
	public String clear() {
		brand = new Brand();
		return "Success";
	}

	public String delete() {
		clearMessages();
		try {
			baseService.delete(getIdParameter(), Brand.class);
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
		Long id = brand.getId();
		try {
			if (id == null || id == 0)
				baseService.save(brand,getCurrentUser());
			else
				baseService.update(brand,getCurrentUser());
			setSuccessMessage(getResourceBundle().getString("SAVED_SUCCESSFULLY"));
		} catch (Exception ex) {
			brand.setId(id);
			setErrorMessage(ex,"Ce brand existe deja");
		}

		
		clear();
		getAll();
		return "Success";
	}


	public String edit() {
		clearMessages();
		brand = (Brand) baseService.getById(Brand.class, getIdParameter());
		return "Success";
	}
	
	@PostConstruct
	private void getAll() {
		brands = baseService.loadAll(Brand.class, getCurrentUser().getSchool());
		setRowCount(new Long(brands.size()));

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

	public List<BaseEntity> getBrands() {
		return brands;
	}

	public void setBrands(List<BaseEntity> brands) {
		this.brands = brands;
	}

	public List<String> getAllBrands() {
		autoComplete("");
		return allBrands;
	}

	public void setAllBrands(List<String> allBrands) {
		this.allBrands = allBrands;
	}

	public Brand getBrand() {
		return brand;
	}

	public void setBrand(Brand brand) {
		this.brand = brand;
	}
}
