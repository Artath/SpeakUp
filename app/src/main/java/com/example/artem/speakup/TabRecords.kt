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
import com.example.artem.speakup.R.id.button_finish_edit
import com.example.artem.speakup.R.id.button_new_record
import kotlinx.android.synthetic.main.tab_records.*
import java.io.File
import com.github.mikephil.charting.charts.Chart.LOG_TAG
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener


class TabRecords : Fragment() {

    private var mListener: OnFragmentInteractionListener? = null

    private var rAdapter: RecordListAdapter? = null
    private lateinit var rList: RecyclerView
    var mDatabase = FirebaseDatabase.getInstance().reference
    var uid = FirebaseAuth.getInstance().uid
    var ref = mDatabase.child("users").child(uid).child("records")
    lateinit var listener : ValueEventListener

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

        /*swipeRefresh.setOnRefreshListener(
                object : SwipeRefreshLayout.OnRefreshListener {
                    override fun onRefresh() {
                        Log.i(LOG_TAG, "onRefresh called from SwipeRefreshLayout")

                        // This method performs the actual data-refresh operation.
                        // The method calls setRefreshing(false) when it's finished.
                        updateRecordsList()
                    }
                }
        )

        updateRecordsList()*/

    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

       /* swipeRefresh.setOnRefreshListener(
                object : SwipeRefreshLayout.OnRefreshListener {
                    override fun onRefresh() {
                        Log.i(LOG_TAG, "onRefresh called from SwipeRefreshLayout")

                        // This method performs the actual data-refresh operation.
                        // The method calls setRefreshing(false) when it's finished.
                        updateRecordsList()
                    }
                }
        )*/

        updateRecordsList()

    }

    fun openNewRecordActivity() {
        val intent = Intent(context, ActivityRecorder::class.java)
        startActivity(intent)
    }

    fun updateRecordsList() {
        var data = ArrayList<AudioRecord>()
        listener = object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                for (snapshot in dataSnapshot.children) {
                    val name = snapshot.key
                    var dt: Long = if( snapshot.child("date").exists() )
                        snapshot.child("date").value as Long else 0
                    var path: String = if( snapshot.child("path").exists() )
                        snapshot.child("path").value as String else ""

                    if( dt == null )
                        dt = 0

                    if( path == null )
                        path = ""

                    val item = AudioRecord(name as String, dt as Long, path as String)
                    data.add(item)
                }
               /* if( data!!.size == 0 ) {
                    records_note.visibility = View.VISIBLE
                    records_refresh.visibility = View.VISIBLE
                    Toast.makeText(context, resources.getString(R.string.no_records), Toast.LENGTH_LONG).show()
                } else {
                    records_note.visibility = View.GONE
                    records_refresh.visibility = View.GONE
                }*/
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

                                    if (File(item.path).exists()){
                                        if (mediaPlayer != null) {
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
                //swipeRefresh.isRefreshing = false
            }

            override fun onCancelled(databaseError: DatabaseError) {
                Toast.makeText(activity, "Loading error", Toast.LENGTH_SHORT).show()
            }
        }
        ref.addListenerForSingleValueEvent(listener)
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
            val uid = FirebaseAuth.getInstance().uid
            val ref = FirebaseDatabase.getInstance().reference
                    .child("users")
                    .child(uid)
                    .child("records")
                    .child(item.getAudioName())
                    .setValue(null)
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