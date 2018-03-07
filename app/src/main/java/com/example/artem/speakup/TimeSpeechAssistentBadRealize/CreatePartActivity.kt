package com.example.artem.speakup.TimeSpeechAssistentBadRealize

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.artem.speakup.R
import com.example.artem.speakup.TimeSpeechAssistant.DBWorkParts
import com.example.artem.speakup.TimeSpeechAssistant.DBWorkSession
import com.example.artem.speakup.TimeSpeechAssistant.Part
import kotlinx.android.synthetic.main.activity_create_part.*

class CreatePartActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_part)

        val id = intent.getLongExtra(SessionsActivity.ID, 0)
        val dbw = DBWorkParts()

        add_part_btn.setOnClickListener {

            val head = speech_name.text.toString()
            val theses = descr_name.text.toString()
            val time = time_plan.text.toString()

            if (head != "" && theses != "" && time != "") {

                dbw.setHead(head)
                dbw.setTheses(theses)
                dbw.setTime(time.toLong())
                dbw.setOrder(0)
                dbw.setSessionId(id)

                speech_name.setText("")
                descr_name.setText("")
                time_plan.setText("")
                dbw.create(applicationContext)

                Toast.makeText(applicationContext, "Часть сформирована", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(applicationContext, "Nu nacher", Toast.LENGTH_SHORT).show()
            }
        }

        ok_btn.setOnClickListener {

                startActivity(Intent(applicationContext, SessionsActivity::class.java))

            }
        }
    }



