package com.example.artem.speakup.TimeSpeechAssistant.Data

import com.example.artem.speakup.DataWork.ExtraSourceObject

//class for parts of SpeechSession. It is slides. Property order may be needed for reordering
open class Part(override var id: Long,
           var head: String,
           var time: Long,
           var theses: String,
           var session_id: Long,
           var order: Int) : ExtraSourceObject
