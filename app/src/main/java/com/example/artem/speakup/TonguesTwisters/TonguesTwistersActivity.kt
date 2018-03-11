package com.example.artem.speakup.TonguesTwisters

import android.annotation.SuppressLint
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.os.CountDownTimer
import com.arellomobile.mvp.presenter.InjectPresenter
import com.example.artem.speakup.R
import com.example.artem.speakup.SpeechAnalysis.Word
import kotlinx.android.synthetic.main.activity_tongues_twisters.*
import ru.yandex.speechkit.*

// some elements - test
class TonguesTwistersActivity : AppCompatActivity(), TGPresenter.TGPresenterInterface {

    @InjectPresenter
    lateinit var presenter: TGPresenter

    private var recognizer: Recognizer? = null
    //test
    private var index = 0
    private var currentTwist = ""
    private var arr = arrayListOf<String>()
    private val API_SPEECH_KIT_KEY = "34e04a4d-07bc-4e70-8527-7b5e49f62cf9"
    private var switch = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_tongues_twisters)

       startSession()
    }

    override fun startSession() {

        SpeechKit.getInstance().configure(applicationContext, API_SPEECH_KIT_KEY)

        Initializer.create(object : InitializerListener {
            override fun onInitializerBegin(p0: Initializer?) {}
            override fun onInitializerDone(p0: Initializer?) {}
            override fun onError(p0: Initializer?, p1: Error?) {}
        }).start()

        tg_stop_btn.setOnClickListener {
            if (switch) {
                onStopSession()
            } else {
                onStartSession()
            }
        }
        //здесь по id уже получаем с базы скороговорки
        val arrTwistId = intent.getIntArrayExtra(TabTwisters.SELECTED_TONGUES_TWISTERS)
        //test
         arr.addAll(arrayListOf("Их пестициды не перепистицидят наши по своей пестицидности",
                "Их пестициды не перепистицидят наши по своей пестицидности",
                "Кокосовары варят в скорококосоварках кокосовый сок",
                "Работники предприятие приватизировали-приватизировали, да не выприватизировали",
                "Сиреневенькая зубовыковыривательница"))

    }

    @SuppressLint("MissingPermission")
    private fun createAndStartRecognizer() {
        recognizer = Recognizer.create(Recognizer.Language.RUSSIAN, Recognizer.Model.NOTES, object : RecognizerListener{

            override fun onRecordingDone(p0: Recognizer?) {}

            override fun onSoundDataRecorded(p0: Recognizer?, p1: ByteArray?) {}

            override fun onPowerUpdated(p0: Recognizer?, p1: Float) {}

            override fun onPartialResults(p0: Recognizer?, p1: Recognition?, p2: Boolean) = changeRes(p1!!.bestResultText)

            override fun onRecordingBegin(p0: Recognizer?) = changeRes("Say!!!")

            override fun onSpeechEnds(p0: Recognizer?) {}

            override fun onSpeechDetected(p0: Recognizer?) {}

            override fun onRecognitionDone(p0: Recognizer?, p1: Recognition?) {
                val res = p1!!.bestResultText
                changeRes(res)
                if (compareTongueTwisters(currentTwist, res)) {
                    //updateProgress(arrTwistId[index])
                    updateCheck("Right!")
                } else {
                    updateCheck("Bad...")
                }
                onStopSession()
            }

            override fun onError(p0: Recognizer?, p1: Error?) {
                if (p1!!.code == Error.ERROR_CANCELED) {
                    changeRes("Cancelled...")
                } else {
                    changeRes("Error occurred " + p1!!.string)
                    cancelRecognizer()
                }
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

    private fun updateTwist(text: String) {
        show_content_txt.text = text
    }

    private fun changeRes(text: String) {
        res_txt.text = text
    }

    private fun updateCheck(text: String) {
        check_res_txt.text = text
    }

    private fun onStopSession() {
        switch = !switch
        cancelRecognizer()
    }

    private fun onStartSession() {
        switch = !switch
        updateTwist(arr[index])
        startTimer(8)
    }
    private fun compareTongueTwisters(tongueTwister: String, resRecognizer: String) =
            Word.toRightFormat(tongueTwister) == Word.toRightFormat(resRecognizer)

    private fun startTimer(sec: Int) {

        object : CountDownTimer(((sec) * 1000).toLong(), 100) {

            override fun onTick(millisUntilFinished: Long) {
                if (millisUntilFinished <= 3000) {
                    changeRes((millisUntilFinished / 1000).toString() + 1)
                }
            }
            override fun onFinish() {
                createAndStartRecognizer()
                //test
                if (index < (arr.size - 1)) {
                    index++
                }
            }
        }.start()
    }

    private fun updateAttempts(id: Long) {
        //здесь добавляются попытки в для конкретного бд
    }


    private fun updateProgress(id: Long) {
        //здесь изменяется прогресс произношения
    }

    //???
    private fun getContentOfTwist(id: Long) {
        //получаем текст скороговорки по айди
    }


}
