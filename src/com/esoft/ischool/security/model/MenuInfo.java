package com.esoft.ischool.security.model;

import java.io.Serializable;

public class MenuInfo implements Serializable {

	private Long id;

	private String menuParentName;

	private String name;

	public MenuInfo() {
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getMenuParentName() {
		return menuParentName;
	}

	public void setMenuParentName(String menuParentName) {
		this.menuParentName = menuParentName;
	}
	
	@Override
	public boolean equals(Object o) {
		
		if (this == o)
			return true;
		if (o == null)
			return false;
		if (!(o instanceof MenuInfo))
			return false;
		MenuInfo that = (MenuInfo) o;
		return getId().equals(that.getId()); 
	}
}
