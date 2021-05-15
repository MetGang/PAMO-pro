package com.example.pamok

import android.content.res.Resources
import android.graphics.Rect
import android.graphics.drawable.Drawable
import androidx.core.content.res.ResourcesCompat
import androidx.core.graphics.drawable.toBitmap

class TargetObject(startX: Int, startY: Int, resources: Resources) {
    private var x: Int = 0
    private var y: Int = 0
    private val width: Int = 250
    private val height: Int = 250
    private var speed: Pair<Double, Double> = Pair(10.0, 10.0)
    val image: Drawable? = ResourcesCompat.getDrawable(resources, R.drawable.target_24, null)

    init {
        x = startX
        y = startY
    }

    fun getBounds() : Rect {
        return Rect(x - width / 2, y - height / 2, x + width / 2, y + height / 2)
    }

    fun getPixel(coordX: Int, coordY: Int): Int?
    {
        val r = getBounds()
        return image?.toBitmap(width, height)?.getPixel(coordX - r.left, coordY - r.top)
    }

    fun move() {
        if ((x - height / 2 <= 0) || (x + height / 2 >= Resources.getSystem().displayMetrics.widthPixels))
        {
            speed = Pair(-speed.first, speed.second)
        }
        x += speed.first.toInt()
        if ((y - width / 2 <= 0) || (y + width / 2 >= Resources.getSystem().displayMetrics.heightPixels))
        {
            speed = Pair(speed.first, -speed.second)
        }
        y += speed.second.toInt()
    }

}