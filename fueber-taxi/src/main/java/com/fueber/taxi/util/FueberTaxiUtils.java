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
	
	/**Method to calculate the distance between two points (Reference : haversine formula)*/
	public static int getDistanceBetweenPoints(double startLatitude, double startLongitude, double destinationLatitude, double destinationLongitude) {
		double radiusOfEarth = 6371e3; 
		double startLatitudeInRadians = startLatitude * Math.PI/180;
		double destLatitudeInRadians = destinationLatitude * Math.PI/180;
		double latitudeDiffereceInRadians = (destinationLatitude - startLatitude) * Math.PI/180;
		double longitudeDiffereceInRadians = (destinationLongitude - startLongitude) * Math.PI/180;
		double squareOfHalfLength = Math.sin(latitudeDiffereceInRadians/2) * Math.sin(latitudeDiffereceInRadians/2) +
          	Math.cos(startLatitudeInRadians) * Math.cos(destLatitudeInRadians) *
          	Math.sin(longitudeDiffereceInRadians/2) * Math.sin(longitudeDiffereceInRadians/2);
		double angularDistanceInRadians = 2 * Math.atan2(Math.sqrt(squareOfHalfLength), Math.sqrt(1-squareOfHalfLength));
		double distanceInKMS = (radiusOfEarth * angularDistanceInRadians)/1000;
		return (int)Math.round(distanceInKMS);
	}
}
