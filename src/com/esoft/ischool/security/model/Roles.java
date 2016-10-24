package com.esoft.ischool.security.model;

import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import com.esoft.ischool.model.BaseEntity;

@Entity
@Table(name = "ROLES")
public class Roles extends BaseEntity {

	@Id
	@Column(name = "ROLE_ID")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(name = "NAME")
	private String name;

	@Column(name = "ROLE_CODE")
	private Integer roleCode;

	@Column(name = "DESCRIPTION")
	private String description;

	@OneToMany(mappedBy = "roles")
	// @JoinColumn(name = "ROLE_ID")
	@Fetch(FetchMode.SELECT)
	private Set<RolesMenu> rolesMenus;

	public Roles() {
	}

	public Roles(Roles role) {
		// TODO Auto-generated constructor stub
		this.name = role.getName();
		this.roleCode = role.getRoleCode();
		this.description = role.getDescription();
	}

	public Integer getRoleCode() {
		return roleCode;
	}

	public void setRoleCode(Integer roleCode) {
		this.roleCode = roleCode;
	}

	public Set<RolesMenu> getRolesMenus() {
		return rolesMenus;
	}

	public void setRolesMenus(Set<RolesMenu> rolesMenus) {
		this.rolesMenus = rolesMenus;
	}

	@Override
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	@Override
	public String toString() {
		return "Level [levelId=" + id + ", name=" + name + ", getCreateDate()=" + getCreateDate() + ", getModDate()="
				+ getModDate() + ", getModifiedBy()=" + getModifiedBy() + "]";
	}

}
