package com.example.artem.speakup.TonguesTwisters

import android.content.Intent
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.arellomobile.mvp.MvpAppCompatFragment
import com.arellomobile.mvp.presenter.InjectPresenter
import com.example.artem.speakup.R
import kotlinx.android.synthetic.main.tab_twisters.*

class TabTwisters : MvpAppCompatFragment(), TGPresenter.TGPView {

    @InjectPresenter
    lateinit var presenter: TGPresenter

    companion object {
        val SELECTED_TONGUES_TWISTERS = "selectedTG"
        fun newInstance(): TabTwisters {
            val fragment = TabTwisters()
            val args = Bundle()

            fragment.arguments = args
            return fragment
        }
    }


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? =
            inflater.inflate(R.layout.tab_twisters, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        tg_start_btn.setOnClickListener {
            presenter.analyzeChoice()
        }

    }

    override fun showTonguesTwisters(adapter: TGAdapter) {
        tg_recycler.layoutManager = LinearLayoutManager(context)
        tg_recycler.adapter = adapter
        adapter.notifyDataSetChanged()
    }

    override fun showMessage(mess: String) {
        Toast.makeText(context, mess, Toast.LENGTH_SHORT).show()
    }

    override fun takeTG(arrTG: ArrayList<String>) {
        startActivity(Intent(context, TonguesTwistersActivity::class.java)
                .putExtra(SELECTED_TONGUES_TWISTERS, arrTG))
    }

}

