package com.esoft.ischool.restservice;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;

import javax.annotation.PostConstruct;
import javax.faces.model.SelectItem;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.esoft.ischool.model.BaseEntity;
import com.esoft.ischool.model.Student;
import com.esoft.ischool.model.Teacher;
import com.esoft.ischool.security.model.Menu;
import com.esoft.ischool.security.model.Roles;
import com.esoft.ischool.security.model.RolesMenu;
import com.esoft.ischool.security.model.RolesUser;
import com.esoft.ischool.security.model.User;
import com.esoft.ischool.service.BaseService;
import com.esoft.ischool.util.MenuIdEnum;
import com.mysql.jdbc.StringUtils;

@Component("roleBean")
@Scope("session")
public class RoleBean extends BaseBean {

	@Autowired
	@Qualifier("baseService")
	private BaseService baseService;
	private Long rowCount;
	private Long userRowCount;
	private Long roleCount;

	private List<BaseEntity> roles;
	private List<BaseEntity> availableMenus = new ArrayList<BaseEntity>();
	private List<BaseEntity> selectedMenus = new ArrayList<BaseEntity>();

	private List<BaseEntity> availableUsers = new ArrayList<BaseEntity>();
	private List<BaseEntity> selectedUsers = new ArrayList<BaseEntity>();

	private List<String> allRoles = new ArrayList<String>();
	private Roles role = new Roles();

	private List<BaseEntity> menuUsers;
	private List<BaseEntity> menuRoles;

	private boolean selectAllMenus;
	private boolean selectAllUsers;
	private boolean selectAllTeachers;
	private boolean selectAllStudents;
	private String year;
	private String className;
	
	private boolean boxChecked;
	private boolean userBoxChecked;
	private boolean individual;

	private String selectedTab = "roleList";
	private String menuName;
	private String userName;
	private String firstName;
	private String lastName;

	private Long roleId;
	
	public List<String> autoComplete(Object suggest) {
		allRoles = new ArrayList<String>();
		for (BaseEntity entity : roles) {
			Roles r = (Roles) entity;
			if (role.getName().equalsIgnoreCase(suggest.toString()))
				allRoles.add(r.getName());
		}
		return allRoles;
	}

	public String updateUserSelection() {
		if (selectAllUsers) {
			selectAllTeachers = false;
			selectAllStudents = false;
			userBoxChecked = true;
		} else {
			userBoxChecked = false;
		}
		return "Success";
	}
 
	public String updateTeacherSelection() {
		if (selectAllTeachers) {
			selectAllStudents = false;
			selectAllUsers = false;
			userBoxChecked = true;
		} else {
			userBoxChecked = false;
		}

		return "Success";
	}

	public String updateStudentSelection() {
		if (selectAllStudents) {
			selectAllTeachers = false;
			selectAllUsers = false;
			userBoxChecked = true;
		} else {
			userBoxChecked = false;
		}
		return "Success";
	}

	
	@Override
	public String getSelectedTab() {
		return selectedTab;
	}

	@Override
	public void setSelectedTab(String selectedTab) {
		this.selectedTab = selectedTab;
	}

	public boolean isBoxChecked() {
		return boxChecked;
	}

	public void setBoxChecked(boolean boxChecked) {
		this.boxChecked = boxChecked;
	}

	public boolean isSelectAllMenus() {
		return selectAllMenus;
	}

	public void setSelectAllMenus(boolean selectAllMenus) {
		this.selectAllMenus = selectAllMenus;
	}

	public boolean isSelectAllUsers() {
		return selectAllUsers;
	}

	public void setSelectAllUsers(boolean selectAllUsers) {
		this.selectAllUsers = selectAllUsers;
	}

	public boolean isSelectMenus() {
		return selectAllMenus;
	}

	public void setSelectMenus(boolean selectAllMenus) {
		this.selectAllMenus = selectAllMenus;
	}

	public String validate() {
		return "succes";
	}

	@Override
	public String clear() {
		role = new Roles();
		return "Success";
	}

	public String clearRoleUser() {
		populateUsers();
		return "Success";
	}

	public String clearRoleMenu() {
		populateMenus();
		return "Success";
	}

	public String getShowAll() {
		getAll();
		return "Success";
	}

	public String delete() {
		clearMessages();
		try {
			baseService.delete(getIdParameter(), Roles.class);
			getAll();
			clear();
			setSuccessMessage(getResourceBundle().getString("DELETE_SUCCESSFULLY"));
		} catch (Exception ex) {
			setErrorMessage(getResourceBundle().getString("DELETE_UNSUCCESSFULL"));
		}

		return "Success";
	}

	public String insert() {
		clearMessages();
		Long id = role.getId();
		try {
			if (id == null || id == 0)
				baseService.save(role, getCurrentUser());
			else
				baseService.update(role, getCurrentUser());

			setSuccessMessage(getResourceBundle().getString("SAVED_SUCCESSFULLY"));
			getAll();
		} catch (Exception ex) {
			ex.printStackTrace();
			role.setId(id);
			setErrorMessage(ex, "Ce role exist deja. ");
		}

		return "S";
	}

	public String insertRoleMenu() {
		clearMessages();
		try {
			// Save selectedMenus
			Roles role = (Roles) baseService.getById(Roles.class, roleId);
			baseService.saveRoleMenus(role, selectedMenus, getCurrentUser());		
			setSuccessMessage(getResourceBundle().getString("SAVED_SUCCESSFULLY"));
			populateMenus();
		} catch (Exception ex) {
			ex.printStackTrace();
			setErrorMessage(ex, "Cet assignement exist deja. ");
		}

		return "S";
	}
	
	public String insertRoleUser() {
		clearMessages();
		try {
			// Save selectedUsers
			Roles role = (Roles) baseService.getById(Roles.class, roleId);
			baseService.saveRoleUsers(role, selectedUsers, getCurrentUser());			
			setSuccessMessage(getResourceBundle().getString("SAVED_SUCCESSFULLY"));
			populateUsers();
		} catch (Exception ex) {
			ex.printStackTrace();
			setErrorMessage(ex, "Cet assignement exist deja. ");
		}

		return "S";
	}
	
	public String edit() {
		clearMessages();
		selectedTab = "roleDetails";
		role = (Roles) baseService.getById(Roles.class, getIdParameter());
		return "Success";
	}
	
	@PostConstruct
	private void getAll() {
		roles = baseService.loadAll(Roles.class, getCurrentUser().getSchool());
		setRowCount(new Long(roles.size()));
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

	public String updateMenuSelection() {
		if (selectAllMenus) {
			boxChecked = true;
		} else {
			boxChecked = false;
		}

		return "Success";
	}

	public String populateMenus() {
		availableMenus = null;
		List<BaseEntity> selectedRolesMenus = null;
		if (selectAllMenus) {
			availableMenus = baseService.loadAll(Menu.class, getCurrentUser().getSchool());
		}
		else if (!StringUtils.isEmptyOrWhitespaceOnly(menuName)) {
			availableMenus = baseService.getMenus(Menu.class, menuName);
		}
		
		if (availableMenus == null)
			availableMenus = new ArrayList<BaseEntity>();
		selectedRolesMenus = baseService.loadAllByParentId(RolesMenu.class, "roles", "id", roleId);
		
		selectedMenus = baseService.getMenusFromRolesMenus(selectedRolesMenus);
		
		availableMenus.removeAll(selectedMenus);

		rowCount = new Long(availableMenus != null ? availableMenus.size() : 0);
		return "Success";
	}
	
	
	public String populateUsers() {
		availableUsers = null;
		List<BaseEntity> selectedRolesUsers = null;
		if (userBoxChecked) {
			if (selectAllUsers)
				availableUsers = baseService.loadAll(User.class, getCurrentUser().getSchool());
			else if (selectAllStudents) {
				List<Student> availableStudents = baseService.searchStudents(
						selectAllStudents, className, year, getCurrentUser().getSchool());
				for (Student student : availableStudents) {
					availableUsers = new ArrayList<BaseEntity>();
					availableUsers.add(student.getUser());
				}
					
			}
			else {
				List<Teacher> availableTeachers = baseService.searchTeachers(
						selectAllTeachers, className, year, getCurrentUser().getSchool());
				for (Teacher teacher : availableTeachers) {
					availableUsers = new ArrayList<BaseEntity>();
					availableUsers.add(teacher.getUser());
				}
			}
		}
		else if (!StringUtils.isEmptyOrWhitespaceOnly(userName) || !StringUtils.isEmptyOrWhitespaceOnly(firstName) || !StringUtils.isEmptyOrWhitespaceOnly(lastName)) {
			availableUsers = baseService.getUsers(User.class, userName, lastName, firstName, getCurrentUser());
		}
		else {
			List<Student> availableStudents = baseService.searchStudents(selectAllStudents,
					className, year, getCurrentUser().getSchool());
			if (availableStudents == null)
				availableStudents = new ArrayList<Student>();
			for (Student student : availableStudents) {
				availableUsers = new ArrayList<BaseEntity>();
				availableUsers.add(student.getUser());
			}
		}
		
		if (availableUsers == null)
			availableUsers = new ArrayList<BaseEntity>();
		selectedRolesUsers = baseService.loadAllByParentId(RolesUser.class, "roles", "id", roleId);
		
		selectedUsers = baseService.getUsersFromRolesUsers(selectedRolesUsers);
		
		availableUsers.removeAll(selectedUsers);

		rowCount = new Long(availableUsers != null ? availableUsers.size() : 0);
		return "Success";
	}

	public List<SelectItem> getSelectedRoles() {
		getAll();
		List<SelectItem> selecteds = new ArrayList<SelectItem>();
		//selectAllUsers  = true;
		//selectAllMenus = true;
		selecteds.add(new SelectItem("", ""));
		
		for (BaseEntity entity : roles) {
			Roles role = (Roles)entity;
			selecteds.add(new SelectItem(role.getId(), role.getName()));
		}

		return selecteds;
	}
	
	public boolean isUserHasWriteAccess() {
		if ( ((String)getSessionParameter("link")).equals("student")) {
			return isUserHasWriteAccess(MenuIdEnum.STUDENT.getValue());
		} else if ( ((String)getSessionParameter("link")).equals("teacher")) {
			return isUserHasWriteAccess(MenuIdEnum.TEACHER.getValue());
		} else if (((String)getSessionParameter("link")).equals("inscriptions")) {
			return isUserHasWriteAccess(MenuIdEnum.INSCRIPTIONS.getValue());
		}
		return false;
	}
	
	
	public String doNothing() {
		return "Success";
	}

	public boolean isIndividual() {
		return individual;
	}

	public void setIndividual(boolean individual) {
		this.individual = individual;
	}

	public Long getRoleCount() {
		return roleCount;
	}

	public void setRoleCount(Long roleCount) {
		this.roleCount = roleCount;
	}

	public List<BaseEntity> getRoles() {
		return roles;
	}

	public void setRoles(List<BaseEntity> roles) {
		this.roles = roles;
	}

	public List<BaseEntity> getAvailableMenus() {
		return availableMenus;
	}

	public void setAvailableMenus(List<BaseEntity> availableMenus) {
		this.availableMenus = availableMenus;
	}

	public List<BaseEntity> getSelectedMenus() {
		return selectedMenus;
	}

	public void setSelectedMenus(List<BaseEntity> selectedMenus) {
		this.selectedMenus = selectedMenus;
	}

	public List<BaseEntity> getAvailableUsers() {
		return availableUsers;
	}

	public void setAvailableUsers(List<BaseEntity> availableUsers) {
		this.availableUsers = availableUsers;
	}

	public List<BaseEntity> getSelectedUsers() {
		return selectedUsers;
	}

	public void setSelectedUsers(List<BaseEntity> selectedUsers) {
		this.selectedUsers = selectedUsers;
	}

	public List<String> getAllRoles() {
		return allRoles;
	}

	public void setAllRoles(List<String> allRoles) {
		this.allRoles = allRoles;
	}

	public Roles getRole() {
		return role;
	}

	public void setRole(Roles role) {
		this.role = role;
	}

	public List<BaseEntity> getMenuUsers() {
		return menuUsers;
	}

	public void setMenuUsers(List<BaseEntity> menuUsers) {
		this.menuUsers = menuUsers;
	}

	public List<BaseEntity> getMenuRoles() {
		return menuRoles;
	}

	public void setMenuRoles(List<BaseEntity> menuRoles) {
		this.menuRoles = menuRoles;
	}

	public String getMenuName() {
		return menuName;
	}

	public void setMenuName(String menuName) {
		this.menuName = menuName;
	}

	public boolean isUserBoxChecked() {
		return userBoxChecked;
	}

	public void setUserBoxChecked(boolean userBoxChecked) {
		this.userBoxChecked = userBoxChecked;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public Long getUserRowCount() {
		return userRowCount;
	}

	public void setUserRowCount(Long userRowCount) {
		this.userRowCount = userRowCount;
	}

	public Long getRoleId() {
		return roleId;
	}

	public void setRoleId(Long roleId) {
		this.roleId = roleId;
	}

	public boolean isSelectAllTeachers() {
		return selectAllTeachers;
	}

	public void setSelectAllTeachers(boolean selectAllTeachers) {
		this.selectAllTeachers = selectAllTeachers;
	}

	public boolean isSelectAllStudents() {
		return selectAllStudents;
	}

	public void setSelectAllStudents(boolean selectAllStudents) {
		this.selectAllStudents = selectAllStudents;
	}

	public String getYear() {
		return year;
	}

	public void setYear(String year) {
		this.year = year;
	}

	public String getClassName() {
		return className;
	}

	public void setClassName(String className) {
		this.className = className;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}
}