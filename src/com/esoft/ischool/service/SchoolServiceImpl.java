package com.esoft.ischool.service;

import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import com.esoft.ischool.dao.SchoolDaoImpl;
import com.esoft.ischool.model.Configuration;
import com.esoft.ischool.model.Grade;
import com.esoft.ischool.model.Rating;
import com.esoft.ischool.model.Role;
import com.esoft.ischool.model.School;
import com.esoft.ischool.model.Student;
import com.esoft.ischool.model.Teacher;
import com.esoft.ischool.model.Term;
import com.esoft.ischool.security.model.Menu;
import com.esoft.ischool.security.model.Roles;
import com.esoft.ischool.security.model.RolesMenu;
import com.esoft.ischool.security.model.RolesUser;
import com.esoft.ischool.security.model.User;
import com.esoft.ischool.util.Constants;

@Service("schoolService")
public class SchoolServiceImpl extends BaseServiceImpl implements SchoolService {

	@Autowired
	@Qualifier("schoolDao")
	private SchoolDaoImpl schoolDao;

	public void saveSchool(School school) {
		schoolDao.saveSchool(school);
	}

	public void updateSchool(School school) {
		schoolDao.updateSchool(school);

	}

	public School getBySchoolId(Class<School> school, Long idParameter) {
		// TODO Auto-generated method stub
		return schoolDao.getBySchoolId(school, idParameter);
	}

	public List<School> loadAllSchool(Class<School> school) {
		// TODO Auto-generated method stub
		return schoolDao.loadAllSchool(school);
	}
	
	public void getTheFuckOutOfHereBastard(School school){
		schoolDao.delete(school);
	}
	
	public void delete(School school) throws Exception{
	
		List<Student> students = schoolDao.loadAll(Student.class,school);
		if(students.size()>0){
			throw new Exception("");
		}
		
		List<Teacher> teachers = schoolDao.loadAll(Teacher.class,school);
		if(teachers.size()>0){
			throw new Exception("");
		}
		
		//Event Type
/* 		List<EventType> eventTs = schoolDao.loadAll(EventType.class,school);
		for (EventType sl : eventTs) {
			schoolDao.delete(sl);
		} 
		
		// copy Level Class
 
		List<LevelClass> lClasses = schoolDao.loadAll(LevelClass.class,school);
		for (LevelClass lc : lClasses) {
			schoolDao.delete(lc);
		} */
		
		// copy school level
/*		List<Level> levels = schoolDao.loadAll(Level.class, school);
		for (Level sl : levels) {
			schoolDao.delete(sl);
		}*/

		// copy menu


		List<RolesMenu> roleMenus = schoolDao.loadAll(RolesMenu.class, school);
		for (RolesMenu roleMenu : roleMenus) {
			schoolDao.delete(roleMenu);
		}
		
		List<User> users = schoolDao.loadAll(User.class, school);
		for (User user : users) {
			schoolDao.delete(user);
		}
		// copy roles
		List<Roles> roles = schoolDao.loadAll(Role.class, school);
		for (Roles role : roles) {
				schoolDao.delete(role);
		}
		

		List<Menu> menus = schoolDao.loadAll(Menu.class, school);
		for (Menu menu : menus) {
				schoolDao.delete(menu);
		}

		// Copy Type Exam
/*		List<ExamType> examTypes = schoolDao.loadAll(ExamType.class,school);
		for (ExamType et : examTypes) {
			schoolDao.delete(et);
		}*/

		// Copy Term
		List<Term> terms = schoolDao.loadAll(Term.class, school);
		for (Term term : terms) {
			schoolDao.delete(term);
		}

		// Copy Subject
/*		List<Subject> subjects = schoolDao.loadAll(Subject.class,school);
		for (Subject sub : subjects) {
			schoolDao.delete(sub);
		}
*/
		// Copy Grade
		List<Grade> grades = schoolDao.loadAll(Grade.class, school);
		for (Grade grd : grades) {
			schoolDao.delete(grd);
		}

/*		List<City> cities = schoolDao.loadAll(City.class, school);
		for (City city : cities) {
			schoolDao.delete(city);
		}
		// Copy Country
		List<Country> countries = schoolDao.loadAll(Country.class,school);
		for (Country country : countries) {
			schoolDao.delete(country);
		}
*/
		// Copy Config
		List<Configuration> confs = schoolDao.loadAll(Configuration.class,school);
		for (Configuration conf : confs) {
			schoolDao.delete(conf);
		}
		// Copy Ratings
		List<Rating> ratings = schoolDao
				.loadAll(Rating.class, school);
		for (Rating rat : ratings) {
			schoolDao.delete(rat);
		}
		
		// delete  PayType
/*		List<PayType> pays = schoolDao
				.loadAll(PayType.class, school);
		for (PayType rat : pays) {
			schoolDao.delete(rat);
		}*/
		
		// delete result range
/*		List<SchoolYear> sys = schoolDao
				.loadAll(SchoolYear.class, school);
		for (SchoolYear sy : sys) {
			schoolDao.delete(sy);
		}
*/
/*
		List<SchoolReligion> sReligions = schoolDao.loadAll(
				SchoolReligion.class, school);
		for (SchoolReligion sr : sReligions) {
			schoolDao.delete(sr);
		}

		// copy school type
		List<SchoolType> sTypes = schoolDao.loadAll(SchoolType.class,school);
		for (SchoolType st : sTypes) {
			schoolDao.delete(st);
		}

		
		// copy school level
		List<SchoolLevel> schoolLevels = schoolDao.loadAll(SchoolLevel.class,school);
		for (SchoolLevel sl : schoolLevels) {
			schoolDao.delete(sl);
		}*/
	}

	public void copyGoldData(School school, User user, String adminPassword) {
		
		school.setModifiedBy(user.getId().intValue());
		schoolDao.saveSchool(school);

		//Event Type
/* 		List<EventType> eventTs = schoolDao.loadAll(EventType.class,
				user.getSchool());
		for (EventType sl : eventTs) {
			schoolDao.save(new EventType(sl), school, user);
		} */
		
		// copy school level
/*		List<SchoolLevel> schoolLevels = schoolDao.loadAll(SchoolLevel.class,
				user.getSchool());
		for (SchoolLevel sl : schoolLevels) {
			schoolDao.save(new SchoolLevel(sl), school, user);
		}*/

		// copy school level
/*		List<Level> levels = schoolDao.loadAll(Level.class, user.getSchool());
		for (Level sl : levels) {
			schoolDao.save(new Level(sl), school, user);
		}*/
		// copy Level Class

/*		List<LevelClass> lClasses = schoolDao.loadAll(LevelClass.class,
				user.getSchool());
		for (LevelClass lc : lClasses) {
			Level level = (Level) schoolDao.findByName(Level.class, lc
					.getLevel().getName(), school);
			schoolDao.save(new LevelClass(lc, level), school, user);

		}
*/
		// copy school religion
/*		List<SchoolReligion> sReligions = schoolDao.loadAll(
				SchoolReligion.class, user.getSchool());
		for (SchoolReligion sr : sReligions) {
			schoolDao.save(new SchoolReligion(sr), school, user);
		}

		// copy school type
		List<SchoolType> sTypes = schoolDao.loadAll(SchoolType.class,
				user.getSchool());
		for (SchoolType st : sTypes) {
			schoolDao.save(new SchoolType(st), school, user);
		}
*/
		// copy menu

		List<Menu> menus = schoolDao.loadAll(Menu.class, user.getSchool());
		for (Menu menu : menus) {
			// create parents first
			if (menu.getMenuParent() == null )
				schoolDao.save(new Menu(menu, null), school, user);
		}

		for (Menu menu : menus) {
			// create kids after
			if (menu.getMenuParent() != null&& menu.getId().intValue()!=29&& menu.getId().intValue()!=30) {
				// get Parent menu by name
				Menu menuP = (Menu) schoolDao.findByColumn(Menu.class,
						"securityCode", menu.getMenuParent().getSecurityCode(), school);
				schoolDao.save(new Menu(menu, menuP), school, user);

			}
		}

		// copy roles
		List<Roles> roles = schoolDao.loadAll(Roles.class, user.getSchool());
		for (Roles role : roles) {
			if (role.getId()> 1 && role.getId()<= 4) {
				Roles localRole = new Roles(role);
				schoolDao.save(localRole, school, user);

				Set<RolesMenu> roleMenus = role.getRolesMenus();
				for (RolesMenu roleMenu : roleMenus) {
					Menu localMenu = (Menu) schoolDao.findByColumn(Menu.class,
							"securityCode", roleMenu.getMenu()
									.getSecurityCode(), school);

					RolesMenu rMenu = new RolesMenu();
					rMenu.setAccessLevel(roleMenu.getAccessLevel());
					rMenu.setMenu(localMenu);
					rMenu.setRoles(localRole);
					schoolDao.save(rMenu, school, user);
				}

				if (role.getId().equals(Constants.ADMIN_ROLE_ID)) {

					User adminUser = new User();
					adminUser.setFirstName(Constants.ADMIN_FIRST_NAME);
					adminUser.setLastName(school.getName());
					adminUser.setPageSkin(Constants.DEFAULT_PAGE_SKIN);
					adminUser.setEmail(school.getEmail());
					adminUser.setUserName(school.getEmail());
					adminUser.setStatus(Constants.ACTIVE_STATUS);
					adminUser.setPhone(school.getPhone());
					adminUser.setPassword(adminPassword);
					schoolDao.save(adminUser, school, user);

					RolesUser ru = new RolesUser();
					ru.setRoles(localRole);
					ru.setUser(adminUser);
					schoolDao.save(ru, school, user);

				}

			}
		}

		// Copy Type Exam
/*		List<ExamType> examTypes = schoolDao.loadAll(ExamType.class,
				user.getSchool());
		for (ExamType et : examTypes) {
			schoolDao.save(new ExamType(et), school, user);
		}*/

		// Copy Term
/*		List<Term> terms = schoolDao.loadAll(Term.class, user.getSchool());
		for (Term term : terms) {
			schoolDao.save(new Term(term), school, user);
		}
*/
		// Copy Subject
/*		List<Subject> subjects = schoolDao.loadAll(Subject.class,
				user.getSchool());
		for (Subject sub : subjects) {
			schoolDao.save(new Subject(sub), school, user);
		}*/

		// Copy Grade
		List<Grade> grades = schoolDao.loadAll(Grade.class, user.getSchool());
		for (Grade grd : grades) {
			schoolDao.save(new Grade(grd), school, user);
		}

/*		// Copy Country
		List<Country> countries = schoolDao.loadAll(Country.class,
				user.getSchool());
		for (Country country : countries) {
			schoolDao.save(new Country(country), school, user);
		}

		// Copy Cities
		List<City> cities = schoolDao.loadAll(City.class, user.getSchool());
		for (City city : cities) {
			Country country = (Country) schoolDao.findByName(Country.class,
					city.getCountry().getName(), school);
			schoolDao.save(new City(city, country), school, user);
		}*/

		// Copy Config
		List<Configuration> confs = schoolDao.loadAll(Configuration.class,
				user.getSchool());
		for (Configuration conf : confs) {
			schoolDao.save(new Configuration(conf), school, user);
		}
		// Copy Ratings
		List<Rating> ratings = schoolDao
				.loadAll(Rating.class, user.getSchool());
		for (Rating rat : ratings) {
			schoolDao.save(new Rating(rat), school, user);
		}
		
/*		// Copy pay type
		List<PayType> pays = schoolDao
				.loadAll(PayType.class, user.getSchool());
		for (PayType rat : pays) {
			schoolDao.save(new PayType(rat), school, user);
		}*/
		
		// Copy result range
/*		List<SchoolYear> sys = schoolDao
				.loadAll(SchoolYear.class, user.getSchool());
		for (SchoolYear sy : sys) {
			schoolDao.save(new SchoolYear(sy), school, user);
		}*/
	}


	public School getSchool(Long idParameter) {
		// TODO Auto-generated method stub
		return schoolDao.getSChool(idParameter);
	}

}
