package com.example.artem.speakup.SpeechAnalysis

class WordParasite(var word: String, var numbRepeate: Int, var frequencyRepeat: Float) {

    companion object {

        fun registerWordParasite(text: String, wordParasite: String): WordParasite {

            val splitText = text.split(" ")
            var maxWords = 0
            var numbWordParasite = 0
            var frequencyRepeat = 0.0f
            val swh = StringWorkHelper()
            for (elem in splitText) {
                if (swh.toRightFormat(elem) != "") { maxWords++ }
                if (swh.toRightFormat(elem) == wordParasite) { numbWordParasite++ }
            }

            try {
                frequencyRepeat = numbWordParasite.toFloat()/maxWords
            }
            catch (e: Exception) { maxWords = 1 }

            return WordParasite(wordParasite, numbWordParasite, frequencyRepeat)
        }

    }
}