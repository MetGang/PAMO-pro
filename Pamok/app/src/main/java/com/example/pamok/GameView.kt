package com.example.pamok

import android.content.Context
import android.content.res.Resources
import android.graphics.*
import android.util.Log
import android.view.GestureDetector
import android.view.MotionEvent
import android.view.SurfaceView
import androidx.core.view.GestureDetectorCompat

class GameView(context: Context) : SurfaceView(context), Runnable, GestureDetector.OnGestureListener {
    private val maxFPS: Int = 60
    private var currentSpawnTime: Int = 0
    private val framePeriod: Int = 1000 / maxFPS
    private var flingLine: Pair<Point?, Point?> = Pair(null, null)
    private val maxTargets: Int = 1
    private var mDetector: GestureDetectorCompat = GestureDetectorCompat(context, this)
    private var points: Int = 0
    private var running: Boolean = false
    private var spawnTime: Int = 1 * maxFPS
    private var targetsList: MutableList<TargetObject> = mutableListOf()
    private var thread: Thread? = null

    override fun onDown(e: MotionEvent?): Boolean {
        return true
    }

    override fun onFling(event1: MotionEvent, event2: MotionEvent, velocityX: Float, velocityY: Float): Boolean {
        flingLine = Pair(Point(event1.x.toInt(), event1.y.toInt()), Point(event2.x.toInt(), event2.y.toInt()))
        return true
    }

    override fun onLongPress(e: MotionEvent?) {
    }

    override fun onScroll(e1: MotionEvent?, e2: MotionEvent?, distanceX: Float, distanceY: Float): Boolean {
        return true
    }

    override fun onShowPress(e: MotionEvent?) {
    }

    override fun onSingleTapUp(e: MotionEvent?): Boolean {
        return true
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        return if (mDetector.onTouchEvent(event)) {
            true
        } else {
            super.onTouchEvent(event)
        }
    }

    fun pause() {
        running = false
        thread!!.join()
    }


    private fun render(canvas: Canvas) {
        canvas.drawRGB(60, 60, 60)
        targetsList.forEach {
            it.draw(canvas)
        }
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
                    val startTime: Long = System.currentTimeMillis()
                    update()
                    render(canvas)
                    val timeDiff: Long = System.currentTimeMillis() - startTime
                    val sleepTime: Long = framePeriod - timeDiff
                    if (sleepTime > 0) {
                        Thread.sleep(sleepTime)
                    }
                    holder.unlockCanvasAndPost(canvas)
                }
            }
        }
        //TODO: "you lost" + points count
    }

    private fun update() {
        if (flingLine.first != null) {
            targetsList.forEach {
                if (it.intersectsWith(flingLine)) {
                    it.cut()
                }
            }
            val combo = targetsList.count{
                it.isCut()
            }
            points += combo
            if (combo > 1) {
                //TODO: show combo counter
            }
            targetsList.removeIf {
                it.isCut()
            }
            flingLine = Pair(null, null)
        }
        if (currentSpawnTime-- == 0) {
            if (targetsList.count() < maxTargets)
            {
                targetsList.add(TargetObject(
                    Resources.getSystem().displayMetrics.widthPixels / 2,
                    Resources.getSystem().displayMetrics.heightPixels,
                    context.resources
                ))
            }
            currentSpawnTime = spawnTime--
        }
        targetsList.forEach {
            if (!it.move()) {
                running = false
            }
        }
    }
}
