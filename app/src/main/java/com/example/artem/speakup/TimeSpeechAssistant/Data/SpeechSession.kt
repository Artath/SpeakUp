package com.example.artem.speakup.TimeSpeechAssistant.Data

import com.example.artem.speakup.DataWork.ExtraSourceObject

class SpeechSession (override var id: Long,
                     var name: String,
                     var description: String,
                     var duration: Long,
                     var numbParts: Int,
                     var selected: Boolean) : ExtraSourceObject