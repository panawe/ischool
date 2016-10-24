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
import com.esoft.ischool.stocks.model.Category;
import com.esoft.ischool.util.MenuIdEnum;

@Component("categoryBean")
@Scope("session")
public class CategoryBean extends BaseBean {

	@Autowired
	@Qualifier("baseService")
	private BaseService baseService;
	private Long rowCount;
	private List<BaseEntity> categories = new ArrayList<BaseEntity>();
	private List<String> allCategories = new ArrayList<String>();
	
	private Category category = new Category();

	public List<Category> autoComplete(Object suggest) {
		String pref = (String) suggest;
		List<Category> result = new ArrayList<Category>();
		allCategories.clear();
		
		for (BaseEntity entity : categories) {
			Category c = (Category) entity;
			if ((c.getName() != null && c.getName().toLowerCase().indexOf(
					pref.toLowerCase()) == 0)
					|| "".equals(pref))
				result.add(c);
			
			allCategories.add(c.getName());
		}

		return result;
	}

	public String validate() {
		return "succes";
	}
	
	@Override
	public String clear() {
		category = new Category();
		return "Success";
	}

	public String delete() {
		clearMessages();
		try {
			baseService.delete(getIdParameter(), Category.class);
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
		Long id = category.getId();
		try {
			if (id == null || id == 0)
				baseService.save(category,getCurrentUser());
			else
				baseService.update(category,getCurrentUser());
			setSuccessMessage(getResourceBundle().getString("SAVED_SUCCESSFULLY"));
		} catch (Exception ex) {
			category.setId(id);
			setErrorMessage(ex,"Ce category existe deja");
		}

		
		clear();
		getAll();
		return "Success";
	}


	public String edit() {
		clearMessages();
		category = (Category) baseService.getById(Category.class, getIdParameter());
		return "Success";
	}
	
	@PostConstruct
	private void getAll() {
		categories = baseService.loadAll(Category.class, getCurrentUser().getSchool());
		setRowCount(new Long(categories.size()));

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

	public List<BaseEntity> getCategories() {
		return categories;
	}

	public void setCategories(List<BaseEntity> categories) {
		this.categories = categories;
	}

	public List<String> getAllCategories() {
		autoComplete("");
		return allCategories;
	}

	public void setAllCategories(List<String> allCategories) {
		this.allCategories = allCategories;
	}

	public Category getCategory() {
		return category;
	}

	public void setCategory(Category category) {
		this.category = category;
	}
}
