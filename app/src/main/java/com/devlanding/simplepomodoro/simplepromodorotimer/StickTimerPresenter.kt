package com.devlanding.simplepomodoro.simplepromodorotimer

import android.content.Context
import com.devlanding.simplepomodoro.simplepromodorotimer.PomodoroTimer.PomodoTimer
import com.devlanding.simplepomodoro.simplepromodorotimer.PomodoroTimer.TimerFinishListener
import com.pawegio.kandroid.fromApi
import com.pawegio.kandroid.toApi
import org.greenrobot.eventbus.EventBus

class StickTimerPresenter(val view: StickyTimerMVP.view, working: Boolean) : TimerFinishListener {


    var notificationTitle = "Break is over"
    var notificationDescription = "Time for work"
    var stickyDescription = "Break"
    var timer: PomodoTimer? = null

    init {
        if (working) {
            notificationTitle = "Time for a break"
            notificationDescription = "Good job"
            stickyDescription = "Working"
        }
    }

    fun displayNotification() {
        fromApi(26) {
            view.displayMarshmallowNotification(notificationTitle, notificationDescription)
        }
        toApi(25) {
            view.displayNormalNotification(notificationTitle, notificationDescription)
        }
    }

    fun displayStickyNotification() {
        fromApi(26) {
            view.stickyMarshmallowNotification("Time: 00:00", stickyDescription)
        }
        toApi(25) {
            view.stickyNormalNotification("Time: 00:00", stickyDescription)
        }
    }

    fun startCountdownTimer(time: Long?, isWorking: Boolean, applicationContext: Context) {
        timer = PomodoTimer(time!!, isWorking, applicationContext)
        timer?.start()
        timer?.onFinishListener(this)
        EventBus.getDefault().post(timer)
    }

    override fun onTick(minutes: Long, seconds: Long) {
        val formattedTimeToDoubleDigits = DoubleDigits().formatTimeToDoubleDigits(minutes, seconds)
        view.updateNotification("Time: $formattedTimeToDoubleDigits")
    }

    override fun onFinished() {
        if (timer!!.secondsLeft < 2) {
            displayNotification()
        }
        timer?.cancel()
        view.cancelStickyNotification()
        view.wakePhone()
    }
}