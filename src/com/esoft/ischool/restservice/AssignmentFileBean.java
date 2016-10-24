package com.esoft.ischool.restservice;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.faces.context.FacesContext;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.richfaces.event.UploadEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.esoft.ischool.bean.BaseBean;
import com.esoft.ischool.model.AssignmentFile;
import com.esoft.ischool.model.BaseEntity;
import com.esoft.ischool.model.Course;
import com.esoft.ischool.model.CourseHistory;
import com.esoft.ischool.model.Mark;
import com.esoft.ischool.service.BaseService;
import com.esoft.ischool.service.ExamService;
import com.esoft.ischool.vo.FileSearchVO;

@Component("assignmentFileBean")
@Scope("session")
public class AssignmentFileBean extends BaseBean {

	@Autowired
	@Qualifier("examService")
	private ExamService examService;
	private Long rowCount;
	private List<AssignmentFile> assignmentFiles;
	private String selectedAssignmentFileTab;
	
	private AssignmentFile assignmentFile = new AssignmentFile();
	private List<AssignmentFile> addedFiles = new ArrayList<AssignmentFile>();
	private String name;
		
	private String yearName;
	private String subjectName;
	private String className;
	private String examTypeName;
	private String fileName;
	private Date examDate;
	private Date returnDate;

	private FileSearchVO fileSearch = new FileSearchVO();
	private FileSearchVO examFile = new FileSearchVO();
	
	
	@Override
	public String clear() {
		assignmentFile = new AssignmentFile();
		return "Success";
	}
	
	public String clearCourseAssignment() {
		examFile = new FileSearchVO();
		removeAllSelectedFiles();
		return "Success";
	}


	public void search() {
		List<String> columnNames = new ArrayList<String>();
		List<String> columnValues = new ArrayList<String>();
		
		if (StringUtils.isNotBlank(name)) {
			columnNames.add("name");
			columnValues.add(name + "%");
		}

		if (columnNames.size() > 0)
			//assignmentFiles = baseService.findByColumnsLike(AssignmentFile.class, columnNames, columnValues);
		
		setRowCount(new Long(assignmentFiles.size()));
	}
	
	public String getAll() {
		//assignmentFiles = baseService.loadAll(AssignmentFile.class, getCurrentUser().getSchool());
		setRowCount(new Long(assignmentFiles.size()));
		
		return "Success";
	}
		
	public String delete() {
		clearMessages();
		try {
			examService.delete(getIdParameter(), AssignmentFile.class);
			getProjectUploadFiles();
			clear();
			setSuccessMessage(getResourceBundle().getString("DELETE_SUCCESSFULLY"));
		} catch (Exception ex) {
			setErrorMessage(getResourceBundle().getString("DELETE_UNSUCCESSFULL"));
		}

		return "Success";
	}
	
	public String searchedFiles() {
		
		if (fileSearch.containsNotEmptyField()) {
			assignmentFiles = examService.getCourseAssignmentFiles(getCurrentUser().getSchool(), fileSearch);
		}
		rowCount = new Long((assignmentFiles != null) ? assignmentFiles.size() : 0);
		return "Success";
	}
	
	public String searchedCourseFiles() {
		
		if (fileSearch.containsNotEmptyField()) {
			assignmentFiles = examService.searchCourseAssignmentFiles(getCurrentUser().getSchool(), fileSearch);
		}
		rowCount = new Long((assignmentFiles != null) ? assignmentFiles.size() : 0);
		return "Success";
	}
	
	public String insert() {
		clearMessages();
		Long id = assignmentFile.getId();
		try {
			assignmentFile.setUploadBy(getCurrentUserId());
			if (StringUtils.isNotEmpty(getErrorMessage()))
				return "ERROR";
			
			if (id == null || id == 0)
				examService.save(assignmentFile, getCurrentUser());
			else
				examService.update(assignmentFile, getCurrentUser());
			
			setSuccessMessage(getResourceBundle().getString("SAVED_SUCCESSFULLY")); 
			//getProjectUploadFiles();
		} catch (Exception ex) {
			assignmentFile.setId(id);
			setErrorMessage(ex,"Ce fichier exist deja. ");
		}
		return "Success";
	}

	public String edit() {
		clearMessages();
		assignmentFile = (AssignmentFile) examService.getById(AssignmentFile.class, getIdParameter());
		setSelectedAssignmentFileTab("clientUploadFileDetails");
		return "Success";
	}
	
	public void openFile() {

        HttpServletResponse response = (HttpServletResponse) FacesContext.getCurrentInstance().getExternalContext().getResponse();
        
        assignmentFile = (AssignmentFile) examService.getById(AssignmentFile.class, getIdParameter());

        response.addHeader("Content-disposition", "attachment;filename=" + assignmentFile.getName() + "." + assignmentFile.getFileType());
		response.setContentType(assignmentFile.getContentType());
        
        try {
            ServletOutputStream out = response.getOutputStream();

            byte[] outputByte = assignmentFile.getContent();
            //copy binary context to output stream
            out.write(outputByte);
           
            out.flush();
            out.close();
            FacesContext.getCurrentInstance().responseComplete();
        }
        catch(IOException e) {
            e.printStackTrace();
        }
	}
	
	public void openFileByName(String fileName) {

        HttpServletResponse response = (HttpServletResponse) FacesContext.getCurrentInstance().getExternalContext().getResponse();
        
        AssignmentFile fileToOpen = null;
		for (AssignmentFile addfile : addedFiles) {
			if (fileName.equals(addfile.getName())) {
				fileToOpen = addfile;
			}
		}
         
        response.addHeader("Content-disposition", "attachment;filename=" + fileToOpen.getName() + "." + fileToOpen.getFileType());
		response.setContentType(fileToOpen.getContentType());
        
        try {
            ServletOutputStream out = response.getOutputStream();

            byte[] outputByte = fileToOpen.getContent();
            //copy binary context to output stream
            out.write(outputByte);
           
            out.flush();
            out.close();
            FacesContext.getCurrentInstance().responseComplete();
        }
        catch(IOException e) {
            e.printStackTrace();
        }
	}
	
	public String getProjectUploadFiles() {
		List<String> columnNames = new ArrayList<String>();
		List<String> columnValues = new ArrayList<String>();
		if(getSessionParameter("currentProjectId")!=null){
			columnNames.add("relatedName1");
			columnNames.add("relatedId1");
			columnValues.add("project");
			columnValues.add(getSessionParameter("currentProjectId").toString());
			//assignmentFiles = baseService.findByColumns(AssignmentFile.class, columnNames, columnValues);
			setRowCount(new Long(assignmentFiles.size()));
		}
		return "Success";
	}
	
	public String findCourseAssignedFiles() {
		if (getSessionParameter("currentTeacherId") != null) {
			if (StringUtils.isNotBlank(yearName) && StringUtils.isNotBlank(subjectName) && StringUtils.isNotBlank(className)) {
				FileSearchVO fileSearchVO = new FileSearchVO(yearName, subjectName, className, "");
				assignmentFiles = examService.getCourseAssignmentFiles(getCurrentUser().getSchool(), fileSearchVO);
			}
		}
		return "Success";
	}
	
	public String getTeacherAssignedFiles() {
		
		return "Success";
	}
	
	public void pieceslistener(UploadEvent event) throws Exception {
		AssignmentFile addedFile = new AssignmentFile();
		addedFile.setName(event.getUploadItem().getFileName());
		assignmentFile.setName(event.getUploadItem().getFileName());
		addedFile.setFileType(event.getUploadItem().getFileName().split("\\.")[1]);
		addedFile.setContent(event.getUploadItem().getData());
		addedFile.setUploadBy(getCurrentUserId());
		addedFiles.add(addedFile);
	}
	
	public void saveDescription () {
		for (AssignmentFile addfile : addedFiles) {
			if (assignmentFile.getName() != null && assignmentFile.getName().equals(addfile.getName())) {
				addfile.setDescription(assignmentFile.getDescription());
			}
		}
		
		assignmentFile = new AssignmentFile();
	}
	
	public String removeSelectedFile(String name) {
		AssignmentFile fileToRemove = null;
		for (AssignmentFile addfile : addedFiles) {
			if (name.equals(addfile.getName())) {
				fileToRemove = addfile;
			}
		}
		
		addedFiles.remove(fileToRemove);
		return "Success";
	}
	
	public String pickFile(String userType, Long fileId) {
		for (AssignmentFile assFile : assignmentFiles) {
			if (assFile.getId().toString().equals(fileId.toString())) {
				addedFiles.add(assFile);
			}
		}
		return "Success";
	}
	
	public String addCourseAssignmentFiles() {
		setErrorMessage("");
		setSuccessMessage("");
		
		if (StringUtils.isNotBlank(getExamFile().getYearName()) && StringUtils.isNotBlank(getExamFile().getSubjectName()) && StringUtils.isNotBlank(getExamFile().getClassName())) {
			Course course = examService.getCourse(getExamFile().getClassName(), getExamFile().getSubjectName());
			if (course == null) {
				setErrorMessage("Cette matiere n'est pas enseignee dans cette classe.");
			}
			else {
				examService.addCourseAssignmentFiles(examFile, addedFiles, getCurrentUser());
				setSuccessMessage("Les documents one ete assigne a la classe. ");
			}
		}
		else {
			setErrorMessage("Annee/Classe/Matiere sont obligatoire. ");
		}
		
		return "Success";
	}
	
	public String removeAllSelectedFiles() {
		addedFiles.clear();
		return "Success";
	}
	
	public boolean isUserHasWriteAccess() {
		/*if ( ((String)getSessionParameter("link")).equals("student")) {
			return isUserHasWriteAccess(MenuIdEnum.STUDENT.getValue());
		} else if ( ((String)getSessionParameter("link")).equals("teacher")) {
			return isUserHasWriteAccess(MenuIdEnum.TEACHER.getValue());
		}
		return false;*/
		
		return true;
	}

	public ExamService getExamService() {
		return examService;
	}

	public void setExamService(ExamService examService) {
		this.examService = examService;
	}

	public Long getRowCount() {
		rowCount = assignmentFiles == null ? new Long(0) : assignmentFiles.size();
		return rowCount;
	}

	public Long getSelectedFileRowCount() {
		rowCount = addedFiles == null ? new Long(0) : addedFiles.size();
		return rowCount;
	}
	
	public void setRowCount(Long rowCount) {
		this.rowCount = rowCount;
	}

	public List<AssignmentFile> getAssignmentFiles() {
		return assignmentFiles;
	}

	public void setAssignmentFiles(List<AssignmentFile> assignmentFiles) {
		this.assignmentFiles = assignmentFiles;
	}

	public String getSelectedAssignmentFileTab() {
		return selectedAssignmentFileTab;
	}

	public void setSelectedAssignmentFileTab(String selectedAssignmentFileTab) {
		this.selectedAssignmentFileTab = selectedAssignmentFileTab;
	}

	public AssignmentFile getAssignmentFile() {
		return assignmentFile;
	}

	public void setAssignmentFile(AssignmentFile assignmentFile) {
		this.assignmentFile = assignmentFile;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getYearName() {
		return yearName;
	}

	public void setYearName(String yearName) {
		this.yearName = yearName;
	}

	public String getSubjectName() {
		return subjectName;
	}

	public void setSubjectName(String subjectName) {
		this.subjectName = subjectName;
	}

	public String getClassName() {
		return className;
	}

	public void setClassName(String className) {
		this.className = className;
	}

	public List<AssignmentFile> getAddedFiles() {
		return addedFiles;
	}

	public void setAddedFiles(List<AssignmentFile> addedFiles) {
		this.addedFiles = addedFiles;
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public FileSearchVO getFileSearch() {
		return fileSearch;
	}

	public void setFileSearch(FileSearchVO fileSearch) {
		this.fileSearch = fileSearch;
	}

	public Date getExamDate() {
		return examDate;
	}

	public void setExamDate(Date examDate) {
		this.examDate = examDate;
	}

	public Date getReturnDate() {
		return returnDate;
	}

	public void setReturnDate(Date returnDate) {
		this.returnDate = returnDate;
	}

	public String getExamTypeName() {
		return examTypeName;
	}

	public void setExamTypeName(String examTypeName) {
		this.examTypeName = examTypeName;
	}

	public FileSearchVO getExamFile() {
		return examFile;
	}

	public void setExamFile(FileSearchVO examFile) {
		this.examFile = examFile;
	}
	
	
	
}
