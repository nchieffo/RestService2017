package it.tecla.utils.logging;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletInputStream;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.slf4j.Marker;
import org.slf4j.MarkerFactory;

public class LoggingFilter implements Filter {
	
	public static final Logger LOGGER = LoggerFactory.getLogger(LoggingFilter.class);
	public static final Marker SKIP_STDOUT_MARKER = MarkerFactory.getMarker("SKIP_STDOUT");
	
	private String urlType;
	private boolean extractBody;

	public void init(FilterConfig config) throws ServletException {
		
		urlType = config.getInitParameter("url_type");
		if (urlType == null) {
			urlType = "path_info";
		}
		
		extractBody = StringUtils.equals(config.getInitParameter("extract_body"), "true");
	}

	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
		
		String requestBody = null;
		String remoteUser = null;
		HttpSession session = null;
		String contentType = null;
		String req = null;
		
		if (request instanceof HttpServletRequest) {

			try {
			
				final HttpServletRequest httpRequest = (HttpServletRequest) request;
				
				StringBuffer url;
				if (urlType.equals("full")) {
					url = httpRequest.getRequestURL();
				} else if (urlType.equals("context_path")) {
					url = new StringBuffer(httpRequest.getContextPath() + httpRequest.getServletPath() + httpRequest.getPathInfo());
				} else if (urlType.equals("servlet_path")) {
					url = new StringBuffer(httpRequest.getServletPath() + httpRequest.getPathInfo());
				} else if (urlType.equals("path_info")) {
					url = new StringBuffer(httpRequest.getPathInfo());
				} else {
					url = new StringBuffer(httpRequest.getServletPath() + httpRequest.getPathInfo());
				}
				
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
				
				if (extractBody && StringUtils.containsAny(contentType, "application/json", "text/json", "application/x-www-form-urlencoded", "text/plain")) {
					
					Reader reader = httpRequest.getReader();
					final String requestBodyTemp = IOUtils.toString(reader);
					requestBody = requestBodyTemp;
					
	//				if (reader != null) {
	//					if (StringUtils.isNotBlank(requestBody)) {
	//						MDC.put("req.body", requestBody);
	//					}
	//				}
					
					request = new HttpServletRequestWrapper(httpRequest) {
						
						BufferedReader reader = new BufferedReader(new StringReader(requestBodyTemp));
						
						@Override
						public ServletInputStream getInputStream() throws IOException {
							
							return new ServletInputStream() {
								
								@Override
								public int read() throws IOException {
									return reader.read();
								}
								
							};
						}
						
						@Override
						public BufferedReader getReader() throws IOException {
							return reader;
						}
						
					};
				}
	
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
