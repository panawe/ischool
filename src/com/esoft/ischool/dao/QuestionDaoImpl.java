package com.esoft.ischool.dao;

import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Repository;

@Repository("questionDao")
@Scope("session")
public class QuestionDaoImpl extends BaseDaoImpl  {

	@Autowired
	public QuestionDaoImpl(SessionFactory sessionFactory) {
		setSessionFactory(sessionFactory);
	}
}
