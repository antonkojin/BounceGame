package com.androidauthority.a2dgame;

import android.content.Context;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.view.MotionEvent;
import android.view.SurfaceView;
import android.view.SurfaceHolder;

public class GameView extends SurfaceView implements SurfaceHolder.Callback {
    private MainThread thread;
    private CharacterSprite characterSprite;

    public GameView(Context context) {
        super(context);

        final SurfaceHolder surfaceHolder = getHolder();
        surfaceHolder.addCallback(this);

        // TODO where to istantiate the MainThread? mh, mhh
        thread = new MainThread(surfaceHolder, this);
        // TODO the surfaceHolder belongs to me GameView, never geve it up!

        setFocusable(true);

    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
        // what is happaning to the surface size? some logs please ...
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        // TODO good it's here, for later
        // TODO some logs please
        return super.onTouchEvent(event);
    }


    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        characterSprite = new CharacterSprite(BitmapFactory.decodeResource(getResources(),R.drawable.avdgreen));

        // TODO here we are starting the MainThread
        thread.setRunning(true);
        thread.start();

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        // TODO why doin' it here, why doin' it?
        // mhh, maybe when the surface isn't here there's no reason to run u.u
        boolean retry = true;
        while (retry) {
            try {
                thread.setRunning(false);
                thread.join();

        } catch(InterruptedException e){
            e.printStackTrace();
        }
        retry = false;
    }
    }

    public void update() {
        // TODO move to MainThread, reponsability, this draws
        characterSprite.update();

    }

    @Override
    public void draw(Canvas canvas)
    {

        super.draw(canvas);
        if(canvas!=null) {
            characterSprite.draw(canvas);

        }
    }


}







