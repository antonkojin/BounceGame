package com.antonkojin.game;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Rect;

import java.util.Random;

public class Point {

    static double maxVelocity = 15;
    static double minVelocity = 5;
    private final Bitmap image;
    public Rect rect;
    private double velocity;


    Point(Context context) {
        this.image = BitmapFactory.decodeResource(context.getResources(), R.drawable.money);
        int imageHeight = this.image.getHeight();
        int imageWidth = this.image.getWidth();
        Random r = new Random();
        int screenWidth = Resources.getSystem().getDisplayMetrics().widthPixels;
        this.velocity = r.nextInt((int) (maxVelocity - minVelocity)) + minVelocity;
        int bounds = 50;
        int left = r.nextInt(screenWidth - imageWidth - (2 * bounds)) + bounds;
        int right = left + imageWidth;
        int bottom = 0;
        int top = bottom - imageHeight;
        this.rect = new Rect(left, top, right, bottom);
    }

    public void draw(Canvas canvas) {
        canvas.drawBitmap(image, rect.left, rect.top, null);

/*
        Paint p = new Paint();
        p.setStyle(Paint.Style.FILL_AND_STROKE);
        p.setColor(Color.RED);
        canvas.drawRect(this.rect, p);
*/
    }

    public void update() {
        int top = (int) (rect.top + velocity);
        int bottom = (int) (rect.bottom + velocity);
        rect.set(rect.left, top, rect.right, bottom);
    }
}


