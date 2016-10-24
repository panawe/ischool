package com.esoft.ischool.restservice;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.math.BigDecimal;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperRunManager;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.faces.context.FacesContext;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.esoft.ischool.model.BaseEntity;
import com.esoft.ischool.model.Parameter;
import com.esoft.ischool.security.model.User;
import com.esoft.ischool.service.BaseService;

import javax.faces.event.ValueChangeEvent;
import javax.faces.model.SelectItem;

import org.apache.commons.lang.StringUtils;


@Component("reportBean")
@Scope("session")
public class ReportBean extends BaseBean {

	@Autowired
	@Qualifier("baseService")
	private BaseService baseService;
	
	private String dummie;
		
	private List<BaseEntity> parameters;
	
	private String selectedValue;
	private Map<String, Serializable> parameterValueMap;
	private Map<String, String> parameterClassMap;
	
	private String reportName;
	private String reportFileName;
	
	@PostConstruct
	private void getAll() {
	}

	public String parameterSelected(ValueChangeEvent value) {
		
		if (value != null) {
			if (value.getNewValue() instanceof String) {
				this.selectedValue = (String) value.getNewValue();
				String parameterName = (String) myValue.getAttributes().get("parameterName");
				if (StringUtils.isNotBlank(this.selectedValue)) {
					if ("java.lang.Long".equalsIgnoreCase(parameterClassMap.get(parameterName)))
						parameterValueMap.put(parameterName, new Long(selectedValue));
					else if ("java.lang.String".equalsIgnoreCase(parameterClassMap.get(parameterName)))
						parameterValueMap.put(parameterName, selectedValue);
					else if ("java.lang.Integer".equalsIgnoreCase(parameterClassMap.get(parameterName)))
						parameterValueMap.put(parameterName, new Integer(selectedValue));
					else if ("java.lang.Float".equalsIgnoreCase(parameterClassMap.get(parameterName)))
						parameterValueMap.put(parameterName, new Float(selectedValue));
					else if ("java.lang.BigDecimal".equalsIgnoreCase(parameterClassMap.get(parameterName)))
						parameterValueMap.put(parameterName, new BigDecimal(selectedValue));
					else if ("java.lang.Double".equalsIgnoreCase(parameterClassMap.get(parameterName)))
						parameterValueMap.put(parameterName, new Double(selectedValue));
					else if ("java.lang.Boolean".equalsIgnoreCase(parameterClassMap.get(parameterName)))
						parameterValueMap.put(parameterName, new Boolean(selectedValue));
				}
				else 
					parameterValueMap.put(parameterName, selectedValue);
			} 
			else if (value.getNewValue() instanceof List) {
				String parameterName = (String) myValue.getAttributes().get("parameterName");
				parameterValueMap.put(parameterName, (Serializable)(List)value.getNewValue());
			}
			else if (value.getNewValue() instanceof Date) {
				//this.selectedValue = (String) value.getNewValue();
				String parameterName = (String) myValue.getAttributes().get("parameterName");
				parameterValueMap.put(parameterName, (Serializable) value.getNewValue());
			} 
		} 
		
		return "";
	}
	
	public void runReport(String reportName, String reportPath, User user) {
		parameterValueMap = new HashMap<String, Serializable>();
		parameterClassMap = new HashMap<String, String>();
		if(parameters!=null&&parameters.size()>0)
			parameters.clear();
		List<String> parameterNames = getParameterNames(reportName, reportPath, user);

		Long schoolId;
		
		if (parameterNames != null && !parameterNames.isEmpty()) {
			schoolId = user == null ? 1L : user.getSchool().getId();
			parameters = baseService.findByColumnValues(Parameter.class, "name", parameterNames, schoolId);
			List<Parameter> tempParameters = new ArrayList<Parameter>();
			
			//Check if there is undefined parameter and create a dummy one in case.
			for (String parameterName : parameterNames) {
				Boolean found = false;
				for (BaseEntity p : parameters) {
					Parameter parameter = (Parameter)p;
					if (parameterName.equalsIgnoreCase(parameter.getName())) {
						found = true;
						break;
					}
				}
				
				if (!found) {
					Parameter parameter = new Parameter();
					parameter.setName(parameterName);
					parameter.setDataType("String");
					parameter.setDisplayName(parameterName.substring(0,1).toUpperCase() + parameterName.substring(1));
					parameter.setInputType("Input");
					parameter.setSize("10");
					parameter.setMaxLength("10");
					parameters.add(parameter);
					tempParameters.add(parameter);
				}
					
			}
		
			for (BaseEntity parameter : parameters ) {
				Parameter param = (Parameter)parameter;
				
				if (StringUtils.isNotBlank(param.getParameterSql())) {
					List<Object[]> objects = baseService.getReportParameterValues(param.getParameterSql().replaceAll("schoolId", schoolId.toString()));
					
					List<SelectItem> items = new ArrayList<SelectItem>();
					
					items.add(new SelectItem("", ""));
					
					for (Object[] obj : objects) {
						SelectItem item = new SelectItem();
						item.setValue( obj[0].toString());
						item.setLabel((String) obj[1]);
						items.add(item);
					}
					
					param.setParamValues(items);
					
				}
				tempParameters.add(param);
					
			}
			parameters.clear();
			for (Parameter tempParameter : tempParameters) {
				parameters.add(tempParameter);
			}
		}
	}
		
	private List<String> getParameterNames(String reportFileName, String reportPath, User user) {
	
		List<String> parameterNames = new ArrayList<String>();
				
		try {
			File file = new File(reportPath + reportFileName + ".jrxml");
			DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
			DocumentBuilder db = dbf.newDocumentBuilder();
			Document doc = db.parse(file);
			doc.getDocumentElement().normalize();
			NodeList nodeLst = doc.getElementsByTagName("parameter");

			NodeList headerLst = doc.getElementsByTagName("jasperReport");
			for (int s = 0; s < headerLst.getLength(); s++) {
				Node fstNode = headerLst.item(s);	    
				if (fstNode.getNodeType() == Node.ELEMENT_NODE) {	  
					Element parameterElmnt = (Element) fstNode;
					this.reportName = parameterElmnt.getAttribute("name");						
				}
			}
			
			this.reportFileName = reportFileName;
			
			for (int s = 0; s < nodeLst.getLength(); s++) {

				Node fstNode = nodeLst.item(s);
		    
				if (fstNode.getNodeType() == Node.ELEMENT_NODE) {
		  
					Element parameterElmnt = (Element) fstNode;
					String parameterName = parameterElmnt.getAttribute("name");
					String parameterClassName = parameterElmnt.getAttribute("class");
										
					if (parameterName != null) {
						if (parameterName.equalsIgnoreCase("SUBREPORT_DIR"))
							parameterValueMap.put(parameterName, parameterName);
						else if (parameterName.equalsIgnoreCase("schoolId"))
							parameterValueMap.put(parameterName, user.getSchool().getId());
						else
							parameterNames.add(parameterName);
						parameterClassMap.put(parameterName, parameterClassName);
					}
				}

			}
		} catch (Exception e) {
		    e.printStackTrace();
		}
		  
		return parameterNames;
	}
	
	
	public String printReport() {
		try {
	
			FacesContext context = getContext();
			
			HttpServletResponse response = (HttpServletResponse) context
					.getExternalContext().getResponse();
	
			InputStream reportStream = context.getExternalContext()
					.getResourceAsStream("/reports/" + reportFileName + ".jasper");
	
			ServletOutputStream ouputStream = response.getOutputStream();
	
			response.addHeader("Content-disposition",
					"attachment;filename=" + reportFileName + "-"+getStringDate()+ ".pdf");
	
			if (parameterValueMap.get("SUBREPORT_DIR") != null)
				parameterValueMap.put("SUBREPORT_DIR", getReportsDirPath() + java.io.File.separator);
			
			JasperRunManager.runReportToPdfStream(reportStream,
					ouputStream, parameterValueMap, getConnection());
			response.setContentType("application/pdf");
			ouputStream.flush();
			ouputStream.close();
			context.responseComplete();
			
		} catch (JRException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return "success";
	}
	
	
	
	
	
	public BaseService getBaseService() {
		return baseService;
	}

	public void setBaseService(BaseService baseService) {
		this.baseService = baseService;
	}

	public String getDummie() {
		return dummie;
	}

	public void setDummie(String dummie) {
		this.dummie = dummie;
	}

	public List<BaseEntity> getParameters() {
		return parameters;
	}

	public void setParameters(List<BaseEntity> parameters) {
		this.parameters = parameters;
	}

	public String getSelectedValue() {
		return selectedValue;
	}

	public void setSelectedValue(String selectedValue) {
		this.selectedValue = selectedValue;
	}

	public String getReportName() {
		return reportName;
	}

	public void setReportName(String reportName) {
		this.reportName = reportName;
	}	
}
