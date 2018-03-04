package com.example.artem.speakup.TimeSpeechAssistentBadRealize

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.artem.speakup.R
import com.example.artem.speakup.TimeSpeechAssistant.DBWorkAssistantSession
import com.example.artem.speakup.TimeSpeechAssistant.Part
import kotlinx.android.synthetic.main.activity_create_part.*

class CreatePartActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_part)

        val parts = arrayListOf<Part>()
        val dbw = DBWorkAssistantSession()

        add_part_btn.setOnClickListener {

            val head = speech_name.text.toString()
            val theses = descr_name.text.toString()
            val time = time_plan.text.toString()

            if (head != "" && theses != "" && time != "") {
                parts.add(Part(head, time.toInt(), theses, 0))
                speech_name.setText("")
                descr_name.setText("")
                time_plan.setText("")
                Toast.makeText(applicationContext, "Часть сформирована", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(applicationContext, "Nu nacher", Toast.LENGTH_SHORT).show()
            }
        }

        save_btn.setOnClickListener {
            if (parts.size != 0) {

                dbw.saveToDBase(applicationContext, intent.getStringExtra(SessionsActivity.NAME),
                        intent.getStringExtra(SessionsActivity.DESCRIPTION), parts)

                Toast.makeText(applicationContext, "Сохранено", Toast.LENGTH_SHORT).show()

                startActivity(Intent(applicationContext, SessionsActivity::class.java))

            }
        }
    }



}