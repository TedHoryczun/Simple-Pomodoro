package com.devlanding.simplepomodoro.simplepromodorotimer

import com.devlanding.simplepomodoro.simplepromodorotimer.Main.*
import org.koin.dsl.module.module


class MainModule(val activity: MainActivity, val view: MainMVP.view){
    val mod = module {
        single { EasyNotificationManager(activity.baseContext) }
        single { AppRate(activity.baseContext) }
        single<MainMVP.presenter> { MainPresenter(view, get(), get()) }
    }

}