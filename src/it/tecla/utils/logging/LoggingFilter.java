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

/**
 * Questa classe estrae dalla request i dati per poi renderli disponibili nella MDC del logger.
 * In caso di eccezione provvede anche a loggare la request che ha generato l'errore.
 * @author Nicolo' Chieffo
 *
 */
public class LoggingFilter implements Filter {
	
	public static final Logger LOGGER = LoggerFactory.getLogger(LoggingFilter.class);
	public static final Marker SKIP_STDOUT_MARKER = MarkerFactory.getMarker("SKIP_STDOUT");
	
	public void init(FilterConfig config) throws ServletException {
		
	}

	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
		
		String remoteUser = null;
		HttpSession session = null;
		String contentType = null;
		String req = null;
		
		if (request instanceof HttpServletRequest) {

			try {
			
				final HttpServletRequest httpRequest = (HttpServletRequest) request;
				
				StringBuffer url;
				
//				url = httpRequest.getRequestURL();
//				url = new StringBuffer(httpRequest.getContextPath() + httpRequest.getServletPath() + httpRequest.getPathInfo());
				url = new StringBuffer(httpRequest.getServletPath() + httpRequest.getPathInfo());
//				url = new StringBuffer(httpRequest.getPathInfo());
				
				if (httpRequest.getQueryString() != null) {
					url.append("?");
					url.append(httpRequest.getQueryString());
				}
				
				remoteUser = httpRequest.getRemoteUser();
				if (remoteUser != null) {
					MDC.put("req.user", remoteUser);
				}
				
				session = httpRequest.getSession(false);
				if (session != null) {
					MDC.put("req.sessionId", session.getId());
				}
				
				contentType = httpRequest.getContentType();
	//			if (contentType != null) {
	//				MDC.put("req.contentType", contentType);
	//			}
	
				if (contentType != null) {
					req = httpRequest.getMethod() + "(" + contentType + ") " + url;
				} else {
					req = httpRequest.getMethod() + " " + url;
				}
				MDC.put("req", req);
			
			} catch (Throwable t) {
				LOGGER.warn("Error while trying to extract request information to log", t);
			}
		}
		
		try {
			
			chain.doFilter(request, response);
			
		} catch(Throwable t) {
			
			String requestBody = MDC.get("req.body");
			
			StringBuilder sb = new StringBuilder();
			sb.append("Uncaught exception while serving request");
			sb.append("\n");
			sb.append(t);
			sb.append("\n");
			sb.append("\n");
			if (remoteUser != null) {
				sb.append("\n");
				sb.append("remoteUser: ");
				sb.append(remoteUser);
			}
			if (session != null) {
				sb.append("\n");
				sb.append("sessionId: ");
				sb.append(session.getId());
			}
			sb.append("\n");
			sb.append(req);
			if (requestBody != null) {
				sb.append("\n");
				sb.append(requestBody);
			}
			String errorMessage = sb.toString();
			
			boolean skipLogging = false;
			if (response instanceof HttpServletResponse) {
				HttpServletResponse httpResponse = (HttpServletResponse)response;
				int status = httpResponse.getStatus();
				if (status != 200 || status == 500) {
					skipLogging = true;
				} else {
					httpResponse.setContentType("text/plain");
					httpResponse.setCharacterEncoding(request.getCharacterEncoding());
					httpResponse.setStatus(500);
					httpResponse.getWriter().write(errorMessage);
				}
			}
			
			if (!skipLogging) {
				LOGGER.error(errorMessage);
				LOGGER.error(SKIP_STDOUT_MARKER, errorMessage, t);
			}
			
			// non è necessario fare throw dell'eccezione perchè è già stata stampata sia da JAX-RS che dal motore delle servlet
			// lo status lo setto prima!!!
			
		}
		
		MDC.clear();
	}

	public void destroy() {
		
	}

}
