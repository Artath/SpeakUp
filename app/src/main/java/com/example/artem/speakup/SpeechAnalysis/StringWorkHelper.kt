package com.example.artem.speakup.SpeechAnalysis

/**
 * Created by ASUS on 06.03.2018.
 */
class StringWorkHelper {

    private val charFilter = arrayOf('.', ',', ':', '-', ' ', ';', '?', '!', '[', ']')

    //to lowercase, delete chars if they in charFilter
    fun toRightFormat(string: String): String {
        val str = string.toLowerCase().toCharArray()
        val res = StringBuffer("")
        (0 until str.size).filter { str[it] !in charFilter }.forEach { res.append(str[it]) }
        return res.toString()
    }
    // function for counter all words in text, counter separate words and frequence of entrance of each
    fun fullAnalysisText(text: String) {

    }

}