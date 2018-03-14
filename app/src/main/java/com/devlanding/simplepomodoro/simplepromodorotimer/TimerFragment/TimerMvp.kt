package com.devlanding.simplepomodoro.simplepromodorotimer.TimerFragment

import android.content.Context
import android.media.Ringtone
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
        fun stopWorkingOrBreak(context: Context)
        fun addSuccessfulPomodoro()
        fun playEndOfPomodoroRingtone(context: Context?)

    }
    interface interactor{

    }

}
