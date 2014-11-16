package com.sbsw.mappapp;


import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

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
import android.view.View;
import android.widget.FrameLayout;

public class CameraActivity extends Activity {
	private static Camera _camera;
	private static CameraPreview _preview;
	private static Context _context;

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
<<<<<<< HEAD

	protected void onResume() {
		super.onResume();
		if(_camera == null) {
			_camera = getCameraInstance();
			Camera.Parameters params = _camera.getParameters();
			params.setRotation(90);
			_camera.setParameters(params);
			_context = getApplicationContext();
			_preview = new CameraPreview(_context, _camera);
			FrameLayout previewLayout = (FrameLayout) findViewById(R.id.camera_preview);
			previewLayout.addView(_preview);
		}
	}
=======
	

>>>>>>> ef1bbcf12c49933510be3524c251742461720aa6


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
			}

		}
	};
}
