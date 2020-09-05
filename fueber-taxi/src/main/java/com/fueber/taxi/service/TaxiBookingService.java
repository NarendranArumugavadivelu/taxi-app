package com.fueber.taxi.service;

import org.springframework.stereotype.Service;

import com.fueber.taxi.exception.TaxiServiceException;
import com.fueber.taxi.vo.CustomerVO;

@Service
public interface TaxiBookingService {

	public CustomerVO bookTaxi(CustomerVO customerVO) throws TaxiServiceException;
}
