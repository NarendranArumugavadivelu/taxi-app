package com.fueber.taxi.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.fueber.taxi.dto.CustomerDTO;
import com.fueber.taxi.exception.TaxiServiceException;

@Service
public interface TaxiBookingValidationService {

	public void validateCustomerPickupLocation(double latitude, double longitude) throws TaxiServiceException;
	
	public void validateCustomerDropLocation(double latitude, double longitude) throws TaxiServiceException;
	
	public void validateCustomerOnRideAlready(String customerMobileNumber, List<CustomerDTO> onRideCustomerList) throws TaxiServiceException;
	
	public void validateRideStatus(String rideStatus) throws TaxiServiceException;
	
	public void validateStartRide(String status, String currentStatus) throws TaxiServiceException;
	
	public void validateCancelRide(String status, String currentStatus) throws TaxiServiceException;
	
	public void validateCompleteRide(String status, String currentStatus) throws TaxiServiceException;
}
