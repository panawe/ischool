package com.esoft.ischool.restservice;

import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Random;

import javax.annotation.PostConstruct;
import javax.faces.context.FacesContext;
import javax.servlet.ServletContext;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;

import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperRunManager;

import org.richfaces.model.selection.SimpleSelection;
import org.springframework.stereotype.Component;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Scope;

import com.esoft.ischool.model.Answer;
import com.esoft.ischool.model.BaseEntity;
import com.esoft.ischool.model.Level;
import com.esoft.ischool.model.Question;
import com.esoft.ischool.model.Student;
import com.esoft.ischool.model.Subject;
import com.esoft.ischool.model.Test;
import com.esoft.ischool.model.TestQuestion;
import com.esoft.ischool.model.UserTest;
import com.esoft.ischool.service.TestService;

@Component("testBean")
@Scope("session")
public class TestBean extends BaseBean {

	@Autowired
	@Qualifier("testService")
	private TestService testService;
	private Test test = new Test();
	private List<BaseEntity> tests = new ArrayList<BaseEntity>();
	private Long rowCount;
	private String subjectName;
	private String levelName;
	private String subjectNameR;
	private String levelNameR;
	private String selectedTab;
	private List<BaseEntity> testQuestions;
	private List<BaseEntity> questions;
	private List<BaseEntity> testResults;
	private SimpleSelection selectedQuestions = new SimpleSelection();
	private Long questionCount;
	private boolean showMyQuestionsOnly;
	private boolean beOrNotBe = true;
	private Question question;
	private List<BaseEntity> answers;
	private List<Student> selectedStudents = new ArrayList<Student>();
	private boolean testSelected = false;
	private boolean tQuestionSelected = false;
	private Integer newScore;
	private Long selectedTqId;
	private Long selectedResultId;
	private UserTest userTest;
	private SimpleSelection selectedResults = new SimpleSelection();
	private String subjectNameG;
	private String levelNameG;
	private boolean showMyQuestionsOnlyG;
	private Integer nbrOfQuestions;
	private Integer pointPerQuestion;

	
	public Integer getPointPerQuestion() {
		return pointPerQuestion;
	}

	public void setPointPerQuestion(Integer pointPerQuestion) {
		this.pointPerQuestion = pointPerQuestion;
	}

	public Integer getNbrOfQuestions() {
		return nbrOfQuestions;
	}

	public void setNbrOfQuestions(Integer nbrOfQuestions) {
		this.nbrOfQuestions = nbrOfQuestions;
	}

	public String getSubjectNameG() {
		return subjectNameG;
	}

	public void setSubjectNameG(String subjectNameG) {
		this.subjectNameG = subjectNameG;
	}

	public String getLevelNameG() {
		return levelNameG;
	}

	public void setLevelNameG(String levelNameG) {
		this.levelNameG = levelNameG;
	}

	public boolean isShowMyQuestionsOnlyG() {
		return showMyQuestionsOnlyG;
	}

	public void setShowMyQuestionsOnlyG(boolean showMyQuestionsOnlyG) {
		this.showMyQuestionsOnlyG = showMyQuestionsOnlyG;
	}

	public void setSelectedResultId(Long selectedResultId) {
		this.selectedResultId = selectedResultId;
	}

	public SimpleSelection getSelectedResults() {
		return selectedResults;
	}

	public void setSelectedResults(SimpleSelection selectedResults) {
		this.selectedResults = selectedResults;
	}

	public UserTest getUserTest() {
		return userTest;
	}

	public void setUserTest(UserTest userTest) {
		this.userTest = userTest;
	}

	public Long getSelectedTqId() {
		return selectedTqId;
	}

	public void setSelectedTqId(Long selectedTqId) {
		this.selectedTqId = selectedTqId;
	}

	public Integer getNewScore() {
		return newScore;
	}

	public void setNewScore(Integer newScore) {
		this.newScore = newScore;
	}

	public boolean istQuestionSelected() {
		return tQuestionSelected;
	}

	public void settQuestionSelected(boolean tQuestionSelected) {
		this.tQuestionSelected = tQuestionSelected;
	}

	public boolean isTestSelected() {
		return testSelected;
	}

	public void setTestSelected(boolean testSelected) {
		this.testSelected = testSelected;
	}

	public List<BaseEntity> getTestResults() {
		return testResults;
	}

	public void setTestResults(List<BaseEntity> testResults) {
		this.testResults = testResults;
	}

	public List<Student> getSelectedStudents() {
		return selectedStudents;
	}

	public void setSelectedStudents(List<Student> selectedStudents) {
		this.selectedStudents = selectedStudents;
	}

	public String getSubjectNameR() {
		return subjectNameR;
	}

	public void setSubjectNameR(String subjectNameR) {
		this.subjectNameR = subjectNameR;
	}

	public String getLevelNameR() {
		return levelNameR;
	}

	public void setLevelNameR(String levelNameR) {
		this.levelNameR = levelNameR;
	}

	public List<BaseEntity> getAnswers() {
		return answers;
	}

	public void setAnswers(List<BaseEntity> answers) {
		this.answers = answers;
	}

	public Question getQuestion() {
		return question;
	}

	public void setQuestion(Question question) {
		this.question = question;
	}

	public boolean isBeOrNotBe() {
		return beOrNotBe;
	}

	public void setBeOrNotBe(boolean beOrNotBe) {
		this.beOrNotBe = beOrNotBe;
	}

	public boolean isShowMyQuestionsOnly() {
		return showMyQuestionsOnly;
	}

	public void setShowMyQuestionsOnly(boolean showMyQuestionsOnly) {
		this.showMyQuestionsOnly = showMyQuestionsOnly;
	}

	public Long getQuestionCount() {
		return questionCount;
	}

	public void setQuestionCount(Long questionCount) {
		this.questionCount = questionCount;
	}

	public String assignTest() {

		try {
			for (Student student : selectedStudents) {
				student = (Student) testService.getById(Student.class,
						student.getId());
				UserTest userTest = fetchUserTest(student, test);

				if (userTest == null || test.getAllowMultipleTrial()) {
					userTest = new UserTest();
					userTest.setTest(test);
					userTest.setUser(student.getUser());
					testService.save(userTest,getCurrentUser());
				}

			}
			testResults = testService.loadAllByParentId(UserTest.class, "test",
					"id", test.getId());
			setSuccessMessage(getResourceBundle().getString("SAVED_SUCCESSFULLY"));
		} catch (Exception ex) {
			ex.printStackTrace();
			setErrorMessage(getResourceBundle().getString("SAVED_UNSUCCESSFULL"));
		}
		return "";
	}

	private UserTest fetchUserTest(Student student, Test test2) {
		// TODO Auto-generated method stub
		return testService.fetchUserTest(student, test2);
	}

	public String showQuestionBody() {
		question = (Question) testService.getById(Question.class,
				getIdParameter());
		answers = testService.loadAllByParentId(Answer.class, "question", "id",
				question.getId());
		tQuestionSelected = false;
		return "";
	}

	public String fetchQuestions() {
		Subject subject = (Subject) testService.findByName(Subject.class,
				subjectNameR,testService.getDefaultSchool());
		Level level = (Level) testService.findByName(Level.class, levelNameR,testService.getDefaultSchool());
		questions = testService.fetchQuestions(level, subject,
				showMyQuestionsOnly, getCurrentUserId());

		questionCount = (long) (questions != null ? questions.size() : 0);
		beOrNotBe = questionCount > 0 ? false : true;

		return "success";
	}
	
	public String generateQuestions() {
		Subject subject = (Subject) testService.findByName(Subject.class,
				subjectNameG,testService.getDefaultSchool());
		Level level = (Level) testService.findByName(Level.class, levelNameG,testService.getDefaultSchool());
		List<BaseEntity> questions = testService.fetchQuestions(level, subject,
				showMyQuestionsOnlyG, getCurrentUserId());

		int i = 1;
		
		Random rand = new Random(System.currentTimeMillis());
		while(i<=nbrOfQuestions &&questions!=null&&questions.size()>0){
			TestQuestion testQuestion = new TestQuestion();			
			int index=rand.nextInt(questions.size());
			Question question= (Question) questions.remove(index);
			testQuestion.setQuestion(question);
			testQuestion.setScore((pointPerQuestion==null||pointPerQuestion==0)?question.getScore():pointPerQuestion);
			testQuestion.setSequence(i++);
			testQuestion.setTest(test);
			testService.save(testQuestion,getCurrentUser());
			test.setScore(test.getScore()+testQuestion.getScore());
			
		}
		
		test.setSystemGenerated(true);
		testService.update(test, getCurrentUser());
		testQuestions = testService.loadAllByParentId(TestQuestion.class,
				"test", "id", test.getId());
		beOrNotBe = true;
		return "success";
	}
	
	public String getSelectedResultId(){
		
		selectedResultId=getIdParameter();
		return "Success";
	}

	public String showUserTestDetails(){

		try {

			Map<String, Serializable> parameters = new HashMap<String, Serializable>();
			if (selectedResultId!=null) {


				parameters.put("userTestId", selectedResultId);

				FacesContext context = FacesContext.getCurrentInstance();
				String reportsDirPath =( (ServletContext) context.getExternalContext().getContext()).getRealPath("/reports/");

				parameters.put("SUBREPORT_DIR", reportsDirPath+java.io.File.separator);
				HttpServletResponse response = (HttpServletResponse) context
						.getExternalContext().getResponse();

				InputStream reportStream = context.getExternalContext()
						.getResourceAsStream("/reports/testResult.jasper");

				ServletOutputStream ouputStream = response.getOutputStream();

				response.addHeader("Content-disposition",
						"attachment;filename=Resultats de test"+ "-"+getStringDate()+".pdf"); 

				JasperRunManager.runReportToPdfStream(reportStream,
						ouputStream, parameters, getConnection());
				response.setContentType("application/pdf");
				ouputStream.flush();
				ouputStream.close();
				context.responseComplete();
			}
		} catch (JRException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return "success";
	
	}
	
	public String addQuestions() {
		Iterator<Object> iterator = selectedQuestions.getKeys();
		int i = 1;
		while (iterator.hasNext()) {
			Integer key = (Integer) iterator.next();

			Question question = (Question) questions.get(key);

			TestQuestion testQuestion = fetchTestQuestion(question, test);

			if (testQuestion == null) {
				testQuestion = new TestQuestion();
				testQuestion.setQuestion(question);
				testQuestion.setScore(question.getScore());
				testQuestion.setSequence(i++);
				testQuestion.setTest(test);
				testService.save(testQuestion,getCurrentUser());
				test.setScore(test.getScore()+testQuestion.getScore());
			} else {
				testQuestion.setScore(question.getScore());
				testQuestion.setModDate(new Date());
				testQuestion.setSequence(i++);
				testService.update(testQuestion,getCurrentUser());
			}

		}
		testService.update(test, getCurrentUser());
		testQuestions = testService.loadAllByParentId(TestQuestion.class,
				"test", "id", test.getId());
		beOrNotBe = true;
		return "";
	}

	private TestQuestion fetchTestQuestion(Question question2, Test test2) {
		// TODO Auto-generated method stub
		return testService.fetchTestQuestion(question2, test2);
	}

	public SimpleSelection getSelectedQuestions() {
		return selectedQuestions;
	}

	public void setSelectedQuestions(SimpleSelection selectedQuestions) {
		this.selectedQuestions = selectedQuestions;
	}

	public List<BaseEntity> getQuestions() {
		return questions;
	}

	public void setQuestions(List<BaseEntity> questions) {
		this.questions = questions;
	}

	public List<BaseEntity> getTestQuestions() {
		return testQuestions;
	}

	public void setTestQuestions(List<BaseEntity> testQuestions) {
		this.testQuestions = testQuestions;
	}

	@Override
	public String getSelectedTab() {
		return selectedTab;
	}

	@Override
	public void setSelectedTab(String selectedTab) {
		this.selectedTab = selectedTab;
	}

	public String getLevelName() {
		return levelName;
	}

	public void setLevelName(String levelName) {
		this.levelName = levelName;
	}

	public String getSubjectName() {
		return subjectName;
	}

	public void setSubjectName(String subjectName) {
		this.subjectName = subjectName;
	}

	public Long getRowCount() {
		return rowCount;
	}

	public void setRowCount(Long rowCount) {
		this.rowCount = rowCount;
	}

	@PostConstruct
	public String getAllTests() {

		try {
			tests = testService.loadAll(Test.class,getCurrentUser().getSchool());

			rowCount = (long) (tests != null ? tests.size() : 0);

		} catch (Exception e) {
			e.printStackTrace();
			return "error";
		}

		return "success";
	}

	public String insert() {
		clearMessages();
		Long id = test.getId();
		try {
			Level level = (Level) testService
					.findByName(Level.class, levelName,testService.getDefaultSchool());
			Subject subject = (Subject) testService.findByName(Subject.class,
					subjectName,testService.getDefaultSchool());

			test.setLevel(level);
			test.setSubject(subject);

			if (id == null || id == 0)
				testService.save(test,getCurrentUser());
			else
				testService.update(test,getCurrentUser());
			setSuccessMessage(getResourceBundle().getString("SAVED_SUCCESSFULLY"));
			clear();
			getAllTests();
		} catch (Exception ex) {
			ex.printStackTrace();
			setErrorMessage(ex, getResourceBundle().getString("SAVED_UNSUCCESSFULL"));
		}

		return "Success";
	}

	public String delete() {
		clearMessages();
		try {
			testService.delete(getIdParameter(), Test.class);
			getAllTests();
			clear();
			setSuccessMessage(getResourceBundle().getString("DELETE_SUCCESSFULLY"));
		} catch (Exception ex) {
			ex.printStackTrace();
			setErrorMessage(getResourceBundle().getString("DELETE_UNSUCCESSFULL"));
		}

		return "Success";
	}

	public String deleteQuestion() {
		clearMessages();
		try {
			testService.delete(getIdParameter(), TestQuestion.class);
			testQuestions = testService.loadAllByParentId(TestQuestion.class,
					"test", "id", test.getId());
			setSuccessMessage(getResourceBundle().getString("DELETE_SUCCESSFULLY"));
		} catch (Exception ex) {
			ex.printStackTrace();
			setErrorMessage(getResourceBundle().getString("DELETE_UNSUCCESSFULL"));
		}

		return "Success";
	}

	public String editQuestion() {
		selectedTqId = getIdParameter();
		TestQuestion tq = (TestQuestion) testService.getById(
				TestQuestion.class, selectedTqId);
		question = tq.getQuestion();
		answers = testService.loadAllByParentId(Answer.class, "question", "id",
				question.getId());
		tQuestionSelected = true;

		return "";
	}

	public String changeScore() {
		clearMessages();
		try {
			TestQuestion tq = (TestQuestion) testService.getById(
					TestQuestion.class, selectedTqId);
			
			test.setScore(test.getScore()-tq.getScore()+newScore);
			testService.update(test,getCurrentUser());
			
			tq.setScore(newScore);
			testService.update(tq,getCurrentUser());

			testQuestions = testService.loadAllByParentId(TestQuestion.class,
					"test", "id", test.getId());
			
			setSuccessMessage(getResourceBundle().getString("SAVED_SUCCESSFULLY"));
		} catch (Exception ex) {
			ex.printStackTrace();
			setErrorMessage(getResourceBundle().getString("SAVED_UNSUCCESSFULL"));
		}

		return "Success";
	}

	public String edit() {
		clearMessages();
		test = (Test) testService.getById(Test.class, getIdParameter());
		testQuestions = testService.loadAllByParentId(TestQuestion.class,
				"test", "id", test.getId());
		testResults = testService.loadAllByParentId(UserTest.class, "test",
				"id", test.getId());
		levelName = test.getLevel().getName();
		subjectName = test.getSubject().getName();
		selectedTab = "testDetailTab";
		testSelected = true;
		return "Success";
	}

	public String doNothing() {
		beOrNotBe = true;
		return "";
	}

	@Override
	public String clear() {
		test = new Test();
		levelName = "";
		subjectName = "";
		subjectName = "";
		testSelected=false;
		return "Success";
	}

	public TestService getTestService() {
		return testService;
	}

	public void setTestService(TestService testService) {
		this.testService = testService;
	}

	public Test getTest() {
		return test;
	}

	public void setTest(Test test) {
		this.test = test;
	}

	public List<BaseEntity> getTests() {
		return tests;
	}

	public void setTests(List<BaseEntity> tests) {
		this.tests = tests;
	}
	@Override
	public Connection getConnection() {
		Connection jdbcConnection = null;

		FacesContext context = FacesContext.getCurrentInstance();

		ApplicationContext ctx = org.springframework.web.jsf.FacesContextUtils
				.getWebApplicationContext(context);

		DataSource dataSource = (DataSource) ctx.getBean("dataSource");
		try {
			jdbcConnection = dataSource.getConnection();
		} catch (Exception e) {
			e.printStackTrace();
		}

		return jdbcConnection;
	}

}