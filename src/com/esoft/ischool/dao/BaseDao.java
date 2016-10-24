package com.esoft.ischool.dao;

import java.util.Date;
import java.util.List;
import java.util.Map;

import com.esoft.ischool.model.BaseEntity;
import com.esoft.ischool.model.Configuration;
import com.esoft.ischool.model.News;
import com.esoft.ischool.security.model.Menu;
import com.esoft.ischool.security.model.User;
import com.esoft.ischool.security.model.YearlySummary;

public interface BaseDao {

	public void delete(BaseEntity entity);

	public BaseEntity getById(Class cl, Long id);

	public void update(BaseEntity entity, User user);

	public void save(BaseEntity entity, User user);

	public BaseEntity load(BaseEntity entity);

	public List<BaseEntity> loadAll(Class<? extends BaseEntity> cl);

	public void deleteByParentIds(String cl, Map<String, Long> parentNameIds);

	public BaseEntity findByName(Class cl, String name, String parentName, String parentProperty,
			String parentPropertyValue);

	public List<BaseEntity> findByColumnsLike(Class cl, List<String> columnNames, List<String> columnValues);

	public List<BaseEntity> loadAllByParentId(Class<? extends BaseEntity> c, String parentName, String parentProperty,
			Long parentPropertyValue);

	public List<BaseEntity> findByParentsIds(Class<? extends BaseEntity> c, String firstParentName, Long firstParentId,
			String secondParentName, Long secondParentId);

	public List<BaseEntity> findByParentsIds(Class<? extends BaseEntity> c, String firstParentName, Long firstParentId,
			String secondParentName, Long secondParentId, String thirdParentName, Long thirdParentId);

	public BaseEntity findByColumn(Class cl, String columnName, Integer columnValue);

	public BaseEntity findByColumn(Class cl, String columnName, String columnValue);

	public List<BaseEntity> findByColumns(Class cl, List<String> columnNames, List<String> columnValues);

	public BaseEntity findByParents(Class cl, String firstParent, Long firstParentId, String secondParent,
			Long secondParentId);

	public List<Menu> loadAllMenus();

	public List<User> loadAllUsers();

	public List<BaseEntity> getUsers(Class cl, String userName, String lastName, String firstName, User user);

	public List<BaseEntity> getMenus(Class cl, String menuName);

	public List<News> getAllSortedNews();

	public BaseEntity findByParents(Class cl, String firstParent, String firstParentName, String secondParent,
			String secondParentName);

	public void save(BaseEntity entity);

	public List<YearlySummary> getYearlySmry();

	public List<BaseEntity> loadAllByColumn(Class cl, String columnName, Integer columnValue);

	public Configuration getConfig(String prop);
	public List<BaseEntity> loadAllByColumn(Class cl, String columnName, Long columnValue);

}
