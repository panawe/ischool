package com.esoft.ischool.security.service;

import java.util.List;

import com.esoft.ischool.model.AlertReceiver;
import com.esoft.ischool.model.BaseEntity;
import com.esoft.ischool.model.Question;
import com.esoft.ischool.model.School;
import com.esoft.ischool.model.UserTest;
import com.esoft.ischool.security.model.Menu;
import com.esoft.ischool.security.model.Roles;
import com.esoft.ischool.security.model.User;
import com.esoft.ischool.service.BaseService;

public interface UserService extends BaseService {
	public List<Long> getGroupeIdsByUser(Long utilisateurId);

	public User getUser(String nom, String password);

	public void savePickedList(Long loginId, Long userId,
			List<Long> availableItemKeys, List<Long> selectedItemKeys);
	
	public  List<Menu> getSubMenus(Long parentId);
	public List<UserTest> getUserTests(User user);
	public List<Question> getQuestions(Long testId);

	public List<Long> getRolesIdsForUser(Long idParameter, Class<User> class1);

	public List<Roles> getAllRoles();

	public void add(User user);
	
	public List<BaseEntity> getPendingQuestions(Long testId, Long userTestId);

	public List<AlertReceiver>  getUserAlerts(User user, School school);

	public List<BaseEntity> getUnaprovedAssignments(User user);

	public List<BaseEntity> getPendingDemandes(School school);
}
