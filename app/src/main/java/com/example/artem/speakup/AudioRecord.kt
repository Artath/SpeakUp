package com.example.artem.speakup

import android.media.MediaMetadataRetriever
import android.util.Log
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*

class AudioRecord(var name: String, var dt: Long, var path: String) {

    fun getAudioName(): String {
        return name
    }

    fun getAudioDT(): String {
        val sdf = SimpleDateFormat("E, dd MMM, yyyy, HH:mm")
        val dtf = Date(dt)
        return sdf.format(dtf)
    }

    fun getDuration(): String {
        val mdt = MediaMetadataRetriever()
        var duration: Long = 0

        try {
            mdt.setDataSource(path)
            duration = mdt.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION).toLong()
        } catch(e: RuntimeException) {
            Log.e("** speakup **", "AudioRecord %s getDuration() failed".format(path))
        }

        val output = "%02d:%02d".format(
                (duration/1000/60) % 60,
                (duration/1000) % 60
        )

        return output
    }
}