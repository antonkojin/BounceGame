package com.androidauthority.a2dgame;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.Log;

import java.util.Random;

public class Cards {

    private final Context context;
    private final Bitmap image;
    public Rect rectOne;
    public Rect rectTwo;
    private int screenWidth;
    private int screenHeight;


    public Cards(Context context) {
        this.context = context;
        this.image = BitmapFactory.decodeResource(this.context.getResources(), R.drawable.card);
        int imageHeight = this.image.getHeight();
        int imageWidth = this.image.getWidth();
        screenHeight = Resources.getSystem().getDisplayMetrics().heightPixels;
        screenWidth = Resources.getSystem().getDisplayMetrics().widthPixels;
        int space = (screenWidth - (2*imageWidth)) / 3;
        int leftOne = space;
        int rightOne = leftOne + imageWidth;
        int leftTwo = rightOne + space;
        int rightTwo = leftTwo + imageWidth;
        int top = (screenHeight / 2) - (imageHeight / 2);
        int bottom = top + imageHeight;
        this.rectOne = new Rect(leftOne, top, rightOne, bottom);
        this.rectTwo = new Rect(leftTwo, top, rightTwo, bottom);
    }

    public void draw(Canvas canvas) {
        canvas.drawBitmap(image, rectOne.left, rectOne.top, null);
        canvas.drawBitmap(image, rectTwo.left, rectTwo.top, null);
    }
}


