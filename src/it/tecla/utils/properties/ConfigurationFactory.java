package it.tecla.utils.properties;

import org.apache.commons.configuration.Configuration;
import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.PropertiesConfiguration;
import org.apache.commons.configuration.reloading.FileChangedReloadingStrategy;

/**
 * Factory per inizializzare i file di properties come oggetti di tipo Configuration
 * @author Nicolo' Chieffo
 *
 */
public class ConfigurationFactory {
	
	public static final String DEFAULT_PROPERTIES_PATH = "/application.properties";
	
	public static Configuration getInstance() {
		return getInstanceFromClasspath(DEFAULT_PROPERTIES_PATH);
	}

	public static Configuration getInstanceFromClasspath(String classPath) {
		
		try {
			
			PropertiesConfiguration config = new PropertiesConfiguration(classPath);
			config.setReloadingStrategy(new FileChangedReloadingStrategy());
			return config;
			
		} catch (ConfigurationException ex) {
			throw new RuntimeException(ex);
		}
	}
	
}