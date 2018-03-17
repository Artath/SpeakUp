package com.example.artem.speakup.TimeSpeechAssistant

import android.app.DialogFragment
import android.content.DialogInterface
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.artem.speakup.R
import kotlinx.android.synthetic.main.duration_dialog.view.*

class TimeEnterDialog : DialogFragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View {

        val v = inflater.inflate(R.layout.duration_dialog, null)

        val minute = v.minute_picker
        val second = v.sec_picker
        minute.minValue = 0
        minute.maxValue = 59
        second.minValue = 0
        second.maxValue = 59

        v.ok_btn.setOnClickListener {
            if (activity !is TimeEnterDialogCallBack)  {
                throw RuntimeException("This activity doesn't impliment TimeEnterDialogCallBack interface!")
            } else {
                (activity as TimeEnterDialogCallBack).takeTime(minute.value, second.value)
            }
            dismiss()
        }
        v.cancel_btn.setOnClickListener {
            dismiss()
        }
        return v
    }

    override fun onDismiss(dialog: DialogInterface) = super.onDismiss(dialog)

    override fun onCancel(dialog: DialogInterface) = super.onCancel(dialog)

    interface TimeEnterDialogCallBack {
        fun takeTime(minute: Int, second: Int)
    }
}