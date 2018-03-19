package com.example.artem.speakup

import android.media.MediaPlayer
import android.util.Log
import com.arellomobile.mvp.InjectViewState
import com.arellomobile.mvp.MvpPresenter
import com.arellomobile.mvp.MvpView
import com.arellomobile.mvp.viewstate.strategy.SkipStrategy
import com.arellomobile.mvp.viewstate.strategy.StateStrategyType
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import java.io.File

@InjectViewState
class RecordsPresenter : MvpPresenter<RecordsPresenter.RecordsView>(){

    private var isResizable = true
    private val CHILD_USERS = "users"
    private val CHILD_RECORDS = "records"
    private val CHILD_DATE = "date"
    private val CHILD_PATH = "path"
    private var adapter: RecordListAdapter? = null

    fun refreshRecords() {
       // if (isResizable) {

        var mDatabase = FirebaseDatabase.getInstance().reference
        var uid = FirebaseAuth.getInstance().uid
        var ref = mDatabase.child(CHILD_USERS).child(uid).child(CHILD_RECORDS)
        val records = arrayListOf<AudioRecord>()

            val listener = object : ValueEventListener {
                override fun onCancelled(p0: DatabaseError?) {
                    viewState.showMessage("Loading error")
                }

                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    for (snapshot in dataSnapshot.children) {
                        val name = snapshot.key
                        var dt: Long = if (snapshot.child(CHILD_DATE).exists())
                            snapshot.child(CHILD_DATE).value as Long else 0L
                        var path: String = if (snapshot.child(CHILD_PATH).exists())
                            snapshot.child(CHILD_PATH).value as String else ""

                        if (dt == null)
                            dt = 0

                        if (path == null)
                            path = ""

                        val item = AudioRecord(name as String, dt, path)
                        records += item
                    }

                    if (records.size == 0) {
                        viewState.changeVisible(true)
                    } else {
                        viewState.changeVisible(true)
                    }

                    val mediaPlayer = MediaPlayer()

                    adapter = RecordListAdapter(records, object : RecordListAdapter.ItemClickListener {
                        override fun onListItemClick(item: AudioRecord) {
                            viewState.showDetails(item)
                        }
                    },
                            object : RecordListAdapter.ItemPlayListener {
                                override fun onItemPlayClick(item: AudioRecord) {

                                    if (File(item.path).exists()){

                                            if (mediaPlayer.isPlaying) {
                                                mediaPlayer.stop()
                                               // mediaPlayer = MediaPlayer()
                                               viewState.showMessage("Record stop")
                                            } else {
                                                mediaPlayer.setDataSource(item.path)
                                                mediaPlayer.prepare()
                                                mediaPlayer.start()
                                                viewState.showMessage("Record play")
                                            }

                                    }
                                }
                            },
                            object : RecordListAdapter.ItemDeleteListener {
                                override fun onItemDeleteClick(item: AudioRecord) {
                                    viewState.showRemoveDialog(item)
                                }
                            },
                            object : RecordListAdapter.ItemLongClickListener {
                                override fun onLongClick(item: AudioRecord) {
                                    switch(true)
                                }
                            })

                    if (adapter != null) {
                        viewState.showRecordList(adapter!!)
                    }
                    viewState.swipeRefresh(false)
                }

            }
            ref.addListenerForSingleValueEvent(listener)
        //    isResizable = false
     //   }

    }

    fun deleteItem(item: AudioRecord) {
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
        refreshRecords()
    }

    fun switch(edit: Boolean) {
        if( adapter != null ) {
            adapter?.edit = edit
        }

        viewState.switchMode(edit)
    }

    interface RecordsView : MvpView {
       // @StateStrategyType(SkipStrategy::class)
        fun showRecordList(adapter: RecordListAdapter)
        @StateStrategyType(SkipStrategy::class)
        fun showMessage(mess: String)
        @StateStrategyType(SkipStrategy::class)
        fun changeVisible(makeVisible: Boolean)
        @StateStrategyType(SkipStrategy::class)
        fun showDetails(item: AudioRecord)
        @StateStrategyType(SkipStrategy::class)
        fun showRemoveDialog(item: AudioRecord)
        @StateStrategyType(SkipStrategy::class)
        fun switchMode(edit: Boolean)
        @StateStrategyType(SkipStrategy::class)
        fun swipeRefresh(isRefreshing: Boolean)
    }
}