package com.example.tilegames;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

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
        findViewById(R.id.button2).setOnClickListener(view -> {
            Intent intent = new Intent(MainActivity.this, Sudoku.class);
            startActivityForResult(intent, NEW_GAME_CODE);
        });
        findViewById(R.id.button3).setOnClickListener(view -> {
            Intent intent = new Intent(MainActivity.this, TicTacToe.class);
            startActivityForResult(intent, NEW_GAME_CODE);
        });
    }
}