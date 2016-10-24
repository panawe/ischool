package com.esoft.ischool.security.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.esoft.ischool.model.BaseEntity;

@Entity
@Table(name = "ROLES_MENU")
public class RolesMenu extends BaseEntity {

	@Id
	@Column(name = "ROLE_MENU_ID")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@ManyToOne
	@JoinColumn(name = "ROLE_ID")
	private Roles roles;

	@ManyToOne
	@JoinColumn(name = "MENU_ID")
	private Menu menu;

	@Column(name = "ACCESS_LEVEL")
	private Integer accessLevel;

	public RolesMenu() {
	}

	@Override
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Roles getRoles() {
		return roles;
	}

	public void setRoles(Roles roles) {
		this.roles = roles;
	}

	public Menu getMenu() {
		return menu;
	}

	public void setMenu(Menu menu) {
		this.menu = menu;
	}

	public Integer getAccessLevel() {
		return accessLevel;
	}

	public void setAccessLevel(Integer accessLevel) {
		this.accessLevel = accessLevel;
	}
}
