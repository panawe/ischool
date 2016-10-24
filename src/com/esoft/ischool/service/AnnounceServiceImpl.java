package com.esoft.ischool.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import com.esoft.ischool.model.Advertisement;
import com.esoft.ischool.model.Announce;
import com.esoft.ischool.model.BaseEntity;
import com.esoft.ischool.model.Project;
import com.esoft.ischool.model.Sponsor;

@Service("announceService")
public class AnnounceServiceImpl extends BaseServiceImpl implements AnnounceService {

	@Override
	public List<Announce> loadActiveAnnounces(Class<Announce> class1) {
		// TODO Auto-generated method stub
		List<BaseEntity> ances = (List<BaseEntity>) loadAllByColumn(class1, "status", 0);
		List<Announce> announces = null;
		if (ances != null) {
			announces = new ArrayList<Announce>();
			for (BaseEntity ance : ances) {
				announces.add((Announce)ance);
			}

		}
		return announces;
	}

	@Override
	public List<Announce> loadAllAnnounces(Class<Announce> class1) {
		// TODO Auto-generated method stub
		List<BaseEntity> ans = loadAll(class1);
		List<Announce> announces = null;
		if (ans != null) {
			announces = new ArrayList<Announce>();
			for (BaseEntity a : ans) {
				announces.add((Announce) a);
			}

		}
		return announces;
	}
}
