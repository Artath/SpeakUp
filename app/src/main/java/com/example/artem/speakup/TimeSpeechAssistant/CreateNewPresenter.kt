package com.example.artem.speakup.TimeSpeechAssistant

import com.arellomobile.mvp.InjectViewState
import com.arellomobile.mvp.MvpPresenter
import com.arellomobile.mvp.MvpView
import com.example.artem.speakup.DataWork.ExtraSourceWorker
import com.example.artem.speakup.TimeSpeechAssistant.Data.DataHelper
import com.example.artem.speakup.TimeSpeechAssistant.Data.Part

@InjectViewState
class CreateNewPresenter : MvpPresenter<CreateNewPresenter.CreateNewView>() {

    private val parts = arrayListOf<Part>(Part(-1, "",0,"",0,0))
    private lateinit var adapter: PartAdapter
    private var currentPos = 0
    private var sessionId = -1L

    init {
        adapter = PartAdapter(parts, object : PartAdapter.PartAdapterCallBack {

            override fun enterTime(pos: Int){
                currentPos = pos
                viewState.onTimeEnter()
            }

            override fun deletePart(pos: Int) {
                if (parts.size > 1 ) {
                    parts.removeAt(pos)
                    adapter.notifyDataSetChanged()
                }
            }
        })
        viewState.showPartList(adapter)
    }

    fun addPart() {
        parts += Part(-1, "",0,"",-1,0)
        adapter.notifyDataSetChanged()
    }

    fun setTime(minute: Int, second: Int) {
        parts[currentPos].time = ((minute*60 + second)*1000).toLong()
        adapter.notifyDataSetChanged()
    }

    fun saveSession(extrW: ExtraSourceWorker, speechName: String) {

        val session = DataHelper.prepareSpeech(speechName, parts)
        sessionId = DataHelper.createSession(extrW, session)
        session.id = sessionId
        TabAssistantPresenter.addSessionToList(session)

    }

    fun saveParts(extrW: ExtraSourceWorker) = DataHelper.createSeveralPart(extrW, sessionId, parts)


    interface CreateNewView : MvpView {
        fun showPartList(adapter: PartAdapter)
        fun onTimeEnter()
    }
}