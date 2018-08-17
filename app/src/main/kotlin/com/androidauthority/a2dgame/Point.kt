package com.androidauthority.a2dgame

import android.content.Context
import android.content.res.Resources
import android.graphics.*
import java.util.*

class Point(private val context: Context) {
    private val image: Bitmap
    var rect: Rect
    private val velocity: Int


    init {
        this.image = BitmapFactory.decodeResource(this.context.resources, R.drawable.money)
        val imageHeight = this.image.height
        val imageWidth = this.image.width
        val r = Random()
        val screenHeight = Resources.getSystem().displayMetrics.heightPixels
        val screenWidth = Resources.getSystem().displayMetrics.widthPixels
        this.velocity = r.nextInt(10) + 3
        val bounds = 50
        val left = r.nextInt(screenWidth - imageWidth - (2 * bounds)) + bounds
        val right = left + imageWidth
        val bottom = 0
        val top = bottom - imageHeight
        this.rect = Rect(left, top, right, bottom)
    }

    fun draw(canvas: Canvas) {
        canvas.drawBitmap(image, rect.left.toFloat(), rect.top.toFloat(), null)
//        val p = Paint()
//        p.style = Paint.Style.FILL_AND_STROKE
//        p.color = Color.RED
//        canvas.drawRect(this.rect, p)
    }

    fun update() {
        val top = rect.top + velocity
        val bottom = rect.bottom + velocity
        rect.set(rect.left, top, rect.right, bottom)
    }
}


