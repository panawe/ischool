package com.esoft.ischool.restservice;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.model.SelectItem;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.esoft.ischool.model.BaseEntity;
import com.esoft.ischool.security.model.Menu;
import com.esoft.ischool.security.model.User;
import com.esoft.ischool.service.BaseService;

@Component("menuBean")
@Scope("session")
public class MenuBean extends BaseBean {

	@Autowired
	@Qualifier("baseService")
	private BaseService baseService;
	private Long rowCount;

	private List<BaseEntity> menus;
	private List<String> allMenus;
	private Menu menu = new Menu();
	
	public List<String> autoComplete(Object suggest) {
		allMenus = new ArrayList<String>();
		for (BaseEntity entity : menus) {
			Menu m = (Menu) entity;
			if (menu.getName().equalsIgnoreCase(suggest.toString()))
				allMenus.add(m.getName());
		}
		return allMenus;
	}

	public String validate() {
		return "succes";
	}

	@Override
	public String clear() {
		Menu menuParent = new Menu();
		menu = new Menu();
		menu.setMenuParent(menuParent);
		
		return "Success";
	}

	public String getShowAll() {
		getAll();
		return "Success";
	}

	public String delete() {
		clearMessages();
		try {
			baseService.delete(getIdParameter(), Menu.class);
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
		Long id = menu.getId();
		if (menu.getMenuParent() != null)
			menu.getMenuParent().setId(menuParentId);
		
		try {
			if (id == null || id == 0)
				baseService.save(menu, getCurrentUser());
			else
				baseService.update(menu, getCurrentUser());

			setSuccessMessage(getResourceBundle().getString("SAVED_SUCCESSFULLY"));
			getAll();
		} catch (Exception ex) {
			ex.printStackTrace();
			menu.setId(id);
			setErrorMessage(ex, "Ce menu exist deja. ");
		}

		return "S";
	}
	
	public String edit() {
		clearMessages();
		menu = (Menu) baseService.getById(Menu.class, getIdParameter());
		menuParentId = menu.getMenuParent() != null ? menu.getMenuParent().getId() : null;
		return "Success";
	}
	
	@PostConstruct
	private void getAll() {
		User user = getCurrentUser();
		if (user!=null&&user.getId().equals(1L)){
			menus = baseService.loadAll(Menu.class, getCurrentUser().getSchool());
		}else{
			//remove super user menu id 29 and 30
			List<BaseEntity> allMenus = baseService.loadAll(Menu.class, getCurrentUser().getSchool());
			menus = new ArrayList<BaseEntity>();
			for(BaseEntity be:allMenus){
				if((((Menu)be)).getSecurityCode().intValue()==29 ||((Menu)be).getSecurityCode().intValue()==30){
					//don't show it
				}else{
					menus.add(be);
				}
			}
		}
		setRowCount(new Long(menus.size()));
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

	public List<SelectItem> getParentMenus() {
		getAll();
		List<SelectItem> selecteds = new ArrayList<SelectItem>();
		selecteds.add(new SelectItem("", ""));
		
		for (BaseEntity entity : menus) {
			Menu menu = (Menu)entity;
			if (menu.getMenuParent() == null)
				selecteds.add(new SelectItem(menu.getId(), menu.getName()));
		}

		return selecteds;
	}	
	
	private Long menuParentId;
	
	public Long getMenuParentId() {
		return menuParentId;
	}

	public void setMenuParentId(Long menuParentId) {
		this.menuParentId = menuParentId;
	}

	public String doNothing() {
		return "Success";
	}

	public List<BaseEntity> getMenus() {
		return menus;
	}

	public void setMenus(List<BaseEntity> menus) {
		this.menus = menus;
	}

	public List<String> getAllMenus() {
		return allMenus;
	}

	public void setAllMenus(List<String> allMenus) {
		this.allMenus = allMenus;
	}

	public Menu getMenu() {
		return menu;
	}

	public void setMenu(Menu menu) {
		this.menu = menu;
	}
}