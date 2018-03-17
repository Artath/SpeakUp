package com.example.artem.speakup.SpeechAnalysis


import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.arellomobile.mvp.MvpAppCompatFragment
import com.arellomobile.mvp.presenter.InjectPresenter
import com.example.artem.speakup.R
import kotlinx.android.synthetic.main.tab_recorder.*
import ru.yandex.speechkit.Error
import ru.yandex.speechkit.Initializer
import ru.yandex.speechkit.InitializerListener
import ru.yandex.speechkit.SpeechKit

class TabRecorder : MvpAppCompatFragment(), AnalysisPresenter.AnalysisView {

    @InjectPresenter
    lateinit var presenter: AnalysisPresenter

    private val API_KEY = "34e04a4d-07bc-4e70-8527-7b5e49f62cf9"


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? =
            inflater!!.inflate(R.layout.tab_recorder, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        SpeechKit.getInstance().configure(context, API_KEY)

        Initializer.create(object : InitializerListener {
            override fun onInitializerBegin(p0: Initializer?) {}
            override fun onInitializerDone(p0: Initializer?) {}
            override fun onError(p0: Initializer?, p1: Error?) {}
        }).start()

        startActivity(Intent(context, ChooseWordsActivity::class.java))
    }

    companion object {

        val CLEAN_TEXT = "clean_txt"
        val GENERAL_TEXT = "general_txt"

        fun newInstance(): TabRecorder {
            val fragment = TabRecorder()
            val args = Bundle()

            fragment.arguments = args
            return fragment
        }
    }

    override fun showPartialRes(text: String) {
        record_timer.text = text
    }

    override fun showResults(cleanText: String, generalText: String) {
        startActivity(Intent(context, ChooseWordsActivity::class.java)
                .putExtra(CLEAN_TEXT, cleanText)
                .putExtra(GENERAL_TEXT, generalText))
        record_timer.text = ""
    }
}
