package com.example.artem.speakup

import android.media.MediaMetadataRetriever
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
        mdt.setDataSource(path)
        val duration = mdt.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION).toLong()

        var output = "%02d:%02d".format(
                (duration/1000/60) % 60,
                (duration/1000) % 60
        )

        return output
    }
}