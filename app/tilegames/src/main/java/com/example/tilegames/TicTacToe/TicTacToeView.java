package com.example.tilegames.TicTacToe;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;

import java.util.Objects;

public class TicTacToeView extends View {

    private final Paint paint;
    private final Bitmap playerOne;
    private final Bitmap playerTwo;

    private boolean gameEnd = false;
    public static int rows = 3;
    static int tilewidth, tileHeight;
    String[] map;

    public TicTacToeView(Context context, AttributeSet attrs) {
        super(context, attrs);
        paint = new Paint();
        playerOne = null;
        playerTwo = null;
        startNew();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        tileHeight = getHeight() / rows;
        tilewidth = getWidth() / rows;
        drawTiles(canvas);

    }

    private void drawTiles(Canvas canvas) {
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < rows; j++) {
                Bitmap bm = null;
                String mapLocation = map[j * rows + i];
                if (mapLocation != null) {//if field was pressed
                    if (mapLocation.equals("player1")) {
                        bm = playerOne;
                    } else if (mapLocation.equals("player2")) {
                        bm = playerTwo;
                    } else continue;
                }
                if (bm == null) continue;
                Rect src = new Rect(0, 0, bm.getWidth() - 1, bm.getHeight() - 1);
                Rect dest = new Rect((i) * tilewidth, (j) * tileHeight, (i + 1) * tilewidth, (j + 1) * tileHeight);
                canvas.drawBitmap(bm, src, dest, paint);
            }
        }
    }


    int tX, tY;
    boolean longClickFlag;

    private class LongClick implements OnLongClickListener {
        @Override
        public boolean onLongClick(View view) {
            if (!Objects.equals(map[tY * rows + tX], "clicked")) {
                longClickFlag = true;
            }
            return false;
        }
    }

    private class ShortClick implements OnClickListener {
        @Override
        public void onClick(View view) {
            if (longClickFlag) {
                longClickFlag = false;
                if (Objects.equals(map[tY * rows + tX], "flag"))
                    map[tY * rows + tX] = "unknown";
                else
                    map[tY * rows + tX] = "flag";

            } else
                recursiveClick(tX, tY);
            invalidate();
        }
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN && !gameEnd) {
            tX = ((int) event.getX()) / (tilewidth);
            tY = ((int) event.getY()) / (tileHeight);
            if (map[tY * rows + tX] == null || !Objects.equals(map[tY * rows + tX], "clicked"))
                map[tY * rows + tX] = "pressed";//only for animation
        }
        invalidate();
        return super.onTouchEvent(event);
    }

    private void recursiveClick(int x, int y) {
        int index = y * rows + x;
        if (x < 0 || y < 0 || x > rows - 1 || y > rows - 1 || Objects.equals(map[index], "clicked")) {//if out of bounds or already clicked, skip
            return;
        }
        map[index] = "clicked";
        checkWinningCondition();
    }

    private void stopGame() {
        gameEnd = true;
        setOnLongClickListener(null);
        setOnClickListener(null);
    }

    public void startNew() {
        makeToast("starting new Game");
        gameEnd = false;
        setOnLongClickListener(new LongClick());
        setOnClickListener(new ShortClick());
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