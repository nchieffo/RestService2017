package it.tecla.utils.jaxrs;

import javax.ws.rs.ApplicationPath;
import javax.ws.rs.core.Application;

@ApplicationPath(JaxRsApplication.API_PATH)
public class JaxRsApplication extends Application {
	
	public static final String API_PATH = "/api";
	
}
