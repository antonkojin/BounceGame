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

public class Character {

    private final Context context;
    private Bitmap image;
    public Rect rect;
    private int screenWidth;
    private int screenHeight;
    int velocity = 20;


    public Character(Context context) {
        this.context = context;
        image = BitmapFactory.decodeResource(context.getResources(), R.drawable.hero);
        screenHeight = Resources.getSystem().getDisplayMetrics().heightPixels;
        screenWidth = Resources.getSystem().getDisplayMetrics().widthPixels;
        int imageHeight = image.getHeight();
        int imageWidth = image.getWidth();
        int bottom = screenHeight - 200;
        int top = bottom - imageHeight;
        int left = (screenWidth / 2) - (imageWidth / 2);
        int right = left + imageWidth;
        this.rect = new Rect(left, top, right, bottom);
    }

    public void draw(Canvas canvas) {
        canvas.drawBitmap(image, rect.left, rect.top, null);
    }

    public void update() {
        // Log.d("", "character x: " + rect.centerX() + " y:  " + rect.centerY());
        // TODO get a reference to the World? has see things to interact with
    }

    public void moveTo(int x) {
        int left = rect.left;
        int right = rect.right;
        int error = 10;
        if (x > rect.centerX() + error) {
            left =  rect.left + velocity;
            right = rect.right + velocity;
        } else if(x < rect.centerX() - error) {
            left =  rect.left - velocity;
            right = rect.right - velocity;
        }
        if(rect.left < 0) {
            left = 0;
            right = left + rect.width();
        }
        if(rect.right > screenWidth) {
            right = screenWidth;
            left = right - rect.width();
        }
        rect.set(left, rect.top, right, rect.bottom);
    }
}


