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
        recorder?.setOutputFormat(MediaRecorder.OutputFormat.MPEG_4)
        recorder?.setOutputFile(fileName)
        recorder?.setAudioEncoder(MediaRecorder.AudioEncoder.AAC)

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

    fun getLevel(): Float {
        if( recorder != null ) {
            try {
                return recorder?.maxAmplitude!!.toFloat()
            } catch (e: Exception) {
                Log.d("** speakup **", "AudioRecorder getLevel() failed; Return 0")
                return 0F
            }
        } else {
            Log.d("** speakup **", "AudioRecorder getLevel() - recorder is NULL; Return 0")
            return 0F
        }
    }
}