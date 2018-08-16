package com.androidauthority.a2dgame

import android.content.Context
import android.content.res.Resources
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.graphics.Rect

class Character(private val context: Context) {
    private val image: Bitmap = BitmapFactory.decodeResource(this.context.resources, R.drawable.hero)
    var rect: Rect
    private val screenWidth: Int = Resources.getSystem().displayMetrics.widthPixels
    private val screenHeight: Int = Resources.getSystem().displayMetrics.heightPixels
    private var velocity = 20


    init {
        val imageHeight = image.height
        val imageWidth = image.width
        val bottom = screenHeight - 200
        val top = bottom - imageHeight
        val left = screenWidth / 2 - imageWidth / 2
        val right = left + imageWidth
        this.rect = Rect(left, top, right, bottom)
    }

    fun draw(canvas: Canvas) {
        canvas.drawBitmap(image, rect.left.toFloat(), rect.top.toFloat(), null)
    }

    fun update() {
        // Log.d("", "character x: " + rect.centerX() + " y:  " + rect.centerY());
        // TODO get a reference to the World? has see things to interact with
    }

    fun moveTo(x: Int) {
        var left = rect.left
        var right = rect.right
        val error = 10
        if (x > rect.centerX() + error) {
            left = rect.left + velocity
            right = rect.right + velocity
        } else if (x < rect.centerX() - error) {
            left = rect.left - velocity
            right = rect.right - velocity
        }
        if (rect.left < 0) {
            left = 0
            right = left + rect.width()
        }
        if (rect.right > screenWidth) {
            right = screenWidth
            left = right - rect.width()
        }
        rect.set(left, rect.top, right, rect.bottom)
    }
}


