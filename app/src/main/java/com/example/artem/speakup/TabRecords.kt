package com.example.artem.speakup

import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.media.MediaPlayer
import android.net.Uri
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.widget.SwipeRefreshLayout
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import kotlinx.android.synthetic.main.tab_records.*
import java.io.File
import com.github.mikephil.charting.charts.Chart.LOG_TAG



class TabRecords : Fragment() {

    private var mListener: OnFragmentInteractionListener? = null

    private var rAdapter: RecordListAdapter? = null
    private lateinit var rList: RecyclerView

    var callback: Callback? = null

    interface Callback {
        fun getARecordsForTabRecords(): ArrayList<AudioRecord>?
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        return inflater.inflate(R.layout.tab_records, container, false)
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

        swipeRefresh.setOnRefreshListener(
                object : SwipeRefreshLayout.OnRefreshListener {
                    override fun onRefresh() {
                        Log.i(LOG_TAG, "onRefresh called from SwipeRefreshLayout")

                        // This method performs the actual data-refresh operation.
                        // The method calls setRefreshing(false) when it's finished.
                        updateRecordsList()
                    }
                }
        )

        updateRecordsList()

    }

    fun openNewRecordActivity() {
        val intent = Intent(context, ActivityRecorder::class.java)
        startActivity(intent)
    }

    fun updateRecordsList() {
        // Method getARecordsForTabRecords() is used from MainActivity
        val data = callback?.getARecordsForTabRecords()

        if( data!!.size == 0 ) {
            records_note.visibility = View.VISIBLE
            records_refresh.visibility = View.VISIBLE
            Toast.makeText(context, resources.getString(R.string.no_records), Toast.LENGTH_LONG).show()
        } else {
            records_note.visibility = View.GONE
            records_refresh.visibility = View.GONE
        }

        var mediaPlayer = MediaPlayer()

        if( rAdapter == null ) {
            rAdapter = RecordListAdapter(data,
                    object: RecordListAdapter.ItemClickListener{
                        override fun onListItemClick(item: AudioRecord) {
                            val intent = Intent(context, ActivityRecordDetails::class.java)
                            intent.putExtra("record_name", item.name)
                            startActivity(intent)
                        }
                    },
                    object: RecordListAdapter.ItemPlayListener{
                        override fun onItemPlayClick(item: AudioRecord) {
                            if(mediaPlayer != null) {
                                var path = item.path
                                if (mediaPlayer.isPlaying) {
                                    mediaPlayer.stop()
                                    mediaPlayer = MediaPlayer()
                                    Toast.makeText(context, "Record stop", Toast.LENGTH_SHORT).show()
                                } else {
                                    mediaPlayer.setDataSource(path)
                                    mediaPlayer.prepare()
                                    mediaPlayer.start()
                                    Toast.makeText(context, "Record play", Toast.LENGTH_SHORT).show()
                                }
                            }
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
        swipeRefresh.isRefreshing = false
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
            _, _ ->
            try {
                val file = File(item.path)
                file.delete()
            } catch(e: Exception) {
                Log.e("** speakup **", "AudioRecord %s delete failed".format(item.getAudioName()))
            }
            updateRecordsList()
        })

        dialog.setButton(AlertDialog.BUTTON_NEGATIVE, "No, keep it", {
            _, _ ->
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
