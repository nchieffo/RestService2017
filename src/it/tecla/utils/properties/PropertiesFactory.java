package it.tecla.utils.properties;

import java.io.InputStream;
import java.util.Properties;

public class PropertiesFactory {

	public static Properties getInstance() {
		
		try {
			
			Properties properties = new Properties();
			InputStream is = PropertiesFactory.class.getResourceAsStream("/application.properties");
			properties.load(is);
			is.close();
			
			return properties;
			
		} catch (Exception ex) {
			throw new RuntimeException(ex);
		}
	}
}
