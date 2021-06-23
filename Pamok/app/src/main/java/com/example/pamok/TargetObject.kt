package com.example.pamok

import android.content.res.Resources
import android.graphics.Canvas
import android.graphics.Point
import android.graphics.Rect
import android.graphics.drawable.Drawable
import android.util.Log
import androidx.core.content.res.ResourcesCompat
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

    fun intersectsWith(line: Pair<Point?, Point?>): Boolean {
        var m: Double = (line.second!!.y - line.first!!.y) / (line.second!!.x - line.first!!.x).toDouble()
        m = if (m == 0.0) 1.0 else m
        val a: Double = 1 + m.pow(2)
        val b: Double = 2 * (-x * m * (m * line.first!!.x + line.first!!.y - y))
        val c: Double = x.toDouble().pow(2) + m.pow(2) * line.first!!.x.toDouble().pow(2) + line.first!!.y.toDouble().pow(2) - 2 * m * line.first!!.x * (line.first!!.y + y) + y.toDouble().pow(2) + 2 * line.first!!.y * y - (width / 2.0).pow(2)
        val top = if (line.first!!.y < line.second!!.y) line.first!!.y else line.second!!.y
        val bottom = if (line.first!!.y < line.second!!.y) line.second!!.y else line.first!!.y
        val left = if (line.first!!.x < line.second!!.x) line.first!!.x else line.second!!.x
        val right = if (line.first!!.x < line.second!!.x) line.second!!.x else line.first!!.x
        return (b.pow(2) - (4 * a * c) > 0)
                && (top <= y + height / 4) && (bottom >= y - height / 4)
                && (left <= x + width / 4) && (right >= x - width / 4)
    }

    fun isCut(): Boolean {
        return isCut
    }

    fun move(): Boolean {
        val t: Double = (System.currentTimeMillis() - createTime) / 1000.0
        y = baseY - (v * t - (g * t.pow(2.0) / 2)).roundToInt()
        return y < baseY || t < 1.0
    }
}