package com.homekode.android.tonguetwisters

import android.util.Log

class TongueTwisters {

    //пока дегенератский списочек
    val twist = arrayListOf<String>(
            "Пакет под попкорн",
            "Банкиров ребрендили-ребрендили-ребрендили, да не выребрендировали",
            "В Каннах львы только ленивым венки не вили",
            "В Кабардино-Балкарии валокордин из Болгарии",
            "Деидеологизировали-деидеологизировали, и додеидеологизировались",
            "Их пестициды не перепистицидят наши по своей пестицидности",
            "Кокосовары варят в скорококосоварках кокосовый сок",
            "Работники предприятие приватизировали-приватизировали, да не выприватизировали",
            "Сиреневенькая зубовыковыривательница",
            "Флюорографист флюорографировал флюорографистку",
            "Я - вертикультяп. Могу вертикультяпнуться, могу вывертикультяпнуться",
            "Стаффордширский терьер ретив, а черношерстный ризеншнауцер резв",
            "Это колониализм? - Нет, это не колониализм, а неоколониализм!",
            "Волховал волхв в хлеву с волхвами",
            "Мы ели-ели ершей у ели. Их еле-еле у ели доели",
            "На дворе - трава, на траве - дрова. Не руби дрова на траве двора!")


    fun compareTongueTwisters(tongueTwister: String, resRecognizer: String) =
            toRightFormat(tongueTwister) == toRightFormat(resRecognizer)
//tests
    companion object {
        val charFilter = arrayOf('.', ',', ':', '-', ' ', ';', '?', '!')
        fun toRightFormat(string: String): String {
            val str = string.toLowerCase().toCharArray()
            val res = StringBuffer("")
            (0 until str.size).filter { str[it] !in charFilter }.forEach { res.append(str[it]) }
            return res.toString()
        }
    }

}