package com.example.artem.speakup.TimeSpeechAssistentBadRealize

import android.content.Intent
import android.graphics.Typeface
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.widget.Toast
import com.example.artem.speakup.R
import com.example.artem.speakup.TimeSpeechAssistant.AssistantDBContract
import com.example.artem.speakup.TimeSpeechAssistant.DBWorkParts
import com.example.artem.speakup.TimeSpeechAssistant.DBWorkSession
import com.example.artem.speakup.TimeSpeechAssistant.Part
import kotlinx.android.synthetic.main.activity_parts.*

class PartsActivity : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_parts)

        val id = intent.getLongExtra(SessionsActivity.ID, 0)

        val dbw = DBWorkParts()
        dbw.addSelection(AssistantDBContract.Parts.SESSION_ID+ " LIKE ?")
        dbw.addSelectionArgs(arrayOf<String>("" + id))
        val parts = dbw.read(applicationContext) as ArrayList<Part>

       /* val adapter = PartAdapter(parts)
        part_list.layoutManager = LinearLayoutManager(applicationContext)
        part_list.adapter = adapter*/
        //adapter.callBack = this
      //  adapter.notifyDataSetChanged()

        start_btn.setOnClickListener {
            val intent  = Intent(applicationContext, AssistantActivity::class.java)
            intent.putExtra(SessionsActivity.ID, id)
            startActivity(intent)
        }
    }
}
