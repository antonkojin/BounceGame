package com.androidauthority.a2dgame;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceView;
import android.view.SurfaceHolder;

import java.util.LinkedList;
import java.util.List;
import java.util.Random;

public class GameSurfaceViewRunnable extends SurfaceView implements SurfaceHolder.Callback, Runnable {
    private final SurfaceHolder surfaceHolder;
    private Thread thread;
    private Character character;
    private List<Villain> villains;
    private Cards cards;
    private boolean running;
    private long lastVillainTime;
    private final double second = 1e9;
    private double villainDelta = second * 1.5;
    private Context context;
    private double points;
    private double cardsPointsThreshold;

    public GameSurfaceViewRunnable(Context context) {
        super(context);
        this.context = context;
        setFocusable(true);
        surfaceHolder = getHolder();
        surfaceHolder.addCallback(this);
        character = new Character(context);
        villains = new LinkedList<>();
        lastVillainTime = System.nanoTime();
        cards = null;
        points = 0;
        cardsPointsThreshold = 10;
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
        // TODO what is happening to the surface size? some logs please ...
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (cards == null) {
            character.moveTo((int) event.getX());
        } else {
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
        int frameCount = 0;
        final int TARGET_FPS = 60;
        final long TARGET_TIME = 1000000000 / TARGET_FPS;

        while (running) {
            startTime = System.nanoTime();

            if (this.cards == null) {
                update();
            }
            render();

            endTime = System.nanoTime();
            deltaTime = endTime - startTime;
            waitTime = TARGET_TIME - deltaTime;
            if (waitTime > 0) {
                try {
                    Thread.sleep(waitTime / 1000000);
                } catch (InterruptedException e) {
                    Log.e("GameSurfaceView", "run: ", e);
                }
            }

            long frameTime = System.nanoTime() - startTime;
            totalTime += frameTime;
            frameCount++;
            if (totalTime >= 1e9) {
                int realFPS = frameCount;
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
        // villains contacts
        List<Villain> villainsToRemove = new LinkedList<Villain>();
        for(Villain v: villains) {
            if(Rect.intersects(v.rect, character.rect)) {
                villainsToRemove.add(v);
                this.points++;
                Log.i("", "points: " + this.points);
            }
            if(v.rect.top >= Resources.getSystem().getDisplayMetrics().heightPixels) {
                villainsToRemove.add(v);
                Log.i("", "enemy out of bottom bounds");
            }
        }
        villains.removeAll(villainsToRemove);

        // update character and villains
        character.update();
        for (Villain v: villains) {
            v.update();
        }

        // spawn villain
        if (System.nanoTime() - lastVillainTime >= villainDelta) {
            Villain v = new Villain(context);
            villains.add(v);
            lastVillainTime = System.nanoTime();
        }

        // spawn cards
        if (points >= cardsPointsThreshold) {
            this.cards = new Cards(context);
        }
    }

    @Override
    public void draw(Canvas canvas) {
        if(canvas != null) {
            super.draw(canvas);
            character.draw(canvas);
            for (Villain v : villains) {
                v.draw(canvas);
            }
            if (cards != null) {
                cards.draw(canvas);
            }
        }
    }


}







