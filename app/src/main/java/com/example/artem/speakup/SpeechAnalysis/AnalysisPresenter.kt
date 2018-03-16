package com.example.artem.speakup.SpeechAnalysis

import android.annotation.SuppressLint
import android.os.CountDownTimer
import com.arellomobile.mvp.InjectViewState
import com.arellomobile.mvp.MvpPresenter
import com.arellomobile.mvp.MvpView
import ru.yandex.speechkit.Error
import ru.yandex.speechkit.Recognition
import ru.yandex.speechkit.Recognizer
import ru.yandex.speechkit.RecognizerListener

@InjectViewState
class AnalysisPresenter : MvpPresenter<AnalysisPresenter.AnalysisView>()   {

    private var recognizer: Recognizer? = null
    private var cleanTextRes = StringBuffer("")
    private var generalRes = StringBuffer("")
    private var partialRes = StringBuffer("")
    private var timer: CountDownTimer = object : CountDownTimer(3000, 1000) {
        override fun onFinish() {
            generalRes.append(" [Long pause] ")
        }

        override fun onTick(p0: Long) {
        }

    }

    fun start() {
        createAndStartRecognizer()
    }

    fun stop() {
        cancelRecognizer()
        viewState.showResults(cleanTextRes.toString(), generalRes.toString())
        cleanTextRes.delete(0, cleanTextRes.length)
        generalRes.delete(0, generalRes.length)
    }

    private fun stopTimer() {
        timer.cancel()
    }

    @SuppressLint("MissingPermission")
    private fun createAndStartRecognizer() {
        recognizer = Recognizer.create(Recognizer.Language.RUSSIAN, Recognizer.Model.NOTES, object : RecognizerListener {

            override fun onRecordingDone(p0: Recognizer?) {}

            override fun onSoundDataRecorded(p0: Recognizer?, p1: ByteArray?) {}

            override fun onPowerUpdated(p0: Recognizer?, p1: Float) {

                when {
                    p1 == 0f -> {
                        timer.start()
                    }
                    p1 != 0f && partialRes.toString() == "" -> {
                        timer.cancel()
                        generalRes.append(" [Bad Speech] ")
                    }else -> {
                        timer.cancel()
                    }
                }
            }

            override fun onPartialResults(p0: Recognizer?, p1: Recognition?, p2: Boolean) {
                if (p1 != null) {
                    partialRes.append(p1.bestResultText)
                    viewState.showPartialRes(p1.bestResultText)
                    if (p2) {
                        partialRes.delete(0, partialRes.length)
                        cleanTextRes.append(p1.bestResultText + " ")
                        generalRes.append(p1.bestResultText + " ")
                    }
                }
            }

            override fun onRecordingBegin(p0: Recognizer?) = viewState.showPartialRes("Say!")

            override fun onSpeechEnds(p0: Recognizer?) {}

            override fun onSpeechDetected(p0: Recognizer?) {}

            override fun onRecognitionDone(p0: Recognizer?, p1: Recognition?) {}

            override fun onError(p0: Recognizer?, p1: Error?) {
               if (p1 != null) when (p1.code) {
                   Error.ERROR_CANCELED -> {
                       viewState.showPartialRes("Cancelled")

                   }
                   Error.ERROR_NETWORK -> {
                       viewState.showPartialRes("Network error")
                   }
                   Error.ERROR_SERVER -> {
                       viewState.showPartialRes("Server recognizer error")
                   }

               }
                cancelRecognizer()
            }

        }, true )
        recognizer!!.start()
    }

    private fun cancelRecognizer() {
        if (recognizer != null) {
            recognizer!!.cancel()
            recognizer = null
        }
    }


    interface AnalysisView : MvpView {
        fun showPartialRes(text: String)
        fun showResults(cleanText: String, generalText: String)
    }
}