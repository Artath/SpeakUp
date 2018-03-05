package com.example.artem.speakup

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.vk.sdk.VKAccessToken
import com.vk.sdk.VKCallback
import com.vk.sdk.VKScope
import com.vk.sdk.VKSdk
import com.vk.sdk.api.VKError
import com.vk.sdk.util.VKUtil
import kotlinx.android.synthetic.main.activity_truly_true_main.*
import com.vk.sdk.VKUIHelper



class TrulyTrueMainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_truly_true_main)
        VKSdk.initialize(this)

        authBtn.setOnClickListener(View.OnClickListener {

            if(VKSdk.wakeUpSession(applicationContext)){
                var intent = Intent(applicationContext, TrueMainActivity::class.java)
                startActivity(intent)
            }else{
                VKSdk.login(this, VKScope.MESSAGES, VKScope.FRIENDS)
            }

        })
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent) {
        if (!// Пользователь успешно авторизовался
        // Произошла ошибка авторизации (например, пользователь запретил авторизацию)
        VKSdk.onActivityResult(requestCode, resultCode, data, object : VKCallback<VKAccessToken> {
            override fun onResult(res: VKAccessToken) {
                var intent = Intent(applicationContext, TrueMainActivity::class.java)
                startActivity(intent)
            }
            override fun onError(error: VKError) {System.exit(0)}
        })) {
            Toast.makeText(applicationContext, "Login failed", Toast.LENGTH_SHORT).show()
            super.onActivityResult(requestCode, resultCode, data)
        }
    }
}
