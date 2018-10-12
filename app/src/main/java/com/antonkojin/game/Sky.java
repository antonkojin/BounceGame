package com.antonkojin.game;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.util.Log;

class Sky {

    final Game game;
    private final Bitmap image;
    final Rect rect;

    Sky(Game game) {
        this.game = game;
        this.image = BitmapFactory.decodeResource(game.context.getResources(), R.drawable.sky);
        this.rect = new Rect(
                0,
                0,
                image.getWidth(),
                image.getHeight()
        );
    }

    public void draw(Canvas canvas) {
        canvas.drawBitmap(image, rect.left, rect.top, null);
        canvas.drawBitmap(image, rect.right, rect.top, null);
        Log.d("", "" + rect.left + "\t" + rect.right + "\t" + game.worldBounds.width());
    }

    public void update() {
        rect.offset(-game.character.speed.x, 0);
        if (rect.right <= 0) {
            rect.offsetTo(0, 0);
        }
    }
}
