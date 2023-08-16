package com.example.tracking_room;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.gson.Gson;

public class MainActivity extends AppCompatActivity {

    private RouteViewModel mRouteViewModel;
    public static final int NEW_ROUTE_ACTIVITY_REQUEST_CODE = 1;
    public static MainActivity main;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        main = this;
        RecyclerView recyclerView = findViewById(R.id.recyclerview2);
        final RouteListAdapter adapter = new RouteListAdapter(new RouteListAdapter.RouteDiff());
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRouteViewModel = new ViewModelProvider(this).get(RouteViewModel.class);
        // Update the cached copy of the routes in the adapter.
        mRouteViewModel.getAllRoutes().observe(this, adapter::submitList);
        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(view -> {
            Intent intent = new Intent(MainActivity.this, ActivityRecordRoute.class);
            startActivityForResult(intent, NEW_ROUTE_ACTIVITY_REQUEST_CODE);
        });



    }

    public void startActivity(Route route){

        Intent intent = new Intent(MainActivity.this, ActivityShowRoute.class);
        intent.putExtra("route", new Gson().toJson(route));
        startActivityForResult(intent, NEW_ROUTE_ACTIVITY_REQUEST_CODE);
    }
    public void startActivity(Poi poi){

        Intent intent = new Intent(MainActivity.this, ActivityPoiDetails.class);
        intent.putExtra("poi", new Gson().toJson(poi));
        startActivityForResult(intent, NEW_ROUTE_ACTIVITY_REQUEST_CODE);
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.d("onActivity", resultCode + "      " + resultCode + "       " + data);
        if (requestCode == NEW_ROUTE_ACTIVITY_REQUEST_CODE && resultCode == RESULT_OK) {
            Route route = new Gson().fromJson(data.getStringExtra(ActivityRecordRoute.EXTRA_REPLY), Route.class);
            mRouteViewModel.insert(route);
        } else {
            Toast.makeText(
                    getApplicationContext(),
                    R.string.empty_not_saved,
                    Toast.LENGTH_LONG).show();
        }
    }

}