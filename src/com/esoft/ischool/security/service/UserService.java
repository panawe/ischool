package com.esoft.ischool.security.service;

import java.util.List;

import com.esoft.ischool.model.BaseEntity;
import com.esoft.ischool.model.BudgetCash;
import com.esoft.ischool.model.Transaction;
import com.esoft.ischool.security.model.Contribution;
import com.esoft.ischool.security.model.Menu;
import com.esoft.ischool.security.model.Roles;
import com.esoft.ischool.security.model.User;
import com.esoft.ischool.service.BaseService;

public interface UserService extends BaseService {
	public List<Long> getGroupeIdsByUser(Long utilisateurId);

	public User getUser(String nom, String password);

	public void savePickedList(Long loginId, Long userId, List<Long> availableItemKeys, List<Long> selectedItemKeys);

	public List<Menu> getSubMenus(Long parentId);

	public List<Long> getRolesIdsForUser(Long idParameter, Class<User> class1);

	public List<Roles> getAllRoles();

	public void add(User user);

	public List<User> loadAllMembers();

	public List<User> loadAllMembersPending();

	public List<User> findMembers(String searchText);

	public List<User> getLeaders();

	public List<Transaction> getAllExpenses();

	public List<Contribution> getContributions();
	
	public List<User> loadAllMembersWithOnlineStatus();
	public List<User> findMembers(String firstName, String lastName, String login, String email);

	public List<BudgetCash> getBudgetCash();
}
