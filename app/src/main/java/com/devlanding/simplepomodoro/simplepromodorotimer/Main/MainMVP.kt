package com.devlanding.simplepomodoro.simplepromodorotimer.Main

/**
 * Created by tedho on 3/2/2018.
 */

interface MainMVP{
    interface view{}
    interface presenter{
        fun createNotificationChannels()
        fun rateApp()
        fun reportBug()
        abstract fun showtimerFragment(shouldStartTimer: Boolean)
    }
    interface interactor{}
}
