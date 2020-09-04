package com.fueber.taxi.util;

import java.io.InputStream;
import java.util.Properties;

public class FueberTaxiUtils {
	
	private FueberTaxiUtils() {
		
	}

	/**Method to load the properties*/
	public static Properties loadProperties(String propertyFilename) {
		Properties properties = null;
		try(InputStream inputStream = FueberTaxiUtils.class.getClassLoader().getResourceAsStream(propertyFilename)) {
			properties = new Properties();
			properties.load(inputStream);
		} catch(Exception exception) {
			properties = new Properties();
		}
		return properties;
	}
}
