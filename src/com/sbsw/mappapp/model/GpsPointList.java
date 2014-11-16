package com.sbsw.mappapp.model;

import java.util.ArrayList;

public class GpsPointList {
	//holds all of the GpsPoints
	private ArrayList<GpsPoint> points;
	private static GpsPointList instance = null;
	
	private GpsPointList() {
		points = new ArrayList<GpsPoint>(); 
	}
	
	public static GpsPointList getInstance() {
		if(instance==null) {
			instance = new GpsPointList();
		}
		return instance;
	}
	
	//puts a GpsPoint at the end of points
	public void write(GpsPoint input) {
		points.add(input);
	}
	
	
	public GpsPoint read() {
		return points.get(points.size());
	}
	
	public int length() {
		return points.size();
	}
}
