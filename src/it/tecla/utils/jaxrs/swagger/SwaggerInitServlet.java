package it.tecla.utils.jaxrs.swagger;

import java.io.File;
import java.io.IOException;
import java.util.Iterator;
import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.filefilter.IOFileFilter;

import io.swagger.jaxrs.config.BeanConfig;
import it.tecla.utils.jaxrs.JaxRsApplication;
import it.tecla.utils.properties.PropertiesFactory;

/**
 * Classe per inizializzare swagger e per fare una redirect da /swagger-ui al percorso relativo.
 * NOTA: swagger-ui si trova dentro ad un jar, per cui durante l'inizializzazione viene cercato e salvato il percorso
 * @author Nicolo' Chieffo
 *
 */
@WebServlet(value = { "/swagger-ui" }, loadOnStartup = 2)
public class SwaggerInitServlet extends HttpServlet {

	private static final long serialVersionUID = 1L;
	
	private static final String SWAGGER_UI_REGEX = "swagger-ui-(.*)\\.jar";
	
	private String swaggerUiPath;

	@Override
	public void init(ServletConfig config) throws ServletException {
		
		super.init(config);
		
		Properties properties = PropertiesFactory.getInstance();
		String swaggerVersion = properties.getProperty("app.version", "unknown");
		String swaggerResources = properties.getProperty("swagger.resources");
		
		if (swaggerResources == null) {
			throw new IllegalArgumentException("please set swagger.resources properties");
		}

		// inizializzazione swagger
		BeanConfig beanConfig = new BeanConfig();
		beanConfig.setVersion(swaggerVersion);
		beanConfig.setBasePath(config.getServletContext().getContextPath() + "/" + JaxRsApplication.API_PATH);
		beanConfig.setResourcePackage(swaggerResources);
		beanConfig.setScan(true);
		
		// inizializzazione swagger-ui tramite ricerca del webjar relativo
		// il percorso nel webjar include una directori con il numero di versione che quindi viene letto dal nome del file
		String libPath = config.getServletContext().getRealPath("/WEB-INF/lib/");
		final Pattern pattern = Pattern.compile(SWAGGER_UI_REGEX);
		Iterator<File> jarFileIterator = FileUtils.iterateFiles(FileUtils.getFile(libPath), new IOFileFilter() {

			@Override
			public boolean accept(File file) {
				return pattern.matcher(file.getName()).matches();
			}

			@Override
			public boolean accept(File dir, String filename) {
				return false;
			}
			
		}, null);
		
		if (jarFileIterator.hasNext()) {
			File swaggerUiJarFile = jarFileIterator.next();
			Matcher matcher = pattern.matcher(swaggerUiJarFile.getName());
			if (matcher.matches()) {
				String swaggerUiVersion = matcher.group(1);
				swaggerUiPath = config.getServletContext().getContextPath() + "/webjars/swagger-ui/" + swaggerUiVersion + "/?url=" + config.getServletContext().getContextPath() + "/api/swagger.json";
			}
		}
	}
	
	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		try {
			if (swaggerUiPath == null) {
				response.sendError(404, "swagger-ui webjar not found in WEB-INF/lib (filename example: swagger-ui-3.2.0.jar)");
			} else {
				response.sendRedirect(swaggerUiPath);
			}
		} catch (Exception ex) {
			// noop
		}
	}

}
