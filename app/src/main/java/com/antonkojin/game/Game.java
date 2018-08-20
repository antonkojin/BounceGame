package com.antonkojin.game;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.view.MotionEvent;

import java.util.LinkedList;
import java.util.List;

import static java.lang.System.nanoTime;

@SuppressWarnings("WeakerAccess")
public class Game {
    private final Context context;
    double pointsCount;
    double cardsPointsThreshold;
    Character character;
    Cards cards;
    Hud hud;
    long lastPointTime;
    double second = 1e9;
    double pointDelta = second * 1.5;
    double baddieDelta = second * 1.5;
    Rect worldBounds;
    boolean pause = false; // TODO set true
    long lastBaddieTime;
    List<Baddie> baddies;
    int cardsPointsThresholdMultiplier = 2;
    List<Point> points;

    Game(Context context) {
        this.context = context;
        character = new Character(context);
        points = new LinkedList<>();
        baddies = new LinkedList<>();
        lastPointTime = nanoTime();
        lastBaddieTime = nanoTime();
        cards = null;
        pointsCount = 0;
        cardsPointsThreshold = 1;
        hud = new Hud(this);
        final int screenWidth = Resources.getSystem().getDisplayMetrics().widthPixels;
        final int screenHeight = Resources.getSystem().getDisplayMetrics().heightPixels;
        worldBounds = new Rect(0, 0, screenWidth, screenHeight);
    }

    public void touchEvent(MotionEvent event) {
        if (cards == null) {
            if (event.getAction() == MotionEvent.ACTION_DOWN) pause = false;
            else if (event.getAction() == MotionEvent.ACTION_UP) {
                // pause = false; // TODO set true
            }
            if (!pause) {
                character.moveTo((int) event.getX());
            }
        } else if (event.getAction() == MotionEvent.ACTION_DOWN) { // cards
            int x = (int) event.getX();
            int y = (int) event.getY();
            if (cards.isSelected(x, y)) {
                cards = null;
                this.pause = false; // TODO remove here
                this.cardsPointsThreshold *= cardsPointsThresholdMultiplier;
            }
        }
    }


    public void render(Canvas canvas) {
        character.draw(canvas);
        for (Point v : points) v.draw(canvas);
        for (Baddie b : baddies) b.draw(canvas);
        hud.draw(canvas);
        if (cards != null) {
            cards.draw(canvas);
        }
    }

    public void update() {
        // spawn cards
        if ((int) pointsCount >= (int) cardsPointsThreshold) {
            this.cards = new Cards(context, this);
            this.pause = true;
        }
        // points contacts
        List<Point> pointsToRemove = new LinkedList<>();
        for (Point v : points) {
            if (Rect.intersects(v.rect, character.rect)) {
                pointsToRemove.add(v);
                this.pointsCount++;
            }
            if (v.rect.top >= Resources.getSystem().getDisplayMetrics().heightPixels) {
                pointsToRemove.add(v);
            }
        }
        points.removeAll(pointsToRemove);
        // baddies contacts
        List<Baddie> baddiesToRemove = new LinkedList<>();
        for (Baddie b : baddies) {
            if (Rect.intersects(b.rect, character.rect)) {
                baddiesToRemove.add(b);
                this.pointsCount--;
            }
            if (b.rect.top >= worldBounds.bottom) {
                baddiesToRemove.add(b);
            }
        }
        baddies.removeAll(baddiesToRemove);

        // update everyone
        character.update();
        for (Point point : points) {
            point.update();
            if (character.magnet.contains(point.rect)) {
                point.moveTo(character.magnet, character.rect);
            }
        }
        for (Baddie b : baddies) b.update();

        // spawn point
        if (nanoTime() - lastPointTime >= pointDelta) {
            Point v = new Point(context);
            points.add(v);
            lastPointTime = nanoTime();
        }
        // spawn baddie
        if (nanoTime() - lastBaddieTime >= baddieDelta) {
            Baddie v = new Baddie(context);
            baddies.add(v);
            lastBaddieTime = nanoTime();
        }
    }
}
