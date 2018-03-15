package com.example.artem.speakup.TimeSpeechAssistant

import com.arellomobile.mvp.InjectViewState
import com.arellomobile.mvp.MvpPresenter
import com.arellomobile.mvp.MvpView
import com.example.artem.speakup.DataWork.ExtraSourceWorker
import com.example.artem.speakup.TimeSpeechAssistant.Data.AssistantDBContract
import com.example.artem.speakup.TimeSpeechAssistant.Data.DBWorkSession
import com.example.artem.speakup.TimeSpeechAssistant.Data.Part
import com.example.artem.speakup.TimeSpeechAssistant.Data.SpeechSession

@InjectViewState
class TabAssistantPresenter : MvpPresenter<TabAssistantPresenter.TabAssistantView>() {


    companion object {

        private val data = arrayListOf<SpeechSession>()
        private var adapter: SessionAdapter? = null
        private var isResizable = true

        fun prepareSpeech(speechName: String, parts: ArrayList<Part>): SpeechSession {
            var descr = ""
            val duration = parts.map { it.time }.sum()
            for (elem in parts) {
                descr += elem.head + " "
                if (descr.length > 30) {
                    descr = descr.substring(0, 30) + "..."
                    break
                }
            }
            return SpeechSession(0, speechName, descr, duration, parts.size, false)
        }

        fun addSessionToList(session: SpeechSession) {
            data.add(session)
            if (adapter != null) {
                adapter!!.notifyDataSetChanged()
            }
        }

        fun replaceSession(session: SpeechSession) {
            (0 until data.size).filter { session.id == data[it].id }.forEach { data[it] = session }
            if (adapter != null) {
                adapter!!.notifyDataSetChanged()
            }
        }
    }

    fun launchPresenter(dbw: ExtraSourceWorker) {
        if (isResizable) {
            if (dbw is DBWorkSession) {
                data.addAll(dbw.read() as ArrayList<SpeechSession>)
            }

            adapter = SessionAdapter(data, object : SessionAdapter.SessionAdapterCallBack {

                override fun deleteSpeechSession(pos: Int) {
                    if (dbw is DBWorkSession) {
                        deleteFromLocalDB(dbw, data[pos].id)
                    }
                    data.removeAt(pos)
                    adapter!!.notifyDataSetChanged()
                }

                override fun editSpeechSession(pos: Int) {
                    viewState.onEditSpeechSession(data[pos].id, data[pos].name)
                }

                override fun startSpeechSession(id: Long) {
                    viewState.onStartSpeechSession(id)
                }
            })
            viewState.showSpeechList(adapter!!)
            isResizable = false
        }
    }

    private fun deleteFromLocalDB(dbw: DBWorkSession, id: Long) {
        dbw.addSelection(AssistantDBContract.Sessions._ID + " LIKE ?")
        dbw.addSelectionArgs(arrayOf<String>(id.toString()))
        dbw.delete()
    }

    interface TabAssistantView : MvpView {
        fun showSpeechList(adapter: SessionAdapter)
        fun onStartSpeechSession(id: Long)
        fun onEditSpeechSession(id: Long, speechName: String)
    }

}