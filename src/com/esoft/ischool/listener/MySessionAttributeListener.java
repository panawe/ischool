package  com.esoft.ischool.listener;

import javax.servlet.http.HttpSessionAttributeListener;
import javax.servlet.http.HttpSessionBindingEvent;

public class MySessionAttributeListener implements HttpSessionAttributeListener {

	public void attributeAdded(HttpSessionBindingEvent event) {
		// System.out.println("Added to session:
		// "+event.getName()+"==>"+event.getValue());
		/*
		 * if(event.getName().equals("testId")){
		 * 
		 * UserBean ub = (UserBean)event.getSession().getAttribute("userBean");
		 * Long testId = new Long(((String)
		 * event.getSession().getAttribute("testId")).trim());
		 * ub.setQuestions(testId, event); }
		 */
	}

	public void attributeRemoved(HttpSessionBindingEvent event) {
		// TODO Auto-generated method stub
		// System.out.println("Removed from session:
		// "+event.getName()+"==>"+event.getValue());
	}

	public void attributeReplaced(HttpSessionBindingEvent event) {
		// System.out.println("Replaced in the session:
		// "+event.getName()+"==>"+event.getValue());
		/*
		 * if(event.getName().equals("testId")){
		 * 
		 * UserBean ub = (UserBean)event.getSession().getAttribute("userBean");
		 * Long testId = new Long(((String)
		 * event.getSession().getAttribute("testId")).trim());
		 * ub.setQuestions(testId,event); }
		 */
	}

}
