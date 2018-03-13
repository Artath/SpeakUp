package com.example.artem.speakup

import android.content.Context
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentStatePagerAdapter

class TabsAdapter(ctx: Context, fm: FragmentManager):
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