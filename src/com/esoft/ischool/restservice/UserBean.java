package com.esoft.ischool.restservice;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Serializable;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import javax.faces.component.html.HtmlGraphicImage;
import javax.faces.component.html.HtmlOutputLabel;
import javax.faces.component.html.HtmlOutputText;
import javax.faces.context.FacesContext;
import javax.faces.model.SelectItem;
import javax.servlet.ServletContext;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSessionBindingEvent;
import javax.sql.DataSource;

import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperRunManager;

import org.ajax4jsf.component.UIRepeat;
import org.apache.commons.lang.StringUtils;
import org.richfaces.component.MenuComponent;
import org.richfaces.component.html.HtmlDropDownMenu;
import org.richfaces.component.html.HtmlMenuItem;
import org.richfaces.component.html.HtmlToolBar;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.esoft.ischool.bean.BaseBean;
import com.esoft.ischool.bean.SkinBean;
import com.esoft.ischool.bean.StudentBean;
import com.esoft.ischool.job.Config;
import com.esoft.ischool.model.Alert;
import com.esoft.ischool.model.AlertReceiver;
import com.esoft.ischool.model.Answer;
import com.esoft.ischool.model.BaseEntity;
import com.esoft.ischool.model.Configuration;
import com.esoft.ischool.model.Exam;
import com.esoft.ischool.model.Mark;
import com.esoft.ischool.model.Message;
import com.esoft.ischool.model.Position;
import com.esoft.ischool.model.QNA;
import com.esoft.ischool.model.Question;
import com.esoft.ischool.model.Search;
import com.esoft.ischool.model.Student;
import com.esoft.ischool.model.Teacher;
import com.esoft.ischool.model.Test;
import com.esoft.ischool.model.TestQuestion;
import com.esoft.ischool.model.UserTest;
import com.esoft.ischool.security.model.Menu;
import com.esoft.ischool.security.model.Roles;
import com.esoft.ischool.security.model.RolesMenu;
import com.esoft.ischool.security.model.RolesUser;
import com.esoft.ischool.security.model.SessionHistory;
import com.esoft.ischool.security.model.User;
import com.esoft.ischool.security.service.UserService;
import com.esoft.ischool.service.BaseService;
import com.esoft.ischool.stocks.model.ProductConsumer;
import com.esoft.ischool.util.Constants;
import com.esoft.ischool.util.MenuIdEnum;

@Component("userBean")
@Scope("session")
public class UserBean extends BaseBean {

	@Autowired
	@Qualifier("userService")
	private UserService userService;
	@Autowired
	@Qualifier("config")
	private Config config;
	private User user = new User();
	List<UserTest> myPendingTests;
	private Long userIdForRoless;
	private List<BaseEntity> users;
	private Map<Menu, List<Menu>> menuMap = new HashMap<Menu, List<Menu>>();
	List<Menu> parentMenus = new LinkedList<Menu>();
	private boolean displayConnect = true;
	private String disconnectMessage;
	private List<BaseEntity> myRecentMarks;
	private List<BaseEntity> myRecentDemands;
	private List<Message> myNews = new ArrayList<Message>();
	private UserTest userTest;
	private Mark mark;
	private ProductConsumer demand;
	private Message news;
	private Search search;
	private Integer iUserTest = 0;
	private Integer iMark = 0;
	private Integer iDemand = 0;
	private Integer iNews = 0;
	private HtmlDropDownMenu home;
	private HtmlDropDownMenu dossiers;
	private HtmlDropDownMenu evaluations;
	private HtmlDropDownMenu bulletins;
	private HtmlDropDownMenu stocks;
	private HtmlDropDownMenu etats;
	private HtmlDropDownMenu configuration;
	private HtmlDropDownMenu aide;
	private HtmlDropDownMenu suivi;
	private HtmlDropDownMenu infirmerie;
	private HtmlDropDownMenu college;
	private HtmlDropDownMenu primaire;
	private Double random;
	private String selectedLocale = "fr";
	SessionHistory sessionHistory;
	private String positionName;
	
	

	public String getPositionName() {
		return positionName;
	}

	public void setPositionName(String positionName) {
		this.positionName = positionName;
	}

	public HtmlDropDownMenu getInfirmerie() {
		return infirmerie;
	}

	public void setInfirmerie(HtmlDropDownMenu infirmerie) {
		this.infirmerie = infirmerie;
	}

	public HtmlDropDownMenu getCollege() {
		return college;
	}

	public void setCollege(HtmlDropDownMenu college) {
		this.college = college;
	}

	public HtmlDropDownMenu getPrimaire() {
		return primaire;
	}

	public void setPrimaire(HtmlDropDownMenu primaire) {
		this.primaire = primaire;
	}

	public SessionHistory getSessionHistory() {
		return sessionHistory;
	}

	public void setSessionHistory(SessionHistory sessionHistory) {
		this.sessionHistory = sessionHistory;
	}

	public void paintLogo(OutputStream stream, Object object)
			throws IOException {
		if (user != null && user.getSchool().getLogo() != null) {
			stream.write(user.getSchool().getLogo());
		} else {
			stream.write(new byte[] {});
		}
	}

	public Integer getiUserTest() {
		return iUserTest;
	}

	public void setiUserTest(Integer iUserTest) {
		this.iUserTest = iUserTest;
	}

	public Integer getiMark() {
		return iMark;
	}

	public void setiMark(Integer iMark) {
		this.iMark = iMark;
	}

	public Integer getiDemand() {
		return iDemand;
	}

	public void setiDemand(Integer iDemand) {
		this.iDemand = iDemand;
	}

	public Integer getiNews() {
		return iNews;
	}

	public void setiNews(Integer iNews) {
		this.iNews = iNews;
	}

	public UserTest getUserTest() {
		return userTest;
	}

	public void setUserTest(UserTest userTest) {
		this.userTest = userTest;
	}

	public Mark getMark() {
		return mark;
	}

	public void setMark(Mark mark) {
		this.mark = mark;
	}

	public ProductConsumer getDemand() {
		return demand;
	}

	public void setDemand(ProductConsumer demand) {
		this.demand = demand;
	}

	public Message getNews() {
		return news;
	}

	public void setNews(Message news) {
		this.news = news;
	}

	public Config getConfig() {
		return config;
	}

	public void setConfig(Config config) {
		this.config = config;
	}

	public List<Message> getMyNews() {
		return myNews;
	}

	public void setMyNews(List<Message> myNews) {
		this.myNews = myNews;
	}

	public List<BaseEntity> getMyRecentDemands() {
		return myRecentDemands;
	}

	public void setMyRecentDemands(List<BaseEntity> myRecentDemands) {
		this.myRecentDemands = myRecentDemands;
	}

	public Double getRandom() {
		return Math.random();
	}

	public void setRandom(Double random) {
		this.random = random;
	}

	public HtmlDropDownMenu getSuivi() {
		return suivi;
	}

	public void setSuivi(HtmlDropDownMenu suivi) {
		this.suivi = suivi;
	}

	private UIRepeat repeater;

	private Test currentTest;

	private Integer currentSequence = 0;

	private List<BaseEntity> testQuestions;

	private TestQuestion currentQuestion;

	private List<BaseEntity> currentAnswers;

	private UserTest currentUserTest;

	private int duration = 0;

	private int initDuration = 0;

	private Date beginTime;

	private int nbrQuestions;

	private String questionHeader;

	private String answerHeader;

	public String getQuestionHeader() {
		return questionHeader;
	}

	public void setQuestionHeader(String questionHeader) {
		this.questionHeader = questionHeader;
	}

	public String getAnswerHeader() {
		return answerHeader;
	}

	public void setAnswerHeader(String answerHeader) {
		this.answerHeader = answerHeader;
	}

	public int getNbrQuestions() {
		return nbrQuestions;
	}

	public void setNbrQuestions(int nbrQuestions) {
		this.nbrQuestions = nbrQuestions;
	}

	public Date getBeginTime() {
		return beginTime;
	}

	public void setBeginTime(Date beginTime) {
		this.beginTime = beginTime;
	}

	public List<BaseEntity> getMyRecentMarks() {
		return myRecentMarks;
	}

	public void setMyRecentMarks(List<BaseEntity> myRecentMarks) {
		this.myRecentMarks = myRecentMarks;
	}

	public int getDuration() {

		if (beginTime != null) {
			duration = (int) (new Date().getTime() - beginTime.getTime())
					/ 60000 + initDuration;
			return duration;
		} else
			return 0;
	}

	public void setDuration(int duration) {
		this.duration = duration;
	}

	public UserTest getCurrentUserTest() {
		return currentUserTest;
	}

	public void setCurrentUserTest(UserTest currentUserTest) {
		this.currentUserTest = currentUserTest;
	}

	public List<BaseEntity> getCurrentAnswers() {
		return currentAnswers;
	}

	public void setCurrentAnswers(List<BaseEntity> currentAnswers) {
		this.currentAnswers = currentAnswers;
	}

	public Integer getCurrentSequence() {
		return currentSequence;
	}

	public void setCurrentSequence(Integer currentSequence) {
		this.currentSequence = currentSequence;
	}

	public List<BaseEntity> getTestQuestions() {
		return testQuestions;
	}

	public void setTestQuestions(List<BaseEntity> testQuestions) {
		this.testQuestions = testQuestions;
	}

	public TestQuestion getCurrentQuestion() {
		return currentQuestion;
	}

	public void setCurrentQuestion(TestQuestion currentQuestion) {
		this.currentQuestion = currentQuestion;
	}

	public Test getCurrentTest() {
		return currentTest;
	}

	public void setCurrentTest(Test currentTest) {
		this.currentTest = currentTest;
	}

	public List<UserTest> getMyPendingTests() {
		return myPendingTests;
	}

	public void setMyPendingTests(List<UserTest> myPendingTests) {
		this.myPendingTests = myPendingTests;
	}

	public HtmlDropDownMenu getEvaluations() {
		return evaluations;
	}

	public void setEvaluations(HtmlDropDownMenu evaluations) {
		this.evaluations = evaluations;
	}

	public HtmlDropDownMenu getHome() {
		return home;
	}

	public void setHome(HtmlDropDownMenu home) {
		this.home = home;
	}

	public HtmlDropDownMenu getBulletins() {
		return bulletins;
	}

	public void setBulletins(HtmlDropDownMenu bulletins) {
		this.bulletins = bulletins;
	}

	public HtmlDropDownMenu getStocks() {
		return stocks;
	}

	public void setStocks(HtmlDropDownMenu stocks) {
		this.stocks = stocks;
	}

	public HtmlDropDownMenu getEtats() {
		return etats;
	}

	public void setEtats(HtmlDropDownMenu etats) {
		this.etats = etats;
	}

	public HtmlDropDownMenu getConfiguration() {
		return configuration;
	}

	public void setConfiguration(HtmlDropDownMenu configuration) {
		this.configuration = configuration;
	}

	public HtmlDropDownMenu getAide() {
		return aide;
	}

	public void setAide(HtmlDropDownMenu aide) {
		this.aide = aide;
	}

	public List<HtmlDropDownMenu> getDropDowns() {
		return dropDowns;
	}

	private List<HtmlDropDownMenu> dropDowns = new ArrayList<HtmlDropDownMenu>();
	private HtmlToolBar toolBar;

	public void evaluateDropDowns() {

		Set<Menu> userMenu = (Set<Menu>) getSessionParameter("menus");
		if (userMenu != null) {
			home = new HtmlDropDownMenu();
			HtmlOutputLabel hol1 = new HtmlOutputLabel();
			HtmlOutputText hot1 = new HtmlOutputText();
			hot1.setValue("");
			HtmlGraphicImage hgi1 = new HtmlGraphicImage();
			hgi1.setStyle("vertical-align: middle;horizontal-align:left");
			hol1.getChildren().add(hot1);
			hol1.getChildren().add(hgi1);
			home.getFacets().put("label", hol1);
			hgi1.setOnclick("document.location.href='dispatch?link=home'");
			hgi1.setUrl("images/home.png");
			home.setSubmitMode(MenuComponent.MODE_NONE);
			for (Menu menu : userMenu) {

				if (menu.getMenuParent() == null) {
					HtmlDropDownMenu ddm = new HtmlDropDownMenu();
					HtmlOutputLabel hol = new HtmlOutputLabel();
					HtmlOutputText hot = new HtmlOutputText();
					hot.setValue(menu.getName());
					HtmlGraphicImage hgi = new HtmlGraphicImage();
					hgi.setStyle("vertical-align: middle;horizontal-align:left");
					hol.getChildren().add(hot);
					hol.getChildren().add(hgi);
					ddm.getFacets().put("label", hol);

					setSubMenus(userMenu, menu, ddm);

					if (menu.getSecurityCode() == 1) {
						hgi.setUrl("images/dossiers_icone.gif");
						dossiers = ddm;
					} else if (menu.getSecurityCode() == 2) {
						hgi.setUrl("images/evaluations_icone.gif");
						evaluations = ddm;
					} else if (menu.getSecurityCode() == 3) {
						hgi.setUrl("images/bulletins_icone.gif");
						bulletins = ddm;
					} else if (menu.getSecurityCode() == 4) {
						hgi.setUrl("images/stocks_icone.gif");
						stocks = ddm;
					} else if (menu.getSecurityCode() == 5) {
						hgi.setUrl("images/suivi_icone.gif");
						suivi = ddm;
					} else if (menu.getSecurityCode() == 6) {
						hgi.setUrl("images/etats_icone.gif");
						etats = ddm;
					} else if (menu.getSecurityCode() == 7) {
						hgi.setUrl("images/configuration_icone.gif");
						configuration = ddm;
					} else if (menu.getSecurityCode() == 500) {
						hgi.setUrl("images/aide_icone.gif");
						aide = ddm;
					} else if (menu.getSecurityCode() == 8) {
						hgi.setUrl("images/infirmerie_icone.gif");
						infirmerie = ddm;
					} else if (menu.getSecurityCode() == 300) {
						hgi.setUrl("images/primaire_icone.gif");
						primaire = ddm;
					} else if (menu.getSecurityCode() ==400) {
						hgi.setUrl("images/college_icone.gif");
						college = ddm;
					}
				}

			}
		}
	}

	public void setDropDowns(List<HtmlDropDownMenu> dropDowns) {
		this.dropDowns = dropDowns;
	}

	public org.richfaces.component.html.HtmlToolBar getToolBar() {
		toolBar = new HtmlToolBar();

		Set<Menu> userMenu = (Set<Menu>) getSessionParameter("menus");
		if (userMenu != null)
			for (Menu menu : userMenu) {

				if (menu.getMenuParent() == null) {
					HtmlDropDownMenu ddm = new HtmlDropDownMenu();
					ddm.setValue(menu.getName());
					setSubMenus(userMenu, menu, ddm);
					toolBar.getChildren().add(ddm);
				}

			}
		return toolBar;
	}

	public void setToolBar(HtmlToolBar toolBar) {
		this.toolBar = toolBar;
	}

	private void setSubMenus(Set<Menu> userMenu, Menu parent,
			HtmlDropDownMenu ddm) {
		for (Menu menu : userMenu) {

			if (menu.getMenuParent() != null
					&& menu.getMenuParent().equals(parent)
					&& menu.getUrl() != null) {

				HtmlMenuItem menuItem = new HtmlMenuItem();
				menuItem.setValue(menu.getName());
				menuItem.setOnclick("document.location.href='" + menu.getUrl()
						+ "'");
				menuItem.setSubmitMode("none");
				// menuItem.setOnclick("document.location.href='/student.faces'");
				ddm.getChildren().add(menuItem);
			}

		}

	}

	public HtmlDropDownMenu getDossiers() {
		return dossiers;
	}

	public void setDossiers(HtmlDropDownMenu dossiers) {
		this.dossiers = dossiers;
	}

	public String getDisconnectMessage() {
		return disconnectMessage;
	}

	public void setDisconnectMessage(String disconnectMessage) {
		this.disconnectMessage = disconnectMessage;
	}

	public boolean isDisplayConnect() {
		return displayConnect;
	}

	public void setDisplayConnect(boolean displayConnect) {
		this.displayConnect = displayConnect;
	}

	@Override
	public String clear() {
		user = new User();
		user.setSchool(getCurrentUser().getSchool());
		return "Success";
	}

	public String getStudentProfile() {
		// user=null;
		if (getSessionParameter("currentStudentId") != null) {
			Student student = (Student) userService
					.getById(Student.class,
							new Long(getSessionParameter("currentStudentId")
									.toString()));
			user = student.getUser();

			if (user == null) {
				user = new User();
			}
		}
		return "Success";
	}

	public String getTeacherProfile() {
		// user=null;
		if (getSessionParameter("currentTeacherId") != null) {
			Teacher teacher = (Teacher) userService
					.getById(Teacher.class,
							new Long(getSessionParameter("currentTeacherId")
									.toString()));
			user = teacher.getUser();

			if (user == null) {
				user = new User();
			}
		}
		return "Success";
	}

	public boolean isUserHasWriteAccessProfile() {
		if (((String) getSessionParameter("link")).equals("student")) {
			return isUserHasWriteAccess(MenuIdEnum.STUDENT.getValue());
		} else if (((String) getSessionParameter("link")).equals("teacher")) {
			return isUserHasWriteAccess(MenuIdEnum.TEACHER.getValue());
		}
		return false;
	}

	public String saveProfile() {
		clearMessages();
		try {
			if(positionName!=null){
				Position position =(Position)userService.findByName(Position.class, positionName,getCurrentUser().getSchool());
				user.setPosition(position);
			}
			if (user.getId() == null || user.getId() == 0) {
				if (userService.findByColumn(User.class, "userName",
						user.getUserName(), getCurrentUser().getSchool()) == null)
					userService.save(user, getCurrentUser());
				else {
					setErrorMessage(getResourceBundle().getString(
							"DUPLICATE_USER"));
					throw new Exception(getErrorMessage());
				}
			} else {
				if (user.getPassword() == null || user.getPassword().equals("")) {
					User util = (User) userService.getById(User.class,
							user.getId());
					user.setPassword(util == null ? "" : util.getPassword());
				}
				userService.update(user, getCurrentUser());
			}
			if (!(getSessionParameter("link") != null && (getSessionParameter(
					"link").equals("student") || getSessionParameter("link")
					.equals("teacher"))))
				clear();
			setSuccessMessage(getResourceBundle().getString(
					"SAVED_SUCCESSFULLY"));
		} catch (Exception e) {
			e.printStackTrace();

			if (StringUtils.isBlank(getErrorMessage()))
				setErrorMessage(e,
						getResourceBundle().getString("SAVED_UNSUCCESSFULL"));
			return "error";
		}
		return "success";
	}

	public String saveStudentProfile() {
		clearMessages();
		try {
			if (user.getId() == null || user.getId() == 0) {
				userService.save(user, getCurrentUser());
			} else {
				if (user.getPassword() == null || user.getPassword().equals("")) {
					User util = (User) userService.getById(User.class,
							user.getId());
					user.setPassword(util == null ? "" : util.getPassword());
				}
				userService.update(user, getCurrentUser());
			}

			if ("student".equals(getParameter("provenance"))
					&& getSessionParameter("currentStudentId") != null) {
				Student student = (Student) userService.getById(Student.class,
						new Long(getSessionParameter("currentStudentId")
								.toString()));

				student.setUser(user);
				userService.update(student, getCurrentUser());

			} else if (getSessionParameter("currentTeacherId") != null) {
				Teacher teacher = (Teacher) userService.getById(Teacher.class,
						new Long(getSessionParameter("currentTeacherId")
								.toString()));
				teacher.setUser(user);
				userService.update(teacher, getCurrentUser());
			}
			setSuccessMessage(getResourceBundle().getString(
					"SAVED_SUCCESSFULLY"));
		} catch (Exception ex) {
			ex.printStackTrace();
			setErrorMessage(ex, "Ce nom d'utilisateur est deja pris");
		}
		return "Success";
	}

	public String delete() {
		clearMessages();
		try {
			userService.delete(getIdParameter(), User.class);
			clear();
			setSuccessMessage(getResourceBundle().getString(
					"DELETE_SUCCESSFULLY"));
		} catch (Exception ex) {
			setErrorMessage(getResourceBundle().getString(
					"DELETE_UNSUCCESSFULL"));
		}
		return "Success";
	}

	public String edit() {
		setLink("security");
		user = (User) userService.getById(User.class, getIdParameter());
		return "Success";
	}

	public BaseService getBaseService() {
		return userService;
	}

	public void getRolesListsForUser() {
		List<Long> associatedRolesIds = userService.getRolesIdsForUser(
				getIdParameter(), User.class);

		this.pickListResult = new ArrayList();
		this.leftPickList = new ArrayList();

		userIdForRoless = getIdParameter();
		user = (User) userService.getById(User.class, userIdForRoless);

		for (BaseEntity g : userService.getAllRoles()) {
			SelectItem s = new SelectItem(((Roles) g).getId().toString(),
					((Roles) g).getName());

			if (associatedRolesIds.contains(((Roles) g).getId()))
				this.pickListResult.add(s);
			else
				this.leftPickList.add(s);
			this.allItemMap.put(((Roles) g).getId(), s);
		}

	}

	public Map<Menu, List<Menu>> getMenuMap() {
		return menuMap;
	}

	public List<Menu> getParentMenus() {
		return parentMenus;
	}

	public int getRowCount() {
		setRowCount(users.size());
		return rowCount;
	}

	public User getUser() {
		return user;
	}

	public Long getUserIdForRoless() {
		return userIdForRoless;
	}

	public List<BaseEntity> getUsers() {
		return users;
	}

	public String insert() {
		clearMessages();
		Long originalNum = user.getId();
		try {
			if(positionName!=null){
				Position position =(Position)userService.findByName(Position.class, positionName,getCurrentUser().getSchool());
				user.setPosition(position);
			}
			if (user.getId() == null || user.getId() == 0) {
				userService.add(user);
			} else {
				if (user.getPassword() == null || user.getPassword().equals("")) {
					User util = (User) userService.getById(User.class,
							user.getId());
					user.setPassword(util == null ? "" : util.getPassword());
				}
				userService.update(user, getCurrentUser());
			}
			clear();
			setSuccessMessage(getResourceBundle().getString(
					"SAVED_SUCCESSFULLY"));
		} catch (Exception ex) {
			user.setId(originalNum);
			ex.printStackTrace();
			setErrorMessage(ex, "Cet user existe deja");
		}
		return "Success";
	}

	public void savePickListData() {
		clearMessages();
		try {
			List<Long> selectedIds = new ArrayList<Long>();
			List<Long> unSelectedIds = new ArrayList<Long>();

			for (SelectItem sItem : pickListResult)
				selectedIds.add(new Long(sItem.getValue().toString()));

			for (SelectItem sItem : leftPickList)
				unSelectedIds.add(new Long(sItem.getValue().toString()));

			userService.savePickedList(getCurrentUserId(), userIdForRoless,
					unSelectedIds, selectedIds);
			setSuccessMessage(getResourceBundle().getString(
					"SAVED_SUCCESSFULLY"));
		} catch (Exception ex) {
			setErrorMessage(getResourceBundle()
					.getString("SAVED_UNSUCCESSFULL"));
			return;
		}
	}

	public void setBaseService(UserService userService) {
		this.userService = userService;
	}

	public void setMenuMap(Map<Menu, List<Menu>> menuMap) {
		this.menuMap = menuMap;
	}

	public void setParentMenus(List<Menu> parentMenus) {
		this.parentMenus = parentMenus;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public void setUserIdForRoless(Long userIdForRoless) {
		this.userIdForRoless = userIdForRoless;
	}

	public void setUsers(List<BaseEntity> users) {
		this.users = users;
	}

	public String validate() {
		clearMessages();
		user = userService.getUser(user.getUserName(), user.getPassword());
		if (user == null) {

			String demo = getParameter("demo");
			if (demo != null) {
				if(demo.equals("2")){
					user = userService.getUser("DEMO_ADMIN", "DEMO_ADMIN");
				}else if(demo.equals("3")){
					user = userService.getUser("DEMO_STUDENT", "DEMO_STUDENT");
				}else if(demo.equals("4")){
					user = userService.getUser("DEMO_TEACHER", "DEMO_TEACHER");
				}else if(demo.equals("5")){
					user = userService.getUser("DEMO_INFIRMIER", "DEMO_INFIRMIER");
				}
			}
			
			if(user==null){
				user = new User();
				setErrorMessage("Nom d'Utilisateur/Mot de passe non valide");
				return "echec";
			}
		}

		HttpServletRequest request = (HttpServletRequest) (FacesContext
				.getCurrentInstance().getExternalContext().getRequest());

		sessionHistory = new SessionHistory();
		sessionHistory.setBeginDate(new Date());
		sessionHistory.setUser(user);
		sessionHistory.setSessionId(request.getSession().getId());
		sessionHistory.setHostIp(request.getRemoteAddr());
		sessionHistory.setHostName(request.getRemoteHost());
		sessionHistory.setLanguage(request.getLocalName());
		sessionHistory.setOsuser(request.getRemoteUser());
		sessionHistory.setBrowser(request.getHeader("User-Agent"));
		userService.save(sessionHistory, user);

		Teacher teach = userService.getTeacher(user);
		Student stud = null;
		if (teach != null) {
			user.setTeacher(true);
		} else {
			stud = userService.getStudent(user);
			if (stud != null) {
				user.setStudent(true);
			}
		}
		setSessionAttribute("user", user);

		if (stud != null) {

			if (stud.getStatus() >= TEMP_STUDENT) {
				StudentBean stBean = (StudentBean) getSessionParameter("studentBean");

				if (stBean == null)
					stBean = (StudentBean) FacesContext
							.getCurrentInstance()
							.getApplication()
							.getELResolver()
							.getValue(
									FacesContext.getCurrentInstance()
											.getELContext(), null,
									"studentBean");
				if (stBean != null) {
					stBean.setStudent(stud);
					stBean.intit();
				}
				return "pending";
			}
		}

		// get menus
		Set<Menu> userMenu = new TreeSet<Menu>();
		Set<RolesUser> groupUtil = user.getRolesUser();

		if (StringUtils.isNotEmpty(user.getCurrentLocale())) {
			setResourceBundle(new Locale(user.getCurrentLocale()));
			setSelectedLocale(user.getCurrentLocale());
		} else {
			Config config = (Config) getApplicationContext().getBean("config");
			List<Configuration> configurations = config
					.getConfigurationListByGroup("DEFAULT_LOCALE", userService,
							getCurrentUser().getSchool());
			if (configurations != null & configurations.size() > 0) {
				setResourceBundle(new Locale(configurations.get(0).getValue()));
				setSelectedLocale(configurations.get(0).getValue());
			}
		}
		for (RolesUser gu : groupUtil) {
			for (RolesMenu gm : gu.getRoles().getRolesMenus()) {
				Menu menu = gm.getMenu();
				menu.setAccessLevel(gm.getAccessLevel());
				String translatedName = getResourceBundle().getString(
						"MENU_" + menu.getSecurityCode());
				if (translatedName != null && !translatedName.equals(""))
					menu.setName(translatedName);

				if (gm.getMenu().getMenuParent() != null) {
					userMenu.add(menu);
					userMenu.add(menu.getMenuParent());
					if ("25".equals(menu.getId().toString())) {
						request.getSession().setAttribute("eventAccessLevel",
								menu.getAccessLevel());
					}
				} else {
					userMenu.add(menu);
					// addAllKids(gm.getMenu(), userMenu);
				}
			}
		}

		setSessionAttribute("menus", userMenu);
		Map<String, String> configuration = new HashMap<String, String>();
		List<BaseEntity> configurations = userService.loadAll(
				Configuration.class, getCurrentUser().getSchool());
		for (BaseEntity be : configurations) {
			configuration.put(((Configuration) be).getName(),
					((Configuration) be).getValue());
		}
		setSessionAttribute("configuration", configuration);

		SkinBean skin = (SkinBean) getSessionParameter("skinBean");

		if (skin != null && user.getPageSkin() != null) {
			skin.setSkin(user.getPageSkin());
		}
		displayConnect = false;
		evaluateDropDowns();
		disconnectMessage = user.getFirstName() + " " + user.getLastName();
		myPendingTests = userService.getUserTests(user);

		// Recent Marks
		if (stud != null)
			myRecentMarks = userService.getStudentTop10Marks(stud);

		// Get Demands
		if (stud != null)
			myRecentDemands = userService.loadByParentsIds(
					ProductConsumer.class, "consumer", stud.getUser().getId(),
					"school", getCurrentUser().getSchool().getId());
		else if (teach != null)
			myRecentDemands = userService.loadByParentsIds(
					ProductConsumer.class, "consumer", teach.getUser().getId(),
					"school", getCurrentUser().getSchool().getId());
		if (myRecentDemands != null)
			for (BaseEntity apc : myRecentDemands) {
				ProductConsumer productConsumer = (ProductConsumer) apc;
				productConsumer.setStatusDesc(config
						.getConfigurationByGroupAndKey("CONSUMER_STATUS",
								productConsumer.getStatus().toString()));
			}

		// Get Alerts
		List<AlertReceiver> alerts = userService.getUserAlerts(user,
				user.getSchool());

		if (alerts != null) {
			for (AlertReceiver alertRcv : alerts) {
				Alert alert = alertRcv.getAlert();
				if (alert.getDisplay().equals(1)) {
					Date dt = new Date(alert.getCreateDate().getTime()
							+ alert.getDaysDisplay() * 86400000);
					if (dt.before(dt)) {
						Message news = new Message();
						news.setMessage(alert.getMessage());
						news.setNewsDate(alert.getCreateDate());
						news.setNewsType(ALERT);
						myNews.add(news);
					}
				}
			}
		}

		// Get pending exam note approval

		if (user.isTeacher()) {
			List<BaseEntity> exams = userService.getUnaprovedAssignments(user);

			if (exams != null) {

				for (BaseEntity be : exams) {
					Exam ex = (Exam) be;
					Message news = new Message();
					news.setNewsType(UNAPPROVED_MARK);
					news.setNewsDate(ex.getExamDate());
					news.setMessage(EXAM_TYPE + " : "
							+ ex.getExamType().getName() + "<br/>"
							+ DESCRIPTION + " : " + ex.getName() + "<br/>"
							+ CLASS + " : "
							+ ex.getCourse().getLevelClass().getName()
							+ "<br/>" + COURSE + " : "
							+ ex.getCourse().getSubject().getName());
					myNews.add(news);

				}
			}
		}

		// Get pending demande approval

		if (user.getCanApprove()) {
			List<BaseEntity> bes = userService.getPendingDemandes(user
					.getSchool());
			if (bes != null) {
				for (BaseEntity be : bes) {
					ProductConsumer pc = (ProductConsumer) be;
					Message news = new Message();
					news.setNewsDate(pc.getCreateDate());
					news.setNewsType(UNAPPROVED_DEMAND);
					news.setMessage(PRODUCT + " : " + pc.getProduct().getName()
							+ "<br/>" + QUANTITY + " : "
							+ pc.getQuantityRequested() + "<br/>" + COMMENT
							+ " : " + pc.getComment());
					myNews.add(news);
				}
			}

		}
		Collections.sort(myNews);
		getNewItems();
		return "succes";
	}
	
	public String getNextNews(){
		if (myNews != null && myNews.size() > 0) {
			news = myNews.get(iNews++);
			if (iNews >= myNews.size()) {
				iNews = 0;
			}
		}
		return "success";
	}

	public String getNextDemand(){
		if (myRecentDemands != null && myRecentDemands.size() > 0) {
			demand = (ProductConsumer) myRecentDemands.get(iDemand++);
			if (iDemand >= myRecentDemands.size()) {
				iDemand = 0;
			}
		}
		return "success";
	}
	
	public String getNextExam(){
		if (myPendingTests != null && myPendingTests.size() > 0) {
			userTest = (UserTest) myPendingTests.get(iUserTest++);
			if (iUserTest >= myPendingTests.size()) {
				iUserTest = 0;
			}
		}
		return "success";
	}
	
	public String getNextMark(){

		if (myRecentMarks != null && myRecentMarks.size() > 0) {
			mark = (Mark) myRecentMarks.get(iMark++);
			if (iMark >= myRecentMarks.size()) {
				iMark = 0;
			}
		}

		return "success";
	}
	
	public String getNewItems() {

		if (myNews != null && myNews.size() > 0) {
			news = myNews.get(iNews++);
			if (iNews >= myNews.size()) {
				iNews = 0;
			}
		}

		if (myRecentDemands != null && myRecentDemands.size() > 0) {
			demand = (ProductConsumer) myRecentDemands.get(iDemand++);
			if (iDemand >= myRecentDemands.size()) {
				iDemand = 0;
			}
		}

		if (myRecentMarks != null && myRecentMarks.size() > 0) {
			mark = (Mark) myRecentMarks.get(iMark++);
			if (iMark >= myRecentMarks.size()) {
				iMark = 0;
			}
		}

		if (myPendingTests != null && myPendingTests.size() > 0) {
			userTest = (UserTest) myPendingTests.get(iUserTest++);
			if (iUserTest >= myPendingTests.size()) {
				iUserTest = 0;
			}
		}
		return "success";
	}

	private void addAllKids(Menu menu, Set<Menu> userMenu) {

		List<Menu> subs = userService.getSubMenus(menu.getId());
		for (Menu aMenu : subs) {
			userMenu.add(aMenu);
		}
	}

	public String validate2() {
		user = userService.getUser(user.getUserName(), user.getPassword());
		if (user == null) {
			setErrorMessage("Invalid Username/password");
			System.out.println(user);
			return "echec";
		}
		addToSession("user", user);
		myPendingTests = userService.getUserTests(user);

		return "succes";
	}

	public UserService getUserService() {
		return userService;
	}

	public void setUserService(UserService userService) {
		this.userService = userService;
	}

	public void setQuestions(Long testId, HttpSessionBindingEvent event) {
		List<Question> questions = userService.getQuestions(testId);
		event.getSession().setAttribute("questions", questions);

	}

	public String startTest() {

		currentUserTest = (UserTest) userService.getById(UserTest.class,
				getIdParameter());
		currentTest = currentUserTest.getTest();
		if (currentUserTest.getDuration() == 0
				& currentUserTest.getWrong() == 0
				&& currentUserTest.getRight() == 0) {
			testQuestions = userService.loadAllByParentId(TestQuestion.class,
					"test", "id", currentTest.getId());
		} else {
			testQuestions = userService.getPendingQuestions(
					currentTest.getId(), currentUserTest.getId());
			initDuration = currentUserTest.getDuration();
		}

		nbrQuestions = testQuestions.size();
		currentSequence = 0;

		return "";
	}

	public String testDone() {
		currentUserTest = null;
		currentTest = null;
		testQuestions = null;
		currentAnswers = null;
		currentQuestion = null;
		currentSequence = 0;
		myPendingTests = userService.getUserTests(user);
		return "success";
	}

	public String fetchQuestion() {
		if (testQuestions != null && currentSequence <= testQuestions.size()) {
			if (currentSequence > 0) {

				for (BaseEntity an : currentAnswers) {
					QNA qna = new QNA();
					qna.setAnswer((Answer) an);
					qna.setQuestion(currentQuestion.getQuestion());
					qna.setUserTest(currentUserTest);
					qna.setIsCorrect(((Answer) an).isChecked());
					userService.save(qna, getCurrentUser());
				}

				if (currentSequence == 1) {
					if (currentUserTest.getBeginDate() == null)
						currentUserTest.setBeginDate(new Date());
				} else if (currentSequence == nbrQuestions) {
					currentUserTest.setEndDate(new Date());
					currentUserTest.setCompleted(true);
				}

				if (passed()) {
					currentUserTest
							.setRight((currentUserTest.getRight() == null ? 0
									: currentUserTest.getRight()) + 1);
					currentUserTest
							.setScore((currentUserTest.getScore() == null ? 0
									: currentUserTest.getScore())
									+ currentQuestion.getScore());
				} else {
					currentUserTest
							.setWrong((currentUserTest.getWrong() == null ? 0
									: currentUserTest.getWrong()) + 1);
				}
				currentUserTest
						.setDuration((int) (new Date().getTime() - beginTime
								.getTime()) / 60000 + initDuration);

				userService.update(currentUserTest, getCurrentUser());
			} else {
				beginTime = new Date();
			}

			if (currentUserTest.getDuration() >= currentTest.getDuration()) {
				currentSequence = nbrQuestions;
			}
			if (currentSequence < nbrQuestions) {
				currentQuestion = (TestQuestion) testQuestions
						.get(currentSequence++);
				currentAnswers = userService
						.loadAllByParentId(Answer.class, "question", "id",
								currentQuestion.getQuestion().getId());
				answerHeader = "";
				questionHeader = "";
				if (currentQuestion.getQuestion().getDisplayNbrAnswer()) {
					short i = 0;
					for (BaseEntity be : currentAnswers) {
						Answer answer = (Answer) be;
						i += answer.getCorrect() ? 1 : 0;
					}
					currentQuestion.setRightAnswers(i);
					answerHeader = " | Choisir " + i;
				}
				if (currentTest.getShowRating()) {
					questionHeader = " | "
							+ currentQuestion.getQuestion().getRating()
									.getName();
				}
				if (currentTest.getShowPoints()) {
					questionHeader += " | " + currentQuestion.getScore()
							+ " Points";
				}

			} else {
				currentSequence++;

			}

		}
		return "succes";
	}

	private boolean passed() {

		boolean passed = true;
		for (BaseEntity an : currentAnswers) {

			if (!((Answer) an).isChecked() == ((Answer) an).getCorrect()) {
				passed = false;
			}

		}

		return passed;
	}

	public UIRepeat getRepeater() {
		return repeater;
	}

	public void setRepeater(UIRepeat repeater) {
		this.repeater = repeater;
	}

	public String changeAnswer() {

		Long answerId = getIdParameter();
		for (BaseEntity ans : currentAnswers) {
			Answer answer = (Answer) ans;
			if (answer.getId().equals(answerId)) {
				answer.setChecked(!answer.isChecked());
			}
		}

		return "";
	}

	public String saveTemp() {
		currentUserTest.setDuration((int) (new Date().getTime() - beginTime
				.getTime()) / 60000 + initDuration);

		userService.update(currentUserTest, getCurrentUser());

		currentQuestion = null;
		currentTest = null;
		testQuestions = null;
		currentSequence = 0;
		currentAnswers = null;
		return "succes";
	}

	public String selectUserTest() {

		try {
			currentUserTest = (UserTest) userService.getById(UserTest.class,
					getIdParameter());
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return "success";
	}

	public String showUserTestDetails() {

		try {

			Map<String, Serializable> parameters = new HashMap<String, Serializable>();

			if (currentUserTest != null) {

				parameters.put("userTestId", currentUserTest.getId());
				parameters
						.put("schoolId", getCurrentUser().getSchool().getId());

				FacesContext context = FacesContext.getCurrentInstance();
				String reportsDirPath = ((ServletContext) context
						.getExternalContext().getContext())
						.getRealPath("/reports/");

				parameters.put("SUBREPORT_DIR", reportsDirPath
						+ java.io.File.separator);
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

	List<BaseEntity> activeUsers;
	private int activeUsersCount;

	public int getActiveUsersCount() {
		return activeUsersCount;
	}

	public void setActiveUsersCount(int activeUsersCount) {
		this.activeUsersCount = activeUsersCount;
	}

	public List<BaseEntity> getActiveUsers() {
		if (activeUsers != null)
			setActiveUsersCount(activeUsers.size());
		else
			setActiveUsersCount(0);
		return activeUsers;
	}

	public String selectAllActiveUsers() {
		activeUsers = userService.loadAll(User.class, getCurrentUser()
				.getSchool());
		return "Success";
	}

	public void setActiveUsers(List<BaseEntity> activeUsers) {
		this.activeUsers = activeUsers;
	}

	private List<String> allActiveUsers;

	public void setAllActiveUsers(List<String> allActiveUsers) {
		this.allActiveUsers = allActiveUsers;
	}

	public List<String> getAllActiveUsers() {
		if (activeUsers == null)
			activeUsers = userService.loadAll(User.class, getCurrentUser()
					.getSchool());

		allActiveUsers = new ArrayList<String>();
		for (BaseEntity entity : activeUsers) {
			User user = (User) entity;
			allActiveUsers.add(user.getUserName());
		}

		return allActiveUsers;
	}

	public String getSearchedUsers() {
		if (selectAllActiveUsers)
			activeUsers = userService.loadAll(User.class, getCurrentUser()
					.getSchool());
		else {
			if (StringUtils.isNotBlank(lastName)
					|| StringUtils.isNotBlank(firstName)
					|| StringUtils.isNotBlank(userName))
				activeUsers = userService.getUsers(User.class, userName,
						lastName, firstName, getCurrentUser());
		}
		setActiveUsersCount(activeUsers != null ? activeUsers.size() : 0);
		return "Success";
	}

	private String userName;
	private String lastName;
	private String firstName;
	private boolean selectAllActiveUsers;

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public boolean isSelectAllActiveUsers() {
		return selectAllActiveUsers;
	}

	public void setSelectAllActiveUsers(boolean selectAllActiveUsers) {
		this.selectAllActiveUsers = selectAllActiveUsers;
	}

	public String changedLocale() {
		getContext().getViewRoot().setLocale(new Locale(selectedLocale));
		user = getCurrentUser();
		user.setCurrentLocale(selectedLocale);
		userService.update(user, getCurrentUser());
		return "";
	}

	public String getSelectedLocale() {
		return selectedLocale;
	}

	public void setSelectedLocale(String selectedLocale) {
		this.selectedLocale = selectedLocale;
	}
}
