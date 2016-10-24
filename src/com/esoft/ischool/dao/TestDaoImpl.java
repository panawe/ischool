package com.esoft.ischool.dao;

import java.util.List;

import org.hibernate.SessionFactory;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Repository;

import com.esoft.ischool.model.BaseEntity;
import com.esoft.ischool.model.Level;
import com.esoft.ischool.model.Question;
import com.esoft.ischool.model.Student;
import com.esoft.ischool.model.Subject;
import com.esoft.ischool.model.Test;
import com.esoft.ischool.model.TestQuestion;
import com.esoft.ischool.model.UserTest;

@Repository("testDao")
@Scope("session")
public class TestDaoImpl extends BaseDaoImpl {

	@Autowired
	public TestDaoImpl(SessionFactory sessionFactory) {
		setSessionFactory(sessionFactory);
	}

	@SuppressWarnings("unchecked")
	public List<Question> getQuestions(Long testId) {

		List<Question> questions = null;
		final String[] paramNames = { "testId" };
		final Object[] paramValues = new Object[] { testId };

		questions = getHibernateTemplate().findByNamedQueryAndNamedParam(
				"getTestQuestions", paramNames, paramValues);

		return questions;

	}

	public List<BaseEntity> fetchQuestions(Level level, Subject subject,
			boolean showMyQuestionsOnly, Long userId) {
		List<BaseEntity> questions = null;

		DetachedCriteria crit = DetachedCriteria.forClass(Question.class);

		if (level != null)
			crit.createCriteria("level").add(
					Restrictions.eq("id", level.getId()));

		if (subject != null)
			crit.createCriteria("subject").add(
					Restrictions.eq("id", subject.getId()));

		if (showMyQuestionsOnly)
			crit.add(Restrictions.eq("modifiedBy",  userId));

		crit.addOrder(Order.desc("createDate"));

		questions = getHibernateTemplate().findByCriteria(crit);

		return questions;
	}

	public TestQuestion fetchTestQuestion(Question question2, Test test2) {
		DetachedCriteria crit = DetachedCriteria.forClass(TestQuestion.class);

		if (test2 != null)
			crit.createCriteria("test").add(
					Restrictions.eq("id", test2.getId()));

		if (question2 != null)
			crit.createCriteria("question").add(
					Restrictions.eq("id", question2.getId()));

		List<TestQuestion> questions = getHibernateTemplate().findByCriteria(crit);

		if(questions!=null && questions.size()>0)			
			return questions.get(0);
		
		return null;

	}

	public UserTest fetchUserTest(Student student, Test test2) {
		DetachedCriteria crit = DetachedCriteria.forClass(UserTest.class);

		if (student != null)
			crit.createCriteria("user").add(
					Restrictions.eq("id", student.getUser().getId()));

		if (test2 != null)
			crit.createCriteria("test").add(
					Restrictions.eq("id", test2.getId()));

		List<UserTest> userTests = getHibernateTemplate().findByCriteria(crit);

		if(userTests!=null && userTests.size()>0)			
			return userTests.get(0);
		
		return null;
	}

}
