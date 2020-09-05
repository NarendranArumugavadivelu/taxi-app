package com.fueber.taxi.vo;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import io.swagger.v3.oas.annotations.media.Schema;

@JsonSerialize
@JsonIgnoreProperties(ignoreUnknown = true)
public class CustomerVO {

	@JsonProperty(value = "mobileNumber")
	@NotNull(message = "{customer.mobileNumber.notNull}")
	@Pattern(regexp = "^[6-9]\\d{9}$", message = "{customer.mobileNumber.invalid}")
	@Schema(description = "Customer mobile number which is a required parameter for all operations", type = "String")
	private String mobileNumber;
	
	@JsonProperty(value = "pickupLatitude")
	@NotNull(message = "{customer.pickupLatitude.notNull}")
	@Schema(description = "Latitude of the pickup location", type = "number", format = "double")
	private Double pickupLatitude;
	
	@JsonProperty(value = "pickupLongitude")
	@NotNull(message = "{customer.pickupLongitude.notNull}")
	@Schema(description = "Longitude of the pickup location", type = "number", format = "double")
	private Double pickupLongitude;
	
	@JsonProperty(value = "dropLatitude")
	@NotNull(message = "{customer.dropLatitude.notNull}")
	@Schema(description = "Latitude of the destination location", type = "number", format = "double")
	private Double dropLatitude;
	
	@JsonProperty(value = "dropLongitude")
	@NotNull(message = "{customer.dropLongitude.notNull}")
	@Schema(description = "Longitude of the destination location", type = "number", format = "double")
	private Double dropLongitude;
	
	@JsonProperty(value = "pinkTaxi", required = false)
	@Schema(description = "Customer requires pink taxi or not", type = "boolean")
	private boolean isPinkTaxi;
	
	@JsonProperty(value = "taxiId", required = false)
	@Schema(description = "The assigned taxi id for the customer", type = "string")
	private String taxiId;
	
	@JsonProperty(value = "distance", required = false)
	@Schema(description = "The distance in kilometers between the source and destination", type = "integer", format = "int64")
	private int distance;
	
	@JsonProperty(value = "status", required = false)
	@Schema(description = "The status of the ride", type = "string")
	private String rideStatus;
	
	@JsonProperty(value = "bookingId", required = false)
	@Schema(description = "The unique booking id of the ride", type = "string")
	private String bookingId;
	
	@JsonProperty(value = "startTime", required = false)
	@Schema(description = "The time when the ride started", type = "string", format = "date-time", example = "2019-10-30T12:34:23Z")
	private String startTime;
	
	@JsonProperty(value = "endTime", required = false)
	@Schema(description = "The time when the ride completed", type = "string", format = "date-time", example = "2019-10-30T12:34:23Z")
	private String endTime;
	
	@JsonProperty(value = "rideCharges", required = false)
	@Schema(description = "The charges for the ride", type = "integer", format = "int64")
	private int rideCharges;
	
	public String getMobileNumber() {
		return mobileNumber;
	}

	public void setMobileNumber(String mobileNumber) {
		this.mobileNumber = mobileNumber;
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

	public boolean isPinkTaxi() {
		return isPinkTaxi;
	}

	public void setPinkTaxi(boolean isPinkTaxi) {
		this.isPinkTaxi = isPinkTaxi;
	}

	public String getTaxiId() {
		return taxiId;
	}

	public void setTaxiId(String taxiId) {
		this.taxiId = taxiId;
	}

	public int getDistance() {
		return distance;
	}

	public void setDistance(int distance) {
		this.distance = distance;
	}

	public String getRideStatus() {
		return rideStatus;
	}

	public void setRideStatus(String rideStatus) {
		this.rideStatus = rideStatus;
	}
	
	public String getBookingId() {
		return bookingId;
	}

	public void setBookingId(String bookingId) {
		this.bookingId = bookingId;
	}
	
	public String getStartTime() {
		return startTime;
	}

	public void setStartTime(String startTime) {
		this.startTime = startTime;
	}
	
	public String getEndTime() {
		return endTime;
	}

	public void setEndTime(String endTime) {
		this.endTime = endTime;
	}
	
	public int getRideCharges() {
		return rideCharges;
	}

	public void setRideCharges(int rideCharges) {
		this.rideCharges = rideCharges;
	}

	@Override
	public String toString() {
		return "CustomerVO [mobileNumber=" + mobileNumber + ", pickupLatitude=" + pickupLatitude + ", pickupLongitude="
				+ pickupLongitude + ", dropLatitude=" + dropLatitude + ", dropLongitude=" + dropLongitude
				+ ", isPinkTaxi=" + isPinkTaxi + ", taxiId=" + taxiId + ", distance=" + distance + ", rideStatus="
				+ rideStatus + ", bookingId=" + bookingId + ", startTime=" + startTime + ", endTime=" + endTime
				+ ", rideCharges=" + rideCharges + "]";
	}

}
