package com.sbsw.mappapp;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.hardware.Camera;
import android.hardware.Camera.AutoFocusCallback;
import android.hardware.Camera.Parameters;
import android.hardware.Camera.PictureCallback;
import android.media.MediaActionSound;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Fragment that handles camera operation
 * @author Reed
 * 
 */
public class CameraFragment extends Fragment{
	
	/**
	 * The camera instance
	 */
	private Camera _camera;
	
	/**
	 * The Camera Preview
	 * @author Reed
	 */
	private CameraPreview _preview;
	
	public void onCreate(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		_camera = getCameraInstance();
	}
	
	/**
	 * Setup a Camera Preview
	 * @author Reed
	 * @return The Camera Preview
	 */
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		if(_preview == null) {
			_preview = new CameraPreview(getActivity(), _camera);
		}
		return _preview;
		//return _preview;
	}
	
	//Called on pause, releases the camera
	public void onPause() {
		super.onPause();
		releaseCamera();
	}
	
	//Called on stop, releases the camera
	public void onStop() {
		super.onStop();
		releaseCamera();
	}
	
	//Called on resume, gets camera instance if need be
	public void onResume() {
		super.onResume();
		//Checks to see if we have a camera instance
		if(_camera == null) {
			_camera = getCameraInstance();
		}
	}
	
	/**
	 * Gets the camera instance and performs setup operations
	 * @author Reed
	 * @throws Exception on Camera Acquisition Error
	 * @return The Camera Instance
	 */
	public static Camera getCameraInstance(){
		Camera c = null;
		try {
			// attempt to get a Camera instance
			c = Camera.open(); 
			//Setup Camera Params
			Parameters parameters = c.getParameters();
			//We dont need High-Res Photos, so we set the quality to shit
			parameters.setJpegQuality(50);
			//Auto flash and auto focus enabled
			parameters.setFlashMode(Camera.Parameters.FLASH_MODE_AUTO);
			parameters.setFocusMode(Camera.Parameters.FOCUS_MODE_AUTO);
			parameters.setRotation(270);
			//Actually set the params
			c.setParameters(parameters);
		}
		catch (Exception e){
			Log.d("Camera Error", e.getMessage());
		}
		return c;
	}
	
	/**
	 * Releases the Camera Instance
	 * @author Reed
	 */
	private void releaseCamera(){
		if (_camera != null){
			_preview.getHolder().removeCallback(_preview);
			_camera.release();
			_camera = null;
		}
	}

	/**
	 * Auto-Focuses The Camera
	 * @author Reed
	 * @param view
	 */
	public void focusCamera(View view) {
		_camera.autoFocus(_focus);
	}
	
	/**
	 * Callback for focusing the camera
	 * @author Reed
	 */
	private AutoFocusCallback _focus = new AutoFocusCallback() {
		
		@Override
		public void onAutoFocus(boolean success, Camera camera) {
			MediaActionSound focusComplete = new MediaActionSound();
			focusComplete.load(MediaActionSound.FOCUS_COMPLETE);
			focusComplete.play(MediaActionSound.FOCUS_COMPLETE);
		}
	};

	/**
	 * Begins picture taking callback
	 * @author Reed
	 * @param v
	 */
	public void takePhoto(View v){
		_camera.takePicture(null, null, _picture);
	}
	
	/**
	 * Actually takes the picture
	 * @author Reed
	 */
	private PictureCallback _picture = new PictureCallback() {

		@Override
		public void onPictureTaken(byte[] data, Camera camera) {
			//If we actually have data, operate on it
			if (data != null) {
				//Get our screen size for scaling
				int screenWidth = getResources().getDisplayMetrics().widthPixels;
				int screenHeight = getResources().getDisplayMetrics().heightPixels;
				Bitmap bm = BitmapFactory.decodeByteArray(data, 0, (data != null) ? data.length : 0);
//				//If the orientation is in portrait, we fix the rotation of the image that is present
//				//This is subject to change, as it fixes bugs on some platforms but creates them in others
//				if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
//					// Notice that width and height are reversed
//					Bitmap scaled = Bitmap.createScaledBitmap(bm, screenHeight, screenWidth, true);
//					int w = scaled.getWidth();
//					int h = scaled.getHeight();
//					// Setting post rotate to 90
//					Matrix mtx = new Matrix();
//					mtx.postRotate(90);
//					// Rotating Bitmap
//					bm = Bitmap.createBitmap(scaled, 0, 0, w, h, mtx, true);
//				}else{// LANDSCAPE MODE
//					//No need to reverse width and height
//					Bitmap scaled = Bitmap.createScaledBitmap(bm, screenWidth,screenHeight , true);
//					bm=scaled;
//				}
				Bitmap scaled = Bitmap.createScaledBitmap(bm, screenWidth,screenHeight , true);
				bm=scaled;
				//Create a file stream to write to
				FileOutputStream out = null;
				File mediaStorageDir = new File(Environment.getExternalStorageDirectory().getPath() + "/MappApp/");
				try {
					out = new FileOutputStream(mediaStorageDir + "tmpMap.png");
					bm.compress(Bitmap.CompressFormat.JPEG, 100, out); // bmp is your Bitmap instance
					// We use JPEG because quality < speed in our use case
				} catch (Exception e) {
					e.printStackTrace();
				} finally {
					try {
						if (out != null) {
							out.close();
						}
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
				//Start the calibration activty
				Intent intent = new Intent(getActivity(), CalibrationActivity.class);
				startActivity(intent);
			}

		}
	};
}
