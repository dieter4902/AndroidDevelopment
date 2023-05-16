package com.example.gps_tracker;

import static android.Manifest.permission.ACCESS_COARSE_LOCATION;
import static android.Manifest.permission.ACCESS_FINE_LOCATION;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    private LocationManager locationManager;
    private LocationListener locationListener;
    TextView latLng;
    TextView altitude;
    TextView speed;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        latLng = findViewById(R.id.textLatLng);
        altitude = findViewById(R.id.textAltitude);
        speed = findViewById(R.id.textSpeed);
        initializeLocationTracking();
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
        };
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            String[] perms = {ACCESS_COARSE_LOCATION, ACCESS_FINE_LOCATION};
            ActivityCompat.requestPermissions(this, perms, 42);
        }
        locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0L, (float) 0, locationListener);// Register the listener with the Location Manager to receive location updates
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        // TODO handle if permission denied
        //quit("required permissions were not found");
    }
}
