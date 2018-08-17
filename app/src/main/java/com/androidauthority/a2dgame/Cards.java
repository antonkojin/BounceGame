package com.androidauthority.a2dgame;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Rect;

public class Cards {

    private final Bitmap image;
    public Rect rectOne;
    public Rect rectTwo;


    Cards(Context context) {
        this.image = BitmapFactory.decodeResource(context.getResources(), R.drawable.card);
        int imageHeight = this.image.getHeight();
        int imageWidth = this.image.getWidth();
        int screenHeight = Resources.getSystem().getDisplayMetrics().heightPixels;
        int screenWidth = Resources.getSystem().getDisplayMetrics().widthPixels;
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


