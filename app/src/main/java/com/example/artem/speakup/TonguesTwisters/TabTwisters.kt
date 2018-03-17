package com.example.artem.speakup.TonguesTwisters


import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.artem.speakup.R
import kotlinx.android.synthetic.main.tab_twisters.*
import java.util.*
import kotlin.collections.HashSet

class TabTwisters : Fragment() {


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? =
            inflater.inflate(R.layout.tab_twisters, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val arrayTwist = arrayListOf<TonguesTwister>()
        val selectedIdTG = arrayListOf<Long>()
        //test
        arrayTwist.add(TonguesTwister(1, "Пакет под попкорн", 0,0, false))
        arrayTwist.add(TonguesTwister(2, "Банкиров ребрендили-ребрендили-ребрендили, да не выребрендировали", 0,0, false))
        arrayTwist.add(TonguesTwister(3, "В Каннах львы только ленивым венки не вили", 0,0, false))
        arrayTwist.add(TonguesTwister(4, "В Кабардино-Балкарии валокордин из Болгарии", 0,0, false))
        arrayTwist.add(TonguesTwister(5, "Деидеологизировали-деидеологизировали, и додеидеологизировались", 0,0, false))
        arrayTwist.add(TonguesTwister(6, "Их пестициды не перепистицидят наши по своей пестицидности", 0,0, false))
        arrayTwist.add(TonguesTwister(7, "Их пестициды не перепистицидят наши по своей пестицидности", 0,0, false))
        arrayTwist.add(TonguesTwister(8, "Кокосовары варят в скорококосоварках кокосовый сок", 0,0, false))
        arrayTwist.add(TonguesTwister(9, "Работники предприятие приватизировали-приватизировали, да не выприватизировали", 0,0, false))
        arrayTwist.add(TonguesTwister(10, "Сиреневенькая зубовыковыривательница", 0,0, false))
        arrayTwist.add(TonguesTwister(11, "Флюорографист флюорографировал флюорографистку", 0,0, false))
        arrayTwist.add(TonguesTwister(12, "Я - вертикультяп. Могу вертикультяпнуться, могу вывертикультяпнутьс", 0,0, false))
        arrayTwist.add(TonguesTwister(13, "Стаффордширский терьер ретив, а черношерстный ризеншнауцер резв", 0,0, false))
        arrayTwist.add(TonguesTwister(14, "Это колониализм? - Нет, это не колониализм, а неоколониализм!", 0,0, false))
        arrayTwist.add(TonguesTwister(15, "Волховал волхв в хлеву с волхвами", 0,0, false))
        arrayTwist.add(TonguesTwister(16, "Волховал волхв в хлеву с волхвами", 0,0, false))
        arrayTwist.add(TonguesTwister(17, "Мы ели-ели ершей у ели. Их еле-еле у ели доели", 0,0, false))
        arrayTwist.add(TonguesTwister(18, "На дворе - трава, на траве - дрова. Не руби дрова на траве двора!", 0,0, false))

        val adapter = TGAdapter(arrayTwist, object : TGAdapter.TGAdapterCallBack{
            override fun multiSelect(id: Long) {
            }
        })
        tg_recycler.layoutManager = LinearLayoutManager(context)
        tg_recycler.adapter = adapter
        adapter.notifyDataSetChanged()

        tg_start_btn.setOnClickListener {
            val randomTG = HashSet<Int>()
            val rand = Random()
            while(randomTG.size < 5) {
                if (randomTG.add(rand.nextInt(arrayTwist.size))) {
                    selectedIdTG.add(arrayTwist[randomTG.size - 1].id)
                }
            }
            startActivity(Intent(context, TonguesTwistersActivity::class.java).putExtra(SELECTED_TONGUES_TWISTERS, selectedIdTG))
        }
    }

    companion object {
        val SELECTED_TONGUES_TWISTERS = "selectedTG"
        fun newInstance(): TabTwisters {
            val fragment = TabTwisters()
            val args = Bundle()

            fragment.arguments = args
            return fragment
        }
    }
}
