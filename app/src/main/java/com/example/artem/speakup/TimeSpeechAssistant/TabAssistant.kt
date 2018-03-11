package com.example.artem.speakup.TimeSpeechAssistant


import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.artem.speakup.R

class TabAssistant : Fragment() {

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        return inflater!!.inflate(R.layout.tab_assistant, container, false)
    }

    companion object {
        fun newInstance(): TabAssistant {
            val fragment = TabAssistant()
            val args = Bundle()

            fragment.arguments = args
            return fragment
        }
    }
}
