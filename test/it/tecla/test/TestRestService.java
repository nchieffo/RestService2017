package it.tecla.test;

import javax.annotation.security.RolesAllowed;
import javax.mail.Session;
import javax.naming.InitialContext;
import javax.ws.rs.Consumes;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
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
	private static final String UNICODE = "òàè+!£$%%&/\u2E80-\u2FD5\u3400-\u4DBF\u4E00-\u9FCC";

	@POST
	@Path("/echo")
	@Consumes("application/x-www-form-urlencoded")
	@Produces("text/plain")
	public String echo(@ApiParam("messaggio che verrà ritornato") @FormParam("msg") String msg) throws Exception {
		
		return msg;
	}

	@GET
	@RolesAllowed("admin")
	@Path("/admin")
	public String doAdmin(@QueryParam("user") String user) throws Exception {
		return "ADMIN!";
	}

	@GET
	@Path("/void")
	public void doVoid() throws Exception {
		
	}

	@GET
	@Path("/log/trace")
	public OperationResult logTrace() throws Exception {
		LOGGER.trace("trace!" + UNICODE);
		return new OperationResult().success("OK", null);
	}

	@GET
	@Path("/log/error")
	public OperationResult logError() throws Exception {
		LOGGER.trace("trace!" + UNICODE);
		LOGGER.debug("debug!" + UNICODE);
		LOGGER.info("info!" + UNICODE);
		LOGGER.warn("warn!" + UNICODE);
		LOGGER.error("error!" + UNICODE);
		
		return new OperationResult().success("OK", null);
	}

	@GET
	@Path("/throw/unhandled")
	public void throwUnhandledException() throws Exception {
		throw new Exception("error!");
	}

	@POST
	@Path("/throw/unhandled")
	@Consumes("application/json")
	public OperationResult throwUnhandledExceptionPost(@SuppressWarnings("unused") OperationResult operationResult) throws Exception {
		throw new Exception("error!");
	}

	@GET
	@Path("/throw/handled")
	public void throwHandledException() throws Exception {
		throw new IllegalArgumentException("handled error");
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
	
	@POST
	@Path("/operationresult")
	@Consumes("application/json")
	public OperationResult identity(OperationResult operationResult) {
//		throw new RuntimeException("error!");
		return operationResult;
	}
}
