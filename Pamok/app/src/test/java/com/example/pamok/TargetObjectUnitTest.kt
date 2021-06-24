package com.example.pamok

import android.graphics.drawable.ShapeDrawable
import org.junit.Test
import org.junit.Assert.*

class TargetObjectUnitTest {

    /**
     * Checks if cut function changes cut state correctly and isCut shows it correctly
     */
    @Test
    fun cutTest() {
        val target = TargetObject(0, 1920, ShapeDrawable())
        assertFalse(target.isCut())
        target.cut()
        assertTrue(target.isCut())
    }

    /**
     * Checks if intersectsWith checks intersections correctly
     */
    @Test
    fun intersectsWithTest() {
        val target = TargetObject(720, 2700, ShapeDrawable())
        assertFalse(target.intersectsWith(0, 0, 2000, 1500))
        assertTrue(target.intersectsWith(970, 2450, 470, 2950))
    }

    /**
     * Checks if move gives correct results
     */
    @Test
    fun moveTest() {
        val target = TargetObject(0, 1920, ShapeDrawable())
        assertTrue(target.move())
        Thread.sleep(20000)
        assertFalse(target.move())
    }
}