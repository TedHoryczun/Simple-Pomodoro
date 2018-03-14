package com.devlanding.simplepomodoro.simplepromodorotimer

/**
 * Created by tedho on 1/3/2018.
 */

interface StickyTimerMVP{
    interface view{
        fun displayMarshmallowNotification(title: String, description: String)
        fun displayNormalNotification(title: String, description: String)
        fun stickyMarshmallowNotification(title: String, description: String)
        fun stickyNormalNotification(title: String, description: String)

        fun updateNotification(title: String)
        fun cancelStickyNotification()
        fun wakePhone()
    }
}