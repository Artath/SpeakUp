package com.example.artem.speakup.TimeSpeechAssistant

import android.app.TimePickerDialog
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.util.Log
import android.widget.TimePicker
import android.widget.Toast
import com.arellomobile.mvp.MvpAppCompatActivity
import com.arellomobile.mvp.presenter.InjectPresenter
import com.example.artem.speakup.R
import com.example.artem.speakup.TimeSpeechAssistant.Data.DBWorkSession
import kotlinx.android.synthetic.main.activity_create_new.*

class CreateNewActivity : MvpAppCompatActivity(), CreateNewPresenter.CreateNewView {

    @InjectPresenter
    lateinit var presenter: CreateNewPresenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_new)

        add_part_btn.setOnClickListener {
            presenter.addPart()
        }

        save_btn.setOnClickListener {
            val speechName = speech_name_edit_text.text.toString()
            if (speechName != "") {
                presenter.save(DBWorkSession(applicationContext), speechName)
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

    }


}
