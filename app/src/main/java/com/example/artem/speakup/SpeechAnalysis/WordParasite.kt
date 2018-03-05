package com.example.artem.speakup.SpeechAnalysis

import android.util.Log
import com.homekode.android.tonguetwisters.TongueTwisters.Companion.toRightFormat

/**
 * Created by ASUS on 04.03.2018.
 */

class WordParasite(var word: String, var numbRepeate: Int, var frequenceRepeat: Float) {

    companion object {
        fun registWortParasite(text: String, wordParasite: String): WordParasite {

            val splitText = text.split(" ")
            val maxWords = splitText.size
            var numbWordPar = 0

            for (elem in splitText) {

                if (wordParasite == toRightFormat(elem)) {
                    numbWordPar++
                }
            }

            return WordParasite(wordParasite, numbWordPar, numbWordPar.toFloat()/maxWords)
        }
    }
}