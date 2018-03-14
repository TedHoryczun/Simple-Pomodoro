package com.devlanding.simplepomodoro.simplepromodorotimer

/**
 * Created by tedho on 1/5/2018.
 */

class WorkTracker{
    var breakDone = false
    var workDone = false
    var numOfPomodoro: Int = 0
    var isTimerRunnig = false

    fun resetWorkingAndBreak() {
        breakDone = false
        workDone = false
    }

    fun timerIsRunning() {
        isTimerRunnig = true
    }

    fun breakIsDone() {
        breakDone = true
        workDone = false
    }
    fun workIsDone(){
        workDone = true
        breakDone = false
    }

    fun addPomodoroToCounter() {
        numOfPomodoro += 1
    }

    fun setTimerNotRunning() {
        isTimerRunnig = false
    }

    fun determineIfBreakOrWorkHasEnded(code: String) {
        if (code == "BreakDone") {
            breakIsDone()
        } else if (code == "WorkDone") {
            workIsDone()
        }
    }
}