package com.example.artem.speakup.SpeechAnalysis

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.Typeface
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.os.CountDownTimer
import android.util.Log
import android.view.View
import com.example.artem.speakup.R
import com.homekode.android.tonguetwisters.TongueTwisters
import kotlinx.android.synthetic.main.activity_assistant.*
import kotlinx.android.synthetic.main.activity_speech_records.*
import ru.yandex.speechkit.*

class SpeechRecordsActivity : AppCompatActivity(), RecognizerListener {

    companion object {
        val RES = "res"
    }

    private val API_KEY = "34e04a4d-07bc-4e70-8527-7b5e49f62cf9"
    private var recognizer: Recognizer? = null
    private var partialRes = ""
    private var res = ""
    private var isRecognition = false

    override fun onRecordingDone(p0: Recognizer?) {}

    override fun onSoundDataRecorded(p0: Recognizer?, p1: ByteArray?) {}

    override fun onPowerUpdated(p0: Recognizer?, p1: Float) {}

    override fun onPartialResults(p0: Recognizer?, p1: Recognition?, p2: Boolean) {
        changePartialRes(p1!!.bestResultText)
        if (p2)
            res += p1!!.bestResultText
    }
    override fun onRecordingBegin(p0: Recognizer?) = updateStatus("Say!!!")

    override fun onSpeechEnds(p0: Recognizer?) = updateStatus("Speech ends")

    override fun onSpeechDetected(p0: Recognizer?) = updateStatus("Speech detected")

    override fun onRecognitionDone(p0: Recognizer?, p1: Recognition?) {
       // res += p1!!.bestResultText
    }

    override fun onError(p0: Recognizer?, p1: Error?) {
        if (p1!!.code == Error.ERROR_CANCELED) {
            updateStatus("Cancelled")
        } else {
            updateStatus("Error occurred " + p1!!.string)
            onStopRec()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_speech_records)

        val tf = Typeface.createFromAsset(assets, "segoepr.ttf")

        status_txt.typeface = tf
        partial_res_txt.typeface = tf

        SpeechKit.getInstance().configure(applicationContext, API_KEY)

        var init = Initializer.create(object : InitializerListener {
            override fun onInitializerBegin(p0: Initializer?) {}
            override fun onInitializerDone(p0: Initializer?) {}
            override fun onError(p0: Initializer?, p1: Error?) {}
        }).start()

        rec_btn.setOnClickListener {
            isRecognition = !isRecognition
            if(!isRecognition) {
                onStopRec()
            } else {
                onStartRec()
            }
        }
    }

    private fun onStartRec() {
        rec_btn.text = "stop"
        createAndStartRecognizer()
    }

    private fun onStopRec() {
        rec_btn.text = "start"
        cancelRecognizer()
        startActivity(Intent(applicationContext, AnalysisResActivity::class.java).putExtra(RES, res))

    }

    override fun onPause() {
        super.onPause()
        onStopRec()
    }

    @SuppressLint("MissingPermission")
    private fun createAndStartRecognizer() {
            recognizer = Recognizer.create(Recognizer.Language.RUSSIAN, Recognizer.Model.NOTES, this, true)
            recognizer!!.start()
    }

    private fun cancelRecognizer() {
        if (recognizer != null) {
            recognizer!!.cancel()
            recognizer = null
        }
    }

    private fun updateStatus(text: String) {
        status_txt.text = text
    }
    private fun changePartialRes(text: String) {
        partial_res_txt.text = text
    }

}