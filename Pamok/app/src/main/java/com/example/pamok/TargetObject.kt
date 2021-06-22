package com.example.pamok

import android.content.res.Resources
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Point
import android.graphics.Rect
import android.graphics.drawable.Drawable
import android.util.Log
import androidx.core.content.res.ResourcesCompat
import androidx.core.graphics.drawable.toBitmap
import kotlin.math.abs
import kotlin.math.pow
import kotlin.math.roundToInt
import kotlin.math.sqrt

class TargetObject(startX: Int, startY: Int, resources: Resources) {
    private var baseY: Int = startY
    private val createTime: Long = System.currentTimeMillis()
    private val g: Double = 9.80665
    private val height: Int = 250
    private val image: Drawable? = ResourcesCompat.getDrawable(resources, R.drawable.target_24, null)
    private var isCut: Boolean = false
    private val v: Double = sqrt(4 / 3 * startY * g)
    private val width: Int = height
    private var x: Int = startX
    private var y: Int = baseY

    fun cut() {
        isCut = true
    }

    fun draw(canvas: Canvas) {
        if (!isCut) {
            image?.bounds = getBounds()
            image?.draw(canvas)
        }
    }

    private fun getBounds(): Rect {
        return Rect(x - width / 2, y - height / 2, x + width / 2, y + height / 2)
    }

    private fun getPixelColor(coordX: Int, coordY: Int): Int? {
        val r: Rect = getBounds()
        return image?.toBitmap(width, height)?.getPixel(coordX - r.left, coordY - r.top)
    }

    fun intersectsWith(line: Pair<Point?, Point?>): Boolean {
        val r: Rect = getBounds()
        val lineX: Int = abs(line.second!!.x - line.first!!.x)
        val lineY: Int = line.second!!.y - line.first!!.y
        val ratio: Double = lineY / lineX.toDouble()
        val points: MutableList<Point> = mutableListOf()
        for (i: Int in 1..lineX) {
            val point = Point(line.first!!.x + if (line.first!!.x > line.second!!.x) -i else i, line.first!!.y + (i * ratio).roundToInt())
            if (r.contains(point.x, point.y) && isEdgePixel(point.x, point.y)) {
                points.add(point)
            }
        }
        return points.count() > 1
    }

    fun isCut(): Boolean {
        return isCut
    }

    private fun isEdgePixel(coordX: Int, coordY: Int): Boolean {
        if (getPixelColor(coordX, coordY) != Color.TRANSPARENT) {
            val surrounding: MutableList<Int?> = mutableListOf()
            surrounding.add(getPixelColor(coordX - 1, coordY - 1))
            surrounding.add(getPixelColor(coordX, coordY - 1))
            surrounding.add(getPixelColor(coordX + 1, coordY - 1))
            surrounding.add(getPixelColor(coordX - 1, coordY))
            surrounding.add(getPixelColor(coordX + 1, coordY))
            surrounding.add(getPixelColor(coordX - 1, coordY + 1))
            surrounding.add(getPixelColor(coordX, coordY + 1))
            surrounding.add(getPixelColor(coordX + 1, coordY + 1))
            return surrounding.any {
                it == Color.TRANSPARENT || it == null
            }
        } else {
            return false
        }
    }

    fun move(): Boolean {
        val t: Double = (System.currentTimeMillis() - createTime) / 1000.0
        y = baseY - (v * t - (g * t.pow(2.0) / 2)).roundToInt()
        return y < baseY || t < 1.0
    }
}