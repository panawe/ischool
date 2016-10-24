package com.esoft.ischool.service;

import java.util.List;

import com.esoft.ischool.model.Advertisement;
import com.esoft.ischool.model.Announce;
import com.esoft.ischool.model.Event;
import com.esoft.ischool.model.Project;
import com.esoft.ischool.model.Sponsor;

public interface AnnounceService extends BaseService {

	List<Announce> loadAllAnnounces(Class<Announce> class1);
	
	List<Announce> loadActiveAnnounces(Class<Announce> class1);

}
