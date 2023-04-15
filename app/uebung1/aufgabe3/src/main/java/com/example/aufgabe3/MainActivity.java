package com.example.aufgabe3;

import static java.lang.Double.parseDouble;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        EditText input = findViewById(R.id.eingabeFeld);
        Button cf = findViewById(R.id.buttoncf);
        Button fc = findViewById(R.id.buttonfc);

        cf.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String eingabe = String.valueOf(input.getText());
                double parser = parseDouble(eingabe);
                double umgerechnet = parser * 9.0/5.0 + 32;
                input.setText(String.valueOf(umgerechnet));
            }


        });

        fc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String eingabe = String.valueOf(input.getText());
                double parser = parseDouble(eingabe);
                double umgerechnet =(parser -32.0) * 5.0/9.0;
                input.setText(String.valueOf(umgerechnet));
            }
        });

    }
}