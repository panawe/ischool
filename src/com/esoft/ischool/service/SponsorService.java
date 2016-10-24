package com.esoft.ischool.service;

import java.util.List;

import com.esoft.ischool.model.Event;
import com.esoft.ischool.model.Project;
import com.esoft.ischool.model.Sponsor;

public interface SponsorService extends BaseService {

	List<Sponsor> loadAllSponsors(Class<Sponsor> class1);

}
