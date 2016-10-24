package com.esoft.ischool.service;

import java.util.List;

import com.esoft.ischool.model.Advertisement;
import com.esoft.ischool.model.Announce;
import com.esoft.ischool.model.Event;
import com.esoft.ischool.model.Project;
import com.esoft.ischool.model.Sponsor;
import com.esoft.ischool.model.Weblink;

public interface WeblinkService extends BaseService {

	List<Weblink> loadAllWeblinks(Class<Weblink> class1);
	
	List<Weblink> loadActiveWeblinks(Class<Weblink> class1);

}
