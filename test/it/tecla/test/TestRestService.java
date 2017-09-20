package it.tecla.test;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;

import io.swagger.annotations.Api;
import it.tecla.utils.model.OperationResult;

@Path("/test")
@Produces("application/json")
@Api
public class TestRestService {

	@GET
	@Path("/echo")
	public OperationResult echo(@QueryParam("msg") String msg) throws InterruptedException {
		
		OperationResult operationResult = new OperationResult();
		
		Thread.sleep(1000);
		
		return operationResult.success("OK", msg);
	}
	
}