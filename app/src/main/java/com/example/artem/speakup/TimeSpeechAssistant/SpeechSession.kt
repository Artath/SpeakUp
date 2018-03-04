package com.example.artem.speakup.TimeSpeechAssistant

/**
 * Created by ASUS on 01.03.2018.
 */
class SpeechSession (var id: Long,
                     var name: String,
                     var description: String,
                     var dateCreated: Long,
                     var parts: ArrayList<Part>,
                     var lastRaiting: Int)