package com.fueber.taxi.dto;

public class TaxiDTO {

	private String taxiID;
	
	private double latitude;
	
	private double longitude;
	
	private boolean isPinkTaxi;
	
	private boolean isAssignedToCustomer;

	public TaxiDTO(String taxiID, double latitude, double longitude, boolean isPinkColor, boolean isAssignedToCustomer) {
		this.taxiID = taxiID;
		this.latitude = latitude;
		this.longitude = longitude;
		this.isPinkTaxi = isPinkColor;
		this.isAssignedToCustomer = isAssignedToCustomer;
	}

	public String getTaxiID() {
		return taxiID;
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

	public boolean isAssignedToCustomer() {
		return isAssignedToCustomer;
	}

	public void setAssignedToCustomer(boolean isAssignedToCustomer) {
		this.isAssignedToCustomer = isAssignedToCustomer;
	}

	@Override
	public String toString() {
		return "TaxiDTO [taxiID=" + taxiID + ", latitude=" + latitude + ", longitude=" + longitude + ", isPinkTaxi="
				+ isPinkTaxi + ", isAssignedToCustomer=" + isAssignedToCustomer + "]";
	}
	
}
