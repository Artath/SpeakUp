package com.example.artem.speakup.TimeSpeechAssistant

import com.arellomobile.mvp.InjectViewState
import com.arellomobile.mvp.MvpPresenter
import com.arellomobile.mvp.MvpView
import com.arellomobile.mvp.viewstate.strategy.SkipStrategy
import com.arellomobile.mvp.viewstate.strategy.StateStrategyType
import com.example.artem.speakup.DataWork.ExtraSourceWorker
import com.example.artem.speakup.TimeSpeechAssistant.Data.*

@InjectViewState
class TabAssistantPresenter : MvpPresenter<TabAssistantPresenter.TabAssistantView>() {


    companion object {

        private val data = arrayListOf<SpeechSession>()
        private var adapter: SessionAdapter? = null
        private var isResizable = true

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

    fun launchPresenter(extrW: ExtraSourceWorker) {
        if (isResizable) {

            data += DataHelper.readSession(extrW, null)

            adapter = SessionAdapter(data, object : SessionAdapter.SessionAdapterCallBack {

                override fun deleteSpeechSession(pos: Int) {

                    DataHelper.deleteSessionWithParts(extrW, data[pos].id)
                    data.removeAt(pos)
                    adapter!!.notifyDataSetChanged()
                }

                override fun editSpeechSession(pos: Int) {
                    viewState.onEditSpeechSession(data[pos].id, data[pos].name)
                }

                override fun startSpeechSession(pos: Int) {
                    viewState.onStartSpeechSession(data[pos].id, data[pos].duration, data[pos].name)
                }
            })
            viewState.showSpeechList(adapter!!)
            isResizable = false
        }
    }

    fun showList() {
        if (adapter != null) {
        viewState.showSpeechList(adapter!!)
        }
    }

    interface TabAssistantView : MvpView {
        fun showSpeechList(adapter: SessionAdapter)
        @StateStrategyType(SkipStrategy::class)
        fun onStartSpeechSession(id: Long, planingTime: Long, speechName: String)
        @StateStrategyType(SkipStrategy::class)
        fun onEditSpeechSession(id: Long, speechName: String)
    }

}