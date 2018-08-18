package com.antonkojin.game;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Rect;

import java.util.Random;

public class Baddie {

    private final Bitmap image;
    static int maxVelocity = 15;
    static int minVelocity = 5;
    public Rect rect;
    private int gravity;


    Baddie(Context context) {
        this.image = BitmapFactory.decodeResource(context.getResources(), R.drawable.enemy);
        int imageHeight = this.image.getHeight();
        int imageWidth = this.image.getWidth();
        Random r = new Random();
        int screenWidth = Resources.getSystem().getDisplayMetrics().widthPixels;
        // this.gravity = r.nextInt(maxVelocity - minVelocity) + minVelocity;
        gravity = 20;
        int bounds = 50;
        int left = r.nextInt(screenWidth - imageWidth - (2 * bounds)) + bounds;
        int right = left + imageWidth;
        int bottom = 0;
        int top = bottom - imageHeight;
        this.rect = new Rect(left, top, right, bottom);
    }

    public void draw(Canvas canvas) {
        canvas.drawBitmap(image, rect.left, rect.top, null);
    }

    public void update() {
        int top = rect.top + gravity;
        int bottom = rect.bottom + gravity;
        rect.set(rect.left, top, rect.right, bottom);
    }
}


