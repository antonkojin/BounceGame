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
        String s = "" + ((int) game.pointsCount);
        Paint p = new Paint();
        float x = 20f;
        float y = 100f;
        p.setColor(Color.RED);
        p.setTextSize(100f);
        canvas.drawText(s, x, y, p);
        if (!game.pause) {
            canvas.drawBitmap(pauseImage, pausePlayRect.left, pausePlayRect.top, null);
        } else {
            canvas.drawBitmap(playImage, pausePlayRect.left, pausePlayRect.top, null);
        }
    }
}


