package it.tecla.utils.web.security;

import java.io.IOException;
import java.security.Principal;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;

/**
 * Servlet Filter implementation class CustomSecurityFilter
 */
@WebFilter("/*")
public class CustomSecurityFilter implements Filter {

	/**
	 * Default constructor.
	 */
	public CustomSecurityFilter() {
		// TODO Auto-generated constructor stub
	}

	/**
	 * @see Filter#init(FilterConfig)
	 */
	@Override
	public void init(FilterConfig fConfig) throws ServletException {
		// TODO Auto-generated method stub
	}

	/**
	 * @see Filter#doFilter(ServletRequest, ServletResponse, FilterChain)
	 */
	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
		
		HttpServletRequestWrapper requestWrapper = new HttpServletRequestWrapper((HttpServletRequest) request) {
			
			private CustomPrincipal principal;
			private boolean principalInitialized;
			
			protected void initPrincipal() {
				if (!principalInitialized) {
					principalInitialized = true;
					String user = this.getParameter("user");
					
					if (user != null) {
						List<String> roles = new ArrayList<String>();
						if (user.equals("admin")) {
							roles.add("admin");
						}
						principal = new CustomPrincipal(user, roles);
					}
				}
			}
			
			@Override
			public Principal getUserPrincipal() {
				
				initPrincipal();
				
				return principal;
			}
			
			@Override
			public boolean isUserInRole(String role) {
				
				initPrincipal();
				
				return principal != null && principal.getRoles().contains(role);
			}
			
			
		};
		
		chain.doFilter(requestWrapper, response);
	}

	/**
	 * @see Filter#destroy()
	 */
	@Override
	public void destroy() {
		// TODO Auto-generated method stub
	}

}
