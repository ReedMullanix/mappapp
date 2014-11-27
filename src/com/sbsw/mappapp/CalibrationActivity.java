package com.sbsw.mappapp;

import java.io.File;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.ImageView;
import com.sbsw.mappapp.Utils.*;

public class CalibrationActivity extends Activity {

	@Override
	public void onCreate(Bundle savedInstanceState) {
		//Load up our layout
		super.onCreate(savedInstanceState);
		setContentView(R.layout.calibrate_main);
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
		//Reload our photo from disk
		//TODO: Store the photo in memory only
		//Makes the app faster, avoids copyright issues
		//Might cause memory issues though
		File mediaStorageDir = new File(Environment.getExternalStorageDirectory().getPath() + "/MappApp/");
		ImageView myImage = (ImageView) findViewById(R.id.imageView1);
		myImage.setImageBitmap(BitmapFactory.decodeFile(mediaStorageDir + "tmpMap.png"));
	}
	
	//Called when we click the button to lock our position
	public void DotLock(View view) {
		//Get the dot
		Dot dot = ((Dot) findViewById(R.id.dot));
		//Save its position in our coordinate converter
		LatLonToXY.saveDotPos(dot.getX(), dot.getY());
		//Start up the walk activty
		Intent intent = new Intent(CalibrationActivity.this, WalkActivity.class);
		startActivity(intent);
	}
}
