package com.example.tilegames.Minesweeper;

import android.os.Bundle;
import android.os.SystemClock;
import android.widget.Chronometer;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.tilegames.R;

public class Minesweeper extends AppCompatActivity {
    Chronometer timer;
    TextView bombs;
    MinesweeperView game;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.minesweeper);
        bombs = findViewById(R.id.bombs);
        bombs.setText(((int)(MinesweeperView.rows * MinesweeperView.rows * MinesweeperView.bombRatio))+"");
        timer = findViewById(R.id.time);
        MinesweeperView.flags = findViewById(R.id.flags);
        MinesweeperView.timer = timer;
        findViewById(R.id.restartButton).setOnClickListener(view -> {
            game.startNew();
            timer.setBase(SystemClock.elapsedRealtime());
        });
        findViewById(R.id.backButton).setOnClickListener(view -> this.finish());
        game = findViewById(R.id.game);
        timer = findViewById(R.id.time);
        timer.start();
    }
}
