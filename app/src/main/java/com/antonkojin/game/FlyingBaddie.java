package com.antonkojin.game;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Rect;

import java.util.Random;

class FlyingBaddie extends Baddie {
    static double spawnPerSpaceUnit = 0.0005;
    int minXSpeed = 1;
    int maxXspeed = 10;
    int xSpeed;

    FlyingBaddie(Game game) {
        super(game);
        bounceXSpeedIncrease = 0;
        bounceYSpeed = 40;
        Random r = new Random();
        this.xSpeed = r.nextInt(maxXspeed - minXSpeed) + minXSpeed;
        this.image = BitmapFactory.decodeResource(game.context.getResources(), R.drawable.flying_baddie);
        int imageHeight = image.getHeight();
        int left = game.worldBounds.right;
        int right = left + image.getWidth();
        final int maxTopPosition = game.background.grassRect.top - (image.getHeight() * 2);
        final int minTopPosition = game.sky.rect.bottom;
        int top = r.nextInt(maxTopPosition - minTopPosition) + minTopPosition;
        int bottom = top + imageHeight;
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
