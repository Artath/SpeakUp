package com.example.artem.speakup

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import kotlinx.android.synthetic.main.activity_record_details.*

class ActivityRecordDetails : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_record_details)

        val name = intent.getStringExtra("record_name")

        if( !name.isEmpty() )
            audio_record_edit_text.setText(name)
        else
            Log.e("** speakup **", "ActivityRecordDetails can't access Audio Record name")

        // Save Audio Recrod
        audio_record_finish_edit.setOnClickListener({ _ -> finish() })
    }
}
