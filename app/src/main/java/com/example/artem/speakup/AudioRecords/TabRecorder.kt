package com.example.artem.speakup.AudioRecords


import android.content.Context
import android.os.Bundle
import android.os.CountDownTimer
import android.support.v4.app.Fragment
import android.support.v4.content.ContextCompat
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.artem.speakup.R
import kotlinx.android.synthetic.main.tab_recorder.*
import java.io.File
import java.lang.Math.abs

class TabRecorder : Fragment() {

    var record_status: String = "ready" // rec, stop, ready
    var aRecorder: AudioRecorder? = null
    var aTimer: CountDownTimer? = null

    var callback: Callback? = null

    interface Callback {
        fun getAppPath(): String
    }

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        callback = context as Callback
    }

    override fun onDetach() {
        super.onDetach()
        callback = null
    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? =
            inflater!!.inflate(R.layout.tab_recorder, container, false)

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        record_button.setOnClickListener({ _ -> recordHandler() })

        // Switch tabs to Records list
        // new_record_audio.setOnClickListener({ _ -> finish() })
    }

    fun recordHandler() {
        when( record_status ) {
            "ready" -> startRecord()
            "rec" -> stopRecord()
        }
    }

    fun startRecord() {
        record_status = "rec"
        record_button.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.stop_red))

        new_record_audio.visibility = View.GONE

        startTimer()

        var fileName = callback?.getAppPath()
        val files = File(fileName).listFiles()
        fileName += "/NewRecord-%s.3gp".format(files.size)

        aRecorder = AudioRecorder()
        aRecorder?.startRecording(fileName as String)
    }

    fun stopRecord() {
        if( aRecorder != null ) {
            record_status = "ready"
            record_button.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.checkbox_blank_circle_red))

            aRecorder?.stopRecording()

            stopTimer()

            new_record_audio.visibility = View.VISIBLE

            // Get new recorded audio file
            val record = aRecorder?.getAudioRecord()
            record_name_new.text = record!!.getAudioName()
            record_date_new.text = record.getAudioDT()
            record_length_new.text = record.getDuration()

            aRecorder = null
        }
    }

    fun startTimer() {
        if( aTimer != null ) {
            stopTimer()
        }
        val startTS = System.currentTimeMillis()

        aTimer = object: CountDownTimer(60*60*1000, 100) {
            override fun onTick(p0: Long) {
                val ts = startTS - System.currentTimeMillis()
                record_timer.text = "%02d:%02d:%03d".format(
                        abs((ts / 1000 / 60) % 60),
                        abs((ts / 1000) % 60),
                        abs(ts % 1000))
            }

            override fun onFinish() { }
        }
        aTimer?.start()
    }

    fun stopTimer() {
        if( aTimer != null ) {
            aTimer?.cancel()
            aTimer = null
            record_timer.text = "00:00:000"
        }
    }

    companion object {
        fun newInstance(): TabRecorder {
            val fragment = TabRecorder()
            val args = Bundle()

            fragment.arguments = args
            return fragment
        }
    }
}
