package com.example.gps_tracker;

import static android.Manifest.permission.ACCESS_COARSE_LOCATION;
import static android.Manifest.permission.ACCESS_FINE_LOCATION;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.provider.SyncStateContract;
import android.util.Log;
import android.view.SurfaceView;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.SearchView;
import android.widget.Spinner;
import android.widget.TextView;

import com.opencsv.CSVWriter;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private LocationManager locationManager;
    private LocationListener locationListener;
    TextView latLng;
    TextView altitude;
    TextView speed;
    TextView trackingStatus;
    Button trackingButton;
    View.OnClickListener startTracking;
    View.OnClickListener stopTracking;

    boolean tracking;

    private List<Node> trackingList;
    DrawView drawView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        latLng = findViewById(R.id.textLatLng);
        altitude = findViewById(R.id.textAltitude);
        speed = findViewById(R.id.textSpeed);
        trackingButton = findViewById(R.id.trackButton);
        trackingStatus = findViewById(R.id.trackingstatus);
        drawView= findViewById(R.id.drawView);
        drawView.setBackgroundColor(Color.WHITE);


        startTracking = view -> {
            trackingList = new ArrayList<>();
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
        };


        initializeLocationTracking();
    }

    private void savetracking() {
        try {
            File file = new File("/storage/emulated/0/Download/tracking.csv");

            if (!file.exists()) {
                file.createNewFile();
            }

            FileWriter fw = new FileWriter(file.getAbsoluteFile());
            BufferedWriter bw = new BufferedWriter(fw);
            for (Node trackingNode : trackingList) {
                bw.write(trackingNode.toStringArr());
            }
            bw.close();
            Log.d("filewrite", file.getAbsolutePath());

        } catch (IOException e) {
            e.printStackTrace();
        }
        trackingStatus.setText("ready to track");
    }


    @SuppressLint("SetTextI18n")
    public void initializeLocationTracking() {
        try {
            locationManager.removeUpdates(locationListener);
        } catch (NullPointerException ignored) {
        }
        locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);// Acquire a reference to the system Location Manager
        locationListener = location -> {// Define a listener that responds to location updates

            latLng.setText(location.getLatitude() + " " + location.getLongitude());
            altitude.setText(Double.toString(location.getAltitude()));
            speed.setText(Double.toString(location.getSpeed()));
            if (tracking) {
                Log.d("tracking", "added node");
                Node node= new Node(LocalDateTime.now(), location.getLatitude(), location.getLongitude(), location.getAltitude());
                drawView.addNode(node);
                drawView.invalidate();
                trackingList.add(node);
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
