package com.example.artem.speakup

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import kotlinx.android.synthetic.main.list_record_item.view.*
import java.io.File

class RecordListAdapter(var data: ArrayList<AudioRecord>?,
                        val itemListener: ItemClickListener,
                        val playListener: ItemPlayListener,
                        val deleteListener: ItemDeleteListener,
                        val lognListener: ItemLongClickListener):
    RecyclerView.Adapter<RecordListAdapter.ViewHolder>() {

    var edit: Boolean = false

    interface ItemClickListener {
        fun onListItemClick(item: AudioRecord)
    }

    interface ItemPlayListener {
        fun onItemPlayClick(item: AudioRecord)
    }

    interface ItemDeleteListener {
        fun onItemDeleteClick(item: AudioRecord)
    }

    interface ItemLongClickListener {
        fun onLongClick(item: AudioRecord)
    }

    companion object {
        var adapterClickListener: ItemClickListener? = null
        var adapterPlayListener: ItemPlayListener? = null
        var adapterDeleteListener: ItemDeleteListener? = null
        var adapterLongClickListener: ItemLongClickListener? = null
    }

    override fun getItemCount() = data!!.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        adapterClickListener = itemListener
        adapterPlayListener = playListener
        adapterDeleteListener = deleteListener
        adapterLongClickListener = lognListener

        val entry: AudioRecord = data!![position]

        holder.play.visibility = if(edit) View.GONE else View.VISIBLE
        holder.delete.visibility = if(edit) View.VISIBLE else View.GONE

        holder.name.text = entry.getAudioName()
        holder.dt.text = entry.getAudioDT()


        if(File(data!![position].path).exists()){
            holder.duration.text = entry.getDuration()
            holder.play.setOnClickListener({
                if (adapterPlayListener != null)
                    adapterPlayListener?.onItemPlayClick(entry)
            })
        }else{
            holder.play.setBackgroundResource(R.color.notification_material_background_media_default_color)
        }




        holder.itemView.setOnClickListener({
            if (adapterClickListener != null)
                adapterClickListener?.onListItemClick(entry)
        })



        holder.delete.setOnClickListener({
            if (adapterDeleteListener != null)
                adapterDeleteListener?.onItemDeleteClick(entry)
        })

        holder.itemView.setOnLongClickListener(View.OnLongClickListener {
            if (adapterLongClickListener != null) {
                if( !edit )
                    adapterLongClickListener?.onLongClick(entry)
            }
            return@OnLongClickListener true
        })
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder =
            ViewHolder(LayoutInflater.from(parent.context)
                    .inflate(R.layout.list_record_item, parent, false))

    class ViewHolder(var view: View): RecyclerView.ViewHolder(view) {
        var name: TextView = view.record_name
        var dt: TextView = view.record_date
        var duration: TextView = view.record_length

        var play: ImageButton = view.record_play
        var delete: ImageButton = view.record_delete
    }
}