package com.example.artem.speakup.SpeechAnalysis

import android.annotation.SuppressLint
import android.util.Log
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
    private var cleanText = StringBuffer("")

    fun startRecognizer() {
        createAndStartRecognizer()
    }

    fun cancelRecognizer() {
        if (recognizer != null) {
            recognizer!!.cancel()
            recognizer = null
            Log.v("dfgdfg", "dfgdfg")
            viewState.showResults(cleanText.toString(), "GENERAL_TEXT")
        }
    }

    @SuppressLint("MissingPermission")
    private fun createAndStartRecognizer() {
        recognizer = Recognizer.create(Recognizer.Language.RUSSIAN, Recognizer.Model.NOTES, object : RecognizerListener {

            override fun onRecordingDone(p0: Recognizer?) {}

            override fun onSoundDataRecorded(p0: Recognizer?, p1: ByteArray?) {}

            override fun onPowerUpdated(p0: Recognizer?, p1: Float) {}

            override fun onPartialResults(p0: Recognizer?, p1: Recognition?, p2: Boolean) {
                if (p2) if (p1 != null) {
                    viewState.showPartialRes(p1.bestResultText)
                    cleanText.append(p1.bestResultText + " ")
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

        }, true)
        recognizer!!.start()
    }


    interface AnalysisView : MvpView {
        fun showPartialRes(text: String)
        fun showResults(cleanText: String, generalText: String)
    }
}