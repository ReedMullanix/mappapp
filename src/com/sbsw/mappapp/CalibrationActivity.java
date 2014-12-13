package com.sbsw.mappapp;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.sbsw.mappapp.Utils.Dot;
import com.sbsw.mappapp.Utils.Map;


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
//		File mediaStorageDir = new File(Environment.getExternalStorageDirectory().getPath() + "/MappApp/");
		ImageView myImage = (ImageView) findViewById(R.id.imageView1);
		myImage.setImageBitmap(Map.getBitmap());
	}
	
	//Called when we click the button to lock our position
	public void DotLock(View view) {
		//Get the dot
		Dot dot = ((Dot) findViewById(R.id.dot));
		//Add it to our Map class
        Map.setDot(dot);
		//Start up the walk activty
        Log.d("ACTIVITY CHANGE", "starting walk activity");
        RelativeLayout myLayout = (RelativeLayout) findViewById(R.id.calibrate_layout);
        myLayout.removeView(dot);
        Intent intent = new Intent(CalibrationActivity.this, WalkActivity.class);
		startActivity(intent);
	}
}
