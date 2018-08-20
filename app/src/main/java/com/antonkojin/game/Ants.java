package com.antonkojin.game;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Rect;

import java.util.LinkedList;
import java.util.List;
import java.util.Random;

import static java.lang.System.nanoTime;

@SuppressWarnings({"FieldCanBeLocal", "WeakerAccess"})
public class Ants {

    private final Game game;
    static double spawnDelta = Game.second * 3;
    private final Bitmap image;
    private final int minYVelocity = 3;
    private final int maxYVelocity = 6;
    List<Ant> ants;
    private long lastSpawnTime;

    Ants(Game game) {
        this.game = game;
        this.image = BitmapFactory.decodeResource(game.context.getResources(), R.drawable.ant);
        ants = new LinkedList<>();
        lastSpawnTime = nanoTime();
    }

    public void draw(Canvas canvas) {
        for (Ant a : ants) a.draw(canvas);
    }

    public void update() {
        // update
        List<Ant> deaths = new LinkedList<>();
        for (Ant ant : ants) {
            boolean dead = ant.update();
            if (dead) {
                deaths.add(ant);
            }
        }
        // remove
        ants.removeAll(deaths);
        // spawn
        if (nanoTime() - lastSpawnTime >= spawnDelta) {
            ants.add(new Ants.Ant());
            lastSpawnTime = nanoTime();
        }
    }

    private class Ant {
        Rect rect;
        private int xVelocity = 0;
        private int yVelocity;

        public Ant() {
            int imageHeight = image.getHeight();
            int imageWidth = image.getWidth();
            Random r = new Random();
            int screenWidth = Resources.getSystem().getDisplayMetrics().widthPixels;
            int bounds = 50;
            int left = r.nextInt(screenWidth - imageWidth - (2 * bounds)) + bounds;
            int right = left + imageWidth;
            int bottom = 0;
            int top = bottom - imageHeight;
            rect = new Rect(left, top, right, bottom);
            yVelocity = r.nextInt(maxYVelocity - minYVelocity) + minYVelocity;
        }

        void draw(Canvas canvas) {
            canvas.drawBitmap(image, rect.left, rect.top, null);
        }


        public boolean update() {
            boolean dead = false;
            if (Rect.intersects(rect, game.character.rect)) {
                dead = true;
                game.pointsCount--;
            }
            if (rect.top >= game.worldBounds.bottom) {
                dead = true;
            }
            int dx = xVelocity;
            int dy = yVelocity;
            rect.offset(dx, dy);
            return dead;
        }

    }
}


