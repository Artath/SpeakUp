package com.example.artem.speakup

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import com.example.artem.speakup.TimeSpeechAssistentBadRealize.SessionsActivity
import com.example.artem.speakup.TonguesTwisters.TongueTwistersActivity
import com.homekode.android.tonguetwisters.TongueTwisters
import kotlinx.android.synthetic.main.activity_true_main.*

class TrueMainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_true_main)

        to_record_btn.setOnClickListener {
            startActivity(Intent(applicationContext, MainActivity::class.java))
        }
        speech_btn.setOnClickListener {
            startActivity(Intent(applicationContext, SessionsActivity::class.java))
        }

        tt_btn.setOnClickListener {
            startActivity(Intent(applicationContext, TongueTwistersActivity::class.java))
        }
    }
}
