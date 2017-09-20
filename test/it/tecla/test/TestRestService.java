package it.tecla.test;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;

import org.apache.commons.configuration.Configuration;

import io.swagger.annotations.Api;
import it.tecla.utils.configuration.ConfigurationFactory;
import it.tecla.utils.logging.Logged;
import it.tecla.utils.model.OperationResult;

@Path("/test")
@Produces("application/json")
@Api
@Logged
public class TestRestService {

	@GET
	@Path("/echo")
	public OperationResult echo(@QueryParam("msg") String msg, @Context HttpServletRequest request) throws InterruptedException {
		
		OperationResult operationResult = new OperationResult();
		
		Thread.sleep(250);
		
		return operationResult.success("OK", msg);
	}

	@GET
	@Path("/void")
	public void doVoid() throws InterruptedException {
		Thread.sleep(250);
	}

	@GET
	@Path("/configuration")
	@Produces("text/plain")
	public String configuration(@QueryParam("key") String key) throws InterruptedException {
		Configuration configuration = ConfigurationFactory.getInstance();
		return configuration.getString(key);
	}
	
}
