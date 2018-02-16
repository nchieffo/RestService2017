package it.tecla.utils.web;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
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
	
	public static void setDefaultCharset(String defaultCharset) {
		CharacterEncodingFilter.defaultCharset = defaultCharset;
	}
	
	@Override
	public void init(FilterConfig config) throws ServletException {
		String charset = config.getInitParameter("defaultCharset");
		if (charset == null) {
			charset = "UTF-8";
		}
		setDefaultCharset(charset);
	}

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
		
		String characterEncoding = request.getCharacterEncoding();
		
		if (characterEncoding == null) {
			request.setCharacterEncoding(defaultCharset);
		}
		
		if (request instanceof HttpServletRequest) {
			
			HttpServletResponseWrapper responseWrapper = new HttpServletResponseWrapper((HttpServletResponse)response) {
				
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

	@Override
	public void destroy() {
		// nothing to destroy
	}

}
