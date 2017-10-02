package it.tecla.utils.logging;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.slf4j.Marker;
import org.slf4j.MarkerFactory;

public class LoggingFilter implements Filter {
	
	public static final Logger LOGGER = LoggerFactory.getLogger(LoggingFilter.class);
	public static final Marker SKIP_STDOUT_MARKER = MarkerFactory.getMarker("SKIP_STDOUT");

	public void init(FilterConfig config) throws ServletException {
		
	}

	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
		
		if (request instanceof HttpServletRequest) {
			
			HttpServletRequest httpRequest = (HttpServletRequest) request;
			
			StringBuffer fullUrl = httpRequest.getRequestURL();
			StringBuffer relativeUrl = new StringBuffer(httpRequest.getRequestURI());
			if (httpRequest.getQueryString() != null) {
				fullUrl.append("?");
				fullUrl.append(httpRequest.getQueryString());
				relativeUrl.append("?");
				relativeUrl.append(httpRequest.getQueryString());
			}
			
			if (httpRequest.getRemoteUser() != null) {
				MDC.put("req.user", httpRequest.getRemoteUser());
			}
			
			HttpSession session = httpRequest.getSession(false);
			if (session != null) {
				MDC.put("req.sessionId", session.getId());
			}

			MDC.put("req", httpRequest.getMethod() + " " + relativeUrl);
		}
		
		try {
			
			chain.doFilter(request, response);
			
		} catch(Throwable t) {
			
			boolean skipLogging = false;
			if (response instanceof HttpServletResponse) {
				HttpServletResponse httpResponse = (HttpServletResponse)response;
				if (httpResponse.getStatus() > 0) {
					skipLogging = true;
				}
			}
			
			if (!skipLogging) {
				LOGGER.error("Uncaught exception while serving request {}", MDC.getCopyOfContextMap());
				LOGGER.error(SKIP_STDOUT_MARKER, "Uncaught exception while serving request " + MDC.getCopyOfContextMap(), t);
			}
			
			if (t instanceof RuntimeException) {
				throw (RuntimeException)t;
			} else if (t instanceof IOException) {
				throw (IOException)t;
			} else if (t instanceof ServletException) {
				throw (ServletException)t;
			}
			throw new RuntimeException(t);
		}
	}

	public void destroy() {
		
	}

}
