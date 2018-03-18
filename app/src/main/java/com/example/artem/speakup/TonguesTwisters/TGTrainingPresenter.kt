package com.example.artem.speakup.TonguesTwisters

import android.annotation.SuppressLint
import android.os.CountDownTimer
import com.arellomobile.mvp.InjectViewState
import com.arellomobile.mvp.MvpPresenter
import com.arellomobile.mvp.MvpView
import com.example.artem.speakup.SpeechAnalysis.Word
import ru.yandex.speechkit.Error
import ru.yandex.speechkit.Recognition
import ru.yandex.speechkit.Recognizer
import ru.yandex.speechkit.RecognizerListener

@InjectViewState
class TGTrainingPresenter : MvpPresenter<TGTrainingPresenter.TGTrainingView>() {

    private val SEC = 8
    private var recognizer: Recognizer? = null
    private val arrayTG = arrayListOf<String>()
    private var index = 0
    private var isResizable = true

    private var timer: CountDownTimer =
            object : CountDownTimer(((SEC) * 1000).toLong(), 100) {

            override fun onTick(millisUntilFinished: Long) {
                if (millisUntilFinished <= 3000) {
                    viewState.showPartialRes(((millisUntilFinished / 1000) + 1).toString() + "!")
                }
            }
            override fun onFinish() {
                createAndStartRecognizer()
            }}


    fun launchPresenter(arrTg: ArrayList<String>) {
        if (isResizable) {
                arrayTG += arrTg
                viewState.showTG(arrayTG[index])
                startTimer()
                isResizable = false

        }
    }

    fun start() {
        stopTimer()
        viewState.showPartialRes("Read...")
        viewState.showNote("")
        if (index < arrayTG.size - 1) {
            index++
            viewState.showTG(arrayTG[index])
        }
        startTimer()

    }

    fun stop() {
        stopTimer()
        cancelRecognizer()
        viewState.showPartialRes("")
        viewState.showTG("")
        viewState.showNote("")
    }

    private fun startTimer() = timer.start()
    private fun stopTimer() = timer.cancel()

    private fun compareTongueTwisters(tongueTwister: String, resRecognizer: String) =
            Word.toRightFormat(tongueTwister) == Word.toRightFormat(resRecognizer)

    @SuppressLint("MissingPermission")
    private fun createAndStartRecognizer() {
        recognizer = Recognizer.create(Recognizer.Language.RUSSIAN, Recognizer.Model.NOTES, object : RecognizerListener {

            override fun onRecordingDone(p0: Recognizer?) {}

            override fun onSoundDataRecorded(p0: Recognizer?, p1: ByteArray?) {}

            override fun onPowerUpdated(p0: Recognizer?, p1: Float) {}

            override fun onPartialResults(p0: Recognizer?, p1: Recognition?, p2: Boolean) {
                if (p1 != null) viewState.showPartialRes(p1.bestResultText)
            }

            override fun onRecordingBegin(p0: Recognizer?) = viewState.showPartialRes("Say!")

            override fun onSpeechEnds(p0: Recognizer?) {}

            override fun onSpeechDetected(p0: Recognizer?) {}

            override fun onRecognitionDone(p0: Recognizer?, p1: Recognition?) {
                if (p1 != null) {
                    viewState.showPartialRes(p1.bestResultText)
                    if (compareTongueTwisters(arrayTG[index], p1.bestResultText)) {
                        viewState.showNote("Right!")
                    } else {
                        viewState.showNote("Bad...")
                    }
                }
            }

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

        } )
        recognizer!!.start()
    }

    private fun cancelRecognizer() {
        if (recognizer != null) {
            recognizer!!.cancel()
            recognizer = null
        }
    }

    interface TGTrainingView : MvpView {
        fun showPartialRes(text: String)
        fun showTG(text: String)
        fun showNote(note: String)
    }
}