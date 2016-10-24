package com.esoft.ischool.service;

import java.util.Date;
import java.util.List;

import org.springframework.transaction.annotation.Transactional;

import com.esoft.ischool.model.BaseEntity;
import com.esoft.ischool.model.Configuration;
import com.esoft.ischool.model.Event;
import com.esoft.ischool.security.model.Menu;
import com.esoft.ischool.security.model.Roles;
import com.esoft.ischool.security.model.User;
import com.esoft.ischool.security.model.YearlySummary;

@Transactional(readOnly = true)
public interface BaseService {
	@Transactional(readOnly = false)
	public void delete(Long id, Class cl);

	public BaseEntity getById(Class cl, Long id);

	public List getMenusFromRolesMenus(List<BaseEntity> rolesMenus);

	public List getUsersFromRolesUsers(List<BaseEntity> rolesUsers);

	public List<BaseEntity> getUsers(Class cl, String userName, String lastName, String firstName, User user);

	public List<BaseEntity> getMenus(Class cl, String menuName);

	@Transactional(readOnly = false)
	public void update(BaseEntity entity, User user);

	@Transactional(readOnly = false)
	public void save(BaseEntity entity, User user);

	public void save(BaseEntity entity);

	public BaseEntity load(BaseEntity entity);

	// public List<BaseEntity> loadAll(Class<?extends BaseEntity> cl);
	public List<BaseEntity> loadAll(Class<? extends BaseEntity> cl);

	public List<Menu> loadAllMenus();

	public List<User> loadAllUsers();

	public List<BaseEntity> loadAllByParentId(Class<? extends BaseEntity> c, String parentName, String parentProperty,
			Long parentPropertyValue);

	@Transactional(readOnly = false)
	public BaseEntity findByColumn(Class cl, String columnName, Integer columnValue);

	public BaseEntity findByColumn(Class cl, String columnName, String columnValue);

	public <T> List<BaseEntity> findByColumnValues(Class cl, String columnName, List<T> columnValue, Long schoolId);

	public BaseEntity findByName(Class cl, String name, String parentName, String parentProperty,
			String parentPropertyValue);

	public BaseEntity findByParents(Class cl, String firstParent, String firstParentName, String secondParent,
			String secondParentName);

	public BaseEntity findByParents(Class cl, String firstParent, Long firstParentId, String secondParent,
			Long secondParentId);

	public List<BaseEntity> loadByParentsIds(Class cl, String firstParent, Long firstParentId, String secondParent,
			Long secondParentId);

	public List<BaseEntity> loadByParentsIds(Class cl, String firstParent, Long firstParentId, String secondParent,
			Long secondParentId, String thirdParent, Long thirdParentId);

	@Transactional(readOnly = false)
	public void saveRoleMenus(Roles role, List<BaseEntity> selectedMenus, User user);

	@Transactional(readOnly = false)
	public void saveRoleUsers(Roles role, List<BaseEntity> selectedUsers, User user);

	public List<BaseEntity> findByColumns(Class cl, List<String> columnNames, List<String> columnValues);

	public List<BaseEntity> getEntityByPropertyComparison(Class cl, String propertyName1, String propertyName2);

	public List<BaseEntity> findByColumnsLike(Class cl, List<String> columnNames, List<String> columnValues);

	public void delete(BaseEntity entity);

	public List<YearlySummary> getYearlySmry();

	public List<BaseEntity> loadAllByColumn(Class cl, String columnName, Integer columnValue);
	
	public Configuration getConfig(String prop);
	public List<BaseEntity> loadAllByColumn(Class cl, String columnName, Long columnValue) ;
	

}
