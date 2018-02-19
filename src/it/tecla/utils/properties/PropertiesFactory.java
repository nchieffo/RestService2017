package it.tecla.utils.properties;

import java.io.InputStream;
import java.util.Properties;

/**
 * Factory per inizializzare i file di properties
 * @author Nicolo' Chieffo
 *
 */
public class PropertiesFactory {
	
	public static final String DEFAULT_PROPERTIES_PATH = "application.properties";
	
	private PropertiesFactory() {
		throw new UnsupportedOperationException("Utility class");
	}
	
	public static Properties getInstance() {
		return getInstanceFromClasspath(DEFAULT_PROPERTIES_PATH);
	}

	public static Properties getInstanceFromClasspath(String classPath) {
		
		try {
			
			Properties properties = new Properties();
			InputStream is = PropertiesFactory.class.getClassLoader().getResourceAsStream(classPath);
			properties.load(is);
			is.close();
			
			return properties;
			
		} catch (Exception ex) {
			throw new IllegalStateException(ex);
		}
	}
}
