package it.tecla.utils.logging;

import java.io.IOException;

import javax.servlet.DispatcherType;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.slf4j.MDC;

@WebFilter(urlPatterns = "/*", dispatcherTypes = { DispatcherType.REQUEST, DispatcherType.FORWARD, DispatcherType.INCLUDE })
public class LoggingFilter implements Filter {

	public void init(FilterConfig config) throws ServletException {
		
	}

	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
		
//		MDC.put("logFileName", Thread.currentThread().getName());
		
		if (request instanceof HttpServletRequest) {
			HttpServletRequest httpRequest = (HttpServletRequest) request;
			
			MDC.put("req.method", httpRequest.getMethod());
			StringBuffer url = httpRequest.getRequestURL();
			if (httpRequest.getQueryString() != null) {
				url.append("?");
				url.append(httpRequest.getQueryString());
			}
			
			MDC.put("req.url", url.toString());
			if (httpRequest.getRemoteUser() != null) {
				MDC.put("req.user", httpRequest.getRemoteUser());
			}
			
			HttpSession session = httpRequest.getSession(false);
			if (session != null) {
				MDC.put("req.sessionId", session.getId());
			}

		}
		
		chain.doFilter(request, response);
	}

	public void destroy() {
		
	}

}
