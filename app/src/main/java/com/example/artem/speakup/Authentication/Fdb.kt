package com.example.artem.speakup.Authentication

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.FirebaseDatabase


/**
 * Created by Artem on 10.03.2018.
 */
class Fdb {


    fun saveSession(session: Session){
        var mUserId = FirebaseAuth.getInstance().currentUser!!.uid
        var mDatabase = FirebaseDatabase.getInstance().getReference();
        mDatabase.child(mUserId).setValue(session)
    }


}