package com.esoft.ischool.servlet;

import java.io.IOException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;

import com.esoft.ischool.restservice.CourseBean;
import com.esoft.ischool.restservice.ReportBean;
import com.esoft.ischool.security.model.User;
import com.esoft.ischool.util.Constants;

public class Dispatcher extends HttpServlet implements Constants {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Override
	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		RequestDispatcher dispatcher = null;
		String lien = request.getParameter("link");
		User user = (User) request.getSession().getAttribute("user");
		String url = "";
		
		 CourseBean bean;
		 ReportBean reportBean;

		if (user == null) {
			url = "/index.faces";
		} else {
			// List<Menu> menus = null;
			request.getSession().setAttribute("link", lien);
			if (lien.equals("onlineTest")) {
				url = "/onlineTest.faces";
			} else if (lien.equals("home")) {
				url = "/home.faces";
			} else if (lien.equals("student")) {
				request.getSession().setAttribute("modifyEvent", "false");
				url = "/student.faces";
			} else if (lien.equals("parent")) {
				request.getSession().setAttribute("modifyEvent", "false");
				url = "/parent.faces";
			} else if (lien.equals("exam")) {
				url = "/exam.faces";
			} else if (lien.equals("teacher")) {
				request.getSession().setAttribute("modifyEvent", "false");
				url = "/teacher.faces";
			} else if (lien.equals("course")) {
				url = "/course.faces";
			} else if (lien.equals("correspondance")) {
				url = "/correspondance.faces";
			} else if (lien.equals("school")) {
				request.getSession().setAttribute("modifyEvent", "true");
				url = "/school.faces";
			} else if (lien.equals("term")) {
				url = "/term.faces";
			} else if (lien.equals("tuition")) {
				url = "/tuition.faces";
			} else if (lien.equals("autre")) {
				url = "/autre.faces";
			} else if (lien.equals("security")) {
				url = "/security.faces";
			} else if (lien.equals("inscriptions")) {
				url = "/inscriptions.faces";
			} else if (lien.equals("imprimerBulletin")) {
				url = "/imprimerBulletin.faces";
			}else if (lien.equals("classementEnLigne")) {
				url = "/resultSummary.faces";
			}else if (lien.equals("question")) {
				url = "/question.faces";
			}else if (lien.equals("commandes")) {
				url = "/commandes.faces";
			}else if (lien.equals("produits")) {
				url = "/produits.faces";
			}else if (lien.equals("demandes")) {
				url = "/demandes.faces";
			}else if (lien.equals("configStock")) {
				url = "/configStock.faces";
			}else if (lien.equals("alerts")) {
				url = "/alerts.faces";
			}else if (lien.equals("paymentDue")) {
				url = "/paymentDue.faces";
			}else if (lien.equals("impressionBulletin")) {
				url = "/impressionBulletin.faces";
			}else if (lien.equals("createSchool")) {
				url = "/schools.faces";
			}else if (lien.equals("consultation")) {
				url = "/consultation.faces";
			}else if (lien.equals("configMed")) {
				url = "/configMed.faces";
			}else if (lien.equals("documentation")) {
				url = "/doc.faces";
			}else if (lien.equals("apropos")) {
				url = "/apropos.faces";
			}else if (lien.equals("resultatsAnnuel")) {
				url = "/resultatAnnuel.faces";
			}else if (StringUtils.isNotBlank(lien)) {
				if (lien.equals("adminCourse")) { 
					bean = (CourseBean) request.getSession().getAttribute("courseBean"); 
					if (bean != null) bean.getAll();
				}
				else if (lien.equals("adminSchool")) { 
					request.getSession().setAttribute("modifyEvent", "true"); 
				}
				else if (lien.equals("report")) { 
					String reportName = request.getParameter("reportName");
					String reportPath = request.getRealPath("/reports/") + "/";
					reportBean = (ReportBean) request.getSession().getAttribute("reportBean"); 
					if (reportBean != null) reportBean.runReport(reportName, reportPath, user);
				}
				url = urls.get(lien);
			} 
		
			else {// unkown
				url = "/index.jsp";
			}

		}
		dispatcher = request.getRequestDispatcher(url);
		dispatcher.forward(request, response);
	}

}
