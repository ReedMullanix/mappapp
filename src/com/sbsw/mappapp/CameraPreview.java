package com.sbsw.mappapp;

import java.io.IOException;

import android.content.Context;
import android.hardware.Camera;
import android.hardware.Camera.Parameters;
import android.util.Log;
import android.view.Display;
import android.view.Surface;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.WindowManager;

public class CameraPreview extends SurfaceView implements SurfaceHolder.Callback {
	private SurfaceHolder _holder;
	private Camera _camera;

	public CameraPreview(Context context, Camera camera) {
		super(context);
		_camera = camera;

		// Install a SurfaceHolder.Callback so we get notified when the
		// underlying surface is created and destroyed.
		_holder = getHolder();
		_holder.addCallback(this);
		// deprecated setting, but required on Android versions prior to 3.0
		_holder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
	}

	@Override
	public void surfaceCreated(SurfaceHolder holder) {
		setWillNotDraw(false);
		_camera.setDisplayOrientation(90);
		previewCamera();
	}

	@Override
	public void surfaceChanged(SurfaceHolder holder, int format, int width,
			int height) {

		if(_holder == null) {
			//Our Preview Doesnt Exist!
			return;
		}

		try {
			//Stop our camera before we make changes!
			_camera.stopPreview();
		} catch (Exception e){
			// ignore: tried to stop a non-existent preview
		}

		previewCamera();        

	}

	public void previewCamera()
	{        
		try 
		{           
			_camera.setPreviewDisplay(_holder);          
			_camera.startPreview();
		}
		catch(Exception e)
		{

		}
	}

	@Override
	public void surfaceDestroyed(SurfaceHolder holder) {
		// TODO Auto-generated method stub

	}
}
