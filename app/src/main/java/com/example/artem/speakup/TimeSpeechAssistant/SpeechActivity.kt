package com.example.artem.speakup.TimeSpeechAssistant

import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import com.arellomobile.mvp.MvpAppCompatActivity
import com.arellomobile.mvp.presenter.InjectPresenter
import com.example.artem.speakup.R
import com.example.artem.speakup.TimeSpeechAssistant.Data.DBWorkParts
import kotlinx.android.synthetic.main.activity_speech.*

class SpeechActivity : MvpAppCompatActivity(), SpeechPresenter.SpeechView {

    @InjectPresenter
    lateinit var presenter: SpeechPresenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_speech)

        presenter.launchPresenter(DBWorkParts(applicationContext),
                intent.getLongExtra(TabAssistant.ID, -1L),
                intent.getLongExtra(TabAssistant.PLANING_TIME, 0L),
                intent.getStringExtra(TabAssistant.SPEECH_NAME))

        next_btn.setOnClickListener {
            presenter.next()
        }

        back_img_btn.setOnClickListener {
            presenter.back()
        }

        stop_speech_img_btn.setOnClickListener {
            presenter.stopAll()
        }
    }

    override fun showPartsList(adapter: SpeechItemAdapter) {
        parts_list.layoutManager = LinearLayoutManager(applicationContext)
        parts_list.adapter = adapter
        adapter.notifyDataSetChanged()
    }

    override fun showSpeechName(speechName: String) {
        speech_name_txt.text = speechName
    }

    override fun showPlaningTime(planingTime: Long) {

        planing_time_txt.text = String.format("%02d:%02d",
                (planingTime / 1000) / 60,
                (planingTime / 1000) % 60)

    }

    override fun showCurrentTime(currentTime: Long) {

        current_time_txt.text = String.format("%02d:%02d:%03d",
                (currentTime / 1000) / 60,
                (currentTime / 1000) % 60,
                (currentTime % 1000))
    }

    override fun stop() {
        finish()
    }
}

