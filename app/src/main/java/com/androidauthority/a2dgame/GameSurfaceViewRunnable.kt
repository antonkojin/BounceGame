package com.androidauthority.a2dgame

import android.annotation.SuppressLint
import android.content.Context
import android.content.res.Resources
import android.graphics.Canvas
import android.graphics.Rect
import android.util.Log
import android.view.MotionEvent
import android.view.SurfaceView
import android.view.SurfaceHolder

import java.util.LinkedList

class GameSurfaceViewRunnable(context: Context) : SurfaceView(context), SurfaceHolder.Callback, Runnable {
    private val surfaceHolder: SurfaceHolder
    private var thread: Thread? = null
    private val character: Character
    private val villains: MutableList<Villain>
    private var cards: Cards? = null
    private var running: Boolean = false
    private var lastVillainTime: Long = 0
    private val second = 1e9
    private val villainDelta = second * 1.5
    private var points: Double = 0.toDouble()
    private var cardsPointsThreshold: Double = 0.toDouble()

    init {
        isFocusable = true
        surfaceHolder = holder
        surfaceHolder.addCallback(this)
        character = Character(context)
        villains = LinkedList()
        lastVillainTime = System.nanoTime()
        cards = null
        points = 0.0
        cardsPointsThreshold = 10.0
    }

    override fun surfaceChanged(holder: SurfaceHolder, format: Int, width: Int, height: Int) {
        // TODO what is happening to the surface size? some logs please ...
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onTouchEvent(event: MotionEvent): Boolean {
        if (cards == null) {
            character.moveTo(event.x.toInt())
        } else {
            val x = event.x.toInt()
            val y = event.y.toInt()
            if (cards!!.rectOne.contains(x, y)) {
                Log.i("", "Card one")
                cards = null
                cardsPointsThreshold *= 1.5
            } else if (cards!!.rectTwo.contains(x, y)) {
                Log.i("", "Card two")
                cards = null
                cardsPointsThreshold *= 1.5
            }
        }
        return true
    }

    override fun surfaceCreated(holder: SurfaceHolder) {
        Log.d("", "surfaceCreated: ")
        thread = Thread(this)
        this.running = true
        thread!!.start()
    }

    override fun run() {
        var startTime: Long
        var endTime: Long
        var deltaTime: Long
        var waitTime: Long
        var totalTime: Long = 0
        var frameCount = 0
        val targetFPS = 60
        val targetTime = (1000000000 / targetFPS).toLong()

        while (running) {
            startTime = System.nanoTime()

            if (this.cards == null) {
                update()
            }
            render()

            endTime = System.nanoTime()
            deltaTime = endTime - startTime
            waitTime = targetTime - deltaTime
            if (waitTime > 0) {
                try {
                    Thread.sleep(waitTime / 1000000)
                } catch (e: InterruptedException) {
                    Log.e("GameSurfaceView", "run: ", e)
                }

            }

            val frameTime = System.nanoTime() - startTime
            totalTime += frameTime
            frameCount++
            if (totalTime >= 1e9) {
                val realFPS = frameCount
                Log.d("", "FPS: $realFPS")
                frameCount = 0
                totalTime = 0
            }
        }
    }

    private fun render() {
        var canvas: Canvas? = null
        try {
            canvas = surfaceHolder.lockCanvas()
            draw(canvas)
        } catch (e: Exception) {
            Log.e("GameSurfaceView:run", "", e)
        } finally {
            if (canvas != null) {
                surfaceHolder.unlockCanvasAndPost(canvas)
            }
        }
    }

    override fun surfaceDestroyed(holder: SurfaceHolder) {
        Log.d("", "surfaceDestroyed: ")
        try {
            running = false
            thread!!.join()
        } catch (e: InterruptedException) {
            Log.e("GameSurfaceView", "surfaceDestroyed: ", e)
        }

    }

    private fun update() {
        // villains contacts
        val villainsToRemove = LinkedList<Villain>()
        for (v in villains) {
            if (Rect.intersects(v.rect, character.rect)) {
                villainsToRemove.add(v)
                this.points++
                Log.i("", "points: " + this.points)
            }
            if (v.rect.top >= Resources.getSystem().displayMetrics.heightPixels) {
                villainsToRemove.add(v)
                Log.i("", "enemy out of bottom bounds")
            }
        }
        villains.removeAll(villainsToRemove)

        // update character and villains
        character.update()
        villains.forEach {it.update()}

        // spawn villain
        if (System.nanoTime() - lastVillainTime >= villainDelta) {
            val v = Villain(context)
            villains.add(v)
            lastVillainTime = System.nanoTime()
        }

        // spawn cards
        if (points >= cardsPointsThreshold) {
            this.cards = Cards(context)
        }
    }

    override fun draw(canvas: Canvas?) {
        if (canvas != null) {
            super.draw(canvas)
            character.draw(canvas)
            for (v in villains) {
                v.draw(canvas)
            }
            if (cards != null) {
                cards!!.draw(canvas)
            }
        }
    }


}







