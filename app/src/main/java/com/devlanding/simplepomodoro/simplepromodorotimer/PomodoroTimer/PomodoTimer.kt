package com.devlanding.simplepomodoro.simplepromodorotimer.PomodoroTimer

import android.content.Context
import android.os.CountDownTimer
import org.greenrobot.eventbus.EventBus
import java.util.concurrent.TimeUnit

class PomodoTimer(val minutesMilli: Long, val isWorking: Boolean, val context: Context) : CountDownTimer(minutesMilli, TimeUnit.SECONDS.toMillis(1)) {
    var listener: TimerFinishListener? = null
    var isDone = false
    var secondsLeft = 1000L

    override fun onFinish() {
        isDone = true
        sendTimeToFragmentToDisplayTime(0, 0, TimeUnit.SECONDS.toMillis(0))
        stopTimer()
    }

    override fun onTick(p0: Long) {
        val minutes = TimeUnit.MILLISECONDS.toMinutes(p0)
        val seconds = ((p0 / 1000) % 60)
        secondsLeft = minutes + seconds
        sendTimeToFragmentToDisplayTime(minutes, seconds, p0)
        listener?.onTick(minutes, seconds)
        println("time: $minutes:$seconds")
    }

    private fun stopTimer() {
        listener?.onFinished()
        notifyFragmentTimerIsDone()
    }

    private fun notifyFragmentTimerIsDone() {
        if (isWorking) {
            EventBus.getDefault().post("WorkDone")
        } else {
            EventBus.getDefault().post("BreakDone")
        }
    }

    private fun sendTimeToFragmentToDisplayTime(minutes: Long, seconds: Long, p0: Long) {
        val time = TimeUnitWatch(minutes.toInt(), seconds.toInt(), p0, minutesMilli, isWorking)
        EventBus.getDefault().post(time)
    }

    fun onFinishListener(listener: TimerFinishListener) {
        this.listener = listener

    }

    fun isTimerDone(): Boolean {
        return isDone
    }

}
