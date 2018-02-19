package it.tecla.utils.jaxrs;

import javax.ws.rs.ApplicationPath;
import javax.ws.rs.core.Application;

/**
 * Questa classe permette l'inizializzazione di JAX-RS sotto il path /api
 * Tutti i servizi rest che saranno creati risponderanno all'indirizzo /api/xxx
 * con xxx = @Path("xxx")
 * @author Nicolo' Chieffo
 *
 */
@ApplicationPath(JaxRsApplication.API_PATH)
public class JaxRsApplication extends Application {
	
	public static final String API_PATH = "api";
	
}
