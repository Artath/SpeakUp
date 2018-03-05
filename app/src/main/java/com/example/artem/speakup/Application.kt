package com.example.artem.speakup

import android.app.Application
import com.vk.sdk.VKSdk

/**
 * Created by Дом on 05.03.2018.
 */
class Application: Application() {
    override fun onCreate() {
        super.onCreate()
        VKSdk.initialize(this)
    }
}