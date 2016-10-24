package  com.esoft.ischool.listener;

import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.google.common.util.concurrent.AtomicLongMap;
import com.esoft.ischool.security.model.SessionHistory;
import com.esoft.ischool.security.model.User;
import com.esoft.ischool.security.service.UserService;

@Service("mySessionListener")
public class MySessionListener implements HttpSessionListener {
	@Autowired
	UserService userService;
	
	public static Set<String> sessions = new HashSet<String>();
	
	public void sessionCreated(HttpSessionEvent se) {

		try {
	
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public void sessionDestroyed(HttpSessionEvent se) {
		try {
			System.out.println("Closing the session ...");

			Long userId = (Long)se.getSession().getAttribute("userId");
			Long sessionHistoryId = (Long)se.getSession().getAttribute("sessionHistoryId");
			SessionHistory sh = (SessionHistory) userService.getById(SessionHistory.class, sessionHistoryId);
			if (sh != null) {
				sh.setEndDate(new Date());
				
				if (userId != null) {
					User user = (User) userService.getById(User.class, userId);
					userService.update(sh, user);				
				}
			}
			sessions.remove(se.getSession().getId());
		} catch (Exception e) {
			System.out.println("Impossible to close connection");
			e.printStackTrace();
		}
	}
	
	public void sessionDestroyed(HttpServletRequest sr) {
		try {
			System.out.println("Closing the session ...");

			Long userId = (Long)sr.getSession().getAttribute("userId");
			Long sessionHistoryId = (Long)sr.getSession().getAttribute("sessionHistoryId");
			SessionHistory sh = (SessionHistory) userService.getById(SessionHistory.class, sessionHistoryId);
			if (sh != null) {
				sh.setEndDate(new Date());
				
				if (userId != null) {
					User user = (User) userService.getById(User.class, userId);
					userService.update(sh, user);				
				}
			}
			sessions.remove(sr.getSession().getId());
		} catch (Exception e) {
			System.out.println("Impossible to close connection");
			e.printStackTrace();
		}
	}
}
