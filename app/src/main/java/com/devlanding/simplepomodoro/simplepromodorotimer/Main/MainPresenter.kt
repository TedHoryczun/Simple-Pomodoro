package com.devlanding.simplepomodoro.simplepromodorotimer.Main

import android.content.Context

/**
 * Created by tedho on 3/2/2018.
 */

class MainPresenter(val view: MainMVP.view, val applicationContext: Context): MainMVP.presenter{
    val interactor = MainInteractor()
    fun initApprate(){
        interactor.initAppRate(applicationContext)

    }

}