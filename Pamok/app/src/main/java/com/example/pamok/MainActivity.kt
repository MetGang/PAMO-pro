package com.example.pamok

import android.content.Intent
import android.os.Bundle
import android.view.View

class MainActivity : FullScreenActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_main)
    }

    fun startGame(view: View) {
        startActivity(Intent(this, GameActivity::class.java))
    }

    fun openLeaderboard(view: View) {
        startActivity(Intent(this, LeaderboardActivity::class.java))
    }

    fun openSettings(view: View) {
        startActivity(Intent(this, SettingsActivity::class.java))
    }

    fun quitApp(view: View) {
        finishAndRemoveTask()
    }
}