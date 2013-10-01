package org.balote.geocodingapp.models;

public class LatLngModel {

	private long latitude;
	private long longitude;
	
	public LatLngModel() {
		
	}
	
	public LatLngModel(long latitude, long longitude) {
		this.latitude = latitude;
		this.longitude = longitude;
	}

	/**
	 * @return the latitude
	 */
	public long getLatitude() {
		return latitude;
	}

	/**
	 * @param latitude the latitude to set
	 */
	public void setLatitude(long latitude) {
		this.latitude = latitude;
	}

	/**
	 * @return the longitude
	 */
	public long getLongitude() {
		return longitude;
	}

	/**
	 * @param longitude the longitude to set
	 */
	public void setLongitude(long longitude) {
		this.longitude = longitude;
	}
	
	
}
