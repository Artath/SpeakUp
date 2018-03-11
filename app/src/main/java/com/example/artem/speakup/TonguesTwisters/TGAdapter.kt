package com.example.artem.speakup.TonguesTwisters

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.artem.speakup.R
import kotlinx.android.synthetic.main.tongues_twist_item.view.*
import java.util.ArrayList

class TGAdapter(var data: ArrayList<TonguesTwister>,
                     var callBack: TGAdapterCallBack) : RecyclerView.Adapter<TGAdapter.UserViewHolder>() {

    override fun getItemCount() = data.size

    override fun onBindViewHolder(holder: UserViewHolder, position: Int) {
        var text = data[position].text
        if (text.length > 30) {
            text = text.substring(0, 30).toString() + "..."
        }

        holder.tongTwistText.text = text
        holder.attempts.text ="Attempts " + data[position].attempts.toString()

        holder.itemView.setOnLongClickListener {
            callBack.multiSelect(data[position].id)
            true
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
            UserViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.tongues_twist_item, parent,false))

    class UserViewHolder(view: View): RecyclerView.ViewHolder(view) {

        var tongTwistText = view.tg_content_txt
        var attempts = view.attempt_txt
    }

    interface TGAdapterCallBack {
        fun multiSelect(id: Long)
    }
}