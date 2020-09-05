package com.fueber.taxi.service.impl;

import java.text.MessageFormat;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.Properties;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.fueber.taxi.common.Constants;
import com.fueber.taxi.dto.CustomerDTO;
import com.fueber.taxi.dto.TaxiDTO;
import com.fueber.taxi.enums.RideStatus;
import com.fueber.taxi.exception.TaxiServiceException;
import com.fueber.taxi.service.TaxiBookingService;
import com.fueber.taxi.service.TaxiBookingValidationService;
import com.fueber.taxi.util.FueberTaxiUtils;
import com.fueber.taxi.vo.CustomerVO;
import com.fueber.taxi.vo.ErrorVO;

@Service
public class TaxiBookingServiceImpl implements TaxiBookingService {
	
	@Autowired
	private TaxiBookingValidationService taxiBookingValidationService;
	
	@Autowired
	private List<TaxiDTO> availableTaxiList;
	
	@Autowired
	private List<CustomerDTO> bookedCustomerList;
	
	@Value("${fueber.taxi.base.kilometerLimit}")
	private String baseKilometerLimit;
	
	@Autowired
	private Properties errorProperties;
	
	@Value("${fueber.taxi.base.ratePerMinute}")
	private String baseRatePerMinute;
	
	@Value("${fueber.taxi.base.ratePerKilometer}")
	private String baseRatePerKilometer;

	@Override
	public CustomerVO bookRide(CustomerVO customerVO) throws TaxiServiceException {
		taxiBookingValidationService.validateCustomerOnRideAlready(customerVO.getMobileNumber(), bookedCustomerList);
		taxiBookingValidationService.validateCustomerPickupLocation(customerVO.getPickupLatitude(), customerVO.getPickupLongitude());
		taxiBookingValidationService.validateCustomerDropLocation(customerVO.getDropLatitude(), customerVO.getDropLongitude());
		TaxiDTO nearByTaxiDTO = getNearestTaxi(customerVO.getPickupLatitude(), customerVO.getPickupLongitude(), customerVO.isPinkTaxi());
		if(nearByTaxiDTO != null) {
			updateTaxiDTO(nearByTaxiDTO.getTaxiID(), true);
			CustomerDTO customerDTO = getCustomerDTOByVO(customerVO);
			customerDTO.setTaxiID(nearByTaxiDTO.getTaxiID());
			customerDTO.setStatus(RideStatus.BOOKED.getStatus());
			customerDTO.setBookingId(UUID.randomUUID().toString());
			bookedCustomerList.add(customerDTO);
			return getCustomerVOByDTO(customerDTO);
		} else {
			String message = errorProperties.getProperty(Constants.TAXI_NOT_AVAILABLE_AT_PICKUP_LOCATION);
			ErrorVO errorVO = new ErrorVO();
			errorVO.setErrorCode(Constants.TAXI_NOT_AVAILABLE_AT_PICKUP_LOCATION);
			errorVO.setErrorMessage(message);
			throw new TaxiServiceException(message, errorVO);
		}
	}
	
	@Override
	public CustomerVO updateRide(CustomerVO customerVO, String bookingId) throws TaxiServiceException {
		CustomerDTO customerDTO = getCustomerDTOByBookingId(bookingId);
		taxiBookingValidationService.validateRideStatus(customerVO.getRideStatus());
		if(customerVO.getRideStatus().equalsIgnoreCase(customerDTO.getStatus())) {
			throwTaxiServiceException(Constants.DUPLICATE_RIDE_STATUS, customerVO.getRideStatus());
		} 
		if(RideStatus.STARTED.getStatus().equalsIgnoreCase(customerVO.getRideStatus())) {
			taxiBookingValidationService.validateStartRide(customerVO.getRideStatus(), customerDTO.getStatus());
			customerDTO.setStartTime(LocalDateTime.now());
		} else if(RideStatus.CANCELED.getStatus().equalsIgnoreCase(customerVO.getRideStatus())) {
			taxiBookingValidationService.validateCancelRide(customerVO.getRideStatus(), customerDTO.getStatus());
			updateTaxiDTO(customerDTO.getTaxiID(), false);
		} else if(RideStatus.COMPLETED.getStatus().equalsIgnoreCase(customerVO.getRideStatus())) {
			taxiBookingValidationService.validateCompleteRide(customerVO.getRideStatus(), customerDTO.getStatus());
			updateTaxiDTO(customerDTO.getTaxiID(), false);
			customerDTO.setEndTime(LocalDateTime.now());
			int rideInMinutes = (int)Duration.between(customerDTO.getEndTime(), customerDTO.getStartTime()).toMinutes();
			int rideCharges = (rideInMinutes * Integer.parseInt(baseRatePerMinute)) + (customerDTO.getDistance() * Integer.parseInt(baseRatePerKilometer));
			rideCharges += customerDTO.isPinkTaxi() ? 5 : 0;
			customerDTO.setRideCharges(rideCharges);
		}
		customerDTO.setStatus(customerVO.getRideStatus());
		updateOnRideCustomerList(customerDTO);
		return getCustomerVOByDTO(customerDTO);
	}
	
	
	/**Method to update the customer booking details based on status*/
	private void updateOnRideCustomerList(CustomerDTO customerDTO) {
		for(CustomerDTO onRideCustomerDTO : bookedCustomerList) {
			if(customerDTO.getBookingId().equals(onRideCustomerDTO.getBookingId())) {
				onRideCustomerDTO.setStartTime(customerDTO.getStartTime());
				onRideCustomerDTO.setEndTime(customerDTO.getEndTime());
				onRideCustomerDTO.setStatus(customerDTO.getStatus());
				onRideCustomerDTO.setRideCharges(customerDTO.getRideCharges());
				break;
			}
		}
	}
	
	/**Method to get the nearest taxi by customer pickup location*/
	private TaxiDTO getNearestTaxi(double pickupLatitude, double pickLongitude, boolean isPinkTaxi) {
		TaxiDTO nearByTaxiDTO = null;
		int nearestTaxiDistance = Integer.parseInt(baseKilometerLimit) + 1; //Search is within base kilometer + 1
		for(TaxiDTO taxiDTO : availableTaxiList) {
			int distanceBetweenTwoPoints = FueberTaxiUtils.getDistanceBetweenPoints(pickupLatitude, pickLongitude, taxiDTO.getLatitude(), taxiDTO.getLongitude());
			if(distanceBetweenTwoPoints < nearestTaxiDistance && !taxiDTO.isAssignedToCustomer() && String.valueOf(isPinkTaxi).equalsIgnoreCase(String.valueOf(taxiDTO.isPinkTaxi()))) {
				nearByTaxiDTO = taxiDTO;
				nearestTaxiDistance = distanceBetweenTwoPoints;
			}
		}
		return nearByTaxiDTO;
	}
	
	/**Method to update the taxi by assigning to customer or free the taxi*/
	private void updateTaxiDTO(String taxiID, boolean isAssignedToCustomer) {
		for(TaxiDTO availableTaxiDTO : availableTaxiList) {
			if(availableTaxiDTO.getTaxiID().equalsIgnoreCase(taxiID)) {
				availableTaxiDTO.setAssignedToCustomer(isAssignedToCustomer);
			}
		}
	}
	
	/**Method to get the transfer object by value object*/
	private CustomerDTO getCustomerDTOByVO(CustomerVO customerVO) {
		CustomerDTO customerDTO = new CustomerDTO();
		customerDTO.setCustomerMobileNumber(customerVO.getMobileNumber());
		customerDTO.setDropLatitude(customerVO.getDropLatitude());
		customerDTO.setDropLongitude(customerVO.getDropLongitude());
		customerDTO.setPickupLatitude(customerVO.getPickupLatitude());
		customerDTO.setPickupLongitude(customerVO.getPickupLongitude());
		customerDTO.setPinkTaxi(customerVO.isPinkTaxi());
		int distanceToBeCovered = FueberTaxiUtils.getDistanceBetweenPoints(customerVO.getPickupLatitude(), customerVO.getPickupLongitude(), customerVO.getDropLatitude(), customerVO.getDropLongitude());
		customerDTO.setDistance(distanceToBeCovered);
		return customerDTO;
	}
	
	/**Method to get the value object by transfer object*/
	private CustomerVO getCustomerVOByDTO(CustomerDTO customerDTO) {
		CustomerVO customerVO = new CustomerVO();
		customerVO.setDropLatitude(customerDTO.getDropLatitude());
		customerVO.setDropLongitude(customerDTO.getDropLongitude());
		customerVO.setMobileNumber(customerDTO.getCustomerMobileNumber());
		customerVO.setPickupLatitude(customerDTO.getPickupLatitude());
		customerVO.setPickupLongitude(customerDTO.getPickupLongitude());
		customerVO.setPinkTaxi(customerDTO.isPinkTaxi());
		customerVO.setTaxiId(customerDTO.getTaxiID());
		customerVO.setDistance(customerDTO.getDistance());
		customerVO.setRideStatus(customerDTO.getStatus());
		customerVO.setBookingId(customerDTO.getBookingId());
		if(customerDTO.getStartTime() != null) {
			customerVO.setStartTime(FueberTaxiUtils.formatDateToString(customerDTO.getStartTime()));
		}
		if(customerDTO.getEndTime() != null) {
			customerVO.setEndTime(FueberTaxiUtils.formatDateToString(customerDTO.getEndTime()));
		}
		customerVO.setRideCharges(customerDTO.getRideCharges());
		return customerVO;
	}
	
	/**Method to get the customer and booking details by booking id*/
	private CustomerDTO getCustomerDTOByBookingId(String bookingId) throws TaxiServiceException {
		Optional<CustomerDTO> optionalCustomerDTO = bookedCustomerList.stream().filter(customerDTO -> customerDTO.getBookingId().equals(bookingId)).findAny();
		if(!optionalCustomerDTO.isPresent()) {
			throwTaxiServiceException(Constants.BOOKING_ID_DOES_NOT_EXISTS, bookingId);
		} 
		return optionalCustomerDTO.get();
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
