package com.esoft.ischool.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import com.esoft.ischool.model.BaseEntity;
import com.esoft.ischool.model.Project;
import com.esoft.ischool.model.Sponsor;

@Service("sponsorService")
public class SponsorServiceImpl extends BaseServiceImpl implements SponsorService {

	@Override
	public List<Sponsor> loadAllSponsors(Class<Sponsor> class1) {
		// TODO Auto-generated method stub
		List<BaseEntity> sps = loadAll(class1);
		List<Sponsor> sponsors = null;
		if (sps != null) {
			sponsors = new ArrayList<Sponsor>();
			for (BaseEntity s : sps) {
				sponsors.add((Sponsor) s);
			}

		}
		return sponsors;
	}

}
