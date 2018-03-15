package com.example.artem.speakup.Auth

import android.app.Application
import com.vk.sdk.VKSdk

/**
 * Created by Дом on 15.03.2018.
 */
public class Application: Application() {
    override fun onCreate() {
        super.onCreate()
        VKSdk.initialize(this)
    }
}