package com.example.tilegames.TicTacToe;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.example.tilegames.R;


public class TicTacToe extends AppCompatActivity {
    TicTacToeView game;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tictactoe);
        findViewById(R.id.restartButton).setOnClickListener(view -> {
            game.startNew();
        });
        findViewById(R.id.backButton).setOnClickListener(view -> this.finish());
        game = findViewById(R.id.game);
    }
}
