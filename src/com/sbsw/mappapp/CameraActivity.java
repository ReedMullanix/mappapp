package com.sbsw.mappapp;


import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.hardware.Camera;
import android.hardware.Camera.PictureCallback;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.Surface;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.Toast;

public class CameraActivity extends Activity {
	private static Camera _camera;
	private static CameraPreview _preview;
	private static Context _context;

	private static final int MEDIA_TYPE_IMAGE = 1;

	private static ImageView myImage;
	private static File imgFile = null; 
	int duration = Toast.LENGTH_LONG;

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.camera_preview);
		_camera = getCameraInstance();
		Camera.Parameters params = _camera.getParameters();
		params.setRotation(90);
		_camera.setParameters(params);
		_context = getApplicationContext();
		_preview = new CameraPreview(_context, _camera);
		FrameLayout previewLayout = (FrameLayout) findViewById(R.id.camera_preview);
		previewLayout.addView(_preview);
	}

	protected void onPause() {
		super.onPause();
		releaseCamera();              // release the camera immediately on pause event
	}
	
	protected void onStop() {
		super.onStop();
		releaseCamera();
	}
	
	protected void onResume() {
		_camera = getCameraInstance();
		Camera.Parameters params = _camera.getParameters();
		params.setRotation(90);
		_camera.setParameters(params);
	}

	private void releaseCamera(){
		if (_camera != null){
			_preview.getHolder().removeCallback(_preview);
			_camera.release();
			_camera = null;
		}
	}

	public static Camera getCameraInstance(){
		Camera c = null;
		try {
			c = Camera.open(); // attempt to get a Camera instance
			Camera.Parameters parameters = c.getParameters();
			parameters.set("orientation", "portrait");
			c.setParameters(parameters);
		}
		catch (Exception e){
			Log.d("Camera Error", e.getMessage());
		}
		return c; // returns null if camera is unavailable
	}

	public void takePhoto(View v){
		_camera.takePicture(null, null, _picture);
	}

	private PictureCallback _picture = new PictureCallback() {

		@Override
		public void onPictureTaken(byte[] data, Camera camera) {
			//	    	File pictureFile = getOutputMediaFile(MEDIA_TYPE_IMAGE);
			//	    	if (pictureFile == null){
			//	            Log.d("ERROR", "Error creating media file, check storage permissions:" );
			//	        }
			//
			//	        try {
			//	            FileOutputStream fos = new FileOutputStream(pictureFile);
			//	            fos.write(data);
			//	            fos.close();
			//
			//	        } catch (FileNotFoundException e) {
			//	            Log.d("ERROR", "File not found: " + e.getMessage());
			//	        } catch (IOException e) {
			//	            Log.d("ERROR", "Error accessing file: " + e.getMessage());
			//	        }
			//	        Intent intent = new Intent(CameraActivity.this, CalibrationActivity.class);
			//        	startActivity(intent);
			//	    }
			if (data != null) {
				int screenWidth = getResources().getDisplayMetrics().widthPixels;
				int screenHeight = getResources().getDisplayMetrics().heightPixels;
				Bitmap bm = BitmapFactory.decodeByteArray(data, 0, (data != null) ? data.length : 0);

				if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
					// Notice that width and height are reversed
					Bitmap scaled = Bitmap.createScaledBitmap(bm, screenHeight, screenWidth, true);
					int w = scaled.getWidth();
					int h = scaled.getHeight();
					// Setting post rotate to 90
					Matrix mtx = new Matrix();
					mtx.postRotate(90);
					// Rotating Bitmap
					bm = Bitmap.createBitmap(scaled, 0, 0, w, h, mtx, true);
				}else{// LANDSCAPE MODE
					//No need to reverse width and height
					Bitmap scaled = Bitmap.createScaledBitmap(bm, screenWidth,screenHeight , true);
					bm=scaled;
				}
				FileOutputStream out = null;
				File mediaStorageDir = new File(Environment.getExternalStorageDirectory().getPath() + "/MappApp/");
				try {
				    out = new FileOutputStream(mediaStorageDir + "tmpMap.png");
				    bm.compress(Bitmap.CompressFormat.PNG, 100, out); // bmp is your Bitmap instance
				    // PNG is a lossless format, the compression factor (100) is ignored
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
				Intent intent = new Intent(CameraActivity.this, CalibrationActivity.class);
				startActivity(intent);
//				((ImageView) findViewById(R.id.imageView1)).setImageBitmap(bm);
			}
			
		}
	};

	private static File getOutputMediaFile(int type){

		Boolean isSDPresent = android.os.Environment.getExternalStorageState().equals(android.os.Environment.MEDIA_MOUNTED);
		if(!isSDPresent)
		{	
			int duration = Toast.LENGTH_LONG;

			Toast toast = Toast.makeText(_context, "card not mounted", duration);
			toast.show();

			Log.d("ERROR", "Card not mounted");
		}
		File mediaStorageDir = new File(Environment.getExternalStorageDirectory().getPath() + "/MappApp/");

		if (! mediaStorageDir.exists()){
			if (! mediaStorageDir.mkdirs()){

				Log.d("MyCameraApp", "failed to create directory");
				return null;
			}
		}

		// Create a media file name
		File mediaFile;
		if (type == MEDIA_TYPE_IMAGE){
			mediaFile = new File(mediaStorageDir.getPath() + File.separator +
					"MapImg.jpg");
			imgFile = mediaFile;
		} else {
			return null;
		}

		return mediaFile;
	}
}
