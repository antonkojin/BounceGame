package com.antonkojin.game;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.view.MotionEvent;

@SuppressWarnings("WeakerAccess")
public class Game {
    static long second = 1_000_000_000;
    final Context context;
    boolean pause = false;
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

    @SuppressWarnings("ConstantConditions")
    public void touchEvent(MotionEvent event) {
        // Log.d("", "touchEvent: " + event);
        final boolean actionDown = event.getAction() == MotionEvent.ACTION_DOWN;
        final boolean actionUp = event.getAction() == MotionEvent.ACTION_UP;
        final boolean actionPausePlay = actionDown
                && hud.pausePlayRect.contains((int) event.getX(), (int) event.getY());
        if (!pause && actionPausePlay) {
            pause = true;
        } else if (pause && actionPausePlay) {
            pause = false;
        } else if (pause && actionDown) {
            updates.select(((int) event.getX()), ((int) event.getY()));
        } else if (!pause) {
            if (actionUp) {
                character.moveToX = character.rect.centerX();
            } else {
                character.moveToX = (int) event.getX();
            }
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
