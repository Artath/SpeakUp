package com.example.artem.speakup.TimeSpeechAssistant

import com.arellomobile.mvp.InjectViewState
import com.arellomobile.mvp.MvpPresenter
import com.example.artem.speakup.DataWork.ExtraSourceWorker
import com.example.artem.speakup.TimeSpeechAssistant.Data.*

@InjectViewState
class EditSessionPresenter : MvpPresenter<CreateNewPresenter.CreateNewView>(){

    private val parts = arrayListOf<Part>()
    private lateinit var adapter: PartAdapter
    private var isResizable = true
    private var sessionId = -1L
    private var currentPos = 0

    fun launchPresenter(extrW: ExtraSourceWorker, id: Long) {
        if (isResizable) {
            sessionId = id
                parts += DataHelper.readPart(extrW, sessionId)
            adapter = PartAdapter(parts, object : PartAdapter.PartAdapterCallBack {

                override fun enterTime(pos: Int) {
                    currentPos = pos
                    viewState.onTimeEnter()
                }

                override fun deletePart(pos: Int) {
                    if (parts.size > 1 ) {

                        if (parts[pos].id >= 0) {
                            DataHelper.deletePart(extrW, parts[pos].id)
                        }
                        parts.removeAt(pos)
                        adapter.notifyDataSetChanged()
                    }
                }
            })

            viewState.showPartList(adapter)
            isResizable = false
        }
    }

    fun addPart() {
        parts += Part(-1, "",0,"",0,0)
        adapter.notifyDataSetChanged()
    }

    fun setTime(minute: Int, second: Int) {
        parts[currentPos].time = ((minute*60 + second)*1000).toLong()
        adapter.notifyDataSetChanged()
    }

    fun updateSession(extrW: ExtraSourceWorker, speechName: String) {
        val session = DataHelper.prepareSpeech(speechName, parts)
        DataHelper.updateSession(extrW, sessionId, session)
        session.id = sessionId
        TabAssistantPresenter.replaceSession(session)
    }

    fun updateParts(extrW: ExtraSourceWorker) = DataHelper.customUpdatePart(extrW, sessionId, parts)

}

