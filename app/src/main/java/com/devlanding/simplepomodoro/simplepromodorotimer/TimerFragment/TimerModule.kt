package com.devlanding.simplepomodoro.simplepromodorotimer.TimerFragment

import android.view.View
import com.devlanding.simplepomodoro.simplepromodorotimer.Analytics
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module.module


class TimerModule() {
    val mod = module (override = true){
        factory { TimerInteractor(androidContext()) }
        factory { Analytics(androidContext()) }
        single<TimerMvp.presenter>{ (view : TimerMvp.view) -> TimerPresenter(view, get(), get()) }

    }
}