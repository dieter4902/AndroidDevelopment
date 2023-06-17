package com.example.tracking_room;

import android.graphics.Color;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import org.alternativevision.gpx.beans.Route;

public class ShowRoute extends AppCompatActivity {

    public Route route;
    DrawView drawView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_route_single);
        drawView = findViewById(R.id.drawView2);
        drawView.setBackgroundColor(Color.WHITE);
        drawView.route = route;
        drawView.invalidate();
    }
}
