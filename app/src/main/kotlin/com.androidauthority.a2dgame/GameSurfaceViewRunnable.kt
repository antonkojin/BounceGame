import android.annotation.SuppressLint
import android.content.Context
import android.content.res.Resources
import android.graphics.Canvas
import android.graphics.Rect
import android.util.Log
import android.view.MotionEvent
import android.view.SurfaceView
import android.view.SurfaceHolder
import java.lang.System.nanoTime

import java.util.LinkedList

class GameSurfaceViewRunnable(context: Context) : SurfaceView(context), SurfaceHolder.Callback, Runnable {
    private val surfaceHolder: SurfaceHolder
    private var thread: Thread? = null
    private val character: Character
    private val points: MutableList<Point>
    private var cards: Cards?
    private var pause: Boolean = true
    private var running: Boolean = false
    private var lastPointTime: Long = 0
    private val second = 1e9
    private val pointDelta = second * 1.5
    private var pointsCount: Double = 0.toDouble()
    private var cardsPointsThreshold: Double = 0.toDouble()

    init {
        isFocusable = true
        surfaceHolder = holder
        surfaceHolder.addCallback(this)
        character = Character(context)
        points = LinkedList()
        lastPointTime = nanoTime()
        cards = null
        pointsCount = 0.0
        cardsPointsThreshold = 10.0
    }

    override fun surfaceChanged(holder: SurfaceHolder, format: Int, width: Int, height: Int) {
        // TODO what is happening to the surface size? some logs please ...
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onTouchEvent(event: MotionEvent): Boolean {
        if (cards == null) {
            if (event.getAction() == MotionEvent.ACTION_DOWN) pause = false
            else if (event.getAction() == MotionEvent.ACTION_UP) pause = true
            if (!pause) {
                character.moveTo(event.x.toInt())
            }
        } else if (event.getAction() == MotionEvent.ACTION_DOWN) {
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
            startTime = nanoTime()

            if (!pause) {
                update()
            }
            render()

            endTime = nanoTime()
            deltaTime = endTime - startTime
            waitTime = targetTime - deltaTime
            if (waitTime > 0) {
                try {
                    Thread.sleep(waitTime / 1000000)
                } catch (e: InterruptedException) {
                    Log.e("GameSurfaceView", "run: ", e)
                }

            }

            val frameTime = nanoTime() - startTime
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
        // points contacts
        val pointsToRemove = LinkedList<Point>()
        for (v in points) {
            if (Rect.intersects(v.rect, character.rect)) {
                pointsToRemove.add(v)
                this.pointsCount++
                Log.i("", "pointsCount: " + this.pointsCount)
            }
            if (v.rect.top >= Resources.getSystem().displayMetrics.heightPixels) {
                pointsToRemove.add(v)
                Log.i("", "enemy out of bottom bounds")
            }
        }
        points.removeAll(pointsToRemove)

        // update character and points
        character.update()
        points.forEach { it.update() }

        // spawn point
        if (nanoTime() - lastPointTime >= pointDelta) {
            val v = Point(context)
            points.add(v)
            lastPointTime = nanoTime()
        }

        // spawn cards
        if (pointsCount >= cardsPointsThreshold) {
            this.cards = Cards(context)
            this.pause = true
        }
    }

    override fun draw(canvas: Canvas?) {
        if (canvas != null) {
            super.draw(canvas)
            character.draw(canvas)
            for (v in points) {
                v.draw(canvas)
            }
            if (cards != null) {
                cards!!.draw(canvas)
            }
        }
    }


}







