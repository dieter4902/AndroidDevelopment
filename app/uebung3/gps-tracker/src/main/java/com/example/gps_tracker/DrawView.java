package com.example.gps_tracker;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

public class DrawView extends View {
    Paint paint = new Paint();
    List<Node> nodeList;

    private void init() {
        nodeList = new ArrayList<>();
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
        if (nodeList.size() <= 1) {
        } else {

            float biglong = (float) nodeList.get(0).longitude;
            float biglat = (float) nodeList.get(0).latitude;
            float smallong = (float) nodeList.get(0).longitude;
            float smallat = (float) nodeList.get(0).latitude;
            for (Node node : nodeList) {
                biglong = (float) Math.max(biglong, node.longitude);
                biglat = (float) Math.max(biglat, node.latitude);
                smallong = (float) Math.min(smallong, node.longitude);
                smallat = (float) Math.min(smallat, node.latitude);
            }
            float longdif = biglong - smallong;
            float latdif = biglat - smallat;
            float scaleY = latdif / longdif;
            float scaleX = longdif / latdif;

            if (scaleX < scaleY) {
                scaleY = 1;
            } else scaleX = 1;

            float offsetX = getWidth() - getWidth() * scaleX;
            float offsetY = getHeight() - getHeight() * scaleY;

            for (int i = 1; i < nodeList.size(); i++) {
                float startY = ((((float) nodeList.get(i - 1).latitude) - smallat) / latdif - 1) * -1 * getWidth() * scaleY + offsetY / 2;
                float startX = (((float) nodeList.get(i - 1).longitude) - smallong) / longdif * getHeight() * scaleX + offsetX / 2;
                float stopY = ((((float) nodeList.get(i).latitude) - smallat) / latdif - 1) * -1 * getWidth() * scaleY + offsetY / 2;
                float stopX = (((float) nodeList.get(i).longitude) - smallong) / longdif * getHeight() * scaleX + offsetX / 2;
                canvas.drawLine(startX, startY, stopX, stopY, paint);
            }
        }
    }

    public void addNode(Node node) {
        nodeList.add(node);
    }

}