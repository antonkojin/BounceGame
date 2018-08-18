package com.antonkojin.game;

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
    public double pointsCount;
    public double cardsPointsThreshold;
    private Character character;
    private SurfaceHolder surfaceHolder;
    private Cards cards;
    private Thread thread = null;
    private List<Point> points;
    private Hud hud;
    private static double second = 1e9;
    private boolean running = false;
    private long lastPointTime;
    static double pointDelta = second * 1.5;
    static double baddieDelta = second * 1.5;
    public Rect worldBounds;
    private boolean pause = false; // TODO set true
    private long lastBaddieTime;
    private List<Baddie> baddies;
    private int cardsPointsThresholdMultiplier = 2;

    public GameSurfaceViewRunnable(Context context) {
        super(context);
        setFocusable(true);
        surfaceHolder = getHolder();
        surfaceHolder.addCallback(this);
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

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
        // TODO what is happening to the surface size? some logs please ...
    }

    @Override
    @SuppressLint("ClickableViewAccessibility")
    public boolean onTouchEvent(MotionEvent event) {
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
            // do the things
            if (!pause) {
                update();
            }
            render();
            // sleep if in time
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
            // log FPS
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
            // here
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
        if ((int) pointsCount >= (int) cardsPointsThreshold) {
            this.cards = new Cards(getContext());
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
            Point v = new Point(getContext());
            points.add(v);
            lastPointTime = nanoTime();
        }
        // spawn baddie
        if (nanoTime() - lastBaddieTime >= baddieDelta) {
            Baddie v = new Baddie(getContext());
            baddies.add(v);
            lastBaddieTime = nanoTime();
        }
    }

    @Override
    public void draw(final Canvas canvas) {
        if(canvas != null) {
            super.draw(canvas);
            character.draw(canvas);
            for (Point v : points) v.draw(canvas);
            for (Baddie b : baddies) b.draw(canvas);
            hud.draw(canvas);
            if (cards != null) {
                cards.draw(canvas);
            }
        }
    }


}







