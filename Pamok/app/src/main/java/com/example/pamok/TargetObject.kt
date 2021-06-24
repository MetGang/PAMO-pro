package com.example.pamok

import android.graphics.Canvas
import android.graphics.Rect
import android.graphics.drawable.Drawable
import android.util.Log
import kotlin.math.pow
import kotlin.math.roundToInt
import kotlin.math.sqrt

/**
 * Class representing cuttable target generated in game
 * @param startX Starting position on x-axis
 * @param startY Starting position on y-axis
 * @param spriteImage Image to use as sprite
 */
class TargetObject(startX: Int, startY: Int, spriteImage: Drawable) {
    private var baseY: Int = startY
    private val createTime: Long = System.currentTimeMillis()
    private val g: Double = 9.80665 * 5
    private val height: Int = 250
    private val image: Drawable = spriteImage
    private var isCut: Boolean = false
    private val v: Double = sqrt(4 / 3 * startY * g)
    private val width: Int = height
    private var x: Int = startX
    private var y: Int = baseY

    /**
     * Sets the object's isCut to true (as object is cut by user)
     */
    fun cut() {
        isCut = true
    }

    /**
     * Draws the object on screen if it's not cut yet
     * @param canvas Canvas on which object is drawn
     */
    fun draw(canvas: Canvas) {
        if (!isCut) {
            image.bounds = Rect(x - width / 2, y - height / 2, x + width / 2, y + height / 2)
            image.draw(canvas)
        }
    }

    /**
     * Checks if line drawn between two points intersects with this target
     * @param x1 First point's position on x-axis
     * @param y1 First point's position on y-axis
     * @param x2 Second point's position on x-axis
     * @param y2 Second point's position on y-axis
     * @return True if line intersects with target, false otherwise
     */
    fun intersectsWith(x1: Int, y1: Int, x2: Int, y2: Int): Boolean {
        var m: Double = (y2 - y1) / (x2 - x1).toDouble()
        m = if (m == 0.0) 1.0 else m
        val a: Double = 1 + m.pow(2)
        val b: Double = 2 * (-x * m * (m * x1 + y1 - y))
        val c: Double = x.toDouble().pow(2) + m.pow(2) * x1.toDouble().pow(2)
                        + y1.toDouble().pow(2) - 2 * m * x1 * (y1 + y)
                        + y.toDouble().pow(2) + 2 * y1 * y - (width / 2.0).pow(2)
        val top = if (y1 < y2) y1 else y2
        val bottom = if (y1 < y2) y2 else y1
        val left = if (x1 < x2) x1 else x2
        val right = if (x1 < x2) x2 else x1
        return (b.pow(2) - (4 * a * c) > 0)
                && (top <= y + height / 4) && (bottom >= y - height / 4)
                && (left <= x + width / 4) && (right >= x - width / 4)
    }

    /**
     * Public access property for isCut private variable
     * @return True if object is already cut, false otherwise
     */
    fun isCut(): Boolean {
        return isCut
    }

    /**
     * Moves the object on screen
     * @return True if current move doesn't end the game (target is still cuttable), false if the target returned to the start
     */
    fun move(): Boolean {
        val t: Double = (System.currentTimeMillis() - createTime) / 1000.0
        y = baseY - (v * t - (g * t.pow(2.0) / 2)).roundToInt()
        if (t.roundToInt() == 10) {
            Log.i("s16663", "$x, $y (po $t)")
        }
        return y < baseY || t < 1.0
    }
}