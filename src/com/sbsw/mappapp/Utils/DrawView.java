package com.sbsw.mappapp.Utils;

import java.util.ArrayList;

import com.sbsw.mappapp.model.GpsPoint;
import com.sbsw.mappapp.model.GpsPointList;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;
import android.widget.Toast;

public class DrawView extends View {
	private Paint paint = new Paint();
	private ArrayList<GpsPoint> dotList;
	private GpsPoint prev;
	
	public DrawView(Context context, AttributeSet attrs) {
		super(context, attrs);
		paint.setColor(Color.rgb(255, 110, 110));
		paint.setStrokeWidth(5);
		setWillNotDraw(false);
		dotList = new ArrayList<GpsPoint>();
		dotList = GpsPointList.getInstance().read();
		prev = dotList.get(0);
	}
	
	
	public void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		for(GpsPoint p : dotList) {
			float[] lastXY = LatLonToXY.convert(p.getLongitude(), p.getLatitude());
			float[] prevXY = LatLonToXY.convert(prev.getLongitude(), prev.getLatitude());
			canvas.drawCircle(lastXY[0], lastXY[1], 5, paint);
			canvas.drawLine(lastXY[0], lastXY[1],prevXY[0], prevXY[1], paint);
			prev = p;
			
		}
		this.invalidate();
	}
	
	
}
