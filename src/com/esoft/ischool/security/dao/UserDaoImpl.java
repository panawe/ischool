package com.esoft.ischool.security.dao;

import java.util.List;

import org.hibernate.Hibernate;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Repository;

import com.esoft.ischool.dao.BaseDaoImpl;
import com.esoft.ischool.model.AlertReceiver;
import com.esoft.ischool.model.BaseEntity;
import com.esoft.ischool.model.Question;
import com.esoft.ischool.model.School;
import com.esoft.ischool.model.UserTest;
import com.esoft.ischool.security.model.RolesUser;
import com.esoft.ischool.security.model.Menu;
import com.esoft.ischool.security.model.User;
import com.esoft.ischool.stocks.model.ProductConsumer;
import com.esoft.ischool.util.ProductConsumerStatus;


@Repository("userDao")
@Scope("session")
public class UserDaoImpl extends BaseDaoImpl {
	@Autowired
	public UserDaoImpl(SessionFactory sessionFactory) {
		setSessionFactory(sessionFactory);
	}

	public void deleteRolesUser(Long userId, Long groupId) {
		RolesUser gu = getRolesUserByUserAndRolesIds(
				userId, groupId);
		getHibernateTemplate().delete(gu);
	}

	public RolesUser getRolesUserByUserAndRolesIds(
			Long userId, Long groupId) {
		RolesUser gu = null;
		List list = getHibernateTemplate()
				.find(
						"from RolesUser gu where gu.user.numUser = ? and gu.group.numRoles = ?",
						new Object[] { userId, groupId });
		if (list.size() > 0)
			gu = (RolesUser) list.get(0);
		return gu;
	}

	public List<Long> getRolesUserListByUser(Long userId) {
		return getHibernateTemplate()
				.find(
						"select gu.group.numRoles from RolesUser gu where gu.user.numUser = ?",
						new Object[] { userId });
	}
	
	public  List<Menu> getSubMenus(Long parentId) {
		// TODO Auto-generated method stub
		 
		final String[] paramNames = { "parentId" };
		final Object[] paramValues = new Object[] { parentId };

		return getHibernateTemplate().findByNamedQueryAndNamedParam(
				"getKidsMenu", paramNames, paramValues);

	
	}


	public User getUser(String nom, String password) {

		User user = null;
		List list = getHibernateTemplate().find(
				"from User where userName = '" + nom
						+ "' and password='" + password + "'");
		if (list.size() > 0) {

			user = (User) list.get(0);

			for (RolesUser gu : user.getRolesUser()) {
				;
				Hibernate.initialize(gu.getRoles().getRolesMenus());
			}
		}

		return user;
	}

	@SuppressWarnings("unchecked")
	public List<UserTest> getUserTests(User user){
		
		List<UserTest> tests = null;
		final String[] paramNames = { "userId"};
		final Object[] paramValues = new Object[] {user.getId()};

		tests = getHibernateTemplate().findByNamedQueryAndNamedParam(
				"getUserTests", paramNames, paramValues);

		return tests;
		
	}
	
	@SuppressWarnings("unchecked")
	public List<Question> getQuestions(Long testId){
		
		List<Question> questions = null;
		final String[] paramNames = { "testId"};
		final Object[] paramValues = new Object[] {testId};

		questions = getHibernateTemplate().findByNamedQueryAndNamedParam(
				"getTestQuestions", paramNames, paramValues);

		return questions;
		
		
		
	}
	
	@SuppressWarnings("unchecked")
	public List<BaseEntity> getPendingQuestions(Long testId, Long userTestId){
		
		List<BaseEntity> testQuestions = null;
		final String[] paramNames = { "testId","userTestId"};
		final Object[] paramValues = new Object[] {testId,userTestId};

		testQuestions = getHibernateTemplate().findByNamedQueryAndNamedParam(
				"getPendingQuestions", paramNames, paramValues);

		return testQuestions;
		
		
		
	}

	public List<AlertReceiver> getUserAlerts(User user, School school) {
		// TODO Auto-generated method stub
		DetachedCriteria crit = DetachedCriteria.forClass(AlertReceiver.class);
		crit.createCriteria("user").add(Restrictions.eq("id", user.getId()));
		List l = getHibernateTemplate().findByCriteria(crit);
		return l;
	}

	public List<BaseEntity> getUnapprovedAssignments(User user) {
		List<BaseEntity> unApproved = null;
		final String[] paramNames = { "userId"};
		final Object[] paramValues = new Object[] {user.getId()};

		unApproved = getHibernateTemplate().findByNamedQueryAndNamedParam(
				"getUnapprovedExams", paramNames, paramValues);

		return unApproved;
		
	}

	public List<BaseEntity> getPendingDemandes(School school) {
		// TODO Auto-generated method stub
		DetachedCriteria crit = DetachedCriteria.forClass(ProductConsumer.class);
		crit.add(Restrictions.eq("status", ProductConsumerStatus.PENDING_APPROVAL.getValue()));
		crit.createCriteria("school").add(Restrictions.eq("id", school.getId()));
		return getHibernateTemplate().findByCriteria(crit);
	}

}
