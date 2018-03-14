package com.devlanding.simplepomodoro.simplepromodorotimer.PomodoroTimer

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.devlanding.simplepomodoro.simplepromodorotimer.StickyTimerNotification
import java.util.concurrent.TimeUnit


class WorkAlarmNotificationReceiver : BroadcastReceiver(){
    var context: Context? = null

    val groupId = "alarm"
    val alarmName = "Alarm"
    val workChannelId = "work"
    override fun onReceive(context: Context?, intent: Intent?) {
        this.context = context
        val intent = Intent(context, StickyTimerNotification::class.java)
        intent.putExtra("isWorking", true)
        intent.putExtra("time", TimeUnit.MINUTES.toMillis(25))
        context?.startService(intent)
    }

}
