package com.example.artem.speakup

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_record.*
import java.io.File

class RecordActivity : AppCompatActivity() {

    var record_status: String = "ready" // rec, stop, ready
    var aRecorder: AudioRecorder? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_record)

        record_button.setOnClickListener({ _ -> recordHandler() })
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

        var fileName = externalCacheDir.absolutePath
        val files = File(fileName).listFiles()
        fileName += "/NewRecord-%s.3gp".format(files.size)

        aRecorder = AudioRecorder()
        aRecorder?.startRecording(fileName)
    }

    fun stopRecord() {
        if( aRecorder != null ) {
            record_status = "stop"
            record_button.text = resources.getString(R.string.record_start)

            aRecorder?.stopRecording()
            aRecorder = null
        }
    }
}
