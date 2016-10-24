package com.esoft.ischool.service;

import java.util.List;

import com.esoft.ischool.model.Advertisement;
import com.esoft.ischool.model.Event;
import com.esoft.ischool.model.Project;

public interface AdvertisementService extends BaseService {

	List<Advertisement> loadActiveAdvertisements(Class<Advertisement> class1);
	
	List<Advertisement> loadAllAdvertisementsBySponsor(Class<Advertisement> class1, Long sponsorId);

}
