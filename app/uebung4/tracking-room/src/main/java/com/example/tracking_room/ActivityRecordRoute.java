package com.example.tracking_room;


import static android.Manifest.permission.ACCESS_COARSE_LOCATION;
import static android.Manifest.permission.ACCESS_FINE_LOCATION;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;

import org.alternativevision.gpx.beans.Route;
import org.alternativevision.gpx.beans.Waypoint;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.atomic.AtomicReference;


public class ActivityRecordRoute extends AppCompatActivity {
    public static final String EXTRA_REPLY = "com.example.android.routelistsql.REPLY";
    private LocationManager locationManager;
    private LocationListener locationListener;
    private TextView trackingStatus;
    private Button trackingButton;
    private View.OnClickListener startTracking, stopTracking;
    protected static Route route;
    private boolean tracking;

    private static final int CAMERA_REQUEST = 1888;
    DrawView drawView;
    List<Long> poiIds;
    private com.example.tracking_room.Route activeRoute;

    long count;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_record);
        activeRoute = new com.example.tracking_room.Route();
        poiIds = new ArrayList<>();
        route = new Route();
        RecyclerView recyclerView = findViewById(R.id.recyclerview2);
        PoiListAdapter adapter = new PoiListAdapter(new PoiListAdapter.PoiDiff());
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        try {
            count = RouteRoomDatabase.getRouteCount()+1;
            if (count == 0) count = 1;
        } catch (NullPointerException e) {
            count = 1;
        }
        PoiViewModel mPoiViewModel = new PoiViewModel(getApplication());
        mPoiViewModel.getPois(count).observe(this, adapter::submitList);

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

        Button photoButton = findViewById(R.id.button1);
        //https://chat.openai.com/share/01252095-f0bc-48f9-8d36-07f14fb0c76d , https://stackoverflow.com/questions/5991319/capture-image-from-camera-and-display-in-activity
        photoButton.setOnClickListener(v -> {
            Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
            startActivityForResult(cameraIntent, CAMERA_REQUEST);
        });
    }

    private void savetracking() {
        if (!route.getRoutePoints().isEmpty()) {
            Intent replyIntent = new Intent();
            long start = route.getRoutePoints().get(0).getTime().getTime();
            long end = route.getRoutePoints().get(route.getRoutePoints().size() - 1).getTime().getTime();
            final Waypoint[] pTmp = {null};
            AtomicReference<Double> distance = new AtomicReference<>((double) 0);
            route.getRoutePoints().forEach(p -> {
                if (pTmp[0] != null) {
                    distance.updateAndGet(v -> v + pTmp[0].distanceTo(p));
                }
                pTmp[0] = p;
            });
            activeRoute.name = "run from " + Date.from(Instant.now());
            activeRoute.start = start;
            activeRoute.end = end;
            activeRoute.route = new Gson().toJson(route);
            activeRoute.distance = distance.get();
            activeRoute.poiIDs = new Gson().toJson(poiIds);

            replyIntent.putExtra(EXTRA_REPLY, new Gson().toJson(activeRoute));
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
                waypoint.setName(Date.from(Instant.now()) + "");
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

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CAMERA_REQUEST && resultCode == Activity.RESULT_OK) {
            Bitmap photo = (Bitmap) data.getExtras().get("data");

            // Save the image to a file
            File imageFile = saveImageToFile(photo);

            // Get the file path
            String imagePath = imageFile.getAbsolutePath();
            //create POI
            Waypoint poiWaypoint = route.getRoutePoints().get(route.getRoutePoints().size() - 1);
            Poi tmp = new Poi(count, poiWaypoint.getName(), new Gson().toJson(poiWaypoint), "empty", imagePath);
            RouteRoomDatabase.addPoi(tmp);
            poiIds.add(count);
            Log.d("e", "added poi for routeId " + count);
        }
    }

    private File saveImageToFile(Bitmap bitmap) {
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(new Date());
        String imageFileName = "IMG_" + timeStamp + ".jpg";

        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File imageFile = new File(storageDir, imageFileName);

        try {
            FileOutputStream outputStream = new FileOutputStream(imageFile);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream);
            outputStream.flush();
            outputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return imageFile;
    }
}
