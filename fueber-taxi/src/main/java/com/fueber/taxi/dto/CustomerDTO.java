package com.fueber.taxi.dto;

import java.time.LocalDateTime;

public class CustomerDTO {

	private String customerMobileNumber;
	
	private Double pickupLatitude;
	
	private Double pickupLongitude;
	
	private Double dropLatitude;
	
	private Double dropLongitude;
	
	private String taxiID;
	
	private boolean isPinkTaxi;
	
	private int distanceInKms;
	
	private LocalDateTime startTime;
	
	private LocalDateTime endTime;

	public String getCustomerMobileNumber() {
		return customerMobileNumber;
	}

	public void setCustomerMobileNumber(String customerMobileNumber) {
		this.customerMobileNumber = customerMobileNumber;
	}

	public Double getPickupLatitude() {
		return pickupLatitude;
	}

	public void setPickupLatitude(Double pickupLatitude) {
		this.pickupLatitude = pickupLatitude;
	}

	public Double getPickupLongitude() {
		return pickupLongitude;
	}

	public void setPickupLongitude(Double pickupLongitude) {
		this.pickupLongitude = pickupLongitude;
	}

	public Double getDropLatitude() {
		return dropLatitude;
	}

	public void setDropLatitude(Double dropLatitude) {
		this.dropLatitude = dropLatitude;
	}

	public Double getDropLongitude() {
		return dropLongitude;
	}

	public void setDropLongitude(Double dropLongitude) {
		this.dropLongitude = dropLongitude;
	}

	public String getTaxiID() {
		return taxiID;
	}

	public void setTaxiID(String taxiID) {
		this.taxiID = taxiID;
	}

	public boolean isPinkTaxi() {
		return isPinkTaxi;
	}

	public void setPinkTaxi(boolean isPinkTaxi) {
		this.isPinkTaxi = isPinkTaxi;
	}

	public int getDistanceInKms() {
		return distanceInKms;
	}

	public void setDistanceInKms(int distanceInKms) {
		this.distanceInKms = distanceInKms;
	}

	public LocalDateTime getStartTime() {
		return startTime;
	}

	public void setStartTime(LocalDateTime startTime) {
		this.startTime = startTime;
	}

	public LocalDateTime getEndTime() {
		return endTime;
	}

	public void setEndTime(LocalDateTime endTime) {
		this.endTime = endTime;
	}

	@Override
	public String toString() {
		return "CustomerDTO [customerMobileNumber=" + customerMobileNumber + ", pickupLatitude=" + pickupLatitude
				+ ", pickupLongitude=" + pickupLongitude + ", dropLatitude=" + dropLatitude + ", dropLongitude="
				+ dropLongitude + ", taxiID=" + taxiID + ", isPinkTaxi=" + isPinkTaxi + ", distanceInKms="
				+ distanceInKms + ", startTime=" + startTime + ", endTime=" + endTime + "]";
	}

}
