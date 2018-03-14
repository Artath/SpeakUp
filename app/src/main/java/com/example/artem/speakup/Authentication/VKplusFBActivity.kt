package com.example.artem.speakup.Authentication

import android.app.Activity
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v4.app.FragmentActivity
import android.util.Log
import android.view.View
import android.widget.ProgressBar
import android.widget.Toast
import com.example.artem.speakup.R
import com.example.artem.speakup.TrueMainActivity
import com.firebase.ui.auth.AuthUI
import com.firebase.ui.auth.IdpResponse
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.vk.sdk.VKAccessToken
import com.vk.sdk.VKCallback
import com.vk.sdk.VKScope
import com.vk.sdk.VKSdk
import com.vk.sdk.api.VKError
import kotlinx.android.synthetic.main.activity_authentification.*
import kotlinx.android.synthetic.main.activity_vkplus_fb.*
import java.util.*


class VKplusFBActivity : AppCompatActivity() {

    private val RC_EMAIL_SIGN_IN = 123

    companion object {
        private val scopeArray = arrayOf(VKScope.EMAIL)
        private val TAG = "VkontakteSignInActivity"
    }

    private lateinit var loginProgressBar: ProgressBar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_vkplus_fb)
        loginProgressBar = findViewById(R.id.login_progress_bar)


        val providers = Arrays.asList(
                AuthUI.IdpConfig.Builder(AuthUI.EMAIL_PROVIDER).build())

        val auth = FirebaseAuth.getInstance()
        if (auth.currentUser != null) {
            // already signed in
            Toast.makeText(applicationContext, "Signed In!", Toast.LENGTH_SHORT).show()
            startActivity(Intent(this@VKplusFBActivity, TrueMainActivity::class.java))
        }


        /*if(VKSdk.wakeUpSession(applicationContext)){
            Toast.makeText(applicationContext, "Signed In with VK!", Toast.LENGTH_SHORT).show()
            startActivity(Intent(this@VKplusFBActivity, TrueMainActivity::class.java))
        }*/

        emailLoginBtn.setOnClickListener({
            startActivityForResult(
                    AuthUI.getInstance()
                            .createSignInIntentBuilder()
                            .setAvailableProviders(providers)
                            .build(),
                    RC_EMAIL_SIGN_IN)

        })

        vkontakteLoginButton.setOnClickListener { signIn() }


    }

    private fun signIn(){
        loginProgressBar.visibility = View.VISIBLE
        // Вызываем VKSdk.login для дальшейшего onActivityResult(), который нам понадобится
        VKSdk.login(this, *scopeArray)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {

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
        }else {


            val callback = object : VKCallback<VKAccessToken> {
                override fun onResult(res: VKAccessToken) {
                    //Собственно, вся "магия" имеет место далее.

                    //Здесь может быть любая логика по подготовке нужных
                    //креденшлов для последующей отправки на Firebase.
                    //val authPass = preparePassForAuth(res.userId)
                    val authPass: String = res.userId

                    //Когда данные готовы, получаем instance FirebaseAuth, ведь дальше без него никак,
                    //и, собственно, создаем корневой коллбек на регистрацию/аутентификацию
                    //пользовательских данных, которые понадобятся нам для учета.

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
                                                .addOnCompleteListener(this@VKplusFBActivity) { innerTask ->
                                                    processAuthResult(innerTask, authPass)
                                                }

                                    } else {
                                        firebaseAuthManager
                                                //Не забываем предварительно трансформировать данные в "email-password"
                                                .createUserWithEmailAndPassword("vk_${res.email}", authPass)
                                                .addOnCompleteListener(this@VKplusFBActivity) { innerTask ->
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
                    Toast.makeText(this@VKplusFBActivity, "Authentication failed. vk",
                            Toast.LENGTH_SHORT).show()
                }
            }
            if (!VKSdk.onActivityResult(requestCode, resultCode, data, callback)) {
                super.onActivityResult(requestCode, resultCode, data)
            }


        }

    }

    fun processAuthResult(innerTask: Task<AuthResult>, authPass: String) {
        //Обрабатываем внутренний AuthResult'овский коллбек
        //в зависимости от нужд. Если все ок, делаем свои дела.
        if (innerTask.isSuccessful) {
            vkontakteLoginButton.visibility = View.INVISIBLE
            loginProgressBar.visibility = View.INVISIBLE
            Toast.makeText(this@VKplusFBActivity, "Signed In with VK!",
                    Toast.LENGTH_SHORT).show()
            startActivity(Intent(this@VKplusFBActivity, TrueMainActivity::class.java))
        } else {
            //На случай auth failure Firebase'овского сервиса
            //Log.w(TAG, "createUpdateUserWithEmail:failure", task.exception)
            Toast.makeText(this@VKplusFBActivity, "Authentication failed. fb",
                    Toast.LENGTH_SHORT).show()
        }
    }

    /*fun preparePassForAuth(userId: String): String {
        //Код для хеширования UID, например
    }

    fun getVKUIdFromPass(hashedPass: String): String {
        //Код для расшифровки UID, например
    }*/
}