package  com.esoft.ischool.listener;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Date;
import java.util.Enumeration;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.esoft.ischool.security.model.SessionHistory;
import com.esoft.ischool.security.service.UserService;

@Service("myServletFilter")
public class MyFilter implements Filter {

	@Autowired
	UserService userService;
	
	FilterConfig filterConfig = null;

	public void init(FilterConfig filterConfig) throws ServletException {
		this.filterConfig = filterConfig;
	}

	public void destroy() {
	}

	public void doFilter(ServletRequest request, ServletResponse response, FilterChain filterChain)
			throws IOException, ServletException {
		SessionHistory sessionHistory = new SessionHistory();
		//sessionHistory.setBeginDate(new Date());
		//sessionHistory.setSessionId(request.get);
		//sessionHistory.setHostIp(request.getRemoteAddr());
		//sessionHistory.setHostName(request.getRemoteHost());
		//sessionHistory.setLanguage(request.getLocalName());
		//sessionHistory.setOsuser(request.getRemoteUser());
		//sessionHistory.setBrowser(request.getHeader("User-Agent"));
		//userService.save(sessionHistory, user);
		
		filterChain.doFilter(request, response);

	}

}
