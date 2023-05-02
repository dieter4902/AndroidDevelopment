package com.example.activtiylivecycle;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        doToast("in onCreate");

    }

    @Override
    protected void onStart() {
        super.onStart();
        doToast("in onStart");
    }

    @Override
    protected void onResume() {
        super.onResume();
        doToast("in onResume");
    }

    @Override
    protected void onPause() {
        super.onPause();
        doToast("in onPause");
    }

    @Override
    protected void onStop() {
        super.onStop();
        doToast("in onStop");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        doToast("in onDestroy");
    }


    private void doToast(String message) {
        int duration = Toast.LENGTH_SHORT;
        Toast toast = Toast.makeText(this, message, duration);
        toast.show();
        Log.d("Event Debug", message);
    }
}