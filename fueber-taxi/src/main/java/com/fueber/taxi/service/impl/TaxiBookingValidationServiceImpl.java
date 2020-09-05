package com.fueber.taxi.service.impl;

import java.text.MessageFormat;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.Properties;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.fueber.taxi.common.Constants;
import com.fueber.taxi.dto.CustomerDTO;
import com.fueber.taxi.enums.RideStatus;
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
	
	private Properties errorProperties;
	
	public TaxiBookingValidationServiceImpl(Properties errorProperties) {
		this.errorProperties = errorProperties;
	}
	
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
	
	@Override
	public void validateRideStatus(String rideStatus) throws TaxiServiceException {
		if(Arrays.stream(RideStatus.values()).noneMatch(x -> x.getStatus().equalsIgnoreCase(rideStatus))) {
			throwTaxiServiceException(Constants.INVALID_RIDE_STATUS, rideStatus);
		}
	}
	
	@Override
	public void validateCustomerOnRideAlready(String customerMobileNumber, List<CustomerDTO> onRideCustomerList) throws TaxiServiceException {
		Optional<CustomerDTO> optionalCustomerDTO = onRideCustomerList.stream().filter(customerDTO -> customerDTO.getCustomerMobileNumber().equals(customerMobileNumber)).findAny();
		if(optionalCustomerDTO.isPresent() && (optionalCustomerDTO.get().getStatus().equalsIgnoreCase(RideStatus.STARTED.getStatus()) || optionalCustomerDTO.get().getStatus().equalsIgnoreCase(RideStatus.BOOKED.getStatus()))) {
			throwTaxiServiceException(Constants.CUSTOMER_ALREADY_ON_RIDE, customerMobileNumber);
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

	@Override
	public void validateStartRide(String status, String currentStatus) throws TaxiServiceException {
		if(currentStatus.equalsIgnoreCase(RideStatus.COMPLETED.getStatus()) || currentStatus.equalsIgnoreCase(RideStatus.CANCELED.getStatus())) {
			throwTaxiServiceException(Constants.RIDE_STATUS_CANNOT_BE_UPDATED, status, currentStatus);
		}
	}

	@Override
	public void validateCancelRide(String status, String currentStatus) throws TaxiServiceException {
		if(RideStatus.STARTED.getStatus().equalsIgnoreCase(currentStatus)) {
			throwTaxiServiceException(Constants.RIDE_ALREADY_STARTED);
		} else if(currentStatus.equalsIgnoreCase(RideStatus.COMPLETED.getStatus())) {
			throwTaxiServiceException(Constants.RIDE_STATUS_CANNOT_BE_UPDATED, status, currentStatus);
		}
	}

	@Override
	public void validateCompleteRide(String status, String currentStatus) throws TaxiServiceException {
		if(!RideStatus.STARTED.getStatus().equalsIgnoreCase(currentStatus)) {
			throwTaxiServiceException(Constants.RIDE_NOT_STARTED_YET);
		} else if(currentStatus.equalsIgnoreCase(RideStatus.CANCELED.getStatus())) {
			throwTaxiServiceException(Constants.RIDE_STATUS_CANNOT_BE_UPDATED, status, currentStatus);
		} 
	}
}
