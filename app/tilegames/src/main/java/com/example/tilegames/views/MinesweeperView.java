package com.example.tilegames.views;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import com.example.tilegames.R;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

public class MinesweeperView extends View {

    private final Paint paint;
    private final Bitmap[] numbers;
    private final Bitmap flag, mine, unknown, exploded, pressed;

    private boolean gameEnd = false;
    static int rows = 10, columns = 10, tilewidth, tileHeight;
    static float bombRatio = 0.05f;
    List<Boolean> minefield;
    String[] map;

    public MinesweeperView(Context context, AttributeSet attrs) {
        super(context, attrs);

        setOnLongClickListener(new LongClick());
        setOnClickListener(new ShortClick());

        map = new String[rows * columns];
        minefield = new ArrayList<>();

        for (int i = 0; i < (rows * columns) * bombRatio; i++) minefield.add(true);
        for (int i = 0; i < (rows * columns) * (1 - bombRatio); i++) minefield.add(false);
        Collections.shuffle(minefield);

        paint = new Paint();

        numbers = new Bitmap[9];
        numbers[0] = BitmapFactory.decodeResource(getResources(), R.drawable.tileempty);
        numbers[1] = BitmapFactory.decodeResource(getResources(), R.drawable.tile1);
        numbers[2] = BitmapFactory.decodeResource(getResources(), R.drawable.tile2);
        numbers[3] = BitmapFactory.decodeResource(getResources(), R.drawable.tile3);
        numbers[4] = BitmapFactory.decodeResource(getResources(), R.drawable.tile4);
        numbers[5] = BitmapFactory.decodeResource(getResources(), R.drawable.tile5);
        numbers[6] = BitmapFactory.decodeResource(getResources(), R.drawable.tile6);
        numbers[7] = BitmapFactory.decodeResource(getResources(), R.drawable.tile7);
        numbers[8] = BitmapFactory.decodeResource(getResources(), R.drawable.tile8);
        flag = BitmapFactory.decodeResource(getResources(), R.drawable.tileflag);
        mine = BitmapFactory.decodeResource(getResources(), R.drawable.tilemine);
        unknown = BitmapFactory.decodeResource(getResources(), R.drawable.tileunknown);
        exploded = BitmapFactory.decodeResource(getResources(), R.drawable.tileexploded);
        Matrix matrix = new Matrix();
        matrix.postRotate(90);
        pressed = Bitmap.createBitmap(unknown, 0, 0, unknown.getWidth(), unknown.getHeight(), matrix, true);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        tileHeight = getHeight() / rows;
        tilewidth = getWidth() / columns;
        drawTiles(canvas);
    }

    private void drawTiles(Canvas canvas) {
        for (int i = 0; i < columns; i++) {
            for (int j = 0; j < rows; j++) {
                Bitmap bm = unknown;
                String mapLocation = map[j * columns + i];
                if (mapLocation != null) {//if field was pressed
                    if (mapLocation.equals("flag")) {
                        bm = flag;
                    } else if (mapLocation.equals("pressed")) {
                        bm = pressed;
                    } else if (mapLocation.equals("bomb")) {
                        bm = mine;
                    } else if (mapLocation.equals("clicked") && minefield.get(j * columns + i)) {
                        bm = exploded;
                    } else {
                        bm = numbers[calculateContent(i, j)];
                    }
                }
                Rect src = new Rect(0, 0, bm.getWidth() - 1, bm.getHeight() - 1);
                Rect dest = new Rect((i) * tilewidth, (j) * tileHeight, (i + 1) * tilewidth, (j + 1) * tileHeight);
                canvas.drawBitmap(bm, src, dest, paint);
            }
        }
    }

    private int calculateContent(int i, int j) {
        int xstart = -1, ystart = -1;
        int xend = 1, yend = 1;
        if (i == 0) xstart = 0;
        if (j == 0) ystart = 0;
        if (i == columns - 1) xend = 0;
        if (j == rows - 1) yend = 0;

        int count = 0;
        for (int y = j + ystart; y < j + yend + 1; y++) {
            for (int x = i + xstart; x < i + xend + 1; x++) {
                if (minefield.get(y * columns + x))
                    count++;
            }
        }
        return count;
    }


    int tX, tY;
    boolean longClickFlag;

    private class LongClick implements OnLongClickListener {
        @Override
        public boolean onLongClick(View view) {
            if (!Objects.equals(map[tY * columns + tX], "clicked")) {
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
                map[tY * columns + tX] = "flag";
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
            if (map[tY * columns + tX] == null || !Objects.equals(map[tY * columns + tX], "clicked"))
                map[tY * columns + tX] = "pressed";//only for animation
        }
        invalidate();
        return super.onTouchEvent(event);
    }

    private void recursiveClick(int x, int y) {
        int index = y * columns + x;
        if (x < 0 || y < 0 || x > columns - 1 || y > rows - 1 || Objects.equals(map[index], "clicked")) {//if out of bounds or already clicked, skip
            return;
        }
        map[index] = "clicked";
        if (calculateContent(x, y) == 0) {
            recursiveClick(x - 1, y);
            recursiveClick(x + 1, y);
            recursiveClick(x, y - 1);
            recursiveClick(x, y + 1);
        }
        if (minefield.get(index)) {
            gameOver(index);
        }

    }

    private void gameOver(int index) {
        gameEnd = true;
        for (int n = 0; n < rows * columns; n++) {
            if (n != index && minefield.get(n)) {
                map[n] = "bomb";
            }
        }
        setOnLongClickListener(null);
        setOnClickListener(null);
    }
}