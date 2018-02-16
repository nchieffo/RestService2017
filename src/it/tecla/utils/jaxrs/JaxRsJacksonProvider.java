package it.tecla.utils.jaxrs;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.annotation.Annotation;
import java.lang.reflect.Type;
import java.util.Calendar;
import java.util.TimeZone;

import javax.ws.rs.Consumes;
import javax.ws.rs.Produces;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.MessageBodyReader;
import javax.ws.rs.ext.MessageBodyWriter;
import javax.ws.rs.ext.Provider;

import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.util.StdDateFormat;

/**
 * Questa classe permette di customizzare il formato di lettura/scrittura dei JSON per i servizi di JAX-RS
 * @author Nicolo' Chieffo
 *
 */
@Provider
@Consumes({"application/json", "text/json"})
@Produces({"application/json", "text/json"})
public class JaxRsJacksonProvider implements MessageBodyReader<Object>, MessageBodyWriter<Object> {
	
	private ObjectMapper mapper = new ObjectMapper();
	
	public JaxRsJacksonProvider() {
		
		// LETTURA
		// Evita le eccezioni quando sul JSON di input vengono passati dei campi sconosciuti al servizio
		mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
		// Legge enumerativi usando il loro nome
		mapper.configure(DeserializationFeature.READ_ENUMS_USING_TO_STRING, true);
		
		// SCRITTURA
		// Abilita Indentazione
		mapper.configure(SerializationFeature.INDENT_OUTPUT, true);
		// Scrive enumerativi usando il loro nome
		mapper.configure(SerializationFeature.WRITE_ENUMS_USING_TO_STRING, true);
		// Evita le eccezioni in caso di input nullo
		mapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
		// Serializza anche i campi nulli
		mapper.setSerializationInclusion(Include.ALWAYS);

		// formato di input/output per le date utilizzando il timezone di sistema, o UTC come fallback
		StdDateFormat dateFormat = new StdDateFormat();
		dateFormat.setCalendar(Calendar.getInstance());
		dateFormat.setTimeZone(TimeZone.getDefault());
		mapper.setDateFormat(dateFormat);
	}

	@Override
	public long getSize(Object value, Class<?> type, Type genericType, Annotation[] annotations, MediaType mediaType) {
		return -1L;
	}

	@Override
	public boolean isWriteable(Class<?> type, Type genericType, Annotation[] annotations, MediaType mediaType) {
		return isJsonType(mediaType);
	}

	@Override
	public void writeTo(Object value, Class<?> type, Type genericType, Annotation[] annotations, MediaType mediaType, MultivaluedMap<String, Object> httpHeaders, OutputStream entityStream) throws IOException {
		mapper.writeValue(entityStream, value);
	}

	@Override
	public boolean isReadable(Class<?> type, Type genericType, Annotation[] annotations, MediaType mediaType) {
		return isJsonType(mediaType);
	}

	@Override
	public Object readFrom(Class<Object> type, Type genericType, Annotation[] annotations, MediaType mediaType, MultivaluedMap<String, String> httpHeaders, InputStream entityStream) throws IOException {
		try {
			return mapper.readValue(entityStream, type);
		} catch (Throwable t) {
			throw new WebApplicationException(t, Response.status(400).entity(t.getMessage()).type("text/plain").build());
		}
	}
	
	protected boolean isJsonType(MediaType mediaType) {
		if (mediaType != null) {
			String subtype = mediaType.getSubtype();
			return (("json".equalsIgnoreCase(subtype)) || (subtype.endsWith("+json")));
		}

		return true;
	}

}
