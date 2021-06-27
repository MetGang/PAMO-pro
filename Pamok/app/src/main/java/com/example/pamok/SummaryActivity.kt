package com.example.pamok

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.view.View
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.android.synthetic.main.activity_summary.*

class SummaryActivity : BasicActivity() {
    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_summary)

        points_view.text = "Points: ${Globals.obtainedPoints}"
    }

    fun backToMenu(view: View) {
        val prefs = getSharedPreferences("LEADERBOARD", Context.MODE_PRIVATE)
        val recordsString = prefs.getString("records", "[]")

        playSound(R.raw.btn_click)

        val gson = Gson()
        val type = object : TypeToken<List<LeaderboardRecord?>?>() {}.type

        val rawRecords: MutableList<LeaderboardRecord> = gson.fromJson(
            recordsString,
            type
        )

        rawRecords.add(LeaderboardRecord(edit_text.text.toString(), Globals.obtainedPoints))

        val newRecordsString = gson.toJson(rawRecords)

        prefs.edit().putString("records", newRecordsString).apply()

        finishAndRemoveTask()
    }
}