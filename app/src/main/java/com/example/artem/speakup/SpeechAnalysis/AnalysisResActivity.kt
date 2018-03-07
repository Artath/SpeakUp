package com.example.artem.speakup.SpeechAnalysis

import android.graphics.Typeface
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.example.artem.speakup.R
import com.example.artem.speakup.SpeechAnalysis.Word.Companion.fullAnalysisText
import kotlinx.android.synthetic.main.activity_analysis_res.*

class AnalysisResActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_analysis_res)

        val res = intent.getStringExtra(SpeechRecordsActivity.RES)

        val arrWprds = fullAnalysisText(res)

        res_txt.typeface = Typeface.createFromAsset(assets, "segoepr.ttf")

        var str = ""
        for (elem in arrWprds) {
            str += "Word parasite: " + elem.word + "\n" +
                    "Number of repeate: " + elem.numbRepeate + "\n" +
                    "Frequence repeate: " + elem.frequencyRepeat + "\n"
        }

        res_txt.text = str + "\n" + res + "\n" + arrWprds.size
    }
}
