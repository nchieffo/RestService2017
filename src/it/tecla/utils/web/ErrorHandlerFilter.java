package it.tecla.utils.web;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Date;
import java.util.UUID;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import it.tecla.utils.properties.PropertiesFactory;

/**
 * Filtro HTTP che permette di gestire gli errori nascondendoli o mostrandoli
 * @author Nicolo' Chieffo
 *
 */
public class ErrorHandlerFilter implements Filter {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(ErrorHandlerFilter.class);
	
	private boolean printError;

	@Override
	public void init(FilterConfig config) throws ServletException {
		printError = PropertiesFactory.getInstance().getProperty("errorHandler.printErrors", "true").equalsIgnoreCase("true");
	}

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
		
		try {
			
			chain.doFilter(request, response);
			
		} catch (Exception ex) {
			
			int status = 0;
			
			if (response instanceof HttpServletResponse) {
				status = ((HttpServletResponse) response).getStatus();
			}
			
			if (status == 200) {
				// se lo status è 200 vuol dire che l'errore non è stato ancora riconosciuto, devo gestire il caso
				
				if (!response.isCommitted()) {
					response.reset();
					response.setContentType("text/plain");
					response.setCharacterEncoding(request.getCharacterEncoding());
					((HttpServletResponse)response).setStatus(500);
				}
				
				String errorUUID = UUID.randomUUID().toString();
				// TODO nel logger è utile aggiungere la request che ha provocato l'errore
				LOGGER.error("ERROR_UUID=" + errorUUID + " ", ex);
				
				PrintWriter writer = response.getWriter();
				try {
					writer.write(new Date().toString());
					writer.write("\n");
					
					writer.write("An unhandled error has occurred\nERROR_UUID=");
					writer.write(errorUUID);
					writer.write("\n");
					
					if (printError) {
						ex.printStackTrace(writer);
						writer.write("\n");
					}
					
				} finally {
					writer.close();
				}
			}
		}
	}

	@Override
	public void destroy() {
		// nothing to destroy
	}

}
