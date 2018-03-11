package com.example.artem.speakup.TimeSpeechAssistant

import com.example.artem.speakup.DataWork.ExtraSourceObject

class SpeechSession (override var id: Long,
                     var name: String,
                     var description: String,
                     var dateCreated: Long,
                     var lastRaiting: Int) : ExtraSourceObject