package com.devlanding.simplepomodoro.simplepromodorotimer.Main

import android.app.Activity
import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.content.Intent.ACTION_VIEW
import android.net.Uri
import com.devlanding.simplepomodoro.simplepromodorotimer.R
import com.devlanding.simplepomodoro.simplepromodorotimer.TimerFragment.TimerFragment


class MainNavigator(val context: MainActivity) {
    fun goToRateApp() = with(context) {
        val uri = Uri.parse("market://details?id=" + packageName)
        val goToMarket = Intent(ACTION_VIEW, uri)
        goToMarket.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY or
                Intent.FLAG_ACTIVITY_NEW_DOCUMENT or
                Intent.FLAG_ACTIVITY_MULTIPLE_TASK)
        try {
            startActivity(goToMarket)
        } catch (e: ActivityNotFoundException) {
            startActivity(Intent(Intent.ACTION_VIEW,
                    Uri.parse("http://play.google.com/store/apps/details?id=" + packageName)))
        }
    }

    fun reportBug() = with(context) {
        val pInfo = packageManager.getPackageInfo(packageName, 0)
        val version = pInfo.versionName
        val emailIntent = Intent(Intent.ACTION_SENDTO, Uri.fromParts(
                "mailto", "devlanding@gmail.com", null))
        emailIntent.putExtra(Intent.EXTRA_SUBJECT, "SimplePomodoroTimer Bug")
        emailIntent.putExtra(Intent.EXTRA_TEXT, "SimplePomodoroTimer\nVersion: $version\n------------------------------------------------\n\n")
        startActivity(Intent.createChooser(emailIntent, "Send email..."))
    }

    fun showTimerFragment(shouldStartTimer: Boolean) = with(context) {
        val fragment = TimerFragment.newInstance(shouldStartTimer, "")
        supportFragmentManager.beginTransaction().replace(R.id.container, fragment).commit()
    }

}