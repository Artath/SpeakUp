package com.example.artem.speakup.TimeSpeechAssistant

import android.support.v7.widget.RecyclerView
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.artem.speakup.R
import com.example.artem.speakup.TimeSpeechAssistant.Data.Part
import kotlinx.android.synthetic.main.part_item.view.*
import java.util.ArrayList


class PartAdapter(var data: ArrayList<Part>,
                  var callBack: PartAdapterCallBack) : RecyclerView.Adapter<PartAdapter.UserViewHolder>() {

    override fun getItemCount() = data.size

    override fun onBindViewHolder(holder: UserViewHolder, position: Int) {

        holder.themeItem.text = "Speech item " + (position + 1)
        holder.theme.setText(data[position].head)
        holder.details.setText(data[position].theses)

        holder.timing
                .setText(String.format("%02d:%02d",
                        (data[position].time / 1000) / 60,
                        (data[position].time / 1000) % 60))

        holder.deleteBtn.setOnClickListener {
            callBack.deletePart(position)
            notifyDataSetChanged()
        }

        holder.theme.addTextChangedListener(object : MyTextWatcher(){
            override fun afterTextChanged(p0: Editable?) {
                if (p0 != null) data[holder.adapterPosition].head = p0.toString()
            }
        })
        holder.details.addTextChangedListener(object : MyTextWatcher(){
            override fun afterTextChanged(p0: Editable?) {
                if (p0 != null) data[holder.adapterPosition].theses = p0.toString()
            }
        })

        holder.timing.setOnClickListener {
            callBack.enterTime(position)
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
            UserViewHolder(LayoutInflater.from(parent.context)
                    .inflate(R.layout.part_item, parent, false))

    class UserViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        var themeItem = view.speech_item_txt
        var theme = view.theme_edtext
        var details = view.details_edtext
        var timing = view.timning_edtext
        var deleteBtn = view.delete_btn

    }

    interface PartAdapterCallBack {
        fun deletePart(pos: Int)
        fun enterTime(pos: Int)
    }

    abstract class MyTextWatcher : TextWatcher {

        override fun afterTextChanged(p0: Editable?) {}
        override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
        override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
    }

}