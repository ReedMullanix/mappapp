package com.sbsw.mappapp;

import android.app.Activity;
import android.graphics.Matrix;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.sbsw.mappapp.Utils.Dot;
import com.sbsw.mappapp.Utils.Map;

public class WalkActivity extends Activity implements OnTouchListener {

	private GpsService _gps;
	private float _previousX;
	private float _previousY;
	private Matrix matrix = new Matrix();
	

	@Override
	public void onCreate(Bundle savedInstanceState) {
		//Load up our layout
		super.onCreate(savedInstanceState);
		_gps = new GpsService(WalkActivity.this);
		//Loop to wait for GPS Pos, sleep thread for one second between polls
		while(Map.getGPSPoints().size() == 0) {
			try {
				Thread.currentThread();
				Thread.sleep(1000);
			} catch (Exception e) {

			}
			Log.d("GPS COMMS", "Polling...");
		}
		Map.setGPSStart(Map.getGPSPoints().get(0));
		setContentView(R.layout.walk_main);
		ImageView myImage = (ImageView) findViewById(R.id.map_img);
		myImage.setImageBitmap(Map.getBitmap());
		Dot dot = Map.getDot();
        RelativeLayout myLayout = (RelativeLayout) findViewById(R.id.walk_layout);
        myLayout.addView(dot);
        dot.lock();
		myImage.setScaleType(ImageView.ScaleType.MATRIX);
		myImage.setOnTouchListener(this);
	}


	public void forceUpdate(View view) {
		_gps.forceUpdate();
	}


	@Override
	public boolean dispatchTouchEvent (MotionEvent ev) {
		ImageView myImage = (ImageView) findViewById(R.id.map_img);
		myImage.dispatchTouchEvent(ev);
		return false;
	}

	@Override
	public boolean onTouch(View v, MotionEvent event) {
		ImageView myImage = (ImageView) findViewById(R.id.map_img);
        //Alias the dot positions
        float dotX = Map.getDot().getX();
        float dotY = Map.getDot().getY();
        //Get position of the event relative to the dot
		float x = event.getX()  - dotX;
		float y = event.getY() - dotY;
        //make sure the event is a move event
        if(event.getAction() == MotionEvent.ACTION_MOVE) {
            //Get the delta from the event and the previous event
			float dx = x - _previousX;
			float dy = y - _previousY;
            //Generate our 3 movement vectors from this data
			float deltaVector = (float) Math.sqrt(dx*dx + dy*dy);
			float touchVector = (float) Math.sqrt(x*x + y*y);
			float prevTouchVector = (float) Math.sqrt(_previousX*_previousX + _previousY*_previousY);
            //Get a scale vector, and scale the matrix
			float scale = touchVector / prevTouchVector;
			matrix.postScale(scale, scale, dotX, dotY);
            //Determine the direction and magnitude of the rotation
            //Then apply it to the matrix
			if(y/x - _previousY/_previousX >=0)
				matrix.postRotate((float)(Math.atan(deltaVector/prevTouchVector)*(180/Math.PI)), dotX, dotY);
			else
				matrix.postRotate((float)(-Math.atan(deltaVector/prevTouchVector)*(180/Math.PI)), dotX, dotY);
		}
        //Manipulate the image
		myImage.setImageMatrix(matrix);
        //Store of the relative position for use in the next cycle
		_previousX = x;
		_previousY = y;
	    return true;
	}
}
