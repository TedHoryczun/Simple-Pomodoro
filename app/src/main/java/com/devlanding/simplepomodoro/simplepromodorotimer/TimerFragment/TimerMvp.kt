package com.devlanding.simplepomodoro.simplepromodorotimer.TimerFragment

import android.content.Context
import android.media.Ringtone
import com.devlanding.simplepomodoro.simplepromodorotimer.PomodoroTimer.PomodoTimer
import com.devlanding.simplepomodoro.simplepromodorotimer.PomodoroTimer.TimeUnitWatch
import com.ldoublem.ringPregressLibrary.Ring

interface TimerMvp{
    interface view{
        fun displayProgressBarChanges(workProgressStyle: MutableList<Ring>, colorShadow: Int)
        fun setTopOfProgressMessage(message: String)
        fun setTime(minutes: String?, seconds: String?)
        fun updateProgressBarPercentage(workProgressStyle: MutableList<Ring>)
        fun displayNewPomodoroCounter(numOfPomodoro: Int)
        fun displayPomodoroAmount(pomodoroAmount: Int)
        fun displayPopupToStopTimer()
        fun displayPopupIsPomodoroValid()
        fun playSound(ringtone: Ringtone?)
        fun showInterstelerAd()
        fun rateApp()

    }
    interface presenter{

        fun stopWorkingOrBreak()
        fun addSuccessfulPomodoro()
        fun playEndOfPomodoroRingtone()
        fun displayPomodoroAmount()
        fun startWork()
        fun toggleBreakWorkIfTimerIsOver()
        fun timeShouldBeZeroIfTimerIsDone()
        fun startLongorShortBreak()
        fun formatTimeToDoubleDigits(time: TimeUnitWatch)
        fun updateWorkProgressBarStyle(percentage: Int, working: Boolean)
        fun determineIfBreakOrWorkHasEnded(toString: String)
        fun setTimer(timer: PomodoTimer)
        fun setIstimerRunning(isTimerRunning: Boolean)
        fun setWorkIsDone()

    }
    interface interactor{

    }

}
