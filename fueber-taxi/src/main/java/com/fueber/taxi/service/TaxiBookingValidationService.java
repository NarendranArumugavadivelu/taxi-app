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
}
