package com.antonkojin.game;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.util.Log;

import java.util.Random;

@SuppressWarnings("FieldCanBeLocal")
public class Ant {

    private final Bitmap image;
    private final Game game;
    private final int maxXVelocity = 3;
    Rect rect;
    private int xVelocity = 0;
    private int yVelocity = 5;
    // this.gravity = r.nextInt(maxVelocity - minVelocity) + minVelocity;

    Ant(Context context, Game game) {
        this.image = BitmapFactory.decodeResource(context.getResources(), R.drawable.ant);
        this.game = game;
        int imageHeight = this.image.getHeight();
        int imageWidth = this.image.getWidth();
        Random r = new Random();
        int screenWidth = Resources.getSystem().getDisplayMetrics().widthPixels;
        int bounds = 50;
        int left = r.nextInt(screenWidth - imageWidth - (2 * bounds)) + bounds;
        int right = left + imageWidth;
        int bottom = 0;
        int top = bottom - imageHeight;
        this.rect = new Rect(left, top, right, bottom);
    }

    public void draw(Canvas canvas) {
        canvas.drawBitmap(image, rect.left, rect.top, null);
        Log.i("", "Ant x: " + rect.centerX() + " y: " + rect.centerY());
    }

    public void update() {
        /*Rect character = game.character.rect;
        if (rect.centerX() >= character.left || rect.centerX() <= character.right) {
            xVelocity = 0;
        } else if (rect.centerX() < character.left) {
            xVelocity = maxXVelocity;
        } else if (rect.centerX() > character.right) {
            xVelocity = -maxXVelocity;
        }*/
        int dx = xVelocity;
        int dy = yVelocity;
        rect.offset(dx, dy);
    }
}


