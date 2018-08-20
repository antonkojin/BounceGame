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
class Points {

    static double spawnDelta = Game.second * 1.5;
    private final Bitmap image;
    double minXVelocity = 1;
    double maxXVelocity = 5;
    double gravity = 10;
    private Game game;
    private List<Point> points;
    private double lastSpawnTime;


    Points(Game game) {
        this.game = game;
        this.image = BitmapFactory.decodeResource(game.context.getResources(), R.drawable.money);
        points = new LinkedList<>();
        lastSpawnTime = nanoTime();
    }

    public void draw(Canvas canvas) {
        for (Point p : points) {
            p.draw(canvas);
        }
    }

    public void update() {
        // update
        List<Point> deaths = new LinkedList<>();
        for (Point point : this.points) {
            boolean dead = point.update();
            if (dead) {
                deaths.add(point);
            }
        }
        // remove
        points.removeAll(deaths);
        // spawn
        if (nanoTime() - lastSpawnTime >= spawnDelta) {
            points.add(new Point());
            lastSpawnTime = nanoTime();
        }
    }

    private class Point {

        private final Rect rect;
        private double xVelocity;

        Point() {
            int imageHeight = Points.this.image.getHeight();
            int imageWidth = Points.this.image.getWidth();
            int screenWidth = Resources.getSystem().getDisplayMetrics().widthPixels;
            int bounds = 50;
            Random r = new Random();
            int left = r.nextInt(screenWidth - imageWidth - (2 * bounds)) + bounds;
            int right = left + imageWidth;
            int bottom = 0;
            int top = bottom - imageHeight;
            this.rect = new Rect(left, top, right, bottom);
        }

        void draw(Canvas canvas) {
            canvas.drawBitmap(Points.this.image, this.rect.left, this.rect.top, null);
        }

        boolean update() {
            boolean dead = false;
            // character absorb
            if (Rect.intersects(this.rect, game.character.rect)) {
                dead = true;
                game.pointsCount++;
            }
            // bottom out destroy
            if (rect.top >= Resources.getSystem().getDisplayMetrics().heightPixels) {
                dead = true;
            }
            // magnet attraction
            if (game.character.magnet.contains(this.rect)) {
                this.magnetAttraction(game.character.magnet, game.character.rect);
            }
            // update position
            this.rect.offset((int) xVelocity, (int) Points.this.gravity);
            return dead;
        }

        void magnetAttraction(Rect magnet, Rect character) {
            if (character.left <= rect.left && character.right >= rect.right) {
                this.xVelocity = 0;
                return;
            }
            double maxDistance = (double) (magnet.height()) / 2.;
            double xDistance = magnet.centerX() - rect.centerX();
            double yDistance = magnet.centerY() - rect.centerY();
            double sign = xDistance > 0 ? 1 : -1;
            @SuppressWarnings("UnnecessaryLocalVariable")
            double module = yDistance;
            double force = maxDistance - module;
            double normalisedForce = force / maxDistance;
            double forceInRange = normalisedForce * (maxXVelocity - minXVelocity) + minXVelocity;
            this.xVelocity = forceInRange * sign;
        }

    }

}


