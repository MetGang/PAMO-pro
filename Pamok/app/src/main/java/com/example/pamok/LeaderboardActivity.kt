package com.example.pamok

import android.os.Bundle
import android.view.View

class LeaderboardActivity : BasicActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        
        setContentView(R.layout.activity_leaderboard)
    }

    fun backToMenu(view: View) {
        playSound(R.raw.btn_click)

        finishAndRemoveTask()

        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left)
    }

    override fun finish() {
        super.finish()

        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left)
    }
}