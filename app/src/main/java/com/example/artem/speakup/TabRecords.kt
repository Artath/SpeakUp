package com.example.artem.speakup

import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import kotlinx.android.synthetic.main.tab_records.*
import java.io.File

class TabRecords : Fragment() {

    private var mListener: OnFragmentInteractionListener? = null

    private var rAdapter: RecordListAdapter? = null
    private lateinit var rList: RecyclerView

    var callback: Callback? = null

    interface Callback {
        fun getAudioRecords(): ArrayList<AudioRecord>?
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        return inflater!!.inflate(R.layout.tab_records, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        rList = records_list
        rList.layoutManager = LinearLayoutManager(context)

        // Open Recorder activty
        button_new_record.setOnClickListener({_ -> openNewRecordActivity()})

        // Finish Records list edit
        button_finish_edit.setOnClickListener({_ -> switchMode(false)})

        // Refresh Records list
        records_refresh.setOnClickListener({ _ -> updateRecordsList() })

        updateRecordsList()
    }

    fun openNewRecordActivity() {
        val intent = Intent(context, ActivityRecorder::class.java)
        startActivity(intent)
    }

    fun updateRecordsList() {
        // Method getAudioRecords() is used from MainActivity
        val data = callback?.getAudioRecords()

        if( data!!.size == 0 ) {
            records_note.visibility = View.VISIBLE
            records_refresh.visibility = View.VISIBLE
            Toast.makeText(context, resources.getString(R.string.no_records), Toast.LENGTH_LONG).show()
        } else {
            records_note.visibility = View.GONE
            records_refresh.visibility = View.GONE
        }

        if( rAdapter == null ) {
            rAdapter = RecordListAdapter(data,
                    object: RecordListAdapter.ItemClickListener{
                        override fun onListItemClick(item: AudioRecord) {
                            Toast.makeText(context, "Record clicked", Toast.LENGTH_SHORT).show()
                        }
                    },
                    object: RecordListAdapter.ItemPlayListener{
                        override fun onItemPlayClick(item: AudioRecord) {
                            Toast.makeText(context, "Record play", Toast.LENGTH_SHORT).show()
                        }
                    },
                    object: RecordListAdapter.ItemDeleteListener{
                        override fun onItemDeleteClick(item: AudioRecord) {
                            removeRecord(item)
                        }
                    },
                    object: RecordListAdapter.ItemLongClickListener{
                        override fun onLongClick(item: AudioRecord) {
                            switchMode(true)
                        }
                    })
            rList.adapter = rAdapter
        } else {
            rAdapter?.data = data
            rAdapter?.notifyDataSetChanged()
        }
    }

    fun switchMode(edit: Boolean) {
        if( rAdapter != null )
            rAdapter?.edit = edit

        button_new_record.visibility = if(edit) View.GONE else View.VISIBLE
        button_finish_edit.visibility = if(edit) View.VISIBLE else View.GONE
        updateRecordsList()
    }

    fun removeRecord(item: AudioRecord) {
        val dialog = AlertDialog.Builder(context).create()
        dialog.setTitle(R.string.remove_record_title)
        dialog.setMessage(resources.getString(R.string.remove_record_msg).format(item.getAudioName()))

        dialog.setButton(AlertDialog.BUTTON_POSITIVE, "Ok", {
            _, i ->
            try {
                val file = File(item.path)
                file.delete()
            } catch(e: Exception) {
                Log.e("** speakup **", "AudioRecord %s delete failed".format(item.getAudioName()))
            }
            updateRecordsList()
        })

        dialog.setButton(AlertDialog.BUTTON_NEGATIVE, "No, keep it", {
            _, i ->
        })

        dialog.show()
    }

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        callback = context as Callback
    }

    override fun onDetach() {
        super.onDetach()
        mListener = null
        callback = null
    }

    interface OnFragmentInteractionListener {
        fun onFragmentInteraction(uri: Uri)
    }

    companion object {
        fun newInstance(): TabRecords {
            val fragment = TabRecords()
            val args = Bundle()

            fragment.arguments = args
            return fragment
        }
    }
}
