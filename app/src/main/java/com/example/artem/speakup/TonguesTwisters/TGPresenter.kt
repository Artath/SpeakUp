package com.example.artem.speakup.TonguesTwisters

import com.arellomobile.mvp.InjectViewState
import com.arellomobile.mvp.MvpPresenter
import com.arellomobile.mvp.MvpView
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import java.util.*
import kotlin.collections.ArrayList

@InjectViewState
class TGPresenter : MvpPresenter<TGPresenter.TGPView>(){

    private val MAX_TG = 5
    private val CHILD = "tongueTwisters"
    private val arrayTG = arrayListOf<TonguesTwister>()
    private val selectedPosTG = arrayListOf<Int>()

    init {

        FirebaseDatabase.getInstance().reference.child(CHILD).addValueEventListener(object : ValueEventListener{

            override fun onCancelled(p0: DatabaseError?) {
                viewState.showMessage("Loading error")
            }

            override fun onDataChange(dataSnapshot: DataSnapshot?) {

                if (dataSnapshot != null) {
                    dataSnapshot
                            .children
                            .forEach { snapshot ->
                                arrayTG += TonguesTwister(snapshot.value!!.toString(), false)
                            }

                    viewState.showTonguesTwisters(TGAdapter(arrayTG, object : TGAdapter.TGAdapterCallBack{

                        override fun multiSelect(pos: Int, isSelected: Boolean) {
                            if (isSelected) {
                                selectedPosTG -= pos
                            } else {
                                selectedPosTG += pos
                            }
                            arrayTG[pos].isSelected = !isSelected
                        }
                    }))

                } else {
                    viewState.showMessage("Loading error")
                }
            }

        })

    }

    fun analyzeChoice() {
        val selectedTG = arrayListOf<String>()
        selectedTG += if (selectedPosTG.size == 0) {
            getRandomTG()
        } else {
            tonguesTwistersToStrings()
        }
        viewState.takeTG(selectedTG)
    }

    private fun getRandomTG(): ArrayList<String> {
        val arrTg = arrayListOf<String>()
        val randomTG = HashSet<Int>()
        val rand = Random()
        var numb = 0
        while (randomTG.size < MAX_TG) {
            numb = rand.nextInt(arrayTG.size)
            if (randomTG.add(numb)) {
                arrTg.add(arrayTG[numb].text)
            }
        }
        return  arrTg
    }

    private fun tonguesTwistersToStrings() : ArrayList<String> {
        val arrTg = arrayListOf<String>()
        selectedPosTG.forEach { elem -> arrTg += arrayTG[elem].text }
        return arrTg
    }
    interface TGPView: MvpView {
        fun showTonguesTwisters(adapter: TGAdapter)
        fun showMessage(mess: String)
        fun takeTG(arrTG: ArrayList<String>)
    }
}