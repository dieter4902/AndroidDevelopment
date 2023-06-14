package com.example.tracking_room;


import static android.Manifest.permission.ACCESS_COARSE_LOCATION;
import static android.Manifest.permission.ACCESS_FINE_LOCATION;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.google.gson.Gson;

import org.alternativevision.gpx.beans.Route;
import org.alternativevision.gpx.beans.Waypoint;

import java.time.Instant;
import java.util.Date;
import java.util.concurrent.atomic.AtomicReference;


public class RecordRoute extends AppCompatActivity {
    public static final String EXTRA_REPLY = "com.example.android.routelistsql.REPLY";
    private LocationManager locationManager;
    private LocationListener locationListener;
    private TextView trackingStatus;
    private Button trackingButton;
    private View.OnClickListener startTracking, stopTracking;
    protected static Route route;
    private boolean tracking;

    DrawView drawView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        route = new Route();
        setContentView(R.layout.activity_record);
        trackingButton = findViewById(R.id.trackButton);
        trackingStatus = findViewById(R.id.trackingstatus);
        drawView = findViewById(R.id.drawView);
        drawView.setBackgroundColor(Color.WHITE);


        startTracking = view -> {
            route = new Route();
            ((TextView) view).setText("recording location");
            trackingStatus.setText("tracking movement");
            view.setOnClickListener(stopTracking);
            tracking = true;
        };
        stopTracking = view -> {
            ((TextView) view).setText("recording location");
            trackingStatus.setText("saving movement");
            view.setOnClickListener(startTracking);
            tracking = false;
            savetracking();
            finish();
        };
        initializeLocationTracking();
    }

    private void savetracking() {
        if (!route.getRoutePoints().isEmpty()) {
            Intent replyIntent = new Intent();
            long start = route.getRoutePoints().get(0).getTime().getTime();
            long end = route.getRoutePoints().get(route.getRoutePoints().size()-1).getTime().getTime();
            final Waypoint[] pTmp = {null};
            AtomicReference<Double> distance = new AtomicReference<>((double) 0);
            route.getRoutePoints().forEach(p -> {
                if (pTmp[0] != null) {
                    distance.updateAndGet(v -> new Double((double) (v + pTmp[0].distanceTo(p))));
                }
                pTmp[0] = p;
            });
            com.example.tracking_room.Route route_t = new com.example.tracking_room.Route("test", start, end, route,distance.get() );
            replyIntent.putExtra(EXTRA_REPLY, new Gson().toJson(route_t));
            setResult(RESULT_OK, replyIntent);
        }
    }


    @SuppressLint("SetTextI18n")
    public void initializeLocationTracking() {
        try {
            locationManager.removeUpdates(locationListener);
        } catch (NullPointerException ignored) {
        }
        locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);// Acquire a reference to the system Location Manager
        locationListener = location -> {

            if (tracking) {
                Waypoint waypoint = new Waypoint();
                waypoint.setTime(Date.from(Instant.now()));
                waypoint.setLatitude(location.getLatitude());
                waypoint.setLongitude(location.getLongitude());
                waypoint.setElevation(location.getAltitude());

                route.addRoutePoint(waypoint);
                drawView.route = route;
                drawView.invalidate();
            }
        };
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            String[] perms = {ACCESS_COARSE_LOCATION, ACCESS_FINE_LOCATION};
            ActivityCompat.requestPermissions(this, perms, 42);
        }
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0L, (float) 0, locationListener);// Register the listener with the Location Manager to receive location updates
        trackingButton.setOnClickListener(startTracking);
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        // TODO handle if permission denied
        //quit("required permissions were not found");
    }
}
