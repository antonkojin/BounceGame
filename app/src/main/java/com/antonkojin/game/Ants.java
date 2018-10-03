package com.antonkojin.game;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Point;
import android.graphics.Rect;

import java.util.LinkedList;
import java.util.List;
import java.util.Random;

import static java.lang.Math.sqrt;
import static java.lang.System.nanoTime;

@SuppressWarnings({"FieldCanBeLocal", "WeakerAccess"})
public class Ants {

    private final Game game;
    static double spawnDelta = Game.second * 5;
    private final Bitmap antImage;
    private final Bitmap bulletImage;
    private final int minYVelocity = 3;
    private final int maxYVelocity = 6;
    List<Ant> ants;
    private long lastSpawnTime;
    private List<Bullet> bullets;

    Ants(Game game) {
        this.game = game;
        this.antImage = BitmapFactory.decodeResource(game.context.getResources(), R.drawable.ant);
        this.bulletImage = BitmapFactory.decodeResource(game.context.getResources(), R.drawable.bullet);
        ants = new LinkedList<>();
        bullets = new LinkedList<>();
        lastSpawnTime = nanoTime();

    }

    public void draw(Canvas canvas) {
        for (Ant a : ants) a.draw(canvas);
        for (Bullet b : bullets) b.draw(canvas);
    }

    public void update() {
        // update
        List<Ant> deadAnts = new LinkedList<>();
        for (Ant ant : ants) {
            boolean dead = ant.update();
            if (dead) {
                deadAnts.add(ant);
            }
        }
        List<Bullet> deadBullets = new LinkedList<>();
        for (Bullet bullet : bullets) {
            boolean dead = bullet.update();
            if (dead) {
                deadBullets.add(bullet);
            }
        }
        // remove
        ants.removeAll(deadAnts);
        bullets.removeAll(deadBullets);
        // spawn
        if (nanoTime() - lastSpawnTime >= spawnDelta) {
            ants.add(new Ants.Ant());
            lastSpawnTime = nanoTime();
        }
    }

    class Bullet {
        int velocity = 7;
        private int xVelocity;
        private int yVelocity;
        private Rect rect;

        public Bullet(Point source, Point destination) {
            int top = source.y - (bulletImage.getHeight() / 2);
            int bottom = top + bulletImage.getHeight();
            int left = source.x - (bulletImage.getWidth() / 2);
            int right = left + bulletImage.getWidth();
            rect = new Rect(left, top, right, bottom);
            double xSpace = destination.x - source.x;
            double ySpace = destination.y - source.y;
            double diagonalSpace = sqrt((xSpace*xSpace) + (ySpace*ySpace));
            double diagonalTime = diagonalSpace / velocity;
            xVelocity = (int) Math.round(xSpace / diagonalTime);
            yVelocity = (int) Math.round(ySpace / diagonalTime);
        }

        public void draw(Canvas canvas) {
            canvas.drawBitmap(bulletImage, rect.left, rect.top, null);
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

    class Ant {
        Rect rect;
        private int xVelocity = 0;
        private int yVelocity;
        private long lastBulletTime;
        private long bulletDeltaTime = Game.second * 2;

        public Ant() {
            int imageHeight = antImage.getHeight();
            int imageWidth = antImage.getWidth();
            Random r = new Random();
            int screenWidth = Resources.getSystem().getDisplayMetrics().widthPixels;
            int bounds = 50;
            int left = r.nextInt(screenWidth - imageWidth - (2 * bounds)) + bounds;
            int right = left + imageWidth;
            int bottom = 0;
            int top = bottom - imageHeight;
            rect = new Rect(left, top, right, bottom);
            yVelocity = r.nextInt(maxYVelocity - minYVelocity) + minYVelocity;
            lastBulletTime = nanoTime();
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
            final boolean bulletTime = nanoTime() - lastBulletTime >= bulletDeltaTime;
            if(bulletTime && rect.bottom < game.worldBounds.height() * 2 / 3) {
                fireBullet();
                lastBulletTime = nanoTime();
            }
            int dx = xVelocity;
            int dy = yVelocity;
            rect.offset(dx, dy);
            return dead;
        }

        private void fireBullet() {
            Point source = new Point(rect.centerX(), rect.bottom);
            Rect character = game.character.rect;
            Point destination = new Point(character.centerX(), character.top);
            bullets.add(new Bullet(source, destination));
        }

        void draw(Canvas canvas) {
            canvas.drawBitmap(antImage, rect.left, rect.top, null);
        }
    }
}


