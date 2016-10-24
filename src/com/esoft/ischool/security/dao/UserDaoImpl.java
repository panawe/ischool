package com.esoft.ischool.security.dao;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.hibernate.Hibernate;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.orm.hibernate5.HibernateCallback;
import org.springframework.stereotype.Repository;

import com.esoft.ischool.dao.BaseDaoImpl;
import com.esoft.ischool.model.BaseEntity;
import com.esoft.ischool.model.BudgetCash;
import com.esoft.ischool.model.Configuration;
import com.esoft.ischool.model.Transaction;
import com.esoft.ischool.security.model.RolesUser;
import com.esoft.ischool.security.model.Contribution;
import com.esoft.ischool.security.model.Menu;
import com.esoft.ischool.security.model.User;

@Repository("userDao")
@Scope("singleton")
public class UserDaoImpl extends BaseDaoImpl {
	@Autowired
	public UserDaoImpl(SessionFactory sessionFactory) {
		setSessionFactory(sessionFactory);
	}

	public void deleteRolesUser(Long userId, Long groupId) {
		RolesUser gu = getRolesUserByUserAndRolesIds(userId, groupId);
		getHibernateTemplate().delete(gu);
	}

	public RolesUser getRolesUserByUserAndRolesIds(Long userId, Long groupId) {
		RolesUser gu = null;
		List list = getHibernateTemplate().find("from RolesUser gu where gu.user.numUser = ? and gu.group.numRoles = ?",
				new Object[] { userId, groupId });
		if (list.size() > 0)
			gu = (RolesUser) list.get(0);
		return gu;
	}

	public List<Long> getRolesUserListByUser(Long userId) {
		return null;
		/*
		 * return (List<Long>) getHibernateTemplate() .find(
		 * "select gu.group.numRoles from RolesUser gu where gu.user.numUser = ?"
		 * , new Object[] { userId });
		 */
	}

	public List<Menu> getSubMenus(Long parentId) {
		// TODO Auto-generated method stub
		/**
		 * final String[] paramNames = { "parentId" }; final Object[]
		 * paramValues = new Object[] { parentId };
		 * 
		 * return (List<Menu>)
		 * getHibernateTemplate().findByNamedQueryAndNamedParam( "getKidsMenu",
		 * paramNames, paramValues);
		 * 
		 */
		return null;
	}

	public User getUser(String nom, String password) {

		User user = null;
		List list = getHibernateTemplate()
				.find("from User where userName = '" + nom + "' and password='" + password + "'");
		if (list.size() > 0) {

			user = (User) list.get(0);
			
			Configuration be= (Configuration) findByColumn(Configuration.class,"name","ANNUAL_FEE");
			Configuration be2= (Configuration) findByColumn(Configuration.class,"name","PAY_ONLINE");
			
			if(user.getMembershipRenewDate()==null||user.getMembershipRenewDate().before(new Date())){				 
				 if(!be2.getValue().trim().equals("1")){ 
					 user.setFee(0.0);
				 }else{
					 user.setFee(new Double(be.getValue()));
					 user.setStatus((short) 0);
				 }
			}
		}

		return user;
	}

	public List<User> loadAllMembers() {

		DetachedCriteria crit = DetachedCriteria.forClass(User.class);
		crit.add(Restrictions.eq("status", (short) 1));
		List l = getHibernateTemplate().findByCriteria(crit);

		return l;
	}
	
	public List<User> loadAllUsersWithOnlineStatus() {
		final String sql = "SELECT DISTINCT  U.USER_ID, U.USER_NAME, U.PASSWORD, U.FIRST_NAME, U.LAST_NAME FROM USERS U INNER JOIN SESSION_HISTORY SH ON "
				+ "SH.USER_ID = U.USER_ID WHERE SH.END_DATE IS NULL AND SH.BEGIN_DATE > "
				+ "DATE_SUB(CURDATE(), INTERVAL 1 DAY) ORDER BY U.FIRST_NAME, U.LAST_NAME ";
		
		Session session = getHibernateTemplate().getSessionFactory().openSession();
		Query query = session.createSQLQuery(sql);

		List<Object[]> objects = query.list();

		List<User> users = new ArrayList<User>();
		
		if(objects!=null){
			for(Object[] row : objects){
				User user = new User();
				user.setId(((BigInteger) row[0]).longValue());
				user.setUserName((String) row[1]);
				user.setPassword((String) row[2]);
				user.setFirstName((String) row[3]);
				try{
				user.setLastName(((String) row[4]).substring(0, 1));
				}catch (Exception e){
					user.setLastName("");
				}
				user.setOnline(true);
				
				users.add(user);
			}
		}
				
		return users;
	}

	public List<User> loadAllMembersPending() {
		DetachedCriteria crit = DetachedCriteria.forClass(User.class);
		crit.add(Restrictions.eq("status", (short) 0));
		List l = getHibernateTemplate().findByCriteria(crit);
		return l;
	}

	public List<User> findMembers(String searchText) {

		if (searchText == null) {
			return null;
		} else {
			DetachedCriteria crit = DetachedCriteria.forClass(User.class);
			String ab[] = searchText.split(" ");
			if (ab.length == 1) {
				Criterion c1 = Restrictions.like("firstName", "%" + searchText.toLowerCase() + "%").ignoreCase();
				Criterion c2 = Restrictions.like("lastName", "%" + searchText.toLowerCase() + "%").ignoreCase();
				//Criterion c3 = Restrictions.eq("status", (short) 1);
				crit.add(Restrictions.or(c1, c2));
				//crit.add(Restrictions.and(c3));
				return (List<User>) getHibernateTemplate().findByCriteria(crit);
			} else {// take first 2
				Criterion c1 = Restrictions.like("firstName", "%" + ab[0].toLowerCase() + "%").ignoreCase();
				Criterion c2 = Restrictions.like("lastName", "%" + ab[1].toLowerCase() + "%").ignoreCase();
				Criterion c3 = Restrictions.like("firstName", "%" + ab[1].toLowerCase() + "%").ignoreCase();
				Criterion c4 = Restrictions.like("lastName", "%" + ab[0].toLowerCase() + "%").ignoreCase();
				//Criterion c5 = Restrictions.eq("status", (short) 1);
				crit.add(Restrictions.or(c1, c2, c3, c4));
				//crit.add(Restrictions.and(c5));
				return (List<User>) getHibernateTemplate().findByCriteria(crit);
			}

		}
	}
	
	public List<User> findMembers(String firstName, String lastName, String login, String email) {

 
			DetachedCriteria crit = DetachedCriteria.forClass(User.class);
	 
				Criterion c1 = Restrictions.eq("firstName", firstName==null?"#":firstName).ignoreCase();
				Criterion c2 = Restrictions.eq("lastName", lastName==null?"#":lastName).ignoreCase();
				Criterion c3 = Restrictions.eq("userName", login==null?"#":login).ignoreCase();
				Criterion c4 = Restrictions.eq("email", email==null?"#":email).ignoreCase();
				//Criterion c3 = Restrictions.eq("status", (short) 1);
				crit.add(Restrictions.and(c1, c2));
				crit.add(Restrictions.or(c3,c4));
				return (List<User>) getHibernateTemplate().findByCriteria(crit);

	}

	public List<User> getLeaders() {
		DetachedCriteria crit = DetachedCriteria.forClass(User.class);
		crit.createCriteria("position").add(Restrictions.gt("id", 1L));
		crit.addOrder(Order.asc("position.id"));
		List l = getHibernateTemplate().findByCriteria(crit);
		return l;
	}

	public List<Transaction> getAllExpenses() {
		DetachedCriteria crit = DetachedCriteria.forClass(Transaction.class);
		crit.add(Restrictions.eq("io", (short) 0));
		crit.addOrder(Order.desc("createDate"));
		List l = getHibernateTemplate().findByCriteria(crit);
		return l;
	}

	public List<BudgetCash> getBudgetCash() {
		// TODO Auto-generated method stub
		String sqlQuery = "SELECT P.TITLE, MAX(P.BUDGET), SUM(IF(AMOUNT IS NULL,0,AMOUNT-IF(FEE IS NULL,0,FEE))) AMT FROM PROJECT P LEFT OUTER JOIN TRANSACTION T ON (P.PROJECT_ID=T.PROJECT_ID) GROUP BY P.TITLE";

		// int parameterIndex = 0;
		Session session = getHibernateTemplate().getSessionFactory().openSession();
		Query query = session.createSQLQuery(sqlQuery);
		// query.setParameter(parameterIndex, year);

		List<Object[]> objects = query.list();

		List<BudgetCash> yss = new ArrayList<BudgetCash>();
		BudgetCash ys;
		for (Object[] obj : objects) {
			ys = new BudgetCash();
			ys.setProject((String) obj[0]);
			ys.setBudget(new Double(obj[1].toString()));
			ys.setCash(new Double(obj[2].toString()));
			yss.add(ys);
		}

		return yss;
	}
}
