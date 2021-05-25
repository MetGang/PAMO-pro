package com.example.pamok

import android.content.Intent
import android.os.Bundle
import android.view.View
import java.io.InputStream
import java.nio.charset.Charset

fun InputStream.readTextAndClose(charset: Charset = Charsets.UTF_8): String {
    return this.bufferedReader(charset).use { it.readText() }
}

class MainActivity : BasicActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_main)
    }

    fun startGame(view: View) {
        playSound(R.raw.btn_click)

        startActivity(Intent(this, GameActivity::class.java))
    }

    fun openLeaderboard(view: View) {
        val recordsString = assets.open("leaderboard.json").readTextAndClose()

        playSound(R.raw.btn_click)

        startActivity(
            Intent(this, LeaderboardActivity::class.java)
                .putExtra("recordsString", recordsString)
        )

        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right)
    }

    fun openSettings(view: View) {
        playSound(R.raw.btn_click)

        startActivity(Intent(this, SettingsActivity::class.java))

        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left)
    }

    fun quitApp(view: View) {
        playSound(R.raw.btn_click)

        finishAndRemoveTask()
    }
}
