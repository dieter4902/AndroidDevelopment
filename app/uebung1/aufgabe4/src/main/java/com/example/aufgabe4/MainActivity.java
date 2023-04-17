package com.example.aufgabe4;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        TextView formula = (TextView) findViewById(R.id.textView_formula);
        TextView result = (TextView) findViewById(R.id.textView_result);


        for (int i = 0; i < 10; i++) {//https://stackoverflow.com/questions/22639218/how-to-get-all-buttons-ids-in-one-time-on-android
            @SuppressLint("DiscouragedApi") int id = getResources().getIdentifier("button" + i, "id", getPackageName());
            ((Button) findViewById(id)).setOnClickListener(v -> formula.append(((TextView) v).getText().toString()));
        }


    }
}