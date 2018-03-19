package com.example.artem.speakup.Auth

import android.app.Activity
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ProgressBar
import android.widget.Toast
import com.example.artem.speakup.MainPack.MainActivity
import com.example.artem.speakup.R
//import com.example.artem.speakup.TrueMainActivity
import com.firebase.ui.auth.AuthUI
import com.firebase.ui.auth.IdpResponse
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.vk.sdk.VKAccessToken
import com.vk.sdk.VKCallback
import com.vk.sdk.VKScope
import com.vk.sdk.VKSdk
import com.vk.sdk.api.VKError
import kotlinx.android.synthetic.main.activity_auth.*
import java.util.*


class AuthActivity : AppCompatActivity() {

    private val RC_EMAIL_SIGN_IN = 123

    companion object {
        private val scopeArray = arrayOf(VKScope.EMAIL)
        private val TAG = "VkontakteSignInActivity"
    }

    private lateinit var loginProgressBar: ProgressBar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_auth)
        loginProgressBar = findViewById(R.id.login_progress_bar)

        val providers = Arrays.asList(
                AuthUI.IdpConfig.Builder(AuthUI.EMAIL_PROVIDER).build())

        val auth = FirebaseAuth.getInstance()
        if (auth.currentUser != null) {
            // already signed in
            Toast.makeText(applicationContext, "Signed In!", Toast.LENGTH_SHORT).show()
            startMainActivity()
        }


        button_auth_email.setOnClickListener({
            startActivityForResult(
                    AuthUI.getInstance()
                            .createSignInIntentBuilder()
                            .setAvailableProviders(providers)
                            .build(),
                    RC_EMAIL_SIGN_IN)

        })

        button_auth_vk.setOnClickListener { signIn() }


    }

    private fun signIn(){
        loginProgressBar.visibility = View.VISIBLE
        background.visibility = View.VISIBLE
        VKSdk.login(this, *scopeArray)
    }

    private fun startMainActivity(){
        var intent = Intent(this, MainActivity::class.java)
                .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        startActivity(intent)
        finish()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {

        if(resultCode != RESULT_CANCELED) {
            button_auth_vk.isEnabled = false
            button_auth_email.isEnabled = false
            if (requestCode == RC_EMAIL_SIGN_IN) {
                val response = IdpResponse.fromResultIntent(data)

                if (resultCode == Activity.RESULT_OK) {
                    // Successfully signed in
                    //writing new user to fb realtime database
                    writeUserInFirebase()
                    Toast.makeText(applicationContext, "Signed In with Email!", Toast.LENGTH_SHORT).show()
                    startMainActivity()
                    // ...
                } else {
                    // Sign in failed, check response for error code
                    // ...
                }
            } else {


                val callback = object : VKCallback<VKAccessToken> {
                    override fun onResult(res: VKAccessToken) {

                        val authPass: String = res.userId
                        var firebaseAuthManager = FirebaseAuth.getInstance()

                        FirebaseAuth.getInstance()
                                .fetchProvidersForEmail("vk_${res.email}")
                                .addOnCompleteListener { task ->
                                    //Если находим хотя бы одного провайдера с существующими креденшлами,
                                    //логиним пользователя. Иначе, регистрируем его в Firebase Auth.
                                    if (task.isSuccessful && task.result.providers != null) {
                                        if (task.result.providers!!.isNotEmpty()) {
                                            firebaseAuthManager
                                                    //Не забываем предварительно трансформировать данные в "email-password"
                                                    .signInWithEmailAndPassword("vk_${res.email}", authPass)
                                                    .addOnCompleteListener(this@AuthActivity) { innerTask ->
                                                        processAuthResult(innerTask, authPass)
                                                    }

                                        } else {
                                            firebaseAuthManager
                                                    //Не забываем предварительно трансформировать данные в "email-password"
                                                    .createUserWithEmailAndPassword("vk_${res.email}", authPass)
                                                    .addOnCompleteListener(this@AuthActivity) { innerTask ->
                                                        processAuthResult(innerTask, authPass)
                                                    }


                                        }
                                    } else {
                                        //На случай unsuccessful response от самого Firebase'овского сервиса
                                        Log.w(TAG, "FirebaseAuth:failure", task.exception)
                                        Toast.makeText(applicationContext,
                                                "Authentication failed.", Toast.LENGTH_SHORT).show()
                                    }
                                }
                    }

                    override fun onError(error: VKError) {
                        loginProgressBar.visibility = View.INVISIBLE
                        Log.d(TAG, error.errorMessage)
                        Toast.makeText(this@AuthActivity, "Authentication failed. vk",
                                Toast.LENGTH_SHORT).show()
                    }
                }
                if (!VKSdk.onActivityResult(requestCode, resultCode, data, callback)) {
                    super.onActivityResult(requestCode, resultCode, data)
                }

            }
        }else{
            loginProgressBar.visibility = View.INVISIBLE
            background.visibility = View.INVISIBLE
        }

    }

    private fun writeUserInFirebase(){
        val user = FirebaseAuth.getInstance().currentUser!!.uid
        var mDatabase = FirebaseDatabase.getInstance().reference
        mDatabase.child("users").child(user).child("userId").setValue(user)
    }

    fun processAuthResult(innerTask: Task<AuthResult>, authPass: String) {
        //Обрабатываем внутренний AuthResult'овский коллбек
        //в зависимости от нужд. Если все ок, делаем свои дела.
        if (innerTask.isSuccessful) {
            loginProgressBar.visibility = View.INVISIBLE
            Toast.makeText(this@AuthActivity, "Signed In with VK!",
                    Toast.LENGTH_SHORT).show()
            writeUserInFirebase()
            startMainActivity()
        } else {
            //На случай auth failure Firebase'овского сервиса
            Toast.makeText(this@AuthActivity, "Authentication failed. fb",
                    Toast.LENGTH_SHORT).show()
        }
    }
}
