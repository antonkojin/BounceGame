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
    private int velocity;


    Baddie(Context context) {
        this.image = BitmapFactory.decodeResource(context.getResources(), R.drawable.enemy);
        int imageHeight = this.image.getHeight();
        int imageWidth = this.image.getWidth();
        Random r = new Random();
        int screenWidth = Resources.getSystem().getDisplayMetrics().widthPixels;
        this.velocity = r.nextInt(maxVelocity - minVelocity) + minVelocity;
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
        int top = rect.top + velocity;
        int bottom = rect.bottom + velocity;
        rect.set(rect.left, top, rect.right, bottom);
    }
}


