package com.example.pamok

import android.annotation.SuppressLint
import android.content.Context
import android.content.res.Resources
import android.graphics.*
import android.view.GestureDetector
import android.view.MotionEvent
import android.view.SurfaceView
import androidx.core.content.res.ResourcesCompat
import androidx.core.view.GestureDetectorCompat

fun createTarget(resources: Resources): TargetObject {
    val x = ((1..4).random() * 0.2f * Resources.getSystem().displayMetrics.widthPixels).toInt()
    val y = Resources.getSystem().displayMetrics.heightPixels
    val resId = listOf(
        R.drawable.ball,
        R.drawable.diamond,
        R.drawable.square,
        R.drawable.cross
    ).random()

    return TargetObject(x, y, ResourcesCompat.getDrawable(resources, resId, null)!!)
}

@SuppressLint("ViewConstructor")
class GameView(context: Context, fontPaint: Paint, playSound: (Int) -> Unit, handleGameOver: () -> Unit) : SurfaceView(context), Runnable, GestureDetector.OnGestureListener {
    private val maxFPS: Int = 60
    private var currentSpawnTime: Int = 0
    private val framePeriod: Int = 1000 / maxFPS
    private var flingLine: Pair<Point?, Point?> = Pair(null, null)
    private val maxTargets: Int = 10
    private var mDetector: GestureDetectorCompat = GestureDetectorCompat(context, this)
    private var points: Int = 0
    private var largestCombo: Int = 0
    private var running: Boolean = false
    private var spawnTime: Int = 1 * maxFPS
    private var targetsList: MutableList<TargetObject> = mutableListOf()
    private var thread: Thread? = null
    private val fontPaint: Paint = fontPaint
    private val playSound: (Int) -> Unit = playSound
    private val handleGameOver: () -> Unit = handleGameOver

    // Empty function to comply GestureDetector.OnGestureListener interface
    override fun onDown(e: MotionEvent?): Boolean {
        return true
    }

    // Gets the movement of user on screen as cutting line between two points (start point and end point)
    override fun onFling(event1: MotionEvent, event2: MotionEvent, velocityX: Float, velocityY: Float): Boolean {
        flingLine = Pair(Point(event1.x.toInt(), event1.y.toInt()), Point(event2.x.toInt(), event2.y.toInt()))
        return true
    }

    // Empty function to comply GestureDetector.OnGestureListener interface
    override fun onLongPress(e: MotionEvent?) {
    }

    // Empty function to comply GestureDetector.OnGestureListener interface
    override fun onScroll(e1: MotionEvent?, e2: MotionEvent?, distanceX: Float, distanceY: Float): Boolean {
        return true
    }

    // Empty function to comply GestureDetector.OnGestureListener interface
    override fun onShowPress(e: MotionEvent?) {
    }

    // Empty function to comply GestureDetector.OnGestureListener interface
    override fun onSingleTapUp(e: MotionEvent?): Boolean {
        return true
    }

    // Checks what gesture user used on screen and executes corresponding event
    @SuppressLint("ClickableViewAccessibility")
    override fun onTouchEvent(event: MotionEvent): Boolean {
        return if (mDetector.onTouchEvent(event)) {
            true
        } else {
            super.onTouchEvent(event)
        }
    }

    // Function called when app is paused ("minimized")
    fun pause() {
        running = false
        thread!!.join()
    }

    // Function called when app is resumed ("unminimized")
    fun resume() {
        running = true
        thread = Thread(this)
        thread!!.start()
    }

    // Main game loop with handled fixed timestep
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

    // Main function for handling logic in game
    private fun update() {
        if (flingLine.first != null) {
            targetsList.forEach {
                if (it.intersectsWith(flingLine.first!!.x, flingLine.first!!.y,
                        flingLine.second!!.x, flingLine.second!!.y)) {
                    it.cut()
                }
            }

            val combo = targetsList.count{
                it.isCut()
            }

            points += combo

            if (combo > 0) {
                playSound(R.raw.fling)

                if (combo > largestCombo) {
                    largestCombo = combo
                }
            }

            targetsList.removeIf {
                it.isCut()
            }

            flingLine = Pair(null, null)
        }

        if (currentSpawnTime-- == 0) {
            if (targetsList.count() < maxTargets)
            {
                targetsList.add(createTarget(resources))
            }
            currentSpawnTime = spawnTime--
        }

        targetsList.forEach {
            if (!it.move()) {
                running = false

                handleGameOver()
            }
        }
    }

    // Main function for handling render in game
    private fun render(canvas: Canvas) {
        canvas.drawRGB(40, 40, 50)

        targetsList.forEach {
            it.draw(canvas)
        }

        canvas.drawText("Points: $points", 30.0f, 12.0f + 48.0f, fontPaint)
        canvas.drawText("Largest combo: $largestCombo", 30.0f, 12.0f + 48.0f + 48.0f + 22.0f, fontPaint)
    }
}
