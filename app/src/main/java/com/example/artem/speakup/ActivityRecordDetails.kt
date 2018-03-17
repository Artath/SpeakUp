package com.example.artem.speakup

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.util.Half.toFloat
import android.util.Log
import android.widget.Toast
import com.example.artem.speakup.TonguesTwisters.TGAdapter
import com.example.artem.speakup.TonguesTwisters.TabTwisters
import com.example.artem.speakup.TonguesTwisters.TonguesTwister
import com.example.artem.speakup.TonguesTwisters.TonguesTwistersActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.activity_record_details.*
import kotlinx.android.synthetic.main.tab_twisters.*
import java.util.*
import kotlin.collections.ArrayList

class ActivityRecordDetails : AppCompatActivity() {

    lateinit var name: String
    var arrayList = ArrayList<Any?>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_record_details)

        name = intent.getStringExtra("record_name")

        if( !name.isEmpty() )
            audio_record_edit_text.setText(name)
        else
            Log.e("** speakup **", "ActivityRecordDetails can't access Audio Record name")

        // Save Audio Recrod
        audio_record_finish_edit.setOnClickListener({ _ -> finish() })
        getRecordFromFb()


    }



    fun getRecordFromFb(){
        var mDatabase = FirebaseDatabase.getInstance().reference
        var uid = FirebaseAuth.getInstance().uid
        mDatabase.child("users").child(uid).child("records").child(name)
                .addValueEventListener(object : ValueEventListener {
                    override fun onDataChange(dataSnapshot: DataSnapshot) {


                        for (snapshot in dataSnapshot.child("signalList").children) {
                            val value = snapshot.value
                            arrayList.add(value)
                        }





                        //здесь доступен заполненный данными ArrayList<Double> нашей записи



                        Toast.makeText(applicationContext, arrayList[0].toString(), Toast.LENGTH_SHORT).show()












                    }

                    override fun onCancelled(databaseError: DatabaseError) {
                        Toast.makeText(applicationContext, "Loading error", Toast.LENGTH_SHORT).show()
                    }
                })
    }
}
