package com.sbsw.mappapp;

import com.sbsw.mappapp.model.GpsPoint;
import com.sbsw.mappapp.model.GpsPointList;

import android.app.AlertDialog;
import android.app.Service;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.IBinder;
import android.provider.Settings;
import android.util.Log;

public class GpsService extends Service implements LocationListener {

	//Our location
	private Location _location;
	//Our position in lat/lon
	private double _lat;
	private double _lon;
	
	private Context _context;
	
	protected LocationManager locationManager;
	
	 // 10 meters
	private static final long MIN_DISTANCE_CHANGE_FOR_UPDATES = 10;
	
	 // 1 minute
	private static final long MIN_TIME_BW_UPDATES = 1000 * 60 * 1;
	
	//Flag to see if we can access Loc
	private boolean _canGetLocation = false;
	
	//Constructor
	public GpsService(Context context) {
        this._context = context;
        getLocation();
    }
	
	//Gets the location
	public Location getLocation() {
		try {
			locationManager = (LocationManager) _context.getSystemService(LOCATION_SERVICE);
			//Check To See If GPS Is enabled
			Log.d("Setup Complete", "Setup Complete");
			if(locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
				this._canGetLocation = true;
				Log.d("Location Provider Enabled", "Location Provider Enabled");
				//Check to see if we have a prior location
				if(_location == null) {
					locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,
							MIN_DISTANCE_CHANGE_FOR_UPDATES, 
							MIN_TIME_BW_UPDATES, 
							this);
					Log.d("GPS Enabled", "GPS Enabled");
					//Request our last known location, and get the lat and lon
					if (locationManager != null) {
                        _location = locationManager
                                .getLastKnownLocation(LocationManager.GPS_PROVIDER);
                        if (_location != null) {
                            _lat = _location.getLatitude();
                            _lon = _location.getLongitude();
                        }
                    }
				}
			} else {
				Log.d("Location Provider disabled", "Location Provider disabled");
				showSettingsAlert();
			}
		} catch(Exception e) {
			e.printStackTrace();
		}
		return _location;
	}
	
	public double getLatitude(){
        if(_location != null){
            _lat = _location.getLatitude();
        }
        // return latitude
        return _lat;
    }
     
  
    public double getLongitude(){
        if(_location != null){
        	_lon = _location.getLongitude();
        }
        // return longitude
        return _lon;
    }
	
    public boolean canGetLocation() {
    	return _canGetLocation;
    }
    
	@Override
	public void onLocationChanged(Location location) {
		GpsPointList.getInstance().write(new GpsPoint(_lat, _lon, System.currentTimeMillis()));
	}
	
	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}



	@Override
	public void onStatusChanged(String provider, int status, Bundle extras) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onProviderEnabled(String provider) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onProviderDisabled(String provider) {
		if(provider.equals(LocationManager.GPS_PROVIDER)) {
			showSettingsAlert();
		}
	}
	
	public void showSettingsAlert(){
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(_context);
      
        // Setting Dialog Title
        alertDialog.setTitle("GPS settings");
  
        // Setting Dialog Message
        alertDialog.setMessage("GPS is not enabled. Do you want to go to settings menu?");
  
        // Setting Icon to Dialog
        //alertDialog.setIcon(R.drawable.delete);
  
        // On pressing Settings button
        alertDialog.setPositiveButton("Settings", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog,int which) {
                Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                _context.startActivity(intent);
            }
        });
  
        // on pressing cancel button
        alertDialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
            dialog.cancel();
            }
        });
  
        // Showing Alert Message
        alertDialog.show();
    }
}
