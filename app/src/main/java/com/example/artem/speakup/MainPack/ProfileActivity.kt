package com.example.artem.speakup.MainPack

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import com.example.artem.speakup.Auth.AuthActivity
import com.example.artem.speakup.R
import com.firebase.ui.auth.AuthUI

class ProfileActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.profile_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.logoutMenuBtn -> {
                AuthUI.getInstance()
                        .signOut(this)
                        .addOnCompleteListener {
                            Toast.makeText(applicationContext, "Signed Out!", Toast.LENGTH_SHORT).show()
                            startActivity(Intent(this, AuthActivity::class.java))
                        }
            }
        }
        return super.onOptionsItemSelected(item)
    }
}
