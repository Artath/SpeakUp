package com.example.artem.speakup.Authentication

import android.app.Activity
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.example.artem.speakup.R
import com.google.firebase.auth.FirebaseAuth
import com.firebase.ui.auth.AuthUI
import kotlinx.android.synthetic.main.activity_authentification.*
import java.util.*
import com.firebase.ui.auth.IdpResponse

class AuthenticationActivity : AppCompatActivity() {

    private val RC_EMAIL_SIGN_IN = 123


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_authentification)

        val providers = Arrays.asList(
                AuthUI.IdpConfig.Builder(AuthUI.EMAIL_PROVIDER).build())

        val auth = FirebaseAuth.getInstance()
        if (auth.currentUser != null) {
            // already signed in
            Toast.makeText(applicationContext, "Signed In with Email!", Toast.LENGTH_SHORT).show()
        }


        signOutBtn.setOnClickListener({
            AuthUI.getInstance()
                    .signOut(this)
                    .addOnCompleteListener {
                        Toast.makeText(applicationContext, "Signed Out!", Toast.LENGTH_SHORT).show()
                    }
        })


        signInBtn.setOnClickListener({
                startActivityForResult(
                        AuthUI.getInstance()
                                .createSignInIntentBuilder()
                                .setAvailableProviders(providers)
                                .build(),
                        RC_EMAIL_SIGN_IN)

        })

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == RC_EMAIL_SIGN_IN) {
            val response = IdpResponse.fromResultIntent(data)

            if (resultCode == Activity.RESULT_OK) {
                // Successfully signed in
                val user = FirebaseAuth.getInstance().currentUser!!.uid
                //здесь записать нового юзера в базу-----------------------------------------------------------------------db
                Toast.makeText(applicationContext, "Signed In with Email!", Toast.LENGTH_SHORT).show()
                startActivity(Intent(this, TestActivity::class.java))
                // ...
            } else {
                // Sign in failed, check response for error code
                // ...
            }
        }
    }


}
