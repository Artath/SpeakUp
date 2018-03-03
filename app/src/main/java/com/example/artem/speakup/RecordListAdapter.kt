package com.example.artem.speakup

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import kotlinx.android.synthetic.main.list_record_item.view.*

class RecordListAdapter(var data: ArrayList<AudioRecord>, val itemListener: ItemClickListener):
    RecyclerView.Adapter<RecordListAdapter.ViewHolder>() {

    interface ItemClickListener {
        fun onListItemClick(item: AudioRecord)
    }

    companion object {
        var adapterClickListener: ItemClickListener? = null
    }

    override fun getItemCount() = data.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        adapterClickListener = itemListener
        val entry: AudioRecord = data[position]

        holder.name.text = entry.getAudioName()

        holder.itemView.setOnClickListener(View.OnClickListener {
            if (adapterClickListener != null)
                adapterClickListener?.onListItemClick(entry)
        })
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder =
            ViewHolder(LayoutInflater.from(parent.context)
                    .inflate(R.layout.list_record_item, parent, false))

    class ViewHolder(var view: View): RecyclerView.ViewHolder(view) {
        var name: TextView = view.record_name
    }
}