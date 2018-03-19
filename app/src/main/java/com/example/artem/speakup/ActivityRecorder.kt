package com.example.artem.speakup

import android.content.Intent
import android.os.Bundle
import android.support.v4.content.ContextCompat
import android.util.Log
import android.view.View
import android.widget.Toast
import com.arellomobile.mvp.MvpAppCompatActivity
import com.arellomobile.mvp.presenter.InjectPresenter
import com.example.artem.speakup.R.id.show_content_txt
import com.example.artem.speakup.SpeechAnalysis.AnalysisPresenter
import com.github.mikephil.charting.data.*

import kotlinx.android.synthetic.main.activity_recorder.*
import ru.yandex.speechkit.Initializer
import ru.yandex.speechkit.InitializerListener
import ru.yandex.speechkit.SpeechKit
import java.lang.Math.abs
import com.vk.sdk.util.VKUtil
import kotlinx.android.synthetic.main.activity_tongues_twisters.*
import java.util.*


class ActivityRecorder : MvpAppCompatActivity(),
    RecorderPresenter.Interface,
    AnalysisPresenter.AnalysisView {

    private val API_KEY = "34e04a4d-07bc-4e70-8527-7b5e49f62cf9"

    @InjectPresenter
    lateinit var presenter: RecorderPresenter

    @InjectPresenter
    lateinit var analysisPresenter: AnalysisPresenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_recorder)



        SpeechKit.getInstance().configure(applicationContext, API_KEY)

        Initializer.create(object : InitializerListener {
            override fun onError(p0: Initializer?, p1: ru.yandex.speechkit.Error?) {}
            override fun onInitializerBegin(p0: Initializer?) {}
            override fun onInitializerDone(p0: Initializer?) {}

        }).start()

        record_button.setOnClickListener({ _ -> recordHandler() })

        // Return to main app view
        new_record_audio.setOnClickListener({ _ ->
            startActivity(Intent(this, MainActivity::class.java)) })

        if( presenter.record_status == "ready" )
            presenter.iniChart()
    }

    fun recordHandler() {
        when( presenter.record_status ) {
            "ready" -> startRecording()
            "rec" ->  stopRecording()
        }
    }

    fun startRecording() {
        presenter.recorderStart()
        analysisPresenter.startRecognizer()
    }

    fun stopRecording() {
        analysisPresenter.cancelRecognizer()
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
        data.setDrawValues(false)

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

        for( i in 0 .. data.entryCount-1 ) {
            lvl.add(BarEntry(
                    i.toFloat(),
                    data.getDataSetByIndex(0).getEntryForIndex(i).y))
        }
        lvl.add(entry)

        val chartData = BarDataSet(lvl, "Level")
        chartData.color = ContextCompat.getColor(applicationContext, R.color.colorPrimary)
        chartData.isHighlightEnabled = false
        chartData.setDrawValues(false)
        record_level_chart.data = BarData(chartData)

        record_level_chart.notifyDataSetChanged()
        record_level_chart.invalidate()
    }

    // AnalysisPresenter implementation
    override fun showPartialRes(text: String) {
        recording_status.text = text
    }

    override fun showResults(cleanText: String) {
        presenter.recorderStop(cleanText)
        Log.d("** speakup **", "showResults %s".format(cleanText))
    }
}
