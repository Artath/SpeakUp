package com.example.artem.speakup.TimeSpeechAssistentBadRealize

import android.graphics.Typeface
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import com.example.artem.speakup.R
import com.example.artem.speakup.TimeSpeechAssistant.DBWorkAssistantSession
import com.example.artem.speakup.TimeSpeechAssistant.Part
import com.example.artem.speakup.TimeSpeechAssistant.SpeechAssistant
import kotlinx.android.synthetic.main.activity_assistant.*

class AssistantActivity : AppCompatActivity() {

    private var scores = 0
    private var partNumb = 0
    private var index = 0
    private lateinit var speechAssistant: SpeechAssistant

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_assistant)

        val parts = DBWorkAssistantSession()
                .getSessions(applicationContext,
                        intent.getLongExtra(SessionsActivity.ID, 0))[0].parts

        val tf = Typeface.createFromAsset(assets, "segoepr.ttf")
        head.typeface = tf
        theses.typeface = tf
        scores_txt.typeface = tf
        text_time.typeface = tf

        head.text= parts[partNumb].head
        theses.text = parts[partNumb].theses
        scores_txt.text = "Scores: " + scores

        speechAssistant = SpeechAssistant(object: SpeechAssistant.SpeechAssistantListener{
            override fun showTime(t: String) {
                text_time.text = t
            }

            override fun onRightTimeNext() {
                partNumb++
                index = partNumb
                if (partNumb <= parts.size - 1) {
                    speechAssistant.start(parts[partNumb].time)
                    scores += 20
                    changeView(parts[partNumb])
                } else {
                    head.text = "Потрачено"
                    theses.text = ""
                }
            }

            override fun onEarlyNext() {
                if (index > partNumb) {
                    partNumb++
                    index = partNumb
                    if (partNumb <= parts.size - 1) {
                        speechAssistant.start(parts[partNumb].time)
                        scores -= 20
                        changeView(parts[partNumb])
                    } else {
                        head.text = "Потрачено"
                        theses.text = ""
                    }
                }
            }

            override fun onLateNext() {
                partNumb++
                index = partNumb
                if (partNumb <= parts.size - 1) {
                    speechAssistant.start(parts[partNumb].time)
                    scores -= 20
                    changeView(parts[partNumb])
                } else {
                    head.text = "Потрачено"
                    theses.text = ""
                }
            }

            override fun onClickBack() {
                scores-=20
                scores_txt.text = "Scores: " + scores
            }

        })

        speechAssistant.start(parts[partNumb].time)

        back_btn.setOnClickListener {
            if (index > 0) {
                index--
                changeView(parts[index])
                speechAssistant.backClick()
            }
        }

        next_btn.setOnClickListener {
            if (index < parts.size - 1) {
                index++
                changeView(parts[index])
                speechAssistant.nextClick()
            }
        }

    }

    private fun changeView(part: Part) {

        head.text = part.head
        theses.text = part.theses
        scores_txt.text = "Scores: " + scores

    }
}
