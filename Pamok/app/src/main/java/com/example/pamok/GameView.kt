package com.example.pamok

import android.content.Context
import android.content.res.Resources
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Rect
import android.util.Log
import android.view.MotionEvent
import android.view.SurfaceView

class GameView : SurfaceView, Runnable {

    private val maxFPS: Int = 60
    private val framePeriod: Int = 1000 / maxFPS
    private val targetsList: MutableList<TargetObject> = mutableListOf()

    private var currentSpawnTime: Int = 0
    private var running: Boolean = false
    private var spawnTime: Int = 1 * maxFPS
    private var thread: Thread? = null

    constructor(context: Context) : super(context)

    override fun onTouchEvent(event: MotionEvent): Boolean {
        val x: Int = event.x.toInt()
        val y: Int = event.y.toInt()

        targetsList.forEach {
            val r: Rect = it.getBounds()

            if (r.contains(x, y) && it.getPixel(x, y) != Color.TRANSPARENT) {
                it.kill()
            }
        }

        targetsList.removeIf {
            !it.isAlive()
        }

        return true
    }

    fun pause() {
        running = false
        thread!!.join()
    }

    fun resume() {
        running = true
        thread = Thread(this)
        thread!!.start()
    }

    override fun run() {
        while (running) {
            val canvas = holder.lockCanvas()
            if (canvas != null) {
                synchronized(holder) {
                    val startTime = System.currentTimeMillis()

                    update()
                    render(canvas)

                    val timeDiff = System.currentTimeMillis() - startTime
                    val sleepTime = framePeriod - timeDiff

                    if (sleepTime > 0) {
                        Thread.sleep(sleepTime)
                    }

                    holder.unlockCanvasAndPost(canvas)
                }
            }
        }
    }

    private fun update() {
        // Targets spawning
        if (currentSpawnTime-- == 0) {
            targetsList.add(TargetObject(
                Resources.getSystem().displayMetrics.widthPixels / 2,
                Resources.getSystem().displayMetrics.heightPixels / 2,
                context.resources
            ))

            currentSpawnTime = spawnTime

            if (spawnTime > 1) {
                --spawnTime
            }
        }

        // Targets movement
        targetsList.forEach {
            it.move()
        }
    }

    private fun render(canvas: Canvas) {
        // Background
        canvas.drawRGB(60, 60, 60)

        // Targets
        targetsList.forEach {
            it.image?.bounds = it.getBounds()
            it.image?.draw(canvas)
        }
    }
}
