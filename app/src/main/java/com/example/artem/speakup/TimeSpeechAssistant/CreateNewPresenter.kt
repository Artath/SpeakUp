package com.example.artem.speakup.TimeSpeechAssistant

import android.content.Context
import com.arellomobile.mvp.InjectViewState
import com.arellomobile.mvp.MvpPresenter
import com.arellomobile.mvp.MvpView
import com.example.artem.speakup.DataWork.ExtraSourceWorker
import com.example.artem.speakup.TimeSpeechAssistant.Data.DBWorkParts
import com.example.artem.speakup.TimeSpeechAssistant.Data.DBWorkSession
import com.example.artem.speakup.TimeSpeechAssistant.Data.Part

@InjectViewState
class CreateNewPresenter : MvpPresenter<CreateNewPresenter.CreateNewView>() {

    private val parts = arrayListOf<Part>(Part(-1, "",0,"",0,0))
    private lateinit var adapter: PartAdapter
    private var currentPos = 0

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
        parts.add(Part(-1, "",0,"",0,0))
        adapter.notifyDataSetChanged()
    }

    fun setTime(minute: Int, second: Int) {
        parts[currentPos].time = ((minute*60 + second)*1000).toLong()
        adapter.notifyDataSetChanged()
    }

    private fun saveSessionToLocalDB(dbw: DBWorkSession, speechName: String): Long {
        val session =  TabAssistantPresenter.prepareSpeech(speechName, parts)
        dbw.setAllValues(session)
        val id = dbw.create()
        session.id = id
        TabAssistantPresenter.addSessionToList(session)
        return id
    }

    private fun savePartsToLocalBD(dbw: DBWorkParts, id: Long) {
        dbw.setSessionId(id)
        parts.forEach { elem ->
            dbw.setAllValues(elem)
            dbw.create()
        }
    }
    fun save(dbw: ExtraSourceWorker, speechName: String) {
        when (dbw) {
            is DBWorkSession -> {
                savePartsToLocalBD(DBWorkParts(dbw.context), saveSessionToLocalDB(dbw, speechName))
            }

            is DBWorkParts -> {
                savePartsToLocalBD(dbw, saveSessionToLocalDB(DBWorkSession(dbw.context), speechName))
            }
        }
    }

    interface CreateNewView : MvpView {
        fun showPartList(adapter: PartAdapter)
        fun onTimeEnter()
    }
}