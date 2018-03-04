package com.example.artem.speakup.TimeSpeechAssistentBadRealize

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import com.example.artem.speakup.R
import kotlinx.android.synthetic.main.activity_create_session.*

class CreateSessionActivity : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_session)

        add_part_btn.setOnClickListener {

            val nameSes = speech_name.text.toString()
            var descr = descr_name.text.toString()
            if (nameSes != "") {
                val intent = Intent(applicationContext, CreatePartActivity::class.java)
                intent.putExtra(SessionsActivity.NAME, nameSes)
                intent.putExtra(SessionsActivity.DESCRIPTION, descr)
                startActivity(intent)
            }
        }
    }
}
