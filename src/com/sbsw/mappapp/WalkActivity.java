package com.sbsw.mappapp;

import java.io.File;

import com.sbsw.mappapp.Utils.Dot;
import com.sbsw.mappapp.Utils.LatLonToXY;
import com.sbsw.mappapp.model.GpsPointList;

import android.app.Activity;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.widget.ImageView;

public class WalkActivity extends Activity{
	
	private GpsService _gps;
	
	public void onCreate(Bundle savedInstanceState) {
		//Load up our layout
		super.onCreate(savedInstanceState);
		_gps = new GpsService(WalkActivity.this);
		//Loop to wait for GPS Pos, sleep thread for one second between polls
		while(GpsPointList.getInstance().length() == 0) {
			try {
				Thread.currentThread().sleep(1000);
			} catch (Exception e) {
				
			}
			Log.d("GPS COMMS", "Polling...");
		}
		LatLonToXY.setDotGPS(GpsPointList.getInstance().read());
		setContentView(R.layout.walk_main);
		File mediaStorageDir = new File(Environment.getExternalStorageDirectory().getPath() + "/MappApp/");
		ImageView myImage = (ImageView) findViewById(R.id.map_img);
		myImage.setImageBitmap(BitmapFactory.decodeFile(mediaStorageDir + "tmpMap.png"));
		Dot dot = (Dot) findViewById(R.id.dot);
		float[] dotPos = LatLonToXY.getDotPos();
		dot.setX(dotPos[0]);
		dot.setY(dotPos[1]);
		dot.lock();
		
	}
}
