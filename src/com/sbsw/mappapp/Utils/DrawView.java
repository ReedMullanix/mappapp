package com.sbsw.mappapp.Utils;

import com.sbsw.mappapp.model.GpsPoint;
import com.sbsw.mappapp.model.GpsPointList;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.view.View;

public class DrawView extends View {
	private Paint paint = new Paint();
	private GpsPoint last;
	private GpsPoint prevLast;
	//the algorithm is startPos.x + (new.lat - startPos.lat)*scale
	private double scale = 1;
	
	public DrawView(Context context) {
		super(context);
		last = GpsPointList.getInstance().read();
		prevLast = GpsPointList.getInstance().read();
		paint.setColor(Color.RED);
	}
	
	public void onDraw(Canvas canvas) {
		//canvas.drawLine(startX, startY, stopX, stopY, paint);
	}
}
