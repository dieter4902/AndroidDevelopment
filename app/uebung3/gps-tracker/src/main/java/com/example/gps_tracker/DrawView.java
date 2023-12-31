package com.example.gps_tracker;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import org.alternativevision.gpx.beans.Route;
import org.alternativevision.gpx.beans.Waypoint;

import java.util.ArrayList;
import java.util.List;

public class DrawView extends View {
    Paint paint = new Paint();


    private void init() {
        paint.setColor(Color.BLACK);
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(10);

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
        List<Waypoint> nodeList = MainActivity.route.getRoutePoints();
        if (nodeList == null || nodeList.size() <= 1) {
        } else {

            double biglong = nodeList.get(0).getLongitude();
            double biglat = nodeList.get(0).getLatitude();
            double smallong = nodeList.get(0).getLongitude();
            double smallat = nodeList.get(0).getLatitude();

            for (Waypoint node : nodeList) {
                biglong = Math.max(biglong, node.getLongitude());
                biglat = Math.max(biglat, node.getLatitude());
                smallong = Math.min(smallong, node.getLongitude());
                smallat = Math.min(smallat, node.getLatitude());
            }

            double longdif = biglong - smallong;
            double latdif = biglat - smallat;
            double scaleY = latdif / longdif;
            double scaleX = longdif / latdif;

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

            double offsetX = getWidth() - getWidth() * scaleX;
            double offsetY = getHeight() - getHeight() * scaleY;

            for (int i = 1; i < nodeList.size(); i++) {
                float startY = (float) ((((nodeList.get(i - 1).getLatitude()) - smallat) / latdif - 1) * -1 * getWidth() * scaleY + offsetY / 2);
                float startX = (float) (((nodeList.get(i - 1).getLongitude()) - smallong) / longdif * getHeight() * scaleX + offsetX / 2);
                float stopY = (float) ((((nodeList.get(i).getLatitude()) - smallat) / latdif - 1) * -1 * getWidth() * scaleY + offsetY / 2);
                float stopX = (float) (((nodeList.get(i).getLongitude()) - smallong) / longdif * getHeight() * scaleX + offsetX / 2);
                float ratio = i / (float) nodeList.size();
                int color = Color.rgb(1 - ratio, 1 - ratio, 1 - ratio);
                paint.setColor(color);
                canvas.drawLine(startX, startY, stopX, stopY, paint);
            }
        }
    }
}