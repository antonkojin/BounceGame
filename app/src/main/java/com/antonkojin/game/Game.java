package com.antonkojin.game;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.view.MotionEvent;

import java.util.AbstractList;
import java.util.LinkedList;
import java.util.Random;

@SuppressWarnings("WeakerAccess")
public class Game {
    static long second = 1_000_000_000;

    boolean pause = true;

    Rect worldBounds;
    Context context;
    Character character;
    AbstractList<Baddie> baddies;
    Hud hud;
    Menu menu;
    Background background;
    Sky sky;

    Game(Context context) {
        this.context = context;
        this.worldBounds = new Rect(0, 0, Resources.getSystem().getDisplayMetrics().widthPixels, Resources.getSystem().getDisplayMetrics().heightPixels);
        background = new Background(this);
        sky = new Sky(this);
        character = new Character(this);
        hud = new Hud(this);
        menu = new Menu(this);
        baddies = new LinkedList<>();
    }

    @SuppressWarnings("ConstantConditions")
    public void touchEvent(MotionEvent event) {
        final boolean actionDown = event.getAction() == MotionEvent.ACTION_DOWN;
        final boolean actionUp = event.getAction() == MotionEvent.ACTION_UP;
        final boolean actionPausePlay = actionDown
                && hud.pausePlayRect.contains((int) event.getX(), (int) event.getY());
        if (!pause && actionPausePlay) {
            pause = true;
        } else if (pause && actionPausePlay) {
            pause = false;
        } else if (pause && actionDown) {
            menu.select(((int) event.getX()), ((int) event.getY()));
        } else if (!pause) {
            if (actionDown) {
                character.boost();
            }
        }
    }


    public void render(Canvas canvas) {
        if (!pause) {
            background.draw(canvas);
            for (Baddie b: baddies) b.draw(canvas);
            character.draw(canvas);
            sky.draw(canvas);
            hud.draw(canvas);
        } else {
            menu.draw(canvas);
        }
    }

    public void update() {
        if (character.speed.x <= 0) {
            gameOver();
        }
        if (!pause) {
            character.update();
            baddiesUpdate();
            background.update();
            sky.update();
            spawnBaddies();

        }
    }

    private void baddiesUpdate() {
        AbstractList<Baddie> baddiesToRemove = new LinkedList<>();
        for (Baddie b: baddies) {
            final boolean dead;
            if (Rect.intersects(b.rect, character.rect)) {
                dead = true;
                character.points++;
                character.speed.x += b.bounceXSpeedIncrease;
                if (!character.skyFall) {
                    character.bounce(b.rect.top, b.bounceYSpeed);
                }
            } else {
                dead = b.update();
            }
            if (dead) baddiesToRemove.add(b);
        }
        baddies.removeAll(baddiesToRemove);
    }

    private void spawnBaddies() {
        Random r = new Random();

        double simpleBaddiesPerFrame = SimpleBaddie.spawnPerSpaceUnit * character.speed.x;
        boolean spawnSimpleBaddie = r.nextDouble() <= simpleBaddiesPerFrame;
        if (spawnSimpleBaddie) {
            baddies.add(new SimpleBaddie(this));
        }

        double flyingBaddiesPerFrame = FlyingBaddie.spawnPerSpaceUnit * character.speed.x;
        boolean spawnFlyingBaddie = r.nextDouble() <= flyingBaddiesPerFrame;
        if (spawnFlyingBaddie) {
            baddies.add(new FlyingBaddie(this));
        }

        double bombBaddiesPerFrame = BombBaddie.spawnPerSpaceUnit * character.speed.x;
        boolean spawnBombBaddie = r.nextDouble() <= bombBaddiesPerFrame * character.speed.x;
        if (spawnBombBaddie) {
            baddies.add(new BombBaddie(this));
        }
    }

    void gameOver() {
        baddies.clear();
        character.reset();
        pause = true;
    }

    void gameStart() {
        pause = false;
    }
}
