package it.tecla.utils.web.security;

import java.security.Principal;
import java.util.Collections;
import java.util.List;

public class CustomPrincipal implements Principal {
	
	private final String name;
	private final List<String> roles;
	
	public CustomPrincipal(String name, List<String> roles) {
		this.name = name;
		this.roles = roles;
	}

	@Override
	public String getName() {
		return name;
	}
	
	public List<String> getRoles() {
		return Collections.unmodifiableList(roles);
	}

}
