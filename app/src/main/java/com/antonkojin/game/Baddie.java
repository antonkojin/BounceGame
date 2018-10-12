package com.antonkojin.game;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Rect;

abstract class Baddie {
    Game game;
    Bitmap image;
    Rect rect;
    int bounceYSpeed;
    int bounceXSpeedIncrease;

    Baddie(Game game) {
        this.game = game;
    }

    abstract void draw(Canvas canvas);
    abstract boolean update();
}
