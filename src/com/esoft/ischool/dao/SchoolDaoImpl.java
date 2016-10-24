package com.esoft.ischool.dao;
import java.util.List;

import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Repository;
import com.esoft.ischool.model.School;

@Repository("schoolDao")
@Scope("session")
public class SchoolDaoImpl extends BaseDaoImpl{

	@Autowired
	public SchoolDaoImpl(SessionFactory sessionFactory) {
		setSessionFactory(sessionFactory);
	}
	public void updateSchool(School school) {
		getHibernateTemplate().update(school);
	}

	public void saveSchool(School school) {
		getHibernateTemplate().save(school);
	}

	public School getBySchoolId(Class<School> school, Long idParameter) {
		// TODO Auto-generated method stub
		return  (School) getHibernateTemplate().get(school, idParameter);
	}

	public List<School> loadAllSchool(Class<School> school) {
		// TODO Auto-generated method stub
		 return getHibernateTemplate().loadAll(school);
	}

}
