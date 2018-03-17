package com.example.artem.speakup.TonguesTwisters

import android.os.Bundle
import com.arellomobile.mvp.MvpAppCompatActivity
import com.arellomobile.mvp.presenter.InjectPresenter
import com.example.artem.speakup.R
import kotlinx.android.synthetic.main.activity_tongues_twisters.*
import ru.yandex.speechkit.Error
import ru.yandex.speechkit.Initializer
import ru.yandex.speechkit.InitializerListener
import ru.yandex.speechkit.SpeechKit

class TonguesTwistersActivity : MvpAppCompatActivity(), TGTrainingPresenter.TGTrainingView {

    private val API_KEY = "34e04a4d-07bc-4e70-8527-7b5e49f62cf9"

    @InjectPresenter
    lateinit var presenter: TGTrainingPresenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_tongues_twisters)

        SpeechKit.getInstance().configure(applicationContext, API_KEY)

        Initializer.create(object : InitializerListener {
            override fun onInitializerBegin(p0: Initializer?) {}
            override fun onInitializerDone(p0: Initializer?) {}
            override fun onError(p0: Initializer?, p1: Error?) {}
        }).start()

        presenter.launchPresenter(intent.getStringArrayListExtra(TabTwisters.SELECTED_TONGUES_TWISTERS))

        speech_img_btn.setOnClickListener {
            presenter.start()
        }
    }

    override fun showPartialRes(text: String) {
        res_txt.text = text
    }

    override fun showTG(text: String) {
       show_content_txt.text = text
    }

    override fun showNote(note: String) {
       check_res_txt.text = note
    }

}
