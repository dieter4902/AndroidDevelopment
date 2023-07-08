package com.example.tilegames;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.example.tilegames.Minesweeper.MinesweeperSettings;
import com.example.tilegames.TicTacToe.TicTacToe;

public class MainActivity extends AppCompatActivity {
    public static final int NEW_GAME_CODE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        findViewById(R.id.minesweeper).setOnClickListener(view -> {
            Intent intent = new Intent(MainActivity.this, MinesweeperSettings.class);
            startActivityForResult(intent, NEW_GAME_CODE);
        });
        findViewById(R.id.tictactoe).setOnClickListener(view -> {
            Intent intent = new Intent(MainActivity.this, TicTacToe.class);
            startActivityForResult(intent, NEW_GAME_CODE);
        });
    }
}