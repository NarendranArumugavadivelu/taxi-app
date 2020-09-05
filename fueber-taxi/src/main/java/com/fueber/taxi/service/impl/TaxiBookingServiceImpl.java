package com.fueber.taxi.service.impl;

import java.util.List;
import java.util.Properties;

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
	private List<CustomerDTO> onRideCustomerList;
	
	@Value("${fueber.taxi.base.kilometerLimit}")
	private String baseKilometerLimit;
	
	@Autowired
	private Properties errorProperties;

	@Override
	public CustomerVO bookTaxi(CustomerVO customerVO) throws TaxiServiceException {
		taxiBookingValidationService.validateCustomerOnRideAlready(customerVO.getMobileNumber(), onRideCustomerList);
		taxiBookingValidationService.validateCustomerPickupLocation(customerVO.getPickupLatitude(), customerVO.getPickupLongitude());
		taxiBookingValidationService.validateCustomerDropLocation(customerVO.getDropLatitude(), customerVO.getDropLongitude());
		TaxiDTO nearByTaxiDTO = getNearestTaxi(customerVO.getPickupLatitude(), customerVO.getPickupLongitude());
		if(nearByTaxiDTO != null) {
			updateTaxiDTO(nearByTaxiDTO, true);
			CustomerDTO customerDTO = getCustomerDTOByVO(customerVO);
			customerDTO.setTaxiID(nearByTaxiDTO.getTaxiID());
			customerDTO.setStatus(RideStatus.BOOKED.getStatus());
			onRideCustomerList.add(customerDTO);
			return getCustomerVOByDTO(customerDTO);
		} else {
			String message = errorProperties.getProperty(Constants.TAXI_NOT_AVAILABLE_AT_PICKUP_LOCATION);
			ErrorVO errorVO = new ErrorVO();
			errorVO.setErrorCode(Constants.TAXI_NOT_AVAILABLE_AT_PICKUP_LOCATION);
			errorVO.setErrorMessage(message);
			throw new TaxiServiceException(message, errorVO);
		}
	}
	
	/**Method to get the nearest taxi by customer pickup location*/
	private TaxiDTO getNearestTaxi(double pickupLatitude, double pickLongitude) {
		TaxiDTO nearByTaxiDTO = null;
		int nearestTaxiDistance = Integer.parseInt(baseKilometerLimit) + 1; //Search is within base kilometer + 1
		for(TaxiDTO taxiDTO : availableTaxiList) {
			int distanceBetweenTwoPoints = FueberTaxiUtils.getDistanceBetweenPoints(pickupLatitude, pickLongitude, taxiDTO.getLatitude(), taxiDTO.getLongitude());
			if(distanceBetweenTwoPoints < nearestTaxiDistance && !taxiDTO.isAssignedToCustomer()) {
				nearByTaxiDTO = taxiDTO;
				nearestTaxiDistance = distanceBetweenTwoPoints;
			}
		}
		return nearByTaxiDTO;
	}
	
	/**Method to update the taxi by assigning to customer or free the taxi*/
	private void updateTaxiDTO(TaxiDTO taxiDTO, boolean isAssignedToCustomer) {
		for(TaxiDTO availableTaxiDTO : availableTaxiList) {
			if(availableTaxiDTO.getTaxiID().equalsIgnoreCase(taxiDTO.getTaxiID())) {
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
		return customerVO;
	}
}
