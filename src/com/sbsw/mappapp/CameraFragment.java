package com.sbsw.mappapp;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import com.sbsw.mappapp.Utils.Map;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.hardware.Camera;
import android.hardware.Camera.AutoFocusCallback;
import android.hardware.Camera.Parameters;
import android.hardware.Camera.PictureCallback;
import android.media.ExifInterface;
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
			parameters.setPictureSize(640, 480);
			//Auto flash and auto focus enabled
			parameters.setFlashMode(Camera.Parameters.FLASH_MODE_AUTO);
			parameters.setFocusMode(Camera.Parameters.FOCUS_MODE_AUTO);
			//Set our picture size to a low res
			parameters.setRotation(90);
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
				Bitmap bm = BitmapFactory.decodeByteArray(data, 0, (data != null) ? data.length : 0);
				Map.getInstance().setBitmap(bm);
				Intent intent = new Intent(getActivity(), CalibrationActivity.class);
				startActivity(intent);
			}

		}
	};
}
