package com.example.artem.speakup

import android.os.Bundle
import android.support.v4.content.ContextCompat
import android.util.Log
import android.view.View
import android.widget.Toast
import com.arellomobile.mvp.MvpAppCompatActivity
import com.arellomobile.mvp.presenter.InjectPresenter
import com.github.mikephil.charting.data.*

import kotlinx.android.synthetic.main.activity_recorder.*
import java.lang.Math.abs

class ActivityRecorder : MvpAppCompatActivity(),
    RecorderPresenter.Interface {

    @InjectPresenter
    lateinit var presenter: RecorderPresenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_recorder)

        record_button.setOnClickListener({ _ -> recordHandler() })

        // Return to main app view
        new_record_audio.setOnClickListener({ _ -> finish() })

        presenter.iniChart()
    }

    fun recordHandler() {
        when( presenter.record_status ) {
            "ready" -> presenter.recorderStart()
            "rec" -> presenter.recorderStop()
        }
    }

    override fun vGetAppPath() {
        presenter.appPath = externalCacheDir.absolutePath
    }

    override fun vUpdateTimer(ts: Long?) {
        if( ts == null ) {
            record_timer.text = "00:00"
            record_timer_ms.text = ":000"
        } else {
            val cts = ts - System.currentTimeMillis()
                record_timer.text = "%02d:%02d".format(
                        abs((cts / 1000 / 60) % 60),
                        abs((cts / 1000) % 60))
                record_timer_ms.text = ":%03d".format(abs(cts % 1000))
        }
    }

    override fun vUpdateButton(state: String) {
        when( state ) {
            "ready" -> record_button.setImageDrawable(
                    ContextCompat.getDrawable(applicationContext, R.drawable.checkbox_blank_circle_red))
            "rec" -> record_button.setImageDrawable(
                    ContextCompat.getDrawable(applicationContext, R.drawable.stop_red))
        }
    }

    override fun vHideNewRecord(hide: Boolean, record: AudioRecord?) {
        new_record_audio.visibility = if ( hide ) View.GONE else View.VISIBLE

        if( !hide && record !== null ) {
            record_name_new.text = record.getAudioName()
            record_date_new.text = record.getAudioDT()
            record_length_new.text = record.getDuration()
        }
    }

    override fun vMessage(msg: String) {
        Toast.makeText( applicationContext, msg, Toast.LENGTH_SHORT).show()
    }

    override fun vInitChart(data: BarDataSet) {
        data.color = ContextCompat.getColor(applicationContext, R.color.colorPrimary)
        data.isHighlightEnabled = false

        record_level_chart.data = BarData(data)

        record_level_chart.description = null
        record_level_chart.legend.isEnabled = false
        record_level_chart.axisLeft.setDrawLabels(false)
        record_level_chart.axisRight.setDrawLabels(false)
        record_level_chart.axisLeft.setDrawAxisLine(false)
        record_level_chart.axisRight.setDrawAxisLine(false)
        record_level_chart.axisLeft.setDrawGridLines(false)
        record_level_chart.axisRight.setDrawGridLines(false)

        record_level_chart.axisRight.axisMaximum = 100F
        record_level_chart.axisLeft.axisMaximum = 100F

        record_level_chart.xAxis.setDrawLabels(false)
        record_level_chart.xAxis.setDrawAxisLine(false)
        record_level_chart.xAxis.setDrawGridLines(false)

        record_level_chart.invalidate()
    }

    override fun vUpdateChart(entry: BarEntry) {
        record_level_chart.data.removeEntry(0F, 0)

        var lvl: ArrayList<BarEntry> = ArrayList<BarEntry>()
        val data = record_level_chart.data

        for( i in 0 .. 98 ) {
            lvl.add(BarEntry(
                    i.toFloat(),
                    data.getDataSetByIndex(0).getEntryForIndex(i).y))
        }
        lvl.add(entry)

        val chartData = BarDataSet(lvl, "Level")
        chartData.color = ContextCompat.getColor(applicationContext, R.color.colorPrimary)
        chartData.isHighlightEnabled = false
        record_level_chart.data = BarData(chartData)

        record_level_chart.notifyDataSetChanged()
        record_level_chart.invalidate()
    }
}
