package com.esoft.ischool.restservice;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import javax.servlet.ServletContext;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.esoft.ischool.model.Event;
import com.esoft.ischool.security.model.User;
import com.esoft.ischool.security.service.UserService;
import com.esoft.ischool.service.EventService;
import com.esoft.ischool.util.SimpleMail;

@RestController
@RequestMapping("/service/event")

public class EventRestService {

	@Autowired
	EventService eventService;
	@Autowired
	ServletContext context;

	@RequestMapping(value = "/receiveFile", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)

	public @ResponseBody String receiveFile(@RequestParam("file") MultipartFile file,
			@RequestParam("eventId") String eventId) {
		System.out.println("Received ");
		if (!file.isEmpty()) {
			try {
				String originalFileExtension = file.getOriginalFilename()
						.substring(file.getOriginalFilename().lastIndexOf("."));

				// transfer to upload folder
				String storageDirectory = null;
				int fileCount = 0;
				if (context != null) {

					storageDirectory = context.getRealPath("/") + File.separator + "images" + File.separator + "events"
							+ File.separator + eventId;
					File dir = new File(storageDirectory);
					if (!dir.exists()) {
						dir.mkdirs();
					}
					fileCount = dir.listFiles().length;

				} else {
					return "Failure";

				}
				String newFilename = eventId + "_" + (fileCount + 1) + ".jpg";

				File newFile = new File(storageDirectory + File.separator + newFilename);
				file.transferTo(newFile);

			} catch (Exception e) {
				e.printStackTrace();
				return "Failure";
			}
		} else {
			return "Failure";
		}

		return "Success";
	}

	@RequestMapping(value = "/createEvent", method = RequestMethod.POST, headers = "Accept=application/json")
	public @ResponseBody Event createEvent(@RequestBody Event event) {
		System.out.println("Event Created:" + event);
		try {
			String eventBeginEnd = event.getBeginEndDateTime();
			if (eventBeginEnd != null && eventBeginEnd.contains("-")) {
				event.setStartsAt(new Date(Date.parse(eventBeginEnd.split("-")[0])));
				event.setEndsAt(new Date(Date.parse(eventBeginEnd.split("-")[1])));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		eventService.save(event);
		Event ee = (Event) eventService.getById(Event.class, event.getId());
		SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy hh:mm aaa");
		ee.setBeginEndDateTime(df.format(ee.getStartsAt()) + " - " + df.format(ee.getEndsAt()));
		return ee;
	}

	@RequestMapping(value = "/deleteEvent", method = RequestMethod.POST, headers = "Accept=application/json")
	public @ResponseBody String deleteEvent(@RequestBody Event event) {
		System.out.println("delete Event:" + event);
		eventService.delete(event);
		return "Success";
	}

	@RequestMapping(value = "/getAllEvents", method = RequestMethod.POST, headers = "Accept=application/json")
	public @ResponseBody List<Event> getEvents() {
		System.out.println("Event list Requested - getEvents");

		List<Event> retList = (List<Event>) eventService.loadAllEvents(Event.class);
		Collections.reverse(retList);
		return retList;
	}

	@RequestMapping(value = "/getFutureEvents", method = RequestMethod.POST, headers = "Accept=application/json")
	public @ResponseBody List<Event> getFutureEvents() {
		System.out.println("Event list Requested - getFutureEvents");

		List<Event> retList = (List<Event>) eventService.loadFutureEvents(Event.class);
		Collections.reverse(retList);
		return retList;
	}
	
	
	@RequestMapping(value = "/getAllEventsWithAlbum", method = RequestMethod.POST, headers = "Accept=application/json")
	public @ResponseBody List<Event> getAllEventsWithAlbum() {
		System.out.println("Event list Requested - getAllEventsWithAlbum");
		List<Event> events = eventService.loadAllEvents(Event.class);
		String storageDirectory = null;

		if (context != null) {

			storageDirectory = context.getRealPath("/") + File.separator + "images" + File.separator + "events";

		}
		List<Event> retList = new ArrayList<Event>();
		for (Event e : events) {
			File dir = new File(storageDirectory + File.separator + e.getId());
			int fileCount = 0;
			if (dir.exists()) {
				fileCount = dir.listFiles().length;
			}
			if (fileCount > 0 ) {				 
				e.setHasPhoto(true);
				retList.add(e);
			}
		}
		Collections.reverse(retList);
		return retList;
	}
	
	@RequestMapping(value = "/getTop5EventsWithAlbum", method = RequestMethod.POST, headers = "Accept=application/json")
	public @ResponseBody List<Event> getTop5EventsWithAlbum() {
		System.out.println("Event list Requested - getTop5EventsWithAlbum");
		List<Event> events = eventService.loadAllEvents(Event.class);
		String storageDirectory = null;

		if (context != null) {

			storageDirectory = context.getRealPath("/") + File.separator + "images" + File.separator + "events";

		}
		List<Event> retList = new ArrayList<Event>();
		Collections.reverse(events);
		int count=0;
		for (Event e : events) {
			File dir = new File(storageDirectory + File.separator + e.getId());
			int fileCount = 0;
			if (dir.exists()) {
				fileCount = dir.listFiles().length;
			}
			if (fileCount > 0 ) {
				count++;
				e.setHasPhoto(true);
				retList.add(e);
				if(count>=4){
					break;
				}
			}
		}
		//Collections.reverse(retList);
		return retList;
	}

	
	@RequestMapping(value = "/getAllEventsWithVideo", method = RequestMethod.POST, headers = "Accept=application/json")
	public @ResponseBody List<Event> getAllEventsWithVideo() {
		System.out.println("Event list Requested - getAllEventsWithAlbum");
		List<Event> events = eventService.loadAllEvents(Event.class);
		List<Event> retList = new ArrayList<Event>();
		for (Event e : events) {
		 
				if(e.getAlbumNote()!=null&&!e.getAlbumNote().equals("")){
					e.setHasYoutube(true);
					String albumNote=e.getAlbumNote();
					e.setVideoId((albumNote==null||albumNote.equals(""))?null:albumNote.split("/")[albumNote.split("/").length-1]);
					retList.add(e);
				}			 
		}
		Collections.reverse(retList);
		return retList;
	}
	@RequestMapping(value = "/getTop5EventsWithVideo", method = RequestMethod.POST, headers = "Accept=application/json")
	public @ResponseBody List<Event> getTop5EventsWithVideo() {
		System.out.println("Event list Requested - getAllEventsWithAlbum");
		List<Event> events = eventService.loadAllEvents(Event.class);
		List<Event> retList = new ArrayList<Event>();
		Collections.reverse(events);
		int count=0;
		for (Event e : events) {
		 
				if(e.getAlbumNote()!=null&&!e.getAlbumNote().equals("")){
					count++;
					e.setHasYoutube(true);
					String albumNote=e.getAlbumNote();
					e.setVideoId((albumNote==null||albumNote.equals(""))?null:albumNote.split("/")[albumNote.split("/").length-1]);
					retList.add(e);
					if(count>=4){
						break;
					}
				}			 
		}
		
		return retList;
	}
	@RequestMapping(value = "/getAllEventsWithAlbumOrRepport", method = RequestMethod.POST, headers = "Accept=application/json")
	public @ResponseBody List<Event> getAllEventsWithAlbumOrRepport() {
		System.out.println("Event list Requested - getAllEventsWithAlbumOrRepport");
		List<Event> events = eventService.loadAllEvents(Event.class);
		String storageDirectory = null;

		if (context != null) {

			storageDirectory = context.getRealPath("/") + File.separator + "images" + File.separator + "events";

		}
		List<Event> retList = new ArrayList<Event>();
		for (Event e : events) {
			File dir = new File(storageDirectory + File.separator + e.getId());
			int fileCount = 0;
			if (dir.exists()) {
				fileCount = dir.listFiles().length;
			}
			if (e.getReport() != null) {
				e.setHasReport(true);

			}
			if (fileCount > 0) {
				e.setHasPhoto(true);
			}

			if (e.isHasPhoto() || e.isHasReport()) {
				retList.add(e);
			}

		}
		Collections.reverse(retList);
		return retList;
	}

	@RequestMapping(value = "/getAllEventsWithRepport", method = RequestMethod.POST, headers = "Accept=application/json")
	public @ResponseBody List<Event> getAllEventsWithRepport() {
		System.out.println("Event list Requested - getAllEventsWithRepport");
		List<Event> events = eventService.loadAllEvents(Event.class);
	
		List<Event> retList = new ArrayList<Event>();
		for (Event e : events) {
		
			if (e.getReport() != null) {
				e.setHasReport(true);
			}
			if (e.isHasReport()) {
				retList.add(e);
			}

		}
		Collections.reverse(retList);
		return retList;
	}
	@RequestMapping(value = "/getEventAlbum", method = RequestMethod.POST, headers = "Accept=application/json")
	public @ResponseBody List<String> getEventAlbum(@RequestBody Event event) {
		System.out.println("getEventAlbum Event:" + event);
		String storageDirectory = null;

		if (context != null) {

			storageDirectory = context.getRealPath("/") + File.separator + "images" + File.separator + "events";

		}
		List<String> retList = new ArrayList<String>();

		File dir = new File(storageDirectory + File.separator + event.getId());

		if (dir.exists()) {
			File[] files = dir.listFiles();
			for (File file : files) {
				retList.add(file.getName());
			}
		}

		Collections.reverse(retList);
		return retList;

	}

}