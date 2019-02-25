package com.devlanding.simplepomodoro.simplepromodorotimer.Main

import com.devlanding.simplepomodoro.simplepromodorotimer.EasyNotificationManager
import org.koin.dsl.module.module


class MainModule(val activity: MainActivity, val view: MainMVP.view){
    val mod = module {
        single { EasyNotificationManager(activity.baseContext) }
        single { AppRate(activity.baseContext) }
        single { MainNavigator(activity) }

        single<MainMVP.presenter> { MainPresenter(view, get(), get(), get()) }
    }

}