package com.androidauthority.a2dgame;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.DisplayMetrics;
import android.util.Log;

import java.util.Random;

public class Villain {

    private final Context context;
    private final Bitmap image;
    public Rect rect;
    private int screenWidth;
    private int screenHeight;
    private int velocity;


    public Villain(Context context) {
        this.context = context;
        this.image = BitmapFactory.decodeResource(this.context.getResources(), R.drawable.enemy);
        int imageHeight = this.image.getHeight();
        int imageWidth = this.image.getWidth();
        Random r = new Random();
        screenHeight = Resources.getSystem().getDisplayMetrics().heightPixels;
        screenWidth = Resources.getSystem().getDisplayMetrics().widthPixels;
        velocity = r.nextInt(10) + 3;
        int bounds = 50;
        int left = r.nextInt(screenWidth - imageWidth - 2*bounds) + bounds;
        int right = left + imageWidth;
        int bottom = 0;
        int top = bottom - imageHeight;
        this.rect = new Rect(left, top, right, bottom);
    }

    public void draw(Canvas canvas) {
        // canvas.drawBitmap(image, rect.left, rect.top, null);
        Paint p = new Paint();
        p.setStyle(Paint.Style.FILL_AND_STROKE);
        p.setColor(Color.RED);
        canvas.drawRect(this.rect, p);
    }

    public void update() {
        int top = rect.top + velocity;
        int bottom = rect.bottom + velocity;
        rect.set(rect.left, top, rect.right, bottom);
    }
}


