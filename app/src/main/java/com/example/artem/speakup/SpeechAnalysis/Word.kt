package com.example.artem.speakup.SpeechAnalysis

class Word(var word: String, var numbRepeate: Int, var frequencyRepeat: Float) {

    companion object {

        private val charFilter = arrayOf('.', ',', ':', '-', ' ', ';', '?', '!', '[', ']')

        fun registerWord(text: String, word: String): Word {

            var maxWords = countWordsInText(text)
            val numbWordParasite = (text.toLowerCase().length - text.toLowerCase().replace(word, "").length) / word.length
            if (maxWords == 0){
                maxWords = 1
            }
            return Word(word, numbWordParasite, numbWordParasite.toFloat()/maxWords)
        }

        //to lowercase, delete chars if they in charFilter
        fun toRightFormat(string: String): String {
            val str = string.toLowerCase().toCharArray()
            val res = StringBuffer("")
            (0 until str.size).filter { str[it] !in charFilter }.forEach { res.append(str[it]) }
            return res.toString()
        }

        fun countWordsInText(text: String): Int =
                text.split(" ").sorted().count { toRightFormat(it) != "" }

        // function for counter all words in text, counter separate words and frequence of entrance of each
        fun fullAnalysisText(text: String): ArrayList<Word> {

            val splitText = text.split(" ").sorted()
            var currentWord = ""
            var formatElem = ""
            val words = arrayListOf<Word>()
            for (elem in splitText) {
                formatElem = toRightFormat(elem)
                if (formatElem != "" && formatElem != currentWord) {
                    words.add(registerWord(text, formatElem))
                    currentWord = formatElem
                }
            }
            return words
        }

    }
}