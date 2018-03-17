package com.example.artem.speakup

import android.os.Parcelable
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentStatePagerAdapter
import com.example.artem.speakup.TimeSpeechAssistant.TabAssistant
import com.example.artem.speakup.TonguesTwisters.TabTwisters

class TabsAdapter(fm: FragmentManager):
        FragmentStatePagerAdapter(fm) {

    override fun getCount(): Int {
        return 3
    }

    override fun getItem(position: Int): Fragment {
        return when( position ) {
            0 -> TabRecords.newInstance()
            1 -> TabAssistant.newInstance()
            2 -> TabTwisters.newInstance()
            else -> TabRecords.newInstance()
        }
    }
}