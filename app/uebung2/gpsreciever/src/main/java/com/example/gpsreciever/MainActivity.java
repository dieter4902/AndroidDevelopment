package com.example.gpsreciever;


import static android.Manifest.permission.ACCESS_COARSE_LOCATION;
import static android.Manifest.permission.ACCESS_FINE_LOCATION;

import android.annotation.SuppressLint;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

public class MainActivity extends AppCompatActivity {
    TextView gpsView;
    LocationManager locationManager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        gpsView = findViewById(R.id.gps_info);
        requestPermissions();


    }

    @SuppressLint("MissingPermission")
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        if (grantResults.length > 0
                && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            // permission was granted, yay!
            locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1000, 0, new LocationListener() {
                @SuppressLint("MissingPermission")
                @Override
                public void onLocationChanged(@NonNull Location location) {
                    updateLocation(location);
                }
            });
        } else {
            // permission denied, boo! Disable the
            // functionality that depends on this permission.
        }

        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

    }

    private void requestPermissions() {
        String[] perms = {ACCESS_COARSE_LOCATION, ACCESS_FINE_LOCATION};
        ActivityCompat.requestPermissions(this, perms, 42);
        //if (ActivityCompat.checkSelfPermission(this, ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
        //ActivityCompat.requestPermissions(this, perms, 42);
        //}
    }


    private void updateLocation(Location location) {
        double latitude = location.getLatitude();
        double longitude = location.getLongitude();

        Log.d("test2", "test");
        Log.d("updated lat", String.valueOf(latitude));
        Log.d("updated lng", String.valueOf(longitude));
        gpsView.setText(location.toString());
    }
}
