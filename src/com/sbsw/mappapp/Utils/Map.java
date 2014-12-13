package com.sbsw.mappapp.Utils;

import android.graphics.Bitmap;
import android.graphics.Point;
import android.util.Log;
import android.view.Display;

import com.sbsw.mappapp.model.GpsPoint;

import java.util.ArrayList;

/**
 * Created by reed on 12/12/14.
 *
 * Allows us to composite data from throughout the session
 * and place it in one spot
 * Also allows for passing of data between activities without
 * writing to disk
 */
public class Map {

    /**
     * Instance that stores all data within a map
     */
    private static MapData instance = new MapData();

    /**
     * Scaling factor for conversion
     */
    private static double scaleFactor = 80000;

    /**
     * Gets the Bitmap associated with this map
     * @return The Map Bitmap
     */
    public static Bitmap getBitmap() {
        return instance.bm;
    }

    /**
     * Sets the Bitmap associated with this map
     * @param bm: the Bitmap to associate
     */
    public static void setBitmap(Bitmap bm) {
        instance.bm = bm;
    }

    /**
     * Gets the Dot associated with this ma
     * @return The associated dot
     */
    public static Dot getDot() {
        return instance.dot;
    }

    /**
     * Sets the associated Dot
     * @param dot: the Dot to associate
     */
    public static void setDot(Dot dot) {
        instance.dot = dot;
    }

    /**
     * Sets the starting GPS position
     * @param gps: The starting Position
     */
    public static void setGPSStart(GpsPoint gps) {
        instance.gps = gps;
    }

    /**
     * Gets the starting GPS Position
     * @return GPS Starting Position
     */
    public static GpsPoint getGPSStart() {
        return instance.gps;
    }

    /**
     * Adds a point to the list of previous points
     * @param point: The Point to add
     */
    public static void addGPSPoint(GpsPoint point) {
        instance.points.add(point);
        Log.d("GPS UPDATE", "Adding GPS POINT " + point.getLatitude() + " " + point.getLongitude());
    }

    /**
     * Gets the list of previous GPS points
     * @return The list of GPS Points
     */
    public static ArrayList<GpsPoint> getGPSPoints() {
        return instance.points;
    }

    /**
     * Converts a GPS Position into screen coordinates for drawing relative
     * to starting Position
     * Uses a scaling factor that can be set by setScaleFactor
     * @param point: A GPS point
     * @return A converted Screen Point Pair
     */
    public static ScreenPointPair convertToScreenCoords(GpsPoint point) {
        //Get our display size
        Point size = new Point();
        instance.display.getSize(size);
        float screenRatio = (float)(size.y)/size.x;
        //There is 69 miles between latitude degrees, so 1/69th of a degree is a mile
        //First we get the change in latitude, then multiply it by 60 so that our delta is scaled to be about a mile per pixel
        //Then, we multiply by the number of pixels/4 to make the entire screen about 4 miles tall
        float ypos = instance.dot.getY() + (float)((instance.gps.getLatitude() - point.getLatitude()) * 69 * (size.y / 4));
        //To calculate the xscale, first we need to get the Distance between to Longitudes at the given lat
        //All of this math assumes the earth is a sphere, so the may be slight errors
        //3959 is the radius of the earth
        float dist = (float) ((Math.PI / 180) * 3959 * Math.cos(point.getLatitude() * (Math.PI / 180)));
        Log.v("CONVERTER", "" + dist);
        //We multiply by size.y/2 to keep things consistent, so moving 100 feet north and west will draw a diagonal line
        float xpos = instance.dot.getX() + (float)((instance.gps.getLongitude() - point.getLongitude()) * dist * (size.y / 4));

        return new ScreenPointPair(xpos, ypos);
    }

    /**
     * Sets the display, call at app initialization
     * @param display: Pretty self explanatory
     */
    public static void setDisplay(Display display) {
        instance.display = display;
    }

    //This is where the actual data structure of a map is stored
    private static class MapData {
        public Bitmap bm;
        public GpsPoint gps;
        public Dot dot;
        public ArrayList<GpsPoint> points = new ArrayList<GpsPoint>();
        //We Pass the display info at init so that we can use it for calculations
        public Display display;
        public MapData() {}
    }
}
