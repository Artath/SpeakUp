package com.example.artem.speakup.TimeSpeechAssistentBadRealize

import android.support.v7.widget.RecyclerView
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.artem.speakup.R
import com.example.artem.speakup.TimeSpeechAssistant.Part
import kotlinx.android.synthetic.main.item_part.view.*
import java.util.ArrayList


class PartAdapter(var data: ArrayList<Part>, var callBack: PartAdapterCallBack) : RecyclerView.Adapter<PartAdapter.UserViewHolder>() {

    override fun getItemCount() = data.size

    override fun onBindViewHolder(holder: UserViewHolder, position: Int) {

        holder.themeItem.text = "Speech item " + (position + 1)
        holder.theme.setText(data[position].head)
        holder.detials.setText(data[position].theses)
        holder.timing.setText(data[position].time.toString())

        holder.deleteBtn.setOnClickListener {
            callBack.deletePart(position)
            notifyDataSetChanged()
        }

        holder.theme.addTextChangedListener(object : MyTextWatcher(){
            override fun afterTextChanged(p0: Editable?) {
                if (p0 != null) data[holder.adapterPosition].head = p0.toString()
            }
        })
        holder.detials.addTextChangedListener(object : MyTextWatcher(){
            override fun afterTextChanged(p0: Editable?) {
                if (p0 != null) data[holder.adapterPosition].theses = p0.toString()
            }
        })
        holder.timing.addTextChangedListener(object : MyTextWatcher(){
            override fun afterTextChanged(p0: Editable?) {
                if (p0 != null) data[holder.adapterPosition].time = p0.toString().toLong()
            }
        })

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
            UserViewHolder(LayoutInflater.from(parent.context)
                    .inflate(R.layout.item_part, parent, false))

    class UserViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        var themeItem = view.speech_item_txt
        var theme = view.theme_edtext
        var detials = view.details_edtext
        var timing = view.timning_edtext
        var deleteBtn = view.delete_btn

    }

    interface PartAdapterCallBack {
        fun deletePart(pos: Int)
    }

    abstract class MyTextWatcher : TextWatcher {

        override fun afterTextChanged(p0: Editable?) {}
        override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
        override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
    }

}