package com.antonkojin.game;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

class Hud {
    private Game game;

    Hud(Game game) {
        this.game = game;
    }

    public void draw(Canvas canvas) {
        String s = "" + ((int) game.pointsCount);
        Paint p = new Paint();
        float x = 20f;
        float y = 100f;
        p.setColor(Color.RED);
        p.setTextSize(100f);
        canvas.drawText(s, x, y, p);
    }
}


