package com.sbsw.mappapp.Utils;

import com.sbsw.mappapp.model.GpsPoint;
import com.sbsw.mappapp.model.GpsPointList;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

public class DrawView extends View {
	private Paint paint = new Paint();
	private GpsPoint last;
	private GpsPoint prevLast;
	//the algorithm is startPos.x + (new.lat - startPos.lat)*scale
	private double scale = 1;
	
	public DrawView(Context context, AttributeSet attrs) {
		super(context, attrs);
		last = GpsPointList.getInstance().read();
		prevLast = GpsPointList.getInstance().read();
		paint.setColor(Color.RED);
	}
	
	public void onDraw(Canvas canvas) {
		last = GpsPointList.getInstance().read();
		float[] lastXY = LatLonToXY.convert(last.getLongitude(), last.getLatitude());
		float[] prevLastXY = LatLonToXY.convert(prevLast.getLongitude(), prevLast.getLatitude());
		canvas.drawLine(lastXY[0], lastXY[1], prevLastXY[0], prevLastXY[1], paint);
		prevLast = last;
	}
}
