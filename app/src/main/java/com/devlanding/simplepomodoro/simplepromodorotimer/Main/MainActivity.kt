package com.devlanding.simplepomodoro.simplepromodorotimer.Main

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import com.devlanding.simplepomodoro.simplepromodorotimer.R
import com.devlanding.simplepomodoro.simplepromodorotimer.TimerFragment.TimerFragment
import com.google.firebase.analytics.FirebaseAnalytics
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

        val shouldAddPomodoro = intent.extras?.getBoolean("shouldAddPomodoro") ?: true
        presenter.showtimerFragment(shouldAddPomodoro)

        presenter.createNotificationChannels()
        setSupportActionBar(toolbar)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when (item?.itemId) {
            R.id.rateApp -> presenter.rateApp()
            R.id.reportBug -> presenter.reportBug()
        }
        return super.onOptionsItemSelected(item)
    }
}
