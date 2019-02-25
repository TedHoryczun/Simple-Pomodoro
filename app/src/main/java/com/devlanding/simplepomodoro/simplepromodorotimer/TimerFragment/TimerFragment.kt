package com.devlanding.simplepomodoro.simplepromodorotimer.TimerFragment

import android.media.Ringtone
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.devlanding.simplepomodoro.simplepromodorotimer.PomodoroTimer.PomodoTimer
import com.devlanding.simplepomodoro.simplepromodorotimer.PomodoroTimer.TimeUnitWatch
import com.devlanding.simplepomodoro.simplepromodorotimer.R
import com.ldoublem.ringPregressLibrary.Ring
import hotchemi.android.rate.AppRate
import kotlinx.android.synthetic.main.fragment_timer.*
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.jetbrains.anko.alert
import org.jetbrains.anko.noButton
import org.jetbrains.anko.yesButton
import java.util.concurrent.TimeUnit


class TimerFragment : Fragment(), TimerMvp.view {

    private var shouldGoToWork: Boolean? = null
    private var mParam2: String? = null
    val presenter by lazy { TimerPresenter(this, context) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (arguments != null) {
            shouldGoToWork = arguments.getBoolean(ARG_PARAM1)
            mParam2 = arguments.getString(ARG_PARAM2)
        }
    }

    override fun onResume() {
        super.onResume()
        presenter.timeShouldBeZeroIfTimerIsDone(context)
    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater!!.inflate(R.layout.fragment_timer, container, false)
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        presenter.displayPomodoroAmount()
        presenter.startWork(context)
        if (!shouldGoToWork!!) {
            setTime("00", "00")
            presenter.workTracker.workIsDone()
        }
        minute.setOnClickListener {
            presenter.toggleBreakWorkIfTimerIsOver(context)
        }

    }
    override fun rateApp() {
        AppRate.showRateDialogIfMeetsConditions(activity);
    }

    override fun showInterstelerAd() {
    }

    override fun displayPopupIsPomodoroValid() {

        activity.alert("Was your pomodoro uninterupted?") {
            yesButton {
                presenter.addSuccessfulPomodoro()
                presenter.startLongorShortBreak(context)

            }
            noButton {
                presenter.stopWorkingOrBreak(context)
                setTime("25", "00")
            }
        }.show()
    }

    override fun displayPopupToStopTimer() {
        activity.alert("Cancel Pomodoro", "Are you sure you would like to cancel this pomodoro? All Progress will be lost.") {
            yesButton {
                presenter.stopWorkingOrBreak(context)
                setTime("25", "00")
            }
            noButton {}
        }.show()
    }

    override fun displayPomodoroAmount(pomodoroAmount: Int) {
        pomodoroCompletedNum.text = pomodoroAmount.toString()

    }

    override fun displayNewPomodoroCounter(numOfPomodoro: Int) {
        pomodoroCompletedNum.text = numOfPomodoro.toString()
    }

    override fun setTime(minutes: String?, seconds: String?) {
        minute.text = minutes
        second.text = seconds
    }

    override fun displayProgressBarChanges(progressBarStyle: MutableList<Ring>, colorShadow: Int) {
        progressBar.sweepAngle = 360
        progressBar.setDrawBg(true, resources.getColor(R.color.pomodooWheelBackground))
        progressBar.setDrawBgShadow(true, resources.getColor(colorShadow))
        progressBar.isCorner = true
        progressBar.setOnSelectRing({})
        progressBar.setData(progressBarStyle as List<Ring>?, 1000)// if >0 animation ==0 null
    }

    override fun updateProgressBarPercentage(workProgressStyle: MutableList<Ring>) {
        progressBar.setData(workProgressStyle, 0)
        progressBar.invalidate()
    }

    override fun setTopOfProgressMessage(message: String) {
        topOfProgressMessage.text = message
    }

    @Subscribe
    fun setTime(time: TimeUnitWatch) {
        presenter.formatTimeToDoubleDigits(time)
        presenter.workTracker.isTimerRunnig = true
        val percentage = if (TimeUnit.SECONDS.toMillis(time.currentMilli) == 0L) {
            ((time.currentMilli) * 100 / time.maxMilli).toInt()

        } else {
            ((time.currentMilli - TimeUnit.SECONDS.toMillis(1)) * 100 / time.maxMilli).toInt()
        }
        presenter.updateWorkProgressBarStyle(context, percentage, time.isWorking)
    }

    @Subscribe
    fun endWorkOrBreak(code: java.lang.String) {
        presenter.determineIfBreakOrWorkHasEnded(code.toString())
        presenter.playEndOfPomodoroRingtone(context)
    }

    @Subscribe
    fun saveTimer(timer: PomodoTimer) {
        presenter.currentTimer = timer
    }

    override fun playSound(ringtone: Ringtone?) {
        ringtone?.play()
    }

    override fun onStart() {
        super.onStart()
        EventBus.getDefault().register(this)
    }

    override fun onStop() {
        super.onStop()
        EventBus.getDefault().unregister(this)
    }

    companion object {
        private val ARG_PARAM1 = "param1"
        private val ARG_PARAM2 = "param2"

        fun newInstance(shouldGoToWork: Boolean, param2: String): TimerFragment {
            val fragment = TimerFragment()
            val args = Bundle()
            args.putBoolean(ARG_PARAM1, shouldGoToWork)
            args.putString(ARG_PARAM2, param2)
            fragment.arguments = args
            return fragment
        }
    }
}
