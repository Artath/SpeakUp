package com.example.artem.speakup


import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

class TabTwisters : Fragment() {


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        return inflater!!.inflate(R.layout.tab_twisters, container, false)
    }

    companion object {
        fun newInstance(): TabTwisters{
            val fragment = TabTwisters()
            val args = Bundle()

            fragment.arguments = args
            return fragment
        }
    }
}
