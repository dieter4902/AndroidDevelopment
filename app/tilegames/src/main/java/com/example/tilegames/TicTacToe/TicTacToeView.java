package com.example.tilegames.TicTacToe;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;

import java.util.Objects;

public class TicTacToeView extends View {

    private final Paint paint;
    private final Paint textPaint;
    private final Paint bannerPaint;
    private final Paint playerOnePaint;
    private final Paint playerTwoPaint, winningPaint;

    private boolean gameEnd = false;
    public static int rows = 3;
    static int tilewidth, tileHeight;
    String[] map;
    float strokeWidth = 50;
    String currentPlayer;
    String message;

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
        textPaint = new Paint();
        textPaint.setColor(Color.BLACK);
        textPaint.setTextSize(100);
        textPaint.setTextAlign(Paint.Align.CENTER);
        bannerPaint = new Paint();
        bannerPaint.setColor(Color.GRAY);
        bannerPaint.setAlpha(255 / 100 * 80);
        winningPaint = new Paint();
        winningPaint.setColor(Color.GREEN);
        winningPaint.setAlpha(200);
        winningPaint.setStyle(Paint.Style.STROKE);
        winningPaint.setStrokeCap(Paint.Cap.ROUND);
        winningPaint.setStrokeWidth(strokeWidth);
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
                        canvas.drawCircle(boxSize * (i + 0.5f), boxSize * (j + 0.5f), boxSize / 2 - padding * 3, playerTwoPaint);
                    }
                }
            }
        }
        if (a != null && b != null) {
            float e = boxSize / 2;
            int sx = a % 3;
            int sy = (a - sx) / 3;
            int ex = b % 3;
            int ey = (b - ex) / 3;
            Log.d("line", sx + " " + sy + ":" + ex + " " + ey);
            canvas.drawLine(e + sx * boxSize, e + sy * boxSize, e + ex * boxSize, e + ey * boxSize, winningPaint);
        }
        if (message != null) {
            canvas.drawRect(padding * 3, boxSize * 1.25f, fieldSize - padding * 3, boxSize * 1.75f, bannerPaint);
            canvas.drawText(message, fieldSize / 2, fieldSize / 2 + textPaint.getTextSize() / 3f, textPaint);
        }
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN && !gameEnd) {
            int tX = ((int) event.getX()) / (tilewidth);
            int tY = ((int) event.getY()) / (tileHeight);
            if (map[tY * rows + tX] == null) {
                map[tY * rows + tX] = currentPlayer;
                currentPlayer = Objects.equals(currentPlayer, "player1") ? "player2" : "player1";
                checkWinningCondition();
            }
        } else if (event.getAction() == MotionEvent.ACTION_DOWN) {
            startNew();
        }
        invalidate();
        return super.onTouchEvent(event);
    }

    private void stopGame(String message) {
        this.message = message;
        makeToast(message);
        gameEnd = true;
    }

    public void startNew() {
        message = null;
        a = null;
        b = null;
        makeToast("starting new Game");
        gameEnd = false;
        currentPlayer = "player1";
        map = new String[rows * rows];
        invalidate();
    }

    public void makeToast(String message) {
        int duration = Toast.LENGTH_SHORT;
        Toast toast = Toast.makeText(this.getContext(), message, duration);
        toast.show();
    }

    private void checkWinningCondition() {
        if (checkIndicees(0, 1, 2) || checkIndicees(3, 4, 5) || checkIndicees(6, 7, 8) ||//horizontal
                checkIndicees(0, 3, 6) || checkIndicees(1, 4, 7) || checkIndicees(2, 5, 8) ||//vertical
                checkIndicees(0, 4, 8) || checkIndicees(2, 4, 6)) {//diagonal
            stopGame((Objects.equals(currentPlayer, "player1") ? "Player One" : "Player Two") + " Won!");
            return;
        }
        for (String e : map) {
            if (e == null) return;
        }
        stopGame("Tie!");
    }

    Integer a, b = null;

    private void markWinningLine(int x, int y) {
        a = x;
        b = y;
    }

    private boolean checkIndicees(int x, int y, int z) {
        boolean result = map[x] != null && Objects.equals(map[x], map[y]) && Objects.equals(map[y], map[z]);
        if (result) {
            markWinningLine(x, z);
        }
        return result;
    }
}