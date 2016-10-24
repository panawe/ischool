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
import com.esoft.ischool.model.Project;
import com.esoft.ischool.model.Sponsor;
import com.esoft.ischool.security.model.User;
import com.esoft.ischool.security.service.UserService;
import com.esoft.ischool.service.EventService;
import com.esoft.ischool.service.ProjectService;
import com.esoft.ischool.service.SponsorService;
import com.esoft.ischool.util.SimpleMail;

@RestController
@RequestMapping("/service/sponsor")

public class SponsorRestService {

	@Autowired
	SponsorService sponsorService;
	@Autowired
	ServletContext context;

	@RequestMapping(value = "/receiveFile", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)

	public @ResponseBody String receiveFile(@RequestParam("file") MultipartFile file,
			@RequestParam("sponsorId") String sponsorId) {

		if (!file.isEmpty()) {
			try {
				String originalFileExtension = file.getOriginalFilename()
						.substring(file.getOriginalFilename().lastIndexOf("."));

				// transfer to upload folder
				String storageDirectory = null;
				int fileCount = 0;
				if (context != null) {

					storageDirectory = context.getRealPath("/") + File.separator + "images" + File.separator
							+ "sponsors" + File.separator + sponsorId;
					File dir = new File(storageDirectory);
					if (!dir.exists()) {
						dir.mkdirs();
					}
					fileCount = dir.listFiles().length;

				} else {
					return "Failure";

				}
				String newFilename = sponsorId + "_" + (fileCount + 1) + ".jpg";

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

	@RequestMapping(value = "/createSponsor", method = RequestMethod.POST, headers = "Accept=application/json")
	public @ResponseBody Sponsor createSponsor(@RequestBody Sponsor sponsor) {

		sponsorService.save(sponsor);

		Sponsor sp = (Sponsor) sponsorService.getById(Sponsor.class, sponsor.getId());
		SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy hh:mm aaa");
		return sp;
	}

	@RequestMapping(value = "/deleteSponsor", method = RequestMethod.POST, headers = "Accept=application/json")
	public @ResponseBody String deleteSponsor(@RequestBody Sponsor sponsor) {
		System.out.println("Delete Sponsor:" + sponsor);
		sponsorService.delete(sponsor);
		return "Success";
	}

	@RequestMapping(value = "/getAllSponsors", method = RequestMethod.POST, headers = "Accept=application/json")
	public @ResponseBody List<Sponsor> getSponsors() {
		System.out.println("Sponsor list Requested - getSponsors");
		List<Sponsor> sponsors = sponsorService.loadAllSponsors(Sponsor.class);
		
		return sponsors;
	}

}