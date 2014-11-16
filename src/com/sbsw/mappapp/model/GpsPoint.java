package com.sbsw.mappapp.model;

public class GpsPoint {
	private double lat;
	private double lon;
	private long time;
	
	public GpsPoint(double lat, double lon, long time) {
		this.lat = lat;
		this.lon = lon;
		this.time = time;
	}
	
	public double getLatitude() {
		return lat;
	}
	
	public double getLongitude() {
		return lon;
	}
	
	public long getTime() {
		return time;
	}
}
