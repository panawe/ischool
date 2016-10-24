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
import com.esoft.ischool.security.model.User;
import com.esoft.ischool.security.service.UserService;
import com.esoft.ischool.service.EventService;
import com.esoft.ischool.service.ProjectService;
import com.esoft.ischool.util.SimpleMail;

@RestController
@RequestMapping("/service/project")

public class ProjectRestService {

	@Autowired
	ProjectService projectService;
	@Autowired
	ServletContext context;

	@RequestMapping(value = "/receiveFile", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)

	public @ResponseBody String receiveFile(@RequestParam("file") MultipartFile file,
			@RequestParam("projectId") String projectId) {

		if (!file.isEmpty()) {
			try {
				String originalFileExtension = file.getOriginalFilename()
						.substring(file.getOriginalFilename().lastIndexOf("."));

				// transfer to upload folder
				String storageDirectory = null;
				int fileCount = 0;
				if (context != null) {

					storageDirectory = context.getRealPath("/") + File.separator + "images" + File.separator
							+ "projects" + File.separator + projectId;
					File dir = new File(storageDirectory);
					if (!dir.exists()) {
						dir.mkdirs();
					}
					fileCount = dir.listFiles().length;

				} else {
					return "Failure";

				}
				String newFilename = projectId + "_" + (fileCount + 1) + ".jpg";

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

	@RequestMapping(value = "/createProject", method = RequestMethod.POST, headers = "Accept=application/json")
	public @ResponseBody Project createProject(@RequestBody Project project) {

		projectService.save(project);

		Project pr = (Project) projectService.getById(Project.class, project.getId());
		SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy hh:mm aaa");
		return pr;
	}

	@RequestMapping(value = "/deleteProject", method = RequestMethod.POST, headers = "Accept=application/json")
	public @ResponseBody String deleteProject(@RequestBody Project project) {
		System.out.println("Delete Project:" + project);
		projectService.delete(project);
		return "Success";
	}

	@RequestMapping(value = "/getAllProjects", method = RequestMethod.POST, headers = "Accept=application/json")
	public @ResponseBody List<Project> getProjects() {
		System.out.println("Project list Requested - getProjects");
		List<Project> projects = projectService.loadAllProjects(Project.class);
		String storageDirectory = null;
		if (context != null) {

			storageDirectory = context.getRealPath("/") + File.separator + "images" + File.separator + "projects";

		}
		for (Project p : projects) {
			File dir = new File(storageDirectory + File.separator + p.getId());
			int fileCount = 0;
			if (dir.exists()) {
				fileCount = dir.listFiles().length;
			}

			if (fileCount > 0) {
				p.setHasPhoto(true);

			}
		}
		return projects;
	}

	@RequestMapping(value = "/getAllProjectsWithAlbum", method = RequestMethod.POST, headers = "Accept=application/json")
	public @ResponseBody List<Project> getAllProjectsWithAlbum() {
		System.out.println("Project list Requested - getAllProjectsWithAlbum");
		List<Project> projects = projectService.loadAllProjects(Project.class);
		String storageDirectory = null;

		if (context != null) {

			storageDirectory = context.getRealPath("/") + File.separator + "images" + File.separator + "projects";

		}
		List<Project> retList = new ArrayList<Project>();
		for (Project p : projects) {
			File dir = new File(storageDirectory + File.separator + p.getId());
			int fileCount = 0;
			if (dir.exists()) {
				fileCount = dir.listFiles().length;
			}

			if (fileCount > 0) {
				p.setHasPhoto(true);
				retList.add(p);
			}
		}

		Collections.reverse(retList);
		return retList;
	}

	@RequestMapping(value = "/getProjectAlbum", method = RequestMethod.POST, headers = "Accept=application/json")
	public @ResponseBody List<String> getProjectAlbum(@RequestBody Project project) {
		System.out.println("getProjectAlbum Project:" + project);
		String storageDirectory = null;

		if (context != null) {

			storageDirectory = context.getRealPath("/") + File.separator + "images" + File.separator + "projects";

		}
		List<String> retList = new ArrayList<String>();

		File dir = new File(storageDirectory + File.separator + project.getId());

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