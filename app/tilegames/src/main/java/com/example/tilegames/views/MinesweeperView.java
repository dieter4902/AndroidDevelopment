package com.example.tilegames.views;

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


/*
 * Created by mariocsee on 9/28/16.
 */

public class MinesweeperView extends View {

    private final Paint paint;
    private final Bitmap[] numbers;
    private Bitmap flag, mine, unknown, empty, exploded, pressed;

    private boolean gameEnd = false;
    int rows, columns, tilewidth, tileHeight;
    List<Boolean> minefield;
    String[] map;

    public MinesweeperView(Context context, AttributeSet attrs) {
        super(context, attrs);

        setOnLongClickListener(new LongClick());
        setOnClickListener(new ShortClick());

        rows = 10;
        columns = 10;
        float bombRatio = 0.05f;
        minefield = new ArrayList<>();

        for (int i = 0; i < (rows * columns) * bombRatio; i++) minefield.add(true);
        for (int i = 0; i < (rows * columns) * (1 - bombRatio); i++) minefield.add(false);
        Collections.shuffle(minefield);

        map = new String[rows * columns];

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


    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
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
        if (x < 0 || y < 0 || x > columns - 1 || y > rows - 1 || Objects.equals(map[index], "clicked")) {
            return;
        }
        map[index] = "clicked";
        if (calculateContent(x, y) == 0) {
            recursiveClick(x - 1, y);
            recursiveClick(x + 1, y);
            recursiveClick(x, y - 1);
            recursiveClick(x, y + 1);
        }
    }

    private void handleCoverTouch(int tX, int tY) {/*
        if (tX < 5 && tY < 5 &&
                MinesweeperModel.getInstance().getCoverContent(tX, tY) == MinesweeperModel.UNTOUCHED &&
                MinesweeperModel.getInstance().getActionType() == MinesweeperModel.REVEAL) {
            MinesweeperModel.getInstance().setCoverContent(tX, tY, MinesweeperModel.getInstance().getTouched());
        } else if (tX < 5 && tY < 5 &&
                MinesweeperModel.getInstance().getCoverContent(tX, tY) == MinesweeperModel.UNTOUCHED &&
                MinesweeperModel.getInstance().getActionType() == MinesweeperModel.FLAG) {
            MinesweeperModel.getInstance().setCoverContent(tX, tY, MinesweeperModel.getInstance().getFlagged());
        }*/
    }

    private void winningModel() {/*
        if (MinesweeperModel.getInstance().checkAllTiles() && !(MinesweeperModel.getInstance().gameLost())) {
            //game won
            ((MainActivity) getContext()).showSnackBarWithDelete(
                    "Congratulations you win!");
            gameEnd = true;
        } else if (MinesweeperModel.getInstance().gameLost()) {
            ((MainActivity) getContext()).showSnackBarWithDelete(
                    "Oh no! You lost!");
            gameEnd = true;
        } else if (!(MinesweeperModel.getInstance().checkAllTiles()) && !(MinesweeperModel.getInstance().gameLost())) {

        }*/
    }
}