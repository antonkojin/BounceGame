package com.androidauthority.a2dgame

import android.content.Context
import android.content.res.Resources
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.graphics.Rect

class Cards(private val context: Context) {
    private val image: Bitmap = BitmapFactory.decodeResource(this.context.resources, R.drawable.card)
    var rectOne: Rect
    var rectTwo: Rect
    private val screenWidth: Int
    private val screenHeight: Int


    init {
        val imageHeight = this.image.height
        val imageWidth = this.image.width
        screenHeight = Resources.getSystem().displayMetrics.heightPixels
        screenWidth = Resources.getSystem().displayMetrics.widthPixels
        val space = (screenWidth - 2 * imageWidth) / 3
        val rightOne = space + imageWidth
        val leftTwo = rightOne + space
        val rightTwo = leftTwo + imageWidth
        val top = screenHeight / 2 - imageHeight / 2
        val bottom = top + imageHeight
        this.rectOne = Rect(space, top, rightOne, bottom)
        this.rectTwo = Rect(leftTwo, top, rightTwo, bottom)
    }

    fun draw(canvas: Canvas) {
        canvas.drawBitmap(image, rectOne.left.toFloat(), rectOne.top.toFloat(), null)
        canvas.drawBitmap(image, rectTwo.left.toFloat(), rectTwo.top.toFloat(), null)
    }
}


