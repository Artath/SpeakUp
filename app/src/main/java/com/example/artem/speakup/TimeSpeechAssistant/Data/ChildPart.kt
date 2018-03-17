package com.example.artem.speakup.TimeSpeechAssistant.Data

class ChildPart (id: Long,
                 head: String,
                 time: Long,
                 theses: String,
                 session_id: Long,
                 order: Int,
                 var currentTime: Long,
                 var selected: Boolean) : Part(id, head, time, theses, session_id, order)

