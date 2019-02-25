package com.devlanding.simplepomodoro.simplepromodorotimer.Main

import com.devlanding.simplepomodoro.simplepromodorotimer.EasyNotificationManager

/**
 * Created by tedho on 3/2/2018.
 */

class MainPresenter(val view: MainMVP.view, val appRate: AppRate, val notificationManager: EasyNotificationManager) : MainMVP.presenter {

    override fun createNotificationChannels() {
        notificationManager.createNotificationChannels()
    }

}