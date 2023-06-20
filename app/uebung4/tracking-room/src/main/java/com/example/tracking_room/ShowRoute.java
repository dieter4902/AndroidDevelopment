package com.example.tracking_room;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;

import java.io.Serializable;

public class ShowRoute extends AppCompatActivity implements Serializable {

    public Route route;
    DrawView drawView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_route_single);
        Intent intent = getIntent();
        route = new Gson().fromJson(intent.getStringExtra("route"), Route.class);
        Log.d("test",route.id+"");
        drawView = findViewById(R.id.drawView2);
        drawView.notRecording = true;
        drawView.route = new Gson().fromJson(route.route, org.alternativevision.gpx.beans.Route.class);

        RecyclerView recyclerView = findViewById(R.id.recyclerview2);
        PoiListAdapter adapter = new PoiListAdapter(new PoiListAdapter.PoiDiff());
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        PoiViewModel mPoiViewModel = new PoiViewModel(getApplication());
        mPoiViewModel.getPois(route.id).observe(this, adapter::submitList);
    }
}
