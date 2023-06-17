package com.example.tracking_room;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;

import com.google.gson.Gson;

import java.io.Serializable;

public class ShowRoute extends AppCompatActivity implements Serializable {

    public Route route;
    DrawView drawView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_route_single);
        Log.d("test","in show route");
        Intent intent = getIntent();
        route = new Gson().fromJson(intent.getStringExtra("route"), Route.class);
        drawView = findViewById(R.id.drawView2);
        drawView.notRecording = true;
        drawView.route = new Gson().fromJson(route.route, org.alternativevision.gpx.beans.Route.class);
    }
}
