package com.devlanding.simplepomodoro.simplepromodorotimer

import java.text.DecimalFormat

class DoubleDigits{
    fun formatTimeToDoubleDigits(minutes: Long, seconds: Long): String {
        val formatter = DecimalFormat("00")
        val minutes = formatter.format(minutes)
        val seconds = formatter.format(seconds)
        return "$minutes:$seconds"
    }
}

