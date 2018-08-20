package com.antonkojin.game;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;

class Updates {
    private final Bitmap image;
    private final Game game;
    private Rect rect;

    public Updates(Context context, Game game) {
        this.image = BitmapFactory.decodeResource(context.getResources(), R.drawable.update);
        int screenHeight = Resources.getSystem().getDisplayMetrics().heightPixels;
        int top = (screenHeight - image.getHeight()) / 2;
        int bottom = top + image.getHeight();
        int screenWidth = Resources.getSystem().getDisplayMetrics().widthPixels;
        int left = (screenWidth - image.getWidth()) / 2;
        int right = left + image.getWidth();
        this.rect = new Rect(left, top, right, bottom);
        this.game = game;
    }

    public void draw(Canvas canvas) {
        canvas.drawBitmap(image, rect.left, rect.top, null);
        Paint paint = new Paint();
        paint.setTextSize(100);
        canvas.drawText("", rect.left + 20, rect.bottom - 20, paint);
    }
}
