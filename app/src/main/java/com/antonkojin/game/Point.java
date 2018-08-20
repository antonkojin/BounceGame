package com.antonkojin.game;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Rect;

import java.util.Random;

public class Point {

    private final Bitmap image;
    public Rect rect;
    private final double minXVelocity = 1;
    private double gravity = 10;
    private double xVelocity = 0;
    private double maxXVelocity = 5;
    private Game game;


    Point(Context context, Game game) {
        this.game = game;
        this.image = BitmapFactory.decodeResource(context.getResources(), R.drawable.money);
        int imageHeight = this.image.getHeight();
        int imageWidth = this.image.getWidth();
        Random r = new Random();
        int screenWidth = Resources.getSystem().getDisplayMetrics().widthPixels;
        // this.gravity = r.nextInt((int) (maxVelocity - minVelocity)) + minVelocity;
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

    public void update() {
        // character absorb
        if (Rect.intersects(this.rect, game.character.rect)) {
            game.points.remove(this);
            game.pointsCount++;
        }
        // bottom out destroy
        if (rect.top >= Resources.getSystem().getDisplayMetrics().heightPixels) {
            game.points.remove(this);
        }
        // update position
        this.rect.offset((int) xVelocity, (int) gravity);
    }

    public void moveTo(Rect magnet, Rect character) {
        if (character.left <= rect.left && character.right >= rect.right) {
            this.xVelocity = 0;
            return;
        }
        double maxDistance = (double) (magnet.height()) / 2.;
        double xDistance = magnet.centerX() - rect.centerX();
        double yDistance = magnet.centerY() - rect.centerY();
        double sign = xDistance > 0 ? 1 : -1;
        double module = yDistance;
        double force = maxDistance - module;
        double normalisedForce = force / maxDistance;
        double forceInRange = normalisedForce * (maxXVelocity - minXVelocity) + minXVelocity;
        this.xVelocity = forceInRange * sign;
    }
}


