package com.devlanding.simplepomodoro.simplepromodorotimer.Main

import android.annotation.SuppressLint
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.ActivityNotFoundException
import android.content.Intent
import android.content.Intent.ACTION_VIEW
import android.media.AudioAttributes
import android.net.Uri
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import com.devlanding.simplepomodoro.simplepromodorotimer.R
import com.devlanding.simplepomodoro.simplepromodorotimer.TimerFragment.TimerFragment
import com.google.firebase.analytics.FirebaseAnalytics
import com.pawegio.kandroid.fromApi
import com.pawegio.kandroid.notificationManager
import kotlinx.android.synthetic.main.activity_main.*





class MainActivity : AppCompatActivity(), MainMVP.view {

    val groupId = "alarm"
    val alarmName = "Alarm"
    val time = "time"
    val workChannelId = "work"
    val presenter by lazy{MainPresenter(this, applicationContext)}
    @SuppressLint("NewApi")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        FirebaseAnalytics.getInstance(this)
        presenter.initApprate()


        var fragment = TimerFragment.newInstance(true, "")
        if (intent.extras != null) {
            val shouldAddPomodoro = intent.extras.getBoolean("shouldAddPomodoro")
            if (shouldAddPomodoro) {
                fragment = TimerFragment.newInstance(false, "")
            }
        }
        supportFragmentManager.beginTransaction().replace(R.id.container, fragment).commit()
        setSupportActionBar(toolbar)
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

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when(item?.itemId){
            R.id.rateApp -> {
                val uri = Uri.parse("market://details?id=" + packageName)
                val goToMarket = Intent(ACTION_VIEW, uri)
                goToMarket.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY or
                        Intent.FLAG_ACTIVITY_NEW_DOCUMENT or
                        Intent.FLAG_ACTIVITY_MULTIPLE_TASK)
                try {
                    startActivity(goToMarket)
                } catch (e: ActivityNotFoundException) {
                    startActivity(Intent(Intent.ACTION_VIEW,
                            Uri.parse("http://play.google.com/store/apps/details?id=" + packageName)))
                }

            }
            R.id.reportBug -> {
                val pInfo = this.packageManager.getPackageInfo(packageName, 0)
                val version = pInfo.versionName
                val emailIntent = Intent(Intent.ACTION_SENDTO, Uri.fromParts(
                        "mailto", "devlanding@gmail.com", null))
                emailIntent.putExtra(Intent.EXTRA_SUBJECT, "SimplePomodoroTimer Bug")
                emailIntent.putExtra(Intent.EXTRA_TEXT, "SimplePomodoroTimer\nVersion: $version\n------------------------------------------------\n\n")
                startActivity(Intent.createChooser(emailIntent, "Send email..."))
            }
        }
        return super.onOptionsItemSelected(item)
    }
}
