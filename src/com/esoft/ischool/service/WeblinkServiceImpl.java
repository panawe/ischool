package com.esoft.ischool.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import com.esoft.ischool.model.Advertisement;
import com.esoft.ischool.model.Announce;
import com.esoft.ischool.model.BaseEntity;
import com.esoft.ischool.model.Project;
import com.esoft.ischool.model.Sponsor;
import com.esoft.ischool.model.Weblink;

@Service("weblinkService")
public class WeblinkServiceImpl extends BaseServiceImpl implements WeblinkService {

	@Override
	public List<Weblink> loadActiveWeblinks(Class<Weblink> class1) {
		// TODO Auto-generated method stub
		List<BaseEntity> wls = (List<BaseEntity>) loadAllByColumn(class1, "status", 0);
		List<Weblink> weblinks = null;
		if (wls != null) {
			weblinks = new ArrayList<Weblink>();
			for (BaseEntity wl : wls) {
				weblinks.add((Weblink)wl);
			}

		}
		return weblinks;
	}

	@Override
	public List<Weblink> loadAllWeblinks(Class<Weblink> class1) {
		// TODO Auto-generated method stub
		List<BaseEntity> wls = loadAll(class1);
		List<Weblink> weblinks = null;
		if (wls != null) {
			weblinks = new ArrayList<Weblink>();
			for (BaseEntity wl : wls) {
				weblinks.add((Weblink) wl);
			}

		}
		return weblinks;
	}
}
