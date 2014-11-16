package com.sbsw.mappapp.Utils;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

public class Dot extends View {
	private static final float RADIUS = 20;
	private float x = 30;
	private float y = 30;
	private float initialX;
	private float initialY;
	private float offsetX;
	private float offsetY;
	private Paint myPaint;
	private boolean isLocked = false;

	public Dot(Context context, AttributeSet attrs) {
		super(context, attrs);

		myPaint = new Paint();
		myPaint.setColor(Color.RED);
		myPaint.setAntiAlias(true);
	}

	public boolean onTouchEvent(MotionEvent event) {
		int action = event.getAction();
		if(!isLocked) {
			switch (action) {
			case MotionEvent.ACTION_DOWN:
//				initialX = x;
//				initialY = y;
//				offsetX = event.getX();
//				offsetY = event.getY();
				x = event.getX();
				y = event.getY();
				break;
			case MotionEvent.ACTION_MOVE:
			case MotionEvent.ACTION_UP:
			case MotionEvent.ACTION_CANCEL:
				//x = initialX + event.getX() - offsetX;
				//y = initialY + event.getY() - offsetY;
				x = event.getX();
				y = event.getY();
				break;
			}
		}
		return (true);
	}
	
	 public void draw(Canvas canvas) {
		    int width = canvas.getWidth();
		    int height = canvas.getHeight();
		    canvas.drawCircle(x, y, RADIUS, myPaint);
		    invalidate();
	 }

	public void lock() {
		isLocked = true;
	}
	 
	 
}
