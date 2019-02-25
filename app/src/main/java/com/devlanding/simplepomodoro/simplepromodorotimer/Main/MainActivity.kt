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
import com.devlanding.simplepomodoro.simplepromodorotimer.MainModule
import com.devlanding.simplepomodoro.simplepromodorotimer.R
import com.devlanding.simplepomodoro.simplepromodorotimer.TimerFragment.TimerFragment
import com.google.firebase.analytics.FirebaseAnalytics
import com.pawegio.kandroid.fromApi
import com.pawegio.kandroid.notificationManager
import kotlinx.android.synthetic.main.activity_main.*
import org.koin.android.ext.android.inject
import org.koin.android.ext.android.startKoin


class MainActivity : AppCompatActivity(), MainMVP.view {

    val presenter: MainMVP.presenter by inject()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        startKoin(this, listOf(MainModule(this, this).mod))
        setContentView(R.layout.activity_main)
        FirebaseAnalytics.getInstance(this)


        var fragment = TimerFragment.newInstance(true, "")
        if (intent.extras != null) {
            val shouldAddPomodoro = intent.extras.getBoolean("shouldAddPomodoro")
            if (shouldAddPomodoro) {
                fragment = TimerFragment.newInstance(false, "")
            }
        }
        supportFragmentManager.beginTransaction().replace(R.id.container, fragment).commit()
        setSupportActionBar(toolbar)
        presenter.createNotificationChannels()
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
