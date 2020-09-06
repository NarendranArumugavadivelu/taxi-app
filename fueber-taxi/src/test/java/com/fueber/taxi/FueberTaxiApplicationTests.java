package com.fueber.taxi;

import static org.junit.jupiter.api.Assertions.assertThrows;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.UUID;

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
	
	private List<TaxiDTO> availableTaxiList;
	
	private List<CustomerDTO> bookedCustomerList;
	
	private Properties errorProperties;
	
	private TaxiBookingValidationServiceImpl taxiBookingValidaitonServiceimpl; 
	
	private TaxiBookingServiceImpl taxiBookingServiceimpl;
	
	private CustomerVO customerVO = null;
	
	private CustomerDTO customerDTO = null;

	@BeforeEach
	public void initializeMockito() { 
		MockitoAnnotations.initMocks(this);
		bookedCustomerList = new ArrayList<>();
		availableTaxiList = new ArrayList<>();
		TaxiDTO taxiDTO = new TaxiDTO("KA04MG4516", 12.979270, 77.571680, false, false);
		availableTaxiList.add(taxiDTO);
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
		
		customerVO = new CustomerVO();
		customerVO.setMobileNumber("9629009410");
		customerVO.setPickupLatitude(12.9789);
		customerVO.setPickupLongitude(77.5710);
		customerVO.setDropLatitude(12.9513);
		customerVO.setDropLongitude(77.5757);
		
		customerDTO = new CustomerDTO();
		customerDTO.setCustomerMobileNumber("9629009410");
		customerDTO.setStatus(RideStatus.BOOKED.getStatus());
		customerDTO.setBookingId(UUID.randomUUID().toString());
		customerDTO.setTaxiID(taxiDTO.getTaxiID());
		customerDTO.setPickupLatitude(customerVO.getPickupLatitude());
		customerDTO.setPickupLongitude(customerVO.getPickupLongitude());
		customerDTO.setDropLatitude(customerVO.getDropLatitude());
		customerDTO.setDropLongitude(customerVO.getDropLongitude());
	}
	
	@Test
	public void book_taxi_succeed() throws TaxiServiceException {
		CustomerVO savedCustomerVO = taxiBookingServiceimpl.bookRide(customerVO);
		Assert.assertNotNull(savedCustomerVO.getBookingId());
		
	}
	
	@Test
	public void book_taxi_failed_pickup_not_available() {
		customerVO.setPickupLatitude(2.9789);
		customerVO.setPickupLongitude(77.5710);
		TaxiServiceException taxiServiceException = assertThrows(TaxiServiceException.class, () -> {
			taxiBookingServiceimpl.bookRide(customerVO);
		});
		Assert.assertEquals(Constants.SERVICE_UNAVAILABLE_AT_PICKUP_LOCATION, taxiServiceException.getErrorVO().getErrorCode());
	}
	
	@Test
	public void book_taxi_failed_drop_not_available() {
		customerVO.setDropLatitude(2.9513);
		customerVO.setDropLongitude(77.5757);
		TaxiServiceException taxiServiceException = assertThrows(TaxiServiceException.class, () -> {
			taxiBookingServiceimpl.bookRide(customerVO);
		});
		Assert.assertEquals(Constants.SERVICE_UNAVAILABLE_AT_DROP_LOCATION, taxiServiceException.getErrorVO().getErrorCode());
	}
	
	@Test
	public void book_taxi_failed_booked_ride_already_exists() {
		bookedCustomerList.add(customerDTO);
		TaxiServiceException taxiServiceException = assertThrows(TaxiServiceException.class, () -> {
			taxiBookingServiceimpl.bookRide(customerVO);
		});
		Assert.assertEquals(Constants.CUSTOMER_ALREADY_ON_RIDE, taxiServiceException.getErrorVO().getErrorCode());
	}
	
	@Test
	public void book_taxi_failed_started_ride_already_exists() {
		bookedCustomerList.add(customerDTO);
		TaxiServiceException taxiServiceException = assertThrows(TaxiServiceException.class, () -> {
			taxiBookingServiceimpl.bookRide(customerVO);
		});
		Assert.assertEquals(Constants.CUSTOMER_ALREADY_ON_RIDE, taxiServiceException.getErrorVO().getErrorCode());
	}
	
	@Test
	public void book_taxi_failed_no_taxi_exists() {
		TaxiDTO taxiDTO = availableTaxiList.get(0);
		taxiDTO.setAssignedToCustomer(true);
		TaxiServiceException taxiServiceException = assertThrows(TaxiServiceException.class, () -> {
			taxiBookingServiceimpl.bookRide(customerVO);
		});
		Assert.assertEquals(Constants.TAXI_NOT_AVAILABLE_AT_PICKUP_LOCATION, taxiServiceException.getErrorVO().getErrorCode());
	}
	
	@Test
	public void update_ride_booking_id_not_exists() {
		String bookingId = UUID.randomUUID().toString();
		customerVO.setBookingId(bookingId);
		TaxiServiceException taxiServiceException = assertThrows(TaxiServiceException.class, () -> {
			taxiBookingServiceimpl.updateRide(customerVO, bookingId);
		});
		Assert.assertEquals(Constants.BOOKING_ID_DOES_NOT_EXISTS, taxiServiceException.getErrorVO().getErrorCode());
	}
	
	@Test
	public void update_ride_duplicate_status() {
		customerDTO.setStatus(RideStatus.BOOKED.getStatus());
		bookedCustomerList.add(customerDTO);
		customerVO.setBookingId(customerDTO.getBookingId());
		customerVO.setRideStatus(RideStatus.BOOKED.getStatus());
		TaxiServiceException taxiServiceException = assertThrows(TaxiServiceException.class, () -> {
			taxiBookingServiceimpl.updateRide(customerVO, customerVO.getBookingId());
		});
		Assert.assertEquals(Constants.DUPLICATE_RIDE_STATUS, taxiServiceException.getErrorVO().getErrorCode());
	}
	
	@Test
	public void start_ride_failed_ride_already_completed() {
		customerDTO.setStatus(RideStatus.COMPLETED.getStatus());
		bookedCustomerList.add(customerDTO);
		customerVO.setBookingId(customerDTO.getBookingId());
		customerVO.setRideStatus(RideStatus.STARTED.getStatus());
		TaxiServiceException taxiServiceException = assertThrows(TaxiServiceException.class, () -> {
			taxiBookingServiceimpl.updateRide(customerVO, customerVO.getBookingId());
		});
		Assert.assertEquals(Constants.RIDE_STATUS_CANNOT_BE_UPDATED, taxiServiceException.getErrorVO().getErrorCode());
	}
	
	@Test
	public void start_ride_failed_ride_already_canceled() {
		customerDTO.setStatus(RideStatus.CANCELED.getStatus());
		bookedCustomerList.add(customerDTO);
		customerVO.setBookingId(customerDTO.getBookingId());
		customerVO.setRideStatus(RideStatus.STARTED.getStatus());
		TaxiServiceException taxiServiceException = assertThrows(TaxiServiceException.class, () -> {
			taxiBookingServiceimpl.updateRide(customerVO, customerVO.getBookingId());
		});
		Assert.assertEquals(Constants.RIDE_STATUS_CANNOT_BE_UPDATED, taxiServiceException.getErrorVO().getErrorCode());
	}
	
	@Test
	public void start_ride_succeed() throws TaxiServiceException {
		bookedCustomerList.add(customerDTO);
		customerVO.setBookingId(customerDTO.getBookingId());
		customerVO.setRideStatus(RideStatus.STARTED.getStatus());
		CustomerVO updatedCustomerVO = taxiBookingServiceimpl.updateRide(customerVO, customerVO.getBookingId());
		Assert.assertEquals(customerVO.getRideStatus(), updatedCustomerVO.getRideStatus());
	}
	
	@Test
	public void cancel_ride_failed_ride_already_started() throws TaxiServiceException {
		customerDTO.setStatus(RideStatus.STARTED.getStatus());
		bookedCustomerList.add(customerDTO);
		customerVO.setBookingId(customerDTO.getBookingId());
		customerVO.setRideStatus(RideStatus.CANCELED.getStatus());
		TaxiServiceException taxiServiceException = assertThrows(TaxiServiceException.class, () -> {
			taxiBookingServiceimpl.updateRide(customerVO, customerVO.getBookingId());
		});
		Assert.assertEquals(Constants.RIDE_ALREADY_STARTED, taxiServiceException.getErrorVO().getErrorCode());
	}
	
	@Test
	public void cancel_ride_failed_ride_already_completed() throws TaxiServiceException {
		customerDTO.setStatus(RideStatus.COMPLETED.getStatus());
		bookedCustomerList.add(customerDTO);
		customerVO.setBookingId(customerDTO.getBookingId());
		customerVO.setRideStatus(RideStatus.CANCELED.getStatus());
		TaxiServiceException taxiServiceException = assertThrows(TaxiServiceException.class, () -> {
			taxiBookingServiceimpl.updateRide(customerVO, customerVO.getBookingId());
		});
		Assert.assertEquals(Constants.RIDE_STATUS_CANNOT_BE_UPDATED, taxiServiceException.getErrorVO().getErrorCode());
	}
	
	@Test
	public void cancel_ride_succeed() throws TaxiServiceException {
		bookedCustomerList.add(customerDTO);
		customerVO.setBookingId(customerDTO.getBookingId());
		customerVO.setRideStatus(RideStatus.CANCELED.getStatus());
		CustomerVO updatedCustomerVO = taxiBookingServiceimpl.updateRide(customerVO, customerVO.getBookingId());
		Assert.assertEquals(customerVO.getRideStatus(), updatedCustomerVO.getRideStatus());
	}
	
	@Test
	public void complete_ride_failed_ride_not_started() throws TaxiServiceException {
		bookedCustomerList.add(customerDTO);
		customerVO.setBookingId(customerDTO.getBookingId());
		customerVO.setRideStatus(RideStatus.COMPLETED.getStatus());
		TaxiServiceException taxiServiceException = assertThrows(TaxiServiceException.class, () -> {
			taxiBookingServiceimpl.updateRide(customerVO, customerVO.getBookingId());
		});
		Assert.assertEquals(Constants.RIDE_NOT_STARTED_YET, taxiServiceException.getErrorVO().getErrorCode());
	}
	
	@Test
	public void complete_ride_failed_ride_already_canceled() throws TaxiServiceException {
		customerDTO.setStatus(RideStatus.CANCELED.getStatus());
		bookedCustomerList.add(customerDTO);
		customerVO.setBookingId(customerDTO.getBookingId());
		customerVO.setRideStatus(RideStatus.COMPLETED.getStatus());
		TaxiServiceException taxiServiceException = assertThrows(TaxiServiceException.class, () -> {
			taxiBookingServiceimpl.updateRide(customerVO, customerVO.getBookingId());
		});
		Assert.assertEquals(Constants.RIDE_STATUS_CANNOT_BE_UPDATED, taxiServiceException.getErrorVO().getErrorCode());
	}
	
	@Test
	public void complete_ride_suceed() throws TaxiServiceException {
		customerDTO.setStatus(RideStatus.STARTED.getStatus());
		customerDTO.setStartTime(LocalDateTime.now());
		customerDTO.setEndTime(LocalDateTime.now().plusMinutes(5));
		bookedCustomerList.add(customerDTO);
		customerVO.setBookingId(customerDTO.getBookingId());
		customerVO.setRideStatus(RideStatus.COMPLETED.getStatus());
		CustomerVO updatedCustomerVO = taxiBookingServiceimpl.updateRide(customerVO, customerVO.getBookingId());
		Assert.assertEquals(customerVO.getRideStatus(), updatedCustomerVO.getRideStatus());
	}
}
