package com.example.artem.speakup.TimeSpeechAssistentBadRealize

import android.graphics.Typeface
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.example.artem.speakup.R
import com.example.artem.speakup.TimeSpeechAssistant.Part
import kotlinx.android.synthetic.main.item_part.view.*
import java.util.ArrayList

/**
 * Created by ASUS on 01.03.2018.
 */
class PartAdapter(var data: ArrayList<Part>) : RecyclerView.Adapter<PartAdapter.UserViewHolder>() {

    var callBack: CallBack? = null

    override fun getItemCount() = data.size

    override fun onBindViewHolder(holder: UserViewHolder, position: Int) {

        holder.head.text = data[position].head
        holder.theses.text = data[position].theses
        holder.timePlan.text = "Plan time " + data[position].time + " sec"

        if (callBack != null) {
            holder.head.typeface = callBack!!.setTypeFace()
            holder.theses.typeface = callBack!!.setTypeFace()
            holder.timePlan.typeface = callBack!!.setTypeFace()
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
            UserViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_part, parent,false))

    class UserViewHolder(view: View): RecyclerView.ViewHolder(view) {

        var head = view.head
        var theses = view.thesis
        var timePlan = view.time
    }

    interface CallBack {
        fun setTypeFace(): Typeface
    }
}