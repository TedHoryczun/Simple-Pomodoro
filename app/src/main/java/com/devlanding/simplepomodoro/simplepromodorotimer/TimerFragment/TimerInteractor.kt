package com.devlanding.simplepomodoro.simplepromodorotimer.TimerFragment

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.media.Ringtone
import android.media.RingtoneManager
import com.devlanding.simplepomodoro.simplepromodorotimer.PomodoroTimer.BreakAlarmNotificationReceiver
import com.devlanding.simplepomodoro.simplepromodorotimer.PomodoroTimer.LongBreakAlarmNotificationReceiver
import com.devlanding.simplepomodoro.simplepromodorotimer.PomodoroTimer.WorkAlarmNotificationReceiver
import com.devlanding.simplepomodoro.simplepromodorotimer.R
import com.devlanding.simplepomodoro.simplepromodorotimer.SettingsHelper
import com.ldoublem.ringPregressLibrary.Ring
import com.pawegio.kandroid.alarmManager
import java.util.concurrent.TimeUnit

class TimerInteractor(context: Context) : TimerMvp.interactor {
    val SHORT_BREAK_DURATION = 5
    val LONG_BREAK_DURATION = 15
    val WORK_DURATION = 25
    val settings = SettingsHelper(context)
    var currentAlarmIntent: Intent? = null

    fun getWorkProgressBarStyle(context: Context): MutableList<Ring> {
        val workColor = context.resources.getColor(R.color.pomodoroWorkColor)
        val r = Ring(100, "", "", workColor, workColor)
        val mlistRing = mutableListOf<Ring>()
        mlistRing.add(r)
        return mlistRing
    }

    fun updateWorkProgressBarStyle(context: Context, percentProgress: Int): MutableList<Ring> {
        val workColor = context.resources.getColor(R.color.pomodoroWorkColor)
        val r = Ring(percentProgress, "", "", workColor, workColor)
        val mlistRing = mutableListOf<Ring>()
        mlistRing.add(r)
        return mlistRing
    }

    fun getBreakProgressBarStyle(context: Context): MutableList<Ring> {
        val breakColor = context.resources.getColor(R.color.pomodoroBreakColor)
        val r = Ring(100, "", "", breakColor, breakColor)
        val mlistRing = mutableListOf<Ring>()
        mlistRing.add(r)
        return mlistRing
    }

    fun updateBreakProgressBarStyle(context: Context, percentage: Int): MutableList<Ring> {
        val breakColor = context.resources.getColor(R.color.pomodoroBreakColor)
        val r = Ring(percentage, "", "", breakColor, breakColor)
        val mlistRing = mutableListOf<Ring>()
        mlistRing.add(r)
        return mlistRing
    }

    fun startBreakTimer(context: Context, minutesBreakTime: Int) {
        cancelAlarm(context)
        currentAlarmIntent = if (minutesBreakTime == SHORT_BREAK_DURATION) {
            Intent(context, BreakAlarmNotificationReceiver::class.java)
        } else if (minutesBreakTime == LONG_BREAK_DURATION) {
            Intent(context, LongBreakAlarmNotificationReceiver::class.java)
        } else {
            Intent(context, BreakAlarmNotificationReceiver::class.java)
        }
        val pendingIntent = PendingIntent.getBroadcast(context, 0, currentAlarmIntent, 0)
        val timeLength = TimeUnit.MINUTES.toMillis(minutesBreakTime.toLong())
        context.alarmManager?.set(AlarmManager.RTC_WAKEUP, timeLength, pendingIntent)
    }

    fun startWorkTimer(context: Context) {
        cancelAlarm(context)
        currentAlarmIntent = Intent(context, WorkAlarmNotificationReceiver::class.java)
        val pendingIntent = PendingIntent.getBroadcast(context, 0, currentAlarmIntent, 0)
        val minutes25 = TimeUnit.MINUTES.toMillis(WORK_DURATION.toLong())
        context.alarmManager?.set(AlarmManager.RTC_WAKEUP, minutes25, pendingIntent)
    }

    fun cancelAlarm(context: Context) {
        if (currentAlarmIntent != null) {
            val pendingIntent = PendingIntent.getBroadcast(context, 0, currentAlarmIntent, 0)
            context.alarmManager?.cancel(pendingIntent)
        }
    }

    fun getEndOfPomodoroRingtone(context: Context?): Ringtone? {
        val notification = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
        return RingtoneManager.getRingtone(context, notification)
    }

}
