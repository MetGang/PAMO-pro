package com.example.pamok

import android.graphics.Color
import android.graphics.Paint
import android.graphics.Typeface
import android.os.Bundle
import androidx.core.content.res.ResourcesCompat

class GameActivity : BasicActivity() {

    private lateinit var gameView: GameView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Preparing paint for drawing text in game
        val typeface = ResourcesCompat.getFont(this, R.font.jamma)
        val fontPaint = Paint()
        fontPaint.color = Color.WHITE
        fontPaint.textSize = 48.0f
        fontPaint.typeface = typeface

        // Actual game logic and render creation
        gameView = GameView(this, fontPaint) {
            playSound(it)
        }

        setContentView(gameView)
    }

    override fun onPause() {
        super.onPause()
        gameView.pause()
    }

    override fun onResume() {
        super.onResume()
        gameView.resume()
    }
}