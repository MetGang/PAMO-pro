package com.example.pamok

import android.os.Bundle
import android.view.View

class SettingsActivity : FullScreenActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        
        setContentView(R.layout.activity_settings)
    }

    fun backToMenu(view: View) {
        finishAndRemoveTask()
    }
}