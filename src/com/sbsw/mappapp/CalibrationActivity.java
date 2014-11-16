package com.sbsw.mappapp;

import java.io.File;
import java.io.FileInputStream;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.Point;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;
import android.widget.ImageView.ScaleType;
import android.view.GestureDetector;

import com.sbsw.mappapp.Utils.*;

public class CalibrationActivity extends Activity implements GestureDetector.OnGestureListener {

	private GpsService _gps;

	public void onCreate(Bundle savedInstanceState) {
		//Load up our layout
		super.onCreate(savedInstanceState);
		setContentView(R.layout.calibrate_main);
		File mediaStorageDir = new File(Environment.getExternalStorageDirectory().getPath() + "/MappApp/");
		ImageView myImage = (ImageView) findViewById(R.id.imageView1);
		myImage.setImageBitmap(BitmapFactory.decodeFile(mediaStorageDir + "tmpMap.png"));
	}

	@Override
	public boolean onDown(MotionEvent e) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void onShowPress(MotionEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean onSingleTapUp(MotionEvent e) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX,
			float distanceY) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void onLongPress(MotionEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX,
			float velocityY) {
		((Dot) findViewById(R.id.dot)).lock();
		Log.d("FLING FLUNG", "FLOONG");
		return true;
	}
}
