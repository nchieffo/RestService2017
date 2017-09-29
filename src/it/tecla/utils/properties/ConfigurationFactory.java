package it.tecla.utils.properties;

import org.apache.commons.configuration.Configuration;
import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.PropertiesConfiguration;
import org.apache.commons.configuration.reloading.FileChangedReloadingStrategy;

public class ConfigurationFactory {

	public static Configuration getInstance() {
		
		try {
			
			PropertiesConfiguration config = new PropertiesConfiguration("/application.properties");
			config.setReloadingStrategy(new FileChangedReloadingStrategy());
			return config;
			
		} catch (ConfigurationException ex) {
			throw new RuntimeException(ex);
		}
	}
	
}