package com.devlanding.simplepomodoro.simplepromodorotimer.Main

import android.content.Context
import android.util.Log
import hotchemi.android.rate.AppRate


/**
 * Created by tedho on 3/2/2018.
 */

class AppRate(val context: Context) {
    init {
        AppRate.with(context)
                .setInstallDays(0) // default 10, 0 means install day.
                .setLaunchTimes(3) // default 10
                .setRemindInterval(2) // default 1
                .setShowLaterButton(true) // default true
                .setOnClickButtonListener { which ->
                    // callback listener.
                    Log.d(MainActivity::class.java.name, Integer.toString(which))
                }
                .monitor()
    }
}