package com.antonkojin.game;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import static java.lang.System.nanoTime;

public class SurfaceViewRunnable extends SurfaceView implements SurfaceHolder.Callback, Runnable {
    Game game;
    private SurfaceHolder surfaceHolder;
    private Thread thread = null;
    private boolean running = false;

    public SurfaceViewRunnable(Context context) {
        super(context);
        setFocusable(true);
        surfaceHolder = getHolder();
        surfaceHolder.addCallback(this);
        game = new Game(context);
    }

    @Override
    @SuppressLint("ClickableViewAccessibility")
    public boolean onTouchEvent(MotionEvent event) {
        game.touchEvent(event);
        return true;
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
            if (!game.pause) {
                game.update();
            }
            getCanvasAndDraw();
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
                // TODO
                frameCount = 0;
                totalTime = 0;
            }
        }
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        Log.d("", "surfaceCreated: ");
        thread = new Thread(this);
        this.running = true;
        thread.start();
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
        // TODO what is happening to the surface size? some logs please ...
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

    private void getCanvasAndDraw() {
        Canvas canvas = null;
        try {
            canvas = surfaceHolder.lockCanvas();
            // here
            this.draw(canvas);
        } catch (Exception e) {
            Log.e("GameSurfaceView:run", "", e);
        } finally {
            if (canvas != null) {
                surfaceHolder.unlockCanvasAndPost(canvas);
            }
        }
    }

    @Override
    public void draw(final Canvas canvas) {
        if (canvas != null) {
            super.draw(canvas);
            game.render(canvas);
        }
    }


}







