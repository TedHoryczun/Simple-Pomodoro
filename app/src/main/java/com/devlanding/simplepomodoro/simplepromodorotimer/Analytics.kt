package com.devlanding.simplepomodoro.simplepromodorotimer

import android.content.Context
import android.os.Bundle
import com.google.firebase.analytics.FirebaseAnalytics

class Analytics(context: Context){
    val analytics = FirebaseAnalytics.getInstance(context)
    val START_POMODORO = "StartPomodoro"
    val BREAK = "Break"
    val LONG_BREAK = "LongBreak"

    fun log(tag: String){
        val bundle = Bundle()
        analytics.logEvent(tag, bundle)
    }
}
