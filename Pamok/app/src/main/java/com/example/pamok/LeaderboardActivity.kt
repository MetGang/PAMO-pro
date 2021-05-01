package com.example.pamok

import android.os.Bundle
import android.view.View

class LeaderboardActivity : FullScreenActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        
        setContentView(R.layout.activity_leaderboard)
    }

    fun backToMenu(view: View) {
        finishAndRemoveTask()
    }
}