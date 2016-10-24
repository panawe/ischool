package com.esoft.ischool.service;

import java.util.List;

import com.esoft.ischool.model.Event;
import com.esoft.ischool.model.Project;

public interface ProjectService extends BaseService {

	List<Project> loadAllProjects(Class<Project> class1);

}
