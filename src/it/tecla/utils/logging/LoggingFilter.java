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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.slf4j.Marker;
import org.slf4j.MarkerFactory;

@WebFilter(urlPatterns = "/*", dispatcherTypes = { DispatcherType.REQUEST, DispatcherType.FORWARD, DispatcherType.INCLUDE })
public class LoggingFilter implements Filter {
	
	public static final Logger LOGGER = LoggerFactory.getLogger(LoggingFilter.class);
	public static final Marker SKIP_STDOUT_MARKER = MarkerFactory.getMarker("SKIP_STDOUT");

	public void init(FilterConfig config) throws ServletException {
		
	}

	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
		
		if (request instanceof HttpServletRequest) {
			
			HttpServletRequest httpRequest = (HttpServletRequest) request;
			
//			MDC.put("req.method", httpRequest.getMethod());
			
			StringBuffer fullUrl = httpRequest.getRequestURL();
			StringBuffer relativeUrl = new StringBuffer(httpRequest.getRequestURI());
			if (httpRequest.getQueryString() != null) {
				fullUrl.append("?");
				fullUrl.append(httpRequest.getQueryString());
				relativeUrl.append("?");
				relativeUrl.append(httpRequest.getQueryString());
			}
			
//			MDC.put("req.url", fullUrl.toString());
			
			if (httpRequest.getRemoteUser() != null) {
				MDC.put("req.user", httpRequest.getRemoteUser());
			}
			
			HttpSession session = httpRequest.getSession(false);
			if (session != null) {
				MDC.put("req.sessionId", session.getId());
			}

			MDC.put("req.compact", httpRequest.getMethod() + " " + relativeUrl);
		}
		
		try {
			chain.doFilter(request, response);
		} catch(Throwable t) {
			LOGGER.error("Uncaught exception while serving request {}", MDC.getCopyOfContextMap());
			LOGGER.error(SKIP_STDOUT_MARKER, "Uncaught exception while serving request " + MDC.getCopyOfContextMap(), t);
			throw t;
		}
	}

	public void destroy() {
		
	}

}
