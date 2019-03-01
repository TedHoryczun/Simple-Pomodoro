package com.devlanding.simplepomodoro.simplepromodorotimer.PomodoroTimer

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.devlanding.simplepomodoro.simplepromodorotimer.StickyTimerNotification
import java.util.concurrent.TimeUnit


class LongBreakAlarmNotificationReceiver: BroadcastReceiver(){
    var context: Context? = null
    override fun onReceive(context: Context?, intent: Intent?) {
        this.context = context
//        val timer = PomodoTimer(TimeUnit.MINUTES.toMillis(15), false, context!!)
//        timer.onFinishListener(this)
//        timer.start()
//        EventBus.getDefault().post(timer)
        val intent = Intent(context, StickyTimerNotification::class.java)
        intent.putExtra("isWorking", false)
        intent.putExtra("time", TimeUnit.MINUTES.toMillis(15))
        context?.startService(intent)
    }

}