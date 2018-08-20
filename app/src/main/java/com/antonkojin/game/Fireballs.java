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

@SuppressWarnings("WeakerAccess")
public class Fireballs {

    long lastSpawnTime;
    private final Bitmap image;
    double spawnDelta = Game.second * 1.5;
    private Game game;
    private List<Fireball> fireballs;
    static int maxVelocity = 15;
    static int minVelocity = 5;

    public Fireballs(Game game) {
        this.game = game;
        this.image = BitmapFactory.decodeResource(game.context.getResources(), R.drawable.fireball);
        this.fireballs = new LinkedList<>();
        lastSpawnTime = nanoTime();
    }

    public void draw(Canvas canvas) {
        for (Fireball f : fireballs) {
            f.draw(canvas);
        }
    }

    public void update() {
        // update
        List<Fireball> deaths = new LinkedList<>();
        for (Fireball fireball : fireballs) {
            boolean dead = fireball.update();
            if (dead) {
                deaths.add(fireball);
            }
        }
        // remove
        fireballs.removeAll(deaths);
        // spawn
        if (nanoTime() - lastSpawnTime >= spawnDelta) {
            fireballs.add(new Fireball());
            lastSpawnTime = nanoTime();
        }
    }

    class Fireball {

        private final int gravity;
        private final Rect rect;

        Fireball() {
            int imageHeight = image.getHeight();
            int imageWidth = image.getWidth();
            Random r = new Random();
            int screenWidth = Resources.getSystem().getDisplayMetrics().widthPixels;
            this.gravity = r.nextInt(maxVelocity - minVelocity) + minVelocity;
            int bounds = 50;
            int left = r.nextInt(screenWidth - imageWidth - (2 * bounds)) + bounds;
            int right = left + imageWidth;
            int bottom = 0;
            int top = bottom - imageHeight;
            this.rect = new Rect(left, top, right, bottom);
        }

        public void draw(Canvas canvas) {
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
            int dx = 0;
            @SuppressWarnings("UnnecessaryLocalVariable")
            int dy = gravity;
            rect.offset(dx, dy);
            return dead;
        }
    }
}


