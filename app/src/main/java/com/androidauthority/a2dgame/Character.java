package com.androidauthority.a2dgame;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Rect;

public class Character {

    private Bitmap image;
    public Rect rect;
    private int screenWidth;
    private int velocity;
    private int toX;


    Character(Context context) {
        this.velocity = 0;
        this.image = BitmapFactory.decodeResource(context.getResources(), R.drawable.hero);
        this.screenWidth = Resources.getSystem().getDisplayMetrics().widthPixels;
        int screenHeight = Resources.getSystem().getDisplayMetrics().heightPixels;
        int imageHeight = image.getHeight();
        int imageWidth = image.getWidth();
        int bottom = screenHeight - 200;
        int top = bottom - imageHeight;
        int left = screenWidth / 2 - imageWidth / 2;
        int right = left + imageWidth;
        this.rect = new Rect(left, top, right, bottom);
        this.toX = this.rect.centerX();
    }

    public void draw(Canvas canvas) {
        canvas.drawBitmap(image, rect.left, rect.top, null);
    }

    public void update() {
        // Log.d("", "character x: " + rect.centerX() + " y:  " + rect.centerY());
        // TODO get a reference to the World? has see things to interact with

        // set velocity to reach x
        int maxVelocity = 20;
        velocity = (toX - rect.centerX()) / 10;
        if (velocity > maxVelocity) velocity = maxVelocity;
        else if (velocity < -maxVelocity) velocity = -maxVelocity;

        int left = rect.left + velocity;
        int right = rect.right + velocity;
        if (rect.left < 0) {
            left = 0;
            right = left + rect.width();
        }
        if (rect.right > screenWidth) {
            right = screenWidth;
            left = right - rect.width();
        }
        rect.set(left, rect.top, right, rect.bottom);
    }

    public void moveTo(int x) {
        this.toX = x;
    }
}


