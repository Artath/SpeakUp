package com.example.artem.speakup.TimeSpeechAssistant

import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.widget.Toast
import com.arellomobile.mvp.MvpAppCompatActivity
import com.arellomobile.mvp.presenter.InjectPresenter
import com.example.artem.speakup.R
import com.example.artem.speakup.TimeSpeechAssistant.Data.DBWorkSession
import kotlinx.android.synthetic.main.activity_new_session.*
import com.example.artem.speakup.TimeSpeechAssistant.Data.DBWorkParts

class CreateNewActivity : MvpAppCompatActivity(),
        CreateNewPresenter.CreateNewView,
        TimeEnterDialog.TimeEnterDialogCallBack {

    @InjectPresenter
    lateinit var presenter: CreateNewPresenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_new_session)

        new_session_add_part_btn.setOnClickListener {
            presenter.addPart()
        }

        new_session_save_btn.setOnClickListener {
            val speechName = new_speech_name_edit_text.text.toString()
            if (speechName != "") {
                presenter.saveSession(DBWorkSession(applicationContext), speechName)
                presenter.saveParts(DBWorkParts(applicationContext))
                finish()
            }
            else
                Toast.makeText(applicationContext, "Enter speech name!", Toast.LENGTH_SHORT).show()
        }
    }

    override fun showPartList(adapter: PartAdapter) {
        recyc_parts.layoutManager = LinearLayoutManager(applicationContext)
        recyc_parts.adapter = adapter
        adapter.notifyDataSetChanged()
    }

    override fun onTimeEnter() {
        TimeEnterDialog().show(fragmentManager, "DurationDialog")
    }

    override fun takeTime(minute: Int, second: Int) {
        presenter.setTime(minute, second)
    }

}
