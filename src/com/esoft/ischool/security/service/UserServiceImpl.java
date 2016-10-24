package com.esoft.ischool.security.service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.esoft.ischool.model.AlertReceiver;
import com.esoft.ischool.model.BaseEntity;
import com.esoft.ischool.model.Question;
import com.esoft.ischool.model.School;
import com.esoft.ischool.model.UserTest;
import com.esoft.ischool.security.dao.UserDaoImpl;
import com.esoft.ischool.security.model.Roles;
import com.esoft.ischool.security.model.RolesUser;
import com.esoft.ischool.security.model.Menu;
import com.esoft.ischool.security.model.User;
import com.esoft.ischool.service.*;

@Service("userService")

public class UserServiceImpl extends BaseServiceImpl implements
		UserService {

	@Autowired
	@Qualifier("userDao")
	private UserDaoImpl userDao;

	@Transactional(readOnly = true)
	public List<Long> getRolesIdsByUser(Long userId) {
		return userDao
				.getRolesUserListByUser(userId);
	}

	@Transactional(readOnly = false)
	public User getUser(String nom, String password) {
		return userDao.getUser(nom, password);
	}

	@Transactional(readOnly = false)
	public void savePickedList(Long loginId, Long userId,
			List<Long> availableItemKeys, List<Long> selectedItemKeys) {
		Set<RolesUser> groupUsersToAdd = new HashSet<RolesUser>();
		//userDao.setUserId(loginId);
		User user = (User) userDao.getById(
				User.class, userId);
		// load the current associations.
		List<Long> groupIds = userDao
				.getRolesUserListByUser(userId);

		// Check to see if any group get associated to the user after we
		// retrieved our pickList.
		for (Long groupId : groupIds) {
			if (!(availableItemKeys.contains(groupId) || selectedItemKeys
					.contains(groupId)))
				selectedItemKeys.add(groupId);

			if (availableItemKeys.contains(groupId))
				userDao.deleteRolesUser(userId, groupId);

			if (selectedItemKeys.contains(groupId))
				selectedItemKeys.remove(groupId);

		}

		for (Long groupId : selectedItemKeys) {
			Roles group = (Roles) userDao.getById(Roles.class,
					groupId);
			RolesUser gu = new RolesUser();
			gu.setRoles(group);
			gu.setUser(user);

			userDao.update(gu,user);
		}
	}

	public List<Menu> getSubMenus(Long parentId) {
		// TODO Auto-generated method stub
		return userDao.getSubMenus(parentId);
	}
	
	public List<UserTest> getUserTests(User user){		
		return userDao.getUserTests(user);
	}
	
	public List<Question> getQuestions(Long testId){
		
		return userDao.getQuestions(testId);
	}

	public List<Long> getGroupeIdsByUser(Long utilisateurId) {
		// TODO Auto-generated method stub
		return null;
	}

	public void add(User user) {
		// TODO Auto-generated method stub
		
	}

	public List<Roles> getAllRoles() {
		// TODO Auto-generated method stub
		return null;
	}

	public List<Long> getRolesIdsForUser(Long idParameter, Class<User> class1) {
		// TODO Auto-generated method stub
		return null;
	}

	public List<BaseEntity> getPendingQuestions(Long testId, Long userTestId) {
		// TODO Auto-generated method stub
		return userDao.getPendingQuestions(testId, userTestId);
	}
	
	public List<AlertReceiver>  getUserAlerts(User user, School school){
		return userDao.getUserAlerts(user, school);
	}
	
	public List<BaseEntity> getUnaprovedAssignments(User user){
		return userDao.getUnapprovedAssignments(user);
	}
	
	public List<BaseEntity> getPendingDemandes(School school){
		return userDao.getPendingDemandes(school);
	}
}
