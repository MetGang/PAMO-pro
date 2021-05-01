package com.example.pamok

import android.content.Context
import android.graphics.Canvas
import android.view.SurfaceView

class GameView : SurfaceView, Runnable {

    private val MAX_FPS: Int = 60
    private val FRAME_PERIOD: Int = 1000 / MAX_FPS

    private var thread: Thread? = null
    private var running: Boolean = false

    constructor(context: Context) : super(context) {

    }

    private fun update() {

    }

    private fun render(canvas: Canvas) {
        // Background
        canvas.drawRGB(60, 60, 60)
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
                    val sleepTime = FRAME_PERIOD - timeDiff

                    if (sleepTime > 0) {
                        Thread.sleep(sleepTime)
                    }

                    holder.unlockCanvasAndPost(canvas)
                }
            }
        }
    }

    fun resume() {
        running = true
        thread = Thread(this)
        thread!!.start()
    }

    fun pause() {
        running = false
        thread!!.join()
    }
}