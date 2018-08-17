package com.androidauthority.a2dgame;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

class Hud {
    private GameSurfaceViewRunnable game;

    public Hud(GameSurfaceViewRunnable game) {
        this.game = game;
    }

    public void draw(Canvas canvas) {
        String s = "" + game.pointsCount + "/" + game.cardsPointsThreshold;
        Paint p = new Paint();
        float x = 0f;
        float y = 100f;
        p.setColor(Color.RED);
        p.setTextSize(100f);
        canvas.drawText(s, x, y, p);
    }
}


