package com.androidauthority.a2dgame;

import android.content.Context;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceView;
import android.view.SurfaceHolder;

public class GameSurfaceViewRunnable extends SurfaceView implements SurfaceHolder.Callback, Runnable {
    private final SurfaceHolder surfaceHolder;
    private Thread thread;
    private CharacterSprite characterSprite;
    private boolean running;

    public GameSurfaceViewRunnable(Context context) {
        super(context);
        setFocusable(true);
        surfaceHolder = getHolder();
        surfaceHolder.addCallback(this);
        characterSprite = new CharacterSprite(BitmapFactory.decodeResource(getResources(), R.drawable.avdgreen));
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
        // TODO what is happaning to the surface size? some logs please ...
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        // TODO good it's here, for later
        // TODO some logs please
        return super.onTouchEvent(event);
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

            //update();
            render();

            endTime = System.nanoTime();
            deltaTime = endTime - startTime;
            waitTime = TARGET_TIME - deltaTime;
            /*if (false || waitTime > 0) {
                try {
                    Thread.sleep(waitTime / 1000000);
                } catch (InterruptedException e) {
                    Log.e("GameSurfaceView", "run: ", e);
                }
            }*/

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

    public void update() {
        characterSprite.update();

    }

    @Override
    public void draw(Canvas canvas) {
        if (canvas != null) {
            super.draw(canvas);
            characterSprite.draw(canvas);
        }
    }


}







