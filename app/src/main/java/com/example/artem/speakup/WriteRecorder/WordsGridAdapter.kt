package com.example.artem.speakup.WriteRecorder

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.example.artem.speakup.R
import com.example.artem.speakup.SpeechAnalysis.Word
import kotlinx.android.synthetic.main.list_record_word.view.*

class WordsGridAdapter(var data: ArrayList<Word>):
        RecyclerView.Adapter<WordsGridAdapter.ViewHolder>() {

    override fun getItemCount() = data!!.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        val entry: Word = data!![position]

        holder.word.text = entry.word
        holder.percent.text = "%.2f".format( entry.frequencyRepeat * 100 )
        holder.count.text = entry.numbRepeate.toString()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder =
            ViewHolder(LayoutInflater.from(parent.context)
                    .inflate(R.layout.list_record_word, parent, false))

    class ViewHolder(var view: View): RecyclerView.ViewHolder(view) {
        var word: TextView = view.list_record_word
        var count: TextView = view.list_record_word_count
        var percent: TextView = view.list_record_word_percent
    }
}