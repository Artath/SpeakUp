package com.example.artem.speakup.TimeSpeechAssistant

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.artem.speakup.R
import com.example.artem.speakup.TimeSpeechAssistant.Data.ChildPart
import kotlinx.android.synthetic.main.speech_part_item.view.*

class SpeechItemAdapter (var data: ArrayList<ChildPart>) : RecyclerView.Adapter<SpeechItemAdapter.UserViewHolder>() {

    override fun getItemCount() = data.size

    override fun onBindViewHolder(holder: SpeechItemAdapter.UserViewHolder, position: Int) {

        holder.head.text = data[position].head
        holder.checkMark.setImageResource(when (data[position].order){
            1 -> {
                R.drawable.check_red
            }
            2 -> {
                R.drawable.check_all
            }
            else -> {
                R.drawable.check_gray
            }
        })
        holder.planTime.text = String.format("%02d:%02d",
                (data[position].time / 1000) / 60,
                (data[position].time / 1000) % 60)

        holder.details.text = data[position].theses

        holder.details.visibility = if(data[position].selected) View.VISIBLE else View.GONE

        holder.progress.visibility = if (data[position].currentTime > 0L) View.VISIBLE else View.GONE

        if (data[position].time != 0L) {
            holder.progress.progress = (1000*(data[position].currentTime.toDouble()/data[position].time)).toInt()
        }

    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
            SpeechItemAdapter.UserViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.speech_part_item, parent, false))

    class UserViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        var head = view.head_txt
        var checkMark = view.chck_mark_img
        var details = view.details_txt
        var planTime = view.duration_txt
        var progress = view.progress_time
    }

}