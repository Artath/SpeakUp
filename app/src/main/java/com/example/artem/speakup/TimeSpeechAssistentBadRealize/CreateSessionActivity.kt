package com.example.artem.speakup.TimeSpeechAssistentBadRealize

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import com.example.artem.speakup.R
import com.example.artem.speakup.TimeSpeechAssistant.AssistantDBContract
import com.example.artem.speakup.TimeSpeechAssistant.DBWorkParts
import com.example.artem.speakup.TimeSpeechAssistant.DBWorkSession
import com.example.artem.speakup.TimeSpeechAssistant.Part
import kotlinx.android.synthetic.main.activity_create_session.*

class CreateSessionActivity : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_session)

        val parts = arrayListOf<Part>(Part(0, "",0,"",0,0))
        parts_list.layoutManager = LinearLayoutManager(applicationContext)
        val adapter = PartAdapter(parts, object : PartAdapter.PartAdapterCallBack{

            override fun deletePart(pos: Int) {
                if (parts.size > 1 ) {
                    parts.removeAt(pos)
                }
            }
        })

        parts_list.adapter = adapter
        adapter.notifyDataSetChanged()

        add_part_btn.setOnClickListener {
            parts.add(Part(0, "" + parts.size,0,"",0,0))
            adapter.notifyDataSetChanged()
        }

        save_btn.setOnClickListener {
            val dbw = DBWorkSession()
            dbw.setCurrentDate()
            dbw.setName(name_sesstion_edtxt.text.toString())
            //dbw.setDescription()
        }
    }
}
