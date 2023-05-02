package com.example.hoehenmesser;

import static android.hardware.Sensor.TYPE_PRESSURE;

import android.app.Service;
import android.content.Context;
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
    float pressure;
    float pressureOffset;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        pressureText = findViewById(R.id.pressuretext);
        altitudeText = findViewById(R.id.altitudetext);
        sensorManager = (SensorManager) getSystemService(Service.SENSOR_SERVICE);
        sensorManager.registerListener(this,
                sensorManager.getDefaultSensor(Sensor.TYPE_PRESSURE),
                SensorManager.SENSOR_DELAY_NORMAL);
        findViewById(R.id.buttonAdd).setOnClickListener(view -> changeOffset(1));
        findViewById(R.id.buttonSub).setOnClickListener(view -> changeOffset(-1));
    }

    private void changeOffset(float n) {
        pressureOffset += n;
        ((TextView) findViewById(R.id.pressureOffset)).setText(pressureOffset + "hPa");
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