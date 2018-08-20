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
    double fireballDelta = second * 1.5;
    Rect worldBounds;
    boolean pause = false; // TODO set true
    long lastFireballTime;
    List<Fireball> fireballs;
    int cardsPointsThresholdMultiplier = 2;
    List<Point> points;
    List<Ant> ants;
    double antDelta = second * 3;
    private long lastAntTime;

    Game(Context context) {
        this.context = context;
        character = new Character(context);
        points = new LinkedList<>();
        fireballs = new LinkedList<>();
        lastPointTime = nanoTime();
        lastFireballTime = nanoTime();
        lastAntTime = nanoTime();
        cards = null;
        pointsCount = 0;
        cardsPointsThreshold = 1;
        hud = new Hud(this);
        final int screenWidth = Resources.getSystem().getDisplayMetrics().widthPixels;
        final int screenHeight = Resources.getSystem().getDisplayMetrics().heightPixels;
        worldBounds = new Rect(0, 0, screenWidth, screenHeight);
        this.ants = new LinkedList<Ant>();
    }

    public void touchEvent(MotionEvent event) {
        //if (cards == null) {
            if (event.getAction() == MotionEvent.ACTION_DOWN) pause = false;
            else if (event.getAction() == MotionEvent.ACTION_UP) {
                // pause = false; // TODO set true
            }
            if (!pause) {
                character.moveTo((int) event.getX());
            }
        //}
        /*else if (event.getAction() == MotionEvent.ACTION_DOWN) { // cards
            int x = (int) event.getX();
            int y = (int) event.getY();
            if (cards.isSelected(x, y)) {
                cards = null;
                this.pause = false; // TODO remove here
                this.cardsPointsThreshold *= cardsPointsThresholdMultiplier;
            }
        }*/
    }


    public void render(Canvas canvas) {
        character.draw(canvas);
        for (Point v : points) v.draw(canvas);
        for (Fireball b : fireballs) b.draw(canvas);
        for (Ant a : ants) a.draw(canvas);
        if (cards != null) cards.draw(canvas);
        hud.draw(canvas);
    }

    public void update() {
        // CHARACTER
        character.update();
        /*
        // CARDS
        if ((int) pointsCount >= (int) cardsPointsThreshold) {
            this.cards = new Cards(context, this);
            this.pause = true;
        }
        */
        // POINTS
        // spawn point
        if (nanoTime() - lastPointTime >= pointDelta) {
            Point v = new Point(context, this);
            points.add(v);
            lastPointTime = nanoTime();
        }
        // update
        for (Point point : points) {
            point.update();
            if (character.magnet.contains(point.rect)) {
                point.moveTo(character.magnet, character.rect);
            }
        }
        // remove points
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

        // FIREBALL
        // spawn fireball
        if (nanoTime() - lastFireballTime >= fireballDelta) {
            Fireball v = new Fireball(context);
            fireballs.add(v);
            lastFireballTime = nanoTime();
        }
        // fireball update
        for (Fireball b : fireballs) b.update();
        // fireball remove
        List<Fireball> fireballsToRemove = new LinkedList<>();
        for (Fireball b : fireballs) {
            if (Rect.intersects(b.rect, character.rect)) {
                fireballsToRemove.add(b);
                this.pointsCount--;
            }
            if (b.rect.top >= worldBounds.bottom) {
                fireballsToRemove.add(b);
            }
        }
        fireballs.removeAll(fireballsToRemove);

        // ANT
        // spawn ant
        if (nanoTime() - lastAntTime >= antDelta) {
            Ant a = new Ant(context, this);
            ants.add(a);
            lastAntTime = nanoTime();
        }
        // remove ants
        List<Ant> antsToRemove = new LinkedList<>();
        for (Ant a : ants) {
            if (Rect.intersects(a.rect, character.rect)) {
                antsToRemove.add(a);
                this.pointsCount--;
            }
            if (a.rect.top >= worldBounds.bottom) {
                antsToRemove.add(a);
            }
        }
        ants.removeAll(antsToRemove);
        // update ants
        for (Ant a : ants) a.update();
    }
}
