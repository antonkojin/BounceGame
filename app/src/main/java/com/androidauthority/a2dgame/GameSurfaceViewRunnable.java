package com.androidauthority.a2dgame;

import android.content.Context;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
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
    private boolean running;
    private long lastVillainTime;
    private long villainDelta = 1500000000;

    public GameSurfaceViewRunnable(Context context) {
        super(context);
        setFocusable(true);
        surfaceHolder = getHolder();
        surfaceHolder.addCallback(this);
        character = new Character();
        villains = new LinkedList<>();
        lastVillainTime = System.nanoTime();
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
        // TODO what is happening to the surface size? some logs please ...
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        character.moveTo((int) event.getX());
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

            update();
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
        character.update();
        for (Villain v: villains) {
            v.update();
        }
        if (System.nanoTime() - lastVillainTime >= villainDelta) {
            Villain v = new Villain();
            villains.add(v);
            lastVillainTime = System.nanoTime();
        }
    }

    @Override
    public void draw(Canvas canvas) {
        if (canvas != null) {
            super.draw(canvas);
            character.draw(canvas);
            for (Villain v: villains) {
                v.draw(canvas);
            }
        }
    }


}







