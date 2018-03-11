package com.example.artem.speakup

import android.content.Context
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentPagerAdapter
import android.support.v4.app.FragmentStatePagerAdapter
import android.widget.Toast

class TabsAdapter(ctx: Context, fm: FragmentManager): FragmentStatePagerAdapter(fm) {

    val cntx = ctx

    override fun getCount(): Int {
        return 4
    }

    override fun getItem(position: Int): Fragment {
        var view: Fragment? = null
        view = when( position ) {
            0 -> TabRecords.newInstance()
            1 -> TabRecorder.newInstance()
            2 -> TabAssistant.newInstance()
            3 -> TabTwisters.newInstance()
            else -> TabRecords.newInstance()
        }

        return view
    }
}