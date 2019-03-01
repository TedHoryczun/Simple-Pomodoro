package com.devlanding.simplepomodoro.simplepromodorotimer

import android.annotation.SuppressLint
import android.app.Notification
import android.app.PendingIntent
import android.app.Service
import android.content.Context
import android.content.Intent
import android.media.AudioAttributes
import android.media.RingtoneManager
import android.net.Uri
import android.os.IBinder
import android.os.PowerManager
import com.devlanding.simplepomodoro.simplepromodorotimer.AlarmConst.Companion.groupId
import com.pawegio.kandroid.notificationManager
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.jetbrains.anko.intentFor
import com.devlanding.simplepomodoro.simplepromodorotimer.Main.MainActivity


class StickyTimerNotification : Service(), StickyTimerMVP.view {

    var notification: Notification.Builder? = null
    var isWorking = false
    var presenter: StickTimerPresenter? = null
    var updateNotification = true

    @SuppressLint("NewApi")
    override fun displayNormalNotification(title: String, description: String) {
        val audioAttributes = AudioAttributes.Builder()
                .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                .setUsage(AudioAttributes.USAGE_NOTIFICATION_RINGTONE)
                .build()
        val mainIntent = intentFor<MainActivity>("shouldAddPomodoro" to isWorking)
        val pending = PendingIntent.getActivity(this, 0, mainIntent, PendingIntent.FLAG_UPDATE_CURRENT)
        val notification = Notification.Builder(this)
                .setContentTitle(title)
                .setContentIntent(pending)
                .setContentText(description)
                .setSmallIcon(R.drawable.ic_launcher_background)
                .setVibrate(longArrayOf(500, 500, 500, 100, 200, 200, 200, 200, 200))
                .setSound(Uri.parse("android.resource://" + packageName + "/" + R.raw.impressed), audioAttributes)
                .setAutoCancel(true)
                .build()
        this.notificationManager!!.notify(1, notification)
    }

    private fun playAlarmSound() {
        var alert = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM)

        if (alert == null) {
            alert = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)

            if (alert == null) {
                alert = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_RINGTONE)
            }
        }
        val ringtone = RingtoneManager.getRingtone(applicationContext, alert)
        ringtone.play()
    }

    override fun displayMarshmallowNotification(title: String, description: String) {
        val mainIntent = Intent(this, MainActivity::class.java)
        mainIntent.putExtra("shouldAddPomodoro", isWorking)
        val pending = PendingIntent.getActivity(this, 0, mainIntent, PendingIntent.FLAG_UPDATE_CURRENT)
        val notification = Notification.Builder(this)
                .setContentTitle(title)
                .setContentIntent(pending)
                .setContentText(description)
                .setSmallIcon(R.drawable.ic_launcher_background)
                .setVibrate(longArrayOf(500, 500, 500, 200, 200, 200, 200, 200, 200))
                .setChannelId(AlarmConst.groupId)
                .setAutoCancel(true)
                .build()
        this.notificationManager!!.notify(1, notification)
    }

    override fun stickyNormalNotification(title: String, description: String) {
        val mainIntent = intentFor<MainActivity>()
        val pending = PendingIntent.getActivity(this, 0, mainIntent, 0)
        notification = Notification.Builder(this)
                .setContentTitle(title)
                .setContentText(description)
                .setOngoing(true)
                .setOnlyAlertOnce(true)
                .setContentIntent(pending)
                .setSmallIcon(R.drawable.ic_launcher_background)
        startForeground(2, notification?.build())
        updateNotification = true
    }

    override fun stickyMarshmallowNotification(title: String, description: String) {
        val mainIntent = Intent(this, MainActivity::class.java)
        val pending = PendingIntent.getActivity(this, 0, mainIntent, 0)
        notification = Notification.Builder(this)
                .setContentTitle(title)
                .setContentText(description)
                .setSmallIcon(R.drawable.ic_launcher_background)
                .setOngoing(true)
                .setOnlyAlertOnce(true)
                .setContentIntent(pending)
                .setChannelId(AlarmConst.groupId)
        startForeground(2, notification?.build())
        updateNotification = true
    }

    override fun cancelStickyNotification() {
        updateNotification = false
        applicationContext.notificationManager?.cancel(2)
        Service.STOP_FOREGROUND_REMOVE
        this.stopForeground(true)
        println("canceled foreground task")
    }

    @Subscribe
    fun cancelPomodoroService(num: java.lang.Integer) {
        presenter?.onFinished()

    }

    override fun onDestroy() {
        EventBus.getDefault().unregister(this)
        super.onDestroy()
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this)
        }
        val startCommand = if (intent?.extras != null) {

            isWorking = intent.extras?.getBoolean("isWorking")!!
            val time = intent.extras?.getLong("time")
            presenter = StickTimerPresenter(this, isWorking)
            presenter?.startCountdownTimer(time, isWorking, applicationContext)
            presenter?.displayStickyNotification()
            Service.START_STICKY
        } else {
            Service.STOP_FOREGROUND_REMOVE
        }
        return startCommand
    }

    override fun onBind(p0: Intent?): IBinder? {

        return null
    }

    override fun updateNotification(title: String) {
        if (updateNotification && notification != null) {
            notification?.setContentTitle(title)

            startForeground(2, notification?.build())
        }
    }

    override fun wakePhone() {
        val pm = getSystemService(Context.POWER_SERVICE) as PowerManager
        val wakeLock = pm.newWakeLock(PowerManager.SCREEN_BRIGHT_WAKE_LOCK or PowerManager.FULL_WAKE_LOCK or PowerManager.ACQUIRE_CAUSES_WAKEUP, "TAG")
        wakeLock.acquire()
    }

}