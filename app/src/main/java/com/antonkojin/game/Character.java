package com.antonkojin.game;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Rect;

import java.util.LinkedList;
import java.util.List;

import static java.lang.Math.sqrt;

public class Character {

    private final Game game;
    private Bitmap image;
    public Rect rect;
    private int screenWidth;
    Rect magnet;
    int moveToX;
    private int speed;
    private List<Friend> friends;


    Character(Game game) {
        this.game = game;
        this.image = BitmapFactory.decodeResource(game.context.getResources(), R.drawable.hero);
        this.speed = 0;
        this.screenWidth = Resources.getSystem().getDisplayMetrics().widthPixels;
        int screenHeight = Resources.getSystem().getDisplayMetrics().heightPixels;
        int imageHeight = image.getHeight();
        int imageWidth = image.getWidth();
        int bottom = screenHeight - 200;
        int top = bottom - imageHeight;
        int left = screenWidth / 2 - imageWidth / 2;
        int right = left + imageWidth;
        this.rect = new Rect(left, top, right, bottom);
        this.moveToX = this.rect.centerX();
        friends = new LinkedList<>();

        this.magnet = new Rect(rect.left - (rect.width() * 2), rect.top - (rect.height() * 2), rect.right + (rect.width() * 2), rect.bottom);
    }

    public void draw(Canvas canvas) {
        for (Friend f : friends) f.draw(canvas);
        canvas.drawBitmap(image, rect.left, rect.top, null);
    }

    public void update() {
        // set speed to reach x
        int maxVelocity = 20;
        speed = (moveToX - rect.centerX()) / 10;
        if (speed > maxVelocity) speed = maxVelocity;
        else if (speed < -maxVelocity) speed = -maxVelocity;

        int left = rect.left + speed;
        int right = rect.right + speed;
        if (rect.left < 0) {
            left = 0;
            right = left + rect.width();
        }
        if (rect.right > screenWidth) {
            right = screenWidth;
            left = right - rect.width();
        }
        rect.set(left, rect.top, right, rect.bottom);
        magnet.offset(rect.centerX() - magnet.centerX(), 0);

        for (Friend f : friends) f.update();
    }

    void addFriend() {
        Friend friend = new Friend();
        friends.add(friend);
    }

    class Friend {
        Bitmap image;
        Rect rect;
        double velocity;
        int xVelocity, yVelocity;

        Friend() {
            image = BitmapFactory.decodeResource(game.context.getResources(), R.drawable.friend);
            int bottom = Character.this.rect.top - 30;
            int right = Character.this.rect.left - 30;
            int top = bottom - image.getHeight();
            int left = right - image.getWidth();
            rect = new Rect(left, top, right, bottom);
        }

        void update() {
            Ants.Ant target = null;
            for (Ants.Ant ant : game.ants.ants) {
                // TODO get nearest ant and fire at it
            }
            if (target != null) {
                fire(new Point(target.rect.centerX(), target.rect.centerY()));
            }

            int bottom = Character.this.rect.top - 30;
            int right = Character.this.rect.left - 30;
            int top = bottom - image.getHeight();
            int left = right - image.getWidth();
            rect.set(left, top, right, bottom);
        }

        void fire(Point destination) {
            Point source = new Point(rect.centerX(), rect.top);
            int top = source.y - (image.getHeight() / 2);
            int bottom = top + image.getHeight();
            int left = source.x - (image.getWidth() / 2);
            int right = left + image.getWidth();
            rect = new Rect(left, top, right, bottom);
            double xSpace = destination.x - source.x;
            double ySpace = destination.y - source.y;
            double diagonalSpace = sqrt((xSpace*xSpace) + (ySpace*ySpace));
            double diagonalTime = diagonalSpace / velocity;
            xVelocity = (int) Math.round(xSpace / diagonalTime);
            yVelocity = (int) Math.round(ySpace / diagonalTime);
        }

        void draw(Canvas canvas) {
            canvas.drawBitmap(image, rect.left, rect.top, null);
        }

    }
}


