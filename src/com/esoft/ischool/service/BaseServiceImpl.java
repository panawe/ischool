package com.esoft.ischool.service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.hibernate.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.esoft.ischool.dao.BaseDao;
import com.esoft.ischool.model.BaseEntity;
import com.esoft.ischool.model.Configuration;
import com.esoft.ischool.model.News;
import com.esoft.ischool.security.model.Menu;
import com.esoft.ischool.security.model.Roles;
import com.esoft.ischool.security.model.RolesMenu;
import com.esoft.ischool.security.model.RolesUser;
import com.esoft.ischool.security.model.User;
import com.esoft.ischool.security.model.YearlySummary;

@Service("baseService")
// @Scope("singleton")
public class BaseServiceImpl implements BaseService {

	@Autowired
	@Qualifier("baseDao")
	private BaseDao baseDao;

	public BaseEntity getById(Class cl, Long id) {
		return baseDao.getById(cl, id);
	}

	@Transactional(readOnly = false)
	public void update(BaseEntity entity, User user) {
		baseDao.update(entity, user);
	}

	@Transactional(readOnly = false)
	public void save(BaseEntity entity, User user) {
		baseDao.save(entity, user);
	}

	@Transactional(readOnly = false)
	public void save(BaseEntity entity) {
		baseDao.save(entity);
	}

	public BaseEntity load(BaseEntity entity) {
		return baseDao.load(entity);

	}

	public List<BaseEntity> loadAll(Class<? extends BaseEntity> cl) {
		return baseDao.loadAll(cl);
	}

	public BaseEntity findByColumn(Class cl, String columnName, Integer columnValue) {
		return baseDao.findByColumn(cl, columnName, columnValue);
	}

	public BaseEntity findByColumn(Class cl, String columnName, String columnValue) {
		return baseDao.findByColumn(cl, columnName, columnValue);
	}

	public List<BaseEntity> findByColumns(Class cl, List<String> columnNames, List<String> columnValues) {
		return baseDao.findByColumns(cl, columnNames, columnValues);
	}

	public List<BaseEntity> findByColumnsLike(Class cl, List<String> columnNames, List<String> columnValues) {
		return baseDao.findByColumnsLike(cl, columnNames, columnValues);
	}

	public BaseEntity findByName(Class cl, String nom, String parentName, String parentProperty,
			String parentPropertyValue) {
		return baseDao.findByName(cl, nom, parentName, parentProperty, parentPropertyValue);
	}

	public List<BaseEntity> loadAllByParentId(Class<? extends BaseEntity> c, String parentName, String parentProperty,
			Long parentPropertyValue) {
		return baseDao.loadAllByParentId(c, parentName, parentProperty, parentPropertyValue);
	}

	public void saveRoleMenus(final Roles role, List<BaseEntity> selectedMenus, User user) {

		baseDao.deleteByParentIds("RolesMenu", new HashMap<String, Long>() {
			{
				put("roles", role.getId());
			}
		});

		for (BaseEntity entity : selectedMenus) {
			Menu menu = (Menu) entity;
			RolesMenu rolesMenu = new RolesMenu();
			rolesMenu.setRoles(role);
			rolesMenu.setMenu(menu);
			rolesMenu.setCreateDate(new Date());
			rolesMenu.setModDate(new Date());
			rolesMenu.setAccessLevel(menu.getAccessLevelCheck() ? 1 : 0);

			baseDao.save(rolesMenu, user);
		}
	}

	public void saveRoleUsers(final Roles role, List<BaseEntity> selectedUsers, User u) {

		baseDao.deleteByParentIds("RolesUser", new HashMap<String, Long>() {
			{
				put("roles", role.getId());
			}
		});

		for (BaseEntity entity : selectedUsers) {
			User user = (User) entity;
			RolesUser rolesUser = new RolesUser();
			rolesUser.setRoles(role);
			rolesUser.setUser(user);
			rolesUser.setCreateDate(new Date());
			rolesUser.setModDate(new Date());
			baseDao.save(rolesUser, u);
		}
	}

	public BaseEntity findByParents(Class cl, String firstParent, String firstParentName, String secondParent,
			String secondParentName) {
		return baseDao.findByParents(cl, firstParent, firstParentName, secondParent, secondParentName);
	}

	public List<BaseEntity> loadByParentsIds(Class cl, String firstParentName, Long firstParentId,
			String secondParentName, Long secondParentId) {
		return baseDao.findByParentsIds(cl, firstParentName, firstParentId, secondParentName, secondParentId);
	}

	public List<BaseEntity> loadByParentsIds(Class cl, String firstParentName, Long firstParentId,
			String secondParentName, Long secondParentId, String thirdParentName, Long thirdParentId) {
		return baseDao.findByParentsIds(cl, firstParentName, firstParentId, secondParentName, secondParentId,
				thirdParentName, thirdParentId);
	}

	public BaseEntity findByParents(Class cl, String firstParent, Long firstParentId, String secondParent,
			Long secondParentId) {
		return baseDao.findByParents(cl, firstParent, firstParentId, secondParent, secondParentId);
	}

	public List<Menu> loadAllMenus() {
		return baseDao.loadAllMenus();
	}

	public List<User> loadAllUsers() {
		return baseDao.loadAllUsers();
	}

	public List getMenusFromRolesMenus(List<BaseEntity> rolesMenus) {
		ArrayList<Menu> selectedMenus = new ArrayList<Menu>();
		for (BaseEntity entity : rolesMenus) {
			RolesMenu rolesMenu = (RolesMenu) entity;
			Menu m = rolesMenu.getMenu();
			m.setAccessLevelCheck(rolesMenu.getAccessLevel().intValue() == 1 ? true : false);
			selectedMenus.add(m);
		}

		return selectedMenus;
	}

	public List getUsersFromRolesUsers(List<BaseEntity> rolesUsers) {
		ArrayList<User> selectedUsers = new ArrayList<User>();
		for (BaseEntity entity : rolesUsers) {
			RolesUser rolesUser = (RolesUser) entity;
			User u = rolesUser.getUser();
			selectedUsers.add(u);
		}

		return selectedUsers;
	}

	public List<BaseEntity> getUsers(Class cl, String userName, String lastName, String firstName, User user) {
		return baseDao.getUsers(cl, userName, lastName, firstName, user);
	}

	public List<BaseEntity> getMenus(Class cl, String menuName) {
		return baseDao.getMenus(cl, menuName);
	}

	public List<News> getAllSortedNews() {
		// TODO Auto-generated method stub
		return baseDao.getAllSortedNews();
	}

	@Override
	public void delete(Long id, Class cl) {
		// TODO Auto-generated method stub

	}

	@Transactional(readOnly = false)
	public void delete(BaseEntity entity) {
		baseDao.delete(entity);
	}

	@Override
	public <T> List<BaseEntity> findByColumnValues(Class cl, String columnName, List<T> columnValue, Long schoolId) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<BaseEntity> getEntityByPropertyComparison(Class cl, String propertyName1, String propertyName2) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<YearlySummary> getYearlySmry() {
		// TODO Auto-generated method stub
		return baseDao.getYearlySmry();
	}

	@Override
	public List<BaseEntity> loadAllByColumn(Class cl, String columnName, Integer columnValue) {
		return baseDao.loadAllByColumn(cl, columnName, columnValue);
	}
	
	@Override
	public List<BaseEntity> loadAllByColumn(Class cl, String columnName, Long columnValue) {
		return baseDao.loadAllByColumn(cl, columnName, columnValue);
	}

	@Override
	public Configuration getConfig(String prop) {
		// TODO Auto-generated method stub
		return baseDao.getConfig(prop);
	}
}
