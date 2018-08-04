package com.androidauthority.a2dgame;

import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.DisplayMetrics;
import android.util.Log;

import java.util.Random;

public class Villain {

    private Rect rect;
    private int screenWidth;
    private int screenHeight;
    private Paint paint;
    private int velocity;
    private final int size;
    private final int halfSize;


    public Villain() {
        Random r = new Random();
        screenHeight = Resources.getSystem().getDisplayMetrics().heightPixels;
        screenWidth = Resources.getSystem().getDisplayMetrics().widthPixels;
        velocity = r.nextInt(10);
        size = r.nextInt(200) + 100;
        halfSize = size / 2;
        int leftBound = halfSize + 10;
        int rightBound = screenWidth - 10 - halfSize;
        int x = r.nextInt(rightBound - leftBound + 1) + leftBound;
        int y = -halfSize;
        int left = x - halfSize;
        int top = y - halfSize;
        int right = x + halfSize;
        int bottom = y + halfSize;
        rect = new Rect(left, top, right, bottom);
        paint = new Paint();
        paint.setColor(Color.RED);
        Log.i("", "Villain size:" + size + " x: " + x + " vel: " + velocity);
    }

    public void draw(Canvas canvas) {
        canvas.drawRect(rect, paint);
    }

    public void update() {
        int top = rect.top + velocity;
        int bottom = rect.bottom + velocity;
        rect.set(rect.left, top, rect.right, bottom);
    }
}


