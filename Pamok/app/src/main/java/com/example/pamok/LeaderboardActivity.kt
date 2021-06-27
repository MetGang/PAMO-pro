package com.example.pamok

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.view.View
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.android.synthetic.main.activity_leaderboard.record0
import kotlinx.android.synthetic.main.activity_leaderboard.record1
import kotlinx.android.synthetic.main.activity_leaderboard.record2

class LeaderboardActivity : BasicActivity() {

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val prefs = getSharedPreferences("LEADERBOARD", Context.MODE_PRIVATE)
        val recordsString = prefs.getString("records", "[]")

        val gson = Gson()
        val type = object : TypeToken<List<LeaderboardRecord?>?>() {}.type

        val rawRecords: List<LeaderboardRecord> = gson.fromJson(
            recordsString,
            type
        )

        val records = rawRecords.sortedByDescending { it.score } .take(3)

        setContentView(R.layout.activity_leaderboard)

        if (records.size > 0) {
            record0.text = "1. ${records[0].name} - ${records[0].score}"
            if (records.size > 1) {
                record1.text = "2. ${records[1].name} - ${records[1].score}"
                if (records.size > 2) {
                    record2.text = "3. ${records[2].name} - ${records[2].score}"
                }
            }
        }
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