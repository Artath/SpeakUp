package com.example.artem.speakup

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import kotlinx.android.synthetic.main.tab_records.*

class TabRecords : Fragment() {

    private var mListener: OnFragmentInteractionListener? = null

    private var rAdapter: RecordListAdapter? = null
    private lateinit var rList: RecyclerView

    var callback: Callback? = null

    interface Callback {
        fun getAudioRecords(): ArrayList<AudioRecord>?
    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        return inflater!!.inflate(R.layout.tab_records, container, false)
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        rList = records_list
        rList.layoutManager = LinearLayoutManager(context)

        // Open Recorder activty
        button_new_record.setOnClickListener({_ -> openNewRecordActivity()})

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
                    })
            rList.adapter = rAdapter
        } else {
            rAdapter?.data = data
            rAdapter?.notifyDataSetChanged()
        }
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
