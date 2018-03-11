package com.example.artem.speakup.TonguesTwisters

import com.arellomobile.mvp.InjectViewState
import com.arellomobile.mvp.MvpPresenter
import com.arellomobile.mvp.MvpView

@InjectViewState
class TGPresenter : MvpPresenter<TGPresenter.TGPresenterInterface>(){

    init {
        presenterLaunch()
    }

    fun presenterLaunch() {
        viewState.startSession()
    }

    interface TGPresenterInterface: MvpView {
        fun startSession()
    }
}