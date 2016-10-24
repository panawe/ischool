package com.esoft.ischool.dao;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.hibernate.Hibernate;
import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.orm.hibernate5.support.HibernateDaoSupport;
import org.springframework.stereotype.Repository;

import com.esoft.ischool.model.BaseEntity;
import com.esoft.ischool.model.Configuration;
import com.esoft.ischool.model.News;
import com.esoft.ischool.security.model.Contribution;
import com.esoft.ischool.security.model.Menu;
import com.esoft.ischool.security.model.User;
import com.esoft.ischool.security.model.YearlySummary;

@Repository("baseDao")
@Scope("singleton")
public class BaseDaoImpl<T extends BaseEntity> extends HibernateDaoSupport implements BaseDao {

	public BaseDaoImpl() {

	}

	@Autowired
	public BaseDaoImpl(SessionFactory sessionFactory) {
		setSessionFactory(sessionFactory);
	}

	public void delete(BaseEntity entity) {
		getHibernateTemplate().delete(entity);
	}

	public BaseEntity getById(Class cl, final Long id) {
		return (BaseEntity) getHibernateTemplate().get(cl, id);
	}

	public void update(BaseEntity entity, User user) {
		entity.setModDate(new Date());
		// entity.setSchool(user.getSchool());
		entity.setModifiedBy(user.getId());
		getHibernateTemplate().update(entity);
	}

	public void save(BaseEntity entity, User user) {
		entity.setCreateDate(new Date());
		entity.setModDate(new Date());
		// entity.setSchool(user.getSchool());
		entity.setModifiedBy(user.getId());
		getHibernateTemplate().save(entity);
	}

	public void save(BaseEntity entity) {

		entity.setModDate(new Date());
		// entity.setSchool(user.getSchool());
		entity.setModifiedBy(1L);

		if (entity.getId() == null) {
			entity.setCreateDate(new Date());
			getHibernateTemplate().save(entity);
		} else {
			getHibernateTemplate().update(entity);
		}
	}

	public BaseEntity load(BaseEntity entity) {
		getHibernateTemplate().load(entity, entity.getId());
		return entity;
	}

	@SuppressWarnings("unchecked")
	public List<BaseEntity> loadAll(Class<? extends BaseEntity> cl) {
		return (List<BaseEntity>) getHibernateTemplate().loadAll(cl);
	}

	public BaseEntity findByName(Class cl, String name, String parentName, String parentProperty,
			String parentPropertyValue) {
		BaseEntity entity = null;
		DetachedCriteria crit = DetachedCriteria.forClass(cl);
		crit.add(Restrictions.eq("name", name)).createCriteria(parentName)
				.add(Restrictions.eq(parentProperty, parentPropertyValue));
		// crit.add(Restrictions.eq(parentName, parentValue));

		List l = getHibernateTemplate().findByCriteria(crit);

		if (l.size() > 0)
			entity = (BaseEntity) l.get(0);

		return entity;
	}

	public BaseEntity findByParents(Class cl, String firstParent, String firstParentName, String secondParent,
			String secondParentName) {

		BaseEntity entity = null;
		DetachedCriteria crit = DetachedCriteria.forClass(cl);

		if (firstParent != null)
			crit.createCriteria(firstParent).add(Restrictions.eq("name", firstParentName));

		if (secondParent != null)
			crit.createCriteria(secondParent).add(Restrictions.eq("name", secondParentName));

		List l = getHibernateTemplate().findByCriteria(crit);

		if (l.size() > 0)
			entity = (BaseEntity) l.get(0);

		return entity;

	}

	public BaseEntity findByParents(Class cl, String firstParent, Long firstParentId, String secondParent,
			Long secondParentId) {

		BaseEntity entity = null;
		DetachedCriteria crit = DetachedCriteria.forClass(cl);

		if (firstParent != null)
			crit.createCriteria(firstParent).add(Restrictions.eq("id", firstParentId));

		if (secondParent != null)
			crit.createCriteria(secondParent).add(Restrictions.eq("id", secondParentId));

		List l = getHibernateTemplate().findByCriteria(crit);

		if (l.size() > 0)
			entity = (BaseEntity) l.get(0);

		return entity;

	}

	public void deleteByParentIds(String cl, Map<String, Long> parentNameIds) {
		String sql = "DELETE FROM " + cl + " WHERE (1 = 1) ";

		for (String name : parentNameIds.keySet()) {
			sql += " AND " + name + ".id = " + parentNameIds.get(name);
		}

		Query query = getHibernateTemplate().getSessionFactory().getCurrentSession().createQuery(sql);
		int rowCount = query.executeUpdate();
	}

	public int executeDeleteQuery(String sqlQuery) {
		Query query = getHibernateTemplate().getSessionFactory().getCurrentSession().createQuery(sqlQuery);
		return query.executeUpdate();
	}

	public int deleteExamMark(Long examId) {
		String sql = "DELETE FROM Mark m WHERE m.exam.id = " + examId;
		Query query = getHibernateTemplate().getSessionFactory().getCurrentSession().createQuery(sql);
		return query.executeUpdate();
	}

	public List<BaseEntity> findByParentsIds(Class<? extends BaseEntity> c, String firstParentName, Long firstParentId,
			String secondParentName, Long secondParentId) {
		BaseEntity entity = null;
		DetachedCriteria crit = DetachedCriteria.forClass(c);

		if (firstParentName != null)
			crit.createCriteria(firstParentName).add(Restrictions.eq("id", firstParentId));
		if (secondParentName != null)
			crit.createCriteria(secondParentName).add(Restrictions.eq("id", secondParentId));

		List l = getHibernateTemplate().findByCriteria(crit);

		return l;
	}

	public List<BaseEntity> findByParentsIds(Class<? extends BaseEntity> c, String firstParentName, Long firstParentId,
			String secondParentName, Long secondParentId, String thirdParentName, Long thirdParentId) {
		BaseEntity entity = null;
		DetachedCriteria crit = DetachedCriteria.forClass(c);
		crit.createCriteria(firstParentName).add(Restrictions.eq("id", firstParentId));
		crit.createCriteria(secondParentName).add(Restrictions.eq("id", secondParentId));
		crit.createCriteria(thirdParentName).add(Restrictions.eq("id", thirdParentId));

		List l = getHibernateTemplate().findByCriteria(crit);

		return l;
	}

	public BaseEntity findByColumn(Class cl, String columnName, String columnValue) {
		BaseEntity entity = null;
		DetachedCriteria crit = DetachedCriteria.forClass(cl);
		crit.add(Restrictions.eq(columnName, columnValue));
		List l = getHibernateTemplate().findByCriteria(crit);

		if (l.size() > 0)
			entity = (BaseEntity) l.get(0);

		return entity;
	}

	public List<BaseEntity> findByColumns(Class cl, List<String> columnNames, List<String> columnValues) {
		BaseEntity entity = null;
		DetachedCriteria crit = DetachedCriteria.forClass(cl);
		int i = 0;
		for (String columnName : columnNames) {
			crit.add(Restrictions.eq(columnName, columnValues.get(i)));
			i++;
		}

		List l = getHibernateTemplate().findByCriteria(crit);
		return l;
	}

	public List<BaseEntity> findByColumnsLike(Class cl, List<String> columnNames, List<String> columnValues) {
		BaseEntity entity = null;
		DetachedCriteria crit = DetachedCriteria.forClass(cl);
		int i = 0;
		for (String columnName : columnNames) {
			crit.add(Restrictions.like(columnName, columnValues.get(i)));
			i++;
		}

		List l = getHibernateTemplate().findByCriteria(crit);
		return l;
	}

	public List<BaseEntity> loadAllByParentId(Class<? extends BaseEntity> c, String parentName, String parentProperty,
			Long parentPropertyValue) {
		DetachedCriteria crit = DetachedCriteria.forClass(c);
		crit.createCriteria(parentName).add(Restrictions.eq(parentProperty, parentPropertyValue));

		List l = getHibernateTemplate().findByCriteria(crit);

		return l;
	}

	public List<BaseEntity> loadEntitiesByParentAndDateRange(Class<? extends BaseEntity> c, String parentName,
			String parentProperty, Long parentPropertyValue, String dateColumn, Date beginDate, Date endDate) {
		DetachedCriteria crit = DetachedCriteria.forClass(c);
		crit.createCriteria(parentName).add(Restrictions.eq(parentProperty, parentPropertyValue));
		if (beginDate != null)
			crit.add(Restrictions.ge(dateColumn, beginDate));
		if (endDate != null)
			crit.add(Restrictions.le(dateColumn, endDate));

		crit.addOrder(Order.desc(dateColumn));
		List l = getHibernateTemplate().findByCriteria(crit);
		return l;
	}

	public List<BaseEntity> loadEntitiesByPropertyAndDateRange(Class<? extends BaseEntity> c, String property,
			String propertyValue, String dateColumn, Date beginDate, Date endDate) {
		DetachedCriteria crit = DetachedCriteria.forClass(c);
		crit.add(Restrictions.eq(property, propertyValue));
		if (beginDate != null)
			crit.add(Restrictions.ge(dateColumn, beginDate));
		if (endDate != null)
			crit.add(Restrictions.le(dateColumn, endDate));

		crit.addOrder(Order.desc(dateColumn));
		List l = getHibernateTemplate().findByCriteria(crit);
		return l;
	}

	@Override
	public BaseEntity findByColumn(Class cl, String columnName, Integer columnValue) {
		BaseEntity entity = null;
		DetachedCriteria crit = DetachedCriteria.forClass(cl);
		crit.add(Restrictions.eq(columnName, new Long(columnValue)));
		List l = getHibernateTemplate().findByCriteria(crit);

		if (l.size() > 0)
			entity = (BaseEntity) l.get(0);

		return entity;
	}
	
	@Override
	public List<BaseEntity> loadAllByColumn(Class cl, String columnName, Integer columnValue) {
		BaseEntity entity = null;
		DetachedCriteria crit = DetachedCriteria.forClass(cl);
		crit.add(Restrictions.eq(columnName, columnValue.shortValue()));
		List l = getHibernateTemplate().findByCriteria(crit);

		return l;
	}
	
	@Override
	public List<BaseEntity> loadAllByColumn(Class cl, String columnName, Long columnValue) {
		BaseEntity entity = null;
		DetachedCriteria crit = DetachedCriteria.forClass(cl);
		crit.add(Restrictions.eq(columnName, columnValue));
		List l = getHibernateTemplate().findByCriteria(crit);

		return l;
	}

	@Override
	public List<Menu> loadAllMenus() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<User> loadAllUsers() {
		// TODO Auto-generated method stub

		DetachedCriteria crit = DetachedCriteria.forClass(User.class);
		crit.add(Restrictions.gt("id", 1L));
		List l = getHibernateTemplate().findByCriteria(crit);

		return l;
	}

	@Override
	public List<BaseEntity> getUsers(Class cl, String userName, String lastName, String firstName, User user) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<BaseEntity> getMenus(Class cl, String menuName) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<News> getAllSortedNews() {
		// TODO Auto-generated method stub
		return null;
	}

	public List<YearlySummary> getYearlySmry() {

		String sqlQuery = "SELECT YEAR_PAID, SUM(CASE IO WHEN  0 THEN 0 ELSE  AMOUNT END ) INN,SUM(CASE IO WHEN 1 THEN 0 ELSE AMOUNT END ) OUTT FROM TRANSACTION GROUP BY YEAR_PAID ORDER BY YEAR_PAID";

		// int parameterIndex = 0;
		Session session = getHibernateTemplate().getSessionFactory().openSession();
		Query query = session.createSQLQuery(sqlQuery);
		// query.setParameter(parameterIndex, year);

		List<Object[]> objects = query.list();

		List<YearlySummary> yss = new ArrayList<YearlySummary>();
		YearlySummary ys;
		for (Object[] obj : objects) {
			ys = new YearlySummary();
			ys.setYear((Integer) obj[0]);
			ys.setIn(new Double(obj[1].toString()));
			ys.setOut(new Double(obj[2].toString()));

			yss.add(ys);
		}

		return yss;
	}

	public List<Contribution> getContributions() {

		String sqlQuery = "SELECT IF (T.FIRST_NAME IS NOT NULL,CONCAT_WS(' ',T.FIRST_NAME, T.LAST_NAME), CONCAT_WS(' ',U.FIRST_NAME, U.LAST_NAME)) MEMBER, SUM(AMOUNT) AMT FROM TRANSACTION T, USERS U WHERE U.USER_ID=T.USER_ID AND IO=1 GROUP BY IF (T.FIRST_NAME IS NOT NULL,CONCAT_WS(' ',T.FIRST_NAME, T.LAST_NAME), CONCAT_WS(' ',U.FIRST_NAME, U.LAST_NAME)) ORDER BY AMT DESC";

		// int parameterIndex = 0;
		Session session = getHibernateTemplate().getSessionFactory().openSession();
		Query query = session.createSQLQuery(sqlQuery);
		// query.setParameter(parameterIndex, year);

		List<Object[]> objects = query.list();

		List<Contribution> yss = new ArrayList<Contribution>();
		Contribution ys;
		for (Object[] obj : objects) {
			ys = new Contribution();
			ys.setMember((String) obj[0]);
			ys.setAmount(new Double(obj[1].toString()));
			yss.add(ys);
		}

		return yss;
	}

	@Override
	public Configuration getConfig(String prop) {
		// TODO Auto-generated method stub
		return  (Configuration) findByColumn(Configuration.class,"name",prop);
	}

}
