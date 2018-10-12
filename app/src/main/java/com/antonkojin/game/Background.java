package com.antonkojin.game;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Rect;

class Background {

    final Game game;
    final Bitmap mountains;
    final Rect grassRect;
    final Rect mountainsRect;
    Bitmap grass;

    Background(Game game) {
        this.game = game;
        this.grass = BitmapFactory.decodeResource(game.context.getResources(), R.drawable.grass);
        this.mountains = BitmapFactory.decodeResource(game.context.getResources(), R.drawable.mountains);
        this.grassRect = new Rect(
                game.worldBounds.width() / 2 - grass.getWidth() / 2,
                game.worldBounds.height() - grass.getHeight(),
                game.worldBounds.width() / 2 - grass.getWidth() / 2 + grass.getWidth(),
                game.worldBounds.height());
        this.mountainsRect = new Rect(
                0,
                grassRect.top - mountains.getHeight(),
                mountains.getWidth(),
                grassRect.top);
    }

    public void draw(Canvas canvas) {
        canvas.drawBitmap(grass, grassRect.left, grassRect.top, null);
        canvas.drawBitmap(mountains, mountainsRect.left, mountainsRect.top, null);
        canvas.drawBitmap(mountains, mountainsRect.right, mountainsRect.top, null);
    }

    public void update() {
        mountainsRect.offset(-game.character.speed.x, 0);
        if (mountainsRect.right <= 0) {
            mountainsRect.offsetTo(0, mountainsRect.top);
        }
    }
}
