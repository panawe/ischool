package com.esoft.ischool.restservice;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.esoft.ischool.model.BaseEntity;
import com.esoft.ischool.model.Level;
import com.esoft.ischool.service.BaseService;

@Component("levelBean")
@Scope("session")
public class LevelBean extends BaseBean {

	@Autowired
	@Qualifier("baseService")
	private BaseService baseService;
	private Long rowCount;
	private List<BaseEntity> levels;
	private List<String> allLevels = new ArrayList();
	
	private Level level = new Level();

	public String validate() {
		return "succes";
	}
	
	@Override
	public String clear() {
		level = new Level();
		return "Success";
	}
	
	public List<Level> autoComplete(Object suggest) {
		String pref = (String) suggest;
		List<Level> result = new ArrayList<Level>();
		allLevels.clear();
		
		for (BaseEntity entity : levels) {
			Level l = (Level) entity;
			if ((l.getName() != null && l.getName().toLowerCase().indexOf(
					pref.toLowerCase()) == 0)
					|| "".equals(pref))
				result.add(l);
			
			allLevels.add(l.getName());
		}

		return result;
	}

	public String delete() {
		clearMessages();
		try {
			baseService.delete(getIdParameter(), Level.class);
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
		Long id = level.getId();
		try {
			if (id == null || id == 0)
				baseService.save(level,getCurrentUser());
			else
				baseService.update(level,getCurrentUser());
			setSuccessMessage(getResourceBundle().getString("SAVED_SUCCESSFULLY"));
		} catch (Exception ex) {
			level.setId(id);
			setErrorMessage(ex,"Ce level existe deja");
		}

		
		clear();
		getAll();
		return "Success";
	}


	public String edit() {
		clearMessages();
		level = (Level) baseService.getById(Level.class, getIdParameter());
		return "Success";
	}
	
	@PostConstruct
	private void getAll() {
		levels = baseService.loadAll(Level.class,baseService.getDefaultSchool());
		setRowCount(new Long(levels.size()));

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

	public List<BaseEntity> getLevels() {
		return levels;
	}

	public void setLevels(List<BaseEntity> levels) {
		this.levels = levels;
	}

	public Level getLevel() {
		return level;
	}

	public void setLevel(Level level) {
		this.level = level;
	}

	public List<String> getAllLevels() {
		autoComplete("");
		return allLevels;
	}

	public void setAllLevels(List<String> allLevels) {
		this.allLevels = allLevels;
	}
	
	
}
