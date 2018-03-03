package com.example.artem.speakup

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.View
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_main.*
import java.io.File

class MainActivity : AppCompatActivity() {

    private var rAdapter: RecordListAdapter? = null
    private lateinit var rList: RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        rList = records_list
        rList.layoutManager = LinearLayoutManager(applicationContext)

        button_new_record.setOnClickListener({ _-> openNewRecordActivity() })

        updateRecordsList()
    }

    override fun onResume() {
        super.onResume()

        updateRecordsList()
    }

    fun openNewRecordActivity() {
        val intent = Intent(applicationContext, RecordActivity::class.java)
        startActivity(intent)
    }

    fun getAudioRecords(): ArrayList<AudioRecord> {
        val files = File(externalCacheDir.absolutePath).listFiles()
        var data: ArrayList<AudioRecord> = arrayListOf()
        files.forEach {
            f -> data.add(AudioRecord(f.nameWithoutExtension)) }

        return data
    }

    fun updateRecordsList() {
        val data = getAudioRecords()

        if( data.size == 0 )
            records_note.visibility = View.VISIBLE

        if( rAdapter == null ) {
            rAdapter = RecordListAdapter(data,
                    object: RecordListAdapter.ItemClickListener{
                        override fun onListItemClick(item: AudioRecord) {
                            Toast.makeText(applicationContext, "Record clicked", Toast.LENGTH_SHORT).show()
                        }
                    })
            rList.adapter = rAdapter
        } else {
            rAdapter?.data = data
            rAdapter?.notifyDataSetChanged()
        }
    }
}
