package com.example.pamok

import android.content.Context
import android.content.res.Resources
import android.graphics.*
import android.view.MotionEvent
import android.view.SurfaceView

class GameView : SurfaceView, Runnable {

    private val maxFPS: Int = 60
    private val framePeriod: Int = 1000 / maxFPS
    private val targetsList: MutableList<TargetObject> = mutableListOf()
    private var touchPoint: Point? = null

    private var currentSpawnTime: Int = 0
    private var running: Boolean = false
    private var spawnTime: Int = 1 * maxFPS
    private var thread: Thread? = null

    constructor(context: Context) : super(context)

    override fun onTouchEvent(event: MotionEvent): Boolean {
        if (touchPoint == null) {
            touchPoint = Point(event.x.toInt(), event.y.toInt())

            return true
        }

        return false
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
        // Targets killing
        if (touchPoint != null) {
            targetsList.forEach {
                val r: Rect = it.getBounds()

                if (r.contains(touchPoint!!.x, touchPoint!!.y) && it.getPixel(touchPoint!!.x, touchPoint!!.y) != Color.TRANSPARENT) {
                    it.kill()
                }
            }

            targetsList.removeIf {
                !it.isAlive()
            }

            touchPoint = null
        }

        // Targets spawning
        if (currentSpawnTime-- == 0) {
            targetsList.add(TargetObject(
                Resources.getSystem().displayMetrics.widthPixels / 2,
                Resources.getSystem().displayMetrics.heightPixels / 2,
                context.resources
            ))

            currentSpawnTime = spawnTime
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
