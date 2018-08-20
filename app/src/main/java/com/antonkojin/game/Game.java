package com.antonkojin.game;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.view.MotionEvent;

@SuppressWarnings("WeakerAccess")
public class Game {
    static double second = 1e9;
    final Context context;
    boolean pause = true;
    double pointsCount = 0;
    Points points;
    Ants ants;
    Character character;
    Fireballs fireballs;
    Hud hud;
    Rect worldBounds;
    Updates updates;

    Game(Context context) {
        this.context = context;
        character = new Character(this);
        points = new Points(this);
        fireballs = new Fireballs(this);
        ants = new Ants(this);
        hud = new Hud(this);
        updates = new Updates(this);
        final int screenWidth = Resources.getSystem().getDisplayMetrics().widthPixels;
        final int screenHeight = Resources.getSystem().getDisplayMetrics().heightPixels;
        worldBounds = new Rect(0, 0, screenWidth, screenHeight);
    }

    public void touchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_UP) pause = true;
        if (!pause) {
            character.moveToX = (int) event.getX();
        } else if (pause && event.getAction() == MotionEvent.ACTION_DOWN) {
            updates.select(((int) event.getX()), ((int) event.getY()));
        }
    }


    public void render(Canvas canvas) {
        if (!pause) {
            ants.draw(canvas);
            points.draw(canvas);
            fireballs.draw(canvas);
            character.draw(canvas);
            hud.draw(canvas);
        } else {
            hud.draw(canvas);
            updates.draw(canvas);
        }
    }

    public void update() {
        if (!pause) {
            character.update();
            points.update();
            fireballs.update();
            ants.update();
        }
    }
}
