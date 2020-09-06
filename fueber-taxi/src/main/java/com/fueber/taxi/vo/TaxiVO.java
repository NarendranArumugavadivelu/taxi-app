package com.fueber.taxi.vo;

import com.fasterxml.jackson.annotation.JsonProperty;

import io.swagger.v3.oas.annotations.media.Schema;

public class TaxiVO {

	@JsonProperty("taxiID")
	@Schema(description = "The taxi id", type = "string")
	private String taxiID;
	
	@JsonProperty("latitude")
	@Schema(description = "The latitude position of the taxi", type = "double")
	private double latitude;
	
	@JsonProperty("longitude")
	@Schema(description = "The longitude position of the taxi", type = "double")
	private double longitude;
	
	@JsonProperty("pinkTaxi")
	@Schema(description = "The color of the taxi", type = "boolean")
	private boolean isPinkTaxi;

	public String getTaxiID() {
		return taxiID;
	}

	public void setTaxiID(String taxiID) {
		this.taxiID = taxiID;
	}

	public double getLatitude() {
		return latitude;
	}

	public void setLatitude(double latitude) {
		this.latitude = latitude;
	}

	public double getLongitude() {
		return longitude;
	}

	public void setLongitude(double longitude) {
		this.longitude = longitude;
	}

	public boolean isPinkTaxi() {
		return isPinkTaxi;
	}

	public void setPinkTaxi(boolean isPinkTaxi) {
		this.isPinkTaxi = isPinkTaxi;
	}
	
	
}
