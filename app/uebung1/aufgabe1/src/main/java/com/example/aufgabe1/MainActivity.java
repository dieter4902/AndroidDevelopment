package com.example.aufgabe1;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        TextView text1 = findViewById(R.id.option1);
        TextView text2 = findViewById(R.id.option2);
        TextView text3 = findViewById(R.id.option3);
        TextView text4 = findViewById(R.id.option4);

        text1.setOnClickListener(view -> Log.d("MainActivity", "Textfeld 1 wurde ausgewählt"));
        text2.setOnClickListener(view -> Log.d("MainActivity", "Textfeld 2 wurde ausgewählt"));
        text3.setOnClickListener(view -> Log.d("MainActivity", "Textfeld 3 wurde ausgewählt"));
        text4.setOnClickListener(view -> Log.d("MainActivity", "Textfeld 4 wurde ausgewählt"));
    }
}