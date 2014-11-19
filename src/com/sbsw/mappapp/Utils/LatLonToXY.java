package com.sbsw.mappapp.Utils;

import com.sbsw.mappapp.model.GpsPoint;

public class LatLonToXY {
	
	private static double scaleFactor = 80000;
	private static float dotX;
	private static float dotY;
	private static GpsPoint dotGps;
	
	public static float[] convert(double lon, double lat) {
		float[] output = new float[2];
		output[0] = dotX + (float)((dotGps.getLongitude() - lon) * scaleFactor);
		output[1] = dotY + (float)((dotGps.getLatitude() - lat) * scaleFactor);
		return output;
	}
	
	public static void saveDotPos(float x, float y) {
		dotX = x;
		dotY = y;
	}
	
	public static void setDotGPS(GpsPoint gps) {
		dotGps = gps;
	}
	
	public static float[] getDotPos() {
		float[] out = {dotX, dotY};
		return out;
	}
	
	public static void setScaleFactor(double scale) {
		scaleFactor = scale;
	}
}
