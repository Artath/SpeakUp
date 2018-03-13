package com.example.artem.speakup

import android.content.Context
import android.os.CountDownTimer
import android.util.Log
import android.widget.Toast
import com.arellomobile.mvp.InjectViewState
import com.arellomobile.mvp.MvpPresenter
import com.arellomobile.mvp.MvpView
import java.io.File
import java.util.*

@InjectViewState
class RecorderPresenter: MvpPresenter<RecorderPresenter.Interface>() {

    override fun isInRestoreState(view: Interface?): Boolean {
        return super.isInRestoreState(view)
    }

    var aTimer: CountDownTimer? = null
    var record_status: String = "ready" // rec, stop, ready
    var aRecorder: AudioRecorder? = null
    var startts: Long = 0
    var appPath: String = ""

    fun recorderStart() {
        record_status = "rec"
        viewState.vUpdateButton(record_status)
        viewState.vHideNewRecord(true, null)

        startTimer()

        startts = Date().time

        viewState.vGetAppPath()

        var fileName = appPath
        val files = File(fileName).listFiles()
        fileName += "/NewRecord-%s.3gp".format(files.size)

        aRecorder = AudioRecorder()
        aRecorder?.startRecording(fileName)
    }

    fun recorderStop() {
        if( ( Date().time - startts ) < 5 * 1000 ) {
            // Set more userfriendly block
            viewState.vMessage("Too short speech, wtf?!")
            return
        }

        if( aRecorder != null ) {
            record_status = "ready"
            viewState.vUpdateButton(record_status)

            aRecorder?.stopRecording()

            stopTimer()

            // Get new recorded audio file
            val record = aRecorder?.getAudioRecord()
            viewState.vHideNewRecord(false, record)

            aRecorder = null
        }
    }

    fun startTimer() {
        if( aTimer != null ) {
            stopTimer()
        }
        val startTS = System.currentTimeMillis()

        aTimer = object: CountDownTimer(60*60*1000, 100) {
            override fun onTick(p0: Long) {
                viewState.vUpdateTimer(startTS)
            }

            override fun onFinish() { }
        }
        aTimer?.start()
    }

    fun stopTimer() {
        if( aTimer != null ) {
            aTimer?.cancel()
            aTimer = null
            viewState.vUpdateTimer(null)
        }
    }

    interface Interface : MvpView {
        fun vGetAppPath()
        fun vUpdateTimer(ts: Long?)
        fun vUpdateButton(state: String)
        fun vHideNewRecord(hide: Boolean, record: AudioRecord?)
        fun vMessage(msg: String)
    }
}