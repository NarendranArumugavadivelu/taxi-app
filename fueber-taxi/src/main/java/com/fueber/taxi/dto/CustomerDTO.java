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
	
	private int distance;
	
	private LocalDateTime startTime;
	
	private LocalDateTime endTime;
	
	private String status;
	
	private String bookingId;
	
	private int rideCharges;

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

	public int getDistance() {
		return distance;
	}

	public void setDistance(int distance) {
		this.distance = distance;
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
	
	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}
	
	public String getBookingId() {
		return bookingId;
	}

	public void setBookingId(String bookingId) {
		this.bookingId = bookingId;
	}
	
	public int getRideCharges() {
		return rideCharges;
	}

	public void setRideCharges(int rideCharges) {
		this.rideCharges = rideCharges;
	}

	@Override
	public String toString() {
		return "CustomerDTO [customerMobileNumber=" + customerMobileNumber + ", pickupLatitude=" + pickupLatitude
				+ ", pickupLongitude=" + pickupLongitude + ", dropLatitude=" + dropLatitude + ", dropLongitude="
				+ dropLongitude + ", taxiID=" + taxiID + ", isPinkTaxi=" + isPinkTaxi + ", distance=" + distance
				+ ", startTime=" + startTime + ", endTime=" + endTime + ", status=" + status + ", bookingId="
				+ bookingId + "]";
	}

}
