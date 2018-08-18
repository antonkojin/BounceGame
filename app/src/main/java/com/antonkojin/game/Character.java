package com.antonkojin.game;

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
    Rect magnet;
    private int toX;
    private int speed;


    Character(Context context) {
        this.image = BitmapFactory.decodeResource(context.getResources(), R.drawable.hero);
        this.speed = 0;
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

        this.magnet = new Rect(rect.left - (rect.width() * 2), rect.top - (rect.height() * 2), rect.right + (rect.width() * 2), rect.bottom);
    }

    public void draw(Canvas canvas) {
//        Paint paint = new Paint();
//        paint.setColor(Color.BLUE);
//        canvas.drawRect(magnet, paint);
        canvas.drawBitmap(image, rect.left, rect.top, null);
    }

    public void update() {
        // Log.d("", "character x: " + rect.centerX() + " y:  " + rect.centerY());
        // TODO get a reference to the World? has see things to interact with

        // set speed to reach x
        int maxVelocity = 20;
        speed = (toX - rect.centerX()) / 10;
        if (speed > maxVelocity) speed = maxVelocity;
        else if (speed < -maxVelocity) speed = -maxVelocity;

        int left = rect.left + speed;
        int right = rect.right + speed;
        if (rect.left < 0) {
            left = 0;
            right = left + rect.width();
        }
        if (rect.right > screenWidth) {
            right = screenWidth;
            left = right - rect.width();
        }
        rect.set(left, rect.top, right, rect.bottom);
        magnet.offset(rect.centerX() - magnet.centerX(), 0);
    }

    public void moveTo(int x) {
        this.toX = x;
    }
}


