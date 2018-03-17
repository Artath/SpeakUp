package com.example.artem.speakup.TimeSpeechAssistant

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.artem.speakup.R
import com.example.artem.speakup.TimeSpeechAssistant.Data.SpeechSession
import kotlinx.android.synthetic.main.session_item.view.*
import java.util.ArrayList

class SessionAdapter(var data: ArrayList<SpeechSession>,
                     var callBack: SessionAdapterCallBack) : RecyclerView.Adapter<SessionAdapter.UserViewHolder>() {

    override fun getItemCount() = data.size

    override fun onBindViewHolder(holder: UserViewHolder, position: Int) {

        holder.nameSes.text = data[position].name
        holder.descr.text = data[position].description
        holder.planTime.text = "Planing time: " + String.format("%02d:%02d",
                (data[position].duration / 1000) / 60,
                (data[position].duration / 1000) % 60)

        holder.parts.text = "Chapters: " + data[position].numbParts.toString()

        holder.bottomPanel.visibility = if (data[position].selected) View.VISIBLE else
            View.GONE

        holder.menuBtn.setOnClickListener {
            data[position].selected = !data[position].selected
            notifyDataSetChanged()
        }

        holder.itemView.setOnClickListener {
            callBack.startSpeechSession(position)
        }

        holder.deleteBtn.setOnClickListener {
            callBack.deleteSpeechSession(position)
        }

        holder.editBtn.setOnClickListener {
            callBack.editSpeechSession(position)
        }
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
            UserViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.session_item, parent,false))

    class UserViewHolder(view: View): RecyclerView.ViewHolder(view) {
        var nameSes = view.name
        var descr = view.descr
        var menuBtn = view.dots_img_btn
        var planTime = view.planing_time_txt
        var parts = view.parts_txt
        var deleteBtn = view.delete_img_btn
        var editBtn = view.edit_img_btn
        var bottomPanel = view.bottom_elem_constraintLayout

    }

    interface SessionAdapterCallBack {

        fun deleteSpeechSession(pos: Int)
        fun editSpeechSession(pos: Int)
        fun startSpeechSession(pos: Int)
    }
}