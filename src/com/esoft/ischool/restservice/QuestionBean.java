package com.esoft.ischool.restservice;
import java.util.List;
import javax.annotation.PostConstruct;
import org.springframework.stereotype.Component;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Scope;

import com.esoft.ischool.model.Answer;
import com.esoft.ischool.model.BaseEntity;
import com.esoft.ischool.model.Level;
import com.esoft.ischool.model.Question;
import com.esoft.ischool.model.Rating;
import com.esoft.ischool.model.Subject;
import com.esoft.ischool.service.BaseService;
import com.esoft.ischool.util.MenuIdEnum;

@Component("questionBean")
@Scope("session")
public class QuestionBean extends BaseBean {
	@Autowired
	@Qualifier("baseService")
	private BaseService baseService;
	private Long rowCount;
	private List<BaseEntity> questions;
	private Question question = new Question();
	private String ratingName;
	private String subjectName;
	private Answer answer=new Answer();
	private List<BaseEntity> answers;
	private String selectedTab;
	private boolean showAnswerTab=false;
	private String levelName;
	
	
	
	public String getLevelName() {
		return levelName;
	}

	public void setLevelName(String levelName) {
		this.levelName = levelName;
	}

	@Override
	public String getSelectedTab() {
		return selectedTab;
	}

	public boolean isShowAnswerTab() {
		return showAnswerTab;
	}

	public void setShowAnswerTab(boolean showAnswerTab) {
		this.showAnswerTab = showAnswerTab;
	}

	@Override
	public void setSelectedTab(String selectedTab) {
		this.selectedTab = selectedTab;
	}

	public List<BaseEntity> getAnswers() {
		return answers;
	}

	public void setAnswers(List<BaseEntity> answers) {
		this.answers = answers;
	}

	public Answer getAnswer() {
		return answer;
	}

	public void setAnswer(Answer answer) {
		this.answer = answer;
	}

	public String validate() {
		return "succes";
	}

	@Override
	public String clear() {
		question = new Question();
		ratingName = "";
		subjectName = "";
		levelName="";
		answer=new Answer();
		return "Success";
	}

	public String clearAnswer() {
		answer=new Answer();
		return "Success";
	}
	public String delete() {
		clearMessages();
		try {
			baseService.delete(getIdParameter(), Question.class);
			setSuccessMessage(getResourceBundle().getString("DELETE_SUCCESSFULLY"));
		} catch (Exception ex) {
			setErrorMessage(getResourceBundle().getString("DELETE_UNSUCCESSFULL"));
		}

		return "Success";
	}
	
	public String deleteAnswer() {
		clearMessages();
		try {
			baseService.delete(getIdParameter(), Answer.class);
			answers= baseService.loadAllByParentId(Answer.class, "question","id",  question.getId());
			setSuccessMessage(getResourceBundle().getString("DELETE_SUCCESSFULLY"));
		} catch (Exception ex) {
			setErrorMessage(getResourceBundle().getString("DELETE_UNSUCCESSFULL"));
		}

		return "Success";
	}


	public String insert() {
		clearMessages();
		Long id = question.getId();

		Rating rating = (Rating) baseService.findByName(Rating.class,
				ratingName,getCurrentUser().getSchool());
		Subject subject = (Subject) baseService.findByName(Subject.class,
				subjectName,baseService.getDefaultSchool());
	    Level level = (Level) baseService.findByName(Level.class,
				levelName,baseService.getDefaultSchool());
		question.setRating(rating);
		question.setSubject(subject);
		question.setLevel(level);

		try {
			if (id == null || id == 0)
				baseService.save(question,getCurrentUser());
			else
				baseService.update(question,getCurrentUser());
			setSuccessMessage(getResourceBundle().getString("SAVED_SUCCESSFULLY"));
			clear();
			getAll();
		} catch (Exception ex) {
			ex.printStackTrace();
		}

		return "Success";
	}

	
	public String addAnswer() {
		clearMessages();
		Long id = answer.getId();
		try {
			answer.setQuestion(question);
			if (id == null || id == 0){
				baseService.save(answer,getCurrentUser());
			}else
				baseService.update(answer,getCurrentUser());
			
			answers= baseService.loadAllByParentId(Answer.class, "question","id",  question.getId());
			setSuccessMessage(getResourceBundle().getString("SAVED_SUCCESSFULLY"));
			clearAnswer();
		} catch (Exception ex) {
			ex.printStackTrace();
		}

		return "Success";
	}
	
	public String editAnswer() {
		clearMessages();
		answer = (Answer) baseService.getById(Answer.class,
				getIdParameter());
		return "Success";
	}
	
	public String edit() {
		clearMessages();
		question = (Question) baseService.getById(Question.class,
				getIdParameter());
		answers= baseService.loadAllByParentId(Answer.class, "question","id",  question.getId());
		ratingName = question.getRating().getName();
		subjectName = question.getSubject().getName();
		levelName=question.getLevel().getName();
		showAnswerTab=true;
		selectedTab="questionDetailPanel";
		return "Success";
	}

	@PostConstruct
	public void getAll() {
		questions = baseService.loadAll(Question.class,getCurrentUser().getSchool());
		rowCount = (long) (questions == null ? 0 : questions.size());
	}

	
	public boolean isUserHasWriteAccess() {
		return isUserHasWriteAccess(MenuIdEnum.QUESTIONS.getValue());
	}

	public BaseService getBaseService() {
		return baseService;
	}

	public void setBaseService(BaseService baseService) {
		this.baseService = baseService;
	}

	public Long getRowCount() {
		return rowCount;
	}

	public void setRowCount(Long rowCount) {
		this.rowCount = rowCount;
	}

	public String getSubjectName() {
		return subjectName;
	}

	public void setSubjectName(String subjectName) {
		this.subjectName = subjectName;
	}

	public List<BaseEntity> getQuestions() {
		return questions;
	}

	public void setQuestions(List<BaseEntity> questions) {
		this.questions = questions;
	}

	public Question getQuestion() {
		return question;
	}

	public void setQuestion(Question question) {
		this.question = question;
	}

	public String getRatingName() {
		return ratingName;
	}

	public void setRatingName(String ratingName) {
		this.ratingName = ratingName;
	}
	
	
}
