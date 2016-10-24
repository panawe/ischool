package com.esoft.ischool.service;

import java.util.List;

import com.esoft.ischool.model.BaseEntity;
import com.esoft.ischool.model.Level;
import com.esoft.ischool.model.Question;
import com.esoft.ischool.model.Student;
import com.esoft.ischool.model.Subject;
import com.esoft.ischool.model.Test;
import com.esoft.ischool.model.TestQuestion;
import com.esoft.ischool.model.UserTest;

public interface TestService extends BaseService {

	public List<Question> getQuestions(Long testId);

	public List<BaseEntity> fetchQuestions(Level level, Subject subject, boolean showMyQuestionsOnly, Long userId);

	public TestQuestion fetchTestQuestion(Question question2, Test test2);

	public UserTest fetchUserTest(Student student, Test test2);
}
