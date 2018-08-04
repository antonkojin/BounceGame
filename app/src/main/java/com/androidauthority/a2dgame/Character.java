package com.androidauthority.a2dgame;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.DisplayMetrics;
import android.util.Log;

public class Character {

    private Rect rect;
    int velocity = 20;
    private int screenWidth;
    private int screenHeight;
    private Paint paint;
    private final int size = 200;
    private final int halfSize = size / 2;


    public Character() {
        final DisplayMetrics displayMetrics = Resources.getSystem().getDisplayMetrics();
        screenHeight = displayMetrics.heightPixels;
        screenWidth = displayMetrics.widthPixels;
        int x = screenWidth / 2;
        int y = screenHeight - halfSize - 200;
        int left = x - halfSize;
        int top = y - halfSize;
        int right = x + halfSize;
        int bottom = y + halfSize;
        rect = new Rect(left, top, right, bottom);
        paint = new Paint();
        paint.setColor(Color.GREEN);
    }

    public void draw(Canvas canvas) {
        canvas.drawRect(rect, paint);
    }

    public void update() {
        // Log.d("", "character x: " + rect.centerX() + " y:  " + rect.centerY());
        // TODO get a reference to the World? has see things to interact with
    }

    public void moveTo(int x) {
        int left, right;
        if (rect.centerX() < x) {
            left =  rect.left + velocity;
            right = rect.right + velocity;
        } else {
            left =  rect.left - velocity;
            right = rect.right - velocity;
        }
        rect.set(left, rect.top, right, rect.bottom);
    }
}


