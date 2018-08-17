package com.androidauthority.a2dgame

import android.content.Context
import android.graphics.Paint
import android.content.res.Resources
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.graphics.Rect
import android.graphics.Color

class Hud(private val context: Context, private val game: GameSurfaceViewRunnable) {

    init {
        val screenHeight = Resources.getSystem().displayMetrics.heightPixels
        val screenWidth = Resources.getSystem().displayMetrics.widthPixels
    }

    fun draw(canvas: Canvas) {
        val s = """ ${game.pointsCount.toLong()}/${game.cardsPointsThreshold.toLong()} """
        val p = Paint()
        val x: Float = 0f
        val y: Float = 100f
        p.setColor(Color.RED)
        p.setTextSize(100f)
        canvas.drawText(s, x, y, p)
    }
}


