package com.example.pamok

import android.os.Bundle
import android.view.View

class SettingsActivity : BasicActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        
        setContentView(R.layout.activity_settings)
    }

    fun backToMenu(view: View) {
        playSound(R.raw.btn_click)

        finishAndRemoveTask()

        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right)
    }

    override fun finish() {
        super.finish()

        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right)
    }
}