package com.example.artem.speakup.TimeSpeechAssistant

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.arellomobile.mvp.MvpAppCompatFragment
import com.arellomobile.mvp.presenter.InjectPresenter
import com.example.artem.speakup.R
import com.example.artem.speakup.TimeSpeechAssistant.Data.DBWorkSession
import kotlinx.android.synthetic.main.tab_assistant.*

class TabAssistant : MvpAppCompatFragment(), TabAssistantPresenter.TabAssistantView {

    @InjectPresenter
    lateinit var presenter: TabAssistantPresenter

    companion object {

        val ID = "id"
        val SPEECH_NAME = "speech_name"
        val PLANING_TIME = "planing_time"

        fun newInstance(): TabAssistant {
            val fragment = TabAssistant()
            val args = Bundle()

            fragment.arguments = args
            return fragment
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? =
            inflater.inflate(R.layout.tab_assistant, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        presenter.launchPresenter(DBWorkSession(context!!))

        new_session_btn.setOnClickListener {
            startActivity(Intent(context, CreateNewActivity::class.java))
        }
    }

    override fun showSpeechList(adapter: SessionAdapter) {
        speech_session_recyc.layoutManager = LinearLayoutManager(context)
        speech_session_recyc.adapter = adapter
        adapter.notifyDataSetChanged()
    }

    override fun onStartSpeechSession(id: Long, planingTime: Long, speechName: String) {
        startActivity(Intent(context, SpeechActivity::class.java)
                .putExtra(ID, id)
                .putExtra(PLANING_TIME, planingTime)
                .putExtra(SPEECH_NAME, speechName))
    }

    override fun onEditSpeechSession(id: Long, speechName: String) {
        startActivity(Intent(context, EditSessionActivity::class.java).putExtra(ID, id)
                .putExtra(SPEECH_NAME, speechName))
    }


}
