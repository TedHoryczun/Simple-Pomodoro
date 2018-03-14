package com.devlanding.simplepomodoro.simplepromodorotimer

import android.content.Context
import android.content.SharedPreferences
import android.preference.PreferenceManager
import java.text.SimpleDateFormat
import java.util.*

/**
 * Created by tedho on 1/2/2018.
 */

class SettingsHelper(context: Context){
    val pref: SharedPreferences = PreferenceManager.getDefaultSharedPreferences(context)
    val NUM_POMODORO = "numpomodoro"
    val CURRENT_DATE = "currentdate"

    fun getNumOfPomodoro(): Int {
        val curentDate = SimpleDateFormat("yyyy/MM/dd").format(Date()).toString()
        val currentDateFromSettings = pref.getString(CURRENT_DATE, "")
        if(curentDate != currentDateFromSettings){
            pref.edit().putString(CURRENT_DATE, curentDate).apply()
            setPomodoroToDefaults()
        }
       return pref.getInt(NUM_POMODORO, 0)
    }

    fun addOneToPomodoro(){
        val pomodoroAmount = getNumOfPomodoro()
       pref.edit().putInt(NUM_POMODORO, getNumOfPomodoro() + 1).apply()
    }
    fun setPomodoroToDefaults(){
        pref.edit().putInt(NUM_POMODORO, 0).apply()

    }

}