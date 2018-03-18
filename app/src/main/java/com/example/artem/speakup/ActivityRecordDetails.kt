package com.example.artem.speakup

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v4.content.ContextCompat
import android.support.v7.widget.LinearLayoutManager
import android.util.Half.toFloat
import android.util.Log
import android.view.View
import android.widget.Toast
import com.example.artem.speakup.TonguesTwisters.TGAdapter
import com.example.artem.speakup.TonguesTwisters.TabTwisters
import com.example.artem.speakup.TonguesTwisters.TonguesTwister
import com.example.artem.speakup.TonguesTwisters.TonguesTwistersActivity
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.activity_record_details.*
import kotlinx.android.synthetic.main.tab_twisters.*
import java.io.File
import java.util.*
import kotlin.collections.ArrayList

class ActivityRecordDetails : AppCompatActivity() {

    var arrayList = ArrayList<BarEntry>()
    var record: AudioRecord? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_record_details)

        val name = intent.getStringExtra("record_name")

        if( !name.isEmpty() ) {
            val records = getAudioRecords()
            if (records != null) {
                if( !records.isEmpty() ) {
                    records.forEach { i ->
                        if( i.name == name )
                            record = i
                    }

                    if( record != null ) {
                        audio_record_edit_text.setText(record?.name)
                        audio_record_text_date.text = record?.getAudioDT()
                        audio_record_text_time.text = record?.getDuration()

                        getRecordFromFb(record?.name.toString())
                    } else
                        Log.e("** speakup **", "ActivityRecordDetails can't access Audio records %s".format(name))
                } else
                    Log.e("** speakup **", "ActivityRecordDetails can't access Audio records (2)")
            } else
                Log.e("** speakup **", "ActivityRecordDetails can't access Audio records (1)")
        } else
            Log.e("** speakup **", "ActivityRecordDetails can't access Audio Record name")

        // Save Audio Recrod
        audio_record_finish_edit.setOnClickListener({ _ -> finish() })
    }



    fun getRecordFromFb(name: String){
        var mDatabase = FirebaseDatabase.getInstance().reference
        var uid = FirebaseAuth.getInstance().uid
        mDatabase.child("users").child(uid).child("records").child(name)
            .addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {

                    var i = 0
                    for (snapshot in dataSnapshot.child("signalList").children) {
                        val value = snapshot.value
                        arrayList.add(BarEntry(i.toFloat(), value.toString().toFloat()))
                        i++
                    }

                    if( arrayList.size > 0 ) {
                        audio_record_level_chart.visibility = View.VISIBLE
                        drawChart(arrayList)
                    } else {
                        audio_record_level_chart.visibility = View.GONE
                        Toast.makeText(applicationContext, R.string.audio_record_no_db_data, Toast.LENGTH_SHORT).show()
                    }

                    var text = dataSnapshot.child("text").value
                    audio_record_text.text = text.toString()
                }

                override fun onCancelled(databaseError: DatabaseError) {
                    Toast.makeText(applicationContext, "Loading error", Toast.LENGTH_SHORT).show()
                }
        })
    }

    fun drawChart(data: ArrayList<BarEntry>) {
        val dataSet = BarDataSet(data, "Level")
        dataSet.color = ContextCompat.getColor(applicationContext, R.color.colorPrimary)
        dataSet.isHighlightEnabled = false
        dataSet.setDrawValues(false)

        audio_record_level_chart.data = BarData(dataSet)

        audio_record_level_chart.description = null
        audio_record_level_chart.legend.isEnabled = false
        audio_record_level_chart.axisLeft.setDrawLabels(false)
        audio_record_level_chart.axisRight.setDrawLabels(false)
        audio_record_level_chart.axisLeft.setDrawAxisLine(false)
        audio_record_level_chart.axisRight.setDrawAxisLine(false)
        audio_record_level_chart.axisLeft.setDrawGridLines(false)
        audio_record_level_chart.axisRight.setDrawGridLines(false)

        audio_record_level_chart.axisRight.axisMaximum = 100F
        audio_record_level_chart.axisLeft.axisMaximum = 100F

        audio_record_level_chart.xAxis.setDrawLabels(false)
        audio_record_level_chart.xAxis.setDrawAxisLine(false)
        audio_record_level_chart.xAxis.setDrawGridLines(false)

        audio_record_level_chart.invalidate()
    }

    fun getAudioRecords(): ArrayList<AudioRecord>? {
        val files = File(externalCacheDir.absolutePath).listFiles()
        val data: ArrayList<AudioRecord> = arrayListOf()


        files.forEach {
            f -> data.add(
                AudioRecord(
                        f.nameWithoutExtension,
                        f.lastModified(),
                        f.path)) }

        data.reverse()
        return data
    }
}
