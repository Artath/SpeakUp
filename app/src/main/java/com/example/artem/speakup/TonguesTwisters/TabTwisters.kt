package com.example.artem.speakup.TonguesTwisters


import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.example.artem.speakup.R
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.android.synthetic.main.tab_twisters.*
import java.util.*
import kotlin.collections.HashSet

class TabTwisters : Fragment() {


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? =
            inflater.inflate(R.layout.tab_twisters, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        val selectedIdTG = arrayListOf<Long>()
        var mDatabase = FirebaseDatabase.getInstance().reference
        var listener = object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                var id: Long = 1
                var arrayList = ArrayList<TonguesTwister>()
                for (snapshot in dataSnapshot.children) {

                    val value = snapshot.value!!.toString()

                    var tongue = TonguesTwister(id, value, 0,0, false)
                    id++
                    arrayList.add(tongue)
                }

                val adapter = TGAdapter(arrayList, object : TGAdapter.TGAdapterCallBack{
                    override fun multiSelect(id: Long) {
                    }
                })
                tg_recycler.layoutManager = LinearLayoutManager(context)
                tg_recycler.adapter = adapter
                adapter.notifyDataSetChanged()

                tg_start_btn.setOnClickListener({_ ->
                    val randomTG = HashSet<Int>()
                    val rand = Random()
                    while(randomTG.size < 5) {
                        if (randomTG.add(rand.nextInt(arrayList.size))) {
                            selectedIdTG.add(arrayList[randomTG.size - 1].id)
                        }
                    }
                    startActivity(Intent(context, TonguesTwistersActivity::class.java).putExtra(SELECTED_TONGUES_TWISTERS, selectedIdTG))
                })
            }
            override fun onCancelled(databaseError: DatabaseError) {
                Toast.makeText(activity, "Loading error", Toast.LENGTH_SHORT).show()
            }
        }
        mDatabase.child("tongueTwisters").addListenerForSingleValueEvent(listener)


    }

    companion object {
        val SELECTED_TONGUES_TWISTERS = "selectedTG"
        fun newInstance(): TabTwisters {
            val fragment = TabTwisters()
            val args = Bundle()

            fragment.arguments = args
            return fragment
        }
    }
}