package com.example.artem.speakup.SpeechAnalysis

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import com.example.artem.speakup.R
import kotlinx.android.synthetic.main.activity_choose_words.*
import kotlinx.android.synthetic.main.duration_dialog.*

class ChooseWordsActivity : AppCompatActivity() {

    val words = arrayListOf<String>()
    val WORDS = "words"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_choose_words)

        words_list.layoutManager = LinearLayoutManager(applicationContext)
        words_list.adapter = ChooseWordAdapter(words, object : ChooseWordAdapter.ChooseWordCallBack{
            override fun deleteWord(pos: Int) {
                if (words.size > 0) {
                    words.removeAt(pos)
                    words_list.adapter.notifyDataSetChanged()
                }
            }

        })
        words_list.adapter.notifyDataSetChanged()

        add_word_btn.setOnClickListener {
            words += ""
            words_list.adapter.notifyDataSetChanged()
        }

        next_btn.setOnClickListener {

        }
    }

    override fun onSaveInstanceState(outState: Bundle?) {
        super.onSaveInstanceState(outState)
        if (outState != null) {
            outState.putStringArrayList(WORDS, words)
        }
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle?) {
        super.onRestoreInstanceState(savedInstanceState)
        if (savedInstanceState != null) {
            words.addAll(savedInstanceState.getStringArrayList(WORDS))
        }
    }
}
