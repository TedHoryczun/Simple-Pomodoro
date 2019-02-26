package com.devlanding.simplepomodoro.simplepromodorotimer.Main

import com.devlanding.simplepomodoro.simplepromodorotimer.EasyNotificationManager
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module.module


class MainModule(val view: MainMVP.view){
    val mod = module(override = true) {
        factory { EasyNotificationManager(androidContext()) }
        factory { AppRate(androidContext()) }
        factory { MainNavigator(androidContext()) }

        factory<MainMVP.presenter> { MainPresenter(view, get(), get(), get()) }
    }

}