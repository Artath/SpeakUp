package com.example.artem.speakup.TimeSpeechAssistentBadRealize

import android.graphics.Typeface
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.artem.speakup.R
import com.example.artem.speakup.TimeSpeechAssistant.SpeechSession
import kotlinx.android.synthetic.main.item_part.view.*
import kotlinx.android.synthetic.main.item_session.view.*
import java.util.ArrayList

/**
 * Created by ASUS on 01.03.2018.
 */
class SessionAdapter(var data: ArrayList<SpeechSession>,
                     var callBack: SessionAdapterCallBack) : RecyclerView.Adapter<SessionAdapter.UserViewHolder>() {

    override fun getItemCount() = data.size

    override fun onBindViewHolder(holder: UserViewHolder, position: Int) {

        holder.nameSes.text = data[position].name
        holder.descr.text = data[position].description

        holder.itemView.setOnClickListener {
            callBack.startAssistant(data[position].id)
        }
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
            UserViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_session, parent,false))

    class UserViewHolder(view: View): RecyclerView.ViewHolder(view) {
        var nameSes = view.name
        var descr = view.descr
        var menu = view.menu_btn

    }

    interface SessionAdapterCallBack {
        fun startAssistant(id: Long)
        fun deleteSession(id: Long, pos: Int)
        fun editSession(id: Long)
    }
}