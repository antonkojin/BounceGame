package com.androidauthority.a2dgame;


import android.graphics.Canvas;
import android.view.SurfaceHolder;

public class MainThread extends Thread {
    private GameView gameView;
    private SurfaceHolder surfaceHolder;
    private boolean running;
    public static Canvas canvas;
    private int targetFPS = 30;
    private double averageFPS;


    public MainThread(SurfaceHolder surfaceHolder, GameView gameView) {
        super();
        this.surfaceHolder = surfaceHolder;
        this.gameView = gameView;
    }

    @Override
    public void run() {
        // TODO responsability issue?
        /*
        this receves the surfaceHolder by GameView,
        after telling GameView to update, tells GameView to draw but
        passing the canvas which is obtained with the surfaceHolder.
        maybe we are only managing time, but why not update and draw in the GameView?
        Responsability
         */

        long startTime;
        long timeMillis;
        long waitTime;
        long totalTime = 0;
        int frameCount =0;
        long targetTime = 1000/targetFPS;


        while(running) {
            startTime = System.nanoTime();
            canvas = null;

            try {
                canvas = this.surfaceHolder.lockCanvas();
                synchronized (surfaceHolder) {
                    this.gameView.update();
                    this.gameView.draw(canvas);
                }
            } catch (Exception e) {
                // TODO wtf, oh java
            }
            finally {
                if(canvas!=null)
                {
                    try {
                        surfaceHolder.unlockCanvasAndPost(canvas);
                    }
                    catch(Exception e){e.printStackTrace();}
                }
            }

            timeMillis = (System.nanoTime() - startTime) / 1000000;
            waitTime = targetTime-timeMillis;

            try{
                this.sleep(waitTime);
            }catch(Exception e){}

            totalTime += System.nanoTime()-startTime;
            frameCount++;
            if(frameCount == targetFPS)
            {
                averageFPS = 1000/((totalTime/frameCount)/1000000);
                frameCount =0;
                totalTime = 0;
                System.out.println(averageFPS);
            }
        }

    }

    public void setRunning(boolean isRunning) {
        running = isRunning;
    }
}
