package com.esoft.ischool.service;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.stereotype.Service;

import com.esoft.ischool.model.BaseEntity;
import com.esoft.ischool.model.Event;

@Service("eventService")
public class EventServiceImpl extends BaseServiceImpl implements EventService {

	@Override
	public List<Event> loadAllEvents(Class<Event> class1) {
		// TODO Auto-generated method stub
		List<BaseEntity> ees = loadAll(class1);
		List<Event> events = null;
		SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy hh:mm aaa");
		if (ees != null) {
			events = new ArrayList<Event>();
			for (BaseEntity be : ees) {
				Event ee = (Event) be;
				ee.setBeginEndDateTime(df.format(ee.getStartsAt()) + " - " + df.format(ee.getEndsAt()));
				events.add(ee);
			}

		}
		return events;
	}

	
	@Override
	public List<Event> loadFutureEvents(Class<Event> class1) {
		// TODO Auto-generated method stub
		List<BaseEntity> ees = loadAll(class1);
		List<Event> events = null;
		SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy hh:mm aaa");
		if (ees != null) {
			events = new ArrayList<Event>();
			for (BaseEntity be : ees) {
				Event ee = (Event) be;
				if(ee.getStartsAt().after(new Date())){
					ee.setBeginEndDateTime(df.format(ee.getStartsAt()) + " - " + df.format(ee.getEndsAt()));
					events.add(ee);
				}
			}

		}
		return events;
	}
}
