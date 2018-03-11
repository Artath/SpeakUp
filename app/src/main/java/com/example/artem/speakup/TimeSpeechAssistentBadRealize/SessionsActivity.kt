package com.example.artem.speakup.TimeSpeechAssistentBadRealize

import android.content.Intent
import android.graphics.Typeface
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import com.example.artem.speakup.R
import com.example.artem.speakup.TimeSpeechAssistant.DBWorkSession
import com.example.artem.speakup.TimeSpeechAssistant.SpeechSession
import kotlinx.android.synthetic.main.activity_sessions.*

class SessionsActivity : AppCompatActivity(){

    companion object {
        val ID = "id"
        val POS = "pos"
        val NAME = "name"
        val DESCRIPTION = "description"
        val TIME = "time"
        val MOD_EDIT = "mod_edit"
    }

    lateinit var sessions: ArrayList<SpeechSession>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sessions)

        sessions = DBWorkSession().read(applicationContext) as ArrayList<SpeechSession>

        speech_list.layoutManager = LinearLayoutManager(applicationContext)
        val adapter = SessionAdapter(sessions, object : SessionAdapter.SessionAdapterCallBack{

            override fun startAssistant(id: Long) {

            }

            override fun deleteSession(id: Long, pos: Int) {

            }

            override fun editSession(id: Long) {

            }

        })
        speech_list.adapter = adapter
        adapter.notifyDataSetChanged()

        add_part.setOnClickListener {
            startActivity(Intent(applicationContext, CreateSessionActivity::class.java))
        }

    }
}