package com.example.artem.speakup.AudioRecords

import android.content.Context
import android.net.Uri
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.example.artem.speakup.R
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
                              savedInstanceState: Bundle?): View? =
            inflater!!.inflate(R.layout.tab_records, container, false)

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        rList = records_list
        rList.layoutManager = LinearLayoutManager(context)

        // Switch tabs to Recorder activty
        // button_new_record.setOnClickListener({_ -> openNewRecordActivity()})

        updateRecordsList()
    }

//    fun openNewRecordActivity() {
//        val intent = Intent(context, RecordActivity::class.java)
//        startActivity(intent)
//    }

    fun updateRecordsList() {
        // Method getAudioRecords() is used from MainActivity
        val data = callback?.getAudioRecords()

        if( data!!.size == 0 )
            records_note.visibility = View.VISIBLE

        if( rAdapter == null ) {
            rAdapter = RecordListAdapter(data,
                    object : RecordListAdapter.ItemClickListener {
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



    fun onButtonPressed(uri: Uri) {
        if (mListener != null) {
            mListener!!.onFragmentInteraction(uri)
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
