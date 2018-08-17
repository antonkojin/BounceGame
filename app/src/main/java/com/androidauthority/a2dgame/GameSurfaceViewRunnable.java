package com.androidauthority.a2dgame;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import java.util.LinkedList;
import java.util.List;

import static java.lang.System.nanoTime;

public class GameSurfaceViewRunnable extends SurfaceView implements SurfaceHolder.Callback, Runnable {
    public double pointsCount = 0.;
    public double cardsPointsThreshold = 0.;
    private Character character;
    private SurfaceHolder surfaceHolder;
    private Cards cards;
    private Thread thread = null;
    private List<Point> points;
    private Hud hud;
    private boolean pause = true;
    private boolean running = false;
    private long lastPointTime = 0;
    private double second = 1e9;
    private double pointDelta = second * 1.5;

    public GameSurfaceViewRunnable(Context context) {
        super(context);
        setFocusable(true);
        surfaceHolder = getHolder();
        surfaceHolder.addCallback(this);
        character = new Character(context);
        points = new LinkedList<Point>();
        lastPointTime = nanoTime();
        cards = null;
        pointsCount = 0;
        cardsPointsThreshold = 10;
        hud = new Hud(this);
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
        // TODO what is happening to the surface size? some logs please ...
    }

    @Override
    @SuppressLint("ClickableViewAccessibility")
    public boolean onTouchEvent(MotionEvent event) {
        if (cards == null) {
            if (event.getAction() == MotionEvent.ACTION_DOWN) pause = false;
            else if (event.getAction() == MotionEvent.ACTION_UP) pause = true;
            if (!pause) {
                character.moveTo((int) event.getX());
            }
        } else if (event.getAction() == MotionEvent.ACTION_DOWN) {
            int x = (int) event.getX();
            int y = (int) event.getY();
            if (cards.rectOne.contains(x, y)) {
                Log.i("", "Card one");
                cards = null;
                cardsPointsThreshold *= 1.5;
            } else if (cards.rectTwo.contains(x, y)) {
                Log.i("", "Card two");
                cards = null;
                cardsPointsThreshold *= 1.5;
            }
        }
        return true;
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        Log.d("", "surfaceCreated: ");
        thread = new Thread(this);
        this.running = true;
        thread.start();
    }

    @Override
    public void run() {
        long startTime;
        long endTime;
        long deltaTime;
        long waitTime;
        long totalTime = 0;
        long frameCount = 0;
        long targetFPS = 60;
        long targetTime = (1000000000 / targetFPS);

        while (running) {
            startTime = nanoTime();

            if (!pause) {
                update();
            }
            render();

            endTime = nanoTime();
            deltaTime = endTime - startTime;
            waitTime = targetTime - deltaTime;
            if (waitTime > 0) {
                try {
                    Thread.sleep(waitTime / 1000000);
                } catch (InterruptedException e) {
                    Log.e("GameSurfaceView", "run: ", e);
                }

            }

            long frameTime = nanoTime() - startTime;
            totalTime += frameTime;
            frameCount++;
            if (totalTime >= 1e9) {
                long realFPS = frameCount;
                Log.d("", "FPS: " + realFPS);
                frameCount = 0;
                totalTime = 0;
            }
        }
    }

    private void render() {
        Canvas canvas = null;
        try {
            canvas = surfaceHolder.lockCanvas();
            draw(canvas);
        } catch (Exception e) {
            Log.e("GameSurfaceView:run", "", e);
        } finally {
            if (canvas != null) {
                surfaceHolder.unlockCanvasAndPost(canvas);
            }
        }
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        Log.d("", "surfaceDestroyed: ");
        try {
            running = false;
            thread.join();
        } catch (InterruptedException e) {
            Log.e("GameSurfaceView", "surfaceDestroyed: ", e);
        }
    }

    private void update() {
        // spawn cards
        if (pointsCount >= cardsPointsThreshold) {
            this.cards = new Cards(getContext());
            this.pause = true;
        }

        // points contacts
        List<Point> pointsToRemove = new LinkedList<Point>();
        for (Point v : points) {
            if (Rect.intersects(v.rect, character.rect)) {
                pointsToRemove.add(v);
                this.pointsCount++;
                Log.i("", "pointsCount: " + this.pointsCount);
            }
            if (v.rect.top >= Resources.getSystem().getDisplayMetrics().heightPixels) {
                pointsToRemove.add(v);
                Log.i("", "enemy out of bottom bounds");
            }
        }
        points.removeAll(pointsToRemove);

        // update character and points
        character.update();
        for (Point p : points) {
            p.update();
        }

        // spawn point
        if (nanoTime() - lastPointTime >= pointDelta) {
            Point v = new Point(getContext());
            points.add(v);
            lastPointTime = nanoTime();
        }
    }

    @Override
    public void draw(Canvas canvas) {
        if(canvas != null) {
            super.draw(canvas);
            character.draw(canvas);
            for (Point v : points) {
                v.draw(canvas);
            }
            if (cards != null) {
                cards.draw(canvas);
            }
            hud.draw(canvas);
        }
    }


}







