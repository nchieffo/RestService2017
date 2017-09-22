package it.tecla.test;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;

import org.apache.commons.configuration.Configuration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.swagger.annotations.Api;
import it.tecla.utils.configuration.ConfigurationFactory;
import it.tecla.utils.logging.LoggerEntryMessage;
import it.tecla.utils.model.OperationResult;

@Path("/test")
@Produces("application/json")
@Api
public class TestRestService {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(TestRestService.class);

	@GET
	@Path("/echo")
	public OperationResult echo(@QueryParam("msg") String msg) throws InterruptedException {
		
		LoggerEntryMessage loggerEntryMessage = LoggerEntryMessage.create(LOGGER, msg).log();
		
		OperationResult operationResult = new OperationResult();
		loggerEntryMessage.getStepMessage("step1").log();
		
		Thread.sleep(250);
		
		operationResult.success("OK", msg);

		loggerEntryMessage.getExitMessage(operationResult).log();
		
		return operationResult;
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
