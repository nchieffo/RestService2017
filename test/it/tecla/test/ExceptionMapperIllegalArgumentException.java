package it.tecla.test;

import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

@Provider
public class ExceptionMapperIllegalArgumentException implements ExceptionMapper<IllegalArgumentException> {

	@Override
	public Response toResponse(IllegalArgumentException ex) {
		return Response.status(Response.Status.BAD_REQUEST).entity("IllegalArgumentException").build();
	}

}
