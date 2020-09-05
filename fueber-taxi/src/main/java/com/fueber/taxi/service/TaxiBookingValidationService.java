package com.fueber.taxi.service;

import org.springframework.stereotype.Service;

import com.fueber.taxi.exception.TaxiServiceException;

@Service
public interface TaxiBookingValidationService {

	public void validateCustomerPickupLocation(double latitude, double longitude) throws TaxiServiceException;
	
	public void validateCustomerDropLocation(double latitude, double longitude) throws TaxiServiceException;
}
