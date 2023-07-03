package com.example.tilegames;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

import com.example.tilegames.views.MinesweeperView;

public class MainActivity extends AppCompatActivity {
    public static final int NEW_GAME_CODE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        findViewById(R.id.button).setOnClickListener(view -> {
            Intent intent = new Intent(MainActivity.this, Minesweeper.class);
            startActivityForResult(intent, NEW_GAME_CODE);
        });
        EditText difficulty  = findViewById(R.id.difficulty);
        difficulty.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }
            @Override
            public void afterTextChanged(Editable editable) {
                if (Float.parseFloat(editable.toString())>75f){
                    difficulty.setText("75");
                }else if (Float.parseFloat(editable.toString())<1f){
                    difficulty.setText("1");
                }
                MinesweeperView.bombRatio =Float.parseFloat(String.valueOf(difficulty.getText()))/100;
            }
        });

        EditText size  = findViewById(R.id.size);
        size.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }
            @Override
            public void afterTextChanged(Editable editable) {
                if (Float.parseFloat(editable.toString())>50){
                    size.setText("50");
                }else if (Float.parseFloat(editable.toString())<3){
                    size.setText("3");
                }
                MinesweeperView.rows =Integer.parseInt(String.valueOf(size.getText()));
                MinesweeperView.columns =Integer.parseInt(String.valueOf(size.getText()));
            }
        });

    }
}