package com.example.hoehenmesser;

import static android.hardware.Sensor.TYPE_PRESSURE;

import android.app.Service;
import android.content.Context;
import android.content.SharedPreferences;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity implements SensorEventListener {

    private SensorManager sensorManager;
    private TextView pressureText;
    private TextView altitudeText;
    private TextView pressureOffsetText;
    float pressure;
    int pressureOffset;
    SharedPreferences sharedPref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        sharedPref = this.getSharedPreferences(getString(R.string.preference_file_key), Context.MODE_PRIVATE);//https://developer.android.com/training/data-storage/shared-preferences
        pressureOffset = sharedPref.getInt(getString(R.string.pressureOffset), 0);

        pressureText = findViewById(R.id.pressuretext);
        altitudeText = findViewById(R.id.altitudetext);
        pressureOffsetText = findViewById(R.id.pressureOffset);
        pressureOffsetText.setText(pressureOffset + "hPa");

        sensorManager = (SensorManager) getSystemService(Service.SENSOR_SERVICE);
        sensorManager.registerListener(this,
                sensorManager.getDefaultSensor(Sensor.TYPE_PRESSURE),
                SensorManager.SENSOR_DELAY_NORMAL);
        findViewById(R.id.buttonAdd).setOnClickListener(view -> changeOffset(1));
        findViewById(R.id.buttonSub).setOnClickListener(view -> changeOffset(-1));
    }

    private void changeOffset(float n) {
        pressureOffset += n;
        pressureOffsetText.setText(pressureOffset + "hPa");

        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putInt(getString(R.string.pressureOffset), pressureOffset);
        editor.apply();
    }


    @Override//https://stackoverflow.com/questions/43245827/pressure-sensor-not-getting-any-values
    public void onSensorChanged(SensorEvent event) {
        if (event.sensor.getType() == Sensor.TYPE_PRESSURE) {
            pressure = event.values[0];
            updateText(pressure + pressureOffset);

        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
        Log.i("Baro", " Accuracy " + accuracy);
    }

    private void updateText(float pressure) {

        pressureText.setText(pressure + "hPa");
        float altitude = SensorManager.getAltitude(SensorManager.PRESSURE_STANDARD_ATMOSPHERE, pressure);
        altitudeText.setText(altitude + "");
    }
}