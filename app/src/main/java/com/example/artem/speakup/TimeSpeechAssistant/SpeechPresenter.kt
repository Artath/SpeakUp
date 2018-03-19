package com.example.artem.speakup.TimeSpeechAssistant

import android.os.CountDownTimer
import com.arellomobile.mvp.InjectViewState
import com.arellomobile.mvp.MvpPresenter
import com.arellomobile.mvp.MvpView
import com.example.artem.speakup.DataWork.ExtraSourceWorker
import com.example.artem.speakup.TimeSpeechAssistant.Data.*

@InjectViewState
class SpeechPresenter : MvpPresenter<SpeechPresenter.SpeechView>() {

    private var isResizable = true
    private val parts = arrayListOf<ChildPart>()
    private var adapter: SpeechItemAdapter? = null
    private var mainTimer: CountDownTimer? = null
    private var partTimer: CountDownTimer? = null
    private var indexTiming = 0
    private var indexChoose = 0

    fun launchPresenter(extrW: ExtraSourceWorker, id: Long, planingTime: Long, speechName: String) {
        if (isResizable) {

            parts += expansionPart(DataHelper.readPart(extrW, id))

            adapter = SpeechItemAdapter(parts)

            viewState.showPartsList(adapter!!)
            startMainTimer(planingTime)
            parts[indexTiming].selected = true
            parts[indexTiming].order = 1
            startPartTimer(parts[indexTiming].time)
            viewState.showPlaningTime(planingTime)
            viewState.showSpeechName(speechName)

            isResizable = false
        }
    }

    fun back() {
        if (indexChoose > 0) {
            parts[indexChoose].selected = false
            indexChoose--
            parts[indexChoose].selected = true
            adapter?.notifyDataSetChanged()

        }
    }

    fun next() {
        if (indexChoose < parts.size - 1) {
            parts[indexChoose].selected = false
            indexChoose++
            parts[indexChoose].selected = true
            adapter?.notifyDataSetChanged()

            if (indexChoose > indexTiming) {
                if (partTimer != null) {
                    partTimer?.cancel()
                    timeNextPart()
                }
            }
        }
    }

    fun stopAll() {
        stopTimers()
        viewState.stop()
    }


    private fun stopTimers() {
        if (mainTimer != null && partTimer != null) {
            mainTimer?.cancel()
            partTimer?.cancel()
        }
    }
    private fun startMainTimer(planingTime: Long) {
        mainTimer = object : CountDownTimer(planingTime, 1) {
            override fun onFinish() {
                viewState.showPlaningTime(planingTime)
                viewState.showSpeechName("The End")
            }

            override fun onTick(p0: Long) {
                viewState.showCurrentTime(planingTime - p0)
            }

        }.start()
    }

    private fun timeNextPart() {

        if (indexTiming < parts.size - 1) {
            parts[indexTiming].currentTime = 0L
            parts[indexTiming].order = 2
            indexTiming++
            parts[indexChoose].selected = false
            indexChoose = indexTiming
            parts[indexTiming].selected = true
            parts[indexTiming].order = 1
            startPartTimer(parts[indexTiming].time)
        } else {
            stopTimers()
            parts[indexTiming].currentTime = 0L
            parts[indexTiming].order = 2
            viewState.showSpeechName("The End")

        }
        adapter?.notifyDataSetChanged()
    }

    private fun startPartTimer(planingTime: Long) {
        partTimer = object : CountDownTimer(planingTime, 1) {
            override fun onFinish() {
                timeNextPart()
            }

            override fun onTick(p0: Long) {
                parts[indexTiming].currentTime = planingTime - p0
                adapter?.notifyDataSetChanged()
            }

        }.start()
    }
    private fun expansionPart(parts: ArrayList<Part>): ArrayList<ChildPart> {
        val childParts = arrayListOf<ChildPart>()
        parts.forEach { elem ->
            childParts += ChildPart(elem.id,
                    elem.head,
                    elem.time,
                    elem.theses,
                    elem.session_id,
                    elem.order, 0L, false)
        }
        return childParts
    }

    interface SpeechView : MvpView {
        fun showPartsList(adapter: SpeechItemAdapter)
        fun showSpeechName(speechName: String)
        fun showPlaningTime(planingTime: Long)
        fun showCurrentTime(currentTime: Long)
        fun stop()
    }
}