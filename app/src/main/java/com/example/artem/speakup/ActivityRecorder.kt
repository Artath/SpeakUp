package com.example.artem.speakup

import android.os.Bundle
import android.support.v4.content.ContextCompat
import android.view.View
import android.widget.Toast
import com.arellomobile.mvp.MvpAppCompatActivity
import com.arellomobile.mvp.presenter.InjectPresenter

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
}
