package com.esoft.ischool.service;

import java.util.List;

import org.springframework.transaction.annotation.Transactional;
import com.esoft.ischool.model.School;
import com.esoft.ischool.security.model.User;

@Transactional(readOnly = true)
public interface SchoolService  extends BaseService {
	
	@Transactional(readOnly = false)
	public void saveSchool(School school);
	
	@Transactional(readOnly = false)
	public void updateSchool(School school);
	public School getBySchoolId(Class<School> class1, Long idParameter);
	
	/*@Transactional(readOnly = false)
	public void delete(Long idParameter, Class<School> class1);*/
	
	public List<School> loadAllSchool(Class<School> class1);

	@Transactional(readOnly = false)
	public void copyGoldData(School school, User user, String adminPassword);

	@Transactional(readOnly = false)
	public void delete(School school) throws Exception;

	@Transactional(readOnly = false)
	public void getTheFuckOutOfHereBastard(School school);

	public School getSchool(Long idParameter);
}
