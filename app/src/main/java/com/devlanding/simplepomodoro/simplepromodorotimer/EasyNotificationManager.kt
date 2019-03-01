package com.devlanding.simplepomodoro.simplepromodorotimer

import android.annotation.SuppressLint
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.media.AudioAttributes
import android.net.Uri
import com.devlanding.simplepomodoro.simplepromodorotimer.AlarmConst.Companion.alarmName
import com.devlanding.simplepomodoro.simplepromodorotimer.AlarmConst.Companion.groupId
import com.pawegio.kandroid.fromApi
import com.pawegio.kandroid.notificationManager


class EasyNotificationManager(val context: Context) {
    val time = "time"

    @SuppressLint("NewApi")
    fun createNotificationChannels() = with(context) {
        fromApi(26) {
            val alarmChannel = NotificationChannel(groupId, alarmName, NotificationManager.IMPORTANCE_HIGH)
            val audioAttributes = AudioAttributes.Builder()
                    .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                    .setUsage(AudioAttributes.USAGE_NOTIFICATION_RINGTONE)
                    .build()
            alarmChannel.setSound(Uri.parse("android.resource://" + packageName + "/" + R.raw.impressed), audioAttributes)
            alarmChannel.enableVibration(true)
            alarmChannel.vibrationPattern = longArrayOf(500, 500, 500, 200, 200, 200, 200, 200, 200)
            notificationManager!!.createNotificationChannel(NotificationChannel(groupId, alarmName, NotificationManager.IMPORTANCE_HIGH))
            notificationManager!!.createNotificationChannel(NotificationChannel("ongoingTime", time, NotificationManager.IMPORTANCE_LOW))
        }

    }

}