package com.fueber.taxi;

import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import org.junit.Assert;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.test.util.ReflectionTestUtils;

import com.fueber.taxi.common.Constants;
import com.fueber.taxi.dto.CustomerDTO;
import com.fueber.taxi.dto.TaxiDTO;
import com.fueber.taxi.enums.RideStatus;
import com.fueber.taxi.exception.TaxiServiceException;
import com.fueber.taxi.service.impl.TaxiBookingServiceImpl;
import com.fueber.taxi.service.impl.TaxiBookingValidationServiceImpl;
import com.fueber.taxi.util.FueberTaxiUtils;
import com.fueber.taxi.vo.CustomerVO;

@RunWith(MockitoJUnitRunner.class)
class FueberTaxiApplicationTests {
	
	private List<TaxiDTO> availableTaxiList = new ArrayList<>();
	
	private List<CustomerDTO> bookedCustomerList = new ArrayList<>();
	
	private Properties errorProperties;
	
	private TaxiBookingValidationServiceImpl taxiBookingValidaitonServiceimpl = null; 
	
	private TaxiBookingServiceImpl taxiBookingServiceimpl = null;

	@BeforeEach
	public void initializeMockito() { 
		MockitoAnnotations.initMocks(this);
		availableTaxiList.add(new TaxiDTO("KA04MG4516", 12.979270, 77.571680, false, false));
		if(errorProperties == null) {
			errorProperties = FueberTaxiUtils.loadProperties("error-messages.properties");
		}
		taxiBookingValidaitonServiceimpl = new TaxiBookingValidationServiceImpl(errorProperties);
		ReflectionTestUtils.setField(taxiBookingValidaitonServiceimpl, "baseLatitude", "12.976143");
		ReflectionTestUtils.setField(taxiBookingValidaitonServiceimpl, "baseLongitude", "77.570541");
		ReflectionTestUtils.setField(taxiBookingValidaitonServiceimpl, "baseKilometerLimit", "25");
		
		taxiBookingServiceimpl = new TaxiBookingServiceImpl(taxiBookingValidaitonServiceimpl, availableTaxiList, bookedCustomerList, errorProperties);
		ReflectionTestUtils.setField(taxiBookingServiceimpl, "baseRatePerMinute", "1");
		ReflectionTestUtils.setField(taxiBookingServiceimpl, "baseRatePerKilometer", "2");
		ReflectionTestUtils.setField(taxiBookingServiceimpl, "baseKilometerLimit", "25");
		ReflectionTestUtils.setField(taxiBookingServiceimpl, "pinkTaxiCharge", "5");
	}
	
	@Test
	public void book_taxi_succeed() throws TaxiServiceException {
		CustomerVO customerVO = new CustomerVO();
		customerVO.setMobileNumber("9629009410");
		customerVO.setPickupLatitude(12.9789);
		customerVO.setPickupLongitude(77.5710);
		customerVO.setDropLatitude(12.9513);
		customerVO.setDropLongitude(77.5757);
		customerVO = taxiBookingServiceimpl.bookRide(customerVO);
		Assert.assertNotNull(customerVO.getBookingId());
		
	}
	
	@Test
	public void book_taxi_failed_pickup_not_available() {
		CustomerVO customerVO = new CustomerVO();
		customerVO.setMobileNumber("9629009410");
		customerVO.setPickupLatitude(2.9789);
		customerVO.setPickupLongitude(77.5710);
		customerVO.setDropLatitude(12.9513);
		customerVO.setDropLongitude(77.5757);
		TaxiServiceException taxiServiceException = assertThrows(TaxiServiceException.class, () -> {
			taxiBookingServiceimpl.bookRide(customerVO);
		});
		Assert.assertEquals(Constants.SERVICE_UNAVAILABLE_AT_PICKUP_LOCATION, taxiServiceException.getErrorVO().getErrorCode());
	}
	
	@Test
	public void book_taxi_failed_drop_not_available() {
		CustomerVO customerVO = new CustomerVO();
		customerVO.setMobileNumber("9629009410");
		customerVO.setPickupLatitude(12.9789);
		customerVO.setPickupLongitude(77.5710);
		customerVO.setDropLatitude(2.9513);
		customerVO.setDropLongitude(77.5757);
		TaxiServiceException taxiServiceException = assertThrows(TaxiServiceException.class, () -> {
			taxiBookingServiceimpl.bookRide(customerVO);
		});
		Assert.assertEquals(Constants.SERVICE_UNAVAILABLE_AT_DROP_LOCATION, taxiServiceException.getErrorVO().getErrorCode());
	}
	
	@Test
	public void book_taxi_failed_ride_already_exists() {
		CustomerDTO customerDTO = new CustomerDTO();
		customerDTO.setCustomerMobileNumber("9629009410");
		customerDTO.setStatus(RideStatus.BOOKED.getStatus());
		bookedCustomerList.add(customerDTO);
		CustomerVO customerVO = new CustomerVO();
		customerVO.setMobileNumber("9629009410");
		customerVO.setPickupLatitude(12.9789);
		customerVO.setPickupLongitude(77.5710);
		customerVO.setDropLatitude(2.9513);
		customerVO.setDropLongitude(77.5757);
		TaxiServiceException taxiServiceException = assertThrows(TaxiServiceException.class, () -> {
			taxiBookingServiceimpl.bookRide(customerVO);
		});
		Assert.assertEquals(Constants.CUSTOMER_ALREADY_ON_RIDE, taxiServiceException.getErrorVO().getErrorCode());
	}
	
	@Test
	public void book_taxi_failed_no_taxi_exists() {
		CustomerVO customerVO = new CustomerVO();
		customerVO.setMobileNumber("9629009410");
		customerVO.setPickupLatitude(12.9789);
		customerVO.setPickupLongitude(77.5710);
		customerVO.setDropLatitude(12.9513);
		customerVO.setDropLongitude(77.5757);
		customerVO.setPinkTaxi(true);
		TaxiServiceException taxiServiceException = assertThrows(TaxiServiceException.class, () -> {
			taxiBookingServiceimpl.bookRide(customerVO);
		});
		Assert.assertEquals(Constants.TAXI_NOT_AVAILABLE_AT_PICKUP_LOCATION, taxiServiceException.getErrorVO().getErrorCode());
		
	}
}
