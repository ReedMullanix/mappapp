package com.sbsw.mappapp.Utils;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

import com.sbsw.mappapp.model.GpsPoint;

import java.util.ArrayList;

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
		dotList = Map.getGPSPoints();
	}
	
	
	@Override
	public void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		dotList = Map.getGPSPoints();
        prev = dotList.get(0);
		for(GpsPoint p : dotList) {
			ScreenPointPair currXY = Map.convertToScreenCoords(p);
            ScreenPointPair prevXY = Map.convertToScreenCoords(prev);
            //Log.d("GPS UPDATE", "Converted XY" + currXY.getX() + " " + currXY.getY());
			canvas.drawCircle(currXY.getX(), currXY.getY(), 5, paint);
			canvas.drawLine(currXY.getX(), currXY.getY(), prevXY.getX(), prevXY.getY(), paint);
			prev = p;
		}
		this.invalidate();
	}
	
	
}
