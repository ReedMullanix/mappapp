package com.sbsw.mappapp;

import java.io.File;

import com.sbsw.mappapp.Utils.Dot;
import com.sbsw.mappapp.Utils.DrawView;
import com.sbsw.mappapp.Utils.LatLonToXY;
import com.sbsw.mappapp.model.GpsPointList;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.PointF;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.ImageView;
import android.widget.Toast;

public class WalkActivity extends Activity implements OnTouchListener {

	private GpsService _gps;
	private float _previousX;
	private float _previousY;
	private float _rotation;
	private float dotX;
	private float dotY;
	private Matrix matrix = new Matrix();
	

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
		LatLonToXY.setDotGPS(GpsPointList.getInstance().read().get(0));
		setContentView(R.layout.walk_main);
		File mediaStorageDir = new File(Environment.getExternalStorageDirectory().getPath() + "/MappApp/");
		ImageView myImage = (ImageView) findViewById(R.id.map_img);
		myImage.setImageBitmap(BitmapFactory.decodeFile(mediaStorageDir + "tmpMap.png"));
		Dot dot = (Dot) findViewById(R.id.dot);
		float[] dotPos = LatLonToXY.getDotPos();
		dot.setX(dotPos[0]);
		dotX = dotPos[0];
		dot.setY(dotPos[1]);
		dotY = dotPos[1];
		dot.lock();
		myImage.setScaleType(ImageView.ScaleType.MATRIX);
		myImage.setOnTouchListener(this);
	}


	public void forceUpdate(View view) {
		_gps.forceUpdate();
	}


	public boolean dispatchTouchEvent (MotionEvent ev) {
		ImageView myImage = (ImageView) findViewById(R.id.map_img);
		myImage.dispatchTouchEvent(ev);
		return false;
	}

	@Override
	public boolean onTouch(View v, MotionEvent event) {
		ImageView myImage = (ImageView) findViewById(R.id.map_img);
		float x = event.getX()  - dotX;
		float y = event.getY() - dotY;
		switch (event.getAction()) {
		case MotionEvent.ACTION_MOVE:
			float dx = x - _previousX;
			float dy = y - _previousY;
			float distVector1 = (float) Math.sqrt(dx*dx + dy*dy);
			float distVector2 = (float) Math.sqrt(x*x + y*y);
			float distVector3 = (float) Math.sqrt(_previousX*_previousX + _previousY*_previousY);
			float scale = distVector2 / distVector3;
			matrix.postScale(scale, scale, dotX, dotY);
			if(y/x - _previousY/_previousX >=0)
				matrix.postRotate((float)(Math.atan(distVector1/distVector3)*(180/Math.PI)), dotX, dotY);
			else
				matrix.postRotate((float)(-Math.atan(distVector1/distVector3)*(180/Math.PI)), dotX, dotY);
		}
		myImage.setImageMatrix(matrix);
		_previousX = x;
		_previousY = y;
	    return true;
	}
}
