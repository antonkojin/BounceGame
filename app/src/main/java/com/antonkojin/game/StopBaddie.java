package com.antonkojin.game;

import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Rect;

import java.util.Random;

class StopBaddie extends Baddie {
    static double spawnPerSpaceUnit = 0.00001;
    int minXSpeed = 1;
    int maxXSpeed = 10;
    int xSpeed;

    StopBaddie(Game game) {
        super(game);
        bounceXSpeedIncrease = -5;
        bounceYSpeed = 40;
        Random r = new Random();
        this.xSpeed = r.nextInt(maxXSpeed - minXSpeed) + minXSpeed;
        this.image = BitmapFactory.decodeResource(game.context.getResources(), R.drawable.stop_baddie);
        int left = game.worldBounds.right;
        int right = left + image.getWidth();
        int bottom = game.background.grassRect.top;
        int top = bottom - image.getHeight();
        this.rect = new Rect(left, top, right, bottom);
    }

    @Override
    public void draw(Canvas canvas) {
        canvas.drawBitmap(image, rect.left, rect.top, null);
    }

    @Override
    public boolean update() {
        final int dx = -(game.character.speed.x - this.xSpeed);
        rect.offset(dx, 0);
        return rect.right <= 0;
    }
}
