package com.example.artem.speakup

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.os.CountDownTimer
import android.view.View
import kotlinx.android.synthetic.main.activity_record.*
import java.io.File
import java.lang.Math.abs
import java.util.*

class RecordActivity : AppCompatActivity() {

    var record_status: String = "ready" // rec, stop, ready
    var aRecorder: AudioRecorder? = null
    var aTimer: CountDownTimer? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_record)

        record_button.setOnClickListener({ _ -> recordHandler() })

        // Return to main view with list of records
        new_record_audio.setOnClickListener({ _ -> finish() })
    }

    fun recordHandler() {
        when( record_status ) {
            "ready" -> startRecord()
            "rec" -> stopRecord()
        }
    }

    fun startRecord() {
        record_status = "rec"
        record_button.text = resources.getString(R.string.record_stop)

        new_record_audio.visibility = View.GONE

        startTimer()

        var fileName = externalCacheDir.absolutePath
        val files = File(fileName).listFiles()
        fileName += "/NewRecord-%s.3gp".format(files.size)

        aRecorder = AudioRecorder()
        aRecorder?.startRecording(fileName)
    }

    fun stopRecord() {
        if( aRecorder != null ) {
            record_status = "ready"
            record_button.text = resources.getString(R.string.record_start)

            aRecorder?.stopRecording()

            stopTimer()

            // Get new recorded audio file
            new_record_audio.visibility = View.VISIBLE
            record_name_new.text = aRecorder?.getAudioRecord()

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
                val ts = startTS - System.currentTimeMillis()
                record_timer.text = "%02d:%02d:%03d".format(
                        abs((ts / 1000 / 60) % 60),
                        abs((ts / 1000) % 60),
                        abs(ts % 1000))
            }

            override fun onFinish() { }
        }
        aTimer?.start()
    }

    fun stopTimer() {
        if( aTimer != null ) {
            aTimer?.cancel()
            aTimer = null
            record_timer.text = "00:00:000"
        }
    }
}
