package com.antonkojin.game;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Point;
import android.graphics.Rect;
import android.util.Log;

public class Character {
    int points = 0;
    int boosts = 10;
    int initialXSpeed = 20;
    int gravity = 1;
    int boostSpeed = 50;
    int skyFallSpeed = 60;

    double boostBounceSpeed = 50;
    int skyfallBounceSpeed = 50;
    int groundBounceSpeed = 25;

    int skyfallBounceXSpeedIncrease = 5;
    int groundBounceXSpeedIncrease = -1;

    boolean skyFall = false;
    boolean boost = false;
    public Point speed;

    private final Game game;
    private Bitmap image;
    public Rect rect;
    private Bitmap skyFallImage;
    private Bitmap boostImage;

    Character(Game game) {
        this.game = game;
        this.image = BitmapFactory.decodeResource(game.context.getResources(), R.drawable.hero);
        this.skyFallImage = BitmapFactory.decodeResource(game.context.getResources(), R.drawable.hero_sky_fall);
        this.boostImage = BitmapFactory.decodeResource(game.context.getResources(), R.drawable.hero_boost);
        int imageHeight = image.getHeight();
        int imageWidth = image.getWidth();
        int bottom = game.worldBounds.height() / 2;
        int top = bottom - imageHeight;
        int left = game.worldBounds.width() / 2 - imageWidth / 2;
        int right = left + imageWidth;
        this.rect = new Rect(left, top, right, bottom);
        this.speed = new Point(initialXSpeed, 0);
    }

    public void draw(Canvas canvas) {
        if (skyFall)
            canvas.drawBitmap(skyFallImage, rect.left, rect.top, null);
        else if (boost)
            canvas.drawBitmap(boostImage, rect.left, rect.top, null);
        else
            canvas.drawBitmap(image, rect.left, rect.top, null);
    }

    public void update() {
        final int ground = game.background.grassRect.top;
        if (rect.bottom < ground) { // in air
            if (rect.bottom < 0 && !skyFall) { // above Sky
                rect.offset(0, -game.worldBounds.height());
                skyFall = true;
                speed.y = skyFallSpeed;
            } else {
                speed.y += gravity;
            }
        } else { // hit ground
            if (boost) {
                final int newTop = game.background.grassRect.top - image.getHeight() - 1;
                rect.offsetTo(rect.left, newTop);
                bounce(ground, boostBounceSpeed);
            } else if (skyFall) {
                bounce(ground, skyfallBounceSpeed);
                speed.x += skyfallBounceXSpeedIncrease;
            } else {
                bounce(ground, groundBounceSpeed);
                speed.x += groundBounceXSpeedIncrease;
            }
        }

        int newBoosts = (int) Math.floor(points / 10);
        if (newBoosts > 0) {
            boosts += newBoosts;
            points = points % 10;
        }
        // Log.d("", "ySpeed: " + Math.abs(speed.y));
        rect.offset(0, speed.y);
    }

    void bounce(int otherY, double newSpeed) {
        speed.y = (int) -Math.abs(newSpeed);
        skyFall = false;
        boost = false;
    }

    public void boost() {
        if (boosts > 0) {
            boost = true;
            speed.y = boostSpeed;
            boosts--;
        }
    }

    public void reset() {
        final int newTop = (game.worldBounds.height() / 2) + image.getHeight();
        rect.offsetTo(rect.left, newTop);
        speed.x = initialXSpeed;
        speed.y = 0;
    }
}


