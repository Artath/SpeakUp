package com.example.artem.speakup.TimeSpeechAssistant

import android.os.CountDownTimer

//class for timer for slide show
class SpeechAssistant (private var listener: SpeechAssistantListener) {

    //current time
    private var currentTime = 0L
    private var timer: CountDownTimer? = null
    //plan time to slide's display
    private var planTime = 0L
    //time's dispersion
    private var dispers = 0L

    //start timer
    fun start(time: Long) {

        planTime = time
        dispers = time/5

        //stop old timer if it exist
        stopTimer()

        timer = object : CountDownTimer(((planTime + dispers) * 1000).toLong(), 100) {

            override fun onTick(millisUntilFinished: Long) {
                //save current time
                currentTime = (millisUntilFinished/1000).toLong()
                //give current time
                listener.showTime("Time: " + millisUntilFinished/1000 + "." + millisUntilFinished % 100)
            }
            override fun onFinish() {
                listener.onLateNext()
            }
        }.start()
    }
    //move to next slide
    fun nextClick() {

        if ((currentTime > 0 ) && currentTime < 2*dispers)  {
            listener.onRightTimeNext()
        } else {
            listener.onEarlyNext()
        }
    }
    //нto back slide
    fun backClick() = listener.onClickBack()
    //stop of timer
    private fun stopTimer() {
        if (timer != null) {
            timer!!.cancel()
        }
    }
    //listener for different timer's events
    interface SpeechAssistantListener {
        //if nextClick() in right time
        fun onRightTimeNext()
        //too early
        fun onEarlyNext()
        //сtoo late
        fun onLateNext()
        //move back
        fun onClickBack()
        //show current time
        fun showTime(t: String)
    }
}