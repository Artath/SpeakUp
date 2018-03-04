package com.example.artem.speakup.TonguesTwisters

import android.annotation.SuppressLint
import android.graphics.Typeface
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.os.CountDownTimer
import android.view.View
import com.example.artem.speakup.R
import com.homekode.android.tonguetwisters.TongueTwisters
import kotlinx.android.synthetic.main.activity_tongue_twisters.*
import ru.yandex.speechkit.*

class TongueTwistersActivity : AppCompatActivity(), RecognizerListener {

    private val API_KEY = "34e04a4d-07bc-4e70-8527-7b5e49f62cf9"
    private var recognizer: Recognizer? = null
    private val SEC = 3
    private  val tt = TongueTwisters()

    override fun onRecordingDone(p0: Recognizer?) {}

    override fun onSoundDataRecorded(p0: Recognizer?, p1: ByteArray?) {}

    override fun onPowerUpdated(p0: Recognizer?, p1: Float) {}

    override fun onPartialResults(p0: Recognizer?, p1: Recognition?, p2: Boolean) {}

    override fun onRecordingBegin(p0: Recognizer?) = updateStatus("Say!!!")

    override fun onSpeechEnds(p0: Recognizer?) = updateStatus("Speech ends")

    override fun onSpeechDetected(p0: Recognizer?) = updateStatus("Speech detected")

    override fun onRecognitionDone(p0: Recognizer?, p1: Recognition?) {
        updateStatus(p1!!.bestResultText)
        next_btn.visibility = View.VISIBLE
        if (tt.compareTongueTwisters(tongue_txt.text.toString(), res_txt.text.toString())) {
            note_txt.text = "Right!"
        } else note_txt.text = "False..."
    }

    override fun onError(p0: Recognizer?, p1: Error?) {
        if (p1!!.code == Error.ERROR_CANCELED) {
            updateStatus("Cancelled")
        } else {
            updateStatus("Error occurred " + p1!!.string)
            cancelRecognizer()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_tongue_twisters)

        val tf = Typeface.createFromAsset(assets, "segoepr.ttf")

        note_txt.typeface = tf
        res_txt.typeface = tf
        tongue_txt.typeface = tf
        timer_txt.typeface = tf

        var index = 0

        SpeechKit.getInstance().configure(applicationContext, API_KEY)

        var init =  Initializer.create(object : InitializerListener {
            override fun onInitializerBegin(p0: Initializer?) {}
            override fun onInitializerDone(p0: Initializer?) {}
            override fun onError(p0: Initializer?, p1: Error?) {}
        }).start()

        cancel_btn.setOnClickListener {
            cancelRecognizer()
            tongue_txt.text =  ""
            next_btn.visibility = View.VISIBLE
        }

        next_btn.visibility = View.VISIBLE
        next_btn.setOnClickListener {
            updateStatus("...")
            note_txt.text = ""
            tongue_txt.text = tt.twist[index]
            next_btn.visibility = View.INVISIBLE
            object : CountDownTimer(((SEC) * 1000).toLong(), 100) {

                override fun onTick(millisUntilFinished: Long) {

                    timer_txt.text = "" + millisUntilFinished/1000 + "." + millisUntilFinished % 100
                }
                override fun onFinish() {
                    timer_txt.text = "0.0"
                    if (index < tt.twist.size - 1) {
                        index++
                        createAndStartRecognizer()
                    }
                }
            }.start()
        }
    }

    public override fun onPause() {
        super.onPause()
        cancelRecognizer()
    }

    @SuppressLint("MissingPermission")
    private fun createAndStartRecognizer() {
        recognizer = Recognizer.create(Recognizer.Language.RUSSIAN, Recognizer.Model.NOTES, this)
        recognizer!!.start()
    }

    private fun cancelRecognizer() {
        if (recognizer != null) {
            recognizer!!.cancel()
            recognizer = null
        }
    }

    private fun updateStatus(text: String) {
        res_txt.text = text
    }
}
