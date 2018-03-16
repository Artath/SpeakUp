package com.example.artem.speakup.MainPackage

import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentStatePagerAdapter
import com.example.artem.speakup.TimeSpeechAssistant.TabAssistant
import com.example.artem.speakup.SpeechAnalysis.TabRecorder
import com.example.artem.speakup.AudioRecords.TabRecords
import com.example.artem.speakup.TonguesTwisters.TabTwisters

class TabsAdapter(fm: FragmentManager): FragmentStatePagerAdapter(fm) {

    override fun getCount() = 4

    override fun getItem(position: Int): Fragment = when( position ) {
        0 -> TabRecords.newInstance()
        1 -> TabRecorder.newInstance()
        2 -> TabAssistant.newInstance()
        3 -> TabTwisters.newInstance()
        else -> TabRecords.newInstance()
    }
}