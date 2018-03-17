package com.example.artem.speakup

import android.os.CountDownTimer
import android.util.Log
import com.arellomobile.mvp.InjectViewState
import com.arellomobile.mvp.MvpPresenter
import com.arellomobile.mvp.MvpView
import com.github.mikephil.charting.data.*
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import java.io.File
import java.util.*
import kotlin.collections.ArrayList

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
    var aLevels: ArrayList<BarEntry> = ArrayList<BarEntry>() // Max 32767

    fun iniChart() {
        // Complete aLevels with empty values
        for ( i in 0 .. 99 ) {
            aLevels.add(BarEntry(i.toFloat(), ((i*100) * 100 / 32767 ).toFloat()))
        }

        val data = BarDataSet(aLevels, "Level")
        viewState.vInitChart(data)
    }

    fun recorderStart() {
        record_status = "rec"
        viewState.vUpdateButton(record_status)
        viewState.vHideNewRecord(true, null)

        startTimer()

        startts = Date().time

        viewState.vGetAppPath()

        var fileName = appPath
        val files = File(fileName).listFiles()
        fileName += "/NewRecord-%s.mp3".format(files.size)

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
            val signal = aRecorder?.getAudioSignal()
            viewState.vHideNewRecord(false, record)

            val name = record!!.name
            val uid = FirebaseAuth.getInstance().uid
            val ref = FirebaseDatabase.getInstance().reference
                    .child("users")
                    .child(uid)
                    .child("records")
                    .child(name)
                    .child("signalList")
                    .setValue(signal)

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
//                Log.d("** speakup **", "vUpdateChart y: %s; entry: %s".format(aRecorder!!.getLevel(), aRecorder!!.getLevel() * 100 / 32767))
                val level = aRecorder!!.getLevel() * 100 / 32767
                aRecorder!!.signal.add(level)
                viewState.vUpdateChart(BarEntry(99.0F, level))
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
        fun vInitChart(data: BarDataSet)
        fun vUpdateChart(entry: BarEntry)
    }
}