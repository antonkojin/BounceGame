package com.antonkojin.game;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;

class Hud {
    final Rect pausePlayRect;
    private final Bitmap pauseImage;
    private Game game;
    private Bitmap playImage;

    Hud(Game game) {
        this.game = game;
        this.pauseImage = BitmapFactory.decodeResource(game.context.getResources(), R.drawable.pause);
        this.playImage = BitmapFactory.decodeResource(game.context.getResources(), R.drawable.play);
        final int screenWidth = Resources.getSystem().getDisplayMetrics().widthPixels;
        final int screenHeight = Resources.getSystem().getDisplayMetrics().heightPixels;
        int pauseSpace = 100;
        int right = screenWidth - pauseSpace;
        @SuppressWarnings("UnnecessaryLocalVariable")
        int top = pauseSpace;
        int left = right - pauseImage.getWidth();
        int bottom = top + pauseImage.getHeight();
        this.pausePlayRect = new Rect(left, top, right, bottom);
    }

    public void draw(Canvas canvas) {
        String pointsString = game.character.points + " points";
        String speedString = "" + game.character.speed.x * 2 + " m/s";
        String boostsString = game.character.boosts + " boosts";
        Paint p = new Paint();
        p.setColor(Color.RED);
        final float textSize = 100f;
        final float space = 20f;
        p.setTextSize(textSize);
        float boostsY = game.sky.rect.bottom + textSize + space;
        float pointsY = boostsY + textSize + space;
        float speedY = pointsY + textSize + space;
        canvas.drawText(boostsString, space, boostsY, p);
        canvas.drawText(speedString, space, speedY, p);
        canvas.drawText(pointsString, space, pointsY, p);
    canvas.drawBitmap(pauseImage, pausePlayRect.left, pausePlayRect.top, null);
    }
}


