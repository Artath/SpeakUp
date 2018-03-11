package com.example.artem.speakup

import android.media.MediaRecorder
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.widget.Toast
import java.io.File
import java.io.IOException

class AudioRecorder: AppCompatActivity() {
    var fileName: String? = null
    var recorder: MediaRecorder? = null

    fun startRecording(fl: String) {
        fileName = fl

        recorder = MediaRecorder()
        recorder?.setAudioSource(MediaRecorder.AudioSource.MIC)
        recorder?.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP)
        recorder?.setOutputFile(fileName)
        recorder?.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB)

        try {
            recorder?.prepare()
        } catch (e: IOException) {
            Toast.makeText(this, "AudioRecorder prepare() failed", Toast.LENGTH_LONG).show()
            Log.e("** speakup **", "AudioRecorder prepare() failed")
        }

        try {
            recorder?.start()
        } catch(e: IOException) {
            Toast.makeText(this, "AudioRecorder start() failed", Toast.LENGTH_LONG).show()
            Log.e("** speakup **", "AudioRecorder start() failed")
        }
    }

    fun stopRecording() {
        recorder?.stop()
        recorder?.release()
        recorder = null
    }

    fun getAudioRecord(): AudioRecord {
        val f = File(fileName)
        return AudioRecord(
                    f.nameWithoutExtension,
                    f.lastModified(),
                    f.path)
    }
}