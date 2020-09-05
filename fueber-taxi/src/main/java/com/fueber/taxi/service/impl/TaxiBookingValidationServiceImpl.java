package com.fueber.taxi.service.impl;

import java.text.MessageFormat;
import java.util.Properties;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.fueber.taxi.common.Constants;
import com.fueber.taxi.exception.TaxiServiceException;
import com.fueber.taxi.service.TaxiBookingValidationService;
import com.fueber.taxi.util.FueberTaxiUtils;
import com.fueber.taxi.vo.ErrorVO;

@Service
public class TaxiBookingValidationServiceImpl implements TaxiBookingValidationService {
	
	@Value("${fueber.taxi.base.latitude}")
	private String baseLatitude;
	
	@Value("${fueber.taxi.base.longitude}")
	private String baseLongitude;
	
	@Value("${fueber.taxi.base.kilometerLimit}")
	private String baseKilometerLimit;
	
	@Autowired
	private Properties errorProperties;

	@Override
	public void validateCustomerPickupLocation(double latitude, double longitude) throws TaxiServiceException {
		int distanceBetweenPoints = getDistanceBetweenTwoPoints(latitude, longitude);
		if(distanceBetweenPoints > Integer.parseInt(baseKilometerLimit)) {
			throwTaxiServiceException(Constants.SERVICE_UNAVAILABLE_AT_PICKUP_LOCATION);
		}
	}
	
	@Override
	public void validateCustomerDropLocation(double latitude, double longitude) throws TaxiServiceException {
		int distanceBetweenPoints = getDistanceBetweenTwoPoints(latitude, longitude);
		if(distanceBetweenPoints > Integer.parseInt(baseKilometerLimit)) {
			throwTaxiServiceException(Constants.SERVICE_UNAVAILABLE_AT_DROP_LOCATION);
		}
	}
	
	private int getDistanceBetweenTwoPoints(double destinationLatitude, double destinationLongitude) {
		double baseLatitudeInDouble = Double.parseDouble(baseLatitude);
		double baseLongitudeInDouble = Double.parseDouble(baseLongitude);
		return FueberTaxiUtils.getDistanceBetweenPoints(baseLatitudeInDouble, baseLongitudeInDouble, destinationLatitude, destinationLongitude);
	}
	
	private void throwTaxiServiceException(String code, Object ... arguments) throws TaxiServiceException {
		String message = errorProperties.getProperty(code);
		if(arguments != null && arguments.length > 0) {
			message = MessageFormat.format(message, arguments);
		}
		ErrorVO errorVO = new ErrorVO();
		errorVO.setErrorCode(code);
		errorVO.setErrorMessage(message);
		throw new TaxiServiceException(message, errorVO);
	}
}
