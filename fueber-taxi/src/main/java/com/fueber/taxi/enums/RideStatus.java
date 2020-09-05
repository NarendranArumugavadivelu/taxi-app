package com.fueber.taxi.enums;

public enum RideStatus {

	BOOKED("Booked"),
	STARTED("Started"),
	CANCELED("Canceled"),
	COMPLETED("Completed");
	
	private String status;

	RideStatus(String status) {
		this.status = status;
	}

	public String getStatus() {
		return status;
	}
	
}
