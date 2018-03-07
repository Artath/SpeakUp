package com.example.artem.speakup.TimeSpeechAssistant

class SpeechSession (override var id: Long,
                     var name: String,
                     var description: String,
                     var dateCreated: Long,
                     var lastRaiting: Int) : ExtraSourceObject