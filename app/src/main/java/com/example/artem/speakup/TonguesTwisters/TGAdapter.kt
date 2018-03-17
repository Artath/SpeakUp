package com.example.artem.speakup.TonguesTwisters

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.artem.speakup.R
import kotlinx.android.synthetic.main.tongues_twist_item.view.*
import java.util.ArrayList

class TGAdapter(var data: ArrayList<TonguesTwister>,
                private var callBack: TGAdapterCallBack) : RecyclerView.Adapter<TGAdapter.UserViewHolder>() {

    private val MAX_LENGTH = 20

    override fun getItemCount() = data.size

    override fun onBindViewHolder(holder: UserViewHolder, position: Int) {
        var text = data[position].text
        if (text.length > MAX_LENGTH) {
            text = text.substring(0, MAX_LENGTH) + "..."
        }

        holder.tongTwistText.text = text
        holder.check.visibility = if (data[position].isSelected) View.VISIBLE else View.INVISIBLE

        holder.itemView.setOnClickListener {
            callBack.multiSelect(position, data[position].isSelected)
            notifyDataSetChanged()
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
            UserViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.tongues_twist_item, parent,false))

    class UserViewHolder(view: View): RecyclerView.ViewHolder(view) {

        var tongTwistText = view.tg_content_txt
        var check = view.check_img
    }

    interface TGAdapterCallBack {
        fun multiSelect(pos: Int, isSelected: Boolean)
    }
}