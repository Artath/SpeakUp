package com.example.artem.speakup.TimeSpeechAssistant

import android.content.Context

//interface for CRUD request from extra source
interface ExtraSourceWorker {

    fun read(context: Context): ArrayList<out ExtraSourceObject>
    fun create(context: Context): Long
    fun update(context: Context): Int
    fun delete(context: Context): Int

}