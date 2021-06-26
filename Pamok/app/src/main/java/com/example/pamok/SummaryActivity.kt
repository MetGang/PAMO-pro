package com.example.pamok

import android.os.Bundle
import android.view.View

class SummaryActivity : BasicActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_summary)
    }

    fun backToMenu(view: View) {
        playSound(R.raw.btn_click)

        finishAndRemoveTask()
    }
}