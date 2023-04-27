package com.example.gpsreciever;


import static android.Manifest.permission.ACCESS_COARSE_LOCATION;
import static android.Manifest.permission.ACCESS_FINE_LOCATION;

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

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

public class MainActivity extends AppCompatActivity {


    private LocationManager locationManager;
    private LocationListener locationListener;
    private String providerString;
    TextView latLng;
    TextView altitude;
    TextView speed;
    TextView provider;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        latLng = findViewById(R.id.textLatLng);
        altitude = findViewById(R.id.textAltitude);
        speed = findViewById(R.id.textSpeed);
        provider = findViewById(R.id.textProvider);
        providerString = LocationManager.NETWORK_PROVIDER;
        initializeLocationTracking();
        Spinner providerSpinner = findViewById(R.id.provider_spinner);
        providerSpinner.setAdapter(ArrayAdapter.createFromResource(this, R.array.providers, android.R.layout.simple_spinner_item));
        providerSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                providerString = ((TextView) view).getText().toString();
                //Log.d("changed provider", providerString);
                initializeLocationTracking();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });
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
            provider.setText(location.getProvider());
        };
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            String[] perms = {ACCESS_COARSE_LOCATION, ACCESS_FINE_LOCATION};
            ActivityCompat.requestPermissions(this, perms, 42);
        }
        locationManager.requestLocationUpdates(providerString, 0L, (float) 0, locationListener);// Register the listener with the Location Manager to receive location updates
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        // TODO handle if permission denied
        //quit("required permissions were not found");
    }
}
