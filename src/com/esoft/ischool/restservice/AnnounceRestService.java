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

import com.esoft.ischool.model.Advertisement;
import com.esoft.ischool.model.Announce;
import com.esoft.ischool.model.Event;
import com.esoft.ischool.model.Project;
import com.esoft.ischool.model.Sponsor;
import com.esoft.ischool.model.Weblink;
import com.esoft.ischool.security.model.User;
import com.esoft.ischool.security.service.UserService;
import com.esoft.ischool.service.AdvertisementService;
import com.esoft.ischool.service.AnnounceService;
import com.esoft.ischool.service.EventService;
import com.esoft.ischool.util.SimpleMail;

@RestController
@RequestMapping("/service/announce")

public class AnnounceRestService {

	@Autowired
	AnnounceService announceService;
	@Autowired
	ServletContext context;

	@RequestMapping(value = "/receiveFile", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)

	public @ResponseBody String receiveFile(@RequestParam("file") MultipartFile file,
			@RequestParam("aannounceId") String announceId) {
		System.out.println("Received ");
		if (!file.isEmpty()) {
			try {
				String originalFileExtension = file.getOriginalFilename()
						.substring(file.getOriginalFilename().lastIndexOf("."));

				// transfer to upload folder
				String storageDirectory = null;
				int fileCount = 0;
				if (context != null) {

					storageDirectory = context.getRealPath("/") + File.separator + "images" + File.separator + "advertisements"
							+ File.separator + announceId;
					File dir = new File(storageDirectory);
					if (!dir.exists()) {
						dir.mkdirs();
					}
					fileCount = dir.listFiles().length;

				} else {
					return "Failure";

				}
				String newFilename = announceId + "_" + (fileCount + 1) + ".jpg";

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

	@RequestMapping(value = "/createAnnounce", method = RequestMethod.POST, headers = "Accept=application/json")
	public @ResponseBody Announce createAnnounce(@RequestBody Announce announce) {
		System.out.println("Announce Created:" + announce);
		announceService.save(announce);
		return announce;
	}

	@RequestMapping(value = "/deleteAnnounce", method = RequestMethod.POST, headers = "Accept=application/json")
	public @ResponseBody String deleteAnnounce(@RequestBody Announce announce) {
		System.out.println("delete Announce:" + announce);
		announceService.delete(announce);
		return "Success";
	}

	@RequestMapping(value = "/getActiveAnnounces", method = RequestMethod.POST, headers = "Accept=application/json")
	public @ResponseBody List<Announce> getActiveAnnounces() {
		System.out.println("Announce list requested");

		List<Announce> retList = (List<Announce>) announceService.loadActiveAnnounces(Announce.class);
			
		Collections.reverse(retList);
		return retList;
	}
	
	@RequestMapping(value = "/getAllAnnounces", method = RequestMethod.POST, headers = "Accept=application/json")
	public @ResponseBody List<Announce> getAnnounces() {
		System.out.println("Announce list Requested - getAnnounces");
		List<Announce> announces = announceService.loadAllAnnounces(Announce.class);
		
		return announces;
	}

}