package com.devlanding.simplepomodoro.simplepromodorotimer.Main

import com.devlanding.simplepomodoro.simplepromodorotimer.EasyNotificationManager

/**
 * Created by tedho on 3/2/2018.
 */

class MainPresenter(val view: MainMVP.view, val appRate: AppRate, val notificationManager: EasyNotificationManager, val navigator: MainNavigator) : MainMVP.presenter {
    override fun showtimerFragment(shouldStartTimer: Boolean) {
        navigator.showTimerFragment(shouldStartTimer)
        view.showTimerFragment(shouldStartTimer)
    }

    override fun createNotificationChannels() {
        notificationManager.createNotificationChannels()
    }

    override fun rateApp() {
        navigator.goToRateApp()
    }

    override fun reportBug() {
        navigator.reportBug()
    }
}