package it.tecla.test;

import javax.annotation.security.RolesAllowed;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;

import io.swagger.annotations.Api;

@Path("/admin")
@Produces("application/json")
@Api("Operazioni di ADMIN")
@RolesAllowed("admin")
public class AdminRestService {
	
	@GET
	@Path("/admin")
	public String doAdmin(@QueryParam("user") String user) throws Exception {
		return "ADMIN!";
	}
	
}
