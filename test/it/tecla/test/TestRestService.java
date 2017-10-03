package it.tecla.test;

import javax.mail.Session;
import javax.naming.InitialContext;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;

import org.apache.commons.configuration.Configuration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiParam;
import it.tecla.utils.model.OperationResult;
import it.tecla.utils.properties.ConfigurationFactory;

@Path("/test")
@Produces("application/json")
@Api("Operazioni di TEST")
public class TestRestService {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(TestRestService.class);

	@GET
	@Path("/echo")
	@Produces("text/plain")
	public String echo(@ApiParam("messaggio che verrà ritornato") @QueryParam("msg") String msg) throws Exception {
		
		return msg;
	}

	@GET
	@Path("/void")
	public void doVoid() throws Exception {
		Thread.sleep(250);
	}

	@GET
	@Path("/log/trace")
	public OperationResult logTrace() throws Exception {
		LOGGER.trace("trace!");
		return new OperationResult().success("OK", null);
	}

	@GET
	@Path("/log/error")
	public OperationResult logError() throws Exception {
		LOGGER.trace("trace!");
		LOGGER.debug("debug!");
		LOGGER.info("info!");
		LOGGER.warn("warn!");
		LOGGER.error("error!", new RuntimeException());
		return new OperationResult().success("OK", null);
	}

	@GET
	@Path("/throw/unhandled")
	public void throwUnhandledException() throws Exception {
		throw new Exception("error!");
	}

	@GET
	@Path("/throw/handled")
	public void throwHandledException() throws Exception {
		throw new IllegalArgumentException("error!");
	}

	@GET
	@Path("/throw/web")
	public void throwWebException() throws Exception {
		throw new WebApplicationException(Response.Status.BAD_REQUEST);
	}

	@GET
	@Path("/configuration")
	@Produces("text/plain")
	public String configuration(@QueryParam("key") String key) throws Exception {
		Configuration configuration = ConfigurationFactory.getInstance();
		return configuration.getString(key);
	}

	@GET
	@Path("/smtp")
	public OperationResult smtp() throws Exception {
		Session session = InitialContext.doLookup("mail/smtp");
		LOGGER.info("{}", session);
		return new OperationResult().success("OK", null);
	}
	
}
