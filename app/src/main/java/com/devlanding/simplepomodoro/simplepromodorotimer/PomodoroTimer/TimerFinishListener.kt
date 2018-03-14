package com.devlanding.simplepomodoro.simplepromodorotimer.PomodoroTimer

/**
 * Created by tedho on 12/28/2017.
 */

interface TimerFinishListener{
    fun onFinished()
    fun onTick(minutes: Long, seconds: Long)
}