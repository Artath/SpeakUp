package com.example.artem.speakup.MainPack

import com.arellomobile.mvp.InjectViewState
import com.arellomobile.mvp.MvpPresenter
import com.arellomobile.mvp.MvpView

@InjectViewState
class MainPresenter: MvpPresenter<MainPresenter.Interface>() {

    var vActiveTab: Int = 0

    fun updateTabs() {
        viewState.vUpdateTabs()
    }

    interface Interface : MvpView {
        fun vUpdateTabs()
    }
}

