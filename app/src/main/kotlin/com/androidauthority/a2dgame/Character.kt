package com.androidauthority.a2dgame

import android.content.Context
import android.content.res.Resources
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.graphics.Rect

class Character(private val context: Context) {
    private val image: Bitmap
    var rect: Rect
    private val screenWidth: Int
    private val screenHeight: Int
    private var velocity: Int
    private var toX: Int


    init {
        this.velocity = 0
        this.image = BitmapFactory.decodeResource(this.context.resources, R.drawable.hero)
        this.screenWidth = Resources.getSystem().displayMetrics.widthPixels
        this.screenHeight = Resources.getSystem().displayMetrics.heightPixels
        val imageHeight = image.height
        val imageWidth = image.width
        val bottom = screenHeight - 200
        val top = bottom - imageHeight
        val left = screenWidth / 2 - imageWidth / 2
        val right = left + imageWidth
        this.rect = Rect(left, top, right, bottom)
        this.toX = this.rect.centerX()
    }

    fun draw(canvas: Canvas) {
        canvas.drawBitmap(image, rect.left.toFloat(), rect.top.toFloat(), null)
    }

    fun update() {
        // Log.d("", "character x: " + rect.centerX() + " y:  " + rect.centerY());
        // TODO get a reference to the World? has see things to interact with

        // set velocity to reach x
        val maxVelocity = 20
        velocity = (toX - rect.centerX()) / 10
        if (velocity > maxVelocity) velocity = maxVelocity
        else if (velocity < -maxVelocity) velocity = -maxVelocity

        var left = rect.left + velocity
        var right = rect.right + velocity
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

    fun moveTo(x: Int) {
        this.toX = x
    }
}


