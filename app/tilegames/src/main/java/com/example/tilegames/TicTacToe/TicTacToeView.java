package com.example.tilegames.TicTacToe;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;

import java.util.Objects;

public class TicTacToeView extends View {

    private final Paint paint;
    private final Paint playerOnePaint;
    private final Paint playerTwoPaint;
    private final Bitmap playerOne;
    private final Bitmap playerTwo;

    private boolean gameEnd = false;
    public static int rows = 3;
    static int tilewidth, tileHeight;
    String[] map;
    float strokeWidth = 50;
    String currentPlayer = "player1";

    public TicTacToeView(Context context, AttributeSet attrs) {
        super(context, attrs);
        paint = new Paint();
        paint.setColor(Color.BLACK);
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeCap(Paint.Cap.ROUND);
        paint.setStrokeWidth(strokeWidth);
        playerOnePaint = new Paint();
        playerOnePaint.setColor(Color.RED);
        playerOnePaint.setStyle(Paint.Style.STROKE);
        playerOnePaint.setStrokeCap(Paint.Cap.ROUND);
        playerOnePaint.setStrokeWidth(strokeWidth);
        playerTwoPaint = new Paint();
        playerTwoPaint.setColor(Color.BLUE);
        playerTwoPaint.setStyle(Paint.Style.STROKE);
        playerTwoPaint.setStrokeCap(Paint.Cap.ROUND);
        playerTwoPaint.setStrokeWidth(strokeWidth);
        playerOne = null;
        playerTwo = null;
        startNew();
        setBackgroundColor(Color.WHITE);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        tileHeight = getHeight() / rows;
        tilewidth = getWidth() / rows;
        drawTiles(canvas);

    }

    private void drawTiles(Canvas canvas) {
        float boxSize = canvas.getHeight() / 3.0f;
        float fieldSize = canvas.getHeight();
        float padding = strokeWidth / 2;
        canvas.drawLine(padding, boxSize, fieldSize - padding, boxSize, paint);
        canvas.drawLine(padding, boxSize * 2, fieldSize - padding, boxSize * 2, paint);
        canvas.drawLine(boxSize, padding, boxSize, fieldSize - padding, paint);
        canvas.drawLine(boxSize * 2, padding, boxSize * 2, fieldSize - padding, paint);
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < rows; j++) {
                String mapLocation = map[j * rows + i];
                if (mapLocation != null) {//if field was pressed
                    if (mapLocation.equals("player1")) {
                        canvas.drawLine(boxSize * i + padding * 3, boxSize * j + padding * 3, boxSize * (i + 1) - padding * 3, boxSize * (j + 1) - padding * 3, playerOnePaint);
                        canvas.drawLine(boxSize * i + padding * 3, boxSize * (j + 1) - padding * 3, boxSize * (i + 1) - padding * 3, boxSize * j + padding * 3, playerOnePaint);
                    } else if (mapLocation.equals("player2")) {
                        canvas.drawCircle(boxSize * (i + 0.5f), boxSize * (j + 0.5f), boxSize/2-padding*3, playerTwoPaint);
                    }
                }
            }
        }
    }


    int tX, tY;
    @SuppressLint("ClickableViewAccessibility")
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN && !gameEnd) {
            tX = ((int) event.getX()) / (tilewidth);
            tY = ((int) event.getY()) / (tileHeight);
            if (map[tY * rows + tX] == null)
                map[tY * rows + tX] = currentPlayer;
            currentPlayer = Objects.equals(currentPlayer, "player1") ? "player2" : "player1";
        }
        invalidate();
        return super.onTouchEvent(event);
    }

    private void stopGame() {
        gameEnd = true;
        setOnLongClickListener(null);
        setOnClickListener(null);
    }

    public void startNew() {
        makeToast("starting new Game");
        gameEnd = false;
        map = new String[rows * rows];
        invalidate();
    }

    public void makeToast(String message) {
        int duration = Toast.LENGTH_SHORT;
        Toast toast = Toast.makeText(this.getContext(), message, duration);
        toast.show();
    }

    private void checkWinningCondition() {
        makeToast("You Have Won!");
        stopGame();

    }
}