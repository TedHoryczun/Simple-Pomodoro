package com.devlanding.simplepomodoro.simplepromodorotimer.TimerFragment

import android.content.Context
import com.devlanding.simplepomodoro.simplepromodorotimer.Analytics
import com.devlanding.simplepomodoro.simplepromodorotimer.PomodoroTimer.PomodoTimer
import com.devlanding.simplepomodoro.simplepromodorotimer.PomodoroTimer.TimeUnitWatch
import com.devlanding.simplepomodoro.simplepromodorotimer.R
import com.devlanding.simplepomodoro.simplepromodorotimer.WorkTracker
import org.greenrobot.eventbus.EventBus
import java.text.DecimalFormat

class TimerPresenter(val view: TimerMvp.view, val interactor: TimerInteractor, val analytics: Analytics) : TimerMvp.presenter {

    val workTracker = WorkTracker()
    var currentTimer: PomodoTimer? = null

    override fun setTimer(timer: PomodoTimer) {
        currentTimer = timer
    }

    override fun setWorkIsDone() {
        workTracker.workIsDone()
    }

    override fun setIstimerRunning(isTimerRunning: Boolean) {
        workTracker.isTimerRunnig = isTimerRunning
    }

    override fun startWork() {
        val workProgressStyle = interactor.getWorkProgressBarStyle()
        view.displayProgressBarChanges(workProgressStyle, R.color.WorkColorShadow)
        view.setTopOfProgressMessage("Time Until Break")
        workTracker.resetWorkingAndBreak()
    }

    fun startBreak() {

        val breakProgressStyle = interactor.getBreakProgressBarStyle()
        view.displayProgressBarChanges(breakProgressStyle, R.color.BreakColorShadow)
        view.setTopOfProgressMessage("Break")
        workTracker.resetWorkingAndBreak()
    }

    fun startWorkTimer() {
        interactor.startWorkTimer()
        startWork()
        workTracker.timerIsRunning()
        analytics.log(analytics.START_POMODORO)
    }

    fun startShortBreakTimer() {
        interactor.startBreakTimer(5)
        startBreak()
        analytics.log(analytics.BREAK)
    }

    fun startLongBreakTimer() {
        interactor.startBreakTimer(15)
        startBreak()
        analytics.log(analytics.LONG_BREAK)
    }

    override fun formatTimeToDoubleDigits(time: TimeUnitWatch) {
        val formatter = DecimalFormat("00")
        val minutes = formatter.format(time.minute)
        val seconds = formatter.format(time.second)
        view.setTime(minutes, seconds)
    }

    override fun updateWorkProgressBarStyle(percentage: Int, working: Boolean) {
        val progressStyle = if (working) {
            interactor.updateWorkProgressBarStyle(percentage)
        } else {
            interactor.updateBreakProgressBarStyle(percentage)
        }
        view.updateProgressBarPercentage(progressStyle)
    }

    override fun determineIfBreakOrWorkHasEnded(code: String) {
        workTracker.determineIfBreakOrWorkHasEnded(code)
    }

    override fun addSuccessfulPomodoro() {
        val pomodoroAmount = interactor.settings.getNumOfPomodoro()
        view.displayNewPomodoroCounter(pomodoroAmount)
        interactor.settings.addOneToPomodoro()
        displayPomodoroAmount()
    }

    override fun toggleBreakWorkIfTimerIsOver() {
        if (workTracker.breakDone) {
            startWorkTimer()
        } else if (workTracker.workDone) {
            view.displayPopupIsPomodoroValid()

        } else {
            if (workTracker.isTimerRunnig) {
                view.displayPopupToStopTimer()
            } else {
                startWorkTimer()
            }
        }
    }

    override fun startLongorShortBreak() {
        val numOfPomodoro = interactor.settings.getNumOfPomodoro()
        if (numOfPomodoro % 4 == 0 && numOfPomodoro != 0) {
            startLongBreakTimer()
        } else {
            startShortBreakTimer()
        }
        view.showInterstelerAd()
        view.rateApp()
    }

    override fun displayPomodoroAmount() {
        val pomodoroAmount = interactor.settings.getNumOfPomodoro()
        view.displayPomodoroAmount(pomodoroAmount)
    }

    override fun stopWorkingOrBreak() {
        currentTimer?.onFinish()
        currentTimer?.cancel()
        EventBus.getDefault().post(1)
        interactor.cancelAlarm()
        workTracker.setTimerNotRunning()
        startWork()
    }

    override fun timeShouldBeZeroIfTimerIsDone() {
        if (currentTimer != null) {
            if (currentTimer?.isTimerDone()!!) {
                workTracker.setTimerNotRunning()
                stopWorkingOrBreak()
                if (currentTimer!!.isWorking) {
                    determineIfBreakOrWorkHasEnded("WorkDone")
                } else {
                    determineIfBreakOrWorkHasEnded("BreakDone")
                }
                view.setTime("00", "00")
            }
        }
    }

    override fun playEndOfPomodoroRingtone() {
        val ringtone = interactor.getEndOfPomodoroRingtone()
        view.playSound(ringtone)
    }
}
