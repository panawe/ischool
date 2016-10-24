package com.esoft.ischool.security.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.esoft.ischool.model.BaseEntity;

@Entity
@Table(name = "MENU")
public class Menu extends BaseEntity implements Comparable, Serializable {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "MENU_ID")
	private Long id;

	@ManyToOne
	@JoinColumn(name = "PARENT_MENU_ID", nullable = true)
	private Menu menuParent;

	// @NotNull
	// @Length(max = 50)
	private String name;

	@Column(name = "SECURITY_CODE")
	private Long securityCode;

	// @Length(max = 200)
	private String description;

	private String url;

	@Transient
	private Integer accessLevel;

	@Transient
	private Boolean accessLevelCheck;

	public Long getSecurityCode() {
		return securityCode;
	}

	public void setSecurityCode(Long securityCode) {
		this.securityCode = securityCode;
	}

	public Menu() {
	}

	public Menu(Menu menu, Menu menuP) {
		// TODO Auto-generated constructor stub
		this.menuParent = menuP;
		this.accessLevel = menu.getAccessLevel();
		this.securityCode = menu.getSecurityCode();
		this.accessLevelCheck = menu.getAccessLevelCheck();
		this.description = menu.getDescription();
		this.url = menu.getUrl();
		this.name = menu.getName();

	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Override
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public int compareTo(Object o) {

		if (o instanceof Menu) {
			return id == ((Menu) o).getId() ? 0 : (id > ((Menu) o).getId() ? 1 : -1);

		} else {
			return 1;
		}
	}

	@Override
	public boolean equals(Object o) {

		if (this == o)
			return true;
		if (o == null)
			return false;
		if (!(o instanceof Menu))
			return false;
		Menu that = (Menu) o;
		return getId().equals(that.getId());
	}

	@Override
	public String toString() {
		return id + " " + name + " " + (menuParent != null ? menuParent.getName() : "") + " " + accessLevelCheck;
	}

	public String getDescription() {
		return description;
	}

	public Menu getMenuParent() {
		return menuParent;
	}

	public String getUrl() {
		return url;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public void setMenuParent(Menu menuParent) {
		this.menuParent = menuParent;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public Integer getAccessLevel() {
		return accessLevel;
	}

	public void setAccessLevel(Integer accessLevel) {
		this.accessLevel = accessLevel;
	}

	public Boolean getAccessLevelCheck() {
		return accessLevelCheck;
	}

	public void setAccessLevelCheck(Boolean accessLevelCheck) {
		this.accessLevelCheck = accessLevelCheck;
	}
}
