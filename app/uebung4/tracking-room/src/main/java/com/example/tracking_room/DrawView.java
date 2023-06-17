package com.example.tracking_room;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import org.alternativevision.gpx.beans.Route;
import org.alternativevision.gpx.beans.Waypoint;

import java.util.List;

public class DrawView extends View {
    Paint paint = new Paint();
    Paint pointPaint = new Paint();
    Paint textpaint = new Paint();
    public Route route;
    public boolean notRecording;

    double smallat, latdif, scaleY, offsetY, smallong, longdif, scaleX, offsetX;


    private void init() {
        notRecording = false;
        paint.setColor(Color.BLACK);
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(10);
        pointPaint.setColor(Color.RED);
        textpaint.setColor(Color.BLACK);
        textpaint.setTextSize(50);

    }

    public DrawView(Context context) {
        super(context);
        init();
    }

    public DrawView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public DrawView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    @Override
    public void onDraw(Canvas canvas) {
        Log.d("test", "ind draw view");

        if (route == null) return;
        List<Waypoint> nodeList = route.getRoutePoints();
        if (nodeList == null || nodeList.size() <= 1) {
        } else {

            double biglong = nodeList.get(0).getLongitude();
            double biglat = nodeList.get(0).getLatitude();
            smallong = nodeList.get(0).getLongitude();
            smallat = nodeList.get(0).getLatitude();

            for (Waypoint node : nodeList) {
                biglong = Math.max(biglong, node.getLongitude());
                biglat = Math.max(biglat, node.getLatitude());
                smallong = Math.min(smallong, node.getLongitude());
                smallat = Math.min(smallat, node.getLatitude());
            }

            longdif = biglong - smallong;
            latdif = biglat - smallat;
            scaleY = latdif / longdif;
            scaleX = longdif / latdif;

            float xGrid = getWidth() / 5.0f;
            float yGrid = getHeight() / 5.0f;

            for (int i = 0; i <= 5; i++) {
                paint.setColor(Color.BLUE);
                canvas.drawLine(0, xGrid * i, getHeight(), xGrid * i, paint);
                canvas.drawLine(yGrid * i, 0, yGrid * i, getWidth(), paint);
            }

            if (scaleX < scaleY) {
                scaleY = 1;
            } else scaleX = 1;

            offsetX = getWidth() - getWidth() * scaleX;
            offsetY = getHeight() - getHeight() * scaleY;

            for (int i = 1; i < nodeList.size(); i++) {
                float startY = convertLat(nodeList.get(i - 1).getLatitude());
                float startX = convertLon(nodeList.get(i - 1).getLongitude());
                float stopY = convertLat(nodeList.get(i).getLatitude());
                float stopX = convertLon(nodeList.get(i).getLongitude());
                float ratio = i / (float) nodeList.size();
                int color = Color.rgb(1 - ratio, 1 - ratio, 1 - ratio);
                paint.setColor(color);
                canvas.drawLine(startX, startY, stopX, stopY, paint);
            }
            drawPoint(canvas,nodeList.get(0),"start");
            if (notRecording) {
                drawPoint(canvas,nodeList.get(nodeList.size() - 1),"finish");
            }else {
                drawPoint(canvas,nodeList.get(nodeList.size() - 1),"location");
            }
        }
    }

    public void drawPoint(Canvas canvas,Waypoint point, String label){
        canvas.drawCircle(convertLon(point.getLongitude()),convertLat(point.getLatitude()),15,pointPaint);
        canvas.drawText(label,convertLon(point.getLongitude()),convertLat(point.getLatitude()),textpaint);
    }

    public float convertLat(double lat) {
        return (float) (((lat - smallat) / latdif - 1) * -1 * getWidth() * scaleY + offsetY / 2);
    }

    public float convertLon(double lon) {
        return (float) ((lon - smallong) / longdif * getHeight() * scaleX + offsetX / 2);
    }
}