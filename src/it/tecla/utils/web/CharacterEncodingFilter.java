package it.tecla.utils.web;

import java.io.IOException;
import java.util.Enumeration;
import java.util.Vector;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServletResponseWrapper;

/**
 * Filtro HTTP che imposta il character set di default, se non specificato nella request
 * @author Nicolo' Chieffo
 *
 */
public class CharacterEncodingFilter implements Filter {

	private static String defaultCharset;
	
	public static String getDefaultCharset() {
		return defaultCharset;
	}
	
	public void init(FilterConfig config) throws ServletException {
		defaultCharset = config.getInitParameter("defaultCharset");
		if (defaultCharset == null) {
			defaultCharset = "UTF-8";
		}
	}

	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
		
		String characterEncoding = request.getCharacterEncoding();
		if (characterEncoding == null) {
			request.setCharacterEncoding(defaultCharset);
		}
		
		if (request instanceof HttpServletRequest) {
			
			HttpServletRequest httpServletRequest = (HttpServletRequest)request;
			HttpServletResponse httpServletResponse = (HttpServletResponse)response;
			String accept = httpServletRequest.getHeader("Accept");
			
			if (accept != null && !accept.toLowerCase().contains("charset=")) {
				
				HttpServletRequestWrapper requestWrapper = new HttpServletRequestWrapper(httpServletRequest) {
					
					@Override
					public String getHeader(String name) {
						Enumeration<String> headerValues = this.getHeaders(name);
						if (headerValues.hasMoreElements()) {
							return headerValues.nextElement();
						}
						return null;
					}
					
					@Override
					public Enumeration<String> getHeaders(String name) {
						
						Enumeration<String> headerValues =  super.getHeaders(name);
						
						if (name.equalsIgnoreCase("Accept")) {
							Vector<String> newHeaderValues = new Vector<String>();
							while (headerValues.hasMoreElements()) {
								String headerValue = headerValues.nextElement();
								if (!headerValue.toLowerCase().contains("charset")) {
									headerValue += ";charset=" + defaultCharset;
								}
								newHeaderValues.add(headerValue);
							}
							headerValues = newHeaderValues.elements();
						}
						
						return headerValues;
					}
					
				};
				
				request = requestWrapper;
			}
			
			HttpServletResponseWrapper responseWrapper = new HttpServletResponseWrapper(httpServletResponse) {
				
				@Override
				public void setContentType(String type) {
					if (!type.toLowerCase().contains("charset")) {
						type += ";charset=" + defaultCharset;
					}
					super.setContentType(type);
				}
				
				@Override
				public void setCharacterEncoding(String charset) {
					if (charset == null) {
						charset = defaultCharset;
					}
					super.setCharacterEncoding(charset);
				}
				
			};
			response = responseWrapper;
		}
		
		chain.doFilter(request, response);
	}

	public void destroy() {
		
	}

}
