package com.esoft.ischool.service;

import java.util.List;

import com.esoft.ischool.model.Event;

public interface EventService extends BaseService {

	List<Event> loadAllEvents(Class<Event> class1);
	List<Event> loadFutureEvents(Class<Event> class1);

}
