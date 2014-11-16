package com.sbsw.mappapp.Utils;

import com.sbsw.mappapp.R;
import com.sbsw.mappapp.model.GpsPoint;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

public class Dot extends View {
	private static final float RADIUS = 20;
	private Bitmap bm;
	private float x = 30;
	private float y = 30;
	private GpsPoint point;
	private Paint myPaint;
	private boolean isLocked = false;

	public Dot(Context context, AttributeSet attrs) {
		super(context, attrs);
		myPaint = new Paint();
		myPaint.setAntiAlias(true);
		bm = BitmapFactory.decodeResource(context.getResources(),
                R.drawable.gps_icon_big);
		bm = Bitmap.createScaledBitmap(bm, 75, 75, false);
		
	}

	public boolean onTouchEvent(MotionEvent event) {
		int action = event.getAction();
		if(!isLocked) {
			switch (action) {
			case MotionEvent.ACTION_DOWN:
				x = event.getX();
				y = event.getY();
				break;
			case MotionEvent.ACTION_MOVE:
			case MotionEvent.ACTION_UP:
			case MotionEvent.ACTION_CANCEL:
				x = event.getX();
				y = event.getY();
				break;
			}
		}
		return (true);
	}
	
	 public void draw(Canvas canvas) {
		    canvas.drawBitmap(bm, x - bm.getWidth()/2, y - bm.getHeight()/2, myPaint);
		    invalidate();
	 }
	 
	 public float getX() {
		 return x;
	 }
	 
	 public float getY() {
		 return y;
	 }
	 
	 public void setX(float x) {
		 this.x = x;
	 }
	 
	 public void setY(float y) {
		 this.y = y;
	 }
	 
	public void lock() {
		isLocked = true;
	}
	 
	 
}
