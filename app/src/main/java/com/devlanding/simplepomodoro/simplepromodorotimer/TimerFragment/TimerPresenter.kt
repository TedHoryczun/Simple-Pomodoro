package com.devlanding.simplepomodoro.simplepromodorotimer.TimerFragment

import android.content.Context
import com.devlanding.simplepomodoro.simplepromodorotimer.Analytics
import com.devlanding.simplepomodoro.simplepromodorotimer.PomodoroTimer.PomodoTimer
import com.devlanding.simplepomodoro.simplepromodorotimer.PomodoroTimer.TimeUnitWatch
import com.devlanding.simplepomodoro.simplepromodorotimer.R
import com.devlanding.simplepomodoro.simplepromodorotimer.WorkTracker
import org.greenrobot.eventbus.EventBus
import java.text.DecimalFormat

class TimerPresenter(val view: TimerMvp.view, context: Context) : TimerMvp.presenter {

    val interactor = TimerInteractor(context)
    val workTracker = WorkTracker()
    var currentTimer: PomodoTimer? = null
    val analytics = Analytics(context)

    fun startWork(context: Context) {
        val workProgressStyle = interactor.getWorkProgressBarStyle(context)
        view.displayProgressBarChanges(workProgressStyle, R.color.WorkColorShadow)
        view.setTopOfProgressMessage("Time Until Break")
        workTracker.resetWorkingAndBreak()
    }

    fun startBreak(context: Context) {

        val breakProgressStyle = interactor.getBreakProgressBarStyle(context)
        view.displayProgressBarChanges(breakProgressStyle, R.color.BreakColorShadow)
        view.setTopOfProgressMessage("Break")
        workTracker.resetWorkingAndBreak()
    }

    fun startWorkTimer(context: Context?) {
        interactor.startWorkTimer(context!!)
        startWork(context)
        workTracker.timerIsRunning()
        analytics.log(analytics.START_POMODORO)
    }

    fun startShortBreakTimer(context: Context) {
        interactor.startBreakTimer(context, 5)
        startBreak(context)
        analytics.log(analytics.BREAK)
    }

    fun startLongBreakTimer(context: Context) {
        interactor.startBreakTimer(context, 15)
        startBreak(context)
        analytics.log(analytics.LONG_BREAK)
    }

    fun formatTimeToDoubleDigits(time: TimeUnitWatch) {
        val formatter = DecimalFormat("00")
        val minutes = formatter.format(time.minute)
        val seconds = formatter.format(time.second)
        view.setTime(minutes, seconds)
    }

    fun updateWorkProgressBarStyle(context: Context, percentage: Int, working: Boolean) {
        val progressStyle = if (working) {
            interactor.updateWorkProgressBarStyle(context, percentage)
        } else {
            interactor.updateBreakProgressBarStyle(context, percentage)
        }
        view.updateProgressBarPercentage(progressStyle)
    }

    fun determineIfBreakOrWorkHasEnded(code: String) {
        workTracker.determineIfBreakOrWorkHasEnded(code)
    }

    override fun addSuccessfulPomodoro() {
        val pomodoroAmount = interactor.settings.getNumOfPomodoro()
        view.displayNewPomodoroCounter(pomodoroAmount)
        interactor.settings.addOneToPomodoro()
        displayPomodoroAmount()
    }

    fun toggleBreakWorkIfTimerIsOver(context: Context) {
        if (workTracker.breakDone) {
            startWorkTimer(context)
        } else if (workTracker.workDone) {
            view.displayPopupIsPomodoroValid()

        } else {
            if (workTracker.isTimerRunnig) {
                view.displayPopupToStopTimer()
            } else {
                startWorkTimer(context)
            }
        }
    }

    fun startLongorShortBreak(context: Context) {
        val numOfPomodoro = interactor.settings.getNumOfPomodoro()
        if (numOfPomodoro % 4 == 0 && numOfPomodoro != 0) {
            startLongBreakTimer(context)
        } else {
            startShortBreakTimer(context)
        }
        view.showInterstelerAd()
        view.rateApp()
    }

    fun displayPomodoroAmount() {
        val pomodoroAmount = interactor.settings.getNumOfPomodoro()
        view.displayPomodoroAmount(pomodoroAmount)
    }

    override fun stopWorkingOrBreak(context: Context) {
        currentTimer?.onFinish()
        currentTimer?.cancel()
        EventBus.getDefault().post(1)
        interactor.cancelAlarm(context)
        workTracker.setTimerNotRunning()
        startWork(context)
    }

    fun timeShouldBeZeroIfTimerIsDone(context: Context) {
        if (currentTimer != null) {
            if (currentTimer?.isTimerDone()!!) {
                workTracker.setTimerNotRunning()
                stopWorkingOrBreak(context)
                if (currentTimer!!.isWorking) {
                    determineIfBreakOrWorkHasEnded("WorkDone")
                } else {
                    determineIfBreakOrWorkHasEnded("BreakDone")
                }
                view.setTime("00", "00")
            }
        }
    }

    override fun playEndOfPomodoroRingtone(context: Context?) {
        val ringtone = interactor.getEndOfPomodoroRingtone(context)
        view.playSound(ringtone)
    }
}
