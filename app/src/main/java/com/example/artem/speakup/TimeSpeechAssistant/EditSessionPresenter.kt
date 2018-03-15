package com.example.artem.speakup.TimeSpeechAssistant

import com.arellomobile.mvp.InjectViewState
import com.arellomobile.mvp.MvpPresenter
import com.example.artem.speakup.DataWork.ExtraSourceWorker
import com.example.artem.speakup.TimeSpeechAssistant.Data.AssistantDBContract
import com.example.artem.speakup.TimeSpeechAssistant.Data.DBWorkParts
import com.example.artem.speakup.TimeSpeechAssistant.Data.DBWorkSession
import com.example.artem.speakup.TimeSpeechAssistant.Data.Part

@InjectViewState
class EditSessionPresenter : MvpPresenter<CreateNewPresenter.CreateNewView>(){

    private val parts = arrayListOf<Part>()
    private lateinit var adapter: PartAdapter
    private var isResizable = true
    private var idSession = 0L

    fun launchPresenter(dbw: ExtraSourceWorker, id: Long) {
        if (isResizable) {
            idSession = id
            if (dbw is DBWorkParts) {
                parts.addAll(readFromLocalDB(dbw, idSession))
            }
            adapter = PartAdapter(parts, object : PartAdapter.PartAdapterCallBack {

                override fun enterTime(): Long {
                    viewState.onTimeEnter()
                    return 0
                }

                override fun deletePart(pos: Int) {
                    if (parts.size > 1 ) {

                        if (parts[pos].id >= 0 && dbw is DBWorkParts) {
                            deleteFromLocalDB(dbw, parts[pos].id)
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
        parts.add(Part(-1, "",0,"",0,0))
        adapter.notifyDataSetChanged()
    }

    private fun readFromLocalDB(dbw: DBWorkParts, id: Long): ArrayList<Part> {
        dbw.addSelection(AssistantDBContract.Parts.SESSION_ID + " LIKE ?")
        dbw.addSelectionArgs(arrayOf<String>(id.toString()))
        return  dbw.read() as ArrayList<Part>
    }

    private fun deleteFromLocalDB(dbw: DBWorkParts, id: Long) {
        dbw.addSelection(AssistantDBContract.Parts._ID + " LIKE ?")
        dbw.addSelectionArgs(arrayOf<String>(id.toString()))
        dbw.delete()
    }

    private fun updateSessionFromLocalBD(dbw: DBWorkSession, speechName: String, id: Long) {
        val session = TabAssistantPresenter.prepareSpeech(speechName, parts)
        session.id = id
        dbw.run {
            setAllValues(session)
            addSelection(AssistantDBContract.Sessions._ID + " LIKE ?")
            addSelectionArgs(arrayOf<String>(id.toString()))
            update()
        }

        TabAssistantPresenter.replaceSession(session)
    }

    private fun updatePartsFromLocalBD(dbw: DBWorkParts, id: Long) {
        dbw.addSelection(AssistantDBContract.Parts._ID + " LIKE ?")
        dbw.setSessionId(id)
        for (elem in parts) {
            dbw.setAllValues(elem)
            if (elem.id >= 0) {
                dbw.addSelectionArgs(arrayOf<String>(elem.id.toString()))
                dbw.update()
            } else {
                dbw.create()
            }
        }
    }


    fun update(dbw: ExtraSourceWorker, speechName: String) {
        when (dbw) {
            is DBWorkSession -> {
                updateSessionFromLocalBD(dbw, speechName, idSession)
                updatePartsFromLocalBD(DBWorkParts(dbw.context), idSession)
            }

            is DBWorkParts -> {
                updateSessionFromLocalBD(DBWorkSession(dbw.context), speechName, idSession)
                updatePartsFromLocalBD(dbw, idSession)
            } 
        }
    }
}

