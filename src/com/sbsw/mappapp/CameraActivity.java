package com.sbsw.mappapp;

import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.View;


/**
 * 
 * @author Reed
 * Activity that controls the CameraFragment
 */
public class CameraActivity extends FragmentActivity {
	
	private CameraFragment fragment;
	
	//Called On View Create
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		//Set up our layout
		setContentView(R.layout.camera_activty);
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
		fragment = (CameraFragment) getSupportFragmentManager().findFragmentById(R.id.camera_view);

		if (fragment == null) {
			fragment = new CameraFragment();
			//Adds the fragment to the FrameLayout called camera_view
			getSupportFragmentManager().beginTransaction()
					.add(R.id.camera_view, fragment).commit();
		}
	}
	
	/**
	 * Called when the photo capture button is pressed
	 * @author Reed
	 * @param View
	 */
	public void takePhoto(View view) {
		fragment.takePhoto(view);
	}
	
	public void focusCamera(View view) {
		fragment.focusCamera(view);
	}
}
