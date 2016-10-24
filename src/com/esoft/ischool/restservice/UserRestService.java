package com.esoft.ischool.restservice;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.esoft.ischool.listener.MySessionListener;
import com.esoft.ischool.model.BaseEntity;
import com.esoft.ischool.model.BudgetCash;
import com.esoft.ischool.model.Event;
import com.esoft.ischool.model.Mail;
import com.esoft.ischool.model.Transaction;
import com.esoft.ischool.security.model.Contribution;
import com.esoft.ischool.security.model.Mariage;
import com.esoft.ischool.security.model.Search;
import com.esoft.ischool.security.model.SessionHistory;
import com.esoft.ischool.security.model.User;
import com.esoft.ischool.security.model.YearlySummary;
import com.esoft.ischool.security.service.UserService;
import com.esoft.ischool.util.SimpleMail;

@RestController
@RequestMapping("/service/user")

public class UserRestService {

	@Autowired
	UserService userService;
	@Autowired
	MySessionListener mySessionListener;
	@Autowired
	ServletContext context;
	@Autowired
	private HttpServletRequest request;

	@RequestMapping(value = "/login", method = RequestMethod.POST, headers = "Accept=application/json")
	public @ResponseBody User login(@RequestBody User user) {
		System.out.println("User Login :" + user);
		user = userService.getUser(user.getUserName(), user.getPassword());

		if (user != null) {
			Long sessionHistoryId = (Long) request.getSession().getAttribute("sessionHistoryId");
			if (sessionHistoryId == null)
				addGuestCount();
			sessionHistoryId = (Long) request.getSession().getAttribute("sessionHistoryId");

			request.getSession().setAttribute("userId", user.getId());
			if (sessionHistoryId != null) {
				SessionHistory sh = (SessionHistory) userService.getById(SessionHistory.class, sessionHistoryId);
				sh.setUser(user);
				userService.update(sh, user);
			}
			return user.minimize();
		}
		return null;
		
	}

	@RequestMapping(value = "/addGuestCount", method = RequestMethod.POST, headers = "Accept=application/json")
	public void addGuestCount() {
		MySessionListener.sessions.add(request.getSession().getId());

		SessionHistory sessionHistory = new SessionHistory();
		sessionHistory.setBeginDate(new Date());
		sessionHistory.setUser(null);
		sessionHistory.setSessionId(request.getSession().getId());
		sessionHistory.setHostIp(request.getRemoteAddr());
		sessionHistory.setHostName(request.getRemoteHost());
		sessionHistory.setLanguage(request.getLocalName());
		sessionHistory.setOsuser(request.getRemoteUser());
		sessionHistory.setBrowser(request.getHeader("User-Agent"));
		userService.save(sessionHistory);

		request.getSession().setAttribute("sessionHistoryId", sessionHistory.getId());
	}

	@RequestMapping(value = "/getGuestCount", method = RequestMethod.POST, headers = "Accept=application/json")
	public @ResponseBody Long getGuestCount() {
		return (long) MySessionListener.sessions.size();
	}

	@RequestMapping(value = "/logout", method = RequestMethod.POST, headers = "Accept=application/json")
	public @ResponseBody Boolean logout() {
		System.out.println("User Logout :" + request.getSession().getAttribute("userId"));

		mySessionListener.sessionDestroyed(request);

		request.getSession().setAttribute("userId", null);
		request.getSession().setAttribute("sessionHistoryId", null);

		return true;
	}

	@RequestMapping(value = "/getMessagingUsers", method = RequestMethod.POST, headers = "Accept=application/json")
	public @ResponseBody List<User> getMessagingUsers() {

		List<User> members = userService.loadAllMembersWithOnlineStatus();

		return members;
	}

	@RequestMapping(value = "/createUser", method = RequestMethod.POST, headers = "Accept=application/json")
	public @ResponseBody User createUser(@RequestBody User user) {
		
		List<User> users= userService.findMembers(user.getFirstName(), user.getLastName(), user.getUserName(), user.getEmail());
		
		if(users!=null && users.size()>0){
			return null;
		}
		if(user.getUserName()==null){
			user.setUserName(user.getFirstName().toLowerCase().replaceAll(" ", "")+"."+user.getLastName().toLowerCase().replaceAll(" ", ""));
		}
		if(user.getPassword()==null){
			user.setPassword("123");
		}
		if(user.getMembershipDate()==null){
			user.setMembershipDate(new Date());
		}
		if(user.getBirthDate()==null){
			user.setBirthDate(new Date());
		}
		if(user.getMembershipRenewDate()==null){
			user.setMembershipDate(new Date());
		}
		 
		user.setMembershipDate(new Date());

		Calendar date = new GregorianCalendar();
		date.set(Calendar.HOUR_OF_DAY, 0);
		date.set(Calendar.MINUTE, 0);
		date.set(Calendar.SECOND, 0);
		date.set(Calendar.MILLISECOND, 0);

		user.setMembershipRenewDate(date.getTime());
		userService.add(user);
		System.out.println("User Created:" + user);
		try {

			String mail = "<blockquote><h2><b>Cher Membre</b></h2><h2>Nous avons bien recu votre demande d'adhesion a "
					+ userService.getConfig("ORG_NAME").getValue()
					+ "  </h2><h2>Votre demande va etre etudier et vous serez notifie d'ici peu.</h2><h2>Encore une fois, merci de votre interet en notre association.</h2><h2><b>Le President.</b></h2></blockquote>";
			SimpleMail.sendMail(
					"Votre demande d'adhesion a " + userService.getConfig("ORG_NAME").getValue() + " bien recue", mail,
					userService.getConfig("ORG_EMAIL").getValue(), user.getEmail(),
					userService.getConfig("ORG_SMTP").getValue(), userService.getConfig("ORG_EMAIL").getValue(),
					userService.getConfig("ORG_EMAIL_PASSWORD").getValue());

			mail = "<blockquote><h2><b>Nom: " + user.getLastName() + "</b></h2><h2><b>Prenom:" + user.getFirstName()
					+ "</b></h2><h2><b>E-mail:" + user.getEmail()
					+ "</b></h2><div><b>Veuillez Approver en allant sur le site: <a href=\""
					+ userService.getConfig("ORG_WEBSITE").getValue() + " \" target=\"\">"
					+ userService.getConfig("ORG_WEBSITE").getValue() + " </a></b></div></blockquote>";
			SimpleMail.sendMail("Demand d'adhesion de " + user.getFirstName() + " " + user.getLastName(), mail,
					userService.getConfig("ORG_EMAIL").getValue(), userService.getConfig("ORG_EMAIL").getValue(),
					userService.getConfig("ORG_SMTP").getValue(), userService.getConfig("ORG_EMAIL").getValue(),
					userService.getConfig("ORG_EMAIL_PASSWORD").getValue());

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return userService.getUser(user.getUserName(), user.getPassword());
	}

	@RequestMapping(value = "/makePayment", method = RequestMethod.POST, headers = "Accept=application/json")
	public @ResponseBody String makePayment(@RequestBody Transaction tran) {
		System.out.println("makePayment called:" + tran);

		try {
			userService.save(tran);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return "Failure";
		}

		return "Success";
	}

	@RequestMapping(value = "/saveExpense", method = RequestMethod.POST, headers = "Accept=application/json")
	public @ResponseBody String saveExpense(@RequestBody Transaction tran) {
		System.out.println("saveExpense called:" + tran);

		try {
			userService.save(tran);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return "Failure";
		}

		return "Success";
	}

	@RequestMapping(value = "/saveUser", method = RequestMethod.POST, headers = "Accept=application/json")
	public @ResponseBody User saveUser(@RequestBody User user) throws Exception {
		
		if(user.getFirstName()==null ||user.getLastName()==null||user.getSex()==null||user.getCityOrigin()==null||user.getCityResidence()==null||
				user.getCountryOrigin()==null||user.getCityResidence()==null){
			throw new Exception("Missing Required fields");
		}
		
		if(user.getId()==null){//new User
			List<User> users= userService.findMembers(user.getFirstName(), user.getLastName(), user.getUserName(), user.getEmail());		
			if(users!=null && users.size()>0){
				throw new Exception("User Exists");
			}
		}
		
		if(user.getUserName()==null){
			user.setUserName(user.getFirstName().toLowerCase().replaceAll(" ", "")+"."+user.getLastName().toLowerCase().replaceAll(" ", ""));
		}
		if(user.getPassword()==null){
			user.setPassword("123");
		}
		if(user.getMembershipDate()==null){
			user.setMembershipDate(new Date());
		}
		if(user.getBirthDate()==null){
			user.setBirthDate(new Date());
		}
		if(user.getMembershipRenewDate()==null){
			user.setMembershipDate(new Date());
		}
		userService.add(user);
		return user.minimize();
		//return userService.getUser(user.getUserName(), user.getPassword());
	}

	@RequestMapping(value = "/receiveFile", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody String receiveFile(@RequestParam("file") MultipartFile file,
			@RequestParam("userId") String userId) {
		if (!file.isEmpty()) {
			try {
				String originalFileExtension = file.getOriginalFilename()
						.substring(file.getOriginalFilename().lastIndexOf("."));

				// transfer to upload folder
				String storageDirectory = null;
				if (context != null) {

					storageDirectory = context.getRealPath("/") + File.separator + "images" + File.separator
							+ "members";
					File dir = new File(storageDirectory);
					if (!dir.exists()) {
						dir.mkdirs();
					}

				} else {
					return "Failure";

				}
				String newFilename = userId + ".jpg";

				File newFile = new File(storageDirectory + File.separator + newFilename);
				file.transferTo(newFile);

			} catch (Exception e) {
				e.printStackTrace();
				return "Failure";
			}
		} else {
			return "Failure";
		}

		return "Success";
	}

	@RequestMapping(value = "/getUsers", method = RequestMethod.POST, headers = "Accept=application/json")
	public @ResponseBody List<User> getUsers() {
		System.out.println("User list Requested - getUsers");
		return userService.loadAllUsers();
	}

	@RequestMapping(value = "/findMembers", method = RequestMethod.POST, headers = "Accept=application/json")
	public @ResponseBody List<User> findMembers(@RequestBody Search searchText) {
		System.out.println("User list Requested - findMembers" + searchText);
		return userService.findMembers(searchText.getSearchText());
	}

	@RequestMapping(value = "/getPendingMembers", method = RequestMethod.POST, headers = "Accept=application/json")
	public @ResponseBody List<User> getPending() {
		System.out.println("User list Requested -getPending ");
		return userService.loadAllMembersPending();
	}

	@RequestMapping(value = "/getLeaders", method = RequestMethod.POST, headers = "Accept=application/json")
	public @ResponseBody List<User> getLeaders() {
		System.out.println("User list Requested -getPending ");
		return userService.getLeaders();
	}

	@RequestMapping(value = "/getAllMembers", method = RequestMethod.POST, headers = "Accept=application/json")
	public @ResponseBody List<User> getAllMembers() {
		System.out.println("User list Requested - getAllMembers");
		return userService.loadAllMembers();
	}

	@RequestMapping(value = "/approveMember", method = RequestMethod.POST, headers = "Accept=application/json")
	public @ResponseBody String approveMember(@RequestBody User user) {
		System.out.println("Member Approval" + user);
		user.setStatus((short) 1);
		userService.update(user, user);
		try {
			String mail = "<blockquote><h2><b>Cher Membre</b></h2><h2><span style=\"color: inherit;\">Nous somme "
					+ "heureux de vous annoncer que votre demande d'adhesion a ete acceptee. Restez aux nouvelles de l'association en visitant"
					+ " <a href=\"" + userService.getConfig("ORG_WEBSITE").getValue() + "\" target=\"\">"
					+ userService.getConfig("ORG_WEBSITE").getValue()
					+ "</a> </span><br/></h2><h2>Encore une fois, merci de votre interet en notre association.</h2><h2><b>Le President.</b></h2></blockquote>";
			SimpleMail.sendMail(
					"Votre demande d'adhesion a " + userService.getConfig("ORG_NAME").getValue() + " Approvee", mail,
					userService.getConfig("ORG_EMAIL").getValue(), user.getEmail(),
					userService.getConfig("ORG_SMTP").getValue(), userService.getConfig("ORG_EMAIL").getValue(),
					userService.getConfig("ORG_EMAIL_PASSWORD").getValue());
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return "success";
	}

	@RequestMapping(value = "/rejectMember", method = RequestMethod.POST, headers = "Accept=application/json")
	public @ResponseBody String rejectMember(@RequestBody User user) {
		System.out.println("rejectMember" + user);
		user.setStatus((short) 1);
		userService.update(user, user);
		try {
			String mail = "<blockquote><h2><b>Cher Membre</b></h2><h2><span style=\"color: inherit;\">Nous somme desole de vous annoncer que "
					+ "votre demande d'adhesion a ete rejetee. Restez aux nouvelles de l'association en visitant"
					+ " <a href=\"" + userService.getConfig("ORG_WEBSITE").getValue() + "\" target=\"\">"
					+ userService.getConfig("ORG_WEBSITE").getValue() + "</a> </span><br/></h2><h2>Encore une fois, "
					+ "merci de votre interet en notre association.</h2><h2><b>Le President.</b></h2></blockquote>";
			SimpleMail.sendMail("Votre demande d'adhesion a UKAD eV Rejetee", mail,
					userService.getConfig("ORG_EMAI").getValue(), user.getEmail(),
					userService.getConfig("ORG_SMTP").getValue(), userService.getConfig("ORG_EMAIL").getValue(),
					userService.getConfig("ORG_EMAIL_PASSWORD").getValue());
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return "success";
	}

	@RequestMapping(value = "/sendMail", method = RequestMethod.POST, headers = "Accept=application/json")
	public @ResponseBody String sendMail(@RequestBody Mail mail) {
		System.out.println("Mail Being sent");
		if (mail == null || mail.getBody() == null || mail.getSubject() == null || mail.getSender() == null) {
			return "Failure";
		}
		List<User> users = userService.loadAllMembers();
		StringBuffer sb = new StringBuffer();

		for (User user : users) {
			sb.append(user.getEmail() + ",");
		}

		try {
			SimpleMail.sendMail(mail.getSubject(), mail.getBody(), mail.getSender().getEmail(),
					sb.substring(0, sb.length() - 1), userService.getConfig("ORG_SMTP").getValue(),
					userService.getConfig("ORG_EMAIL").getValue(),
					userService.getConfig("ORG_EMAIL_PASSWORD").getValue());

			mail.setStatus((short) 1);
			userService.save(mail, mail.getSender());
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return "Success";
	}

	@RequestMapping(value = "/saveReportAndMail", method = RequestMethod.POST, headers = "Accept=application/json")
	public @ResponseBody String saveReportAndMail(@RequestBody Mail mail) {
		System.out.println("Mail Being sent");
		if (mail == null || mail.getBody() == null || mail.getSubject() == null || mail.getSender() == null
				|| mail.getEventId() == null) {
			return "Failure";
		}

		Event event = (Event) userService.getById(Event.class, mail.getEventId());

		event.setReport(mail.getBody());

		List<User> users = userService.loadAllMembers();
		StringBuffer sb = new StringBuffer();

		for (User user : users) {
			sb.append(user.getEmail() + ",");
		}

		try {
			SimpleMail.sendMail(mail.getSubject(), mail.getBody(), mail.getSender().getEmail(),
					sb.substring(0, sb.length() - 1), userService.getConfig("ORG_SMTP").getValue(),
					userService.getConfig("ORG_EMAIL").getValue(),
					userService.getConfig("ORG_EMAIL_PASSWORD").getValue());

			mail.setStatus((short) 1);
			userService.save(mail, mail.getSender());
			userService.update(event, mail.getSender());
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return "Success";
	}

	@RequestMapping(value = "/getYearlySummary", method = RequestMethod.POST, headers = "Accept=application/json")
	public @ResponseBody List<YearlySummary> getYearlySummary() {
		System.out.println("YearlySummary list Requested - YearlySummary");
		return userService.getYearlySmry();
	}

	@RequestMapping(value = "/getBudgetCash", method = RequestMethod.POST, headers = "Accept=application/json")
	public @ResponseBody List<BudgetCash> getBudgetCash() {
		System.out.println("BudgetCash list Requested - getBudgetCash");
		return userService.getBudgetCash();
	}

	@RequestMapping(value = "/getAllExpenses", method = RequestMethod.POST, headers = "Accept=application/json")
	public @ResponseBody List<Transaction> getAllExpenses() {
		System.out.println("YearlySummary list Requested - getAllExpenses");
		return userService.getAllExpenses();
	}

	@RequestMapping(value = "/deleteExpense", method = RequestMethod.POST, headers = "Accept=application/json")
	public @ResponseBody String deleteExpense(@RequestBody Transaction exp) {
		System.out.println("delete deleteExpense:" + exp);
		userService.delete(exp);
		return "Success";
	}

	@RequestMapping(value = "/getContributions", method = RequestMethod.POST, headers = "Accept=application/json")
	public @ResponseBody List<Contribution> getContributions() {
		System.out.println("getContributions list Requested - getContributions");
		return userService.getContributions();
	}

	@RequestMapping(value = "/getChildren", method = RequestMethod.POST, headers = "Accept=application/json")
	public @ResponseBody List<User> getChildren(@RequestBody User user) {
		// reload user as some values are missing
		// user=(User)userService.getById(User.class, user.getId());
		System.out.println("List Requested - getChildren");
		List<User> kids = new ArrayList<User>();
		try {
			List<BaseEntity> l = null;
			if (user.getSex().equals("M")) {

				l = userService.loadAllByColumn(User.class, "dad.id", user.getId());
			} else {
				l = userService.loadAllByColumn(User.class, "mum.id", user.getId());
			}
			if (l != null) {
				for (BaseEntity b : l) {
					kids.add((User) b);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return kids;
	}

	@RequestMapping(value = "/getSiblings", method = RequestMethod.POST, headers = "Accept=application/json")
	public @ResponseBody Set<User> getSiblings(@RequestBody User user) {
		System.out.println("List Requested - getSiblings");
		// reload user as some values are missing
		// user=(User)userService.getById(User.class, user.getId());
		Set<User> siblings = new HashSet<User>();
		try {
			List<BaseEntity> l = null;
			if (user.getDad() != null) {
				l = userService.loadAllByColumn(User.class, "dad.id", user.getDad().getId());
				if (l != null) {
					for (BaseEntity b : l) {
						// same Mum
						if (!user.equals((User) b)
								&& (((User) b).getMum() != null && ((User) b).getMum().equals(user.getMum())))
							siblings.add((User) b);
					}
				}
			}
			if (user.getMum() != null) {
				l = userService.loadAllByColumn(User.class, "mum.id", user.getMum().getId());
				if (l != null) {
					for (BaseEntity b : l) {
						if (!user.equals((User) b)
								&& (((User) b).getDad() != null && ((User) b).getDad().equals(user.getDad())))
							siblings.add((User) b);
					}
				}

			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		return siblings;
	}

	@RequestMapping(value = "/getSpouses", method = RequestMethod.POST, headers = "Accept=application/json")
	public @ResponseBody List<User> getSpouses(@RequestBody User user) {
		System.out.println("List Requested - getSpouses");
		// reload user as some values are missing
		// user=(User)userService.getById(User.class, user.getId());
		List<User> spouses = new ArrayList<User>();
		try {
			List<BaseEntity> l = null;
			if (user.getSex().equals("M")) {

				l = userService.loadAllByColumn(Mariage.class, "husband.id", user.getId());
				if (l != null) {
					for (BaseEntity b : l) {
						spouses.add(((Mariage) b).getWife());
					}
				}
			} else {
				l = userService.loadAllByColumn(Mariage.class, "wife.id", user.getId());
				if (l != null) {
					for (BaseEntity b : l) {
						spouses.add(((Mariage) b).getHusband());
					}
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		return spouses;
	}

	@RequestMapping(value = "/saveFamLink", method = RequestMethod.POST, headers = "Accept=application/json")
	public @ResponseBody String saveFamLink(@RequestBody User user) {
		System.out.println("Requested - sameFamLink");

		String data[] = user.getData().split(",");
		
		if (data[1].equals("0")) {// remove all links
			// reload user
			user = (User) userService.getById(User.class, user.getId());
			User second = (User) userService.getById(User.class, new Long(data[0]));
			String message = "Vous n'aviez aucun lien de toute facon";
			// not my kid
			if (second.getMum() != null && second.getMum().equals(user)) {
				message = user.getFirstName()+" n'est plus maman de "+second.getFirstName();
				second.setMum(null);
				userService.save(second);
			}
			if (second.getDad() != null && second.getDad().equals(user)) {
				message = user.getFirstName()+" n'est plus papa de "+second.getFirstName();
				second.setDad(null);
				userService.save(second);
			}

			if (user.getMum() != null && user.getMum().equals(second)) {				
				//reload user
				user = (User) userService.getById(User.class, user.getId());
				user.setMum(null);
				userService.save(user);
				message = second.getFirstName()+" n'est plus maman de "+user.getFirstName();
			}
			if (user.getDad() != null && user.getDad().equals(second)) {				
				//reload user
				user = (User) userService.getById(User.class, user.getId());
				user.setDad(null);
				userService.save(user);
				message = second.getFirstName()+" n'est plus papa de "+user.getFirstName();
			}

			try {
				List<BaseEntity> l = null;
				if (user.getSex().equals("M")) {
					l = userService.loadAllByColumn(Mariage.class, "husband.id", user.getId());
					if (l != null) {
						for (BaseEntity b : l) {
							if (((Mariage) b).getWife().equals(second)) {
								userService.delete(b);
								message = "Vous n'etes plus maries";
								break;
							}
							;
						}
					}
				} else {
					l = userService.loadAllByColumn(Mariage.class, "wife.id", user.getId());
					if (l != null) {
						for (BaseEntity b : l) {
							if (((Mariage) b).getHusband().equals(second)) {
								userService.delete(b);
								message = "Vous n'etes plus maries";
								break;
							}
							;
						}
					}
				}

			} catch (Exception e) {
				e.printStackTrace();
			}
			return message;
		} else if (data[1].equals("1")) {// son
			User second = (User) userService.getById(User.class, new Long(data[0]));
			if (user.getSex().equals("M")) {
				second.setDad(user);
				userService.save(second);
				return user.getFirstName()+" est maintenant Papa de "+second.getFirstName();
			} else {
				second.setMum(user);
				userService.save(second);
				return user.getFirstName()+" est maintenant Maman de "+second.getFirstName();
			}
			
		} else if (data[1].equals("2")) {// Epouse
			User second = (User) userService.getById(User.class, new Long(data[0]));
			if(second.getSex()!=null&&user.getSex()!=null&&second.getSex().equals(user.getSex())){
				return user.getFirstName()+" et "+second.getFirstName() +" sont de meme sexe! Pas de mariage gay ici :-(";
			}
			if(second.getSex()!=null&&second.getSex().equals("M")){
				return second.getFirstName()+" ne peux pas etre une epouse. C'est un homme!";
			}
			Mariage ma = new Mariage();
			ma.setHusband(user);
			ma.setWife(second);
			try {
				userService.save(ma);
			} catch (Exception e) {
				return user.getFirstName()+" et "+second.getFirstName() +" sont deja Mari et femme";
			}
			return user.getFirstName()+" et "+second.getFirstName() +" sont maintenant Mari et femme";
		} else if (data[1].equals("3")) {// Epoux
			User second = (User) userService.getById(User.class, new Long(data[0]));
			if(second.getSex()!=null&&user.getSex()!=null&&second.getSex().equals(user.getSex())){
				return user.getFirstName()+" et "+second.getFirstName() +" sont de meme sexe! Pas de mariage gay ici :-(";
			}
			if(second.getSex()!=null&&second.getSex().equals("F")){
				return second.getFirstName()+" ne peux pas etre un epoux. C'est une femme!";
			}
			Mariage ma = new Mariage();
			ma.setHusband(second);
			ma.setWife(user);
			try {
				userService.save(ma);
			} catch (Exception e) {
				return user.getFirstName()+" et "+second.getFirstName() +" sont deja Mari et femme";
			}
			return user.getFirstName()+" et "+second.getFirstName() +" sont maintenant Mari et femme";
		} else if (data[1].equals("4")) {// Maman
			User second = (User) userService.getById(User.class, new Long(data[0]));
			// reload user
			user = (User) userService.getById(User.class, user.getId());
			if(second.getSex()!=null&&second.getSex().equals("P")){
				return second.getFirstName()+" ne peux pas etre une maman. C'est un homme!";
			}
			user.setMum(second);
			userService.save(user);

			return second.getFirstName()+" est maintenant Maman de "+user.getFirstName();
		} else if (data[1].equals("5")) {// Papa
			User second = (User) userService.getById(User.class, new Long(data[0]));
			// reload user
			user = (User) userService.getById(User.class, user.getId());
			if(second.getSex()!=null&&second.getSex().equals("F")){
				return second.getFirstName()+" ne peux pas etre un Papa. C'est une femme!";
			}
			user.setDad(second);
			userService.save(user);
			return second.getFirstName()+" est maintenant Papa de "+user.getFirstName();
		} else {

			return "Operation non connue";
		}
	}
}
