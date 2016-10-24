package com.esoft.ischool.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import com.esoft.ischool.model.BaseEntity;
import com.esoft.ischool.model.Project;

@Service("projectService")
public class ProjectServiceImpl extends BaseServiceImpl implements ProjectService {

	@Override
	public List<Project> loadAllProjects(Class<Project> class1) {
		// TODO Auto-generated method stub
		List<BaseEntity> prjts = loadAll(class1);
		List<Project> projects = null;
		if (prjts != null) {
			projects = new ArrayList<Project>();
			for (BaseEntity p : prjts) {
				Project pr = (Project) p;
				projects.add(pr);
			}

		}
		return projects;
	}

}
