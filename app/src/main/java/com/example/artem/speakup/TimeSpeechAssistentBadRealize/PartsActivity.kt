package com.example.artem.speakup.TimeSpeechAssistentBadRealize

import android.content.Intent
import android.graphics.Typeface
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import com.example.artem.speakup.R
import com.example.artem.speakup.TimeSpeechAssistant.DBWorkAssistantSession
import com.example.artem.speakup.TimeSpeechAssistant.Part
import kotlinx.android.synthetic.main.activity_parts.*

class PartsActivity : AppCompatActivity(), PartAdapter.CallBack {

    lateinit var parts: ArrayList<Part>

    override fun setTypeFace() = Typeface.createFromAsset(assets, "segoepr.ttf")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_parts)

        val id = intent.getLongExtra(SessionsActivity.ID, 0)

        parts = DBWorkAssistantSession()
                .getSessions(applicationContext,
                        id)[0].parts

        val adapter = PartAdapter(parts)
        part_list.layoutManager = LinearLayoutManager(applicationContext)
        part_list.adapter = adapter
        adapter.callBack = this
        adapter.notifyDataSetChanged()

        start_btn.setOnClickListener {
            val intent  = Intent(applicationContext, AssistantActivity::class.java)
            intent.putExtra(SessionsActivity.ID, id)
            startActivity(intent)
        }
    }
}
