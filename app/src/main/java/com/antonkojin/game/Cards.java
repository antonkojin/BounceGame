package com.antonkojin.game;

import android.arch.core.util.Function;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;

public class Cards {

    private final Bitmap image;
    private final Game game;
    Rect rectOne;
    Rect rectTwo;
    private Function<Void, Void> cardOne;
    private Function<Void, Void> cardTwo;
    private String textOne, textTwo;


    Cards(Context context, final Game game) {
        this.game = game;
        this.image = BitmapFactory.decodeResource(context.getResources(), R.drawable.card);
        int imageHeight = this.image.getHeight();
        int imageWidth = this.image.getWidth();
        int screenHeight = Resources.getSystem().getDisplayMetrics().heightPixels;
        int screenWidth = Resources.getSystem().getDisplayMetrics().widthPixels;
        int space = (screenWidth - (2*imageWidth)) / 3;
        @SuppressWarnings("UnnecessaryLocalVariable")
        int leftOne = space;
        int rightOne = leftOne + imageWidth;
        int leftTwo = rightOne + space;
        int rightTwo = leftTwo + imageWidth;
        int top = (screenHeight / 2) - (imageHeight / 2);
        int bottom = top + imageHeight;
        this.rectOne = new Rect(leftOne, top, rightOne, bottom);
        this.rectTwo = new Rect(leftTwo, top, rightTwo, bottom);

        textOne = "+money";
        this.cardOne = new Function<Void, Void>() {
            @Override
            public Void apply(Void input) {
                game.pointDelta = game.pointDelta / 1.1;
                return null;
            }
        };
        textTwo = "-baddie";
        this.cardTwo = new Function<Void, Void>() {
            @Override
            public Void apply(Void input) {
                game.baddieDelta = game.baddieDelta * 1.1;
                return null;
            }
        };
    }

    public void draw(Canvas canvas) {
        Paint p = new Paint();
        p.setTextSize(100);
        p.setColor(Color.WHITE);
        // one
        canvas.drawBitmap(image, rectOne.left, rectOne.top, null);
        canvas.drawText(textOne, rectOne.left + 50, rectOne.centerY() + 100, p);
        // two
        canvas.drawBitmap(image, rectTwo.left, rectTwo.top, null);
        canvas.drawText(textTwo, rectTwo.left + 50, rectTwo.centerY() + 100, p);
    }

    public boolean isSelected(int x, int y) {
        if (rectOne.contains(x, y)) {
            this.cardOne.apply(null);
            return true;
        } else if (rectTwo.contains(x, y)) {
            this.cardTwo.apply(null);
            return true;
        } else {
            return false;
        }
    }
}


