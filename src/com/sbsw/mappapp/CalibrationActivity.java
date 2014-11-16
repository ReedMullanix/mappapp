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
import android.support.v4.view.GestureDetectorCompat;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;
import android.widget.ImageView.ScaleType;

import com.sbsw.mappapp.Utils.*;

public class CalibrationActivity extends Activity {

	private GpsService _gps;
	private GestureDetectorCompat _detector; 
	
	
	public void onCreate(Bundle savedInstanceState) {
		//Load up our layout
		super.onCreate(savedInstanceState);
		setContentView(R.layout.calibrate_main);
		File mediaStorageDir = new File(Environment.getExternalStorageDirectory().getPath() + "/MappApp/");
		ImageView myImage = (ImageView) findViewById(R.id.imageView1);
		myImage.setImageBitmap(BitmapFactory.decodeFile(mediaStorageDir + "tmpMap.png"));
	}
	
	public void DotLock(View view) {
		((Dot) findViewById(R.id.dot)).lock();
	}
}
