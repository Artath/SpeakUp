package com.example.artem.speakup.TimeSpeechAssistant

import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.widget.Toast
import com.arellomobile.mvp.MvpAppCompatActivity
import com.arellomobile.mvp.presenter.InjectPresenter
import com.example.artem.speakup.R
import com.example.artem.speakup.TimeSpeechAssistant.Data.DBWorkParts
import kotlinx.android.synthetic.main.activity_create_new.*

class EditSessionActivity : MvpAppCompatActivity(),
        CreateNewPresenter.CreateNewView,
        TimeEnterDialog.TimeEnterDialogCallBack {

    @InjectPresenter
    lateinit var presenter: EditSessionPresenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_new)
        presenter.launchPresenter(DBWorkParts(applicationContext), intent.getLongExtra(TabAssistant.ID, 0))
        speech_name_edit_text.setText(intent.getStringExtra(TabAssistant.SPEECH_NAME))

        add_part_btn.setOnClickListener {
            presenter.addPart()
        }

        save_btn.text = "Change"
        save_btn.setOnClickListener {
            val speechName = speech_name_edit_text.text.toString()
            if (speechName != "") {
                presenter.update(DBWorkParts(applicationContext), speechName)
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
