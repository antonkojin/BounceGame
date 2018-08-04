package com.androidauthority.a2dgame;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.util.DisplayMetrics;
import android.util.Log;

public class CharacterSprite {

    private Bitmap image;
    private int x, y;
    private int xVelocity = 10;
    private int yVelocity = 5;
    private int screenWidth;
    private int screenHeight;

    public CharacterSprite(Bitmap image) {
        this.image = image;
        // TODO move x, y to constructor parameters
        x = 100;
        y = 100;
        final DisplayMetrics displayMetrics = Resources.getSystem().getDisplayMetrics();
        screenHeight = displayMetrics.heightPixels;
        screenWidth = displayMetrics.widthPixels;
    }

    public void draw(Canvas canvas) {
        canvas.drawBitmap(image, x, y, null);
    }

    public void update() {
        // TODO get a reference to the World? has see things to interact with
        // no interface for now, only one world there is and aver will be
        x += xVelocity;
        y += yVelocity;
        if ((x > screenWidth - image.getWidth()) || (x < 0)) {
            xVelocity = xVelocity * -1;
        }
        if ((y > screenHeight - image.getHeight()) || (y < 0)) {
            yVelocity = yVelocity * -1;
        }

    }
}


